/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import javax.swing.JFrame;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.obsolete.Application;
import com.puttysoftware.fantastlereboot.obsolete.TallerTower;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.MazeObject;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.MazeObjectList;
import com.puttysoftware.fantastlereboot.obsolete.maze2.GenerateTask;
import com.puttysoftware.fantastlereboot.obsolete.maze2.Maze;
import com.puttysoftware.fantastlereboot.obsolete.maze2.MazeConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractMazeObject;
import com.puttysoftware.fantastlereboot.obsolete.maze2.objects.Empty;
import com.puttysoftware.fantastlereboot.obsolete.maze2.objects.EmptyVoid;
import com.puttysoftware.fantastlereboot.utilities.ArrowTypeConstants;
import com.puttysoftware.fantastlereboot.utilities.ImageConstants;
import com.puttysoftware.fantastlereboot.utilities.TypeConstants;

public final class GameLogicManager {
    // Fields
    private boolean savedGameFlag;
    private final GameViewingWindowManager vwMgr;
    private boolean stateChanged;
    private MazeObject objectBeingUsed;
    private ObjectInventory objectInv;
    private boolean pullInProgress;
    private boolean using;
    private int lastUsedObjectIndex;
//    private boolean keepNextMessage;
//    private boolean delayedDecayActive;
//    private MazeObject delayedDecayObject;
//    private boolean actingRemotely;
//    private int[] remoteCoords;
    private String gameOverMessage;
//    private boolean arrowActive;
    private int activeArrowType;
    private boolean isTeleporting;
    private final ScoreTracker scorer;
    private final GameGUIManager gui;
    private final EffectManager em;
    private final MovementTask mt;

    // Constructors
    public GameLogicManager() {
        this.vwMgr = new GameViewingWindowManager();
        this.em = new EffectManager();
        this.gui = new GameGUIManager();
        this.scorer = new ScoreTracker();
        this.mt = new MovementTask(this.vwMgr, this.em, this.gui);
        this.mt.start();
        this.pullInProgress = false;
        this.using = false;
        this.objectBeingUsed = null;
        this.lastUsedObjectIndex = 0;
//        this.keepNextMessage = false;
        this.savedGameFlag = false;
//        this.delayedDecayActive = false;
//        this.delayedDecayObject = null;
//        this.actingRemotely = false;
//        this.remoteCoords = new int[4];
//        this.arrowActive = false;
        this.isTeleporting = false;
        this.activeArrowType = ArrowTypeConstants.ARROW_TYPE_PLAIN;
        this.savedGameFlag = false;
        this.stateChanged = true;
    }

    // Methods
    public boolean newGame() {
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        final JFrame owner = bag.getOutputFrame();
        this.em.deactivateAllEffects();
        if (this.savedGameFlag) {
            if (PartyManager.getParty() != null) {
                return true;
            } else {
                return PartyManager.createParty(owner);
            }
        } else {
            return PartyManager.createParty(owner);
        }
    }

    public void enableEvents() {
        this.mt.fireStepActions();
        this.gui.enableEvents();
    }

    public void disableEvents() {
        this.gui.disableEvents();
    }

    public void stopMovement() {
        this.mt.stopMovement();
    }

    public void deactivateAllEffects() {
        this.em.deactivateAllEffects();
    }

    public void viewingWindowSizeChanged() {
        this.gui.viewingWindowSizeChanged(this.em);
        this.resetViewingWindow();
    }

    public void stateChanged() {
        this.stateChanged = true;
    }

    public ScoreTracker getScoreTracker() {
        return this.scorer;
    }

    public GameViewingWindowManager getViewManager() {
        return this.vwMgr;
    }

    public void setArrowType(final int type) {
        this.activeArrowType = type;
    }

    void arrowDone() {
//        this.arrowActive = false;
        this.activeArrowType = ArrowTypeConstants.ARROW_TYPE_PLAIN;
    }

    public void setSavedGameFlag(final boolean value) {
        this.savedGameFlag = value;
    }

    public void activateEffect(final int effectID) {
        this.em.activateEffect(effectID, -1);
    }

