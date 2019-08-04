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

import net.worldwizard.fantastle5.generic.MazeObject;
import net.worldwizard.fantastle5.objects.Monster;
import net.worldwizard.fantastle5.objects.MovingBlock;
import net.worldwizard.io.DataReader;
import net.worldwizard.io.DataWriter;

public interface Maze extends FormatConstants {
    // Constants
    public static final int LAYER_GROUND = 0;
    public static final int LAYER_OBJECT = 1;
    public static final int LAYER_COUNT = 2;
    public static final int SIZE_CURRENT = -1;

    // Methods
    public int getMinLevels();

    public int getMaxLevels();

    public void cutLevel(int level);

    public void copyLevel(int level);

    public void pasteLevel(int level);

    public boolean isPasteBlocked();

    public boolean isCutBlocked();

    public boolean insertLevelFromClipboard();

    public boolean addLevel(final int rows, final int cols, final int floors);

    public boolean removeLevel(int level);

    public MazeObject getCell(final int row, final int col, final int floor,
            final int level, final int extra);

    public int getFindResultRow(int level);

    public int getFindResultColumn(int level);

    public int getFindResultFloor(int level);

    public int getFindResultLevel();

    public int getStartRow(final int level);

    public int getStartColumn(final int level);

    public int getStartFloor(final int level);

    public int getStartLevel();

    public int getRows(int level);

    public int getColumns(int level);

    public int getFloors(int level);

    public int getRows();

    public int getColumns();

    public int getFloors();

    public int getLevels();

    public int getVisionRadius(int level);

    public void findAllStarts();

    public boolean findPlayerOnLevel(final int level);

    public boolean findNthMazeObjectOnLevel(final int level,
            final MazeObject obj, final int N);

    public void findAllObjectPairsAndSwap(final int level, final MazeObject o1,
            final MazeObject o2);

    public void findAllMatchingObjectsAndDecay(final int level,
            final MazeObject o);

    public void masterTrapTrigger(final int level);

    public void tickTimers(final int level, final int floor);

    public boolean rotateRadiusClockwise(final int x, final int y, final int z,
            final int w, final int r);

    public boolean rotateRadiusCounterclockwise(final int x, final int y,
            final int z, final int w, final int r);

    public void resize(final int x, final int y, final int z, final int w);

    public boolean radialScan(final int x, final int y, final int z,
            final int w, final int l, final int r, final String targetName);

    public void radialScanTimerAction(final int x, final int y, final int z,
            final int w, final int l, final int r, final String targetName,
            final int timerMod);

    public void radialScanKillMonsters(final int x, final int y, final int z,
            final int w, final int l, final int r);

    public void radialScanFreezeObjects(final int x, final int y, final int z,
            final int w, final int l, final int r);

    public void radialScanWarpObjects(final int x, final int y, final int z,
            final int w, final int l, final int r);

    public void radialScanShuffleObjects(final int x, final int y, final int z,
            final int w, final int r);

    public boolean isSquareVisible(int x1, int y1, int x2, int y2, int w);

    public void setCell(final MazeObject mo, final int row, final int col,
            final int floor, final int level, final int extra);

    public void setStartRow(final int level, final int newStartRow);

    public void setStartColumn(final int level, final int newStartColumn);

    public void setStartFloor(final int level, final int newStartFloor);

    public void setStartLevel(final int newStartLevel);

    public void setVisionRadiusToMaximum(int level);

    public void setVisionRadiusToMinimum(int level);

    public void incrementVisionRadius(int level);

    public void decrementVisionRadius(int level);

    public void fill(MazeObject bottom, MazeObject top);

    public void fillLevel(int level, MazeObject bottom, MazeObject top);

    public void save();

    public void restore();

    public void saveLevel(final int level);

    public void restoreLevel(final int level);

    public void updateMonsterPosition(final int move, final int xLoc,
            final int yLoc, final Monster monster);

    public void updateMovingBlockPosition(final int move, final int xLoc,
            final int yLoc, final MovingBlock block);

    public void postBattle(final Monster monster, final int xLoc,
            final int yLoc, final boolean player);

    public void generateOneMonster();

    public void warpObject(MazeObject mo, int x, int y, int z, int w, int l);

    public void enableHorizontalWraparound(int level);

    public void disableHorizontalWraparound(int level);

    public void enableVerticalWraparound(int level);

    public void disableVerticalWraparound(int level);

    public void enable3rdDimensionWraparound(int level);

    public void disable3rdDimensionWraparound(int level);

    public boolean isHorizontalWraparoundEnabled(int level);

    public boolean isVerticalWraparoundEnabled(int level);

    public boolean is3rdDimensionWraparoundEnabled(int level);

    public void writeMaze(DataWriter writer) throws IOException;

    public Maze readMaze(DataReader reader, int formatVersion)
            throws IOException;

    public void writeSavedMazeState(DataWriter writer) throws IOException;

    public void readSavedMazeState(DataReader reader, int formatVersion)
            throws IOException;
}