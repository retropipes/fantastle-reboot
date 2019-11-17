/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.FileStateManager;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModelList;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Wall;

final class MovementTask extends Thread {
  // Fields
  private static FantastleObjectModel saved;
  private static boolean proceed;
  private static boolean relative;
  private static int moveX, moveY, moveZ;

  // Constructors
  public MovementTask() {
    this.setName("Movement Handler");
    MovementTask.saved = new OpenSpace();
  }

  // Methods
  @Override
  public void run() {
    try {
      while (true) {
        this.waitForWork();
        if (MovementTask.relative) {
          MovementTask.updatePositionRelative(MovementTask.moveX,
              MovementTask.moveY, MovementTask.moveZ);
        }
        if (!MovementTask.relative) {
          MovementTask.updatePositionAbsolute(MovementTask.moveX,
              MovementTask.moveY, MovementTask.moveZ);
        }
      }
    } catch (final Throwable t) {
      FantastleReboot.logError(t);
    }
  }

  private synchronized void waitForWork() {
    try {
      this.wait();
    } catch (final InterruptedException e) {
      // Ignore
    }
  }

  public synchronized void moveRelative(final int x, final int y, final int z) {
    MovementTask.moveX = x;
    MovementTask.moveY = y;
    MovementTask.moveZ = z;
    MovementTask.relative = true;
    this.notify();
  }

  public synchronized void moveAbsolute(final int x, final int y, final int z) {
    MovementTask.moveX = x;
    MovementTask.moveY = y;
    MovementTask.moveZ = z;
    MovementTask.relative = false;
    this.notify();
  }

