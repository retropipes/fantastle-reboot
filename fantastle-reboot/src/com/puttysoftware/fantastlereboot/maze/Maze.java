/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.VersionException;
import com.puttysoftware.fantastlereboot.creatures.monsters.Monster;
import com.puttysoftware.fantastlereboot.maze.abc.AbstractMazeObject;
import com.puttysoftware.randomrange.RandomLongRange;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;



public class Maze {
    // Properties
    private LayeredTower mazeData;
    private int startW;
    private int locW;
    private int saveW;
    private int levelCount;
    private int activeLevel;
    private String basePath;
    private PrefixIO prefixHandler;
    private SuffixIO suffixHandler;
    private final int[] savedStart;
    private static final int MIN_LEVELS = 1;
    private static final int MAX_LEVELS = 55;

    // Constructors
    public Maze() {
        this.mazeData = null;
        this.levelCount = 0;
        this.startW = 0;
        this.locW = 0;
        this.saveW = 0;
        this.activeLevel = 0;
        this.savedStart = new int[4];
        final long random = new RandomLongRange(0, Long.MAX_VALUE).generate();
        final String randomID = Long.toHexString(random);
        this.basePath = System.getProperty("java.io.tmpdir") + File.separator
                + "TallerTower" + File.separator + randomID + ".maze";
        final File base = new File(this.basePath);
        final boolean success = base.mkdirs();
        if (!success) {
            CommonDialogs.showErrorDialog(
                    "Maze temporary folder creation failed!", "TallerTower");
        }
    }

    // Static methods
    public static String getMazeTempFolder() {
        return System.getProperty("java.io.tmpdir") + File.separator
                + "TallerTower";
    }

    public static int getMinLevels() {
        return Maze.MIN_LEVELS;
    }

    public static int getMaxLevels() {
        return Maze.MAX_LEVELS;
    }

    public static int getMaxFloors() {
        return LayeredTower.getMaxFloors();
    }

    public static int getMaxColumns() {
        return LayeredTower.getMaxColumns();
    }

    public static int getMaxRows() {
        return LayeredTower.getMaxRows();
    }

    // Methods
    public static Maze getTemporaryBattleCopy() {
        final Maze temp = new Maze();
        temp.addLevel(FantastleReboot.getBattleMazeSize(),
                FantastleReboot.getBattleMazeSize(), 1);
        temp.fill(new Tile(), new Empty());
        return temp;
    }

    public void updateMonsterPosition(final int move, final int xLoc,
            final int yLoc, final Monster monster) {
        this.mazeData.updateMonsterPosition(move, xLoc, yLoc, monster);
    }

    public void postBattle(final Monster m, final int xLoc, final int yLoc,
            final boolean player) {
        this.mazeData.postBattle(m, xLoc, yLoc, player);
    }

    public String getBasePath() {
        return this.basePath;
    }

    public void setPrefixHandler(final PrefixIO xph) {
        this.prefixHandler = xph;
    }

    public void setSuffixHandler(final SuffixIO xsh) {
        this.suffixHandler = xsh;
    }

    public void tickTimers(final int floor) {
        this.mazeData.tickTimers(floor);
    }

    public void resetVisibleSquares() {
        this.mazeData.resetVisibleSquares();
    }

    public void updateVisibleSquares(final int xp, final int yp, final int zp) {
        this.mazeData.updateVisibleSquares(xp, yp, zp);
    }

    public void switchLevel(final int level) {
        this.switchLevelInternal(level);
    }

    public void switchLevelOffset(final int level) {
        this.switchLevelInternal(this.activeLevel + level);
    }

    private void switchLevelInternal(final int level) {
        if (this.activeLevel != level) {
            if (this.mazeData != null) {
                try (XDataWriter writer = this.getLevelWriter()) {
                    // Save old level
                    this.writeMazeLevel(writer);
                } catch (final IOException io) {
                    // Ignore
                }
            }
            this.activeLevel = level;
            try (XDataReader reader = this.getLevelReader()) {
                // Load new level
                this.readMazeLevel(reader);
            } catch (final IOException io) {
                // Ignore
            }
        }
    }

