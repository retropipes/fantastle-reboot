/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Modes;
import com.puttysoftware.fantastlereboot.assets.MusicGroup;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.loaders.ImageConstants;
import com.puttysoftware.fantastlereboot.loaders.MusicPlayer;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.GenerateTask;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Player;
import com.puttysoftware.fantastlereboot.objects.temporary.ArrowType;

public final class Game {
  // Fields
  private static boolean savedGameFlag = false;
  private static boolean stateChanged = true;
  private static ObjectInventory objectInv;
  private static boolean pullInProgress = false;
  private static boolean using = false;
  private static FantastleObjectModel objectBeingUsed;
  private static String gameOverMessage;
  private static ArrowType activeArrowType = ArrowType.WOODEN;
  private static boolean isTeleporting = false;
  private static MovementTask mt = new MovementTask();

  // Constructors
  private Game() {
    // Do nothing
  }

  // Methods
  public static boolean newGame() {
    Game.initialize();
    EffectManager.deactivateAllEffects();
    if (Game.savedGameFlag) {
      if (PartyManager.getParty() != null) {
        return true;
      } else {
        return PartyManager.createParty();
      }
    } else {
      return PartyManager.createParty();
    }
  }

  public static void enableEvents() {
    MovementTask.fireStepActions();
    GameGUI.enableEvents();
  }

  public static void disableEvents() {
    GameGUI.disableEvents();
  }

  public static void stopMovement() {
    MovementTask.stopMovement();
  }

  public static void deactivateAllEffects() {
    EffectManager.deactivateAllEffects();
  }

  public static void initialize() {
    if (!Game.mt.isAlive()) {
      Game.mt.start();
      EffectManager.initialize();
      GameGUI.viewingWindowSizeChanged();
      Game.resetViewingWindow();
    }
  }

  public static void stateChanged() {
    Game.stateChanged = true;
  }

  public static void setArrowType(final ArrowType type) {
    Game.activeArrowType = type;
  }

  static void arrowDone() {
    Game.activeArrowType = ArrowType.WOODEN;
  }

  public static void setSavedGameFlag(final boolean value) {
    Game.savedGameFlag = value;
  }

  public static void activateEffect(final int effectID) {
    EffectManager.activateEffect(effectID, -1);
  }

  public static void setStatusMessage(final String msg) {
    GameGUI.setStatusMessage(msg);
  }

  public static void updatePositionRelative(final int dirX, final int dirY,
      final int dirZ) {
    Game.mt.moveRelative(dirX, dirY, dirZ);
  }

  public static boolean tryUpdatePositionAbsolute(final int x, final int y,
      final int z) {
    return MovementTask.tryAbsolute(x, y, z);
  }

  public static void updatePositionAbsolute(final int x, final int y,
      final int z) {
    Game.mt.moveAbsolute(x, y, z);
  }

  public static void redrawMaze() {
    GameGUI.redrawMaze();
  }

  public static void redrawOneSquare(final int inX, final int inY,
      final FantastleObjectModel obj4) {
    GameGUI.redrawOneSquare(inX, inY, obj4);
  }

  public static void resetViewingWindowAndPlayerLocation() {
    Game.resetPlayerLocation();
    Game.resetViewingWindow();
  }

  public static void resetViewingWindow() {
    final Maze m = MazeManager.getMaze();
    if (m != null) {
      GameView.setViewingWindowLocationX(
          m.getPlayerLocationY() - GameView.getOffsetFactorX());
      GameView.setViewingWindowLocationY(
          m.getPlayerLocationX() - GameView.getOffsetFactorY());
    }
  }

  public static void resetPlayerLocation() {
    final Maze m = MazeManager.getMaze();
    if (m != null) {
      m.setPlayerToStart();
    }
  }

  public static void resetObjectInventory() {
    Game.objectInv = new ObjectInventory();
  }

  public static boolean isTeleporting() {
    return Game.isTeleporting;
  }

  public static boolean usingAnItem() {
    return Game.using;
  }

  public static void setUsingAnItem(final boolean isUsing) {
    Game.using = isUsing;
  }

  public static boolean isPullInProgress() {
    return Game.pullInProgress;
  }

  public static void setPullInProgress(final boolean pulling) {
    Game.pullInProgress = pulling;
  }

  public static void activateEffect(final int effectID, final int duration) {
    EffectManager.activateEffect(effectID, duration);
  }

