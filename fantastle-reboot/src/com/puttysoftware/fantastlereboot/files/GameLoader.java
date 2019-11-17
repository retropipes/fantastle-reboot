/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JProgressBar;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.VersionException;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fileutils.ZipUtilities;

public class GameLoader extends Thread {
  // Fields
  private final String filename;
  private MainWindow loadFrame;

  // Constructors
  public GameLoader(final String file) {
    this.filename = file;
    this.setName("Game Loader");
  }

  // Methods
  @Override
  public void run() {
    final String sg = "Game";
    final File mazeFile = new File(this.filename);
    try {
      this.loadFrame = MainWindow.getOutputFrame();
      this.loadFrame.setTitle("Loading");
      final JProgressBar loadBar = new JProgressBar();
      loadBar.setIndeterminate(true);
      final Container content = new Container();
      content.add(loadBar);
      this.loadFrame.setContentPane(content);
      this.loadFrame.pack();
      final BagOStuff app = FantastleReboot.getBagOStuff();
      int startW;
      Game.setSavedGameFlag(false);
      final File tempLock = new File(Maze.getMazeTempFolder() + "lock.tmp");
      Maze gameMaze = new Maze();
      // Unlock the file
      GameFileManager.load(mazeFile, tempLock);
      ZipUtilities.unzipDirectory(tempLock, new File(gameMaze.getBasePath()));
      final boolean success = tempLock.delete();
      if (!success) {
        throw new IOException("Failed to delete temporary file!");
      }
      // Set prefix handler
      gameMaze.setPrefixHandler(new PrefixHandler());
      // Set suffix handler
      gameMaze.setSuffixHandler(new SuffixHandler());
      gameMaze = gameMaze.readMaze();
      if (gameMaze == null) {
        throw new IOException("Unknown object encountered.");
      }
      app.getMazeManager().setMaze(gameMaze);
      startW = gameMaze.getStartLevel();
      gameMaze.switchLevel(startW);
      final boolean playerExists = gameMaze.doesPlayerExist();
      if (playerExists) {
        app.getMazeManager().getMaze().setPlayerToStart();
        Game.resetViewingWindow();
      }
      gameMaze.save();
      // Final cleanup
      Game.stateChanged();
      app.getMazeManager().setLoaded(true);
      CommonDialogs.showDialog(sg + " loaded.");
      Game.playMaze();
      app.getMazeManager().handleDeferredSuccess(true, false, null);
    } catch (final VersionException ve) {
      CommonDialogs.showDialog("Loading the " + sg.toLowerCase()
          + " failed, due to the format version being unsupported.");
      FantastleReboot.getBagOStuff().getMazeManager()
          .handleDeferredSuccess(false, true, mazeFile);
    } catch (final FileNotFoundException fnfe) {
      CommonDialogs.showDialog("Loading the " + sg.toLowerCase()
          + " failed, probably due to illegal characters in the file name.");
      FantastleReboot.getBagOStuff().getMazeManager()
          .handleDeferredSuccess(false, false, null);
    } catch (final IOException ie) {
      CommonDialogs.showDialog("Loading the " + sg.toLowerCase()
          + " failed, due to some other type of I/O error.");
      FantastleReboot.getBagOStuff().getMazeManager()
          .handleDeferredSuccess(false, false, null);
    } catch (final Exception ex) {
      FantastleReboot.logError(ex);
    }
  }
}
