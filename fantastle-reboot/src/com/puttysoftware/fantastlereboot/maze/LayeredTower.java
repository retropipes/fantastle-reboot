/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze;

import java.io.IOException;
import java.util.Arrays;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.MonsterObject;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Tile;
import com.puttysoftware.fantastlereboot.objects.WallOff;
import com.puttysoftware.fantastlereboot.objects.WallOn;
import com.puttysoftware.fantastlereboot.utilities.FantastleObjectModelList;
import com.puttysoftware.fantastlereboot.utilities.RandomGenerationRule;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.storage.FlagStorage;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

final class LayeredTower implements Cloneable {
  // Properties
  private LowLevelAMODataStore data;
  private LowLevelAMODataStore savedTowerState;
  private FlagStorage visionData;
  private final LowLevelNoteDataStore noteData;
  private final int[] playerStartData;
  private final int[] playerLocationData;
  private final int[] savedPlayerLocationData;
  private final int[] findResult;
  private boolean horizontalWraparoundEnabled;
  private boolean verticalWraparoundEnabled;
  private int visionMode;
  private int visionModeExploreRadius;
  private int visionRadius;
  private int initialVisionRadius;
  private int regionSize;
  private static final int MAX_VISION_RADIUS = 16;
  private static final int MAX_FLOORS = 1;
  private static final int MAX_COLUMNS = 250;
  private static final int MAX_ROWS = 250;

