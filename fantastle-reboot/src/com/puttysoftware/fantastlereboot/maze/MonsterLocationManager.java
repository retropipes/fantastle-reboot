package com.puttysoftware.fantastlereboot.maze;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.storage.FlagStorage;

public final class MonsterLocationManager {
  private static FlagStorage DATA;

  private MonsterLocationManager() {
    super();
  }

  static void create(final int rows, final int cols) {
    DATA = new FlagStorage(cols, rows);
  }

  public static boolean hasMonster(final int x, final int y) {
    if (DATA == null) {
      return false;
    }
    return DATA.getCell(y, x);
  }

  public static void addMonster(final int x, final int y) {
    if (DATA != null) {
      if (!DATA.getCell(y, x)) {
        DATA.setCell(true, y, x);
      }
    }
  }

  public static void removeMonster(final int x, final int y) {
    if (DATA != null) {
      if (DATA.getCell(y, x)) {
        DATA.setCell(false, y, x);
      }
    }
  }

  private static void moveMonster(final int locX, final int locY,
      final int moveX, final int moveY) {
    if (DATA != null) {
      DATA.setCell(false, locY, locX);
      DATA.setCell(true, locY + moveY, locX + moveX);
    }
  }

  private static void moveOneMonster(final Maze maze, final int moveX,
      final int moveY, final int locX, final int locY) {
    if (DATA != null) {
      final int pz = maze.getPlayerLocationZ();
      final int rows = DATA.getShape()[1];
      final int cols = DATA.getShape()[0];
      if (locX + moveX >= 0 && locX + moveX < cols && locY + moveY >= 0
          && locY + moveY < rows) {
        final FantastleObjectModel there = maze.getCell(locX + moveX,
            locY + moveY, pz, Layers.OBJECT);
        if (!there.isSolid() && !hasMonster(locX + moveX, locY + moveY)) {
          // Move the monster
          moveMonster(locX, locY, moveX, moveY);
        }
      }
    }
  }

  public static void checkForBattle(final int px, final int py) {
    // If the player is now standing on a monster...
    if (hasMonster(px, py)) {
      // ... and we aren't already in battle...
      BagOStuff bag = FantastleReboot.getBagOStuff();
      if (!bag.inBattle()) {
        // ... start a battle with that monster!
        Game.stopMovement();
        bag.getBattle().doBattle(px, py);
      }
    }
  }

  public static void moveAllMonsters(final Maze maze) {
    if (DATA != null) {
      int x, y;
      final int rows = DATA.getShape()[1];
      final int cols = DATA.getShape()[0];
      // Tick all object timers
      for (x = 0; x < cols; x++) {
        for (y = 0; y < rows; y++) {
          int objMovedX = RandomRange.generate(-1, 1);
          int objMovedY = RandomRange.generate(-1, 1);
          if (MonsterLocationManager.hasMonster(x, y)) {
            MonsterLocationManager.moveOneMonster(maze, objMovedX, objMovedY,
                x + objMovedX, y + objMovedY);
          }
        }
      }
    }
  }

  public static void postBattle(final Maze maze, final int locX,
      final int locY) {
    // Clear the monster just defeated
    removeMonster(locX, locY);
    // Generate a new monster
    final int pz = maze.getPlayerLocationZ();
    final RandomRange row = new RandomRange(0, maze.getRows() - 1);
    final RandomRange column = new RandomRange(0, maze.getColumns() - 1);
    int genX = row.generate();
    int genY = column.generate();
    FantastleObjectModel currObj = maze.getCell(genX, genY, pz, Layers.OBJECT);
    if (!currObj.isSolid() && !hasMonster(genX, genY)) {
      addMonster(genX, genY);
    } else {
      while (currObj.isSolid() || hasMonster(genX, genY)) {
        genX = row.generate();
        genY = column.generate();
        currObj = maze.getCell(genX, genY, pz, Layers.OBJECT);
      }
      addMonster(genX, genY);
    }
  }
}
