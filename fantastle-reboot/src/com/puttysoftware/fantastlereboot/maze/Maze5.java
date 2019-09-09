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
package com.puttysoftware.fantastlereboot.maze;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.generic.MazeObject;
import com.puttysoftware.fantastlereboot.objects.Monster;
import com.puttysoftware.fantastlereboot.objects.MovingBlock;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class Maze5 implements Maze {
    // Properties
    private final LayeredTower[] mazeData;
    private LayeredTower clipboard;
    private int startW;
    private int resultLevel;
    private int levelCount;
    private static final int MIN_LEVELS = 1;
    private static final int MAX_LEVELS = 8;

    // Constructors
    public Maze5() {
        this.mazeData = new LayeredTower[Maze5.MAX_LEVELS];
        this.clipboard = null;
        this.levelCount = 0;
        this.startW = 0;
    }

    // Methods
    @Override
    public int getMinLevels() {
        return Maze5.MIN_LEVELS;
    }

    @Override
    public int getMaxLevels() {
        return Maze5.MAX_LEVELS;
    }

    @Override
    public void cutLevel(final int level) {
        if (this.levelCount > 1) {
            this.clipboard = this.mazeData[level];
            this.removeLevel(level + 1);
        }
    }

    @Override
    public void copyLevel(final int level) {
        this.clipboard = this.mazeData[level].clone();
    }

    @Override
    public void pasteLevel(final int level) {
        if (this.clipboard != null) {
            this.mazeData[level] = this.clipboard.clone();
            FantastleReboot.getBagOStuff().getMazeManager().setDirty(true);
        }
    }

    @Override
    public boolean isPasteBlocked() {
        return this.clipboard == null;
    }

    @Override
    public boolean isCutBlocked() {
        return this.levelCount <= 1;
    }

    @Override
    public boolean insertLevelFromClipboard() {
        if (this.levelCount < Maze5.MAX_LEVELS) {
            this.mazeData[this.levelCount] = this.clipboard.clone();
            this.levelCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addLevel(final int rows, final int cols, final int floors) {
        if (this.levelCount < Maze5.MAX_LEVELS) {
            this.mazeData[this.levelCount] = new LayeredTower(rows, cols,
                    floors);
            this.levelCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeLevel(final int level) {
        int fL = level;
        if (this.levelCount > 1) {
            if (fL >= 1 && fL <= this.levelCount) {
                fL--;
                this.mazeData[fL] = null;
                // Shift all higher-numbered levels down
                for (int x = fL; x < this.levelCount; x++) {
                    this.mazeData[x] = this.mazeData[x + 1];
                }
                this.levelCount--;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public MazeObject getCell(final int row, final int col, final int floor,
            final int level, final int extra) {
        return this.mazeData[level].getCell(row, col, floor, extra);
    }

    @Override
    public int getFindResultRow(final int level) {
        return this.mazeData[level].getFindResultRow();
    }

    @Override
    public int getFindResultColumn(final int level) {
        return this.mazeData[level].getFindResultColumn();
    }

    @Override
    public int getFindResultFloor(final int level) {
        return this.mazeData[level].getFindResultFloor();
    }

    @Override
    public int getFindResultLevel() {
        return this.resultLevel;
    }

    @Override
    public int getStartRow(final int level) {
        return this.mazeData[level].getStartRow();
    }

    @Override
    public int getStartColumn(final int level) {
        return this.mazeData[level].getStartColumn();
    }

    @Override
    public int getStartFloor(final int level) {
        return this.mazeData[level].getStartFloor();
    }

    @Override
    public int getStartLevel() {
        return this.startW;
    }

    @Override
    public int getRows(final int level) {
        return this.mazeData[level].getRows();
    }

    @Override
    public int getColumns(final int level) {
        return this.mazeData[level].getColumns();
    }

    @Override
    public int getFloors(final int level) {
        return this.mazeData[level].getFloors();
    }

    @Override
    public int getRows() {
        return this.mazeData[0].getRows();
    }

    @Override
    public int getColumns() {
        return this.mazeData[0].getColumns();
    }

    @Override
    public int getFloors() {
        return this.mazeData[0].getFloors();
    }

    @Override
    public int getLevels() {
        return this.levelCount;
    }

    @Override
    public int getVisionRadius(final int level) {
        return this.mazeData[level].getVisionRadius();
    }

    @Override
    public void findAllStarts() {
        int x;
        for (x = 0; x < this.getLevels(); x++) {
            this.mazeData[x].findStart();
        }
    }

    @Override
    public boolean findPlayerOnLevel(final int level) {
        if (level > this.levelCount) {
            return false;
        } else {
            return this.mazeData[level].findPlayer();
        }
    }

    @Override
    public boolean findNthMazeObjectOnLevel(final int level,
            final MazeObject obj, final int N) {
        if (level > this.levelCount) {
            return false;
        } else {
            return this.mazeData[level].findNthMazeObject(obj, N);
        }
    }

    @Override
    public void findAllObjectPairsAndSwap(final int level, final MazeObject o1,
            final MazeObject o2) {
        this.mazeData[level].findAllObjectPairsAndSwap(o1, o2);
    }

    @Override
    public void findAllMatchingObjectsAndDecay(final int level,
            final MazeObject o) {
        this.mazeData[level].findAllMatchingObjectsAndDecay(o);
    }

    @Override
    public void masterTrapTrigger(final int level) {
        this.mazeData[level].masterTrapTrigger();
    }

    @Override
    public void tickTimers(final int level, final int floor) {
        this.mazeData[level].tickTimers(floor);
    }

    @Override
    public boolean rotateRadiusClockwise(final int x, final int y, final int z,
            final int w, final int r) {
        return this.mazeData[w].rotateRadiusClockwise(x, y, z, r);
    }

    @Override
    public boolean rotateRadiusCounterclockwise(final int x, final int y,
            final int z, final int w, final int r) {
        return this.mazeData[w].rotateRadiusCounterclockwise(x, y, z, r);
    }

    @Override
    public void resize(final int x, final int y, final int z, final int w) {
        this.mazeData[w].resize(x, y, z);
    }

    @Override
    public boolean radialScan(final int x, final int y, final int z,
            final int w, final int l, final int r, final String targetName) {
        return this.mazeData[w].radialScan(x, y, z, l, r, targetName);
    }

    @Override
    public void radialScanTimerAction(final int x, final int y, final int z,
            final int w, final int l, final int r, final String targetName,
            final int timerMod) {
        this.mazeData[w].radialScanTimerAction(x, y, z, l, r, targetName,
                timerMod);
    }

    @Override
    public void radialScanKillMonsters(final int x, final int y, final int z,
            final int w, final int l, final int r) {
        this.mazeData[w].radialScanKillMonsters(x, y, z, l, r);
    }

    @Override
    public void radialScanFreezeObjects(final int x, final int y, final int z,
            final int w, final int l, final int r) {
        this.mazeData[w].radialScanFreezeObjects(x, y, z, l, r);
    }

    @Override
    public void radialScanWarpObjects(final int x, final int y, final int z,
            final int w, final int l, final int r) {
        this.mazeData[w].radialScanWarpObjects(x, y, z, l, r);
    }

    @Override
    public void radialScanShuffleObjects(final int x, final int y, final int z,
            final int w, final int r) {
        this.mazeData[w].radialScanShuffleObjects(x, y, z, r);
    }

    @Override
    public boolean isSquareVisible(final int x1, final int y1, final int x2,
            final int y2, final int w) {
        return this.mazeData[w].isSquareVisible(x1, y1, x2, y2);
    }

    @Override
    public void setCell(final MazeObject mo, final int row, final int col,
            final int floor, final int level, final int extra) {
        this.mazeData[level].setCell(mo, row, col, floor, extra);
    }

    @Override
    public void setStartRow(final int level, final int newStartRow) {
        this.mazeData[level].setStartRow(newStartRow);
    }

    @Override
    public void setStartColumn(final int level, final int newStartColumn) {
        this.mazeData[level].setStartColumn(newStartColumn);
    }

    @Override
    public void setStartFloor(final int level, final int newStartFloor) {
        this.mazeData[level].setStartFloor(newStartFloor);
    }

    @Override
    public void setStartLevel(final int newStartLevel) {
        this.startW = newStartLevel;
    }

    @Override
    public void setVisionRadiusToMaximum(final int level) {
        this.mazeData[level].setVisionRadiusToMaximum();
    }

    @Override
    public void setVisionRadiusToMinimum(final int level) {
        this.mazeData[level].setVisionRadiusToMinimum();
    }

    @Override
    public void incrementVisionRadius(final int level) {
        this.mazeData[level].incrementVisionRadius();
    }

    @Override
    public void decrementVisionRadius(final int level) {
        this.mazeData[level].decrementVisionRadius();
    }

    @Override
    public void fill(final MazeObject bottom, final MazeObject top) {
        int x;
        for (x = 0; x < this.levelCount; x++) {
            this.mazeData[x].fill(bottom, top);
        }
    }

    @Override
    public void fillLevel(final int level, final MazeObject bottom,
            final MazeObject top) {
        this.mazeData[level].fill(bottom, top);
    }

    @Override
    public void save() {
        int x;
        for (x = 0; x < this.levelCount; x++) {
            this.mazeData[x].save();
        }
    }

    @Override
    public void restore() {
        int x;
        for (x = 0; x < this.levelCount; x++) {
            this.mazeData[x].restore();
        }
    }

    @Override
    public void saveLevel(final int level) {
        this.mazeData[level].save();
    }

    @Override
    public void restoreLevel(final int level) {
        this.mazeData[level].restore();
    }

    @Override
    public void updateMonsterPosition(final int move, final int xLoc,
            final int yLoc, final Monster monster) {
        final int wLoc = FantastleReboot.getBagOStuff().getGameManager()
                .getPlayerManager().getPlayerLocationW();
        this.mazeData[wLoc].updateMonsterPosition(move, xLoc, yLoc, monster);
    }

    @Override
    public void updateMovingBlockPosition(final int move, final int xLoc,
            final int yLoc, final MovingBlock block) {
        final int wLoc = FantastleReboot.getBagOStuff().getGameManager()
                .getPlayerManager().getPlayerLocationW();
        this.mazeData[wLoc].updateMovingBlockPosition(move, xLoc, yLoc, block);
    }

    @Override
    public void postBattle(final Monster monster, final int xLoc,
            final int yLoc, final boolean player) {
        final int wLoc = FantastleReboot.getBagOStuff().getGameManager()
                .getPlayerManager().getPlayerLocationW();
        this.mazeData[wLoc].postBattle(monster, xLoc, yLoc, player);
    }

    @Override
    public void generateOneMonster() {
        final int wLoc = FantastleReboot.getBagOStuff().getGameManager()
                .getPlayerManager().getPlayerLocationW();
        this.mazeData[wLoc].generateOneMonster();
    }

    @Override
    public void warpObject(final MazeObject mo, final int x, final int y,
            final int z, final int w, final int l) {
        this.mazeData[w].warpObject(mo, x, y, z, l);
    }

    @Override
    public void enableHorizontalWraparound(final int level) {
        this.mazeData[level].enableHorizontalWraparound();
    }

    @Override
    public void disableHorizontalWraparound(final int level) {
        this.mazeData[level].disableHorizontalWraparound();
    }

    @Override
    public void enableVerticalWraparound(final int level) {
        this.mazeData[level].enableVerticalWraparound();
    }

    @Override
    public void disableVerticalWraparound(final int level) {
        this.mazeData[level].disableVerticalWraparound();
    }

    @Override
    public void enable3rdDimensionWraparound(final int level) {
        this.mazeData[level].enable3rdDimensionWraparound();
    }

    @Override
    public void disable3rdDimensionWraparound(final int level) {
        this.mazeData[level].disable3rdDimensionWraparound();
    }

    @Override
    public boolean isHorizontalWraparoundEnabled(final int level) {
        return this.mazeData[level].isHorizontalWraparoundEnabled();
    }

    @Override
    public boolean isVerticalWraparoundEnabled(final int level) {
        return this.mazeData[level].isVerticalWraparoundEnabled();
    }

    @Override
    public boolean is3rdDimensionWraparoundEnabled(final int level) {
        return this.mazeData[level].is3rdDimensionWraparoundEnabled();
    }

    @Override
    public Maze readMaze(final XDataReader reader, final int formatVersion)
            throws IOException {
        final int levels = reader.readInt();
        final Maze5 m = new Maze5();
        m.levelCount = levels;
        for (int x = 0; x < levels; x++) {
            m.mazeData[x] = LayeredTower.readLayeredTower(reader,
                    formatVersion);
        }
        // New in V5.00
        if (formatVersion == FormatConstants.MAZE_FORMAT_3) {
            m.startW = 0;
        } else if (formatVersion == FormatConstants.MAZE_FORMAT_4
                || formatVersion == FormatConstants.MAZE_FORMAT_5) {
            final int sw = reader.readInt();
            m.startW = sw;
        }
        return m;
    }

    @Override
    public void readSavedMazeState(final XDataReader reader,
            final int formatVersion) throws IOException {
        for (int x = 0; x < this.levelCount; x++) {
            this.mazeData[x].readSavedTowerState(reader, formatVersion);
        }
    }

    @Override
    public void writeMaze(final XDataWriter writer) throws IOException {
        writer.writeInt(this.levelCount);
        for (int x = 0; x < this.levelCount; x++) {
            this.mazeData[x].writeLayeredTower(writer);
        }
        // New in V4.00
        writer.writeInt(this.startW);
    }

    @Override
    public void writeSavedMazeState(final XDataWriter writer)
            throws IOException {
        for (int x = 0; x < this.levelCount; x++) {
            this.mazeData[x].writeSavedTowerState(writer);
        }
    }
}