  // Constructors
  public LayeredTower(final int rows, final int cols, final int floors) {
    this.data = new LowLevelAMODataStore(cols, rows, floors, Layers.COUNT);
    this.savedTowerState = new LowLevelAMODataStore(cols, rows, floors,
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
    this.horizontalWraparoundEnabled = false;
    this.verticalWraparoundEnabled = false;
    this.visionMode = MazeConstants.VISION_MODE_EXPLORE_AND_LOS;
    this.visionModeExploreRadius = 2;
    this.visionRadius = MAX_VISION_RADIUS;
    this.regionSize = 8;
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
  public void updateMonsterPosition(final int move, final int xLoc,
      final int yLoc, final MonsterObject monster) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final int[] dirMove = DirectionResolver.unresolve(move);
    final int pLocX = this.getPlayerRow();
    final int pLocY = this.getPlayerColumn();
    final int zLoc = this.getPlayerFloor();
    try {
      final FantastleObjectModel there = this.getCell(xLoc + dirMove[0],
          yLoc + dirMove[1], zLoc, Layers.OBJECT);
      final FantastleObjectModel ground = this.getCell(xLoc + dirMove[0],
          yLoc + dirMove[1], zLoc, Layers.GROUND);
      if (!there.isSolid() && !(there instanceof MonsterObject)) {
        if (LayeredTower.radialScan(xLoc, yLoc, 0, pLocX, pLocY)) {
          if (bag.getMode() != BagOStuff.STATUS_BATTLE) {
            app.getGameManager().stopMovement();
            app.getBattle().doBattle();
            this.postBattle(monster, xLoc, yLoc, false);
          }
        } else {
          // Move the monster
          this.setCell(monster.getSavedObject(), xLoc, yLoc, zLoc,
              Layers.OBJECT);
          monster.setSavedObject(there);
          this.setCell(monster, xLoc + dirMove[0], yLoc + dirMove[1], zLoc,
              Layers.OBJECT);
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

  public void postBattle(final MonsterObject m, final int xLoc, final int yLoc,
      final boolean player) {
    final FantastleObjectModel saved = m.getSavedObject();
    final int zLoc = this.getPlayerFloor();
    if (!player) {
      this.setCell(saved, xLoc, yLoc, zLoc, Layers.OBJECT);
    }
    this.generateOneMonster();
  }

  private void generateOneMonster() {
    final RandomRange row = new RandomRange(0, this.getRows() - 1);
    final RandomRange column = new RandomRange(0, this.getColumns() - 1);
    final int zLoc = this.getPlayerFloor();
    int randomRow, randomColumn;
    randomRow = row.generate();
    randomColumn = column.generate();
    FantastleObjectModel currObj = this.getCell(randomRow, randomColumn, zLoc,
        Layers.OBJECT);
    if (!currObj.isSolid()) {
      final FantastleObjectModel m = new MonsterObject();
      m.setSavedObject(currObj);
      this.setCell(m, randomRow, randomColumn, zLoc, Layers.OBJECT);
    } else {
      while (currObj.isSolid()) {
        randomRow = row.generate();
        randomColumn = column.generate();
        currObj = this.getCell(randomRow, randomColumn, zLoc, Layers.OBJECT);
      }
      final FantastleObjectModel m = new MonsterObject();
      m.setSavedObject(currObj);
      this.setCell(m, randomRow, randomColumn, zLoc, Layers.OBJECT);
    }
  }

  public boolean hasNote(final int x, final int y, final int z) {
    return this.noteData.getNote(y, x, z) != null;
  }

  public void createNote(final int x, final int y, final int z) {
    this.noteData.setNote(new MazeNote(), y, x, z);
  }

  public MazeNote getNote(final int x, final int y, final int z) {
    return this.noteData.getNote(y, x, z);
  }

  public FantastleObjectModel getCell(final int row, final int col,
      final int floor, final int extra) {
    int fR = row;
    int fC = col;
    final int fF = floor;
    if (this.verticalWraparoundEnabled) {
      fC = this.normalizeColumn(fC);
    }
    if (this.horizontalWraparoundEnabled) {
      fR = this.normalizeRow(fR);
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
    for (int x = 0; x < this.playerStartData.length; x++) {
      res = res && (this.playerStartData[x] != -1);
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

  public void updateVisibleSquares(final int xp, final int yp, final int zp) {
    if ((this.visionMode
        | MazeConstants.VISION_MODE_EXPLORE) == this.visionMode) {
      for (int x = xp - this.visionModeExploreRadius; x <= xp
          + this.visionModeExploreRadius; x++) {
        for (int y = yp - this.visionModeExploreRadius; y <= yp
            + this.visionModeExploreRadius; y++) {
          int fx, fy;
          if (this.isHorizontalWraparoundEnabled()) {
            fx = this.normalizeColumn(x);
          } else {
            fx = x;
          }
          if (this.isVerticalWraparoundEnabled()) {
            fy = this.normalizeRow(y);
          } else {
            fy = y;
          }
          boolean alreadyVisible = false;
          try {
            alreadyVisible = this.visionData.getCell(fx, fy, zp);
          } catch (final ArrayIndexOutOfBoundsException aioobe) {
            // Ignore
          }
          if (!alreadyVisible) {
            if ((this.visionMode
                | MazeConstants.VISION_MODE_LOS) == this.visionMode) {
              if (this.isSquareVisibleLOS(x, y, xp, yp)) {
                try {
                  this.visionData.setCell(true, fx, fy, zp);
                } catch (final ArrayIndexOutOfBoundsException aioobe) {
                  // Ignore
                }
              }
            } else {
              try {
                this.visionData.setCell(true, fx, fy, zp);
              } catch (final ArrayIndexOutOfBoundsException aioobe) {
                // Ignore
              }
            }
          }
        }
      }
    }
  }

  public boolean isSquareVisible(final int x1, final int y1, final int x2,
      final int y2) {
    if (this.visionMode == MazeConstants.VISION_MODE_NONE) {
      return true;
    } else {
      boolean result = false;
      if ((this.visionMode
          | MazeConstants.VISION_MODE_EXPLORE) == this.visionMode) {
        result = result || this.isSquareVisibleExplore(x2, y2);
        if (result && (this.visionMode
            | MazeConstants.VISION_MODE_LOS) == this.visionMode) {
          if (this.areCoordsInBounds(x1, y1, x2, y2)) {
            // In bounds
            result = result || this.isSquareVisibleLOS(x1, y1, x2, y2);
          } else {
            // Out of bounds
            result = result && this.isSquareVisibleLOS(x1, y1, x2, y2);
          }
        }
      } else {
        if (this.areCoordsInBounds(x1, y1, x2, y2)) {
          // In bounds
          result = result || this.isSquareVisibleLOS(x1, y1, x2, y2);
        } else {
          // Out of bounds
          result = result && this.isSquareVisibleLOS(x1, y1, x2, y2);
        }
      }
      return result;
    }
  }

  private boolean areCoordsInBounds(final int x1, final int y1, final int x2,
      final int y2) {
    int fx1, fx2, fy1, fy2;
    if (this.isHorizontalWraparoundEnabled()) {
      fx1 = this.normalizeColumn(x1);
      fx2 = this.normalizeColumn(x2);
    } else {
      fx1 = x1;
      fx2 = x2;
    }
    if (this.isVerticalWraparoundEnabled()) {
      fy1 = this.normalizeRow(y1);
      fy2 = this.normalizeRow(y2);
    } else {
      fy1 = y1;
      fy2 = y2;
    }
    return fx1 >= 0 && fx1 <= this.getRows() && fx2 >= 0
        && fx2 <= this.getRows() && fy1 >= 0 && fy1 <= this.getColumns()
        && fy2 >= 0 && fy2 <= this.getColumns();
  }

  private boolean isSquareVisibleExplore(final int x2, final int y2) {
    final int zLoc = this.getPlayerFloor();
    int fx2, fy2;
    if (this.isHorizontalWraparoundEnabled()) {
      fx2 = this.normalizeColumn(x2);
    } else {
      fx2 = x2;
    }
    if (this.isVerticalWraparoundEnabled()) {
      fy2 = this.normalizeRow(y2);
    } else {
      fy2 = y2;
    }
    try {
      return this.visionData.getCell(fx2, fy2, zLoc);
    } catch (final ArrayIndexOutOfBoundsException aioobe) {
      return true;
    }
  }

  private boolean isSquareVisibleLOS(final int x1, final int y1, final int x2,
      final int y2) {
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
      // Does object block LOS?
      try {
        final FantastleObjectModel obj = this.getCell(fx1, fy1, zLoc,
            Layers.OBJECT);
        if (obj.isSightBlocking()) {
          // This object blocks LOS
          if (fx1 != x1 || fy1 != y1) {
            return false;
          }
        }
      } catch (final ArrayIndexOutOfBoundsException aioobe) {
        // Void blocks LOS
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
    // No objects block LOS
    return true;
  }

  public void setCell(final FantastleObjectModel mo, final int row,
      final int col, final int floor, final int extra) {
    int fR = row;
    int fC = col;
    final int fF = floor;
    if (this.verticalWraparoundEnabled) {
      fC = this.normalizeColumn(fC);
    }
    if (this.horizontalWraparoundEnabled) {
      fR = this.normalizeRow(fR);
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

  public void fillFloor(final FantastleObjectModel bottom,
      final FantastleObjectModel top, final int z) {
    int x, y, e;
    for (x = 0; x < this.getColumns(); x++) {
      for (y = 0; y < this.getRows(); y++) {
        for (e = 0; e < Layers.COUNT; e++) {
          if (e == Layers.GROUND) {
            this.setCell(bottom, y, x, z, e);
          } else {
            this.setCell(top, y, x, z, e);
          }
        }
      }
    }
  }

  public void fillRandomly(final Maze maze, final int w) {
    for (int z = 0; z < this.getFloors(); z++) {
      this.fillFloorRandomly(maze, z, w);
    }
  }

  public void fillFloorRandomly(final Maze maze, final int z, final int w) {
    // Pre-Pass
    final FantastleObjectModelList objects = FantastleReboot.getBagOStuff()
        .getObjects();
    final FantastleObjectModel pass1FillBottom = new Tile();
    final FantastleObjectModel pass1FillTop = new OpenSpace();
    RandomRange r = null;
    int x, y, e;
    // Pass 1
    this.fillFloor(pass1FillBottom, pass1FillTop, z);
    // Pass 2
    final int columns = this.getColumns();
    final int rows = this.getRows();
    for (e = 0; e < Layers.COUNT; e++) {
      final FantastleObjectModel[] objectsWithoutPrerequisites = objects
          .getAllWithoutPrerequisiteAndNotRequired(e);
      if (objectsWithoutPrerequisites != null) {
        r = new RandomRange(0, objectsWithoutPrerequisites.length - 1);
        for (x = 0; x < columns; x++) {
          for (y = 0; y < rows; y++) {
            final FantastleObjectModel placeObj = objectsWithoutPrerequisites[r
                .generate()];
            final boolean okay = placeObj.shouldGenerateObject(maze, x, y, z, w,
                e);
            if (okay) {
              this.setCell(
                  objects.getNewInstanceByUniqueID(placeObj.getUniqueID()), y,
                  x, z, e);
            }
          }
        }
      }
    }
    // Pass 3
    for (int layer = 0; layer < Layers.COUNT; layer++) {
      final FantastleObjectModel[] requiredObjects = objects
          .getAllRequired(layer);
      if (requiredObjects != null) {
        final RandomRange row = new RandomRange(0, this.getRows() - 1);
        final RandomRange column = new RandomRange(0, this.getColumns() - 1);
        int randomColumn, randomRow;
        for (x = 0; x < requiredObjects.length; x++) {
          final FantastleObjectModel currObj = requiredObjects[x];
          final int min = currObj.getMinimumRequiredQuantity(maze);
          int max = currObj.getMaximumRequiredQuantity(maze);
          if (max == RandomGenerationRule.NO_LIMIT) {
            // Maximum undefined, so define it relative to this maze
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
            if (currObj.shouldGenerateObject(maze, randomRow, randomColumn, z,
                w, layer)) {
              this.setCell(
                  objects.getNewInstanceByUniqueID(currObj.getUniqueID()),
                  randomColumn, randomRow, z, layer);
            } else {
              while (!currObj.shouldGenerateObject(maze, randomColumn,
                  randomRow, z, w, layer)) {
                randomRow = row.generate();
                randomColumn = column.generate();
              }
              this.setCell(
                  objects.getNewInstanceByUniqueID(currObj.getUniqueID()),
                  randomColumn, randomRow, z, layer);
            }
          }
        }
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

  public boolean isHorizontalWraparoundEnabled() {
    return this.horizontalWraparoundEnabled;
  }

  public boolean isVerticalWraparoundEnabled() {
    return this.verticalWraparoundEnabled;
  }

  public void tickTimers(final int floor) {
    int x, y;
    // Tick all FantastleObjectModel timers
    for (x = 0; x < this.getColumns(); x++) {
      for (y = 0; y < this.getRows(); y++) {
        final FantastleObjectModel mo = this.getCell(y, x, floor,
            Layers.OBJECT);
        if (mo != null) {
          mo.tickTimer();
        }
      }
    }
  }

  public static boolean radialScan(final int cx, final int cy, final int r,
      final int tx, final int ty) {
    return (Math.abs(tx - cx) <= r && Math.abs(ty - cy) <= r);
  }

  public void fullScanButton(final int l) {
    int u, v, z;
    z = LayeredTower.MAX_FLOORS - 1;
    // Perform the scan
    for (u = 0; u < this.getColumns(); u++) {
      for (v = 0; v < this.getRows(); v++) {
        final FantastleObjectModel testObj = this.getCell(v, u, z, l);
        if (testObj instanceof WallOff) {
          this.setCell(new WallOn(), v, u, z, l);
        } else if (testObj instanceof WallOn) {
          this.setCell(new WallOff(), v, u, z, l);
        }
      }
    }
  }

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
    writer.writeBoolean(this.horizontalWraparoundEnabled);
    writer.writeBoolean(this.verticalWraparoundEnabled);
    writer.writeInt(this.visionMode);
    writer.writeInt(this.visionModeExploreRadius);
    writer.writeInt(this.visionMode);
    writer.writeInt(this.visionModeExploreRadius);
    writer.writeInt(this.visionRadius);
    writer.writeInt(this.initialVisionRadius);
    writer.writeInt(this.regionSize);
  }

  public static LayeredTower readLayeredTowerV1(final XDataReader reader)
      throws IOException {
    int y, x, z, e, mazeSizeX, mazeSizeY, mazeSizeZ;
    mazeSizeX = reader.readInt();
    mazeSizeY = reader.readInt();
    mazeSizeZ = reader.readInt();
    final LayeredTower lt = new LayeredTower(mazeSizeX, mazeSizeY, mazeSizeZ);
    for (x = 0; x < lt.getColumns(); x++) {
      for (y = 0; y < lt.getRows(); y++) {
        for (z = 0; z < lt.getFloors(); z++) {
          for (e = 0; e < Layers.COUNT; e++) {
            lt.setCell(FantastleReboot.getBagOStuff().getObjects().readObject(
                reader, FormatConstants.MAZE_FORMAT_LATEST), y, x, z, e);
            if (lt.getCell(y, x, z, e) == null) {
              return null;
            }
          }
          lt.visionData.setCell(reader.readBoolean(), y, x, z);
          final boolean hasNote = reader.readBoolean();
          if (hasNote) {
            final MazeNote mn = MazeNote.readNote(reader);
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
    lt.horizontalWraparoundEnabled = reader.readBoolean();
    lt.verticalWraparoundEnabled = reader.readBoolean();
    lt.visionMode = reader.readInt();
    lt.visionModeExploreRadius = reader.readInt();
    lt.visionMode = reader.readInt();
    lt.visionModeExploreRadius = reader.readInt();
    lt.visionRadius = reader.readInt();
    lt.initialVisionRadius = reader.readInt();
    lt.regionSize = reader.readInt();
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
    this.savedTowerState = new LowLevelAMODataStore(sizeY, sizeX, sizeZ,
        Layers.COUNT);
    for (x = 0; x < sizeY; x++) {
      for (y = 0; y < sizeX; y++) {
        for (z = 0; z < sizeZ; z++) {
          for (e = 0; e < Layers.COUNT; e++) {
            this.savedTowerState.setCell(FantastleReboot.getBagOStuff()
                .getObjects().readObject(reader, formatVersion), y, x, z, e);
          }
        }
      }
    }
  }
}
