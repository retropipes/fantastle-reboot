/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.files.FileExtensions;
import com.puttysoftware.fantastlereboot.files.PrefixIO;
import com.puttysoftware.fantastlereboot.files.SuffixIO;
import com.puttysoftware.fantastlereboot.files.versions.MazeVersionException;
import com.puttysoftware.fantastlereboot.files.versions.MazeVersions;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
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
  private int globalVisionMode;
  private int globalVisionModeExploreRadius;
  private int globalVisionRadius;
  private String basePath;
  private PrefixIO prefixHandler;
  private SuffixIO suffixHandler;
  private final int[] savedStart;
  private static final int MIN_LEVELS = 1;
  private static final int MAX_LEVELS = 55;
  private static final int UNSET = -1;
  static final int MAX_VISION_RADIUS = 16;

  // Constructors
  public Maze() {
    this.mazeData = null;
    this.levelCount = 0;
    this.startW = 0;
    this.locW = 0;
    this.saveW = 0;
    this.activeLevel = 0;
    this.globalVisionMode = Maze.UNSET;
    this.globalVisionModeExploreRadius = Maze.UNSET;
    this.globalVisionRadius = Maze.UNSET;
    this.savedStart = new int[4];
    final long random = new RandomLongRange(0, Long.MAX_VALUE).generate();
    final String randomID = Long.toHexString(random);
    this.basePath = System.getProperty("java.io.tmpdir") + File.separator
        + "FantastleReboot" + File.separator + randomID
        + FileExtensions.getMazeTempExtensionWithPeriod();
    final File base = new File(this.basePath);
    final boolean success = base.mkdirs();
    if (!success) {
      CommonDialogs.showErrorDialog("Maze temporary folder creation failed!",
          "FantastleReboot");
    }
  }

  // Static methods
  public static String getMazeTempFolder() {
    return System.getProperty("java.io.tmpdir") + File.separator
        + "FantastleReboot";
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
    return temp;
  }

  public boolean hasMonster(final int x, final int y, final int z) {
    return this.mazeData.hasMonster(x, y, z);
  }

  public void addMonster(final int x, final int y, final int z) {
    this.mazeData.addMonster(x, y, z);
  }

  public void removeMonster(final int x, final int y, final int z) {
    this.mazeData.removeMonster(x, y, z);
  }

  public boolean checkForBattle(final int px, final int py, final int pz) {
    return this.mazeData.checkForBattle(px, py, pz);
  }

  public void moveAllMonsters() {
    this.mazeData.moveAllMonsters(this);
  }

  public void postBattle(final int locX, final int locY) {
    this.mazeData.postBattle(this, locX, locY);
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

  public void updateExploredSquares(final int xp, final int yp, final int zp) {
    this.mazeData.updateExploredSquares(xp, yp, zp);
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
    return this.activeLevel + level < this.levelCount
        && this.activeLevel + level >= 0;
  }

  public boolean canAddLevel() {
    return this.activeLevel + 1 < Maze.MAX_LEVELS;
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

  public boolean cellRangeCheck(final int row, final int col, final int floor) {
    return this.mazeData.cellRangeCheck(row, col, floor);
  }

  public boolean cellRangeCheck(final int row, final int col, final int floor,
      final int level, final int extra) {
    return this.mazeData.cellRangeCheck(row, col, floor, extra)
        && level >= Maze.MIN_LEVELS - 1 && level < Maze.MAX_LEVELS;
  }

  public boolean floorRangeCheck(final int floor) {
    return this.mazeData.floorRangeCheck(floor);
  }

  public boolean levelRangeCheck(final int level) {
    return level >= Maze.MIN_LEVELS - 1 && level < Maze.MAX_LEVELS;
  }

  public FantastleObjectModel getCell(final int row, final int col,
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

  public int getPlayerLocationW() {
    return this.locW;
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

  public int getLevels() {
    return this.levelCount;
  }

  public boolean doesPlayerExist() {
    return this.mazeData.doesPlayerExist();
  }

  public boolean isSquareVisible(final int x1, final int y1, final int x2,
      final int y2) {
    return this.mazeData.isSquareVisible(x1, y1, x2, y2);
  }

  public void setCell(final FantastleObjectModel mo, final int row,
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

  public void setPlayerLocationW(final int newPlayerLevel) {
    this.startW = newPlayerLevel;
  }

  public void offsetPlayerLocationX(final int newPlayerRow) {
    this.mazeData.offsetPlayerRow(newPlayerRow);
  }

  public void offsetPlayerLocationY(final int newPlayerColumn) {
    this.mazeData.offsetPlayerColumn(newPlayerColumn);
  }

  public void offsetPlayerLocationZ(final int newPlayerFloor) {
    this.mazeData.offsetPlayerFloor(newPlayerFloor);
  }

  public void offsetPlayerLocationW(final int newPlayerLevel) {
    this.locW += newPlayerLevel;
  }

  public void fillLevelRandomly() {
    if (Prefs.isMazeGeneratorPureRandom()) {
      this.mazeData.fillRandomlyPure(this, this.activeLevel);
    } else if (Prefs.isMazeGeneratorConstrainedRandom()) {
      this.mazeData.fillRandomlyConstrained(this, this.activeLevel);
      // } else if (Prefs.isMazeGeneratorTwister()) {
      // this.mazeData.fillRandomlyTwister(this, this.activeLevel);
    } else {
      this.mazeData.fillRandomlyConstrained(this, this.activeLevel);
    }
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

  // Global settings
  public int getGlobalVisionRadius() {
    return this.globalVisionRadius;
  }

  public void setGlobalVisionRadius(final int value) {
    this.globalVisionRadius = value;
  }

  public boolean isGlobalVisionRadiusSet() {
    return this.globalVisionRadius == Maze.UNSET;
  }

  public void unsetGlobalVisionRadius() {
    this.globalVisionRadius = Maze.UNSET;
  }

  public int getGlobalExploreExpansionRadius() {
    return this.globalVisionModeExploreRadius;
  }

  public void setGlobalExploreExpansionRadius(final int value) {
    this.globalVisionModeExploreRadius = value;
  }

  public boolean isGlobalExploreExpansionRadiusSet() {
    return this.globalVisionModeExploreRadius == Maze.UNSET;
  }

  public void unsetGlobalExploreExpansionRadius() {
    this.globalVisionModeExploreRadius = Maze.UNSET;
  }

  public boolean isGlobalVisionModeNone() {
    return this.globalVisionMode == VisionModes.NONE;
  }

  public boolean isGlobalVisionModeExplore() {
    return (this.globalVisionMode
        | VisionModes.EXPLORE) == this.globalVisionMode;
  }

  public boolean isGlobalVisionModeFieldOfView() {
    return (this.globalVisionMode
        | VisionModes.FIELD_OF_VIEW) == this.globalVisionMode;
  }

  public boolean isGlobalVisionModeFixedRadius() {
    return (this.globalVisionMode
        | VisionModes.FIXED_RADIUS) == this.globalVisionMode;
  }

  public void resetGlobalVisionModeNone() {
    this.globalVisionMode = VisionModes.NONE;
  }

  public boolean isGlobalVisionModeSet() {
    return this.globalVisionMode == Maze.UNSET;
  }

  public void unsetGlobalVisionMode() {
    this.globalVisionMode = Maze.UNSET;
  }

  public void addGlobalVisionModeExplore() {
    this.removeGlobalVisionModeFixedRadius();
    if (!this.isGlobalVisionModeExplore()) {
      this.globalVisionMode += VisionModes.EXPLORE;
    }
  }

  public void addGlobalVisionModeFieldOfView() {
    if (!this.isGlobalVisionModeFieldOfView()) {
      this.globalVisionMode += VisionModes.FIELD_OF_VIEW;
    }
  }

  public void addGlobalVisionModeFixedRadius() {
    this.removeGlobalVisionModeExplore();
    if (!this.isGlobalVisionModeFixedRadius()) {
      this.globalVisionMode += VisionModes.FIXED_RADIUS;
    }
  }

  public void removeGlobalVisionModeExplore() {
    if (this.isGlobalVisionModeExplore()) {
      this.globalVisionMode -= VisionModes.EXPLORE;
    }
  }

  public void removeGlobalVisionModeFieldOfView() {
    if (this.isGlobalVisionModeFieldOfView()) {
      this.globalVisionMode -= VisionModes.FIELD_OF_VIEW;
    }
  }

  public void removeGlobalVisionModeFixedRadius() {
    if (this.isGlobalVisionModeFixedRadius()) {
      this.globalVisionMode -= VisionModes.FIXED_RADIUS;
    }
  }

  // Settings
  public boolean isHorizontalWrapEnabled() {
    return this.mazeData.isHorizontalWrapEnabled();
  }

  public boolean isVerticalWrapEnabled() {
    return this.mazeData.isVerticalWrapEnabled();
  }

  public boolean isFloorWrapEnabled() {
    return this.mazeData.isFloorWrapEnabled();
  }

  public void setHorizontalWrapEnabled(final boolean value) {
    this.mazeData.setHorizontalWrapEnabled(value);
  }

  public void setVerticalWrapEnabled(final boolean value) {
    this.mazeData.setVerticalWrapEnabled(value);
  }

  public void setFloorWrapEnabled(final boolean value) {
    this.mazeData.setFloorWrapEnabled(value);
  }

  public int getVisionRadius() {
    if (this.isGlobalVisionRadiusSet()) {
      return this.getGlobalVisionRadius();
    }
    return this.mazeData.getVisionRadius();
  }

  public int getVisionRadiusValue() {
    return this.mazeData.getVisionRadius();
  }

  public void setVisionRadius(final int value) {
    this.mazeData.setVisionRadius(value);
  }

  public int getExploreExpansionRadius() {
    if (this.isGlobalExploreExpansionRadiusSet()) {
      return this.getGlobalExploreExpansionRadius();
    }
    return this.mazeData.getExploreExpansionRadius();
  }

  public int getExploreExpansionRadiusValue() {
    return this.mazeData.getExploreExpansionRadius();
  }

  public void setExploreExpansionRadius(final int value) {
    this.mazeData.setExploreExpansionRadius(value);
  }

  public boolean isVisionModeNone() {
    if (this.isGlobalVisionModeSet()) {
      return this.isGlobalVisionModeNone();
    }
    return this.mazeData.isVisionModeNone();
  }

  public boolean isVisionModeExplore() {
    if (this.isGlobalVisionModeSet()) {
      return this.isGlobalVisionModeExplore();
    }
    return this.mazeData.isVisionModeExplore();
  }

  public boolean isVisionModeFieldOfView() {
    if (this.isGlobalVisionModeSet()) {
      return this.isGlobalVisionModeFieldOfView();
    }
    return this.mazeData.isVisionModeFieldOfView();
  }

  public boolean isVisionModeFixedRadius() {
    if (this.isGlobalVisionModeSet()) {
      return this.isGlobalVisionModeFixedRadius();
    }
    return this.mazeData.isVisionModeFixedRadius();
  }

  public boolean isLocalVisionModeNone() {
    return this.mazeData.isVisionModeNone();
  }

  public boolean isLocalVisionModeExplore() {
    return this.mazeData.isVisionModeExplore();
  }

  public boolean isLocalVisionModeFieldOfView() {
    return this.mazeData.isVisionModeFieldOfView();
  }

  public boolean isLocalVisionModeFixedRadius() {
    return this.mazeData.isVisionModeFixedRadius();
  }

  public void resetVisionModeNone() {
    this.mazeData.resetVisionModeNone();
  }

  public void addVisionModeExplore() {
    this.mazeData.addVisionModeExplore();
  }

  public void addVisionModeFieldOfView() {
    this.mazeData.addVisionModeFieldOfView();
  }

  public void addVisionModeFixedRadius() {
    this.mazeData.addVisionModeFixedRadius();
  }

  public void removeVisionModeExplore() {
    this.mazeData.removeVisionModeExplore();
  }

  public void removeVisionModeFieldOfView() {
    this.mazeData.removeVisionModeFieldOfView();
  }

  public void removeVisionModeFixedRadius() {
    this.mazeData.removeVisionModeFixedRadius();
  }

  // File I/O
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
    return new XDataReader(
        this.basePath + File.separator + "level" + this.activeLevel + ".xml",
        "level");
  }

  private int readMazeMetafile(final XDataReader reader) throws IOException {
    int ver = MazeVersions.LATEST;
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
    this.globalVisionMode = reader.readInt();
    this.globalVisionModeExploreRadius = reader.readInt();
    this.globalVisionRadius = reader.readInt();
    if (this.suffixHandler != null) {
      this.suffixHandler.readSuffix(reader, ver);
    }
    return ver;
  }

  private void readMazeLevel(final XDataReader reader) throws IOException {
    this.readMazeLevel(reader, MazeVersions.LATEST);
  }

  private void readMazeLevel(final XDataReader reader, final int formatVersion)
      throws IOException {
    if (formatVersion == MazeVersions.LATEST) {
      this.mazeData = LayeredTower.readLayeredTowerV1(reader);
      this.mazeData.readSavedTowerState(reader, formatVersion);
    } else {
      throw new MazeVersionException(formatVersion);
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
    return new XDataWriter(
        this.basePath + File.separator + "level" + this.activeLevel + ".xml",
        "level");
  }

  private void writeMazeMetafile(final XDataWriter writer) throws IOException {
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
    writer.writeInt(this.globalVisionMode);
    writer.writeInt(this.globalVisionModeExploreRadius);
    writer.writeInt(this.globalVisionRadius);
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
