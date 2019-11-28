package com.puttysoftware.fantastlereboot;

import com.puttysoftware.fantastlereboot.editor.Editor;
import com.puttysoftware.fantastlereboot.editor.LevelPrefs;
import com.puttysoftware.fantastlereboot.editor.MazePrefs;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.Prefs;

public class Modes {
  private Modes() {
    super();
  }

  private static final int NULL = 0;
  private static final int GAME = 1;
  private static final int EDITOR = 2;
  private static final int PREFS = 3;
  private static final int BATTLE = 4;
  private static final int ABOUT = 5;
  private static final int HELP = 6;
  private static final int MAZE_PREFS = 7;
  private static final int LEVEL_PREFS = 8;
  private static final int GUI = 9;
  private static int current;
  private static int former;

  public static void setInGUI() {
    Modes.current = Modes.GUI;
  }

  public static void setInPrefs() {
    Modes.save();
    Modes.current = Modes.PREFS;
  }

  public static void setInGame() {
    Modes.current = Modes.GAME;
  }

  public static void setInEditor() {
    Modes.current = Modes.EDITOR;
  }

  public static void setInBattle() {
    Modes.save();
    Modes.current = Modes.BATTLE;
  }

  public static void setInAbout() {
    Modes.save();
    Modes.current = Modes.ABOUT;
  }

  public static void setInHelp() {
    Modes.save();
    Modes.current = Modes.HELP;
  }

  public static void setInMazePrefs() {
    Modes.save();
    Modes.current = Modes.MAZE_PREFS;
  }

  public static void setInLevelPrefs() {
    Modes.save();
    Modes.current = Modes.LEVEL_PREFS;
  }

  public static boolean inGUI() {
    return Modes.current == Modes.GUI;
  }

  public static boolean inPrefs() {
    return Modes.current == Modes.PREFS;
  }

  public static boolean inGame() {
    return Modes.current == Modes.GAME;
  }

  public static boolean inEditor() {
    return Modes.current == Modes.EDITOR;
  }

  public static boolean inBattle() {
    return Modes.current == Modes.BATTLE;
  }

  public static boolean inAbout() {
    return Modes.current == Modes.ABOUT;
  }

  public static boolean inHelp() {
    return Modes.current == Modes.HELP;
  }

  public static boolean inMazePrefs() {
    return Modes.current == Modes.MAZE_PREFS;
  }

  public static boolean inLevelPrefs() {
    return Modes.current == Modes.LEVEL_PREFS;
  }

  public static boolean inUnknown() {
    return Modes.current == Modes.NULL;
  }

  private static void save() {
    Modes.former = Modes.current;
  }

  public static void restore() {
    BagOStuff bag = FantastleReboot.getBagOStuff();
    Modes.current = Modes.former;
    switch (Modes.current) {
    case GUI:
      bag.getGUIManager().showGUI();
      break;
    case GAME:
      Game.playDungeonMusic();
      break;
    case EDITOR:
      Editor.showOutput();
      break;
    case PREFS:
      Prefs.showPrefs();
      break;
    case BATTLE:
      bag.getBattle().showBattle();
      break;
    case ABOUT:
      bag.getAboutDialog().showAboutDialog();
      break;
    case HELP:
      bag.getGeneralHelpManager().showHelp();
      break;
    case MAZE_PREFS:
      MazePrefs.showPrefs();
      break;
    case LEVEL_PREFS:
      LevelPrefs.showPrefs();
      break;
    default:
      break;
    }
  }
}