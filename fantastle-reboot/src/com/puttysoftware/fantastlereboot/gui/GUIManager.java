/*  Fantastle: A World-Solving Game
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
package com.puttysoftware.fantastlereboot.gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Modes;
import com.puttysoftware.fantastlereboot.assets.UserInterfaceImageIndex;
import com.puttysoftware.fantastlereboot.loaders.UserInterfaceImageLoader;
import com.puttysoftware.images.BufferedImageIcon;

public class GUIManager {
  // Fields
  private MainWindow guiFrame;
  private final JPanel guiPane;
  private final JLabel logoLabel;

  // Constructors
  public GUIManager() {
    this.guiPane = new JPanel();
    this.guiPane.setLayout(new GridLayout(1, 1));
    this.logoLabel = new JLabel("", null, SwingConstants.CENTER);
    this.logoLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
    this.guiPane.add(this.logoLabel);
    final BufferedImageIcon logo = UserInterfaceImageLoader
        .load(UserInterfaceImageIndex.LOGO);
    this.logoLabel.setIcon(logo);
  }

  // Methods
  public void showGUI() {
    this.guiFrame = MainWindow.getOutputFrame();
    this.guiFrame.setTitle("Fantastle Reboot");
    this.guiFrame.attachContent(this.guiPane);
    this.guiFrame.pack();
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    Modes.setInGUI();
    bag.getMenuManager().setMainMenus();
    bag.getMenuManager().checkFlags();
    BagOStuff.checkForUpdates(false);
  }
}
