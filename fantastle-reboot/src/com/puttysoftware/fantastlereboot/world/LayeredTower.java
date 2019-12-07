/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.world;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Modes;
import com.puttysoftware.fantastlereboot.files.versions.WorldVersions;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.GameObjects;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objectmodel.RandomGenerationRule;
import com.puttysoftware.fantastlereboot.objects.ArmorShop;
import com.puttysoftware.fantastlereboot.objects.BankShop;
import com.puttysoftware.fantastlereboot.objects.BonusShop;
import com.puttysoftware.fantastlereboot.objects.ClosedDoor;
import com.puttysoftware.fantastlereboot.objects.ElementalShop;
import com.puttysoftware.fantastlereboot.objects.HealShop;
import com.puttysoftware.fantastlereboot.objects.ItemShop;
import com.puttysoftware.fantastlereboot.objects.NecklaceShop;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Pit;
import com.puttysoftware.fantastlereboot.objects.RegenerateShop;
import com.puttysoftware.fantastlereboot.objects.SpellShop;
import com.puttysoftware.fantastlereboot.objects.Spring;
import com.puttysoftware.fantastlereboot.objects.StairsDown;
import com.puttysoftware.fantastlereboot.objects.StairsUp;
import com.puttysoftware.fantastlereboot.objects.SuperPit;
import com.puttysoftware.fantastlereboot.objects.SuperSpring;
import com.puttysoftware.fantastlereboot.objects.Wall;
import com.puttysoftware.fantastlereboot.objects.WeaponShop;
import com.puttysoftware.randomrange.RandomLongRange;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.storage.FlagStorage;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

final class LayeredTower implements Cloneable {
  // Properties
  private final LowLevelWorldDataStore data;
  private LowLevelWorldDataStore savedTowerState;
  private final FlagStorage visionData;
  private final FlagStorage monsterData;
  private final LowLevelNoteDataStore noteData;
  private final int[] playerStartData;
  private final int[] playerLocationData;
  private final int[] savedPlayerLocationData;
  private final int[] findResult;
  private boolean horizontalWrapEnabled;
  private boolean verticalWrapEnabled;
  private boolean floorWrapEnabled;
  private int visionMode;
  private int visionModeExploreRadius;
  private int visionRadius;
  private String[][] tiles;
  private final Dimension size;
  private final Random rand;
  private final long seed;
  private final int hallsize;
  private final int roomsize;
  private int lastRoomX;
  private int lastRoomY;
  private int lastRoomW;
  private int lastRoomH;
  private static final int MAX_FLOORS = 16;
  private static final int MAX_COLUMNS = 64;
  private static final int MAX_ROWS = 64;

  // Constructors
  public LayeredTower(final int rows, final int cols, final int floors) {
    this(rows, cols, floors, RandomLongRange.generateRaw());
  }

  public LayeredTower(final int rows, final int cols, final int floors,
      final long randSeed) {
    this.data = new LowLevelWorldDataStore(cols, rows, floors, Layers.COUNT);
    this.monsterData = new FlagStorage(cols, rows, floors);
    this.savedTowerState = new LowLevelWorldDataStore(cols, rows, floors,
        Layers.COUNT);
    this.visionData = new FlagStorage(cols, rows, floors);
    this.noteData = new LowLevelNoteDataStore(cols, rows, floors);
    this.playerStartData = new int[3];
    Arrays.fill(this.playerStartData, -1);
    this.playerLocationData = new int[3];
    Arrays.fill(this.playerLocationData, -1);
    this.savedPlayerLocationData = new int[3];
    Arrays.fill(this.savedPlayerLocationData, -1);
    this.findResult = new int[3];
    Arrays.fill(this.findResult, -1);
    this.horizontalWrapEnabled = false;
    this.verticalWrapEnabled = false;
    this.floorWrapEnabled = false;
    this.visionMode = VisionModes.EXPLORE | VisionModes.FIELD_OF_VIEW;
    this.visionModeExploreRadius = 2;
    this.visionRadius = World.MAX_VISION_RADIUS;
    this.size = new Dimension(cols, rows);
    this.seed = randSeed;
    this.rand = new Random(this.seed);
    this.hallsize = Prefs.getRandomHallSize();
    this.roomsize = Prefs.getRandomRoomSize();
    this.fill();
  }

  // Static methods
  public static int getMaxFloors() {
    return LayeredTower.MAX_FLOORS;
  }

  public static int getMaxColumns() {
    return LayeredTower.MAX_COLUMNS;
  }

  public static int getMaxRows() {
    return LayeredTower.MAX_ROWS;
  }

  // Methods
  public boolean hasMonster(final int x, final int y, final int z) {
    return this.monsterData.getCell(y, x, z);
  }

  public void addMonster(final int x, final int y, final int z) {
    if (!this.monsterData.getCell(y, x, z)) {
      this.monsterData.setCell(true, y, x, z);
    }
  }

  public void removeMonster(final int x, final int y, final int z) {
    if (this.monsterData.getCell(y, x, z)) {
      this.monsterData.setCell(false, y, x, z);
    }
  }

  private boolean checkMoveMonster(final int locX, final int locY,
      final int locZ, final int moveX, final int moveY) {
    if (this.monsterData.getCell(locY, locX, locZ)
        && !this.monsterData.getCell(locY + moveY, locX + moveX, locZ)) {
      return true;
    }
    return false;
  }

  private void moveMonster(final int locX, final int locY, final int locZ,
      final int moveX, final int moveY) {
    if (this.checkMoveMonster(locX, locY, locZ, moveX, moveY)) {
      this.monsterData.setCell(false, locY, locX, locZ);
      this.monsterData.setCell(true, locY + moveY, locX + moveX, locZ);
    }
  }

  public boolean checkForBattle(final int px, final int py, final int pz) {
    // If the player is now standing on a monster...
    if (this.hasMonster(px, py, pz)) {
      // ... and we aren't already in battle...
      final BagOStuff bag = FantastleReboot.getBagOStuff();
      if (!Modes.inBattle()) {
        // ... start a battle with that monster!
        Game.stopMovement();
        bag.getBattle().doBattle(px, py);
        return true;
      }
    }
    return false;
  }

