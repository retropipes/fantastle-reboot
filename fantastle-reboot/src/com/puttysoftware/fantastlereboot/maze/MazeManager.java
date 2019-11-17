/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.files.FileExtensions;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.files.GameFinder;
import com.puttysoftware.fantastlereboot.files.GameLoader;
import com.puttysoftware.fantastlereboot.files.GameSaver;
import com.puttysoftware.fantastlereboot.files.MazeFinder;
import com.puttysoftware.fantastlereboot.files.MazeLoader;
import com.puttysoftware.fantastlereboot.files.MazeSaver;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fileutils.FilenameChecker;

public final class MazeManager {
  // Fields
  private Maze gameMaze;
  private static final String MAC_PREFIX = "HOME";
  private static final String WIN_PREFIX = "APPDATA";
  private static final String UNIX_PREFIX = "HOME";
  private static final String MAC_MAZE_DIR = "/Library/BagOStuff Support/Putty Software/FantastleReboot/Mazes/";
  private static final String WIN_MAZE_DIR = "\\Putty Software\\FantastleReboot\\Mazes\\";
  private static final String UNIX_MAZE_DIR = "/.puttysoftware/tallertower/mazes/";
  private static final String MAC_GAME_DIR = "/Library/BagOStuff Support/Putty Software/FantastleReboot/Games/";
  private static final String WIN_GAME_DIR = "\\Putty Software\\FantastleReboot\\Games\\";
  private static final String UNIX_GAME_DIR = "/.puttysoftware/tallertower/games/";

  // Constructors
  public MazeManager() {
    this.gameMaze = null;
  }

  // Methods
  public Maze getMaze() {
    return this.gameMaze;
  }

  public void setMaze(final Maze newMaze) {
    this.gameMaze = newMaze;
  }

  public static void handleDeferredSuccess(final boolean value,
      final boolean versionError, final File triedToLoad) {
    if (value) {
      FileStateManager.setLoaded(true);
    }
    if (versionError) {
      triedToLoad.delete();
    }
    FileStateManager.setDirty(false);
    Game.stateChanged();
    FantastleReboot.getBagOStuff().getMenuManager().checkFlags();
  }

  public static boolean loadGame() {
    int status = 0;
    boolean saved = true;
    String filename;
    final GameFinder gf = new GameFinder();
    if (FileStateManager.getDirty()) {
      status = FileStateManager.showSaveDialog();
      if (status == JOptionPane.YES_OPTION) {
        saved = MazeManager.saveGame();
      } else if (status == JOptionPane.CANCEL_OPTION) {
        saved = false;
      } else {
        FileStateManager.setDirty(false);
      }
    }
    if (saved) {
      final String gameDir = MazeManager.getGameDirectory();
      final String[] rawChoices = new File(gameDir).list(gf);
      if (rawChoices != null) {
        final String[] choices = new String[rawChoices.length];
        // Strip extension
        for (int x = 0; x < choices.length; x++) {
          choices[x] = MazeManager.getNameWithoutExtension(rawChoices[x]);
        }
        final String returnVal = CommonDialogs.showInputDialog("Select a Game",
            "Load Game", choices, choices[0]);
        if (returnVal != null) {
          int index = -1;
          for (int x = 0; x < choices.length; x++) {
            if (returnVal.equals(choices[x])) {
              index = x;
              break;
            }
          }
          if (index != -1) {
            final File file = new File(gameDir + rawChoices[index]);
            filename = file.getAbsolutePath();
            MazeManager.loadGameFile(filename);
          } else {
            // Result not found
            if (FileStateManager.getLoaded()) {
              return true;
            }
          }
        } else {
          // User cancelled
          if (FileStateManager.getLoaded()) {
            return true;
          }
        }
      } else {
        CommonDialogs.showErrorDialog("No Games Found!", "Load Game");
        if (FileStateManager.getLoaded()) {
          return true;
        }
      }
    }
    return false;
  }

  private static void loadGameFile(final String filename) {
    if (!FilenameChecker.isFilenameOK(MazeManager
        .getNameWithoutExtension(MazeManager.getFileNameOnly(filename)))) {
      CommonDialogs.showErrorDialog(
          "The file you selected contains illegal characters in its\n"
              + "name. These characters are not allowed: /?<>\\:|\"\n"
              + "Files named con, nul, or prn are illegal, as are files\n"
              + "named com1 through com9 and lpt1 through lpt9.",
          "Load");
    } else {
      final GameLoader llt = new GameLoader(filename);
      llt.start();
    }
  }

