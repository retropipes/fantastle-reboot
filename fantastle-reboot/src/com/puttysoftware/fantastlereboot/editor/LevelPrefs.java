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

public class LevelPrefs {
  // Fields
  private static MainWindow prefFrame;
  private static JPanel mainPrefPane, buttonPane, editorPane;
  private static JButton prefsOK, prefsCancel;
  private static JSlider visionRadius;
  private static JSlider exploreExpansionRadius;
  private static JCheckBox visionModeNone;
  private static JCheckBox visionModeExplore;
  private static JCheckBox visionModeFieldOfView;
  private static JCheckBox visionModeFixedRadius;
  private static JCheckBox horizontalWrap;
  private static JCheckBox verticalWrap;
  private static JCheckBox floorWrap;
  private static EventHandler handler;
  private static boolean guiSetUp = false;
  private static final int MIN_VISION_RADIUS = 1;
  private static final int MAX_VISION_RADIUS = 15;
  private static final int GRID_LENGTH = 11;

  // Constructors
  private LevelPrefs() {
    super();
  }

  // Methods
  public static void showPrefs() {
    if (!LevelPrefs.guiSetUp) {
      LevelPrefs.setUpGUI();
      LevelPrefs.guiSetUp = true;
    }
    LevelPrefs.prefFrame = MainWindow.getOutputFrame();
    LevelPrefs.prefFrame.setTitle("Level Preferences");
    LevelPrefs.prefFrame.setDefaultButton(LevelPrefs.prefsOK);
    LevelPrefs.prefFrame.attachContent(LevelPrefs.mainPrefPane);
    LevelPrefs.prefFrame.addWindowListener(LevelPrefs.handler);
    LevelPrefs.prefFrame.pack();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    Modes.setInLevelPrefs();
    app.getMenuManager().setPrefMenus();
  }

  private static void hidePrefs() {
    if (!LevelPrefs.guiSetUp) {
      LevelPrefs.setUpGUI();
      LevelPrefs.guiSetUp = true;
    }
    LevelPrefs.prefFrame.setDefaultButton(null);
    LevelPrefs.prefFrame.removeWindowListener(LevelPrefs.handler);
    LevelPrefs.savePrefs();
    Modes.restore();
  }

  private static void loadPrefs() {
    if (!LevelPrefs.guiSetUp) {
      LevelPrefs.setUpGUI();
      LevelPrefs.guiSetUp = true;
    }
    Maze m = MazeManager.getMaze();
    LevelPrefs.visionRadius.setValue(m.getVisionRadiusValue());
    LevelPrefs.exploreExpansionRadius
        .setValue(m.getExploreExpansionRadiusValue());
    LevelPrefs.visionModeNone.setSelected(m.isLocalVisionModeNone());
    LevelPrefs.visionModeExplore.setSelected(m.isLocalVisionModeExplore());
    LevelPrefs.visionModeFieldOfView
        .setSelected(m.isLocalVisionModeFieldOfView());
    LevelPrefs.visionModeFixedRadius
        .setSelected(m.isLocalVisionModeFixedRadius());
    LevelPrefs.horizontalWrap.setSelected(m.isHorizontalWrapEnabled());
    LevelPrefs.verticalWrap.setSelected(m.isVerticalWrapEnabled());
    LevelPrefs.floorWrap.setSelected(m.isFloorWrapEnabled());
  }

  private static void savePrefs() {
    if (!LevelPrefs.guiSetUp) {
      LevelPrefs.setUpGUI();
      LevelPrefs.guiSetUp = true;
    }
    Maze m = MazeManager.getMaze();
    m.setVisionRadius(LevelPrefs.visionRadius.getValue());
    m.setExploreExpansionRadius(LevelPrefs.exploreExpansionRadius.getValue());
    if (LevelPrefs.visionModeNone.isSelected()) {
      m.resetVisionModeNone();
    }
    if (LevelPrefs.visionModeExplore.isSelected()) {
      m.addVisionModeExplore();
    } else {
      m.removeVisionModeExplore();
    }
    if (LevelPrefs.visionModeFieldOfView.isSelected()) {
      m.addVisionModeFieldOfView();
    } else {
      m.removeVisionModeFieldOfView();
    }
    if (LevelPrefs.visionModeFixedRadius.isSelected()) {
      m.addVisionModeFixedRadius();
    } else {
      m.removeVisionModeFixedRadius();
    }
    m.setHorizontalWrapEnabled(LevelPrefs.horizontalWrap.isSelected());
    m.setVerticalWrapEnabled(LevelPrefs.verticalWrap.isSelected());
    m.setFloorWrapEnabled(LevelPrefs.floorWrap.isSelected());
  }