  public void moveAllMonsters(final World world) {
    int locX, locY;
    final int rows = this.monsterData.getShape()[1] - 1;
    final int cols = this.monsterData.getShape()[0] - 1;
    final int pz = world.getPlayerLocationZ();
    // Tick all object timers
    for (locX = 0; locX < cols; locX++) {
      for (locY = 0; locY < rows; locY++) {
        final int moveX = RandomRange.generate(-1, 1);
        final int moveY = RandomRange.generate(-1, 1);
        if (this.hasMonster(locX, locY, pz)) {
          if (locX + moveX >= 0 && locX + moveX < cols && locY + moveY >= 0
              && locY + moveY < rows) {
            final FantastleObjectModel there = world.getCell(locX + moveX,
                locY + moveY, pz, Layers.OBJECT);
            final boolean checkMove = this.checkMoveMonster(locX, locY, pz,
                moveX, moveY);
            if (!there.isSolid() && checkMove) {
              // Move the monster
              this.moveMonster(locX, locY, pz, moveX, moveY);
            }
          }
        }
      }
    }
  }

  public void postBattle(final World world, final int locX, final int locY) {
    // Clear the monster just defeated
    final int pz = world.getPlayerLocationZ();
    this.removeMonster(locX, locY, pz);
    // Generate a new monster
    final int rows = this.monsterData.getShape()[1] - 1;
    final int cols = this.monsterData.getShape()[0] - 1;
    final RandomRange row = new RandomRange(0, rows);
    final RandomRange column = new RandomRange(0, cols);
    int genX = row.generate();
    int genY = column.generate();
    FantastleObjectModel currObj = world.getCell(genX, genY, pz, Layers.OBJECT);
    if (!currObj.isSolid() && !this.hasMonster(genX, genY, pz)) {
      this.addMonster(genX, genY, pz);
    } else {
      while (currObj.isSolid() || this.hasMonster(genX, genY, pz)) {
        genX = row.generate();
        genY = column.generate();
        currObj = world.getCell(genX, genY, pz, Layers.OBJECT);
      }
      this.addMonster(genX, genY, pz);
    }
  }

  public boolean hasNote(final int x, final int y, final int z) {
    return this.noteData.getNote(y, x, z) != null;
  }

  public void createNote(final int x, final int y, final int z) {
    this.noteData.setNote(new WorldNote(), y, x, z);
  }

  public WorldNote getNote(final int x, final int y, final int z) {
    return this.noteData.getNote(y, x, z);
  }

  public boolean floorRangeCheck(final int floor) {
    return floor >= 0 && floor < this.getFloors();
  }

  public boolean cellRangeCheck(final int row, final int col, final int floor) {
    int fR = row;
    int fC = col;
    final int fF = floor;
    if (this.verticalWrapEnabled) {
      fC = this.normalizeColumn(fC);
    }
    if (this.horizontalWrapEnabled) {
      fR = this.normalizeRow(fR);
    }
    return fR >= 0 && fR < this.getRows() && fC >= 0 && fC < this.getColumns()
        && fF >= 0 && fF < this.getFloors();
  }

  public boolean cellRangeCheck(final int row, final int col, final int floor,
      final int extra) {
    int fR = row;
    int fC = col;
    final int fF = floor;
    if (this.verticalWrapEnabled) {
      fC = this.normalizeColumn(fC);
    }
    if (this.horizontalWrapEnabled) {
      fR = this.normalizeRow(fR);
    }
    return fR >= 0 && fR < this.getRows() && fC >= 0 && fC < this.getColumns()
        && fF >= 0 && fF < this.getFloors() && extra >= 0
        && extra < Layers.COUNT;
  }

  public FantastleObjectModel getCell(final int row, final int col,
      final int floor, final int extra) {
    int fR = row;
    int fC = col;
    int fF = floor;
    if (this.verticalWrapEnabled) {
      fC = this.normalizeColumn(fC);
    }
    if (this.horizontalWrapEnabled) {
      fR = this.normalizeRow(fR);
    }
    if (this.floorWrapEnabled) {
      fF = this.normalizeFloor(fF);
    }
    return this.data.getCell(fC, fR, fF, extra);
  }

  public int getPlayerRow() {
    return this.playerLocationData[1];
  }

  public int getPlayerColumn() {
    return this.playerLocationData[0];
  }

  public int getPlayerFloor() {
    return this.playerLocationData[2];
  }

  public int getRows() {
    return this.data.getShape()[1];
  }

  public int getColumns() {
    return this.data.getShape()[0];
  }

  public int getFloors() {
    return this.data.getShape()[2];
  }

  public boolean doesPlayerExist() {
    boolean res = true;
    for (final int element : this.playerStartData) {
      res = res && element != -1;
    }
    return res;
  }

  public void resetVisibleSquares() {
    for (int x = 0; x < this.getRows(); x++) {
      for (int y = 0; y < this.getColumns(); y++) {
        for (int z = 0; z < this.getFloors(); z++) {
          this.visionData.setCell(false, x, y, z);
        }
      }
    }
  }

  public void updateExploredSquares(final int xp, final int yp, final int zp) {
    if ((this.visionMode | VisionModes.EXPLORE) == this.visionMode) {
      for (int x = xp - this.visionModeExploreRadius; x <= xp
          + this.visionModeExploreRadius; x++) {
        for (int y = yp - this.visionModeExploreRadius; y <= yp
            + this.visionModeExploreRadius; y++) {
          int fx, fy;
          if (this.isHorizontalWrapEnabled()) {
            fx = this.normalizeColumn(x);
          } else {
            fx = x;
          }
          if (this.isVerticalWrapEnabled()) {
            fy = this.normalizeRow(y);
          } else {
            fy = y;
          }
          boolean alreadyVisible = false;
          if (this.cellRangeCheck(fx, fy, zp)) {
            alreadyVisible = this.visionData.getCell(fx, fy, zp);
          }
          if (!alreadyVisible) {
            if ((this.visionMode
                | VisionModes.FIELD_OF_VIEW) == this.visionMode) {
              if (this.isSquareVisibleFieldOfView(x, y, xp, yp)) {
                if (this.cellRangeCheck(fx, fy, zp)) {
                  this.visionData.setCell(true, fx, fy, zp);
                }
              }
            } else {
              if (this.cellRangeCheck(fx, fy, zp)) {
                this.visionData.setCell(true, fx, fy, zp);
              }
            }
          }
        }
      }
    }
  }

