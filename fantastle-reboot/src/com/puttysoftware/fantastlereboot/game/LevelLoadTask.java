/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.world.World;
import com.puttysoftware.fantastlereboot.world.WorldManager;

public class LevelLoadTask extends Thread {
  // Fields
  private MainWindow loadFrame;
  private final int level;
  private final JPanel content = new JPanel();

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
      this.loadFrame.attachContent(this.content);
      this.loadFrame.pack();
      final World gameWorld = WorldManager.getWorld();
      Game.disableEvents();
      gameWorld.switchLevelOffset(this.level);
      gameWorld.offsetPlayerLocationW(this.level);
      PartyManager.getParty().offsetMonsterLevel(this.level);
      Game.resetViewingWindow();
      Game.enableEvents();
      Game.showOutput();
      Game.redrawWorld();
    } catch (final Exception ex) {
      FantastleReboot.exception(ex);
    }
  }
}
