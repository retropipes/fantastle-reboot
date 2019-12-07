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
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.loaders.MusicPlayer;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.world.GenerateTask;
import com.puttysoftware.fantastlereboot.world.World;
import com.puttysoftware.fantastlereboot.world.WorldManager;

public final class Game {
  // Fields
  private static boolean savedGameFlag = false;
  private static boolean stateChanged = true;
  private static boolean using = false;
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

  public static void setSavedGameFlag(final boolean value) {
    Game.savedGameFlag = value;
  }

  public static void setStatusMessage(final String msg) {
    GameGUI.setStatusMessage(msg);
  }

  public static void updatePositionRelative(final int dirX, final int dirY,
      final int dirZ) {
    Game.mt.moveRelative(dirX, dirY, dirZ);
  }

  public static void redrawWorld() {
    GameGUI.redrawWorld();
  }

  public static void resetViewingWindowAndPlayerLocation() {
    Game.resetPlayerLocation();
    Game.resetViewingWindow();
  }

  public static void resetViewingWindow() {
    final World m = WorldManager.getWorld();
    if (m != null) {
      GameView.setViewingWindowLocationX(
          m.getPlayerLocationY() - GameView.getOffsetFactorX());
      GameView.setViewingWindowLocationY(
          m.getPlayerLocationX() - GameView.getOffsetFactorY());
    }
  }

  public static void resetPlayerLocation() {
    final World m = WorldManager.getWorld();
    if (m != null) {
      m.setPlayerToStart();
    }
  }

  public static boolean usingAnItem() {
    return Game.using;
  }

  public static boolean isFloorBelow() {
    final World m = WorldManager.getWorld();
    return m.floorRangeCheck(m.getPlayerLocationZ() - 1);
  }

  public static boolean isFloorAbove() {
    final World m = WorldManager.getWorld();
    return m.floorRangeCheck(m.getPlayerLocationZ() + 1);
  }

  public static boolean areTwoFloorsBelow() {
    final World m = WorldManager.getWorld();
    return m.floorRangeCheck(m.getPlayerLocationZ() - 2);
  }

  public static boolean areTwoFloorsAbove() {
    final World m = WorldManager.getWorld();
    return m.floorRangeCheck(m.getPlayerLocationZ() + 2);
  }

  public static boolean isLevelBelow() {
    final World m = WorldManager.getWorld();
    return m.levelRangeCheck(m.getPlayerLocationW() - 1);
  }

  public static boolean isLevelAbove() {
    final World m = WorldManager.getWorld();
    return m.levelRangeCheck(m.getPlayerLocationW() + 1);
  }

  public static void goToLevelOffset(final int level) {
    final World m = WorldManager.getWorld();
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
    final World m = WorldManager.getWorld();
    // Restore the world
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

  public static void morph(final FantastleObjectModel morphInto) {
    final World m = WorldManager.getWorld();
    m.setCell(morphInto, m.getPlayerLocationX(), m.getPlayerLocationY(),
        m.getPlayerLocationZ(), morphInto.getLayer());
  }

  public static void keepNextMessage() {
    GameGUI.keepNextMessage();
  }

  public static void playWorld() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final World m = WorldManager.getWorld();
    if (FileStateManager.getLoaded()) {
      if (Game.stateChanged) {
        // Initialize only if the world state has changed
        WorldManager.getWorld()
            .switchLevel(WorldManager.getWorld().getStartLevel());
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
      Game.redrawWorld();
    } else {
      CommonDialogs.showDialog("No World Opened");
    }
  }

  public static void resetCurrentLevel() {
    Game.resetLevel();
  }

  public static void resetGameState() {
    Game.deactivateAllEffects();
    final World m = WorldManager.getWorld();
    FileStateManager.setDirty(false);
    m.restore();
    Game.setSavedGameFlag(false);
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      m.save();
    }
  }

  public static void resetLevel() {
    PartyManager.getParty().getLeader().healAndRegenerateFully();
    Game.deactivateAllEffects();
    final World m = WorldManager.getWorld();
    FileStateManager.setDirty(true);
    m.restore();
    final boolean playerExists = m.doesPlayerExist();
    if (playerExists) {
      Game.resetPlayerLocation();
      Game.resetViewingWindow();
      Game.redrawWorld();
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