  public boolean isSquareVisible(final int x1, final int y1, final int x2,
      final int y2) {
    if (this.visionMode == VisionModes.NONE) {
      return true;
    } else {
      boolean result = this.isSquareVisibleFixedRadius(x1, y1, x2, y2);
      if (this.areCoordsInBounds(x1, y1, x2, y2)) {
        if (result && (this.visionMode
            | VisionModes.FIELD_OF_VIEW) == this.visionMode) {
          result = result && this.isSquareVisibleFieldOfView(x1, y1, x2, y2);
        }
        if ((this.visionMode | VisionModes.EXPLORE) == this.visionMode) {
          result = result && this.isSquareVisibleExplore(x2, y2);
        }
      }
      return result;
    }
  }

  private boolean areCoordsInBounds(final int x1, final int y1, final int x2,
      final int y2) {
    int fx1, fx2, fy1, fy2;
    if (this.isHorizontalWrapEnabled()) {
      fx1 = this.normalizeColumn(x1);
      fx2 = this.normalizeColumn(x2);
    } else {
      fx1 = x1;
      fx2 = x2;
    }
    if (this.isVerticalWrapEnabled()) {
      fy1 = this.normalizeRow(y1);
      fy2 = this.normalizeRow(y2);
    } else {
      fy1 = y1;
      fy2 = y2;
    }
    return fx1 >= -1 && fx1 <= this.getRows() && fx2 >= -1
        && fx2 <= this.getRows() && fy1 >= -1 && fy1 <= this.getColumns()
        && fy2 >= -1 && fy2 <= this.getColumns();
  }

  private boolean isSquareVisibleExplore(final int x2, final int y2) {
    final int zLoc = this.getPlayerFloor();
    int fx2, fy2;
    if (this.isHorizontalWrapEnabled()) {
      fx2 = this.normalizeColumn(x2);
    } else {
      fx2 = x2;
    }
    if (this.isVerticalWrapEnabled()) {
      fy2 = this.normalizeRow(y2);
    } else {
      fy2 = y2;
    }
    if (this.cellRangeCheck(fx2, fy2, zLoc)) {
      return this.visionData.getCell(fx2, fy2, zLoc);
    } else {
      return true;
    }
  }

  private boolean isSquareVisibleFieldOfView(final int x1, final int y1,
      final int x2, final int y2) {
    int fx1, fx2, fy1, fy2;
    fx1 = x1;
    fx2 = x2;
    fy1 = y1;
    fy2 = y2;
    final int zLoc = this.getPlayerFloor();
    final int dx = Math.abs(fx2 - fx1);
    final int dy = Math.abs(fy2 - fy1);
    int sx, sy;
    if (fx1 < fx2) {
      sx = 1;
    } else {
      sx = -1;
    }
    if (fy1 < fy2) {
      sy = 1;
    } else {
      sy = -1;
    }
    int err = dx - dy;
    int e2;
    do {
      if (fx1 == fx2 && fy1 == fy2) {
        break;
      }
      // Does object block FoV?
      if (this.cellRangeCheck(fx1, fy1, zLoc)) {
        final FantastleObjectModel obj = this.getCell(fx1, fy1, zLoc,
            Layers.OBJECT);
        if (obj.isSightBlocking()) {
          // This object blocks FoV
          if (fx1 != x1 || fy1 != y1) {
            return false;
          }
        }
      } else {
        // Void blocks FoV
        return false;
      }
      e2 = 2 * err;
      if (e2 > -dy) {
        err = err - dy;
        fx1 = fx1 + sx;
      }
      if (e2 < dx) {
        err = err + dx;
        fy1 = fy1 + sy;
      }
    } while (true);
    // No objects block FoV
    return true;
  }

  private boolean isSquareVisibleFixedRadius(final int x1, final int y1,
      final int x2, final int y2) {
    int fx1, fy1, fx2, fy2;
    if (this.isHorizontalWrapEnabled()) {
      fx1 = this.normalizeColumn(x1);
      fx2 = this.normalizeColumn(x2);
    } else {
      fx1 = x1;
      fx2 = x2;
    }
    if (this.isVerticalWrapEnabled()) {
      fy1 = this.normalizeRow(y1);
      fy2 = this.normalizeRow(y2);
    } else {
      fy1 = y1;
      fy2 = y2;
    }
    final int zLoc = this.getPlayerFloor();
    if (this.cellRangeCheck(fx1, fy1, zLoc)
        && this.cellRangeCheck(fx2, fy2, zLoc)) {
      return LayeredTower.radialScan(fx2, fy2, this.visionRadius, fx1, fy1);
    } else {
      return true;
    }
  }

  public void setCell(final FantastleObjectModel mo, final int row,
      final int col, final int floor, final int extra) {
    int fR = row;
    int fC = col;
    int fF = floor;
    if (this.verticalWrapEnabled) {
      fC = this.normalizeColumn(fC);
    }
    if (this.horizontalWrapEnabled) {
      fR = this.normalizeRow(fR);
    }
    if (this.floorWrapEnabled) {
      fF = this.normalizeFloor(fF);
    }
    this.data.setCell(mo, fC, fR, fF, extra);
  }

  public void savePlayerLocation() {
    System.arraycopy(this.playerLocationData, 0, this.savedPlayerLocationData,
        0, this.playerLocationData.length);
  }

  public void restorePlayerLocation() {
    System.arraycopy(this.savedPlayerLocationData, 0, this.playerLocationData,
        0, this.playerLocationData.length);
  }

  public void setPlayerToStart() {
    System.arraycopy(this.playerStartData, 0, this.playerLocationData, 0,
        this.playerStartData.length);
  }

  public void setStartRow(final int newStartRow) {
    this.playerStartData[1] = newStartRow;
  }

  public void setStartColumn(final int newStartColumn) {
    this.playerStartData[0] = newStartColumn;
  }

  public void setStartFloor(final int newStartFloor) {
    this.playerStartData[2] = newStartFloor;
  }

  public void setPlayerRow(final int newPlayerRow) {
    this.playerLocationData[1] = newPlayerRow;
  }

  public void setPlayerColumn(final int newPlayerColumn) {
    this.playerLocationData[0] = newPlayerColumn;
  }

