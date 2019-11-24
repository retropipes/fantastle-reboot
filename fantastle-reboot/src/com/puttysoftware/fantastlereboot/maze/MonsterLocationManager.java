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
    MonsterLocationManager.DATA = new FlagStorage(cols, rows);
  }

  public static boolean hasMonster(final int x, final int y) {
    if (MonsterLocationManager.DATA == null) {
      return false;
    }
    return MonsterLocationManager.DATA.getCell(y, x);
  }

  public static void addMonster(final int x, final int y) {
    if (MonsterLocationManager.DATA != null) {
      if (!MonsterLocationManager.DATA.getCell(y, x)) {
        MonsterLocationManager.DATA.setCell(true, y, x);
      }
    }
  }

  public static void removeMonster(final int x, final int y) {
    if (MonsterLocationManager.DATA != null) {
      if (MonsterLocationManager.DATA.getCell(y, x)) {
        MonsterLocationManager.DATA.setCell(false, y, x);
      }
    }
  }

  private static boolean checkMoveMonster(final int locX, final int locY,
      final int moveX, final int moveY) {
    if (MonsterLocationManager.DATA != null) {
      if (MonsterLocationManager.DATA.getCell(locY, locX)
          && !MonsterLocationManager.DATA.getCell(locY + moveY, locX + moveX)) {
        return true;
      }
      return false;
    }
    return true;
  }

  private static void moveMonster(final int locX, final int locY,
      final int moveX, final int moveY) {
    if (MonsterLocationManager.checkMoveMonster(locX, locY, moveX, moveY)) {
      MonsterLocationManager.DATA.setCell(false, locY, locX);
      MonsterLocationManager.DATA.setCell(true, locY + moveY, locX + moveX);
    }
  }

  public static boolean checkForBattle(final int px, final int py) {
    // If the player is now standing on a monster...
    if (MonsterLocationManager.hasMonster(px, py)) {
      // ... and we aren't already in battle...
      final BagOStuff bag = FantastleReboot.getBagOStuff();
      if (!bag.inBattle()) {
        // ... start a battle with that monster!
        Game.stopMovement();
        bag.getBattle().doBattle(px, py);
        return true;
      }
    }
    return false;
  }

  public static void moveAllMonsters(final Maze maze) {
    if (MonsterLocationManager.DATA != null) {
      int locX, locY;
      final int rows = MonsterLocationManager.DATA.getShape()[1] - 1;
      final int cols = MonsterLocationManager.DATA.getShape()[0] - 1;
      final int pz = maze.getPlayerLocationZ();
      // Tick all object timers
      for (locX = 0; locX < cols; locX++) {
        for (locY = 0; locY < rows; locY++) {
          final int moveX = RandomRange.generate(-1, 1);
          final int moveY = RandomRange.generate(-1, 1);
          if (MonsterLocationManager.hasMonster(locX, locY)) {
            if (locX + moveX >= 0 && locX + moveX < cols && locY + moveY >= 0
                && locY + moveY < rows) {
              final FantastleObjectModel there = maze.getCell(locX + moveX,
                  locY + moveY, pz, Layers.OBJECT);
              final boolean checkMove = MonsterLocationManager
                  .checkMoveMonster(locX, locY, moveX, moveY);
              if (!there.isSolid() && checkMove) {
                // Move the monster
                MonsterLocationManager.moveMonster(locX, locY, moveX, moveY);
              }
            }
          }
        }
      }
    }
  }

  public static void postBattle(final Maze maze, final int locX,
      final int locY) {
    // Clear the monster just defeated
    MonsterLocationManager.removeMonster(locX, locY);
    // Generate a new monster
    final int pz = maze.getPlayerLocationZ();
    final int rows = MonsterLocationManager.DATA.getShape()[1] - 1;
    final int cols = MonsterLocationManager.DATA.getShape()[0] - 1;
    final RandomRange row = new RandomRange(0, rows);
    final RandomRange column = new RandomRange(0, cols);
    int genX = row.generate();
    int genY = column.generate();
    FantastleObjectModel currObj = maze.getCell(genX, genY, pz, Layers.OBJECT);
    if (!currObj.isSolid() && !MonsterLocationManager.hasMonster(genX, genY)) {
      MonsterLocationManager.addMonster(genX, genY);
    } else {
      while (currObj.isSolid()
          || MonsterLocationManager.hasMonster(genX, genY)) {
        genX = row.generate();
        genY = column.generate();
        currObj = maze.getCell(genX, genY, pz, Layers.OBJECT);
      }
      MonsterLocationManager.addMonster(genX, genY);
    }
  }
}
