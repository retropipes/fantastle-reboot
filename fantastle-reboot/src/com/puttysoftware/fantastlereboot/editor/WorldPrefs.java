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
import com.puttysoftware.fantastlereboot.world.World;
import com.puttysoftware.fantastlereboot.world.WorldManager;

public class WorldPrefs {
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
  private static final int DEFAULT_VISION_RADIUS = 2;
  private static final int MAX_VISION_RADIUS = 16;
  private static final int GRID_LENGTH = 12;

  // Constructors
  private WorldPrefs() {
    super();
  }

  // Methods
  public static void showPrefs() {
    if (!WorldPrefs.guiSetUp) {
      WorldPrefs.setUpGUI();
      WorldPrefs.guiSetUp = true;
    }
    WorldPrefs.prefFrame = MainWindow.getOutputFrame();
    WorldPrefs.prefFrame.setTitle("World Preferences");
    WorldPrefs.prefFrame.setDefaultButton(WorldPrefs.prefsOK);
    WorldPrefs.prefFrame.attachContent(WorldPrefs.mainPrefPane);
    WorldPrefs.prefFrame.addWindowListener(WorldPrefs.handler);
    WorldPrefs.prefFrame.pack();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    Modes.setInWorldPrefs();
    app.getMenuManager().setPrefMenus();
  }

  private static void hidePrefs() {
    if (!WorldPrefs.guiSetUp) {
      WorldPrefs.setUpGUI();
      WorldPrefs.guiSetUp = true;
    }
    WorldPrefs.prefFrame.setDefaultButton(null);
    WorldPrefs.prefFrame.removeWindowListener(WorldPrefs.handler);
    WorldPrefs.savePrefs();
    Modes.restore();
  }

  private static void loadPrefs() {
    if (!WorldPrefs.guiSetUp) {
      WorldPrefs.setUpGUI();
      WorldPrefs.guiSetUp = true;
    }
    World m = WorldManager.getWorld();
    WorldPrefs.useGlobalVisionRadiusOverride
        .setSelected(m.isGlobalVisionRadiusSet());
    WorldPrefs.useGlobalExploreExpansionRadiusOverride
        .setSelected(m.isGlobalExploreExpansionRadiusSet());
    WorldPrefs.globalVisionRadius.setValue(m.getGlobalVisionRadius());
    WorldPrefs.globalExploreExpansionRadius
        .setValue(m.getGlobalExploreExpansionRadius());
    WorldPrefs.useGlobalVisionModeOverride
        .setSelected(m.isGlobalVisionModeSet());
    WorldPrefs.globalVisionModeNone.setSelected(m.isGlobalVisionModeNone());
    WorldPrefs.globalVisionModeExplore
        .setSelected(m.isGlobalVisionModeExplore());
    WorldPrefs.globalVisionModeFieldOfView
        .setSelected(m.isGlobalVisionModeFieldOfView());
    WorldPrefs.globalVisionModeFixedRadius
        .setSelected(m.isGlobalVisionModeFixedRadius());
  }

  private static void savePrefs() {
    if (!WorldPrefs.guiSetUp) {
      WorldPrefs.setUpGUI();
      WorldPrefs.guiSetUp = true;
    }
    World m = WorldManager.getWorld();
    if (WorldPrefs.useGlobalVisionRadiusOverride.isSelected()) {
      m.setGlobalVisionRadius(WorldPrefs.globalVisionRadius.getValue());
    } else {
      m.unsetGlobalVisionRadius();
    }
    if (WorldPrefs.useGlobalExploreExpansionRadiusOverride.isSelected()) {
      m.setGlobalExploreExpansionRadius(
          WorldPrefs.globalExploreExpansionRadius.getValue());
    } else {
      m.unsetGlobalExploreExpansionRadius();
    }
    if (WorldPrefs.useGlobalVisionModeOverride.isSelected()) {
      if (WorldPrefs.globalVisionModeNone.isSelected()) {
        m.resetGlobalVisionModeNone();
      }
      if (WorldPrefs.globalVisionModeExplore.isSelected()) {
        m.addGlobalVisionModeExplore();
      } else {
        m.removeGlobalVisionModeExplore();
      }
      if (WorldPrefs.globalVisionModeFieldOfView.isSelected()) {
        m.addGlobalVisionModeFieldOfView();
      } else {
        m.removeGlobalVisionModeFieldOfView();
      }
      if (WorldPrefs.globalVisionModeFixedRadius.isSelected()) {
        m.addGlobalVisionModeFixedRadius();
      } else {
        m.removeGlobalVisionModeFixedRadius();
      }
    } else {
      m.unsetGlobalVisionMode();
    }
  }