  public void setPlayerFloor(final int newPlayerFloor) {
    this.playerLocationData[2] = newPlayerFloor;
  }

  public void offsetPlayerRow(final int newPlayerRow) {
    this.playerLocationData[1] += newPlayerRow;
  }

  public void offsetPlayerColumn(final int newPlayerColumn) {
    this.playerLocationData[0] += newPlayerColumn;
  }

  private final void fill() {
    final FantastleObjectModel bottom = Prefs.getEditorDefaultFill();
    final FantastleObjectModel top = new OpenSpace();
    for (int z = 0; z < this.getFloors(); z++) {
      this.fillFloor(bottom, top, z);
    }
  }

  private final void fillFloor(final FantastleObjectModel bottom,
      final FantastleObjectModel top, final int z) {
    int x, y, e;
    for (x = 0; x < this.getColumns(); x++) {
      for (y = 0; y < this.getRows(); y++) {
        for (e = 0; e < Layers.COUNT; e++) {
          if (e == Layers.GROUND) {
            this.setCell(bottom, y, x, z, e);
            if (this.savedTowerState.getCell(y, x, z, e) == null) {
              this.savedTowerState.setCell(bottom, y, x, z, e);
            }
          } else {
            this.setCell(top, y, x, z, e);
            if (this.savedTowerState.getCell(y, x, z, e) == null) {
              this.savedTowerState.setCell(top, y, x, z, e);
            }
          }
        }
      }
    }
  }

  void fillRandomlyConstrained(final World world, final int w) {
    for (int z = 0; z < this.getFloors(); z++) {
      this.fillFloorRandomlyConstrained(world, z, w);
    }
  }

  private void fillFloorRandomlyConstrained(final World world, final int z,
      final int w) {
    // Fill the world with the "constrained" algorithm
    final RandomRange row = new RandomRange(0, this.getRows() - 1);
    final RandomRange column = new RandomRange(0, this.getColumns() - 1);
    RandomRange r = null;
    int x, y, e;
    // Pass 1
    final int columns = this.getColumns();
    final int rows = this.getRows();
    for (e = 0; e < Layers.COUNT; e++) {
      final FantastleObjectModel[] objectsWithoutPrerequisites = GameObjects
          .getAllWithoutPrerequisiteAndNotRequired(e);
      if (objectsWithoutPrerequisites != null) {
        r = new RandomRange(0, objectsWithoutPrerequisites.length - 1);
        for (x = 0; x < columns; x++) {
          for (y = 0; y < rows; y++) {
            final FantastleObjectModel placeObj = objectsWithoutPrerequisites[r
                .generate()];
            final boolean okay = placeObj.shouldGenerateObject(world, x, y, z,
                w, e);
            if (okay) {
              this.setCell(
                  GameObjects.getNewInstanceByUniqueID(placeObj.getUniqueID()),
                  y, x, z, e);
            }
          }
        }
      }
    }
    // Pass 2
    for (int layer = 0; layer < Layers.COUNT; layer++) {
      final FantastleObjectModel[] requiredObjects = GameObjects
          .getAllRequired(layer);
      if (requiredObjects != null) {
        int randomColumn, randomRow;
        for (x = 0; x < requiredObjects.length; x++) {
          final FantastleObjectModel currObj = requiredObjects[x];
          final int min = currObj.getMinimumRequiredQuantity(world);
          int max = currObj.getMaximumRequiredQuantity(world);
          if (max == RandomGenerationRule.NO_LIMIT) {
            // Maximum undefined, so define it relative to this world
            max = this.getRows() * this.getColumns() / 10;
            // Make sure max is valid
            if (max < min) {
              max = min;
            }
          }
          final RandomRange howMany = new RandomRange(min, max);
          final int generateHowMany = howMany.generate();
          for (y = 0; y < generateHowMany; y++) {
            randomRow = row.generate();
            randomColumn = column.generate();
            if (currObj.shouldGenerateObject(world, randomRow, randomColumn, z,
                w, layer)) {
              this.setCell(
                  GameObjects.getNewInstanceByUniqueID(currObj.getUniqueID()),
                  randomColumn, randomRow, z, layer);
            } else {
              while (!currObj.shouldGenerateObject(world, randomColumn,
                  randomRow, z, w, layer)) {
                randomRow = row.generate();
                randomColumn = column.generate();
              }
              this.setCell(
                  GameObjects.getNewInstanceByUniqueID(currObj.getUniqueID()),
                  randomColumn, randomRow, z, layer);
            }
          }
        }
      }
    }
    // Pass 3: Add monsters
    this.addMonstersRandomly(z);
  }

  void fillRandomlyTwister(final World world, final int w) {
    for (int z = 0; z < this.getFloors(); z++) {
      this.fillFloorRandomlyTwister(world, z, w);
    }
  }

  private void fillFloorRandomlyTwister(final World world, final int z,
      final int w) {
    // Fill the world with the "twister" algorithm
    this.tiles = new String[this.size.width][this.size.height];
    // Fill the dungeon with wall tiles
    rectFill(0, 0, this.size.width, this.size.height, "#");
    // Create a starting room
    startingRoom(w);
    // Generate halls/rooms until no prospects remain
    while (hasProspects()) {
      hallsGenerate();
      roomsGenerate();
    }
    // Do some extra work for the ending room
    endingRoom(w);
    // Finalize the dungeon
    cleanUp(z);
    // Add monsters
    this.addMonstersRandomly(z);
  }

  private void rectFill(int x, int y, int w, int h, String tile) {
    for (int ya = y; ya < y + h; ya++) {
      for (int xa = x; xa < x + w; xa++) {
        this.tiles[xa][ya] = tile;
      }
    }
  }

  private void startingRoom(final int w) {
    int center = this.size.width / 2;
    rectFill(center - 2, 1, 3, 3, "R");
    placeIfWall(center - 1, 4, "P");
    if (w != 0) {
      placeIfEmpty(center, 2, "s");
    }
  }

  private void endingRoom(final int w) {
    if (w != World.getMaxLevels() - 1) {
      try {
        placeIfEmpty(this.lastRoomX + this.lastRoomW - 2,
            this.lastRoomY + this.lastRoomH - 2, "S");
      } catch (ArrayIndexOutOfBoundsException e) {
        placeIfEmpty(this.lastRoomX + 1, this.lastRoomY + 1, "S");
      }
    }
  }