  static int[] doEffects(final int x, final int y) {
    return EffectManager.doEffects(x, y);
  }

  public static boolean isFloorBelow() {
    final Maze m = MazeManager.getMaze();
    return m.floorRangeCheck(m.getPlayerLocationZ() - 1);
  }

  public static boolean isFloorAbove() {
    final Maze m = MazeManager.getMaze();
    return m.floorRangeCheck(m.getPlayerLocationZ() + 1);
  }

  public static boolean areTwoFloorsBelow() {
    final Maze m = MazeManager.getMaze();
    return m.floorRangeCheck(m.getPlayerLocationZ() - 2);
  }

  public static boolean areTwoFloorsAbove() {
    final Maze m = MazeManager.getMaze();
    return m.floorRangeCheck(m.getPlayerLocationZ() + 2);
  }

  public static boolean isLevelBelow() {
    final Maze m = MazeManager.getMaze();
    return m.levelRangeCheck(m.getPlayerLocationW() - 1);
  }

  public static boolean isLevelAbove() {
    final Maze m = MazeManager.getMaze();
    return m.levelRangeCheck(m.getPlayerLocationW() + 1);
  }

  public static boolean doesFloorExist(final int floor) {
    final Maze m = MazeManager.getMaze();
    return m.floorRangeCheck(floor);
  }

  public static boolean doesLevelExist(final int level) {
    final Maze m = MazeManager.getMaze();
    return m.levelRangeCheck(level);
  }

  public static void fireArrow(final int x, final int y) {
    final ArrowTask at = new ArrowTask(x, y, Game.activeArrowType);
    at.start();
  }

  public static void goToLevelOffset(final int level) {
    final Maze m = MazeManager.getMaze();
    final boolean levelExists = m.doesLevelExistOffset(level);
    Game.stopMovement();
    if (levelExists) {
      new LevelLoadTask(level).start();
    } else {
      new GenerateTask(false).start();
    }
  }

