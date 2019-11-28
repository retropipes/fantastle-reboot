/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import java.io.File;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fileutils.ZipUtilities;

public class GameSaver extends Thread {
  // Fields
  private String filename;

  // Constructors
  public GameSaver(final String file) {
    this.filename = file;
    this.setName("Game Writer");
  }

  @Override
  public void run() {
    final boolean success = true;
    final String sg = "Game";
    try {
      final BagOStuff bag = FantastleReboot.getBagOStuff();
      // filename check
      final boolean hasExtension = GameSaver.hasExtension(this.filename);
      if (!hasExtension) {
        this.filename += FileExtensions.getGameExtensionWithPeriod();
      }
      final File mazeFile = new File(this.filename);
      // Set prefix handler
      MazeManager.getMaze().setPrefixHandler(new PrefixHandler());
      // Set suffix handler
      MazeManager.getMaze().setSuffixHandler(new SuffixHandler());
      MazeManager.getMaze().writeMaze();
      // Zip the file
      ZipUtilities.zipDirectory(new File(MazeManager.getMaze().getBasePath()),
          mazeFile);
      bag.showMessage(sg + " saved.");
    } catch (final Exception ex) {
      FantastleReboot.exception(ex);
    }
    MazeFileManager.handleDeferredSuccess(success, false, null);
  }

  private static boolean hasExtension(final String s) {
    String ext = null;
    final int i = s.lastIndexOf('.');
    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(i + 1).toLowerCase();
    }
    if (ext == null) {
      return false;
    } else {
      return true;
    }
  }
}
