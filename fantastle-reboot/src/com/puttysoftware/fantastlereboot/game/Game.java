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
import com.puttysoftware.fantastlereboot.assets.MusicGroup;
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

public final class Game {
  // Fields
  private static boolean savedGameFlag;
  private static boolean stateChanged;
  private static ObjectInventory objectInv;
  private static boolean pullInProgress;
  private static boolean using;
  private static FantastleObjectModel objectBeingUsed;
  private static String gameOverMessage;
  private static ArrowType activeArrowType;
  private static boolean isTeleporting;
  private final ScoreTracker scorer;
  private final MovementTask mt;

  // Constructors
  public Game() {
    this.scorer = new ScoreTracker();
    this.mt = new MovementTask();
    this.mt.start();
    Game.pullInProgress = false;
    Game.using = false;
    Game.savedGameFlag = false;
    Game.isTeleporting = false;
    Game.activeArrowType = ArrowType.WOODEN;
    Game.savedGameFlag = false;
    Game.stateChanged = true;
  }

  // Methods
  public boolean newGame() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final JFrame owner = bag.getOutputFrame();
    EffectManager.deactivateAllEffects();
    if (Game.savedGameFlag) {
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
    MovementTask.fireStepActions();
    GameGUI.enableEvents();
  }

  public void disableEvents() {
    GameGUI.disableEvents();
  }

  public void stopMovement() {
    MovementTask.stopMovement();
  }

  public void deactivateAllEffects() {
    EffectManager.deactivateAllEffects();
  }

  public void viewingWindowSizeChanged() {
    GameGUI.viewingWindowSizeChanged();
    this.resetViewingWindow();
  }

  public void stateChanged() {
    Game.stateChanged = true;
  }

  public ScoreTracker getScoreTracker() {
    return this.scorer;
  }

  public void setArrowType(final ArrowType type) {
    Game.activeArrowType = type;
  }

  void arrowDone() {
    Game.activeArrowType = ArrowType.WOODEN;
  }

  public void setSavedGameFlag(final boolean value) {
    Game.savedGameFlag = value;
  }

  public void activateEffect(final int effectID) {
    EffectManager.activateEffect(effectID, -1);
  }

  public void setStatusMessage(final String msg) {
    GameGUI.setStatusMessage(msg);
  }

  public void updatePositionRelative(final int dirX, final int dirY,
      final int dirZ) {
    this.mt.moveRelative(dirX, dirY, dirZ);
  }

  public boolean tryUpdatePositionAbsolute(final int x, final int y,
      final int z) {
    return MovementTask.tryAbsolute(x, y, z);
  }

  public void updatePositionAbsolute(final int x, final int y, final int z) {
    this.mt.moveAbsolute(x, y, z);
  }

  public void redrawMaze() {
    GameGUI.redrawMaze();
  }

  public void redrawOneSquare(final int inX, final int inY,
      final FantastleObjectModel obj4) {
    GameGUI.redrawOneSquare(inX, inY, obj4);
  }

  public void resetViewingWindowAndPlayerLocation() {
    Game.resetPlayerLocation();
    this.resetViewingWindow();
  }

  public void resetViewingWindow() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    if (m != null) {
      GameView.setViewingWindowLocationX(
          m.getPlayerLocationY() - GameView.getOffsetFactorX());
      GameView.setViewingWindowLocationY(
          m.getPlayerLocationX() - GameView.getOffsetFactorY());
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
    Game.objectInv = new ObjectInventory();
  }

  public boolean isTeleporting() {
    return Game.isTeleporting;
  }

  public boolean usingAnItem() {
    return Game.using;
  }

  public void setUsingAnItem(final boolean isUsing) {
    Game.using = isUsing;
  }

  public boolean isPullInProgress() {
    return Game.pullInProgress;
  }

  public void setPullInProgress(final boolean pulling) {
    Game.pullInProgress = pulling;
  }

  public void activateEffect(final int effectID, final int duration) {
    EffectManager.activateEffect(effectID, duration);
  }

