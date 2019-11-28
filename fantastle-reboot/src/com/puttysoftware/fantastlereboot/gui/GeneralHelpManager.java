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

import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JPanel;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.Modes;
import com.puttysoftware.fantastlereboot.loaders.HelpLoader;
import com.puttysoftware.help.HTMLHelpViewer;

public class GeneralHelpManager {
  // Fields
  private MainWindow helpFrame;
  private final EventHandler handler = new EventHandler();
  private final JPanel helpContent;
  private final HTMLHelpViewer hv;
  private static final int MAX_WINDOW_SIZE = 700;

  // Constructors
  public GeneralHelpManager() {
    this.helpContent = new JPanel();
    this.hv = HelpLoader.getHelpViewer();
    this.hv.setHelpSize(GeneralHelpManager.MAX_WINDOW_SIZE,
        GeneralHelpManager.MAX_WINDOW_SIZE);
    this.helpContent.setLayout(new FlowLayout());
    this.helpContent.add(this.hv.getHelp());
  }

  // Methods
  public void showHelp() {
    Modes.setInHelp();
    this.helpFrame = MainWindow.getOutputFrame();
    this.helpFrame.setTitle("Fantastle Help");
    this.helpFrame.attachContent(this.helpContent);
    this.helpFrame.pack();
    this.helpFrame.addWindowListener(this.handler);
  }

  public void hideHelp() {
    this.helpFrame.removeWindowListener(this.handler);
    Modes.restore();
  }

  public void updateHelpSize() {
    this.hv.setHelpSize(GeneralHelpManager.MAX_WINDOW_SIZE,
        GeneralHelpManager.MAX_WINDOW_SIZE);
  }

  private class EventHandler implements WindowListener {
    public EventHandler() {
      super();
    }

    @Override
    public void windowOpened(final WindowEvent inE) {
      // Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent inE) {
      GeneralHelpManager.this.hideHelp();
    }

    @Override
    public void windowClosed(final WindowEvent inE) {
    }

    @Override
    public void windowIconified(final WindowEvent inE) {
      // Do nothing
    }

    @Override
    public void windowDeiconified(final WindowEvent inE) {
      // Do nothing
    }

    @Override
    public void windowActivated(final WindowEvent inE) {
      // Do nothing
    }

    @Override
    public void windowDeactivated(final WindowEvent inE) {
      // Do nothing
    }
  }
}