/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.puttysoftware.fantastlereboot.obsolete.loaders1.GraphicsManager;
import com.puttysoftware.images.BufferedImageIcon;

public class GUIManager {
    // Fields
    private final JFrame guiFrame;
    private final Container guiPane;
    private final JLabel logoLabel;

    // Constructors
    public GUIManager() {
        this.guiFrame = new JFrame("Fantastle");
        this.guiPane = this.guiFrame.getContentPane();
        this.guiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.guiFrame.setLayout(new GridLayout(1, 1));
        this.logoLabel = new JLabel("", null, SwingConstants.CENTER);
        this.logoLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.guiPane.add(this.logoLabel);
        this.guiFrame.pack();
        this.guiFrame.setResizable(false);
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
        final BagOStuff app = FantastleReboot.getBagOStuff();
        app.setInGUI(true);
        this.guiFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
        this.guiFrame.setVisible(true);
        app.getMenuManager().setMainMenus();
        app.getMenuManager().checkFlags();
    }

    public void hideGUI() {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        app.setInGUI(false);
        this.guiFrame.setVisible(false);
    }

    public void hideGUITemporarily() {
        this.guiFrame.setVisible(false);
    }

    public void updateLogo() {
        final BufferedImageIcon logo = GraphicsManager.getLogo();
        this.logoLabel.setIcon(logo);
        this.guiFrame.pack();
    }
}
