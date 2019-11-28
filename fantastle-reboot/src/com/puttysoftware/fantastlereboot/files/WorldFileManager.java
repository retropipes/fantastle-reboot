/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fileutils.FilenameChecker;

public final class WorldFileManager {
  // Constructors
  private WorldFileManager() {
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

  public static boolean loadWorld() {
    int status = 0;
    boolean saved = true;
    String filename;
    final WorldFinder gf = new WorldFinder();
    if (FileStateManager.getDirty()) {
      status = FileStateManager.showSaveDialog();
      if (status == JOptionPane.YES_OPTION) {
        saved = WorldFileManager.saveWorld();
      } else if (status == JOptionPane.CANCEL_OPTION) {
        saved = false;
      } else {
        FileStateManager.setDirty(false);
      }
    }
    if (saved) {
      final String gameDir = WorldFileManager.getWorldDirectory();
      final String[] rawChoices = new File(gameDir).list(gf);
      if (rawChoices != null && rawChoices.length > 0) {
        final String[] choices = new String[rawChoices.length];
        // Strip extension
        for (int x = 0; x < choices.length; x++) {
          choices[x] = WorldFileManager.getNameWithoutExtension(rawChoices[x]);
        }
        final String returnVal = CommonDialogs.showInputDialog("Select a World",
            "Load World", choices, choices[0]);
        if (returnVal != null) {
          int index = -1;
          for (int x = 0; x < choices.length; x++) {
            if (returnVal.equals(choices[x])) {
              index = x;
              break;
            }
          }
          if (index != -1) {
            final File file = new File(
                gameDir + File.separator + rawChoices[index]);
            filename = file.getAbsolutePath();
            WorldFileManager.loadWorldFile(filename);
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
        CommonDialogs.showErrorDialog("No Worlds Found!", "Load World");
        if (FileStateManager.getLoaded()) {
          return true;
        }
      }
    }
    return false;
  }

  private static void loadWorldFile(final String filename) {
    if (!FilenameChecker.isFilenameOK(WorldFileManager
        .getNameWithoutExtension(WorldFileManager.getFileNameOnly(filename)))) {
      CommonDialogs.showErrorDialog(
          "The file you selected contains illegal characters in its\n"
              + "name. These characters are not allowed: /?<>\\:|\"\n"
              + "Files named con, nul, or prn are illegal, as are files\n"
              + "named com1 through com9 and lpt1 through lpt9.",
          "Load");
    } else {
      final WorldLoader llt = new WorldLoader(filename);
      llt.start();
    }
  }

  public static boolean saveWorld() {
    String filename = "";
    String extension;
    String returnVal = "\\";
    while (!FilenameChecker.isFilenameOK(returnVal)) {
      returnVal = CommonDialogs.showTextInputDialog("Name?", "Save World");
      if (returnVal != null) {
        extension = FileExtensions.getWorldExtensionWithPeriod();
        final File file = new File(
            WorldFileManager.getWorldDirectory() + returnVal + extension);
        filename = file.getAbsolutePath();
        if (!FilenameChecker.isFilenameOK(returnVal)) {
          CommonDialogs.showErrorDialog(
              "The file name you entered contains illegal characters.\n"
                  + "These characters are not allowed: /?<>\\:|\"\n"
                  + "Files named con, nul, or prn are illegal, as are files\n"
                  + "named com1 through com9 and lpt1 through lpt9.",
              "Save World");
        } else {
          // Make sure folder exists
          if (!file.getParentFile().exists()) {
            final boolean okay = file.getParentFile().mkdirs();
            if (!okay) {
              FantastleReboot
                  .exception(new IOException("Cannot create game folder!"));
            }
          }
          WorldFileManager.saveWorldFile(filename);
        }
      } else {
        break;
      }
    }
    return false;
  }

  private static void saveWorldFile(final String filename) {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final String sg = "Saved World";
    bag.showMessage("Saving " + sg + " file...");
    final WorldSaver lst = new WorldSaver(filename);
    lst.start();
  }

  private static String getWorldDirectory() {
    return CommonPaths.getAppDirectoryFor("Worlds");
  }

  private static String getNameWithoutExtension(final String s) {
    String ext = null;
    final int i = s.lastIndexOf('.');
    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(0, i);
    } else {
      ext = s;
    }
    return ext;
  }

  private static String getFileNameOnly(final String s) {
    String fno = null;
    final int i = s.lastIndexOf(File.separatorChar);
    if (i > 0 && i < s.length() - 1) {
      fno = s.substring(i + 1);
    } else {
      fno = s;
    }
    return fno;
  }
}
