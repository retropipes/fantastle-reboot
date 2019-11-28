/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Modes;

public final class FileStateManager {
  // Fields
  private static boolean loaded = false;
  private static boolean isDirty = false;

  // Constructors
  FileStateManager() {
    // Do nothing
  }

  // Methods
  public static int showSaveDialog() {
    String source, action;
    if (Modes.inGame()) {
      action = "Do you want to suspend your game?";
      source = "Fantastle Reboot";
    } else if (Modes.inEditor()) {
      action = "Do you want to save your world?";
      source = "Editor";
    } else {
      // Not in the game or editor, so abort
      return JOptionPane.NO_OPTION;
    }
    return CommonDialogs.showYNCConfirmDialog(action, source);
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
