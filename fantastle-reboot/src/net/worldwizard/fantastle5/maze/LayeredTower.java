/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package net.worldwizard.fantastle5.maze;

import java.io.IOException;

import com.puttysoftware.randomrange.RandomRange;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.battle.Battle;
import net.worldwizard.fantastle5.generic.MazeObject;
import net.worldwizard.fantastle5.generic.TypeConstants;
import net.worldwizard.fantastle5.objects.BarrierGenerator;
import net.worldwizard.fantastle5.objects.Empty;
import net.worldwizard.fantastle5.objects.IcedBarrierGenerator;
import net.worldwizard.fantastle5.objects.IcedMonster;
import net.worldwizard.fantastle5.objects.Monster;
import net.worldwizard.fantastle5.objects.MovingBlock;
import net.worldwizard.io.DataReader;
import net.worldwizard.io.DataWriter;

class LayeredTower {
    // Properties
    private MazeObject[][][][] towerData;
    private SavedTowerState savedTowerState;
    private int[] playerData;
    private int[] findResult;
    private int visionRadius;
    private boolean horizontalWraparoundEnabled;
    private boolean verticalWraparoundEnabled;
    private boolean thirdDimensionWraparoundEnabled;
    private static final int MAX_VISION_RADIUS = 6;
    private static final int MIN_VISION_RADIUS = 1;

    // Constructors
    public LayeredTower() {
        this.horizontalWraparoundEnabled = false;
        this.verticalWraparoundEnabled = false;
        this.thirdDimensionWraparoundEnabled = false;
    }

    public LayeredTower(final int rows, final int cols, final int floors) {
        this.towerData = new MazeObject[cols][rows][floors][Maze.LAYER_COUNT];
        this.savedTowerState = new SavedTowerState(rows, cols, floors);
        this.playerData = new int[3];
        this.findResult = new int[3];
        this.setVisionRadiusToMaximum();
        this.horizontalWraparoundEnabled = false;
        this.verticalWraparoundEnabled = false;
        this.thirdDimensionWraparoundEnabled = false;
    }