    public boolean doesLevelExistOffset(final int level) {
        return (this.activeLevel + level < this.levelCount
                && this.activeLevel + level >= 0);
    }

    public boolean addLevel(final int rows, final int cols, final int floors) {
        if (this.levelCount < Maze.getMaxLevels()) {
            if (this.mazeData != null) {
                try (XDataWriter writer = this.getLevelWriter()) {
                    // Save old level
                    this.writeMazeLevel(writer);
                } catch (final IOException io) {
                    // Ignore
                }
            }
            this.mazeData = new LayeredTower(rows, cols, floors);
            this.levelCount++;
            this.activeLevel = this.levelCount - 1;
            return true;
        } else {
            return false;
        }
    }

    public boolean hasNote(final int x, final int y, final int z) {
        return this.mazeData.hasNote(x, y, z);
    }

    public void createNote(final int x, final int y, final int z) {
        this.mazeData.createNote(x, y, z);
    }

    public MazeNote getNote(final int x, final int y, final int z) {
        return this.mazeData.getNote(x, y, z);
    }

    public AbstractMazeObject getCell(final int row, final int col,
            final int floor, final int extra) {
        return this.mazeData.getCell(row, col, floor, extra);
    }

    public int getPlayerLocationX() {
        return this.mazeData.getPlayerRow();
    }

    public int getPlayerLocationY() {
        return this.mazeData.getPlayerColumn();
    }

    public int getPlayerLocationZ() {
        return this.mazeData.getPlayerFloor();
    }

    public int getStartLevel() {
        return this.startW;
    }

    public int getRows() {
        return this.mazeData.getRows();
    }

    public int getColumns() {
        return this.mazeData.getColumns();
    }

    public int getFloors() {
        return this.mazeData.getFloors();
    }

    public boolean doesPlayerExist() {
        return this.mazeData.doesPlayerExist();
    }

    public boolean isSquareVisible(final int x1, final int y1, final int x2,
            final int y2) {
        return this.mazeData.isSquareVisible(x1, y1, x2, y2);
    }

    public void setCell(final AbstractMazeObject mo, final int row,
            final int col, final int floor, final int extra) {
        this.mazeData.setCell(mo, row, col, floor, extra);
    }

    public void setStartRow(final int newStartRow) {
        this.mazeData.setStartRow(newStartRow);
    }

    public void setStartColumn(final int newStartColumn) {
        this.mazeData.setStartColumn(newStartColumn);
    }

    public void setStartFloor(final int newStartFloor) {
        this.mazeData.setStartFloor(newStartFloor);
    }

    public void savePlayerLocation() {
        this.saveW = this.locW;
        this.mazeData.savePlayerLocation();
    }

    public void restorePlayerLocation() {
        this.locW = this.saveW;
        this.mazeData.restorePlayerLocation();
    }

    public void setPlayerToStart() {
        this.mazeData.setPlayerToStart();
    }

    public void setPlayerLocationX(final int newPlayerRow) {
        this.mazeData.setPlayerRow(newPlayerRow);
    }

    public void setPlayerLocationY(final int newPlayerColumn) {
        this.mazeData.setPlayerColumn(newPlayerColumn);
    }

    public void setPlayerLocationZ(final int newPlayerFloor) {
        this.mazeData.setPlayerFloor(newPlayerFloor);
    }

    public void offsetPlayerLocationX(final int newPlayerRow) {
        this.mazeData.offsetPlayerRow(newPlayerRow);
    }

    public void offsetPlayerLocationY(final int newPlayerColumn) {
        this.mazeData.offsetPlayerColumn(newPlayerColumn);
    }

    public void offsetPlayerLocationW(final int newPlayerLevel) {
        this.locW += newPlayerLevel;
    }

    public void fillLevelRandomly() {
        this.mazeData.fillRandomly(this, this.activeLevel);
    }

