/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.world;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.randomrange.RandomRange;

public class GenerateTask extends Thread {
  // Fields
  private MainWindow generateFrame;
  private final boolean scratch;
  private final JPanel content = new JPanel();

  // Constructors
  public GenerateTask(final boolean startFromScratch) {
    this.scratch = startFromScratch;
    this.setName("Level Generator");
    final JProgressBar loadBar = new JProgressBar();
    loadBar.setIndeterminate(true);
    this.content.add(loadBar);
  }

  // Methods
  @Override
  public void run() {
    try {
      this.generateFrame = MainWindow.getOutputFrame();
      this.generateFrame.setTitle("Generating...");
      this.generateFrame.attachContent(this.content);
      this.generateFrame.pack();
      final BagOStuff app = FantastleReboot.getBagOStuff();
      World gameWorld = WorldManager.getWorld();
      if (!this.scratch) {
        Game.disableEvents();
      } else {
        gameWorld = new World();
        WorldManager.setWorld(gameWorld);
      }
      gameWorld.addLevel(World.getMaxRows(), World.getMaxColumns(),
          World.getMaxFloors());
      gameWorld.fillLevelRandomly();
      final RandomRange rR = new RandomRange(0, World.getMaxRows() - 1);
      final RandomRange rC = new RandomRange(0, World.getMaxColumns() - 1);
      final RandomRange rF = new RandomRange(0, World.getMaxFloors() - 1);
      if (this.scratch) {
        int startR, startC, startF;
        do {
          startR = rR.generate();
          startC = rC.generate();
          startF = rF.generate();
        } while (gameWorld.getCell(startR, startC, startF, Layers.OBJECT)
            .isSolid());
        gameWorld.setStartRow(startR);
        gameWorld.setStartColumn(startC);
        gameWorld.setStartFloor(startF);
        FileStateManager.setLoaded(true);
        final boolean playerExists = gameWorld.doesPlayerExist();
        if (playerExists) {
          gameWorld.setPlayerToStart();
          Game.resetViewingWindow();
        }
      } else {
        int startR, startC, startF;
        do {
          startR = rR.generate();
          startC = rC.generate();
          startF = rF.generate();
        } while (gameWorld.getCell(startR, startC, startF, Layers.OBJECT)
            .isSolid());
        gameWorld.setPlayerLocationX(startR);
        gameWorld.setPlayerLocationY(startC);
        gameWorld.setPlayerLocationZ(startF);
        PartyManager.getParty().offsetMonsterLevel(1);
      }
      gameWorld.save();
      // Final cleanup
      if (this.scratch) {
        Game.stateChanged();
        app.getMenuManager().checkFlags();
        app.getGUIManager().showGUI();
      } else {
        // Going deeper...
        SoundPlayer.playSound(SoundIndex.DOWN, SoundGroup.GAME);
        Game.goToLevelOffset(1);
        Game.resetViewingWindow();
        Game.enableEvents();
        Game.showOutput();
        Game.redrawWorld();
      }
    } catch (final Throwable t) {
      FantastleReboot.exception(t);
    }
  }
}