  public static void exitGame() {
    Game.stateChanged = true;
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = MazeManager.getMaze();
    // Restore the maze
    m.restore();
    m.resetVisibleSquares();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      Game.resetViewingWindowAndPlayerLocation();
    } else {
      FileStateManager.setLoaded(false);
    }
    // Reset saved game flag
    Game.savedGameFlag = false;
    FileStateManager.setDirty(false);
    // Exit game
    Game.hideOutput();
    app.getGUIManager().showGUI();
  }

  public static void invalidateScore() {
    ScoreTracker.invalidateScore();
  }

  public static void showCurrentScore() {
    ScoreTracker.showCurrentScore();
  }

  public static void showScoreTable() {
    ScoreTracker.showScoreTable();
  }

  public static void validateScore() {
    ScoreTracker.validateScore();
  }

  public static void morph(final FantastleObjectModel morphInto) {
    final Maze m = MazeManager.getMaze();
    m.setCell(morphInto, m.getPlayerLocationX(), m.getPlayerLocationY(),
        m.getPlayerLocationZ(), morphInto.getLayer());
  }

  public static void keepNextMessage() {
    GameGUI.keepNextMessage();
  }

  public static void playMaze() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final Maze m = MazeManager.getMaze();
    if (FileStateManager.getLoaded()) {
      if (Game.stateChanged) {
        // Initialize only if the maze state has changed
        MazeManager.getMaze()
            .switchLevel(MazeManager.getMaze().getStartLevel());
        Game.stateChanged = false;
      }
      // Make sure message area is attached to the border pane
      bag.getMenuManager().setGameMenus();
      GameGUI.updateGameGUI();
      // Make sure initial area player is in is visible
      final int px = m.getPlayerLocationX();
      final int py = m.getPlayerLocationY();
      final int pz = m.getPlayerLocationZ();
      m.updateExploredSquares(px, py, pz);
      Modes.setInGame();
      Game.playDungeonMusic();
      Game.redrawMaze();
    } else {
      CommonDialogs.showDialog("No Maze Opened");
    }
  }

  public static void updateStats() {
    // Update stats
    GameGUI.updateStats();
    // Check for game over
    if (!PartyManager.getParty().getLeader().isAlive()) {
      Game.gameOver();
    }
  }

  public static void resetCurrentLevel() {
    Game.resetLevel();
  }

  public static void resetGameState() {
    Game.deactivateAllEffects();
    final Maze m = MazeManager.getMaze();
    FileStateManager.setDirty(false);
    m.restore();
    Game.setSavedGameFlag(false);
    ScoreTracker.resetScore();
    Game.objectInv = new ObjectInventory();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      m.save();
    }
  }

  public static void resetLevel() {
    PartyManager.getParty().getLeader().healAndRegenerateFully();
    Game.deactivateAllEffects();
    final Maze m = MazeManager.getMaze();
    FileStateManager.setDirty(true);
    m.restore();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      ScoreTracker.resetScore();
      Game.resetPlayerLocation();
      Game.resetViewingWindow();
      Game.redrawMaze();
    }
  }

  public static void solvedLevel() {
    Game.deactivateAllEffects();
    final Maze m = MazeManager.getMaze();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      m.restore();
      Game.resetPlayerLocation();
      Game.resetViewingWindow();
      Game.redrawMaze();
    } else {
      Game.solvedMaze();
    }
  }

  private static void gameOver() {
    SoundPlayer.playSound(SoundIndex.GAME_OVER, SoundGroup.GAME);
    if (Game.gameOverMessage == null) {
      CommonDialogs.showDialog("You have died - Game Over!");
    } else {
      CommonDialogs.showDialog(Game.gameOverMessage);
    }
    Game.solvedMaze();
  }

  public static void gameOverWithMessage(final String msg) {
    Game.gameOverMessage = msg;
  }

  public static void solvedMaze() {
    PartyManager.getParty().getLeader().healAndRegenerateFully();
    Game.deactivateAllEffects();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    final Maze m = MazeManager.getMaze();
    // Restore the maze
    m.restore();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      Game.resetViewingWindowAndPlayerLocation();
    } else {
      FileStateManager.setLoaded(false);
    }
    // Wipe the inventory
    Game.objectInv = new ObjectInventory();
    // Reset saved game flag
    Game.savedGameFlag = false;
    FileStateManager.setDirty(false);
    if (ScoreTracker.checkScore()) {
      app.playHighScoreSound();
    }
    ScoreTracker.commitScore();
    Game.hideOutput();
    app.getGUIManager().showGUI();
  }

  public static ObjectInventory getObjectInventory() {
    return Game.objectInv;
  }

  public static void
      setObjectInventory(final ObjectInventory newObjectInventory) {
    Game.objectInv = newObjectInventory;
  }

  public static void useItemHandler(final int x, final int y) {
    final Maze m = MazeManager.getMaze();
    final int xOffset = GameView.getViewingWindowLocationX()
        - GameView.getOffsetFactorX();
    final int yOffset = GameView.getViewingWindowLocationY()
        - GameView.getOffsetFactorY();
    final int destX = x / ImageConstants.SIZE
        + GameView.getViewingWindowLocationX() - xOffset + yOffset;
    final int destY = y / ImageConstants.SIZE
        + GameView.getViewingWindowLocationY() + xOffset - yOffset;
    final int destZ = m.getPlayerLocationZ();
    if (Game.usingAnItem() && Modes.inGame()) {
      if (m.cellRangeCheck(destX, destY, destZ)) {
        final FantastleObjectModel target = m.getCell(destX, destY, destZ,
            Layers.OBJECT);
        if (target instanceof Player) {
          Game.setUsingAnItem(false);
          Game.setStatusMessage("Don't aim at yourself!");
        }
      } else {
        Game.setUsingAnItem(false);
        Game.setStatusMessage("Aim within the maze");
      }
      if (Game.usingAnItem()) {
        Game.objectInv.use(Game.objectBeingUsed);
        Game.redrawMaze();
      }
    }
  }

  public static void controllableTeleport() {
    Game.isTeleporting = true;
    Game.setStatusMessage("Click to set destination");
  }

  static void controllableTeleportHandler(final int x, final int y) {
    final Maze m = MazeManager.getMaze();
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
      Game.updatePositionAbsolute(destX, destY, destZ);
      SoundPlayer.playSound(SoundIndex.TELEPORT, SoundGroup.GAME);
      Game.isTeleporting = false;
    }
  }

  public static void playDungeonMusic() {
    MusicPlayer.playMusic(MusicIndex.DUNGEON, MusicGroup.GAME);
    GameGUI.showOutput();
  }

  public static void showOutput() {
    GameGUI.showOutput();
  }

  public static void hideOutput() {
    Game.stopMovement();
    GameGUI.hideOutput();
  }
}