    public void setStatusMessage(final String msg) {
        this.gui.setStatusMessage(msg);
    }

    public void updatePositionRelative(final int dirX, final int dirY,
            final int dirZ) {
        this.mt.moveRelative(dirX, dirY, dirZ);
    }

    public boolean tryUpdatePositionAbsolute(final int x, final int y,
            final int z) {
        return this.mt.tryAbsolute(x, y, z);
    }

    public void updatePositionAbsolute(final int x, final int y, final int z) {
        this.mt.moveAbsolute(x, y, z);
    }

    public void redrawMaze() {
        this.gui.redrawMaze();
    }

    public void resetViewingWindowAndPlayerLocation() {
        GameLogicManager.resetPlayerLocation();
        this.resetViewingWindow();
    }

    public void resetViewingWindow() {
        final Application app = TallerTower.getApplication();
        final Maze m = app.getMazeManager().getMaze();
        if (m != null && this.vwMgr != null) {
            this.vwMgr.setViewingWindowLocationX(m.getPlayerLocationY()
                    - GameViewingWindowManager.getOffsetFactorX());
            this.vwMgr.setViewingWindowLocationY(m.getPlayerLocationX()
                    - GameViewingWindowManager.getOffsetFactorY());
        }
    }

    public static void resetPlayerLocation() {
        GameLogicManager.resetPlayerLocation(0);
    }

    public static void resetPlayerLocation(final int level) {
        final Application app = TallerTower.getApplication();
        final Maze m = app.getMazeManager().getMaze();
        if (m != null) {
            m.switchLevel(level);
            m.setPlayerToStart();
        }
    }

    public void resetObjectInventory() {
        this.objectInv = new ObjectInventory();
    }

    public boolean isTeleporting() {
        return this.isTeleporting;
    }

    public boolean usingAnItem() {
        return this.using;
    }

    public void setUsingAnItem(final boolean isUsing) {
        this.using = isUsing;
    }

    public boolean isPullInProgress() {
        return this.pullInProgress;
    }

    public void setPullInProgress(final boolean pulling) {
        this.pullInProgress = pulling;
    }

    public void activateEffect(final int effectID, final int duration) {
        this.em.activateEffect(effectID, duration);
    }

    int[] doEffects(final int x, final int y) {
        return this.em.doEffects(x, y);
    }

    public void setRemoteAction(final int x, final int y, final int z,
            final int w) {
//        this.remoteCoords = new int[] { x, y, z, w };
//        this.actingRemotely = true;
    }

    public void doRemoteAction(final int x, final int y, final int z,
            final int w) {
        this.setRemoteAction(x, y, z, w);
        // final MazeObject acted =
        // FantastleReboot.getBagOStuff().getMazeManager()
        // .getMazeObject(x, y, z, w, Maze.LAYER_OBJECT);
        // acted.postMoveAction(false, x, y, this.objectInv);
        // if (acted.doesChainReact()) {
        // acted.chainReactionAction(x, y, z, w);
        // }
    }

