/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.files.versions.VersionException;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.world.World;
import com.puttysoftware.fantastlereboot.world.WorldManager;
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
    final File worldFile = new File(this.filename);
    try {
      this.loadFrame = MainWindow.getOutputFrame();
      this.loadFrame.setTitle("Loading");
      final JProgressBar loadBar = new JProgressBar();
      loadBar.setIndeterminate(true);
      final JPanel content = new JPanel();
      content.add(loadBar);
      this.loadFrame.attachContent(content);
      this.loadFrame.pack();
      int startW;
      Game.setSavedGameFlag(false);
      World gameWorld = new World();
      // Unzip the file
      ZipUtilities.unzipDirectory(worldFile, new File(gameWorld.getBasePath()));
      // Set prefix handler
      gameWorld.setPrefixHandler(new PrefixHandler());
      // Set suffix handler
      gameWorld.setSuffixHandler(new SuffixHandler());
      gameWorld = gameWorld.readWorld();
      if (gameWorld == null) {
        throw new IOException("Unknown object encountered.");
      }
      WorldManager.setWorld(gameWorld);
      startW = gameWorld.getStartLevel();
      gameWorld.switchLevel(startW);
      final boolean playerExists = gameWorld.doesPlayerExist();
      if (playerExists) {
        WorldManager.getWorld().setPlayerToStart();
        Game.resetViewingWindow();
      }
      gameWorld.save();
      // Final cleanup
      Game.stateChanged();
      FileStateManager.setLoaded(true);
      CommonDialogs.showDialog(sg + " loaded.");
      Game.playWorld();
      GameFileManager.handleDeferredSuccess(true, false, null);
    } catch (final VersionException ve) {
      CommonDialogs.showDialog("Loading the " + sg.toLowerCase()
          + " failed, due to the format version being unsupported.");
      GameFileManager.handleDeferredSuccess(false, true, worldFile);
    } catch (final Exception ex) {
      FantastleReboot.exception(ex);
    }
  }
}
