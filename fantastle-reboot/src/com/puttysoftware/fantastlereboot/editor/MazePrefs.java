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
package com.puttysoftware.fantastlereboot.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Modes;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;

public class MazePrefs {
  // Fields
  private static MainWindow prefFrame;
  private static JPanel mainPrefPane, buttonPane, editorPane;
  private static JButton prefsOK, prefsCancel;
  private static JCheckBox useGlobalVisionRadiusOverride;
  private static JCheckBox useGlobalExploreExpansionRadiusOverride;
  private static JCheckBox useGlobalVisionModeOverride;
  private static JSlider globalVisionRadius;
  private static JSlider globalExploreExpansionRadius;
  private static JCheckBox globalVisionModeNone;
  private static JCheckBox globalVisionModeExplore;
  private static JCheckBox globalVisionModeFieldOfView;
  private static JCheckBox globalVisionModeFixedRadius;
  private static EventHandler handler;
  private static boolean guiSetUp = false;
  private static final int MIN_VISION_RADIUS = 1;
  private static final int MAX_VISION_RADIUS = 15;
  private static final int GRID_LENGTH = 12;

  // Constructors
  private MazePrefs() {
    super();
  }

  // Methods
  public static void showPrefs() {
    if (!MazePrefs.guiSetUp) {
      MazePrefs.setUpGUI();
      MazePrefs.guiSetUp = true;
    }
    MazePrefs.prefFrame = MainWindow.getOutputFrame();
    MazePrefs.prefFrame.setTitle("Maze Preferences");
    MazePrefs.prefFrame.setDefaultButton(MazePrefs.prefsOK);
    MazePrefs.prefFrame.attachContent(MazePrefs.mainPrefPane);
    MazePrefs.prefFrame.addWindowListener(MazePrefs.handler);
    MazePrefs.prefFrame.pack();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    Modes.setInMazePrefs();
    app.getMenuManager().setPrefMenus();
  }

  private static void hidePrefs() {
    if (!MazePrefs.guiSetUp) {
      MazePrefs.setUpGUI();
      MazePrefs.guiSetUp = true;
    }
    MazePrefs.prefFrame.setDefaultButton(null);
    MazePrefs.prefFrame.removeWindowListener(MazePrefs.handler);
    MazePrefs.savePrefs();
    Modes.restore();
  }

  private static void loadPrefs() {
    if (!MazePrefs.guiSetUp) {
      MazePrefs.setUpGUI();
      MazePrefs.guiSetUp = true;
    }
    Maze m = MazeManager.getMaze();
    MazePrefs.useGlobalVisionRadiusOverride
        .setSelected(m.isGlobalVisionRadiusSet());
    MazePrefs.useGlobalExploreExpansionRadiusOverride
        .setSelected(m.isGlobalExploreExpansionRadiusSet());
    MazePrefs.globalVisionRadius.setValue(m.getGlobalVisionRadius());
    MazePrefs.globalExploreExpansionRadius
        .setValue(m.getGlobalExploreExpansionRadius());
    MazePrefs.useGlobalVisionModeOverride
        .setSelected(m.isGlobalVisionModeSet());
    MazePrefs.globalVisionModeNone.setSelected(m.isGlobalVisionModeNone());
    MazePrefs.globalVisionModeExplore
        .setSelected(m.isGlobalVisionModeExplore());
    MazePrefs.globalVisionModeFieldOfView
        .setSelected(m.isGlobalVisionModeFieldOfView());
    MazePrefs.globalVisionModeFixedRadius
        .setSelected(m.isGlobalVisionModeFixedRadius());
  }

  private static void savePrefs() {
    if (!MazePrefs.guiSetUp) {
      MazePrefs.setUpGUI();
      MazePrefs.guiSetUp = true;
    }
    Maze m = MazeManager.getMaze();
    if (MazePrefs.useGlobalVisionRadiusOverride.isSelected()) {
      m.setGlobalVisionRadius(MazePrefs.globalVisionRadius.getValue());
    } else {
      m.unsetGlobalVisionRadius();
    }
    if (MazePrefs.useGlobalExploreExpansionRadiusOverride.isSelected()) {
      m.setGlobalExploreExpansionRadius(
          MazePrefs.globalExploreExpansionRadius.getValue());
    } else {
      m.unsetGlobalExploreExpansionRadius();
    }
    if (MazePrefs.useGlobalVisionModeOverride.isSelected()) {
      if (MazePrefs.globalVisionModeNone.isSelected()) {
        m.resetGlobalVisionModeNone();
      }
      if (MazePrefs.globalVisionModeExplore.isSelected()) {
        m.addGlobalVisionModeExplore();
      } else {
        m.removeGlobalVisionModeExplore();
      }
      if (MazePrefs.globalVisionModeFieldOfView.isSelected()) {
        m.addGlobalVisionModeFieldOfView();
      } else {
        m.removeGlobalVisionModeFieldOfView();
      }
      if (MazePrefs.globalVisionModeFixedRadius.isSelected()) {
        m.addGlobalVisionModeFixedRadius();
      } else {
        m.removeGlobalVisionModeFixedRadius();
      }
    } else {
      m.unsetGlobalVisionMode();
    }
  }