  private boolean rectCheck(int x, int y, int w, int h) {
    for (int ya = y; ya < y + h; ya++) {
      for (int xa = x; xa < x + w; xa++) {
        String tile;
        try {
          tile = this.tiles[xa][ya];
        } catch (ArrayIndexOutOfBoundsException e) {
          return false;
        }
        if (!"P".equals(tile) && !"#".equals(tile)) {
          return false;
        }
      }
    }
    return true;
  }

  private void placeIfWall(int x, int y, String tile) {
    if ("#".equals(this.tiles[x][y])) {
      this.tiles[x][y] = tile;
    }
  }

  private void placeIfEmpty(int x, int y, String tile) {
    if (isEmpty(this.tiles[x][y])) {
      this.tiles[x][y] = tile;
    }
  }

  private static boolean isEmpty(String string) {
    if (" ".equals(string)) {
      return true;
    }
    if ("H".equals(string)) {
      return true;
    }
    return "R".equals(string);
  }

  private boolean hasProspects() {
    for (int y = 0; y < this.size.height; y++) {
      for (int x = 0; x < this.size.width; x++) {
        if ("P".equals(this.tiles[x][y])) {
          return true;
        }
      }
    }
    return false;
  }

  private List<Point> getProspects() {
    ArrayList<Point> prospects = new ArrayList<>();
    for (int y = 0; y < this.size.height; y++) {
      for (int x = 0; x < this.size.width; x++) {
        if ("P".equals(this.tiles[x][y])) {
          prospects.add(new Point(x, y));
        }
      }
    }
    return prospects;
  }

  private void hallsGenerate() {
    getProspects().stream().forEach((p) -> {
      try {
        hallMake(p.x, p.y);
      } catch (ArrayIndexOutOfBoundsException e) {
        this.tiles[p.x][p.y] = "#";
      }
    });
  }

  private void hallMake(int x, int y) {
    if (isEmpty(this.tiles[x - 1][y]) && isEmpty(this.tiles[x + 1][y])) {
      this.tiles[x][y] = "H";
      return;
    }
    if (isEmpty(this.tiles[x][y - 1]) && isEmpty(this.tiles[x][y + 1])) {
      this.tiles[x][y] = "H";
      return;
    }
    if (isEmpty(this.tiles[x - 1][y])) {
      hallMakeEastbound(x, y, this.rand.nextInt(this.hallsize));
      return;
    }
    if (isEmpty(this.tiles[x + 1][y])) {
      hallMakeWestbound(x, y, this.rand.nextInt(this.hallsize));
      return;
    }
    if (isEmpty(this.tiles[x][y - 1])) {
      hallMakeSouthbound(x, y, this.rand.nextInt(this.hallsize));
      return;
    }
    if (isEmpty(this.tiles[x][y + 1])) {
      hallMakeNorthbound(x, y, this.rand.nextInt(this.hallsize));
      return;
    }
    this.tiles[x][y] = "R";
  }

  private void hallMakeEastbound(int x, int y, int length) {
    if (rectCheck(x, y - 1, length, 3)) {
      rectFill(x, y, length, 1, "H");
      placeIfEmpty(x + (length - 1), y, "P");
    } else {
      this.tiles[x][y] = "#";
    }
  }

  private void hallMakeWestbound(int x, int y, int length) {
    if (rectCheck(x - length, y - 1, length, 3)) {
      rectFill(x - (length - 1), y, length, 1, "H");
      placeIfEmpty(x - (length - 1), y, "P");
      if (length >= 3) {
        int sbranch = this.rand.nextInt(this.hallsize);
        if (sbranch > 1 && sbranch < length - 1) {
          placeIfEmpty(x - sbranch, y + 1, "P");
          hallMakeSouthbound(x - sbranch, y + 1,
              this.rand.nextInt(this.hallsize));
        }
        int nbranch = this.rand.nextInt(this.hallsize);
        if (nbranch > 1 && nbranch < length - 1) {
          placeIfEmpty(x - nbranch, y - 1, "P");
          hallMakeNorthbound(x - nbranch, y - 1,
              this.rand.nextInt(this.hallsize));
        }
      }
    } else {
      if (length > 0) {
        hallMakeWestbound(x, y, length - 1);
      } else {
        this.tiles[x][y] = "#";
      }
    }
  }

  private void hallMakeSouthbound(int x, int y, int length) {
    if (rectCheck(x - 1, y, 3, length + 1)) {
      rectFill(x, y, 1, length, "H");
      this.tiles[x][y + (length - 1)] = "P";
    } else {
      this.tiles[x][y] = "#";
    }
  }

  private void hallMakeNorthbound(int x, int y, int length) {
    if (rectCheck(x - 1, y - length, 3, length + 1)) {
      rectFill(x, y - (length - 1), 1, length, "H");
      this.tiles[x][y - (length - 1)] = "P";
    } else {
      this.tiles[x][y] = "#";
    }
  }

  private void roomsGenerate() {
    getProspects().stream().forEach((p) -> {
      try {
        roomMake(p.x, p.y);
      } catch (ArrayIndexOutOfBoundsException e) {
      }
    });
  }

  private void roomMake(int x, int y) {
    if (isEmpty(this.tiles[x - 1][y]) && isEmpty(this.tiles[x + 1][y])) {
      this.tiles[x][y] = "R";
      return;
    }
    if (isEmpty(this.tiles[x][y - 1]) && isEmpty(this.tiles[x][y + 1])) {
      this.tiles[x][y] = "R";
      return;
    }
    int w = 3 + this.rand.nextInt(this.roomsize);
    int h = 3 + this.rand.nextInt(this.roomsize);
    if (isEmpty(this.tiles[x][y - 1])) {
      roomMakeSouthbound(x, y, w, h);
      this.lastRoomX = x;
      this.lastRoomY = y;
      this.lastRoomW = w;
      this.lastRoomH = h;
      return;
    }
    if (isEmpty(this.tiles[x - 1][y])) {
      roomMakeEastbound(x, y, w, h);
      this.lastRoomX = x;
      this.lastRoomY = y;
      this.lastRoomW = w;
      this.lastRoomH = h;
      return;
    }
    if (isEmpty(this.tiles[x + 1][y])) {
      roomMakeWestbound(x, y, w, h);
      this.lastRoomX = x;
      this.lastRoomY = y;
      this.lastRoomW = w;
      this.lastRoomH = h;
      return;
    }
    if (isEmpty(this.tiles[x][y + 1])) {
      roomMakeNorthbound(x, y, w, h);
      this.lastRoomX = x;
      this.lastRoomY = y;
      this.lastRoomW = w;
      this.lastRoomH = h;
      return;
    }
    this.tiles[x][y] = "#";
  }

