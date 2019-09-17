/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.obsolete.loaders.LogoManager;
import com.puttysoftware.fantastlereboot.obsolete.maze2.Maze;
import com.puttysoftware.fantastlereboot.obsolete.maze2.MazeManager;
import com.puttysoftware.fileutils.DirectoryUtilities;
import com.puttysoftware.images.BufferedImageIcon;

public final class GUIManager {
    // Fields
    private final JFrame guiFrame;
    private final JLabel logoLabel;

    // Constructors
    public GUIManager() {
        final CloseHandler cHandler = new CloseHandler();
        this.guiFrame = new JFrame("TallerTower");
        final Image iconlogo = LogoManager.getIconLogo();
        this.guiFrame.setIconImage(iconlogo);
        final Container guiPane = this.guiFrame.getContentPane();
        this.guiFrame
                .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.guiFrame.setLayout(new GridLayout(1, 1));
        this.logoLabel = new JLabel("", null, SwingConstants.CENTER);
        this.logoLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        guiPane.add(this.logoLabel);
        this.guiFrame.setResizable(false);
        this.guiFrame.addWindowListener(cHandler);
    }

    // Methods
    public JFrame getGUIFrame() {
        if (this.guiFrame.isVisible()) {
            return this.guiFrame;
        } else {
            return null;
        }
    }

    public void showGUI() {
        final Application app = TallerTower.getApplication();
        app.setMode(Application.STATUS_GUI);
        this.guiFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
        this.guiFrame.setVisible(true);
        app.getMenuManager().setMainMenus();
        app.getMenuManager().checkFlags();
    }

    public void hideGUI() {
        this.guiFrame.setVisible(false);
    }

    public void hideGUITemporarily() {
        this.guiFrame.setVisible(false);
    }

    public void updateLogo() {
        final BufferedImageIcon logo = LogoManager.getLogo();
        this.logoLabel.setIcon(logo);
        final Image iconlogo = Application.getIconLogo();
        this.guiFrame.setIconImage(iconlogo);
        this.guiFrame.pack();
    }

    // Used by reflection, so cannot be static!
    @SuppressWarnings("static-method")
    public boolean quitHandler() {
        final MazeManager mm = TallerTower.getApplication().getMazeManager();
        boolean saved = true;
        int status = JOptionPane.DEFAULT_OPTION;
        if (mm.getDirty()) {
            status = MazeManager.showSaveDialog();
            if (status == JOptionPane.YES_OPTION) {
                saved = MazeManager.saveGame();
            } else if (status == JOptionPane.CANCEL_OPTION) {
                saved = false;
            } else {
                mm.setDirty(false);
            }
        }
        if (saved) {
            FantastleReboot.getBagOStuff().getPrefsManager().writePrefs();
            // Run cleanup task
            try {
                final File dirToDelete = new File(Maze.getMazeTempFolder());
                DirectoryUtilities.removeDirectory(dirToDelete);
            } catch (final Throwable t) {
                // Ignore
            }
        }
        return saved;
    }

    private class CloseHandler implements WindowListener {
        CloseHandler() {
            // Do nothing
        }

        @Override
        public void windowActivated(final WindowEvent arg0) {
            // Do nothing
        }

        @Override
        public void windowClosed(final WindowEvent arg0) {
            // Do nothing
        }

        @Override
        public void windowClosing(final WindowEvent arg0) {
            if (GUIManager.this.quitHandler()) {
                System.exit(0);
            }
        }

        @Override
        public void windowDeactivated(final WindowEvent arg0) {
            // Do nothing
        }

        @Override
        public void windowDeiconified(final WindowEvent arg0) {
            // Do nothing
        }

        @Override
        public void windowIconified(final WindowEvent arg0) {
            // Do nothing
        }

        @Override
        public void windowOpened(final WindowEvent arg0) {
            // Do nothing
        }
    }
}