  int[] doEffects(final int x, final int y) {
    return EffectManager.doEffects(x, y);
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
    final ArrowTask at = new ArrowTask(x, y, Game.activeArrowType);
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
    Game.stateChanged = true;
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
    Game.savedGameFlag = false;
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
    return GameGUI.getOutputFrame();
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
    GameGUI.keepNextMessage();
  }

  public void playMaze() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    if (app.getMazeManager().getLoaded()) {
      app.getGUIManager().hideGUI();
      if (Game.stateChanged) {
        // Initialize only if the maze state has changed
        app.getMazeManager().getMaze()
            .switchLevel(app.getMazeManager().getMaze().getStartLevel());
        Game.stateChanged = false;
      }
      // Make sure message area is attached to the border pane
      GameGUI.updateGameGUI();
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
    GameGUI.updateStats();
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
    Game.decay();
    Game.objectInv = new ObjectInventory();
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
      Game.resetPlayerLocation();
      this.resetViewingWindow();
      Game.decay();
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
      Game.resetPlayerLocation();
      this.resetViewingWindow();
      Game.decay();
      this.redrawMaze();
    } else {
      this.solvedMaze();
    }
  }

  private void gameOver() {
    SoundPlayer.playSound(SoundIndex.GAME_OVER, SoundGroup.GAME);
    if (Game.gameOverMessage == null) {
      Messager.showDialog("You have died - Game Over!");
    } else {
      Messager.showDialog(Game.gameOverMessage);
    }
    this.solvedMaze();
  }

  public void gameOverWithMessage(final String msg) {
    Game.gameOverMessage = msg;
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
    Game.objectInv = new ObjectInventory();
    // Reset saved game flag
    Game.savedGameFlag = false;
    app.getMazeManager().setDirty(false);
    if (this.scorer.checkScore()) {
      app.playHighScoreSound();
    }
    this.scorer.commitScore();
    this.hideOutput();
    app.getGUIManager().showGUI();
  }

  public ObjectInventory getObjectInventory() {
    return Game.objectInv;
  }

  public void setObjectInventory(final ObjectInventory newObjectInventory) {
    Game.objectInv = newObjectInventory;
  }

  public void useItemHandler(final int x, final int y) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    final int xOffset = GameView.getViewingWindowLocationX()
        - GameView.getOffsetFactorX();
    final int yOffset = GameView.getViewingWindowLocationY()
        - GameView.getOffsetFactorY();
    final int destX = x / ImageConstants.SIZE
        + GameView.getViewingWindowLocationX() - xOffset + yOffset;
    final int destY = y / ImageConstants.SIZE
        + GameView.getViewingWindowLocationY() + xOffset - yOffset;
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
        Game.objectInv.use(Game.objectBeingUsed);
        this.redrawMaze();
      }
    }
  }

  public void controllableTeleport() {
    Game.isTeleporting = true;
    Messager.showMessage("Click to set destination");
  }

  void controllableTeleportHandler(final int x, final int y) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = app.getMazeManager().getMaze();
    if (Game.isTeleporting) {
      final int xOffset = GameView.getViewingWindowLocationX()
          - GameView.getOffsetFactorX();
      final int yOffset = GameView.getViewingWindowLocationY()
          - GameView.getOffsetFactorY();
      final int destX = x / ImageConstants.SIZE
          + GameView.getViewingWindowLocationX() - xOffset + yOffset;
      final int destY = y / ImageConstants.SIZE
          + GameView.getViewingWindowLocationY() + xOffset - yOffset;
      final int destZ = m.getPlayerLocationZ();
      this.updatePositionAbsolute(destX, destY, destZ);
      SoundPlayer.playSound(SoundIndex.TELEPORT, SoundGroup.GAME);
      Game.isTeleporting = false;
    }
  }

  public void showOutput() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    bag.setInGame();
    MusicPlayer.playMusic(MusicIndex.DUNGEON, MusicGroup.GAME);
    GameGUI.showOutput();
  }

  public void hideOutput() {
    this.stopMovement();
    GameGUI.hideOutput();
  }
}
