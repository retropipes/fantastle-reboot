/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.savedgames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.utilities.FileExtensions;
import com.puttysoftware.fantastlereboot.utilities.PrefixHandler;
import com.puttysoftware.fantastlereboot.utilities.SuffixHandler;
import com.puttysoftware.fileutils.ZipUtilities;

public class GameSaveTask extends Thread {
  // Fields
  private String filename;

  // Constructors
  public GameSaveTask(final String file) {
    this.filename = file;
    this.setName("Game Writer");
  }

  @Override
  public void run() {
    boolean success = true;
    final String sg = "Game";
    try {
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final BagOStuff bag = FantastleReboot.getBagOStuff();
      // filename check
      final boolean hasExtension = GameSaveTask.hasExtension(this.filename);
      if (!hasExtension) {
        this.filename += FileExtensions.getGameExtensionWithPeriod();
      }
      final File mazeFile = new File(this.filename);
      final File tempLock = new File(Maze.getMazeTempFolder() + "lock.tmp");
      // Set prefix handler
      app.getMazeManager().getMaze().setPrefixHandler(new PrefixHandler());
      // Set suffix handler
      app.getMazeManager().getMaze().setSuffixHandler(new SuffixHandler());
      app.getMazeManager().getMaze().writeMaze();
      ZipUtilities.zipDirectory(
          new File(app.getMazeManager().getMaze().getBasePath()), tempLock);
      // Lock the file
      GameFileManager.save(tempLock, mazeFile);
      final boolean delSuccess = tempLock.delete();
      if (!delSuccess) {
        throw new IOException("Failed to delete temporary file!");
      }
      bag.showMessage(sg + " saved.");
    } catch (final FileNotFoundException fnfe) {
      CommonDialogs.showDialog("Writing the " + sg.toLowerCase()
          + " failed, probably due to illegal characters in the file name.");
      success = false;
    } catch (final Exception ex) {
      FantastleReboot.logError(ex);
    }
    FantastleReboot.getBagOStuff().getMazeManager()
        .handleDeferredSuccess(success, false, null);
  }

  private static boolean hasExtension(final String s) {
    String ext = null;
    final int i = s.lastIndexOf('.');
    if ((i > 0) && (i < s.length() - 1)) {
      ext = s.substring(i + 1).toLowerCase();
    }
    if (ext == null) {
      return false;
    } else {
      return true;
    }
  }
}
