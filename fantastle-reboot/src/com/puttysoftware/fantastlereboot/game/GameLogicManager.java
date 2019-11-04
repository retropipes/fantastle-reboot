/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import javax.swing.JFrame;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.loaders.MusicPlayer;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.GenerateTask;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Player;
import com.puttysoftware.fantastlereboot.objects.temporary.ArrowType;
import com.puttysoftware.fantastlereboot.utilities.ImageConstants;

public final class GameLogicManager {
  // Fields
  private boolean savedGameFlag;
  private final GameViewingWindowManager vwMgr;
  private boolean stateChanged;
  private ObjectInventory objectInv;
  private boolean pullInProgress;
  private boolean using;
  private FantastleObjectModel objectBeingUsed;
  private String gameOverMessage;
  private ArrowType activeArrowType;
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
    this.savedGameFlag = false;
    this.isTeleporting = false;
    this.activeArrowType = ArrowType.WOODEN;
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

  public void setArrowType(final ArrowType type) {
    this.activeArrowType = type;
  }

  void arrowDone() {
    this.activeArrowType = ArrowType.WOODEN;
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

  public void redrawOneSquare(final int inX, final int inY,
      final FantastleObjectModel obj4) {
    this.gui.redrawOneSquare(inX, inY, obj4);
  }

  public void resetViewingWindowAndPlayerLocation() {
    GameLogicManager.resetPlayerLocation();
    this.resetViewingWindow();
  }

  public void resetViewingWindow() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    if (m != null && this.vwMgr != null) {
      this.vwMgr.setViewingWindowLocationX(
          m.getPlayerLocationY() - GameViewingWindowManager.getOffsetFactorX());
      this.vwMgr.setViewingWindowLocationY(
          m.getPlayerLocationX() - GameViewingWindowManager.getOffsetFactorY());
    }
  }