  private void roomMakeSouthbound(int x, int y, int w, int h) {
    int wc = w / 2;
    int hc = h / 2;
    int xorig = x - wc;
    int yorig = y + 1;
    if (rectCheck(xorig - 1, y, w + 1, h + 1)) {
      this.tiles[x][y] = "H";
      rectFill(xorig, yorig, w, h, "R");
      placeIfWall(xorig + wc, yorig + h, "P");
      placeIfWall(xorig - 1, yorig + hc, "P");
      placeIfWall(xorig + w, yorig + hc, "P");
    } else {
      this.tiles[x][y] = "#";
    }
  }

  private void roomMakeEastbound(int x, int y, int w, int h) {
    int wc = w / 2;
    int hc = h / 2;
    int xorig = x + 1;
    int yorig = y - hc;
    if (rectCheck(xorig, yorig - 2, w + 1, h + 1)) {
      this.tiles[x][y] = "H";
      rectFill(xorig, yorig, w, h, "R");
      placeIfWall(xorig + wc, yorig - 1, "P");
      placeIfWall(xorig + wc, y + hc, "P");
      placeIfWall(xorig + w, y, "P");
    } else {
      if (w > 3 && h > 3) {
        roomMakeEastbound(x, y, w - 1, h - 1);
      } else {
        this.tiles[x][y] = "#";
      }
    }
  }

  private void roomMakeWestbound(int x, int y, int w, int h) {
    int hc = h / 2;
    int wc = w / 2;
    int xorig = x - w;
    int yorig = y - hc;
    if (rectCheck(xorig - 1, yorig - 1, w + 1, h + 1)) {
      this.tiles[x][y] = "H";
      rectFill(xorig, yorig, w, h, "R");
      placeIfWall(xorig + wc, yorig - 1, "P");
      placeIfWall(xorig + wc, y + hc, "P");
      placeIfWall(xorig - 1, y, "P");
    } else {
      this.tiles[x][y] = "#";
    }
  }

  private void roomMakeNorthbound(int x, int y, int w, int h) {
    int wc = w / 2;
    int hc = h / 2;
    int xorig = x - wc;
    int yorig = y - h;
    if (rectCheck(xorig - 1, yorig - 1, w + 1, h + 1)) {
      this.tiles[x][y] = "H";
      rectFill(xorig, yorig, w, h, "R");
      placeIfWall(x, yorig - 1, "P");
      placeIfWall(xorig - 1, y - hc, "P");
      placeIfWall(xorig + w, y - hc, "P");
    } else {
      this.tiles[x][y] = "#";
    }
  }

  private void cleanUp(final int z) {
    getProspects().stream().forEach((p) -> {
      this.tiles[p.x][p.y] = "#";
    });
    fixOneBlockDeadEnds();
    fixOneBlockHalls();
    makeDoors();
    // Convert tiles to dungeon data
    convertTilestoObjects(z);
  }

  private void fixOneBlockDeadEnds() {
    for (int x = 0; x < this.size.width; x++) {
      for (int y = 0; y < this.size.height; y++) {
        String tile = this.tiles[x][y];
        if ("H".equals(tile)) {
          // Westbound
          if ("R".equals(this.tiles[x + 1][y])
              && "#".equals(this.tiles[x - 1][y])) {
            this.tiles[x][y] = "#";
          }
          // Eastbound
          if ("R".equals(this.tiles[x - 1][y])
              && "#".equals(this.tiles[x + 1][y])) {
            this.tiles[x][y] = "#";
          }
          // Southbound
          if ("R".equals(this.tiles[x][y - 1])
              && "#".equals(this.tiles[x][y + 1])) {
            this.tiles[x][y] = "#";
          }
          // Northbound
          if ("R".equals(this.tiles[x][y + 1])
              && "#".equals(this.tiles[x][y - 1])) {
            this.tiles[x][y] = "#";
          }
        }
      }
    }
  }

  private void fixOneBlockHalls() {
    for (int x = 0; x < this.size.width; x++) {
      for (int y = 0; y < this.size.height; y++) {
        String tile = this.tiles[x][y];
        if ("H".equals(tile)) {
          // Westbound
          if ("R".equals(this.tiles[x + 1][y])
              && "R".equals(this.tiles[x - 1][y])) {
            this.tiles[x][y] = "D";
          }
          // Eastbound
          if ("R".equals(this.tiles[x - 1][y])
              && "R".equals(this.tiles[x + 1][y])) {
            this.tiles[x][y] = "D";
          }
          // Southbound
          if ("R".equals(this.tiles[x][y - 1])
              && "R".equals(this.tiles[x][y + 1])) {
            this.tiles[x][y] = "D";
          }
          // Northbound
          if ("R".equals(this.tiles[x][y + 1])
              && "R".equals(this.tiles[x][y - 1])) {
            this.tiles[x][y] = "D";
          }
        }
      }
    }
  }

  private void makeDoors() {
    for (int x = 0; x < this.size.width; x++) {
      for (int y = 0; y < this.size.height; y++) {
        String tile = this.tiles[x][y];
        if ("H".equals(tile)) {
          // Southbound
          if ("R".equals(this.tiles[x][y - 1])
              && "H".equals(this.tiles[x][y + 1])) {
            this.tiles[x][y] = "D";
          }
          // Northbound
          if ("R".equals(this.tiles[x][y + 1])
              && "H".equals(this.tiles[x][y - 1])) {
            this.tiles[x][y] = "D";
          }
          // Westbound
          if ("R".equals(this.tiles[x + 1][y])
              && "H".equals(this.tiles[x - 1][y])) {
            this.tiles[x][y] = "D";
          }
          // Eastbound
          if ("R".equals(this.tiles[x - 1][y])
              && "H".equals(this.tiles[x + 1][y])) {
            this.tiles[x][y] = "D";
          }
        }
      }
    }
  }