    @SuppressWarnings("static-method")
    public boolean isFloorBelow() {
        return false;
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        try {
//            m.getCell(this.plMgr.getPlayerLocationX(),
//                    this.plMgr.getPlayerLocationY(),
//                    this.plMgr.getPlayerLocationZ() - 1,
//                    this.plMgr.getPlayerLocationW(), Maze.LAYER_OBJECT);
//            return true;
//        } catch (final ArrayIndexOutOfBoundsException ae) {
//            return false;
//        } catch (final NullPointerException np) {
//            return false;
//        }
    }

    @SuppressWarnings("static-method")
    public boolean isFloorAbove() {
        return false;
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        try {
//            m.getCell(this.plMgr.getPlayerLocationX(),
//                    this.plMgr.getPlayerLocationY(),
//                    this.plMgr.getPlayerLocationZ() + 1,
//                    this.plMgr.getPlayerLocationW(), Maze.LAYER_OBJECT);
//            return true;
//        } catch (final ArrayIndexOutOfBoundsException ae) {
//            return false;
//        } catch (final NullPointerException np) {
//            return false;
//        }
    }

    @SuppressWarnings("static-method")
    public boolean isLevelBelow() {
        return false;
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        try {
//            m.getCell(this.plMgr.getPlayerLocationX(),
//                    this.plMgr.getPlayerLocationY(),
//                    this.plMgr.getPlayerLocationZ(),
//                    this.plMgr.getPlayerLocationW() - 1, Maze.LAYER_OBJECT);
//            return true;
//        } catch (final ArrayIndexOutOfBoundsException ae) {
//            return false;
//        } catch (final NullPointerException np) {
//            return false;
//        }
    }

    @SuppressWarnings("static-method")
    public boolean isLevelAbove() {
        return false;
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        try {
//            m.getCell(this.plMgr.getPlayerLocationX(),
//                    this.plMgr.getPlayerLocationY(),
//                    this.plMgr.getPlayerLocationZ(),
//                    this.plMgr.getPlayerLocationW() + 1, Maze.LAYER_OBJECT);
//            return true;
//        } catch (final ArrayIndexOutOfBoundsException ae) {
//            return false;
//        } catch (final NullPointerException np) {
//            return false;
//        }
    }

    @SuppressWarnings("static-method")
    public boolean doesFloorExist(final int floor) {
        return false;
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        try {
//            m.getCell(this.plMgr.getPlayerLocationX(),
//                    this.plMgr.getPlayerLocationY(), floor,
//                    this.plMgr.getPlayerLocationW(), Maze.LAYER_OBJECT);
//            return true;
//        } catch (final ArrayIndexOutOfBoundsException ae) {
//            return false;
//        } catch (final NullPointerException np) {
//            return false;
//        }
    }

    @SuppressWarnings("static-method")
    public boolean doesLevelExist(final int level) {
        return false;
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        try {
//            m.getCell(this.plMgr.getPlayerLocationX(),
//                    this.plMgr.getPlayerLocationY(),
//                    this.plMgr.getPlayerLocationZ(), level, Maze.LAYER_OBJECT);
//            return true;
//        } catch (final ArrayIndexOutOfBoundsException ae) {
//            return false;
//        } catch (final NullPointerException np) {
//            return false;
//        }
    }

    public void doClockwiseRotate(final int r) {
//        final Maze m = FantastleReboot.getBagOStuff().getMazeManager()
//                .getMaze();
//        boolean b = false;
//        if (this.actingRemotely) {
//            b = m.rotateRadiusClockwise(this.remoteCoords[0],
//                    this.remoteCoords[1], this.remoteCoords[2],
//                    this.remoteCoords[3], r);
//        } else {
//            b = m.rotateRadiusClockwise(this.plMgr.getPlayerLocationX(),
//                    this.plMgr.getPlayerLocationY(),
//                    this.plMgr.getPlayerLocationZ(),
//                    this.plMgr.getPlayerLocationW(), r);
//        }
//        if (b) {
//            this.findPlayerAndAdjust();
//        } else {
//            this.keepNextMessage();
//            Messager.showMessage("Rotation failed!");
//        }
    }

    public void doCounterclockwiseRotate(final int r) {
//        final Maze m = FantastleReboot.getBagOStuff().getMazeManager()
//                .getMaze();
//        boolean b = false;
//        if (this.actingRemotely) {
//            b = m.rotateRadiusCounterclockwise(this.remoteCoords[0],
//                    this.remoteCoords[1], this.remoteCoords[2],
//                    this.remoteCoords[3], r);
//        } else {
//            b = m.rotateRadiusCounterclockwise(this.plMgr.getPlayerLocationX(),
//                    this.plMgr.getPlayerLocationY(),
//                    this.plMgr.getPlayerLocationZ(),
//                    this.plMgr.getPlayerLocationW(), r);
//        }
//        if (b) {
//            this.findPlayerAndAdjust();
//        } else {
//            this.keepNextMessage();
//            Messager.showMessage("Rotation failed!");
//        }
    }

    public void findPlayerAndAdjust() {
        // Find the player, adjust player location
//        final Maze m = FantastleReboot.getBagOStuff().getMazeManager()
//                .getMaze();
//        final int w = this.plMgr.getPlayerLocationW();
//        m.findPlayerOnLevel(w);
//        this.plMgr.setPlayerLocation(m.getFindResultColumn(w),
//                m.getFindResultRow(w), m.getFindResultFloor(w), w);
//        this.resetViewingWindow();
//        this.redrawMaze();
    }

    public void fireArrow(final int x, final int y) {
        final ArrowTask at = new ArrowTask(x, y, this.activeArrowType);
//        this.arrowActive = true;
        at.start();
    }

    public void goToLevelOffset(final int level) {
        final Application app = TallerTower.getApplication();
        final Maze m = app.getMazeManager().getMaze();
        final boolean levelExists = m.doesLevelExistOffset(level);
        this.stopMovement();
        if (levelExists) {
            new LevelLoadTask(level).start();
        } else {
            new GenerateTask(false).start();
        }
    }

    public void exitGame() {
        this.stateChanged = true;
        final Application app = TallerTower.getApplication();
        final Maze m = app.getMazeManager().getMaze();
        // Restore the maze
        m.restore();
        m.resetVisibleSquares();
        final boolean playerExists = m.doesPlayerExist();
        if (playerExists) {
            this.resetViewingWindowAndPlayerLocation();
        } else {
            app.getMazeManager().setLoaded(false);
        }
        // Reset saved game flag
        this.savedGameFlag = false;
        app.getMazeManager().setDirty(false);
        // Exit game
        this.hideOutput();
        app.getGUIManager().showGUI();
    }

    public void invalidateScore() {
        this.scorer.invalidateScore();
    }

    public void showCurrentScore() {
        this.scorer.showCurrentScore();
    }

    public void showScoreTable() {
        this.scorer.showScoreTable();
    }

    public void validateScore() {
        this.scorer.validateScore();
    }

    public JFrame getOutputFrame() {
        return this.gui.getOutputFrame();
    }

    public static void decay() {
        final Application app = TallerTower.getApplication();
        final Maze m = app.getMazeManager().getMaze();
        m.setCell(new Empty(), m.getPlayerLocationX(), m.getPlayerLocationY(),
                m.getPlayerLocationZ(), MazeConstants.LAYER_OBJECT);
    }

    public static void morph(final AbstractMazeObject morphInto) {
        final Application app = TallerTower.getApplication();
        final Maze m = app.getMazeManager().getMaze();
        m.setCell(morphInto, m.getPlayerLocationX(), m.getPlayerLocationY(),
                m.getPlayerLocationZ(), morphInto.getLayer());
    }

    public void keepNextMessage() {
        this.gui.keepNextMessage();
    }

    public void identifyObject(final int x, final int y) {
        final Application app = TallerTower.getApplication();
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        final Maze m = app.getMazeManager().getMaze();
        final int xOffset = this.vwMgr.getViewingWindowLocationX()
                - GameViewingWindowManager.getOffsetFactorX();
        final int yOffset = this.vwMgr.getViewingWindowLocationY()
                - GameViewingWindowManager.getOffsetFactorY();
        final int destX = x / ImageConstants.SIZE
                + this.vwMgr.getViewingWindowLocationX() - xOffset + yOffset;
        final int destY = y / ImageConstants.SIZE
                + this.vwMgr.getViewingWindowLocationY() + xOffset - yOffset;
        final int destZ = m.getPlayerLocationZ();
        try {
            final AbstractMazeObject target1 = m.getCell(destX, destY, destZ,
                    MazeConstants.LAYER_GROUND);
            final AbstractMazeObject target2 = m.getCell(destX, destY, destZ,
                    MazeConstants.LAYER_OBJECT);
            target1.determineCurrentAppearance(destX, destY, destZ);
            target2.determineCurrentAppearance(destX, destY, destZ);
            final String gameName1 = target1.getGameName();
            final String gameName2 = target2.getGameName();
            bag.showMessage(gameName2 + " on " + gameName1);
            SoundPlayer.playSound(SoundIndex.IDENTIFY);
        } catch (final ArrayIndexOutOfBoundsException ae) {
            final EmptyVoid ev = new EmptyVoid();
            ev.determineCurrentAppearance(destX, destY, destZ);
            bag.showMessage(ev.getGameName());
            SoundPlayer.playSound(SoundIndex.IDENTIFY);
        }
    }

    public void playMaze() {
        final Application app = TallerTower.getApplication();
        final Maze m = app.getMazeManager().getMaze();
        if (app.getMazeManager().getLoaded()) {
            this.gui.initViewManager();
            app.getGUIManager().hideGUI();
            if (this.stateChanged) {
                // Initialize only if the maze state has changed
                app.getMazeManager().getMaze().switchLevel(
                        app.getMazeManager().getMaze().getStartLevel());
                this.stateChanged = false;
            }
            // Make sure message area is attached to the border pane
            this.gui.updateGameGUI(this.em);
            // Make sure initial area player is in is visible
            final int px = m.getPlayerLocationX();
            final int py = m.getPlayerLocationY();
            final int pz = m.getPlayerLocationZ();
            m.updateVisibleSquares(px, py, pz);
            this.showOutput();
            this.redrawMaze();
        } else {
            CommonDialogs.showDialog("No Maze Opened");
        }
    }

    public void updateStats() {
        // Update stats
        this.gui.updateStats();
        // Check for game over
        if (!PartyManager.getParty().getLeader().isAlive()) {
            this.gameOver();
        }
    }

    public void resetCurrentLevel() {
//        this.resetLevel(this.plMgr.getPlayerLocationW());
    }

    public void resetGameState() {
//        this.deactivateAllEffects();
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        app.getMazeManager().setDirty(false);
//        m.restore();
//        this.setSavedGameFlag(false);
//        this.scorer.resetScore(app.getMazeManager().getScoresFileName());
//        this.decay();
//        this.objectInv = new ObjectInventory();
//        this.savedObjectInv = new ObjectInventory();
//        final int startW = m.getStartLevel();
//        final boolean playerExists = m.findPlayerOnLevel(startW);
//        if (playerExists) {
//            m.findAllStarts();
//            this.plMgr.setPlayerLocation(m.getFindResultColumn(startW),
//                    m.getFindResultRow(startW), m.getFindResultFloor(startW),
//                    startW);
//            m.save();
//        }
    }

    public void resetLevel(final int level) {
//        PartyManager.getParty().getLeader().healAndRegenerateFully();
//        this.deactivateAllEffects();
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        app.getMazeManager().setDirty(true);
//        m.restoreLevel(level);
//        final boolean playerExists = m.findPlayerOnLevel(level);
//        if (playerExists) {
//            this.scorer.resetScore(app.getMazeManager().getScoresFileName());
//            this.resetPlayerLocation(level);
//            this.resetViewingWindow();
//            m.setVisionRadiusToMaximum(level);
//            this.decay();
//            this.restoreObjectInventory();
//            this.redrawMaze();
//        }
    }

    public void solvedLevel() {
//        this.deactivateAllEffects();
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        final boolean playerExists = m
//                .findPlayerOnLevel(this.plMgr.getPlayerLocationW() + 1);
//        if (playerExists) {
//            m.restoreLevel(this.plMgr.getPlayerLocationW());
//            this.resetPlayerLocation(this.plMgr.getPlayerLocationW() + 1);
//            this.resetViewingWindow();
//            m.setVisionRadiusToMaximum(this.plMgr.getPlayerLocationW());
//            this.decay();
//            this.saveObjectInventory();
//            this.redrawMaze();
//        } else {
//            this.solvedMaze();
//        }
    }

    public void solvedLevelWarp(final int level) {
//        this.deactivateAllEffects();
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        final boolean playerExists = m.findPlayerOnLevel(level);
//        if (playerExists) {
//            m.restoreLevel(this.plMgr.getPlayerLocationW());
//            this.resetPlayerLocation(level);
//            this.resetViewingWindow();
//            this.decay();
//            this.saveObjectInventory();
//            this.redrawMaze();
//        } else {
//            this.solvedMaze();
//        }
    }

    private void gameOver() {
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_UI)) {
            SoundPlayer.playSound(SoundIndex.GAME_OVER);
        }
        if (this.gameOverMessage == null) {
            Messager.showDialog("You have died - Game Over!");
        } else {
            Messager.showDialog(this.gameOverMessage);
        }
        this.solvedMaze();
    }

    public void gameOverWithMessage(final String msg) {
        this.gameOverMessage = msg;
    }

    public void solvedMaze() {
//        PartyManager.getParty().getLeader().healAndRegenerateFully();
//        this.deactivateAllEffects();
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        // Restore the maze
//        m.restoreLevel(this.plMgr.getPlayerLocationW());
//        final boolean playerExists = m.findPlayerOnLevel(0);
//        if (playerExists) {
//            this.resetViewingWindowAndPlayerLocation();
//        } else {
//            app.getMazeManager().setLoaded(false);
//        }
//        // Wipe the inventory
//        this.objectInv = new ObjectInventory();
//        // Reset saved game flag
//        this.savedGameFlag = false;
//        app.getMazeManager().setDirty(false);
//        if (this.scorer.checkScore()) {
//            app.playHighScoreSound();
//        }
//        this.scorer.commitScore();
//        this.hideOutput();
//        app.getGUIManager().showGUI();
    }

    public void showInventoryDialog() {
        final String[] inv1String = this.objectInv
                .generateInventoryStringArray();
        final String[] inv2String = PartyManager.getParty().getLeader()
                .getItems().generateInventoryStringArray(inv1String.length);
        final String[] invString = new String[inv1String.length
                + inv2String.length];
        for (int x = 0; x < inv1String.length + inv2String.length; x++) {
            if (x < inv1String.length) {
                invString[x] = inv1String[x];
            } else {
                invString[x] = inv2String[x - inv1String.length];
            }
        }
        Messager.showInputDialog("Inventory", "Inventory", invString,
                invString[0]);
    }

    public void showUseDialog() {
        int x;
        final MazeObjectList list = FantastleReboot.getBagOStuff().getObjects();
        final MazeObject[] choices = list.getAllUsableObjects();
        final String[] userChoices = this.objectInv.generateUseStringArray();
        final String result = Messager.showInputDialog("Use which item?",
                "Fantastle", userChoices,
                userChoices[this.lastUsedObjectIndex]);
        try {
            for (x = 0; x < choices.length; x++) {
                if (result.equals(userChoices[x])) {
                    this.lastUsedObjectIndex = x;
                    this.objectBeingUsed = choices[x];
                    if (this.objectInv.getUses(this.objectBeingUsed) == 0) {
                        Messager.showMessage(
                                "That item has no more uses left.");
                        this.setUsingAnItem(false);
                    } else if (!this.objectBeingUsed
                            .isOfType(TypeConstants.TYPE_BOW)) {
                        Messager.showMessage("Click to set target");
                    } else {
                        this.useItemHandler(0, 0);
                        this.setUsingAnItem(false);
                    }
                    return;
                }
            }
        } catch (final NullPointerException np) {
            this.setUsingAnItem(false);
        }
    }

    public ObjectInventory getObjectInventory() {
        return this.objectInv;
    }

    public void setObjectInventory(final ObjectInventory newObjectInventory) {
        this.objectInv = newObjectInventory;
    }

    public void useItemHandler(final int x, final int y) {
//        final BagOStuff app = FantastleReboot.getBagOStuff();
//        final Maze m = app.getMazeManager().getMaze();
//        final int xOffset = this.vwMgr.getViewingWindowLocationX()
//                - GameViewingWindowManager.getOffsetFactorX();
//        final int yOffset = this.vwMgr.getViewingWindowLocationY()
//                - GameViewingWindowManager.getOffsetFactorY();
//        final int destX = x / ImageConstants.SIZE
//                + this.vwMgr.getViewingWindowLocationX() - xOffset + yOffset;
//        final int destY = y / ImageConstants.SIZE
//                + this.vwMgr.getViewingWindowLocationY() + xOffset - yOffset;
//        final int destZ = this.plMgr.getPlayerLocationZ();
//        final int destW = this.plMgr.getPlayerLocationW();
//        if (this.usingAnItem() && app.getMode() == BagOStuff.STATUS_GAME) {
//            if (!this.objectBeingUsed.isOfType(TypeConstants.TYPE_BOW)) {
//                final boolean visible = app.getMazeManager().getMaze()
//                        .isSquareVisible(this.plMgr.getPlayerLocationX(),
//                                this.plMgr.getPlayerLocationY(), destX, destY,
//                                this.plMgr.getPlayerLocationW());
//                try {
//                    final MazeObject target = m.getCell(destX, destY, destZ,
//                            destW, Maze.LAYER_OBJECT);
//                    final String name = this.objectBeingUsed.getName();
//                    if ((target.isSolid() || !visible)
//                            && name.equals(new TeleportWand().getName())) {
//                        this.setUsingAnItem(false);
//                        Messager.showMessage("Can't teleport there");
//                    }
//                    if (target.getName().equals(new Player().getName())) {
//                        this.setUsingAnItem(false);
//                        Messager.showMessage("Don't aim at yourself!");
//                    }
//                    if (!target.isDestroyable()
//                            && name.equals(new AnnihilationWand().getName())) {
//                        this.setUsingAnItem(false);
//                        Messager.showMessage("Can't destroy that");
//                    }
//                    if (!target.isDestroyable()
//                            && name.equals(new WallMakingWand().getName())) {
//                        this.setUsingAnItem(false);
//                        Messager.showMessage("Can't create a wall there");
//                    }
//                    if (!target.isDestroyable()
//                            && name.equals(new FinishMakingWand().getName())) {
//                        this.setUsingAnItem(false);
//                        Messager.showMessage("Can't create a finish there");
//                    }
//                    if ((!target.isDestroyable()
//                            || !target.isOfType(TypeConstants.TYPE_WALL))
//                            && name.equals(new WallBreakingWand().getName())) {
//                        this.setUsingAnItem(false);
//                        Messager.showMessage("Aim at a wall");
//                    }
//                    if ((!target.isDestroyable()
//                            || !target.isOfType(TypeConstants.TYPE_TRAP))
//                            && name.equals(new DisarmTrapWand().getName())) {
//                        this.setUsingAnItem(false);
//                        Messager.showMessage("Aim at a trap");
//                    }
//                } catch (final ArrayIndexOutOfBoundsException ae) {
//                    this.setUsingAnItem(false);
//                    Messager.showMessage("Aim within the maze");
//                } catch (final NullPointerException np) {
//                    this.setUsingAnItem(false);
//                }
//            }
//            if (this.usingAnItem()) {
//                this.objectInv.use(this.objectBeingUsed, destX, destY, destZ,
//                        destW);
//                this.redrawMaze();
//            }
//        }
    }

    public void controllableTeleport() {
        this.isTeleporting = true;
        Messager.showMessage("Click to set destination");
    }

    void controllableTeleportHandler(final int x, final int y) {
//        if (this.isTeleporting) {
//            final int xOffset = this.vwMgr.getViewingWindowLocationX()
//                    - GameViewingWindowManager.getOffsetFactorX();
//            final int yOffset = this.vwMgr.getViewingWindowLocationY()
//                    - GameViewingWindowManager.getOffsetFactorY();
//            final int destX = x / ImageConstants.SIZE
//                    + this.vwMgr.getViewingWindowLocationX() - xOffset
//                    + yOffset;
//            final int destY = y / ImageConstants.SIZE
//                    + this.vwMgr.getViewingWindowLocationY() + xOffset
//                    - yOffset;
//            final int destZ = this.plMgr.getPlayerLocationZ();
//            final int destW = this.plMgr.getPlayerLocationW();
//            this.updatePositionAbsolute(destX, destY, destZ, destW);
//            if (FantastleReboot.getBagOStuff().getPrefsManager()
//                    .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
//                SoundPlayer.playSound(GameSound.TELEPORT);
//            }
//            this.isTeleporting = false;
//        }
    }

    public void delayedDecayTo(final MazeObject obj) {
//        this.delayedDecayActive = true;
//        this.delayedDecayObject = obj;
    }

    public void showOutput() {
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        bag.setInGame(true);
        this.gui.showOutput();
    }

    public void hideOutput() {
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        bag.setInGame(false);
        this.stopMovement();
        this.gui.hideOutput();
    }
}
