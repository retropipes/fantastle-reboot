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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Modes;
import com.puttysoftware.fantastlereboot.assets.UserInterfaceImageIndex;
import com.puttysoftware.fantastlereboot.loaders.UserInterfaceImageLoader;

public class AboutDialog implements AboutHandler {
  // Fields
  private MainWindow aboutFrame;
  private JPanel aboutPane, textPane, buttonPane, logoPane;
  private JButton aboutOK;
  private EventHandler handler;

  // Constructors
  public AboutDialog(final String ver) {
    this.setUpGUI(ver);
  }

  // Methods
  @Override
  public void handleAbout(final AboutEvent inE) {
    this.showAboutDialog();
  }

  public void showAboutDialog() {
    Modes.setInAbout();
    this.aboutFrame = MainWindow.getOutputFrame();
    this.aboutFrame.setTitle("About Fantastle");
    this.aboutFrame.setDefaultButton(this.aboutOK);
    this.aboutFrame.addWindowListener(this.handler);
    this.aboutFrame.attachContent(this.aboutPane);
    this.aboutFrame.pack();
  }

  void hideAboutDialog() {
    this.aboutFrame.removeWindowListener(this.handler);
    Modes.restore();
  }

  private void setUpGUI(final String ver) {
    this.handler = new EventHandler();
    this.aboutPane = new JPanel();
    this.textPane = new JPanel();
    this.buttonPane = new JPanel();
    this.logoPane = new JPanel();
    this.aboutOK = new JButton("OK");
    this.aboutOK.setDefaultCapable(true);
    this.aboutPane.setLayout(new BorderLayout());
    this.logoPane.setLayout(new FlowLayout());
    this.logoPane.add(new JLabel("",
        UserInterfaceImageLoader.load(UserInterfaceImageIndex.MINI_LOGO),
        SwingConstants.LEFT));
    this.textPane.setLayout(new GridLayout(8, 1));
    this.textPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    this.textPane.add(new JLabel("Fantastle Reboot"));
    this.textPane.add(new JLabel("A world solving + role playing hybrid game"));
    this.textPane.add(new JLabel("Version: " + ver));
    this.textPane.add(new JLabel("Author: Putty Software"));
    this.textPane.add(new JLabel(
        "Web Site: https://puttysoftware.github.com/fantastle-reboot/"));
    this.textPane.add(new JLabel("Special thanks to:"));
    this.textPane.add(
        new JLabel("The #IndieDevWorldOrder gang on Twitter, for feedback"));
    this.buttonPane.setLayout(new FlowLayout());
    this.buttonPane.add(this.aboutOK);
    this.aboutPane.add(this.logoPane, BorderLayout.WEST);
    this.aboutPane.add(this.textPane, BorderLayout.CENTER);
    this.aboutPane.add(this.buttonPane, BorderLayout.SOUTH);
    this.aboutOK.addActionListener(this.handler);
  }

  private class EventHandler implements ActionListener, WindowListener {
    public EventHandler() {
      // TODO Auto-generated constructor stub
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
      try {
        final AboutDialog ad = AboutDialog.this;
        final String cmd = e.getActionCommand();
        if (cmd.equals("OK")) {
          ad.hideAboutDialog();
        }
      } catch (final Exception ex) {
        FantastleReboot.exception(ex);
      }
    }

    @Override
    public void windowOpened(final WindowEvent inE) {
      // Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent inE) {
      AboutDialog.this.aboutFrame.setDefaultButton(null);
      AboutDialog.this.aboutFrame.removeWindowListener(this);
      Modes.restore();
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