  public static boolean tryAbsolute(final int x, final int y, final int z) {
    try {
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final Maze m = app.getMazeManager().getMaze();
      final FantastleObjectModel below = m.getCell(m.getPlayerLocationX(),
          m.getPlayerLocationY(), m.getPlayerLocationZ(), Layers.GROUND);
      final FantastleObjectModel nextBelow = m.getCell(x, y, z, Layers.GROUND);
      final FantastleObjectModel nextAbove = m.getCell(x, y, z, Layers.OBJECT);
      return MovementTask.checkSolidAbsolute(MovementTask.saved, below,
          nextBelow, nextAbove);
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  public static void stopMovement() {
    MovementTask.proceed = false;
  }

  static void fireStepActions() {
    BagOStuff bag = FantastleReboot.getBagOStuff();
    final Maze m = bag.getMazeManager().getMaze();
    final int px = m.getPlayerLocationX();
    final int py = m.getPlayerLocationY();
    final int pz = m.getPlayerLocationZ();
    m.updateVisibleSquares(px, py, pz);
    m.tickTimers(pz);
    PartyManager.getParty().fireStepActions();
    GameGUI.updateStats();
    MovementTask.checkGameOver();
    MovementTask.checkStairs();
  }

  private static void decayEffects() {
    EffectManager.decayEffects();
  }

  private static int[] doEffects(final int x, final int y) {
    return EffectManager.doEffects(x, y);
  }

  private static boolean checkSolidAbsolute(final FantastleObjectModel inside,
      final FantastleObjectModel below, final FantastleObjectModel nextBelow,
      final FantastleObjectModel nextAbove) {
    final boolean insideSolid = inside.isSolid();
    final boolean belowSolid = below.isSolid();
    final boolean nextBelowSolid = nextBelow.isSolid();
    final boolean nextAboveSolid = nextAbove.isSolid();
    if (insideSolid || belowSolid || nextBelowSolid || nextAboveSolid) {
      return true;
    } else {
      return false;
    }
  }

  private static void updatePositionRelative(final int dirX, final int dirY,
      final int dirZ) {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final Maze m = bag.getMazeManager().getMaze();
    final FantastleObjectModelList objects = bag.getObjects();
    int px = m.getPlayerLocationX();
    int py = m.getPlayerLocationY();
    int pz = m.getPlayerLocationZ();
    int fX;
    int fY;
    final int fZ = dirZ;
    final int[] mod = MovementTask.doEffects(dirX, dirY);
    fX = mod[0];
    fY = mod[1];
    MovementTask.proceed = false;
    FantastleObjectModel below = null;
    FantastleObjectModel nextBelow = null;
    FantastleObjectModel nextAbove = new Wall();
    boolean loopCheck = true;
    do {
      try {
        below = m.getCell(px, py, pz, Layers.GROUND);
      } catch (final ArrayIndexOutOfBoundsException ae) {
        below = new OpenSpace();
      }
      try {
        nextBelow = m.getCell(px + fX, py + fY, pz + fZ, Layers.GROUND);
      } catch (final ArrayIndexOutOfBoundsException ae) {
        nextBelow = new OpenSpace();
      }
      try {
        nextAbove = m.getCell(px + fX, py + fY, pz + fZ, Layers.OBJECT);
      } catch (final ArrayIndexOutOfBoundsException ae) {
        nextAbove = new Wall();
      }
      MovementTask.proceed = true;
      if (MovementTask.proceed) {
        m.savePlayerLocation();
        GameView.saveViewingWindow();
        try {
          if (MovementTask.checkSolid(MovementTask.saved, below, nextBelow,
              nextAbove)) {
            m.offsetPlayerLocationX(fX);
            m.offsetPlayerLocationY(fY);
            px += fX;
            py += fY;
            GameView.offsetViewingWindowLocationX(fY);
            GameView.offsetViewingWindowLocationY(fX);
            FileStateManager.setDirty(true);
            MovementTask.fireStepActions();
            MovementTask.decayEffects();
            GameGUI.redrawMaze();
            if (MovementTask.proceed) {
              MovementTask.saved = m.getCell(px, py, pz, Layers.OBJECT);
            }
          } else {
            MovementTask.moveFailed();
          }
        } catch (final ArrayIndexOutOfBoundsException ae) {
          MovementTask.moveFailed();
        }
        MovementTask.fireStepActions();
      } else {
        MovementTask.moveFailed();
      }
      px = m.getPlayerLocationX();
      py = m.getPlayerLocationY();
      pz = m.getPlayerLocationZ();
      loopCheck = MovementTask.checkLoopCondition(below, nextBelow, nextAbove);
      if (loopCheck && !nextBelow.hasFriction()) {
        // Sliding on ice
        SoundPlayer.playSound(SoundIndex.WALK_ICE, SoundGroup.GAME);
      } else if (nextBelow.hasFriction() && MovementTask.proceed) {
        // Walking normally
        SoundPlayer.playSound(SoundIndex.WALK, SoundGroup.GAME);
      }
      if (MovementTask.proceed && objects.sendsToShop(nextAbove)) {
        // Send player to shop
        bag.getShop(objects.sendsToWhichShop(nextAbove)).showShop();
      }
    } while (loopCheck);
  }

  private static boolean checkLoopCondition(final FantastleObjectModel below,
      final FantastleObjectModel nextBelow,
      final FantastleObjectModel nextAbove) {
    return MovementTask.proceed
        && !EffectManager.isEffectActive(EffectConstants.EFFECT_STICKY)
        && !nextBelow.hasFriction() && MovementTask
            .checkSolid(MovementTask.saved, below, nextBelow, nextAbove);
  }

  private static boolean checkSolid(final FantastleObjectModel inside,
      final FantastleObjectModel below, final FantastleObjectModel nextBelow,
      final FantastleObjectModel nextAbove) {
    final boolean insideSolid = inside.isSolid();
    final boolean belowSolid = below.isSolid();
    final boolean nextBelowSolid = nextBelow.isSolid();
    final boolean nextAboveSolid = nextAbove.isSolid();
    if (insideSolid || belowSolid || nextBelowSolid || nextAboveSolid) {
      return false;
    } else {
      return true;
    }
  }

  private static void updatePositionAbsolute(final int x, final int y,
      final int z) {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final Maze m = bag.getMazeManager().getMaze();
    m.savePlayerLocation();
    GameView.saveViewingWindow();
    try {
      if (!(m.getCell(x, y, z, Layers.OBJECT).isSolid())) {
        m.setPlayerLocationX(x);
        m.setPlayerLocationY(y);
        m.setPlayerLocationZ(z);
        GameView.setViewingWindowLocationX(
            m.getPlayerLocationY() - GameView.getOffsetFactorX());
        GameView.setViewingWindowLocationY(
            m.getPlayerLocationX() - GameView.getOffsetFactorY());
        MovementTask.saved = m.getCell(m.getPlayerLocationX(),
            m.getPlayerLocationY(), m.getPlayerLocationZ(), Layers.OBJECT);
        FileStateManager.setDirty(true);
        final int px = m.getPlayerLocationX();
        final int py = m.getPlayerLocationY();
        final int pz = m.getPlayerLocationZ();
        m.updateVisibleSquares(px, py, pz);
        GameGUI.redrawMaze();
      }
    } catch (final ArrayIndexOutOfBoundsException ae) {
      MovementTask.moveFailed();
    }
  }

  private static void moveFailed() {
    // Move failed
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final Maze m = bag.getMazeManager().getMaze();
    m.restorePlayerLocation();
    GameView.restoreViewingWindow();
    SoundPlayer.playSound(SoundIndex.WALK_FAILED, SoundGroup.GAME);
    Game.setStatusMessage("Can't go that way");
    MovementTask.fireStepActions();
    MovementTask.decayEffects();
    MovementTask.proceed = false;
  }

  private static void checkStairs() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final FantastleObjectModelList objects = bag.getObjects();
    final Maze m = bag.getMazeManager().getMaze();
    final int px = m.getPlayerLocationX();
    final int py = m.getPlayerLocationY();
    final int pz = m.getPlayerLocationZ();
    final FantastleObjectModel below = m.getCell(px, py, pz, Layers.GROUND);
    if (objects.sendsDown(below)) {
      if (Game.isFloorBelow()) {
        // Going down...
        SoundPlayer.playSound(SoundIndex.DOWN, SoundGroup.GAME);
        MovementTask.updatePositionAbsolute(px, py, pz + 1);
      } else {
        MovementTask.moveFailed();
      }
    } else if (objects.sendsUp(below)) {
      if (Game.isFloorAbove()) {
        // Going up...
        SoundPlayer.playSound(SoundIndex.UP, SoundGroup.GAME);
        MovementTask.updatePositionAbsolute(px, py, pz - 1);
      } else {
        MovementTask.moveFailed();
      }
    }
  }

  private static void checkGameOver() {
    if (!PartyManager.getParty().isAlive()) {
      SoundPlayer.playSound(SoundIndex.GAME_OVER, SoundGroup.GAME);
      CommonDialogs.showDialog(
          "You have died! You lose 10% of your experience and all your Gold, but you are healed fully.");
      PartyManager.getParty().getLeader().onDeath(-10);
    }
  }
}
