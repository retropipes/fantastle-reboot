/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.GenerateTask;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.GameObjects;
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
    final Maze m = MazeManager.getMaze();
    if (m.cellRangeCheck(x, y, z)) {
      final FantastleObjectModel below = m.getCell(m.getPlayerLocationX(),
          m.getPlayerLocationY(), m.getPlayerLocationZ(), Layers.GROUND);
      final FantastleObjectModel nextBelow = m.getCell(x, y, z, Layers.GROUND);
      final FantastleObjectModel nextAbove = m.getCell(x, y, z, Layers.OBJECT);
      return MovementTask.checkSolidAbsolute(MovementTask.saved, below,
          nextBelow, nextAbove);
    }
    return false;
  }

  public static void stopMovement() {
    MovementTask.proceed = false;
  }

  static void fireStepActions() {
    final Maze m = MazeManager.getMaze();
    final int px = m.getPlayerLocationX();
    final int py = m.getPlayerLocationY();
    final int pz = m.getPlayerLocationZ();
    m.updateVisibleSquares(px, py, pz);
    m.tickTimers(pz);
    PartyManager.getParty().fireStepActions();
    GameGUI.updateStats();
    MovementTask.checkGameOver();
    MovementTask.checkFloorChange();
    MovementTask.checkLevelChange();
    m.checkForBattle(px, py, pz);
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
    final Maze m = MazeManager.getMaze();
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
      if (m.cellRangeCheck(px, py, pz)) {
        below = m.getCell(px, py, pz, Layers.GROUND);
      } else {
        below = new OpenSpace();
      }
      if (m.cellRangeCheck(px + fX, py + fY, pz + fZ)) {
        nextBelow = m.getCell(px + fX, py + fY, pz + fZ, Layers.GROUND);
        nextAbove = m.getCell(px + fX, py + fY, pz + fZ, Layers.OBJECT);
      } else {
        nextBelow = new OpenSpace();
        nextAbove = new Wall();
      }
      MovementTask.proceed = true;
      if (MovementTask.proceed) {
        m.savePlayerLocation();
        GameView.saveViewingWindow();
        if (m.cellRangeCheck(px + fX, py + fY, pz + fZ)) {
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
        } else {
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
      if (MovementTask.proceed && GameObjects.sendsToShop(nextAbove)) {
        // Send player to shop
        bag.getShop(GameObjects.sendsToWhichShop(nextAbove)).showShop();
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
    final Maze m = MazeManager.getMaze();
    m.savePlayerLocation();
    GameView.saveViewingWindow();
    if (m.cellRangeCheck(x, y, z)) {
      if (!m.getCell(x, y, z, Layers.OBJECT).isSolid()) {
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
    } else {
      MovementTask.moveFailed();
    }
  }

  private static void moveFailed() {
    // Move failed
    final Maze m = MazeManager.getMaze();
    m.restorePlayerLocation();
    GameView.restoreViewingWindow();
    SoundPlayer.playSound(SoundIndex.WALK_FAILED, SoundGroup.GAME);
    Game.setStatusMessage("Can't go that way");
    MovementTask.fireStepActions();
    MovementTask.decayEffects();
    MovementTask.proceed = false;
  }

  private static void checkFloorChange() {
    final Maze m = MazeManager.getMaze();
    final int px = m.getPlayerLocationX();
    final int py = m.getPlayerLocationY();
    final int pz = m.getPlayerLocationZ();
    final FantastleObjectModel below = m.getCell(px, py, pz, Layers.GROUND);
    if (GameObjects.sendsDown(below)) {
      if (Game.isFloorBelow()) {
        // Going down...
        SoundPlayer.playSound(SoundIndex.FALLING, SoundGroup.GAME);
        MovementTask.updatePositionAbsolute(px, py, pz + 1);
      } else {
        MovementTask.moveFailed();
      }
    } else if (GameObjects.sendsUp(below)) {
      if (Game.isFloorAbove()) {
        // Going up...
        SoundPlayer.playSound(SoundIndex.SPRING, SoundGroup.GAME);
        MovementTask.updatePositionAbsolute(px, py, pz - 1);
      } else {
        MovementTask.moveFailed();
      }
    } else if (GameObjects.sendsDown2(below)) {
      if (Game.areTwoFloorsBelow()) {
        // Going down 2...
        SoundPlayer.playSound(SoundIndex.FALLING, SoundGroup.GAME);
        MovementTask.updatePositionAbsolute(px, py, pz - 2);
      } else {
        MovementTask.moveFailed();
      }
    } else if (GameObjects.sendsUp2(below)) {
      if (Game.areTwoFloorsAbove()) {
        // Going up 2...
        SoundPlayer.playSound(SoundIndex.SPRING, SoundGroup.GAME);
        MovementTask.updatePositionAbsolute(px, py, pz - 2);
      } else {
        MovementTask.moveFailed();
      }
    }
  }

  private static void checkLevelChange() {
    final Maze m = MazeManager.getMaze();
    final int px = m.getPlayerLocationX();
    final int py = m.getPlayerLocationY();
    final int pz = m.getPlayerLocationZ();
    final FantastleObjectModel below = m.getCell(px, py, pz, Layers.GROUND);
    if (GameObjects.sendsNext(below)) {
      // Going deeper...
      if (!Game.isLevelBelow()) {
        if (m.canAddLevel()) {
          new GenerateTask(false).start();
        } else {
          MovementTask.moveFailed();
        }
      } else {
        SoundPlayer.playSound(SoundIndex.DOWN, SoundGroup.GAME);
        Game.goToLevelOffset(1);
      }
    } else if (GameObjects.sendsPrevious(below)) {
      if (Game.isLevelAbove()) {
        // Going shallower...
        SoundPlayer.playSound(SoundIndex.UP, SoundGroup.GAME);
        Game.goToLevelOffset(-1);
      } else {
        MovementTask.moveFailed();
      }
    }
  }

  private static void checkGameOver() {
    if (!PartyManager.getParty().isAlive()) {
      SoundPlayer.playSound(SoundIndex.GAME_OVER, SoundGroup.GAME);
      CommonDialogs.showDialog("You have died!");
      PartyManager.getParty().getLeader().onGotKilled();
    }
  }
}
