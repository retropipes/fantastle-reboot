/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot;

import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;

import javax.swing.JOptionPane;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.maze.MazeManager;

public final class FileStateManager implements QuitHandler {
  // Fields
  private static boolean loaded = false;
  private static boolean isDirty = false;

  // Constructors
  FileStateManager() {
    // Do nothing
  }

  // Methods
  @Override
  public void handleQuitRequestWith(QuitEvent inE, QuitResponse inResponse) {
    boolean saved = true;
    int status = JOptionPane.DEFAULT_OPTION;
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
      PreferencesManager.writePrefs();
      inResponse.performQuit();
    } else {
      inResponse.cancelQuit();
    }
  }

  public static int showSaveDialog() {
    String type, source;
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final int mode = bag.getMode();
    if (mode == BagOStuff.STATUS_GAME) {
      type = "game";
      source = "FantastleReboot";
    } else {
      // Not in the game or editor, so abort
      return JOptionPane.NO_OPTION;
    }
    return CommonDialogs
        .showYNCConfirmDialog("Do you want to save your " + type + "?", source);
  }

  public static boolean getLoaded() {
    return FileStateManager.loaded;
  }

  public static void setLoaded(final boolean status) {
    FileStateManager.loaded = status;
    FantastleReboot.getBagOStuff().getMenuManager().checkFlags();
  }

  public static boolean getDirty() {
    return FileStateManager.isDirty;
  }

  public static void setDirty(final boolean newDirty) {
    FileStateManager.isDirty = newDirty;
    FantastleReboot.getBagOStuff().getMenuManager().checkFlags();
  }
}
