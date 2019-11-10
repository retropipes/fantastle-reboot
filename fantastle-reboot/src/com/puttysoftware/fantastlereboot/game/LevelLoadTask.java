/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.MainWindow;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.maze.Maze;

public class LevelLoadTask extends Thread {
  // Fields
  private final JFrame loadFrame;
  private final int level;

  // Constructors
  public LevelLoadTask(final int offset) {
    this.level = offset;
    this.setName("Level Loader");
    this.loadFrame = MainWindow.getOutputFrame();
    this.loadFrame.setTitle("Loading...");
    final JProgressBar loadBar = new JProgressBar();
    loadBar.setIndeterminate(true);
    this.loadFrame.getContentPane().add(loadBar);
    this.loadFrame.pack();
  }

  // Methods
  @Override
  public void run() {
    try {
      this.loadFrame.setVisible(true);
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final Maze gameMaze = app.getMazeManager().getMaze();
      Game.disableEvents();
      gameMaze.switchLevelOffset(this.level);
      gameMaze.offsetPlayerLocationW(this.level);
      PartyManager.getParty().offsetMonsterLevel(this.level);
      Game.resetViewingWindow();
      Game.enableEvents();
      Game.redrawMaze();
    } catch (final Exception ex) {
      FantastleReboot.logError(ex);
    } finally {
      this.loadFrame.setVisible(false);
    }
  }
}