    public void fullScanButton(final int l) {
        this.mazeData.fullScanButton(l);
    }

    public void save() {
        this.mazeData.save();
    }

    public void restore() {
        this.mazeData.restore();
    }

    private void fill(final AbstractMazeObject bottom,
            final AbstractMazeObject top) {
        this.mazeData.fillFloor(bottom, top, 0);
    }

    public Maze readMaze() throws IOException {
        final Maze m = new Maze();
        // Attach handlers
        m.setPrefixHandler(this.prefixHandler);
        m.setSuffixHandler(this.suffixHandler);
        // Make base paths the same
        m.basePath = this.basePath;
        int version = 0;
        // Create metafile reader
        try (XDataReader metaReader = new XDataReader(
                m.basePath + File.separator + "metafile.xml", "maze")) {
            // Read metafile
            version = m.readMazeMetafile(metaReader);
        } catch (final IOException ioe) {
            throw ioe;
        }
        // Create data reader
        try (XDataReader dataReader = m.getLevelReader()) {
            // Read data
            m.readMazeLevel(dataReader, version);
            return m;
        } catch (final IOException ioe) {
            throw ioe;
        }
    }

    private XDataReader getLevelReader() throws IOException {
        return new XDataReader(this.basePath + File.separator + "level"
                + this.activeLevel + ".xml", "level");
    }

    private int readMazeMetafile(final XDataReader reader) throws IOException {
        int ver = FormatConstants.MAZE_FORMAT_LATEST;
        if (this.prefixHandler != null) {
            ver = this.prefixHandler.readPrefix(reader);
        }
        this.levelCount = reader.readInt();
        this.startW = reader.readInt();
        this.locW = reader.readInt();
        this.saveW = reader.readInt();
        this.activeLevel = reader.readInt();
        for (int y = 0; y < 4; y++) {
            this.savedStart[y] = reader.readInt();
        }
        if (this.suffixHandler != null) {
            this.suffixHandler.readSuffix(reader, ver);
        }
        return ver;
    }

    private void readMazeLevel(final XDataReader reader) throws IOException {
        this.readMazeLevel(reader, FormatConstants.MAZE_FORMAT_LATEST);
    }

    private void readMazeLevel(final XDataReader reader,
            final int formatVersion) throws IOException {
        if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
            this.mazeData = LayeredTower.readLayeredTowerV1(reader);
            this.mazeData.readSavedTowerState(reader, formatVersion);
        } else {
            throw new VersionException(
                    "Unknown maze format version: " + formatVersion + "!");
        }
    }

    public void writeMaze() throws IOException {
        try {
            // Create metafile writer
            try (XDataWriter metaWriter = new XDataWriter(
                    this.basePath + File.separator + "metafile.xml", "maze")) {
                // Write metafile
                this.writeMazeMetafile(metaWriter);
            }
            // Create data writer
            try (XDataWriter dataWriter = this.getLevelWriter()) {
                // Write data
                this.writeMazeLevel(dataWriter);
            }
        } catch (final IOException ioe) {
            throw ioe;
        }
    }

    private XDataWriter getLevelWriter() throws IOException {
        return new XDataWriter(this.basePath + File.separator + "level"
                + this.activeLevel + ".xml", "level");
    }

    private void writeMazeMetafile(final XDataWriter writer)
            throws IOException {
        if (this.prefixHandler != null) {
            this.prefixHandler.writePrefix(writer);
        }
        writer.writeInt(this.levelCount);
        writer.writeInt(this.startW);
        writer.writeInt(this.locW);
        writer.writeInt(this.saveW);
        writer.writeInt(this.activeLevel);
        for (int y = 0; y < 4; y++) {
            writer.writeInt(this.savedStart[y]);
        }
        if (this.suffixHandler != null) {
            this.suffixHandler.writeSuffix(writer);
        }
    }

    private void writeMazeLevel(final XDataWriter writer) throws IOException {
        // Write the level
        this.mazeData.writeLayeredTower(writer);
        this.mazeData.writeSavedTowerState(writer);
    }
}
