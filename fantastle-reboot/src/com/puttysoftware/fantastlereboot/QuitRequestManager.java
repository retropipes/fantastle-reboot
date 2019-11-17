/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot;

import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;

import javax.swing.JOptionPane;

import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.maze.FileStateManager;
import com.puttysoftware.fantastlereboot.maze.MazeManager;

final class QuitRequestManager implements QuitHandler {
  // Constructors
  QuitRequestManager() {
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
}