  private static void setUpGUI() {
    MazePrefs.handler = new EventHandler();
    MazePrefs.mainPrefPane = new JPanel();
    MazePrefs.editorPane = new JPanel();
    MazePrefs.buttonPane = new JPanel();
    MazePrefs.prefsOK = new JButton("OK");
    MazePrefs.prefsOK.setDefaultCapable(true);
    MazePrefs.prefsCancel = new JButton("Cancel");
    MazePrefs.prefsCancel.setDefaultCapable(false);
    MazePrefs.useGlobalVisionRadiusOverride = new JCheckBox(
        "Enable Maze-Wide Vision Radius Override", false);
    MazePrefs.useGlobalExploreExpansionRadiusOverride = new JCheckBox(
        "Enable Maze-Wide Exploring Expansion Radius Override", false);
    MazePrefs.useGlobalVisionModeOverride = new JCheckBox(
        "Enable Maze-Wide Vision Mode Override", false);
    MazePrefs.globalVisionRadius = new JSlider(MazePrefs.MIN_VISION_RADIUS,
        MazePrefs.MAX_VISION_RADIUS);
    MazePrefs.globalVisionRadius
        .setLabelTable(MazePrefs.globalVisionRadius.createStandardLabels(1));
    MazePrefs.globalVisionRadius.setPaintLabels(true);
    MazePrefs.globalExploreExpansionRadius = new JSlider(
        MazePrefs.MIN_VISION_RADIUS, MazePrefs.MAX_VISION_RADIUS);
    MazePrefs.globalExploreExpansionRadius.setLabelTable(
        MazePrefs.globalExploreExpansionRadius.createStandardLabels(1));
    MazePrefs.globalExploreExpansionRadius.setPaintLabels(true);
    MazePrefs.globalVisionModeNone = new JCheckBox("Maze-Wide Vision Mode: OFF",
        false);
    MazePrefs.globalVisionModeExplore = new JCheckBox(
        "Maze-Wide Vision Mode: Exploring", true);
    MazePrefs.globalVisionModeFieldOfView = new JCheckBox(
        "Maze-Wide Vision Mode: Field of View", true);
    MazePrefs.globalVisionModeFixedRadius = new JCheckBox(
        "Maze-Wide Vision Mode: Fixed Radius", false);
    MazePrefs.mainPrefPane.setLayout(new BorderLayout());
    MazePrefs.editorPane.setLayout(new GridLayout(MazePrefs.GRID_LENGTH, 1));
    MazePrefs.editorPane.add(new JLabel("Maze-Level Override Enablers"));
    MazePrefs.editorPane.add(MazePrefs.useGlobalVisionRadiusOverride);
    MazePrefs.editorPane.add(MazePrefs.useGlobalExploreExpansionRadiusOverride);
    MazePrefs.editorPane.add(MazePrefs.useGlobalVisionModeOverride);
    MazePrefs.editorPane.add(new JLabel("Maze-Level Vision Radius"));
    MazePrefs.editorPane.add(MazePrefs.globalVisionRadius);
    MazePrefs.editorPane
        .add(new JLabel("Maze-Level Exploring Expansion Radius"));
    MazePrefs.editorPane.add(MazePrefs.globalExploreExpansionRadius);
    MazePrefs.editorPane.add(MazePrefs.globalVisionModeNone);
    MazePrefs.editorPane.add(MazePrefs.globalVisionModeExplore);
    MazePrefs.editorPane.add(MazePrefs.globalVisionModeFieldOfView);
    MazePrefs.editorPane.add(MazePrefs.globalVisionModeFixedRadius);
    MazePrefs.buttonPane.setLayout(new FlowLayout());
    MazePrefs.buttonPane.add(MazePrefs.prefsOK);
    MazePrefs.buttonPane.add(MazePrefs.prefsCancel);
    MazePrefs.mainPrefPane.add(MazePrefs.editorPane, BorderLayout.CENTER);
    MazePrefs.mainPrefPane.add(MazePrefs.buttonPane, BorderLayout.SOUTH);
    MazePrefs.useGlobalVisionRadiusOverride.addItemListener(MazePrefs.handler);
    MazePrefs.useGlobalExploreExpansionRadiusOverride
        .addItemListener(MazePrefs.handler);
    MazePrefs.useGlobalVisionModeOverride.addItemListener(MazePrefs.handler);
    MazePrefs.globalVisionModeNone.addItemListener(MazePrefs.handler);
    MazePrefs.globalVisionModeExplore.addItemListener(MazePrefs.handler);
    MazePrefs.globalVisionModeFixedRadius.addItemListener(MazePrefs.handler);
    MazePrefs.prefsOK.addActionListener(MazePrefs.handler);
    MazePrefs.prefsCancel.addActionListener(MazePrefs.handler);
  }