  public static boolean saveGame() {
    String filename = "";
    String extension;
    String returnVal = "\\";
    while (!FilenameChecker.isFilenameOK(returnVal)) {
      returnVal = CommonDialogs.showTextInputDialog("Name?", "Save Game");
      if (returnVal != null) {
        extension = FileExtensions.getGameExtensionWithPeriod();
        final File file = new File(
            MazeManager.getGameDirectory() + returnVal + extension);
        filename = file.getAbsolutePath();
        if (!FilenameChecker.isFilenameOK(returnVal)) {
          CommonDialogs.showErrorDialog(
              "The file name you entered contains illegal characters.\n"
                  + "These characters are not allowed: /?<>\\:|\"\n"
                  + "Files named con, nul, or prn are illegal, as are files\n"
                  + "named com1 through com9 and lpt1 through lpt9.",
              "Save Game");
        } else {
          // Make sure folder exists
          if (!file.getParentFile().exists()) {
            final boolean okay = file.getParentFile().mkdirs();
            if (!okay) {
              FantastleReboot
                  .logError(new IOException("Cannot create game folder!"));
            }
          }
          MazeManager.saveGameFile(filename);
        }
      } else {
        break;
      }
    }
    return false;
  }

  private static void saveGameFile(final String filename) {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final String sg = "Saved Game";
    bag.showMessage("Saving " + sg + " file...");
    final GameSaver lst = new GameSaver(filename);
    lst.start();
  }

  private static String getGameDirectoryPrefix() {
    final String osName = System.getProperty("os.name");
    if (osName.indexOf("Mac OS X") != -1) {
      // Mac OS X
      return System.getenv(MazeManager.MAC_PREFIX);
    } else if (osName.indexOf("Windows") != -1) {
      // Windows
      return System.getenv(MazeManager.WIN_PREFIX);
    } else {
      // Other - assume UNIX-like
      return System.getenv(MazeManager.UNIX_PREFIX);
    }
  }

  private static String getGameDirectoryName() {
    final String osName = System.getProperty("os.name");
    if (osName.indexOf("Mac OS X") != -1) {
      // Mac OS X
      return MazeManager.MAC_GAME_DIR;
    } else if (osName.indexOf("Windows") != -1) {
      // Windows
      return MazeManager.WIN_GAME_DIR;
    } else {
      // Other - assume UNIX-like
      return MazeManager.UNIX_GAME_DIR;
    }
  }

  private static String getGameDirectory() {
    final StringBuilder b = new StringBuilder();
    b.append(MazeManager.getGameDirectoryPrefix());
    b.append(MazeManager.getGameDirectoryName());
    return b.toString();
  }

  public static boolean loadMaze() {
    int status = 0;
    boolean saved = true;
    String filename;
    final MazeFinder gf = new MazeFinder();
    if (FileStateManager.getDirty()) {
      status = FileStateManager.showSaveDialog();
      if (status == JOptionPane.YES_OPTION) {
        saved = MazeManager.saveMaze();
      } else if (status == JOptionPane.CANCEL_OPTION) {
        saved = false;
      } else {
        FileStateManager.setDirty(false);
      }
    }
    if (saved) {
      final String gameDir = MazeManager.getMazeDirectory();
      final String[] rawChoices = new File(gameDir).list(gf);
      if (rawChoices != null) {
        final String[] choices = new String[rawChoices.length];
        // Strip extension
        for (int x = 0; x < choices.length; x++) {
          choices[x] = MazeManager.getNameWithoutExtension(rawChoices[x]);
        }
        final String returnVal = CommonDialogs.showInputDialog("Select a Maze",
            "Load Maze", choices, choices[0]);
        if (returnVal != null) {
          int index = -1;
          for (int x = 0; x < choices.length; x++) {
            if (returnVal.equals(choices[x])) {
              index = x;
              break;
            }
          }
          if (index != -1) {
            final File file = new File(gameDir + rawChoices[index]);
            filename = file.getAbsolutePath();
            MazeManager.loadMazeFile(filename);
          } else {
            // Result not found
            if (FileStateManager.getLoaded()) {
              return true;
            }
          }
        } else {
          // User cancelled
          if (FileStateManager.getLoaded()) {
            return true;
          }
        }
      } else {
        CommonDialogs.showErrorDialog("No Mazes Found!", "Load Maze");
        if (FileStateManager.getLoaded()) {
          return true;
        }
      }
    }
    return false;
  }