  private static void setUpGUI() {
    LevelPrefs.handler = new EventHandler();
    LevelPrefs.mainPrefPane = new JPanel();
    LevelPrefs.editorPane = new JPanel();
    LevelPrefs.buttonPane = new JPanel();
    LevelPrefs.prefsOK = new JButton("OK");
    LevelPrefs.prefsOK.setDefaultCapable(true);
    LevelPrefs.prefsCancel = new JButton("Cancel");
    LevelPrefs.prefsCancel.setDefaultCapable(false);
    LevelPrefs.visionRadius = new JSlider(LevelPrefs.MIN_VISION_RADIUS,
        LevelPrefs.MAX_VISION_RADIUS);
    LevelPrefs.visionRadius
        .setLabelTable(LevelPrefs.visionRadius.createStandardLabels(1));
    LevelPrefs.visionRadius.setPaintLabels(true);
    LevelPrefs.exploreExpansionRadius = new JSlider(
        LevelPrefs.MIN_VISION_RADIUS, LevelPrefs.MAX_VISION_RADIUS);
    LevelPrefs.exploreExpansionRadius.setLabelTable(
        LevelPrefs.exploreExpansionRadius.createStandardLabels(1));
    LevelPrefs.exploreExpansionRadius.setPaintLabels(true);
    LevelPrefs.visionModeNone = new JCheckBox("Maze-Wide Vision Mode: OFF",
        false);
    LevelPrefs.visionModeExplore = new JCheckBox(
        "Maze-Wide Vision Mode: Exploring", true);
    LevelPrefs.visionModeFieldOfView = new JCheckBox(
        "Maze-Wide Vision Mode: Field of View", true);
    LevelPrefs.visionModeFixedRadius = new JCheckBox(
        "Maze-Wide Vision Mode: Fixed Radius", false);
    LevelPrefs.mainPrefPane.setLayout(new BorderLayout());
    LevelPrefs.editorPane.setLayout(new GridLayout(LevelPrefs.GRID_LENGTH, 1));
    LevelPrefs.editorPane.add(new JLabel("Level-Wide Vision Radius"));
    LevelPrefs.editorPane.add(LevelPrefs.visionRadius);
    LevelPrefs.editorPane
        .add(new JLabel("Level-Wide Exploring Expansion Radius"));
    LevelPrefs.editorPane.add(LevelPrefs.exploreExpansionRadius);
    LevelPrefs.editorPane.add(LevelPrefs.visionModeNone);
    LevelPrefs.editorPane.add(LevelPrefs.visionModeExplore);
    LevelPrefs.editorPane.add(LevelPrefs.visionModeFieldOfView);
    LevelPrefs.editorPane.add(LevelPrefs.visionModeFixedRadius);
    LevelPrefs.editorPane.add(LevelPrefs.horizontalWrap);
    LevelPrefs.editorPane.add(LevelPrefs.verticalWrap);
    LevelPrefs.editorPane.add(LevelPrefs.floorWrap);
    LevelPrefs.buttonPane.setLayout(new FlowLayout());
    LevelPrefs.buttonPane.add(LevelPrefs.prefsOK);
    LevelPrefs.buttonPane.add(LevelPrefs.prefsCancel);
    LevelPrefs.mainPrefPane.add(LevelPrefs.editorPane, BorderLayout.CENTER);
    LevelPrefs.mainPrefPane.add(LevelPrefs.buttonPane, BorderLayout.SOUTH);
    LevelPrefs.visionModeNone.addItemListener(LevelPrefs.handler);
    LevelPrefs.visionModeExplore.addItemListener(LevelPrefs.handler);
    LevelPrefs.visionModeFixedRadius.addItemListener(LevelPrefs.handler);
    LevelPrefs.prefsOK.addActionListener(LevelPrefs.handler);
    LevelPrefs.prefsCancel.addActionListener(LevelPrefs.handler);
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
          LevelPrefs.savePrefs();
          LevelPrefs.hidePrefs();
        } else if (cmd.equals("Cancel")) {
          LevelPrefs.loadPrefs();
          LevelPrefs.hidePrefs();
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
      try {
        final Object o = e.getItem();
        if (o.getClass().equals(LevelPrefs.visionModeNone.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(LevelPrefs.visionModeNone)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              LevelPrefs.visionModeExplore.setSelected(false);
              LevelPrefs.visionModeFieldOfView.setSelected(false);
              LevelPrefs.visionModeFixedRadius.setSelected(false);
              LevelPrefs.visionModeExplore.setEnabled(false);
              LevelPrefs.visionModeFieldOfView.setEnabled(false);
              LevelPrefs.visionModeFixedRadius.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              LevelPrefs.visionModeExplore.setEnabled(true);
              LevelPrefs.visionModeFieldOfView.setEnabled(true);
              LevelPrefs.visionModeFixedRadius.setEnabled(true);
            }
          }
        } else if (o.getClass()
            .equals(LevelPrefs.visionModeExplore.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(LevelPrefs.visionModeExplore)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              LevelPrefs.visionModeFixedRadius.setSelected(false);
              LevelPrefs.visionModeFixedRadius.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              LevelPrefs.visionModeFixedRadius.setEnabled(true);
            }
          }
        } else if (o.getClass()
            .equals(LevelPrefs.visionModeFixedRadius.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(LevelPrefs.visionModeFixedRadius)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              LevelPrefs.visionModeExplore.setSelected(false);
              LevelPrefs.visionModeExplore.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              LevelPrefs.visionModeExplore.setEnabled(true);
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
        LevelPrefs.hidePrefs();
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
