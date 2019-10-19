/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.apple.eawt.Application;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.utilities.ImageColorConstants;

public class LevelLoadTask extends Thread {
    // Fields
    private final JFrame loadFrame;
    private final int level;

    // Constructors
    public LevelLoadTask(final int offset) {
        this.level = offset;
        this.setName("Level Loader");
        this.loadFrame = new JFrame("Loading...");
        final JProgressBar loadBar = new JProgressBar();
        loadBar.setIndeterminate(true);
        this.loadFrame.getContentPane().add(loadBar);
        this.loadFrame.setResizable(false);
        this.loadFrame
                .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.loadFrame.pack();
    }

    // Methods
    @Override
    public void run() {
        try {
            this.loadFrame.setVisible(true);
            final Application app = FantastleReboot.getBagOStuff();
            final Maze gameMaze = app.getMazeManager().getMaze();
            app.getGameManager().disableEvents();
            gameMaze.switchLevelOffset(this.level);
            gameMaze.offsetPlayerLocationW(this.level);
            PartyManager.getParty().offsetMonsterLevel(this.level);
            FantastleObjectModel
                    .setTemplateColor(ImageColorConstants.getColorForLevel(
                            PartyManager.getParty().getMonsterLevel()));
            app.getGameManager().resetViewingWindow();
            app.getGameManager().enableEvents();
            app.getGameManager().redrawMaze();
        } catch (final Exception ex) {
            FantastleReboot.logError(ex);
        } finally {
            this.loadFrame.setVisible(false);
        }
    }
}
