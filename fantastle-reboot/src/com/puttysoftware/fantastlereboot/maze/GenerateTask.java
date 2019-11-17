/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze;

import java.awt.Container;

import javax.swing.JProgressBar;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.randomrange.RandomRange;

public class GenerateTask extends Thread {
  // Fields
  private MainWindow generateFrame;
  private final boolean scratch;
  private final Container content = new Container();

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
      this.generateFrame.setContentPane(this.content);
      this.generateFrame.pack();
      final BagOStuff app = FantastleReboot.getBagOStuff();
      Maze gameMaze = MazeManager.getMaze();
      if (!this.scratch) {
        Game.disableEvents();
      } else {
        gameMaze = new Maze();
        MazeManager.setMaze(gameMaze);
      }
      gameMaze.addLevel(Maze.getMaxRows(), Maze.getMaxColumns(),
          Maze.getMaxFloors());
      gameMaze.fillLevelRandomly();
      final RandomRange rR = new RandomRange(0, Maze.getMaxRows() - 1);
      final RandomRange rC = new RandomRange(0, Maze.getMaxColumns() - 1);
      final RandomRange rF = new RandomRange(0, Maze.getMaxFloors() - 1);
      if (this.scratch) {
        int startR, startC, startF;
        do {
          startR = rR.generate();
          startC = rC.generate();
          startF = rF.generate();
        } while (gameMaze.getCell(startR, startC, startF, Layers.OBJECT)
            .isSolid());
        gameMaze.setStartRow(startR);
        gameMaze.setStartColumn(startC);
        gameMaze.setStartFloor(startF);
        FileStateManager.setLoaded(true);
        final boolean playerExists = gameMaze.doesPlayerExist();
        if (playerExists) {
          gameMaze.setPlayerToStart();
          Game.resetViewingWindow();
        }
      } else {
        int startR, startC, startF;
        do {
          startR = rR.generate();
          startC = rC.generate();
          startF = rF.generate();
        } while (gameMaze.getCell(startR, startC, startF, Layers.OBJECT)
            .isSolid());
        gameMaze.setPlayerLocationX(startR);
        gameMaze.setPlayerLocationY(startC);
        gameMaze.setPlayerLocationZ(startF);
        PartyManager.getParty().offsetMonsterLevel(1);
      }
      gameMaze.save();
      // Final cleanup
      if (this.scratch) {
        Game.stateChanged();
        app.getMenuManager().checkFlags();
        app.getGUIManager().showGUI();
      } else {
        Game.resetViewingWindow();
        Game.enableEvents();
        Game.showOutput();
        Game.redrawMaze();
      }
    } catch (final Throwable t) {
      FantastleReboot.logError(t);
    }
  }
}