  private static void loadMazeFile(final String filename) {
    if (!FilenameChecker.isFilenameOK(MazeManager
        .getNameWithoutExtension(MazeManager.getFileNameOnly(filename)))) {
      CommonDialogs.showErrorDialog(
          "The file you selected contains illegal characters in its\n"
              + "name. These characters are not allowed: /?<>\\:|\"\n"
              + "Files named con, nul, or prn are illegal, as are files\n"
              + "named com1 through com9 and lpt1 through lpt9.",
          "Load");
    } else {
      final MazeLoader llt = new MazeLoader(filename);
      llt.start();
    }
  }

  public static boolean saveMaze() {
    String filename = "";
    String extension;
    String returnVal = "\\";
    while (!FilenameChecker.isFilenameOK(returnVal)) {
      returnVal = CommonDialogs.showTextInputDialog("Name?", "Save Maze");
      if (returnVal != null) {
        extension = FileExtensions.getMazeExtensionWithPeriod();
        final File file = new File(
            MazeManager.getMazeDirectory() + returnVal + extension);
        filename = file.getAbsolutePath();
        if (!FilenameChecker.isFilenameOK(returnVal)) {
          CommonDialogs.showErrorDialog(
              "The file name you entered contains illegal characters.\n"
                  + "These characters are not allowed: /?<>\\:|\"\n"
                  + "Files named con, nul, or prn are illegal, as are files\n"
                  + "named com1 through com9 and lpt1 through lpt9.",
              "Save Maze");
        } else {
          // Make sure folder exists
          if (!file.getParentFile().exists()) {
            final boolean okay = file.getParentFile().mkdirs();
            if (!okay) {
              FantastleReboot
                  .logError(new IOException("Cannot create game folder!"));
            }
          }
          MazeManager.saveMazeFile(filename);
        }
      } else {
        break;
      }
    }
    return false;
  }

  private static void saveMazeFile(final String filename) {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final String sg = "Saved Maze";
    bag.showMessage("Saving " + sg + " file...");
    final MazeSaver lst = new MazeSaver(filename);
    lst.start();
  }

  private static String getMazeDirectoryPrefix() {
    final String osName = System.getProperty("os.name");
    if (osName.indexOf("Mac OS X") != -1) {
      // Mac OS X
      return System.getenv(MazeManager.MAC_PREFIX);
    } else if (osName.indexOf("Windows") != -1) {
      // Windows
      return System.getenv(MazeManager.WIN_PREFIX);
    } else {
      // Other - assume UNIX-like
      return System.getenv(MazeManager.UNIX_PREFIX);
    }
  }

  private static String getMazeDirectoryName() {
    final String osName = System.getProperty("os.name");
    if (osName.indexOf("Mac OS X") != -1) {
      // Mac OS X
      return MazeManager.MAC_MAZE_DIR;
    } else if (osName.indexOf("Windows") != -1) {
      // Windows
      return MazeManager.WIN_MAZE_DIR;
    } else {
      // Other - assume UNIX-like
      return MazeManager.UNIX_MAZE_DIR;
    }
  }

  private static String getMazeDirectory() {
    final StringBuilder b = new StringBuilder();
    b.append(MazeManager.getMazeDirectoryPrefix());
    b.append(MazeManager.getMazeDirectoryName());
    return b.toString();
  }

  private static String getNameWithoutExtension(final String s) {
    String ext = null;
    final int i = s.lastIndexOf('.');
    if ((i > 0) && (i < s.length() - 1)) {
      ext = s.substring(0, i);
    } else {
      ext = s;
    }
    return ext;
  }

  private static String getFileNameOnly(final String s) {
    String fno = null;
    final int i = s.lastIndexOf(File.separatorChar);
    if ((i > 0) && (i < s.length() - 1)) {
      fno = s.substring(i + 1);
    } else {
      fno = s;
    }
    return fno;
  }
}
