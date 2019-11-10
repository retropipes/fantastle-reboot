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

import java.awt.FlowLayout;

import javax.swing.JFrame;

import com.puttysoftware.fantastlereboot.loaders.HelpLoader;
import com.puttysoftware.fantastlereboot.utilities.ImageConstants;
import com.puttysoftware.help.HTMLHelpViewer;

public class GeneralHelpManager {
  // Fields
  private final JFrame helpFrame;
  private final HTMLHelpViewer hv;

  // Constructors
  public GeneralHelpManager() {
    this.hv = HelpLoader.getHelpViewer();
    this.helpFrame = MainWindow.getOutputFrame();
    this.helpFrame.setTitle("Fantastle Help");
    this.helpFrame.setLayout(new FlowLayout());
    this.helpFrame.add(this.hv.getHelp());
    // if (FantastleReboot.getBagOStuff().getPrefsManager()
    // .isMobileModeEnabled()) {
    // this.hv.setHelpSize(ImageConstants.MAX_MOBILE_WINDOW_SIZE,
    // ImageConstants.MAX_MOBILE_WINDOW_SIZE);
    // } else {
    this.hv.setHelpSize(ImageConstants.MAX_WINDOW_SIZE,
        ImageConstants.MAX_WINDOW_SIZE);
    // }
    this.helpFrame.pack();
  }

  // Methods
  public void showHelp() {
    this.helpFrame.setVisible(true);
  }

  public void updateHelpSize() {
    // if (FantastleReboot.getBagOStuff().getPrefsManager()
    // .isMobileModeEnabled()) {
    // this.hv.setHelpSize(ImageConstants.MAX_MOBILE_WINDOW_SIZE,
    // ImageConstants.MAX_MOBILE_WINDOW_SIZE);
    // } else {
    this.hv.setHelpSize(ImageConstants.MAX_WINDOW_SIZE,
        ImageConstants.MAX_WINDOW_SIZE);
    // }
    this.helpFrame.pack();
  }
}