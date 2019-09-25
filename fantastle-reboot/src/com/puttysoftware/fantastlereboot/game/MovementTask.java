/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.obsolete.Application;
import com.puttysoftware.fantastlereboot.obsolete.TallerTower;
import com.puttysoftware.fantastlereboot.obsolete.maze2.Maze;
import com.puttysoftware.fantastlereboot.obsolete.maze2.MazeConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractMazeObject;
import com.puttysoftware.fantastlereboot.obsolete.maze2.objects.Empty;
import com.puttysoftware.fantastlereboot.obsolete.maze2.objects.Wall;
import com.puttysoftware.fantastlereboot.utilities.TypeConstants;

final class MovementTask extends Thread {
    // Fields
    private final GameViewingWindowManager vwMgr;
    private final GameGUIManager gui;
    private final EffectManager em;
    private AbstractMazeObject saved;
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
        this.saved = new Empty();
    }

    // Methods
    @Override
    public void run() {
        try {
            while (true) {
                this.waitForWork();
                if (this.relative) {
                    this.updatePositionRelative(this.moveX, this.moveY,
                            this.moveZ);
                }
                if (!this.relative) {
                    this.updatePositionAbsolute(this.moveX, this.moveY,
                            this.moveZ);
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

    public synchronized void moveRelative(final int x, final int y,
            final int z) {
        this.moveX = x;
        this.moveY = y;
        this.moveZ = z;
        this.relative = true;
        this.notify();
    }

    public synchronized void moveAbsolute(final int x, final int y,
            final int z) {
        this.moveX = x;
        this.moveY = y;
        this.moveZ = z;
        this.relative = false;
        this.notify();
    }

    public boolean tryAbsolute(final int x, final int y, final int z) {
        try {
            final Application app = TallerTower.getApplication();
            final Maze m = app.getMazeManager().getMaze();
            final AbstractMazeObject below = m.getCell(m.getPlayerLocationX(),
                    m.getPlayerLocationY(), m.getPlayerLocationZ(),
                    MazeConstants.LAYER_GROUND);
            final AbstractMazeObject nextBelow = m.getCell(x, y, z,
                    MazeConstants.LAYER_GROUND);
            final AbstractMazeObject nextAbove = m.getCell(x, y, z,
                    MazeConstants.LAYER_OBJECT);
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
        final Maze m = TallerTower.getApplication().getMazeManager().getMaze();
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

    private static boolean checkSolidAbsolute(final AbstractMazeObject inside,
            final AbstractMazeObject below, final AbstractMazeObject nextBelow,
            final AbstractMazeObject nextAbove) {
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
        final Application app = TallerTower.getApplication();
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
        AbstractMazeObject below = null;
        AbstractMazeObject nextBelow = null;
        AbstractMazeObject nextAbove = new Wall();
        do {
            try {
                try {
                    below = m.getCell(px, py, pz, MazeConstants.LAYER_GROUND);
                } catch (final ArrayIndexOutOfBoundsException ae) {
                    below = new Empty();
                }
                try {
                    nextBelow = m.getCell(px + fX, py + fY, pz + fZ,
                            MazeConstants.LAYER_GROUND);
                } catch (final ArrayIndexOutOfBoundsException ae) {
                    nextBelow = new Empty();
                }
                try {
                    nextAbove = m.getCell(px + fX, py + fY, pz + fZ,
                            MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException ae) {
                    nextAbove = new Wall();
                }
                try {
                    this.proceed = nextAbove.preMoveAction(true, px + fX,
                            py + fY);
                } catch (final ArrayIndexOutOfBoundsException ae) {
                    this.proceed = true;
                }
            } catch (final NullPointerException np) {
                this.proceed = false;
                this.decayEffects();
                nextAbove = new Empty();
            }
            if (this.proceed) {
                m.savePlayerLocation();
                this.vwMgr.saveViewingWindow();
                try {
                    if (MovementTask.checkSolid(this.saved, below, nextBelow,
                            nextAbove)) {
                        AbstractMazeObject groundInto;
                        m.offsetPlayerLocationX(fX);
                        m.offsetPlayerLocationY(fY);
                        px += fX;
                        py += fY;
                        this.vwMgr.offsetViewingWindowLocationX(fY);
                        this.vwMgr.offsetViewingWindowLocationY(fX);
                        app.getMazeManager().setDirty(true);
                        app.saveFormerMode();
                        this.fireStepActions();
                        this.decayEffects();
                        this.redrawMaze();
                        if (app.modeChanged()) {
                            this.proceed = false;
                        }
                        if (this.proceed) {
                            this.saved = m.getCell(px, py, pz,
                                    MazeConstants.LAYER_OBJECT);
                            groundInto = m.getCell(px, py, pz,
                                    MazeConstants.LAYER_GROUND);
                            if (groundInto.overridesDefaultPostMove()) {
                                groundInto.postMoveAction(false, px, py);
                                if (!this.saved.isOfType(
                                        TypeConstants.TYPE_PASS_THROUGH)) {
                                    this.saved.postMoveAction(false, px, py);
                                }
                            } else {
                                this.saved.postMoveAction(false, px, py);
                            }
                        }
                    } else {
                        // Move failed - object is solid in that direction
                        MovementTask.fireMoveFailedActions(px + fX, py + fY,
                                this.saved, below, nextBelow, nextAbove);
                        this.fireStepActions();
                        this.decayEffects();
                    }
                } catch (final ArrayIndexOutOfBoundsException ae) {
                    this.vwMgr.restoreViewingWindow();
                    m.restorePlayerLocation();
                    // Move failed - attempted to go outside the maze
                    nextAbove.moveFailedAction(false, px, py);
                    app.showMessage("Can't go that way");
                    nextAbove = new Empty();
                    this.decayEffects();
                    this.proceed = false;
                }
                this.fireStepActions();
            } else {
                // Move failed - pre-move check failed
                nextAbove.moveFailedAction(false, px + fX, py + fY);
                this.fireStepActions();
                this.decayEffects();
                this.proceed = false;
            }
            px = m.getPlayerLocationX();
            py = m.getPlayerLocationY();
            pz = m.getPlayerLocationZ();
        } while (this.checkLoopCondition(below, nextBelow, nextAbove));
    }

    private boolean checkLoopCondition(final AbstractMazeObject below,
            final AbstractMazeObject nextBelow,
            final AbstractMazeObject nextAbove) {
        return this.proceed
                && !this.em.isEffectActive(EffectConstants.EFFECT_STICKY)
                && !nextBelow.hasFriction() && MovementTask
                        .checkSolid(this.saved, below, nextBelow, nextAbove);
    }

    private static boolean checkSolid(final AbstractMazeObject inside,
            final AbstractMazeObject below, final AbstractMazeObject nextBelow,
            final AbstractMazeObject nextAbove) {
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

    private static void fireMoveFailedActions(final int x, final int y,
            final AbstractMazeObject inside, final AbstractMazeObject below,
            final AbstractMazeObject nextBelow,
            final AbstractMazeObject nextAbove) {
        final boolean insideSolid = inside.isSolid();
        final boolean belowSolid = below.isSolid();
        final boolean nextBelowSolid = nextBelow.isSolid();
        final boolean nextAboveSolid = nextAbove.isSolid();
        if (insideSolid) {
            inside.moveFailedAction(false, x, y);
        }
        if (belowSolid) {
            below.moveFailedAction(false, x, y);
        }
        if (nextBelowSolid) {
            nextBelow.moveFailedAction(false, x, y);
        }
        if (nextAboveSolid) {
            nextAbove.moveFailedAction(false, x, y);
        }
    }

    private void updatePositionAbsolute(final int x, final int y, final int z) {
        final Application app = TallerTower.getApplication();
        final Maze m = app.getMazeManager().getMaze();
        try {
            m.getCell(x, y, z, MazeConstants.LAYER_OBJECT).preMoveAction(true,
                    x, y);
        } catch (final ArrayIndexOutOfBoundsException ae) {
            // Ignore
        } catch (final NullPointerException np) {
            // Ignore
        }
        m.savePlayerLocation();
        this.vwMgr.saveViewingWindow();
        try {
            if (!(m.getCell(x, y, z, MazeConstants.LAYER_OBJECT).isSolid())) {
                m.setPlayerLocationX(x);
                m.setPlayerLocationY(y);
                m.setPlayerLocationZ(z);
                this.vwMgr.setViewingWindowLocationX(m.getPlayerLocationY()
                        - GameViewingWindowManager.getOffsetFactorX());
                this.vwMgr.setViewingWindowLocationY(m.getPlayerLocationX()
                        - GameViewingWindowManager.getOffsetFactorY());
                this.saved = m.getCell(m.getPlayerLocationX(),
                        m.getPlayerLocationY(), m.getPlayerLocationZ(),
                        MazeConstants.LAYER_OBJECT);
                app.getMazeManager().setDirty(true);
                this.saved.postMoveAction(false, x, y);
                final int px = m.getPlayerLocationX();
                final int py = m.getPlayerLocationY();
                final int pz = m.getPlayerLocationZ();
                m.updateVisibleSquares(px, py, pz);
                this.redrawMaze();
            }
        } catch (final ArrayIndexOutOfBoundsException ae) {
            m.restorePlayerLocation();
            this.vwMgr.restoreViewingWindow();
            app.showMessage("Can't go outside the maze");
        } catch (final NullPointerException np) {
            m.restorePlayerLocation();
            this.vwMgr.restoreViewingWindow();
            app.showMessage("Can't go outside the maze");
        }
    }

    private static void checkGameOver() {
        if (!PartyManager.getParty().isAlive()) {
            SoundLoader.playSound(GameSound.GAME_OVER);
            CommonDialogs.showDialog(
                    "You have died! You lose 10% of your experience and all your Gold, but you are healed fully.");
            PartyManager.getParty().getLeader().onDeath(-10);
        }
    }

    private void redrawMaze() {
        this.gui.redrawMaze();
    }
}