  public static void resetPlayerLocation() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    if (m != null) {
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

  public static boolean isFloorBelow() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    try {
      m.getCell(m.getPlayerLocationX(), m.getPlayerLocationY(),
          m.getPlayerLocationZ() - 1, Layers.OBJECT);
      return true;
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  public static boolean isFloorAbove() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    try {
      m.getCell(m.getPlayerLocationX(), m.getPlayerLocationY(),
          m.getPlayerLocationZ() + 1, Layers.OBJECT);
      return true;
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  public static boolean isLevelBelow() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    try {
      m.getCell(m.getPlayerLocationX(), m.getPlayerLocationY(),
          m.getPlayerLocationZ() - 1, Layers.OBJECT);
      return true;
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  public static boolean isLevelAbove() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    try {
      m.getCell(m.getPlayerLocationX(), m.getPlayerLocationY(),
          m.getPlayerLocationZ() + 1, Layers.OBJECT);
      return true;
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  public static boolean doesFloorExist(final int floor) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    try {
      m.getCell(m.getPlayerLocationX(), m.getPlayerLocationY(), floor,
          Layers.OBJECT);
      return true;
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  public static boolean doesLevelExist(final int level) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    try {
      m.getCell(m.getPlayerLocationX(), m.getPlayerLocationY(),
          m.getPlayerLocationZ(), Layers.OBJECT);
      return true;
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  public void fireArrow(final int x, final int y) {
    final ArrowTask at = new ArrowTask(x, y, this.activeArrowType);
    at.start();
  }

  public void goToLevelOffset(final int level) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
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
    final BagOStuff app = FantastleReboot.getBagOStuff();
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
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    m.setCell(new OpenSpace(), m.getPlayerLocationX(), m.getPlayerLocationY(),
        m.getPlayerLocationZ(), Layers.OBJECT);
  }

  public static void morph(final FantastleObjectModel morphInto) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    m.setCell(morphInto, m.getPlayerLocationX(), m.getPlayerLocationY(),
        m.getPlayerLocationZ(), morphInto.getLayer());
  }

  public void keepNextMessage() {
    this.gui.keepNextMessage();
  }

  public void playMaze() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    if (app.getMazeManager().getLoaded()) {
      this.gui.initViewManager();
      app.getGUIManager().hideGUI();
      if (this.stateChanged) {
        // Initialize only if the maze state has changed
        app.getMazeManager().getMaze()
            .switchLevel(app.getMazeManager().getMaze().getStartLevel());
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
    this.resetLevel();
  }

  public void resetGameState() {
    this.deactivateAllEffects();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    app.getMazeManager().setDirty(false);
    m.restore();
    this.setSavedGameFlag(false);
    this.scorer.resetScore();
    GameLogicManager.decay();
    this.objectInv = new ObjectInventory();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      m.save();
    }
  }

  public void resetLevel() {
    PartyManager.getParty().getLeader().healAndRegenerateFully();
    this.deactivateAllEffects();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    app.getMazeManager().setDirty(true);
    m.restore();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      this.scorer.resetScore();
      GameLogicManager.resetPlayerLocation();
      this.resetViewingWindow();
      GameLogicManager.decay();
      this.redrawMaze();
    }
  }

  public void solvedLevel() {
    this.deactivateAllEffects();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      m.restore();
      GameLogicManager.resetPlayerLocation();
      this.resetViewingWindow();
      GameLogicManager.decay();
      this.redrawMaze();
    } else {
      this.solvedMaze();
    }
  }

  private void gameOver() {
    SoundPlayer.playSound(SoundIndex.GAME_OVER, SoundGroup.GAME);
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
    PartyManager.getParty().getLeader().healAndRegenerateFully();
    this.deactivateAllEffects();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    // Restore the maze
    m.restore();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      this.resetViewingWindowAndPlayerLocation();
    } else {
      app.getMazeManager().setLoaded(false);
    }
    // Wipe the inventory
    this.objectInv = new ObjectInventory();
    // Reset saved game flag
    this.savedGameFlag = false;
    app.getMazeManager().setDirty(false);
    if (this.scorer.checkScore()) {
      app.playHighScoreSound();
    }
    this.scorer.commitScore();
    this.hideOutput();
    app.getGUIManager().showGUI();
  }

  public ObjectInventory getObjectInventory() {
    return this.objectInv;
  }

  public void setObjectInventory(final ObjectInventory newObjectInventory) {
    this.objectInv = newObjectInventory;
  }

  public void useItemHandler(final int x, final int y) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
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
    if (this.usingAnItem() && app.getMode() == BagOStuff.STATUS_GAME) {
      try {
        final FantastleObjectModel target = m.getCell(destX, destY, destZ,
            Layers.OBJECT);
        if (target instanceof Player) {
          this.setUsingAnItem(false);
          Messager.showMessage("Don't aim at yourself!");
        }
      } catch (final ArrayIndexOutOfBoundsException ae) {
        this.setUsingAnItem(false);
        Messager.showMessage("Aim within the maze");
      }
      if (this.usingAnItem()) {
        this.objectInv.use(this.objectBeingUsed);
        this.redrawMaze();
      }
    }
  }

  public void controllableTeleport() {
    this.isTeleporting = true;
    Messager.showMessage("Click to set destination");
  }

  void controllableTeleportHandler(final int x, final int y) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    if (this.isTeleporting) {
      final int xOffset = this.vwMgr.getViewingWindowLocationX()
          - GameViewingWindowManager.getOffsetFactorX();
      final int yOffset = this.vwMgr.getViewingWindowLocationY()
          - GameViewingWindowManager.getOffsetFactorY();
      final int destX = x / ImageConstants.SIZE
          + this.vwMgr.getViewingWindowLocationX() - xOffset + yOffset;
      final int destY = y / ImageConstants.SIZE
          + this.vwMgr.getViewingWindowLocationY() + xOffset - yOffset;
      final int destZ = m.getPlayerLocationZ();
      this.updatePositionAbsolute(destX, destY, destZ);
      SoundPlayer.playSound(SoundIndex.TELEPORT, SoundGroup.GAME);
      this.isTeleporting = false;
    }
  }

  public void showOutput() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    bag.setInGame();
    MusicPlayer.playMusic(MusicIndex.DUNGEON);
    this.gui.showOutput();
  }

  public void hideOutput() {
    this.stopMovement();
    this.gui.hideOutput();
  }
}
