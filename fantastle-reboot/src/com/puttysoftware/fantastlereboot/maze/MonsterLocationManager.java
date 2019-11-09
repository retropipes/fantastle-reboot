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

  static synchronized void create(final int rows, final int cols) {
    DATA = new FlagStorage(cols, rows);
  }

  public static synchronized boolean hasMonster(final int x, final int y) {
    if (DATA == null) {
      return false;
    }
    return DATA.getCell(y, x);
  }

  public static synchronized void addMonster(final int x, final int y) {
    if (DATA != null) {
      if (!DATA.getCell(y, x)) {
        DATA.setCell(true, y, x);
      }
    }
  }

  public static synchronized void removeMonster(final int x, final int y) {
    if (DATA != null) {
      if (DATA.getCell(y, x)) {
        DATA.setCell(false, y, x);
      }
    }
  }

  static synchronized void moveOneMonster(final Maze maze, final int moveX,
      final int moveY, final int locX, final int locY) {
    if (DATA != null) {
      final int px = maze.getPlayerLocationX();
      final int py = maze.getPlayerLocationY();
      final int pz = maze.getPlayerLocationZ();
      if (locX + moveX >= 0 && locX + moveX < DATA.getShape()[1]
          && locY + moveY >= 0 && locY + moveY < DATA.getShape()[0]) {
        final FantastleObjectModel there = maze.getCell(locX + moveX,
            locY + moveY, pz, Layers.OBJECT);
        if (!there.isSolid() && !hasMonster(locX + moveX, locY + moveY)) {
          // Move the monster
          removeMonster(locX, locY);
          addMonster(locX + moveX, locY + moveY);
          // If the player is now standing on a monster...
          if (locX + moveX == px && locY + moveY == py) {
            // ... and we aren't already in battle...
            BagOStuff bag = FantastleReboot.getBagOStuff();
            if (!bag.inBattle()) {
              // ... start battle with that monster!
              Game.stopMovement();
              bag.getBattle().doBattle(px, py);
            }
          }
        }
      }
    }
  }

  public static synchronized void postBattle(final Maze maze, final int locX,
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
