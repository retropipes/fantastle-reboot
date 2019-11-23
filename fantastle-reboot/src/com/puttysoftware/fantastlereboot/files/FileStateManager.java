/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;

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