  private void convertTilestoObjects(final int z) {
    OpenSpace open = new OpenSpace();
    FantastleObjectModel[] roomReplacers = new FantastleObjectModel[] {
        new ArmorShop(), new BankShop(), new BonusShop(), new ElementalShop(),
        new HealShop(), new ItemShop(), new NecklaceShop(),
        new RegenerateShop(), new SpellShop(), new WeaponShop(), new Pit(),
        new SuperPit(), new Spring(), new SuperSpring(), open, open, open, open,
        open, open, open, open, open, open, open, open, open, open, open, open,
        open, open, open, open, open, open, open, open, open, open, open,
        open };
    for (int x = 0; x < this.size.width; x++) {
      for (int y = 0; y < this.size.height; y++) {
        String tile = this.tiles[x][y];
        if ("#".equals(tile)) {
          // Convert # to walls
          this.setCell(new Wall(), y, x, z, Layers.OBJECT);
        } else if ("D".equals(tile)) {
          // Convert D to closed doors
          this.setCell(new ClosedDoor(), y, x, z, Layers.OBJECT);
        } else if ("s".equals(tile)) {
          // Convert s to stairs up
          this.setCell(new StairsUp(), y, x, z, Layers.OBJECT);
        } else if ("S".equals(tile)) {
          // Convert S to stairs down
          this.setCell(new StairsDown(), y, x, z, Layers.OBJECT);
        } else if ("R".equals(tile)) {
          // Convert R to a random mixture
          int rIndex = RandomRange.generate(0, roomReplacers.length - 1);
          this.setCell(roomReplacers[rIndex], y, x, z, Layers.OBJECT);
        } else {
          // Convert anything else to open space
          this.setCell(open, y, x, z, Layers.OBJECT);
        }
      }
    }
  }

  private void addMonstersRandomly(final int z) {
    final RandomRange row = new RandomRange(0, this.getRows() - 1);
    final RandomRange column = new RandomRange(0, this.getColumns() - 1);
    final int space = this.getColumns() * this.getRows();
    final int monsterMin = space / 32;
    final int monsterMax = space / 16;
    final RandomRange howMany = new RandomRange(monsterMin, monsterMax);
    final int generateHowMany = howMany.generate();
    for (int y = 0; y < generateHowMany; y++) {
      int xLoc = row.generate();
      int yLoc = column.generate();
      FantastleObjectModel there = this.getCell(xLoc, yLoc, z, Layers.OBJECT);
      if (!there.isSolid() && !this.hasMonster(xLoc, yLoc, z)) {
        this.addMonster(xLoc, yLoc, z);
      } else {
        while (there.isSolid() || this.hasMonster(xLoc, yLoc, z)) {
          xLoc = row.generate();
          yLoc = column.generate();
          there = this.getCell(xLoc, yLoc, z, Layers.OBJECT);
        }
        this.addMonster(xLoc, yLoc, z);
      }
    }
  }

  public void save() {
    int y, x, z, e;
    for (x = 0; x < this.getColumns(); x++) {
      for (y = 0; y < this.getRows(); y++) {
        for (z = 0; z < this.getFloors(); z++) {
          for (e = 0; e < Layers.COUNT; e++) {
            this.savedTowerState.setCell(this.getCell(y, x, z, e), x, y, z, e);
          }
        }
      }
    }
  }

  public void restore() {
    int y, x, z, e;
    for (x = 0; x < this.getColumns(); x++) {
      for (y = 0; y < this.getRows(); y++) {
        for (z = 0; z < this.getFloors(); z++) {
          for (e = 0; e < Layers.COUNT; e++) {
            this.setCell(this.savedTowerState.getCell(x, y, z, e), y, x, z, e);
          }
        }
      }
    }
  }

  private int normalizeRow(final int row) {
    int fR = row;
    int rows = this.getRows();
    if (fR < 0) {
      fR += rows;
      while (fR < 0) {
        fR += rows;
      }
    } else if (fR > rows - 1) {
      fR -= rows;
      while (fR > rows - 1) {
        fR -= rows;
      }
    }
    return fR;
  }

  private int normalizeColumn(final int column) {
    int fC = column;
    int columns = this.getColumns();
    if (fC < 0) {
      fC += columns;
      while (fC < 0) {
        fC += columns;
      }
    } else if (fC > columns - 1) {
      fC -= columns;
      while (fC > columns - 1) {
        fC -= columns;
      }
    }
    return fC;
  }

  private int normalizeFloor(final int floor) {
    int fF = floor;
    int floors = this.getFloors();
    if (fF < 0) {
      fF += floors;
      while (fF < 0) {
        fF += floors;
      }
    } else if (fF > floors - 1) {
      fF -= floors;
      while (fF > floors - 1) {
        fF -= floors;
      }
    }
    return fF;
  }

  public void tickTimers(final int floor) {
    int x, y;
    // Tick all object timers
    for (x = 0; x < this.getColumns(); x++) {
      for (y = 0; y < this.getRows(); y++) {
        final FantastleObjectModel obj = this.getCell(y, x, floor,
            Layers.OBJECT);
        obj.tickTimer();
      }
    }
  }

  public static boolean radialScan(final int cx, final int cy, final int r,
      final int tx, final int ty) {
    return Math.abs(tx - cx) <= r && Math.abs(ty - cy) <= r;
  }

  // Settings
  public boolean isHorizontalWrapEnabled() {
    return this.horizontalWrapEnabled;
  }

  public boolean isVerticalWrapEnabled() {
    return this.verticalWrapEnabled;
  }

  public boolean isFloorWrapEnabled() {
    return this.floorWrapEnabled;
  }

  public void setHorizontalWrapEnabled(final boolean value) {
    this.horizontalWrapEnabled = value;
  }

  public void setVerticalWrapEnabled(final boolean value) {
    this.verticalWrapEnabled = value;
  }

  public void setFloorWrapEnabled(final boolean value) {
    this.floorWrapEnabled = value;
  }

  public int getVisionRadius() {
    return this.visionRadius;
  }

  public void setVisionRadius(final int value) {
    this.visionRadius = value;
  }

  public int getExploreExpansionRadius() {
    return this.visionModeExploreRadius;
  }

  public void setExploreExpansionRadius(final int value) {
    this.visionModeExploreRadius = value;
  }