  private static void setUpGUI() {
    WorldPrefs.handler = new EventHandler();
    WorldPrefs.mainPrefPane = new JPanel();
    WorldPrefs.editorPane = new JPanel();
    WorldPrefs.buttonPane = new JPanel();
    WorldPrefs.prefsOK = new JButton("OK");
    WorldPrefs.prefsOK.setDefaultCapable(true);
    WorldPrefs.prefsCancel = new JButton("Cancel");
    WorldPrefs.prefsCancel.setDefaultCapable(false);
    WorldPrefs.useGlobalVisionRadiusOverride = new JCheckBox(
        "Enable World-Wide Vision Radius Override", false);
    WorldPrefs.useGlobalExploreExpansionRadiusOverride = new JCheckBox(
        "Enable World-Wide Exploring Expansion Radius Override", false);
    WorldPrefs.useGlobalVisionModeOverride = new JCheckBox(
        "Enable World-Wide Vision Mode Override", false);
    WorldPrefs.globalVisionRadius = new JSlider(WorldPrefs.MIN_VISION_RADIUS,
        WorldPrefs.MAX_VISION_RADIUS, WorldPrefs.DEFAULT_VISION_RADIUS);
    WorldPrefs.globalVisionRadius
        .setLabelTable(WorldPrefs.globalVisionRadius.createStandardLabels(1));
    WorldPrefs.globalVisionRadius.setPaintLabels(true);
    WorldPrefs.globalExploreExpansionRadius = new JSlider(
        WorldPrefs.MIN_VISION_RADIUS, WorldPrefs.MAX_VISION_RADIUS,
        WorldPrefs.DEFAULT_VISION_RADIUS);
    WorldPrefs.globalExploreExpansionRadius.setLabelTable(
        WorldPrefs.globalExploreExpansionRadius.createStandardLabels(1));
    WorldPrefs.globalExploreExpansionRadius.setPaintLabels(true);
    WorldPrefs.globalVisionModeNone = new JCheckBox(
        "World-Wide Vision Mode: OFF", false);
    WorldPrefs.globalVisionModeExplore = new JCheckBox(
        "World-Wide Vision Mode: Exploring", true);
    WorldPrefs.globalVisionModeFieldOfView = new JCheckBox(
        "World-Wide Vision Mode: Field of View", true);
    WorldPrefs.globalVisionModeFixedRadius = new JCheckBox(
        "World-Wide Vision Mode: Fixed Radius", false);
    WorldPrefs.mainPrefPane.setLayout(new BorderLayout());
    WorldPrefs.editorPane.setLayout(new GridLayout(WorldPrefs.GRID_LENGTH, 1));
    WorldPrefs.editorPane.add(new JLabel("World-Level Override Enablers"));
    WorldPrefs.editorPane.add(WorldPrefs.useGlobalVisionRadiusOverride);
    WorldPrefs.editorPane
        .add(WorldPrefs.useGlobalExploreExpansionRadiusOverride);
    WorldPrefs.editorPane.add(WorldPrefs.useGlobalVisionModeOverride);
    WorldPrefs.editorPane.add(new JLabel("World-Level Vision Radius"));
    WorldPrefs.editorPane.add(WorldPrefs.globalVisionRadius);
    WorldPrefs.editorPane
        .add(new JLabel("World-Level Exploring Expansion Radius"));
    WorldPrefs.editorPane.add(WorldPrefs.globalExploreExpansionRadius);
    WorldPrefs.editorPane.add(WorldPrefs.globalVisionModeNone);
    WorldPrefs.editorPane.add(WorldPrefs.globalVisionModeExplore);
    WorldPrefs.editorPane.add(WorldPrefs.globalVisionModeFieldOfView);
    WorldPrefs.editorPane.add(WorldPrefs.globalVisionModeFixedRadius);
    WorldPrefs.buttonPane.setLayout(new FlowLayout());
    WorldPrefs.buttonPane.add(WorldPrefs.prefsOK);
    WorldPrefs.buttonPane.add(WorldPrefs.prefsCancel);
    WorldPrefs.mainPrefPane.add(WorldPrefs.editorPane, BorderLayout.CENTER);
    WorldPrefs.mainPrefPane.add(WorldPrefs.buttonPane, BorderLayout.SOUTH);
    WorldPrefs.useGlobalVisionRadiusOverride
        .addItemListener(WorldPrefs.handler);
    WorldPrefs.useGlobalExploreExpansionRadiusOverride
        .addItemListener(WorldPrefs.handler);
    WorldPrefs.useGlobalVisionModeOverride.addItemListener(WorldPrefs.handler);
    WorldPrefs.globalVisionModeNone.addItemListener(WorldPrefs.handler);
    WorldPrefs.globalVisionModeExplore.addItemListener(WorldPrefs.handler);
    WorldPrefs.globalVisionModeFixedRadius.addItemListener(WorldPrefs.handler);
    WorldPrefs.prefsOK.addActionListener(WorldPrefs.handler);
    WorldPrefs.prefsCancel.addActionListener(WorldPrefs.handler);
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
          WorldPrefs.savePrefs();
          WorldPrefs.hidePrefs();
        } else if (cmd.equals("Cancel")) {
          WorldPrefs.loadPrefs();
          WorldPrefs.hidePrefs();
        }
      } catch (final Exception ex) {
        FantastleReboot.exception(ex);
      }
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
      try {
        final Object o = e.getItem();
        if (o.getClass()
            .equals(WorldPrefs.useGlobalVisionRadiusOverride.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(WorldPrefs.useGlobalVisionRadiusOverride)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              WorldPrefs.globalVisionRadius.setEnabled(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              WorldPrefs.globalVisionRadius.setEnabled(false);
            }
          }
        } else if (o.getClass().equals(
            WorldPrefs.useGlobalExploreExpansionRadiusOverride.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check
              .equals(WorldPrefs.useGlobalExploreExpansionRadiusOverride)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              WorldPrefs.globalExploreExpansionRadius.setEnabled(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              WorldPrefs.globalExploreExpansionRadius.setEnabled(false);
            }
          }
        } else if (o.getClass()
            .equals(WorldPrefs.useGlobalVisionModeOverride.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(WorldPrefs.useGlobalVisionModeOverride)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              WorldPrefs.globalVisionModeNone.setEnabled(true);
              WorldPrefs.globalVisionModeExplore.setEnabled(true);
              WorldPrefs.globalVisionModeFieldOfView.setEnabled(true);
              WorldPrefs.globalVisionModeFixedRadius.setEnabled(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              WorldPrefs.globalVisionModeNone.setEnabled(false);
              WorldPrefs.globalVisionModeExplore.setEnabled(false);
              WorldPrefs.globalVisionModeFieldOfView.setEnabled(false);
              WorldPrefs.globalVisionModeFixedRadius.setEnabled(false);
            }
          }
        } else if (o.getClass()
            .equals(WorldPrefs.globalVisionModeNone.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(WorldPrefs.globalVisionModeNone)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              WorldPrefs.globalVisionModeExplore.setEnabled(false);
              WorldPrefs.globalVisionModeFieldOfView.setEnabled(false);
              WorldPrefs.globalVisionModeFixedRadius.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              WorldPrefs.globalVisionModeExplore.setEnabled(true);
              WorldPrefs.globalVisionModeFieldOfView.setEnabled(true);
              WorldPrefs.globalVisionModeFixedRadius.setEnabled(true);
            }
          }
        } else if (o.getClass()
            .equals(WorldPrefs.globalVisionModeExplore.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(WorldPrefs.globalVisionModeExplore)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              WorldPrefs.globalVisionModeFixedRadius.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              WorldPrefs.globalVisionModeFixedRadius.setEnabled(true);
            }
          }
        } else if (o.getClass()
            .equals(WorldPrefs.globalVisionModeFixedRadius.getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(WorldPrefs.globalVisionModeFixedRadius)) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              WorldPrefs.globalVisionModeExplore.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              WorldPrefs.globalVisionModeExplore.setEnabled(true);
            }
          }
        }
      } catch (final Exception ex) {
        FantastleReboot.exception(ex);
      }
    }

    @Override
    public void windowOpened(final WindowEvent e) {
      // Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent e) {
      try {
        WorldPrefs.hidePrefs();
      } catch (final Exception ex) {
        FantastleReboot.exception(ex);
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
