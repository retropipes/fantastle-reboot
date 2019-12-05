/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.world;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.files.FileExtensions;
import com.puttysoftware.fantastlereboot.files.PrefixIO;
import com.puttysoftware.fantastlereboot.files.SuffixIO;
import com.puttysoftware.fantastlereboot.files.versions.WorldVersionException;
import com.puttysoftware.fantastlereboot.files.versions.WorldVersions;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.randomrange.RandomLongRange;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class World {
  // Properties
  private LayeredTower worldData;
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
  public World() {
    this.worldData = null;
    this.levelCount = 0;
    this.startW = 0;
    this.locW = 0;
    this.saveW = 0;
    this.activeLevel = 0;
    this.globalVisionMode = World.UNSET;
    this.globalVisionModeExploreRadius = World.UNSET;
    this.globalVisionRadius = World.UNSET;
    this.savedStart = new int[4];
    final long random = new RandomLongRange(0, Long.MAX_VALUE).generate();
    final String randomID = Long.toHexString(random);
    this.basePath = System.getProperty("java.io.tmpdir") + File.separator
        + "FantastleReboot" + File.separator + randomID
        + FileExtensions.getWorldTempExtensionWithPeriod();
    final File base = new File(this.basePath);
    final boolean success = base.mkdirs();
    if (!success) {
      CommonDialogs.showErrorDialog("World temporary folder creation failed!",
          "FantastleReboot");
    }
  }

  // Static methods
  public static String getWorldTempFolder() {
    return System.getProperty("java.io.tmpdir") + File.separator
        + "FantastleReboot";
  }

  public static int getMinLevels() {
    return World.MIN_LEVELS;
  }

  public static int getMaxLevels() {
    return World.MAX_LEVELS;
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
  public static World getTemporaryBattleCopy() {
    final World temp = new World();
    temp.addLevel(FantastleReboot.getBattleWorldSize(),
        FantastleReboot.getBattleWorldSize(), 1);
    return temp;
  }

  public boolean hasMonster(final int x, final int y, final int z) {
    return this.worldData.hasMonster(x, y, z);
  }

  public void addMonster(final int x, final int y, final int z) {
    this.worldData.addMonster(x, y, z);
  }

  public void removeMonster(final int x, final int y, final int z) {
    this.worldData.removeMonster(x, y, z);
  }

  public boolean checkForBattle(final int px, final int py, final int pz) {
    return this.worldData.checkForBattle(px, py, pz);
  }

  public void moveAllMonsters() {
    this.worldData.moveAllMonsters(this);
  }

  public void postBattle(final int locX, final int locY) {
    this.worldData.postBattle(this, locX, locY);
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
    this.worldData.tickTimers(floor);
  }

  public void resetVisibleSquares() {
    this.worldData.resetVisibleSquares();
  }

  public void updateExploredSquares(final int xp, final int yp, final int zp) {
    this.worldData.updateExploredSquares(xp, yp, zp);
  }

  public void switchLevel(final int level) {
    this.switchLevelInternal(level);
  }

  public void switchLevelOffset(final int level) {
    this.switchLevelInternal(this.activeLevel + level);
  }

  private void switchLevelInternal(final int level) {
    if (this.activeLevel != level) {
      if (this.worldData != null) {
        try (XDataWriter writer = this.getLevelWriter()) {
          // Save old level
          this.writeWorldLevel(writer);
        } catch (final IOException io) {
          // Ignore
        }
      }
      this.activeLevel = level;
      try (XDataReader reader = this.getLevelReader()) {
        // Load new level
        this.readWorldLevel(reader);
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
    return this.activeLevel + 1 < World.MAX_LEVELS;
  }

  public boolean addLevel(final int rows, final int cols, final int floors) {
    if (this.levelCount < World.getMaxLevels()) {
      if (this.worldData != null) {
        try (XDataWriter writer = this.getLevelWriter()) {
          // Save old level
          this.writeWorldLevel(writer);
        } catch (final IOException io) {
          // Ignore
        }
      }
      this.worldData = new LayeredTower(rows, cols, floors);
      this.levelCount++;
      this.activeLevel = this.levelCount - 1;
      return true;
    } else {
      return false;
    }
  }

  public boolean hasNote(final int x, final int y, final int z) {
    return this.worldData.hasNote(x, y, z);
  }

  public void createNote(final int x, final int y, final int z) {
    this.worldData.createNote(x, y, z);
  }

  public WorldNote getNote(final int x, final int y, final int z) {
    return this.worldData.getNote(x, y, z);
  }

  public boolean cellRangeCheck(final int row, final int col, final int floor) {
    return this.worldData.cellRangeCheck(row, col, floor);
  }

  public boolean cellRangeCheck(final int row, final int col, final int floor,
      final int level, final int extra) {
    return this.worldData.cellRangeCheck(row, col, floor, extra)
        && level >= World.MIN_LEVELS - 1 && level < World.MAX_LEVELS;
  }

  public boolean floorRangeCheck(final int floor) {
    return this.worldData.floorRangeCheck(floor);
  }

  public boolean levelRangeCheck(final int level) {
    return level >= World.MIN_LEVELS - 1 && level < World.MAX_LEVELS;
  }

  public FantastleObjectModel getCell(final int row, final int col,
      final int floor, final int extra) {
    return this.worldData.getCell(row, col, floor, extra);
  }

  public int getPlayerLocationX() {
    return this.worldData.getPlayerRow();
  }

  public int getPlayerLocationY() {
    return this.worldData.getPlayerColumn();
  }

  public int getPlayerLocationZ() {
    return this.worldData.getPlayerFloor();
  }

  public int getPlayerLocationW() {
    return this.locW;
  }

  public int getStartLevel() {
    return this.startW;
  }

  public int getRows() {
    return this.worldData.getRows();
  }

  public int getColumns() {
    return this.worldData.getColumns();
  }

  public int getFloors() {
    return this.worldData.getFloors();
  }

  public int getLevels() {
    return this.levelCount;
  }

  public boolean doesPlayerExist() {
    return this.worldData.doesPlayerExist();
  }

  public boolean isSquareVisible(final int x1, final int y1, final int x2,
      final int y2) {
    return this.worldData.isSquareVisible(x1, y1, x2, y2);
  }

  public void setCell(final FantastleObjectModel mo, final int row,
      final int col, final int floor, final int extra) {
    this.worldData.setCell(mo, row, col, floor, extra);
  }

  public void setStartRow(final int newStartRow) {
    this.worldData.setStartRow(newStartRow);
  }

  public void setStartColumn(final int newStartColumn) {
    this.worldData.setStartColumn(newStartColumn);
  }

  public void setStartFloor(final int newStartFloor) {
    this.worldData.setStartFloor(newStartFloor);
  }

  public void savePlayerLocation() {
    this.saveW = this.locW;
    this.worldData.savePlayerLocation();
  }

  public void restorePlayerLocation() {
    this.locW = this.saveW;
    this.worldData.restorePlayerLocation();
  }

  public void setPlayerToStart() {
    this.worldData.setPlayerToStart();
  }

  public void setPlayerLocationX(final int newPlayerRow) {
    this.worldData.setPlayerRow(newPlayerRow);
  }

  public void setPlayerLocationY(final int newPlayerColumn) {
    this.worldData.setPlayerColumn(newPlayerColumn);
  }

  public void setPlayerLocationZ(final int newPlayerFloor) {
    this.worldData.setPlayerFloor(newPlayerFloor);
  }

  public void setPlayerLocationW(final int newPlayerLevel) {
    this.startW = newPlayerLevel;
  }

  public void offsetPlayerLocationX(final int newPlayerRow) {
    this.worldData.offsetPlayerRow(newPlayerRow);
  }

  public void offsetPlayerLocationY(final int newPlayerColumn) {
    this.worldData.offsetPlayerColumn(newPlayerColumn);
  }

  public void offsetPlayerLocationZ(final int newPlayerFloor) {
    this.worldData.offsetPlayerFloor(newPlayerFloor);
  }

  public void offsetPlayerLocationW(final int newPlayerLevel) {
    this.locW += newPlayerLevel;
  }

  public void fillLevelRandomly() {
    if (Prefs.isWorldGeneratorConstrainedRandom()) {
      this.worldData.fillRandomlyConstrained(this, this.activeLevel);
    } else if (Prefs.isWorldGeneratorTwister()) {
      this.worldData.fillRandomlyTwister(this, this.activeLevel);
    } else {
      this.worldData.fillRandomlyConstrained(this, this.activeLevel);
    }
  }

  public void fullScanButton(final int l) {
    this.worldData.fullScanButton(l);
  }

  public void save() {
    this.worldData.save();
  }

  public void restore() {
    this.worldData.restore();
  }

  // Global settings
  public int getGlobalVisionRadius() {
    return this.globalVisionRadius;
  }

  public void setGlobalVisionRadius(final int value) {
    this.globalVisionRadius = value;
  }

  public boolean isGlobalVisionRadiusSet() {
    return this.globalVisionRadius == World.UNSET;
  }

  public void unsetGlobalVisionRadius() {
    this.globalVisionRadius = World.UNSET;
  }

  public int getGlobalExploreExpansionRadius() {
    return this.globalVisionModeExploreRadius;
  }

  public void setGlobalExploreExpansionRadius(final int value) {
    this.globalVisionModeExploreRadius = value;
  }

  public boolean isGlobalExploreExpansionRadiusSet() {
    return this.globalVisionModeExploreRadius == World.UNSET;
  }

  public void unsetGlobalExploreExpansionRadius() {
    this.globalVisionModeExploreRadius = World.UNSET;
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
    return this.globalVisionMode == World.UNSET;
  }

  public void unsetGlobalVisionMode() {
    this.globalVisionMode = World.UNSET;
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
    return this.worldData.isHorizontalWrapEnabled();
  }

  public boolean isVerticalWrapEnabled() {
    return this.worldData.isVerticalWrapEnabled();
  }

  public boolean isFloorWrapEnabled() {
    return this.worldData.isFloorWrapEnabled();
  }

  public void setHorizontalWrapEnabled(final boolean value) {
    this.worldData.setHorizontalWrapEnabled(value);
  }

  public void setVerticalWrapEnabled(final boolean value) {
    this.worldData.setVerticalWrapEnabled(value);
  }

  public void setFloorWrapEnabled(final boolean value) {
    this.worldData.setFloorWrapEnabled(value);
  }

  public int getVisionRadius() {
    if (this.isGlobalVisionRadiusSet()) {
      return this.getGlobalVisionRadius();
    }
    return this.worldData.getVisionRadius();
  }

  public int getVisionRadiusValue() {
    return this.worldData.getVisionRadius();
  }

  public void setVisionRadius(final int value) {
    this.worldData.setVisionRadius(value);
  }

  public int getExploreExpansionRadius() {
    if (this.isGlobalExploreExpansionRadiusSet()) {
      return this.getGlobalExploreExpansionRadius();
    }
    return this.worldData.getExploreExpansionRadius();
  }

  public int getExploreExpansionRadiusValue() {
    return this.worldData.getExploreExpansionRadius();
  }

  public void setExploreExpansionRadius(final int value) {
    this.worldData.setExploreExpansionRadius(value);
  }

  public boolean isVisionModeNone() {
    if (this.isGlobalVisionModeSet()) {
      return this.isGlobalVisionModeNone();
    }
    return this.worldData.isVisionModeNone();
  }

  public boolean isVisionModeExplore() {
    if (this.isGlobalVisionModeSet()) {
      return this.isGlobalVisionModeExplore();
    }
    return this.worldData.isVisionModeExplore();
  }

  public boolean isVisionModeFieldOfView() {
    if (this.isGlobalVisionModeSet()) {
      return this.isGlobalVisionModeFieldOfView();
    }
    return this.worldData.isVisionModeFieldOfView();
  }

  public boolean isVisionModeFixedRadius() {
    if (this.isGlobalVisionModeSet()) {
      return this.isGlobalVisionModeFixedRadius();
    }
    return this.worldData.isVisionModeFixedRadius();
  }

  public boolean isLocalVisionModeNone() {
    return this.worldData.isVisionModeNone();
  }

  public boolean isLocalVisionModeExplore() {
    return this.worldData.isVisionModeExplore();
  }

  public boolean isLocalVisionModeFieldOfView() {
    return this.worldData.isVisionModeFieldOfView();
  }

  public boolean isLocalVisionModeFixedRadius() {
    return this.worldData.isVisionModeFixedRadius();
  }

  public void resetVisionModeNone() {
    this.worldData.resetVisionModeNone();
  }

  public void addVisionModeExplore() {
    this.worldData.addVisionModeExplore();
  }

  public void addVisionModeFieldOfView() {
    this.worldData.addVisionModeFieldOfView();
  }

  public void addVisionModeFixedRadius() {
    this.worldData.addVisionModeFixedRadius();
  }

  public void removeVisionModeExplore() {
    this.worldData.removeVisionModeExplore();
  }

  public void removeVisionModeFieldOfView() {
    this.worldData.removeVisionModeFieldOfView();
  }

  public void removeVisionModeFixedRadius() {
    this.worldData.removeVisionModeFixedRadius();
  }

  // File I/O
  public World readWorld() throws IOException {
    final World m = new World();
    // Attach handlers
    m.setPrefixHandler(this.prefixHandler);
    m.setSuffixHandler(this.suffixHandler);
    // Make base paths the same
    m.basePath = this.basePath;
    int version = 0;
    // Create metafile reader
    try (XDataReader metaReader = new XDataReader(
        m.basePath + File.separator + "metafile.xml", "world")) {
      // Read metafile
      version = m.readWorldMetafile(metaReader);
    } catch (final IOException ioe) {
      throw ioe;
    }
    // Create data reader
    try (XDataReader dataReader = m.getLevelReader()) {
      // Read data
      m.readWorldLevel(dataReader, version);
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

  private int readWorldMetafile(final XDataReader reader) throws IOException {
    int ver = WorldVersions.LATEST;
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

  private void readWorldLevel(final XDataReader reader) throws IOException {
    this.readWorldLevel(reader, WorldVersions.LATEST);
  }

  private void readWorldLevel(final XDataReader reader, final int formatVersion)
      throws IOException {
    if (formatVersion == WorldVersions.LATEST) {
      this.worldData = LayeredTower.readLayeredTowerV1(reader);
      this.worldData.readSavedTowerState(reader, formatVersion);
    } else {
      throw new WorldVersionException(formatVersion);
    }
  }

  public void writeWorld() throws IOException {
    try {
      // Create metafile writer
      try (XDataWriter metaWriter = new XDataWriter(
          this.basePath + File.separator + "metafile.xml", "world")) {
        // Write metafile
        this.writeWorldMetafile(metaWriter);
      }
      // Create data writer
      try (XDataWriter dataWriter = this.getLevelWriter()) {
        // Write data
        this.writeWorldLevel(dataWriter);
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

  private void writeWorldMetafile(final XDataWriter writer) throws IOException {
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

  private void writeWorldLevel(final XDataWriter writer) throws IOException {
    // Write the level
    this.worldData.writeLayeredTower(writer);
    this.worldData.writeSavedTowerState(writer);
  }
}
