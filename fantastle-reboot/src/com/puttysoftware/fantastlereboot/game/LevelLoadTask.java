/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import java.awt.Container;

import javax.swing.JProgressBar;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.MainWindow;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.maze.Maze;

public class LevelLoadTask extends Thread {
  // Fields
  private MainWindow loadFrame;
  private final int level;
  private final Container content = new Container();

  // Constructors
  public LevelLoadTask(final int offset) {
    this.level = offset;
    this.setName("Level Loader");
    final JProgressBar loadBar = new JProgressBar();
    loadBar.setIndeterminate(true);
    this.content.add(loadBar);
  }

  // Methods
  @Override
  public void run() {
    try {
      this.loadFrame = MainWindow.getOutputFrame();
      this.loadFrame.setTitle("Loading...");
      this.loadFrame.setContentPane(this.content);
      this.loadFrame.pack();
      this.loadFrame.setVisible(true);
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final Maze gameMaze = app.getMazeManager().getMaze();
      Game.disableEvents();
      gameMaze.switchLevelOffset(this.level);
      gameMaze.offsetPlayerLocationW(this.level);
      PartyManager.getParty().offsetMonsterLevel(this.level);
      Game.resetViewingWindow();
      Game.enableEvents();
      Game.showOutput();
      Game.redrawMaze();
    } catch (final Exception ex) {
      FantastleReboot.logError(ex);
    }
  }
}