  public boolean isVisionModeNone() {
    return this.visionMode == VisionModes.NONE;
  }

  public boolean isVisionModeExplore() {
    return (this.visionMode | VisionModes.EXPLORE) == this.visionMode;
  }

  public boolean isVisionModeFieldOfView() {
    return (this.visionMode | VisionModes.FIELD_OF_VIEW) == this.visionMode;
  }

  public boolean isVisionModeFixedRadius() {
    return (this.visionMode | VisionModes.FIXED_RADIUS) == this.visionMode;
  }

  public void resetVisionModeNone() {
    this.visionMode = VisionModes.NONE;
  }

  public void addVisionModeExplore() {
    this.removeVisionModeFixedRadius();
    if (!this.isVisionModeExplore()) {
      this.visionMode += VisionModes.EXPLORE;
    }
  }

  public void addVisionModeFieldOfView() {
    if (!this.isVisionModeFieldOfView()) {
      this.visionMode += VisionModes.FIELD_OF_VIEW;
    }
  }

  public void addVisionModeFixedRadius() {
    this.removeVisionModeExplore();
    if (!this.isVisionModeFixedRadius()) {
      this.visionMode += VisionModes.FIXED_RADIUS;
    }
  }

  public void removeVisionModeExplore() {
    if (this.isVisionModeExplore()) {
      this.visionMode -= VisionModes.EXPLORE;
    }
  }

  public void removeVisionModeFieldOfView() {
    if (this.isVisionModeFieldOfView()) {
      this.visionMode -= VisionModes.FIELD_OF_VIEW;
    }
  }

  public void removeVisionModeFixedRadius() {
    if (this.isVisionModeFixedRadius()) {
      this.visionMode -= VisionModes.FIXED_RADIUS;
    }
  }

  // File I/O
  public void writeLayeredTower(final XDataWriter writer) throws IOException {
    int y, x, z, e;
    writer.writeInt(this.getColumns());
    writer.writeInt(this.getRows());
    writer.writeInt(this.getFloors());
    for (x = 0; x < this.getColumns(); x++) {
      for (y = 0; y < this.getRows(); y++) {
        for (z = 0; z < this.getFloors(); z++) {
          for (e = 0; e < Layers.COUNT; e++) {
            this.getCell(y, x, z, e).writeObject(writer);
          }
          writer.writeBoolean(this.visionData.getCell(y, x, z));
          final boolean hasNote = this.noteData.getNote(y, x, z) != null;
          writer.writeBoolean(hasNote);
          if (hasNote) {
            this.noteData.getNote(y, x, z).writeNote(writer);
          }
        }
      }
    }
    for (y = 0; y < 3; y++) {
      writer.writeInt(this.playerStartData[y]);
      writer.writeInt(this.playerLocationData[y]);
      writer.writeInt(this.savedPlayerLocationData[y]);
      writer.writeInt(this.findResult[y]);
    }
    writer.writeBoolean(this.horizontalWrapEnabled);
    writer.writeBoolean(this.verticalWrapEnabled);
    writer.writeBoolean(this.floorWrapEnabled);
    writer.writeInt(this.visionMode);
    writer.writeInt(this.visionModeExploreRadius);
    writer.writeInt(this.visionRadius);
  }

  public static LayeredTower readLayeredTowerV1(final XDataReader reader)
      throws IOException {
    int y, x, z, e, worldSizeX, worldSizeY, worldSizeZ;
    worldSizeX = reader.readInt();
    worldSizeY = reader.readInt();
    worldSizeZ = reader.readInt();
    final LayeredTower lt = new LayeredTower(worldSizeX, worldSizeY,
        worldSizeZ);
    for (x = 0; x < lt.getColumns(); x++) {
      for (y = 0; y < lt.getRows(); y++) {
        for (z = 0; z < lt.getFloors(); z++) {
          for (e = 0; e < Layers.COUNT; e++) {
            lt.setCell(GameObjects.readObject(reader, WorldVersions.LATEST), y,
                x, z, e);
            if (lt.getCell(y, x, z, e) == null) {
              return null;
            }
          }
          lt.visionData.setCell(reader.readBoolean(), y, x, z);
          final boolean hasNote = reader.readBoolean();
          if (hasNote) {
            final WorldNote mn = WorldNote.readNote(reader);
            lt.noteData.setNote(mn, y, x, z);
          }
        }
      }
    }
    for (y = 0; y < 3; y++) {
      lt.playerStartData[y] = reader.readInt();
      lt.playerLocationData[y] = reader.readInt();
      lt.savedPlayerLocationData[y] = reader.readInt();
      lt.findResult[y] = reader.readInt();
    }
    lt.horizontalWrapEnabled = reader.readBoolean();
    lt.verticalWrapEnabled = reader.readBoolean();
    lt.floorWrapEnabled = reader.readBoolean();
    lt.visionMode = reader.readInt();
    lt.visionModeExploreRadius = reader.readInt();
    lt.visionRadius = reader.readInt();
    return lt;
  }

  public void writeSavedTowerState(final XDataWriter writer)
      throws IOException {
    int x, y, z, e;
    writer.writeInt(this.getColumns());
    writer.writeInt(this.getRows());
    writer.writeInt(this.getFloors());
    for (x = 0; x < this.getColumns(); x++) {
      for (y = 0; y < this.getRows(); y++) {
        for (z = 0; z < this.getFloors(); z++) {
          for (e = 0; e < Layers.COUNT; e++) {
            this.savedTowerState.getCell(y, x, z, e).writeObject(writer);
          }
        }
      }
    }
  }

  public void readSavedTowerState(final XDataReader reader,
      final int formatVersion) throws IOException {
    int x, y, z, e, sizeX, sizeY, sizeZ;
    sizeX = reader.readInt();
    sizeY = reader.readInt();
    sizeZ = reader.readInt();
    this.savedTowerState = new LowLevelWorldDataStore(sizeY, sizeX, sizeZ,
        Layers.COUNT);
    for (x = 0; x < sizeY; x++) {
      for (y = 0; y < sizeX; y++) {
        for (z = 0; z < sizeZ; z++) {
          for (e = 0; e < Layers.COUNT; e++) {
            this.savedTowerState.setCell(
                GameObjects.readObject(reader, formatVersion), y, x, z, e);
          }
        }
      }
    }
  }
}
