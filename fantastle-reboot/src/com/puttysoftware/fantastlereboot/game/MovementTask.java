/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Wall;

final class MovementTask extends Thread {
  // Fields
  private final GameViewingWindowManager vwMgr;
  private final GameGUIManager gui;
  private final EffectManager em;
  private FantastleObjectModel saved;
  private boolean proceed;
  private boolean relative;
  private int moveX, moveY, moveZ;

  // Constructors
  public MovementTask(final GameViewingWindowManager view,
      final EffectManager effect, final GameGUIManager gameGUI) {
    this.setName("Movement Handler");
    this.vwMgr = view;
    this.em = effect;
    this.gui = gameGUI;
    this.saved = new OpenSpace();
  }

  // Methods
  @Override
  public void run() {
    try {
      while (true) {
        this.waitForWork();
        if (this.relative) {
          this.updatePositionRelative(this.moveX, this.moveY, this.moveZ);
        }
        if (!this.relative) {
          this.updatePositionAbsolute(this.moveX, this.moveY, this.moveZ);
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
    this.moveX = x;
    this.moveY = y;
    this.moveZ = z;
    this.relative = true;
    this.notify();
  }

  public synchronized void moveAbsolute(final int x, final int y, final int z) {
    this.moveX = x;
    this.moveY = y;
    this.moveZ = z;
    this.relative = false;
    this.notify();
  }

  public boolean tryAbsolute(final int x, final int y, final int z) {
    try {
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final Maze m = app.getMazeManager().getMaze();
      final FantastleObjectModel below = m.getCell(m.getPlayerLocationX(),
          m.getPlayerLocationY(), m.getPlayerLocationZ(), Layers.GROUND);
      final FantastleObjectModel nextBelow = m.getCell(x, y, z, Layers.GROUND);
      final FantastleObjectModel nextAbove = m.getCell(x, y, z, Layers.OBJECT);
      return MovementTask.checkSolidAbsolute(this.saved, below, nextBelow,
          nextAbove);
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  public void stopMovement() {
    this.proceed = false;
  }

  void fireStepActions() {
    final Maze m = FantastleReboot.getBagOStuff().getMazeManager().getMaze();
    final int px = m.getPlayerLocationX();
    final int py = m.getPlayerLocationY();
    final int pz = m.getPlayerLocationZ();
    m.updateVisibleSquares(px, py, pz);
    m.tickTimers(pz);
    PartyManager.getParty().fireStepActions();
    this.gui.updateStats();
    MovementTask.checkGameOver();
  }

  private void decayEffects() {
    this.em.decayEffects();
  }

  private int[] doEffects(final int x, final int y) {
    return this.em.doEffects(x, y);
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

  private void updatePositionRelative(final int dirX, final int dirY,
      final int dirZ) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    int px = m.getPlayerLocationX();
    int py = m.getPlayerLocationY();
    int pz = m.getPlayerLocationZ();
    int fX;
    int fY;
    final int fZ = dirZ;
    final int[] mod = this.doEffects(dirX, dirY);
    fX = mod[0];
    fY = mod[1];
    this.proceed = false;
    FantastleObjectModel below = null;
    FantastleObjectModel nextBelow = null;
    FantastleObjectModel nextAbove = new Wall();
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
      this.proceed = true;
      if (this.proceed) {
        m.savePlayerLocation();
        this.vwMgr.saveViewingWindow();
        try {
          if (MovementTask.checkSolid(this.saved, below, nextBelow,
              nextAbove)) {
            m.offsetPlayerLocationX(fX);
            m.offsetPlayerLocationY(fY);
            px += fX;
            py += fY;
            this.vwMgr.offsetViewingWindowLocationX(fY);
            this.vwMgr.offsetViewingWindowLocationY(fX);
            app.getMazeManager().setDirty(true);
            this.fireStepActions();
            this.decayEffects();
            this.redrawMaze();
            if (this.proceed) {
              this.saved = m.getCell(px, py, pz, Layers.OBJECT);
            }
          } else {
            // Move failed - object is solid in that direction
            bag.showMessage("Can't go that way");
            this.fireStepActions();
            this.decayEffects();
          }
        } catch (final ArrayIndexOutOfBoundsException ae) {
          this.vwMgr.restoreViewingWindow();
          m.restorePlayerLocation();
          // Move failed - attempted to go outside the maze
          bag.showMessage("Can't go that way");
          nextAbove = new OpenSpace();
          this.decayEffects();
          this.proceed = false;
        }
        this.fireStepActions();
      } else {
        // Move failed - pre-move check failed
        this.fireStepActions();
        this.decayEffects();
        this.proceed = false;
      }
      px = m.getPlayerLocationX();
      py = m.getPlayerLocationY();
      pz = m.getPlayerLocationZ();
    } while (this.checkLoopCondition(below, nextBelow, nextAbove));
  }

  private boolean checkLoopCondition(final FantastleObjectModel below,
      final FantastleObjectModel nextBelow,
      final FantastleObjectModel nextAbove) {
    return this.proceed
        && !this.em.isEffectActive(EffectConstants.EFFECT_STICKY)
        && !nextBelow.hasFriction()
        && MovementTask.checkSolid(this.saved, below, nextBelow, nextAbove);
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

  private void updatePositionAbsolute(final int x, final int y, final int z) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    m.savePlayerLocation();
    this.vwMgr.saveViewingWindow();
    try {
      if (!(m.getCell(x, y, z, Layers.OBJECT).isSolid())) {
        m.setPlayerLocationX(x);
        m.setPlayerLocationY(y);
        m.setPlayerLocationZ(z);
        this.vwMgr.setViewingWindowLocationX(m.getPlayerLocationY()
            - GameViewingWindowManager.getOffsetFactorX());
        this.vwMgr.setViewingWindowLocationY(m.getPlayerLocationX()
            - GameViewingWindowManager.getOffsetFactorY());
        this.saved = m.getCell(m.getPlayerLocationX(), m.getPlayerLocationY(),
            m.getPlayerLocationZ(), Layers.OBJECT);
        app.getMazeManager().setDirty(true);
        final int px = m.getPlayerLocationX();
        final int py = m.getPlayerLocationY();
        final int pz = m.getPlayerLocationZ();
        m.updateVisibleSquares(px, py, pz);
        this.redrawMaze();
      }
    } catch (final ArrayIndexOutOfBoundsException ae) {
      m.restorePlayerLocation();
      this.vwMgr.restoreViewingWindow();
      bag.showMessage("Can't go outside the maze");
    }
  }

  private static void checkGameOver() {
    if (!PartyManager.getParty().isAlive()) {
      SoundPlayer.playSound(SoundIndex.GAME_OVER);
      CommonDialogs.showDialog(
          "You have died! You lose 10% of your experience and all your Gold, but you are healed fully.");
      PartyManager.getParty().getLeader().onDeath(-10);
    }
  }

  private void redrawMaze() {
    this.gui.redrawMaze();
  }
}