    // Methods
    @Override
    public LayeredTower clone() {
        final LayeredTower copy = new LayeredTower(this.getRows(),
                this.getColumns(), this.getFloors());
        int x, y, z, e;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    for (e = 0; e < Maze.LAYER_COUNT; e++) {
                        copy.towerData[x][y][z][e] = this.towerData[x][y][z][e]
                                .clone();
                    }
                }
            }
        }
        copy.savedTowerState = this.savedTowerState.clone();
        System.arraycopy(this.playerData, 0, copy.playerData, 0,
                this.playerData.length);
        System.arraycopy(this.findResult, 0, copy.findResult, 0,
                this.findResult.length);
        copy.visionRadius = this.visionRadius;
        copy.horizontalWraparoundEnabled = this.horizontalWraparoundEnabled;
        copy.verticalWraparoundEnabled = this.verticalWraparoundEnabled;
        copy.thirdDimensionWraparoundEnabled = this.thirdDimensionWraparoundEnabled;
        return copy;
    }

    public MazeObject getCell(final int row, final int col, final int floor,
            final int extra) {
        int fR = row;
        int fC = col;
        int fF = floor;
        if (this.verticalWraparoundEnabled) {
            fC = this.normalizeColumn(fC);
        }
        if (this.horizontalWraparoundEnabled) {
            fR = this.normalizeRow(fR);
        }
        if (this.thirdDimensionWraparoundEnabled) {
            fF = this.normalizeFloor(fF);
        }
        return this.towerData[fC][fR][fF][extra];
    }

    public int getFindResultRow() {
        return this.findResult[1];
    }

    public int getFindResultColumn() {
        return this.findResult[0];
    }

    public int getFindResultFloor() {
        return this.findResult[2];
    }

    public int getStartRow() {
        return this.playerData[1];
    }

    public int getStartColumn() {
        return this.playerData[0];
    }

    public int getStartFloor() {
        return this.playerData[2];
    }

    public int getRows() {
        return this.towerData[0].length;
    }

    public int getColumns() {
        return this.towerData.length;
    }

    public int getFloors() {
        return this.towerData[0][0].length;
    }

    public int getVisionRadius() {
        return this.visionRadius;
    }

    public void findStart() {
        int x, y, z;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    final MazeObject mo = this.towerData[x][y][z][Maze.LAYER_OBJECT];
                    if (mo != null) {
                        if (mo.getName().equals("Player")) {
                            this.playerData[1] = x;
                            this.playerData[0] = y;
                            this.playerData[2] = z;
                        }
                    }
                }
            }
        }
    }

    public boolean findPlayer() {
        int x, y, z;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    final MazeObject mo = this.towerData[x][y][z][Maze.LAYER_OBJECT];
                    if (mo != null) {
                        if (mo.getName().equals("Player")) {
                            this.findResult[1] = x;
                            this.findResult[0] = y;
                            this.findResult[2] = z;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean findNthMazeObject(final MazeObject obj, final int N) {
        int x, y, z, found;
        found = 0;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    final MazeObject mo = this.towerData[x][y][z][Maze.LAYER_OBJECT];
                    if (mo != null) {
                        if (mo.getName().equals(obj.getName())) {
                            found++;
                            if (found == N) {
                                this.findResult[1] = x;
                                this.findResult[0] = y;
                                this.findResult[2] = z;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void findAllObjectPairsAndSwap(final MazeObject o1,
            final MazeObject o2) {
        int x, y, z;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    final MazeObject mo = this.towerData[x][y][z][Maze.LAYER_OBJECT];
                    if (mo != null) {
                        if (mo.getName().equals(o1.getName())) {
                            this.towerData[x][y][z][Maze.LAYER_OBJECT] = o2;
                        } else if (mo.getName().equals(o2.getName())) {
                            this.towerData[x][y][z][Maze.LAYER_OBJECT] = o1;
                        }
                    }
                }
            }
        }
    }

    public void findAllMatchingObjectsAndDecay(final MazeObject o) {
        int x, y, z;
        final MazeObject decayTo = new Empty();
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    final MazeObject mo = this.towerData[x][y][z][Maze.LAYER_OBJECT];
                    if (mo != null) {
                        if (mo.getName().equals(o.getName())) {
                            this.towerData[x][y][z][Maze.LAYER_OBJECT] = decayTo;
                        }
                    }
                }
            }
        }
    }

    public void masterTrapTrigger() {
        int x, y, z;
        final MazeObject decayTo = new Empty();
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    final MazeObject mo = this.towerData[x][y][z][Maze.LAYER_OBJECT];
                    if (mo != null) {
                        if (mo.isOfType(TypeConstants.TYPE_WALL_TRAP) || mo
                                .isOfType(TypeConstants.TYPE_TRAPPED_WALL)) {
                            this.towerData[x][y][z][Maze.LAYER_OBJECT] = decayTo;
                        }
                    }
                }
            }
        }
    }

    public void tickTimers(final int floor) {
        int x, y;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                final MazeObject mo = this.towerData[x][y][floor][Maze.LAYER_OBJECT];
                if (mo != null) {
                    mo.tickTimer(y, x);
                }
            }
        }
    }

    public boolean rotateRadiusClockwise(final int x, final int y, final int z,
            final int r) {
        int u, v, l;
        u = v = l = 0;
        int uFix, vFix, uRot, vRot, uAdj, vAdj;
        uFix = vFix = uRot = vRot = uAdj = vAdj = 0;
        final int cosineTheta = 0;
        final int sineTheta = 1;
        final MazeObject[][][] tempStorage = new MazeObject[2 * r + 1][2 * r
                + 1][Maze.LAYER_COUNT];
        try {
            for (u = x - r; u <= x + r; u++) {
                for (v = y - r; v <= y + r; v++) {
                    for (l = 0; l < Maze.LAYER_COUNT; l++) {
                        uFix = u - x;
                        vFix = v - y;
                        uRot = uFix * cosineTheta - vFix * sineTheta;
                        vRot = uFix * sineTheta + vFix * cosineTheta;
                        uAdj = uRot + r;
                        vAdj = vRot + r;
                        tempStorage[uAdj][vAdj][l] = this.getCell(u, v, z, l);
                    }
                }
            }
            for (u = x - r; u <= x + r; u++) {
                for (v = y - r; v <= y + r; v++) {
                    for (l = 0; l < Maze.LAYER_COUNT; l++) {
                        uFix = u - (x - r);
                        vFix = v - (y - r);
                        this.setCell(tempStorage[uFix][vFix][l], u, v, z, l);
                    }
                }
            }
            return true;
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            return false;
        }
    }

    public boolean rotateRadiusCounterclockwise(final int x, final int y,
            final int z, final int r) {
        int u, v, l;
        u = v = l = 0;
        int uFix, vFix, uRot, vRot, uAdj, vAdj;
        uFix = vFix = uRot = vRot = uAdj = vAdj = 0;
        final int cosineTheta = 0;
        final int sineTheta = 1;
        final MazeObject[][][] tempStorage = new MazeObject[2 * r + 1][2 * r
                + 1][Maze.LAYER_COUNT];
        try {
            for (u = x - r; u <= x + r; u++) {
                for (v = y - r; v <= y + r; v++) {
                    for (l = 0; l < Maze.LAYER_COUNT; l++) {
                        uFix = u - x;
                        vFix = v - y;
                        uRot = uFix * cosineTheta + vFix * sineTheta;
                        vRot = -uFix * sineTheta + vFix * cosineTheta;
                        uAdj = uRot + r;
                        vAdj = vRot + r;
                        tempStorage[uAdj][vAdj][l] = this.getCell(u, v, z, l);
                    }
                }
            }
            for (u = x - r; u <= x + r; u++) {
                for (v = y - r; v <= y + r; v++) {
                    for (l = 0; l < Maze.LAYER_COUNT; l++) {
                        uFix = u - (x - r);
                        vFix = v - (y - r);
                        this.setCell(tempStorage[uFix][vFix][l], u, v, z, l);
                    }
                }
            }
            return true;
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            return false;
        }
    }

    public void resize(final int x, final int y, final int z) {
        int fX = x;
        int fY = y;
        int fZ = z;
        // Adjust size parameters
        if (fX == Maze.SIZE_CURRENT) {
            fX = this.getRows();
        }
        if (fY == Maze.SIZE_CURRENT) {
            fY = this.getColumns();
        }
        if (fZ == Maze.SIZE_CURRENT) {
            fZ = this.getFloors();
        }
        // Allocate temporary storage array
        final MazeObject[][][][] tempStorage = new MazeObject[fY][fX][fZ][Maze.LAYER_COUNT];
        // Copy existing maze into temporary array
        int u, v, w, e;
        for (u = 0; u < fY; u++) {
            for (v = 0; v < fX; v++) {
                for (w = 0; w < fZ; w++) {
                    for (e = 0; e < Maze.LAYER_COUNT; e++) {
                        try {
                            tempStorage[u][v][w][e] = this.towerData[u][v][w][e];
                        } catch (final ArrayIndexOutOfBoundsException aioob) {
                            // Do nothing
                        }
                    }
                }
            }
        }
        // Set the current data to the temporary array
        this.towerData = tempStorage;
        // Fill any blanks
        this.fillNulls();
        // Recreate saved tower state
        this.savedTowerState = new SavedTowerState(fX, fY, fZ);
    }

    public boolean radialScan(final int x, final int y, final int z,
            final int l, final int r, final String targetName) {
        int u, v;
        u = v = 0;
        // Perform the scan
        for (u = x - r; u <= x + r; u++) {
            for (v = y - r; v <= y + r; v++) {
                try {
                    final String testName = this.getCell(u, v, z, l).getName();
                    if (testName.equals(targetName)) {
                        return true;
                    }
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Do nothing
                }
            }
        }
        return false;
    }

    public void radialScanTimerAction(final int x, final int y, final int z,
            final int l, final int r, final String targetName,
            final int timerMod) {
        int u, v;
        u = v = 0;
        // Perform the scan, and do the action
        for (u = x - r; u <= x + r; u++) {
            for (v = y - r; v <= y + r; v++) {
                try {
                    final String testName = this.getCell(u, v, z, l).getName();
                    if (testName.equals(targetName)) {
                        this.getCell(u, v, z, l).extendTimer(timerMod);
                    }
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Do nothing
                }
            }
        }
    }

    public void radialScanKillMonsters(final int x, final int y, final int z,
            final int l, final int r) {
        int u, v;
        u = v = 0;
        // Perform the scan, and do the action
        for (u = x - r; u <= x + r; u++) {
            for (v = y - r; v <= y + r; v++) {
                try {
                    final String testName = this.getCell(u, v, z, l).getName();
                    if (testName.equals("Monster")) {
                        // Kill the monster
                        final Monster m = (Monster) this.getCell(u, v, z, l);
                        this.setCell(m.getSavedObject(), u, v, z, l);
                        // Reward player for monster death
                        Fantastle5.getApplication().getBattle()
                                .doBattleByProxy();
                        // Respawn it
                        this.generateOneMonster();
                    }
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Do nothing
                }
            }
        }
    }

    public void radialScanFreezeObjects(final int x, final int y, final int z,
            final int l, final int r) {
        int u, v;
        u = v = 0;
        // Perform the scan, and do the action
        for (u = x - r; u <= x + r; u++) {
            for (v = y - r; v <= y + r; v++) {
                try {
                    final boolean reactsToIce = this.getCell(u, v, z, l)
                            .isOfType(TypeConstants.TYPE_REACTS_TO_ICE);
                    if (reactsToIce) {
                        final MazeObject there = this.getCell(u, v, z, l);
                        if (there.getClass() == Monster.class) {
                            final Monster m = (Monster) there;
                            // Freeze the monster
                            this.setCell(new IcedMonster(m.getSavedObject()), u,
                                    v, z, l);
                        } else if (there.getClass() == BarrierGenerator.class) {
                            // Freeze the generator
                            this.setCell(new IcedBarrierGenerator(), u, v, z,
                                    l);
                        } else {
                            // Assume object is already iced, and extend its
                            // timer
                            there.extendTimerByInitialValue();
                        }
                    }
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Do nothing
                }
            }
        }
    }

    public void radialScanWarpObjects(final int x, final int y, final int z,
            final int l, final int r) {
        int u, v;
        u = v = 0;
        // Perform the scan, and do the action
        for (u = x - r; u <= x + r; u++) {
            for (v = y - r; v <= y + r; v++) {
                try {
                    // If the object here isn't an empty apsce
                    if (!this.getCell(u, v, z, l).getName().equals("Empty")) {
                        // Warp the object
                        this.warpObject(this.getCell(u, v, z, l), u, v, z, l);
                    }
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Do nothing
                }
            }
        }
    }

    public void radialScanShuffleObjects(final int x, final int y, final int z,
            final int r) {
        int u, v, l, uFix, vFix;
        final MazeObject[][][] preShuffle = new MazeObject[2 * r + 1][2 * r
                + 1][Maze.LAYER_COUNT];
        // Load the preShuffle array
        for (u = x - r; u <= x + r; u++) {
            for (v = y - r; v <= y + r; v++) {
                for (l = 0; l < Maze.LAYER_COUNT; l++) {
                    uFix = u - (x - r);
                    vFix = v - (y - r);
                    try {
                        preShuffle[uFix][vFix][l] = this.getCell(u, v, z, l);
                    } catch (final ArrayIndexOutOfBoundsException aioob) {
                        preShuffle[uFix][vFix][l] = null;
                    }
                }
            }
        }
        // Do the shuffle
        final MazeObject[][][] postShuffle = LayeredTower
                .shuffleObjects(preShuffle, r);
        // Load the maze with the postShuffle array
        for (u = x - r; u <= x + r; u++) {
            for (v = y - r; v <= y + r; v++) {
                for (l = 0; l < Maze.LAYER_COUNT; l++) {
                    uFix = u - (x - r);
                    vFix = v - (y - r);
                    if (postShuffle[uFix][vFix][l] != null) {
                        this.setCell(postShuffle[uFix][vFix][l], u, v, z, l);
                    }
                }
            }
        }
    }

    public boolean isSquareVisible(final int x1, final int y1, final int x2,
            final int y2) {
        boolean result = true;
        final int xDist = this.pointDistance(x1, x2, 1);
        final int yDist = this.pointDistance(y1, y2, 2);
        if (xDist <= this.visionRadius && yDist <= this.visionRadius) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private int pointDistance(final int u, final int v, final int w) {
        if (w == 1) {
            if (this.horizontalWraparoundEnabled) {
                final int max = this.getRows();
                return Math.min(Math.abs(u - v), max - Math.abs(u - v));
            } else {
                return Math.abs(u - v);
            }
        } else if (w == 2) {
            if (this.verticalWraparoundEnabled) {
                final int max = this.getColumns();
                return Math.min(Math.abs(u - v), max - Math.abs(u - v));
            } else {
                return Math.abs(u - v);
            }
        } else if (w == 3) {
            if (this.thirdDimensionWraparoundEnabled) {
                final int max = this.getFloors();
                return Math.min(Math.abs(u - v), max - Math.abs(u - v));
            } else {
                return Math.abs(u - v);
            }
        } else {
            return Math.abs(u - v);
        }
    }

    public void setCell(final MazeObject mo, final int row, final int col,
            final int floor, final int extra) {
        int fR = row;
        int fC = col;
        int fF = floor;
        if (this.verticalWraparoundEnabled) {
            fC = this.normalizeColumn(fC);
        }
        if (this.horizontalWraparoundEnabled) {
            fR = this.normalizeRow(fR);
        }
        if (this.thirdDimensionWraparoundEnabled) {
            fF = this.normalizeFloor(fF);
        }
        this.towerData[fC][fR][fF][extra] = mo;
    }

    public void setStartRow(final int newStartRow) {
        this.playerData[1] = newStartRow;
    }

    public void setStartColumn(final int newStartColumn) {
        this.playerData[0] = newStartColumn;
    }

    public void setStartFloor(final int newStartFloor) {
        this.playerData[2] = newStartFloor;
    }

    public void setVisionRadiusToMaximum() {
        this.visionRadius = LayeredTower.MAX_VISION_RADIUS;
    }

    public void setVisionRadiusToMinimum() {
        this.visionRadius = LayeredTower.MIN_VISION_RADIUS;
    }

    public void incrementVisionRadius() {
        if (this.visionRadius < LayeredTower.MAX_VISION_RADIUS) {
            this.visionRadius++;
        }
    }

    public void decrementVisionRadius() {
        if (this.visionRadius > LayeredTower.MIN_VISION_RADIUS) {
            this.visionRadius--;
        }
    }

    public void fill(final MazeObject bottom, final MazeObject top) {
        int x, y, z, e;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    for (e = 0; e < Maze.LAYER_COUNT; e++) {
                        if (e == Maze.LAYER_GROUND) {
                            this.towerData[x][y][z][e] = bottom;
                        } else {
                            this.towerData[x][y][z][e] = top;
                        }
                    }
                }
            }
        }
    }

    private void fillNulls() {
        final MazeObject bottom = Fantastle5.getApplication().getPrefsManager()
                .getEditorDefaultFill();
        final MazeObject top = new Empty();
        int x, y, z, e;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    for (e = 0; e < Maze.LAYER_COUNT; e++) {
                        if (this.towerData[x][y][z][e] == null) {
                            if (e == Maze.LAYER_GROUND) {
                                this.towerData[x][y][z][e] = bottom;
                            } else {
                                this.towerData[x][y][z][e] = top;
                            }
                        }
                    }
                }
            }
        }
    }

    public void save() {
        int x, y, z, e;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    for (e = 0; e < Maze.LAYER_COUNT; e++) {
                        this.savedTowerState.setDataCell(
                                this.towerData[x][y][z][e], x, y, z, e);
                    }
                }
            }
        }
    }

    public void restore() {
        int x, y, z, e;
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    for (e = 0; e < Maze.LAYER_COUNT; e++) {
                        this.towerData[x][y][z][e] = this.savedTowerState
                                .getDataCell(x, y, z, e);
                    }
                }
            }
        }
    }

    public void updateMonsterPosition(final int move, final int xLoc,
            final int yLoc, final Monster monster) {
        final int[] dirMove = MazeObject.unresolveRelativeDirection(move);
        final int zLoc = Fantastle5.getApplication().getGameManager()
                .getPlayerManager().getPlayerLocationZ();
        try {
            final MazeObject there = this.getCell(xLoc + dirMove[0],
                    yLoc + dirMove[1], zLoc, Maze.LAYER_OBJECT);
            final MazeObject ground = this.getCell(xLoc + dirMove[0],
                    yLoc + dirMove[1], zLoc, Maze.LAYER_GROUND);
            if (!there.isSolid() && !there.getName().equals("Monster")) {
                if (there.getName().equals("Player")) {
                    if (!Battle.isInBattle()) {
                        Fantastle5.getApplication().getBattle().doBattle();
                        this.postBattle(monster, xLoc, yLoc, false);
                    }
                } else {
                    // Move the monster
                    this.setCell(monster.getSavedObject(), xLoc, yLoc, zLoc,
                            Maze.LAYER_OBJECT);
                    monster.setSavedObject(there);
                    this.setCell(monster, xLoc + dirMove[0], yLoc + dirMove[1],
                            zLoc, Maze.LAYER_OBJECT);
                    // Does the ground have friction?
                    if (!ground.hasFriction()) {
                        // No - move the monster again
                        this.updateMonsterPosition(move, xLoc + dirMove[0],
                                yLoc + dirMove[1], monster);
                    }
                }
            }
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Do nothing
        }
    }

    public void updateMovingBlockPosition(final int move, final int xLoc,
            final int yLoc, final MovingBlock block) {
        final int[] dirMove = MazeObject.unresolveRelativeDirection(move);
        final int zLoc = Fantastle5.getApplication().getGameManager()
                .getPlayerManager().getPlayerLocationZ();
        try {
            final MazeObject there = this.getCell(xLoc + dirMove[0],
                    yLoc + dirMove[1], zLoc, Maze.LAYER_OBJECT);
            final MazeObject ground = this.getCell(xLoc + dirMove[0],
                    yLoc + dirMove[1], zLoc, Maze.LAYER_GROUND);
            if (!there.isSolid() && !there.getName().equals("Player")) {
                this.setCell(block.getSavedObject(), xLoc, yLoc, zLoc,
                        Maze.LAYER_OBJECT);
                // Move the block
                block.setSavedObject(there);
                this.setCell(block, xLoc + dirMove[0], yLoc + dirMove[1], zLoc,
                        Maze.LAYER_OBJECT);
                // Does the ground have friction?
                if (!ground.hasFriction()) {
                    // No - move the block again
                    this.updateMovingBlockPosition(move, xLoc + dirMove[0],
                            yLoc + dirMove[1], block);
                }
            }
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Do nothing
        }
    }

    public void postBattle(final Monster m, final int xLoc, final int yLoc,
            final boolean player) {
        final MazeObject saved = m.getSavedObject();
        final int zLoc = Fantastle5.getApplication().getGameManager()
                .getPlayerManager().getPlayerLocationZ();
        if (player) {
            Fantastle5.getApplication().getGameManager()
                    .setSavedMazeObject(saved);
        } else {
            this.setCell(saved, xLoc, yLoc, zLoc, Maze.LAYER_OBJECT);
        }
        this.generateOneMonster();
    }

    public void generateOneMonster() {
        final RandomRange row = new RandomRange(0, this.getRows() - 1);
        final RandomRange column = new RandomRange(0, this.getColumns() - 1);
        final int zLoc = Fantastle5.getApplication().getGameManager()
                .getPlayerManager().getPlayerLocationZ();
        int randomRow, randomColumn;
        randomRow = row.generate();
        randomColumn = column.generate();
        MazeObject currObj = this.getCell(randomRow, randomColumn, zLoc,
                Maze.LAYER_OBJECT);
        if (!currObj.isSolid()) {
            final Monster m = new Monster();
            m.setSavedObject(currObj);
            this.setCell(m, randomRow, randomColumn, zLoc, Maze.LAYER_OBJECT);
        } else {
            while (currObj.isSolid()) {
                randomRow = row.generate();
                randomColumn = column.generate();
                currObj = this.getCell(randomRow, randomColumn, zLoc,
                        Maze.LAYER_OBJECT);
            }
            final Monster m = new Monster();
            m.setSavedObject(currObj);
            this.setCell(m, randomRow, randomColumn, zLoc, Maze.LAYER_OBJECT);
        }
    }

    public void warpObject(final MazeObject mo, final int x, final int y,
            final int z, final int l) {
        final RandomRange row = new RandomRange(0, this.getRows() - 1);
        final RandomRange column = new RandomRange(0, this.getColumns() - 1);
        int randomRow, randomColumn;
        randomRow = row.generate();
        randomColumn = column.generate();
        MazeObject currObj = this.getCell(randomRow, randomColumn, z,
                Maze.LAYER_OBJECT);
        if (!currObj.isSolid()) {
            this.setCell(new Empty(), x, y, z, l);
            this.setCell(mo, randomRow, randomColumn, z, Maze.LAYER_OBJECT);
        } else {
            while (currObj.isSolid()) {
                randomRow = row.generate();
                randomColumn = column.generate();
                currObj = this.getCell(randomRow, randomColumn, z,
                        Maze.LAYER_OBJECT);
            }
            this.setCell(new Empty(), x, y, z, l);
            this.setCell(mo, randomRow, randomColumn, z, Maze.LAYER_OBJECT);
        }
    }

    private static MazeObject[][][] shuffleObjects(
            final MazeObject[][][] preShuffle, final int r) {
        final MazeObject[][][] postShuffle = new MazeObject[2 * r + 1][2 * r
                + 1][Maze.LAYER_COUNT];
        int[][] randomLocations = new int[(2 * r + 1) * (2 * r + 1)][2];
        // Populate randomLocations array
        int counter = 0;
        for (int x = 0; x < preShuffle.length; x++) {
            for (int y = 0; y < preShuffle[x].length; y++) {
                if (preShuffle[x][y] == null) {
                    randomLocations[counter][0] = -1;
                    randomLocations[counter][1] = -1;
                } else {
                    randomLocations[counter][0] = x;
                    randomLocations[counter][1] = y;
                }
                counter++;
            }
        }
        // Shuffle locations array
        randomLocations = LayeredTower.shuffleArray(randomLocations);
        // Populate postShuffle array
        counter = 0;
        for (int x = 0; x < preShuffle.length; x++) {
            for (int y = 0; y < preShuffle[x].length; y++) {
                for (int z = 0; z < Maze.LAYER_COUNT; z++) {
                    if (preShuffle[x][y][z] == null) {
                        postShuffle[x][y][z] = null;
                    } else {
                        postShuffle[x][y][z] = preShuffle[randomLocations[counter][0]][randomLocations[counter][1]][z];
                    }
                }
                counter++;
            }
        }
        return postShuffle;
    }

    private static int[][] shuffleArray(final int[][] src) {
        int temp = 0;
        final int minSwaps = (int) Math.sqrt(src.length);
        final int maxSwaps = src.length - 1;
        int oldLoc, newLoc;
        final RandomRange rSwap = new RandomRange(minSwaps, maxSwaps);
        final RandomRange locSwap = new RandomRange(0, src.length - 1);
        final int swaps = rSwap.generate();
        for (int s = 0; s < swaps; s++) {
            do {
                oldLoc = locSwap.generate();
                newLoc = locSwap.generate();
            } while (src[oldLoc][0] == -1 || src[newLoc][0] == -1);
            for (int w = 0; w < src[0].length; w++) {
                // Swap
                temp = src[newLoc][w];
                src[newLoc][w] = src[oldLoc][w];
                src[oldLoc][w] = temp;
            }
        }
        return src;
    }

    private int normalizeRow(final int row) {
        int fR = row;
        if (fR < 0) {
            fR += this.getRows();
            while (fR < 0) {
                fR += this.getRows();
            }
        } else if (fR > this.getRows() - 1) {
            fR -= this.getRows();
            while (fR > this.getRows() - 1) {
                fR -= this.getRows();
            }
        }
        return fR;
    }

    private int normalizeColumn(final int column) {
        int fC = column;
        if (fC < 0) {
            fC += this.getColumns();
            while (fC < 0) {
                fC += this.getColumns();
            }
        } else if (fC > this.getColumns() - 1) {
            fC -= this.getColumns();
            while (fC > this.getColumns() - 1) {
                fC -= this.getColumns();
            }
        }
        return fC;
    }

    private int normalizeFloor(final int floor) {
        int fF = floor;
        if (fF < 0) {
            fF += this.getFloors();
            while (fF < 0) {
                fF += this.getFloors();
            }
        } else if (fF > this.getFloors() - 1) {
            fF -= this.getFloors();
            while (fF > this.getFloors() - 1) {
                fF -= this.getFloors();
            }
        }
        return fF;
    }

    public void enableHorizontalWraparound() {
        this.horizontalWraparoundEnabled = true;
    }

    public void disableHorizontalWraparound() {
        this.horizontalWraparoundEnabled = false;
    }

    public void enableVerticalWraparound() {
        this.verticalWraparoundEnabled = true;
    }

    public void disableVerticalWraparound() {
        this.verticalWraparoundEnabled = false;
    }

    public void enable3rdDimensionWraparound() {
        this.thirdDimensionWraparoundEnabled = true;
    }

    public void disable3rdDimensionWraparound() {
        this.thirdDimensionWraparoundEnabled = false;
    }

    public boolean isHorizontalWraparoundEnabled() {
        return this.horizontalWraparoundEnabled;
    }

    public boolean isVerticalWraparoundEnabled() {
        return this.verticalWraparoundEnabled;
    }

    public boolean is3rdDimensionWraparoundEnabled() {
        return this.thirdDimensionWraparoundEnabled;
    }

    public void writeLayeredTower(final DataWriter writer) throws IOException {
        int x, y, z, e;
        writer.writeInt(this.getColumns());
        writer.writeInt(this.getRows());
        writer.writeInt(this.getFloors());
        for (x = 0; x < this.getColumns(); x++) {
            for (y = 0; y < this.getRows(); y++) {
                for (z = 0; z < this.getFloors(); z++) {
                    for (e = 0; e < Maze.LAYER_COUNT; e++) {
                        this.towerData[x][y][z][e].writeMazeObject(writer);
                    }
                }
            }
        }
        for (y = 0; y < 3; y++) {
            writer.writeInt(this.playerData[y]);
        }
        // New in V4.00
        writer.writeBoolean(this.horizontalWraparoundEnabled);
        writer.writeBoolean(this.verticalWraparoundEnabled);
        writer.writeBoolean(this.thirdDimensionWraparoundEnabled);
    }

    public static LayeredTower readLayeredTower(final DataReader reader,
            final int formatVersion) throws IOException {
        int x, y, z, e, mazeSizeX, mazeSizeY, mazeSizeZ;
        mazeSizeX = reader.readInt();
        mazeSizeY = reader.readInt();
        mazeSizeZ = reader.readInt();
        final LayeredTower lt = new LayeredTower(mazeSizeX, mazeSizeY,
                mazeSizeZ);
        for (x = 0; x < lt.getColumns(); x++) {
            for (y = 0; y < lt.getRows(); y++) {
                for (z = 0; z < lt.getFloors(); z++) {
                    for (e = 0; e < Maze.LAYER_COUNT; e++) {
                        lt.towerData[x][y][z][e] = Fantastle5.getApplication()
                                .getObjects()
                                .readMazeObject(reader, formatVersion);
                        if (lt.towerData[x][y][z][e] == null) {
                            return null;
                        }
                    }
                }
            }
        }
        for (y = 0; y < 3; y++) {
            lt.playerData[y] = reader.readInt();
        }
        // New in V5.00
        if (formatVersion == FormatConstants.MAZE_FORMAT_4
                || formatVersion == FormatConstants.MAZE_FORMAT_5) {
            lt.horizontalWraparoundEnabled = reader.readBoolean();
            lt.verticalWraparoundEnabled = reader.readBoolean();
            lt.thirdDimensionWraparoundEnabled = reader.readBoolean();
        } else if (formatVersion == FormatConstants.MAZE_FORMAT_3) {
            lt.horizontalWraparoundEnabled = false;
            lt.verticalWraparoundEnabled = false;
            lt.thirdDimensionWraparoundEnabled = false;
        }
        return lt;
    }

    public void writeSavedTowerState(final DataWriter writer)
            throws IOException {
        this.savedTowerState.writeSavedTowerState(writer);
    }

    public void readSavedTowerState(final DataReader reader,
            final int formatVersion) throws IOException {
        this.savedTowerState = SavedTowerState.readSavedTowerState(reader,
                formatVersion);
    }
}