  private static class EventHandler
      implements ActionListener, ItemListener, WindowListener {
    public EventHandler() {
      // Do nothing
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
      try {
        final String cmd = e.getActionCommand();
        if (cmd.equals("OK")) {
          MazePrefs.savePrefs();
          MazePrefs.hidePrefs();
        } else if (cmd.equals("Cancel")) {
          MazePrefs.loadPrefs();
          MazePrefs.hidePrefs();
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
      try {
        final Object o = e.getItem();
        if (o.getClass()
            .equals(MazePrefs.useGlobalVisionRadiusOverride.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(MazePrefs.useGlobalVisionRadiusOverride)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              MazePrefs.globalVisionRadius.setEnabled(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              MazePrefs.globalVisionRadius.setEnabled(false);
            }
          }
        } else if (o.getClass().equals(
            MazePrefs.useGlobalExploreExpansionRadiusOverride.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(MazePrefs.useGlobalExploreExpansionRadiusOverride)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              MazePrefs.globalExploreExpansionRadius.setEnabled(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              MazePrefs.globalExploreExpansionRadius.setEnabled(false);
            }
          }
        } else if (o.getClass()
            .equals(MazePrefs.useGlobalVisionModeOverride.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(MazePrefs.useGlobalVisionModeOverride)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              MazePrefs.globalVisionModeNone.setEnabled(true);
              MazePrefs.globalVisionModeExplore.setEnabled(true);
              MazePrefs.globalVisionModeFieldOfView.setEnabled(true);
              MazePrefs.globalVisionModeFixedRadius.setEnabled(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              MazePrefs.globalVisionModeNone.setSelected(false);
              MazePrefs.globalVisionModeExplore.setSelected(false);
              MazePrefs.globalVisionModeFieldOfView.setSelected(false);
              MazePrefs.globalVisionModeFixedRadius.setSelected(false);
              MazePrefs.globalVisionModeNone.setEnabled(false);
              MazePrefs.globalVisionModeExplore.setEnabled(false);
              MazePrefs.globalVisionModeFieldOfView.setEnabled(false);
              MazePrefs.globalVisionModeFixedRadius.setEnabled(false);
            }
          }
        } else if (o.getClass()
            .equals(MazePrefs.globalVisionModeNone.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(MazePrefs.globalVisionModeNone)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              MazePrefs.globalVisionModeExplore.setSelected(false);
              MazePrefs.globalVisionModeFieldOfView.setSelected(false);
              MazePrefs.globalVisionModeFixedRadius.setSelected(false);
              MazePrefs.globalVisionModeExplore.setEnabled(false);
              MazePrefs.globalVisionModeFieldOfView.setEnabled(false);
              MazePrefs.globalVisionModeFixedRadius.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              MazePrefs.globalVisionModeExplore.setEnabled(true);
              MazePrefs.globalVisionModeFieldOfView.setEnabled(true);
              MazePrefs.globalVisionModeFixedRadius.setEnabled(true);
            }
          }
        } else if (o.getClass()
            .equals(MazePrefs.globalVisionModeExplore.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(MazePrefs.globalVisionModeExplore)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              MazePrefs.globalVisionModeFixedRadius.setSelected(false);
              MazePrefs.globalVisionModeFixedRadius.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              MazePrefs.globalVisionModeFixedRadius.setEnabled(true);
            }
          }
        } else if (o.getClass()
            .equals(MazePrefs.globalVisionModeFixedRadius.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(MazePrefs.globalVisionModeFixedRadius)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              MazePrefs.globalVisionModeExplore.setSelected(false);
              MazePrefs.globalVisionModeExplore.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              MazePrefs.globalVisionModeExplore.setEnabled(true);
            }
          }
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    @Override
    public void windowOpened(final WindowEvent e) {
      // Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent e) {
      try {
        MazePrefs.hidePrefs();
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    @Override
    public void windowClosed(final WindowEvent e) {
      // Do nothing
    }

    @Override
    public void windowIconified(final WindowEvent e) {
      // Do nothing
    }

    @Override
    public void windowDeiconified(final WindowEvent e) {
      // Do nothing
    }

    @Override
    public void windowActivated(final WindowEvent e) {
      // Do nothing
    }

    @Override
    public void windowDeactivated(final WindowEvent e) {
      // Do nothing
    }
  }
}
