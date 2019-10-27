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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import com.puttysoftware.fantastlereboot.maze.Extension;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objects.Tile;

public class PreferencesManager implements PreferencesHandler {
  // Fields
  JFrame prefFrame;
  private JTabbedPane prefTabPane;
  private Container mainPrefPane, buttonPane, miscPane, soundPane;
  // private Container editorPane, musicPane;
  private JButton prefsOK, prefsCancel;
  private JButton prefsExport, prefsImport;
  final JCheckBox[] sounds;
  // final JCheckBox[] music;
  // JCheckBox checkUpdatesStartup;
  // JCheckBox checkBetaUpdatesStartup;
  JCheckBox moveOneAtATime;
  JCheckBox monstersVisible;
  JCheckBox mapBattleEngine;
  JCheckBox timeBattleEngine;
  // JComboBox<String> editorFillChoices;
  // private String[] editorFillChoiceArray;
  JComboBox<String> difficultyChoices;
  // JComboBox<String> updateCheckInterval;
  // private String[] updateCheckIntervalValues;
  private JComboBox<String> viewingWindowChoices;
  // int editorFill;
  private EventHandler handler;
  private final PreferencesFileManager fileMgr;
  private final ExportImportManager eiMgr;
  // boolean checkUpdatesStartupEnabled;
  // boolean checkBetaUpdatesStartupEnabled;
  boolean moveOneAtATimeEnabled;
  boolean monstersVisibleEnabled;
  boolean useMapBattleEngine;
  boolean useTimeBattleEngine;
  int difficultySetting = PreferencesManager.DEFAULT_DIFFICULTY;
  int viewingWindowIndex;
  final boolean[] soundsEnabled;
  // final boolean[] musicEnabled;
  String lastDirOpen;
  String lastDirSave;
  int lastFilterUsed;
  static final int[] VIEWING_WINDOW_SIZES = new int[] { 7, 9, 11, 13, 15, 17,
      19, 21, 23, 25 };
  static final int DEFAULT_SIZE_INDEX = 2;
  static final int DEFAULT_VIEWING_WINDOW_SIZE = VIEWING_WINDOW_SIZES[DEFAULT_SIZE_INDEX];
  private static final String[] VIEWING_WINDOW_SIZE_NAMES = new String[] {
      "Tiny", "Small", "Medium", "Large", "Huge", "Tiny HD", "Small HD",
      "Medium HD", "Large HD", "Huge HD" };
  private static final String[] DIFFICULTY_CHOICE_NAMES = new String[] {
      "Very Easy", "Easy", "Normal", "Hard", "Very Hard" };
  private static final int SOUNDS_ALL = 0;
  public static final int SOUNDS_UI = 1;
  public static final int SOUNDS_GAME = 2;
  public static final int SOUNDS_BATTLE = 3;
  public static final int SOUNDS_SHOP = 4;
  // public static final int MUSIC_ALL = 0;
  // public static final int MUSIC_BATTLE = 1;
  // public static final int MUSIC_EXPLORING = 2;
  public static final int FILTER_MAZE_V2 = 1;
  public static final int FILTER_MAZE_V3 = 2;
  public static final int FILTER_MAZE_V4 = 3;
  public static final int FILTER_GAME = 4;
  public static final int FILTER_MAZE_V5 = 5;
  public static final int DIFFICULTY_VERY_EASY = 0;
  public static final int DIFFICULTY_EASY = 1;
  public static final int DIFFICULTY_NORMAL = 2;
  public static final int DIFFICULTY_HARD = 3;
  public static final int DIFFICULTY_VERY_HARD = 4;
  private static final int DEFAULT_DIFFICULTY = DIFFICULTY_NORMAL;
  private static final int BATTLE_SPEED = 1000;
  private static final int MUSIC_LENGTH = 3;
  private static final int SOUNDS_LENGTH = 5;
  private static final int GRID_LENGTH = 6;
  private static final int PREFS_VERSION_MAJOR = 5;
  private static final int PREFS_VERSION_MINOR = 0;

  // Constructors
  public PreferencesManager() {
    this.sounds = new JCheckBox[PreferencesManager.SOUNDS_LENGTH];
    this.soundsEnabled = new boolean[PreferencesManager.SOUNDS_LENGTH];
    // this.music = new JCheckBox[PreferencesManager.MUSIC_LENGTH];
    // this.musicEnabled = new boolean[PreferencesManager.MUSIC_LENGTH];
    this.setUpGUI();
    this.fileMgr = new PreferencesFileManager();
    this.eiMgr = new ExportImportManager();
    this.setDefaultPrefs();
  }

  // Methods
  public static int getBattleSpeed() {
    return PreferencesManager.BATTLE_SPEED;
  }

  public boolean useMapBattleEngine() {
    return this.useMapBattleEngine;
  }

  public void setMapBattleEngine(final boolean value) {
    this.useMapBattleEngine = value;
  }

  public boolean useTimeBattleEngine() {
    return this.useTimeBattleEngine;
  }

  public void setTimeBattleEngine(final boolean value) {
    this.useTimeBattleEngine = value;
  }

  public int getGameDifficulty() {
    return this.difficultySetting;
  }

  public void setGameDifficulty(final int value) {
    this.difficultySetting = value;
  }

  public String getLastDirOpen() {
    return this.lastDirOpen;
  }

  public void setLastDirOpen(final String value) {
    this.lastDirOpen = value;
  }

  public String getLastDirSave() {
    return this.lastDirSave;
  }

  public void setLastDirSave(final String value) {
    this.lastDirSave = value;
  }

  public int getLastFilterUsedIndex() {
    return this.lastFilterUsed;
  }

  public void setLastFilterUsedIndex(final int value) {
    this.lastFilterUsed = value;
  }
  //
  // public boolean shouldCheckUpdatesAtStartup() {
  // return this.checkUpdatesStartupEnabled;
  // }
  //
  // public boolean shouldCheckBetaUpdatesAtStartup() {
  // return this.checkBetaUpdatesStartupEnabled;
  // }

  public boolean oneMove() {
    return this.moveOneAtATimeEnabled;
  }

  public int getViewingWindowSize() {
    return PreferencesManager.VIEWING_WINDOW_SIZES[this
        .getViewingWindowSizeIndex()];
  }

  public int getViewingWindowSizeIndex() {
    return this.viewingWindowIndex;
  }

  public void setViewingWindowSizeIndex(final int value) {
    this.viewingWindowIndex = value;
  }

  public boolean monstersVisible() {
    return this.monstersVisibleEnabled;
  }

  public boolean getSoundEnabled(final int snd) {
    if (!this.soundsEnabled[PreferencesManager.SOUNDS_ALL]) {
      return false;
    } else {
      return this.soundsEnabled[snd];
    }
  }
  //
  // public boolean getMusicEnabled(final int mus) {
  // if (!this.musicEnabled[PreferencesManager.MUSIC_ALL]) {
  // return false;
  // } else {
  // return this.musicEnabled[mus];
  // }
  // }

  public FantastleObjectModel getEditorDefaultFill() {
    return new Tile();
  }

  private void defaultEnableSounds() {
    for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
      this.soundsEnabled[x] = true;
    }
  }
  //
  // private void defaultEnableMusic() {
  // for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
  // this.musicEnabled[x] = true;
  // }
  // }

  public void setSoundEnabled(final int snd, final boolean status) {
    this.soundsEnabled[snd] = status;
  }
  //
  // public void setMusicEnabled(final int mus, final boolean status) {
  // this.musicEnabled[mus] = status;
  // }

  public JFrame getPrefFrame() {
    if (this.prefFrame != null && this.prefFrame.isVisible()) {
      return this.prefFrame;
    } else {
      return null;
    }
  }

  @Override
  public void handlePreferences(PreferencesEvent inE) {
    this.showPrefs();
  }

  public void showPrefs() {
    if (FantastleReboot.inFantastleReboot()) {
      final BagOStuff app = FantastleReboot.getBagOStuff();
      app.setInPrefs();
      app.getMenuManager().attachMenus();
      app.getMenuManager().setPrefMenus();
      this.prefFrame.setVisible(true);
      if (app.getMode() == BagOStuff.STATUS_BATTLE) {
        app.getBattle().getOutputFrame().setVisible(false);
      } else {
        final int formerMode = app.getFormerMode();
        if (formerMode == BagOStuff.STATUS_GUI) {
          app.getGUIManager().hideGUI();
        } else if (formerMode == BagOStuff.STATUS_GAME) {
          app.getGameManager().hideOutput();
          // } else if (formerMode == BagOStuff.STATUS_EDITOR) {
          // app.getEditor().hideOutput();
        }
      }
    }
  }

  public void hidePrefs() {
    if (FantastleReboot.inFantastleReboot()) {
      final BagOStuff app = FantastleReboot.getBagOStuff();
      this.prefFrame.setVisible(false);
      this.fileMgr.writePreferencesFile();
      if (app.getMode() == BagOStuff.STATUS_BATTLE) {
        app.getBattle().getOutputFrame().setVisible(true);
      } else {
        final int formerMode = app.getFormerMode();
        if (formerMode == BagOStuff.STATUS_GUI) {
          app.getGUIManager().showGUI();
        } else if (formerMode == BagOStuff.STATUS_GAME) {
          app.getGameManager().showOutput();
          // } else if (formerMode == BagOStuff.STATUS_EDITOR) {
          // app.getEditor().showOutput();
        }
      }
    }
  }

  public void writePrefs() {
    this.fileMgr.writePreferencesFile();
  }

  public void resetPrefs() {
    this.fileMgr.deletePreferencesFile();
    this.lastDirOpen = null;
    this.lastDirSave = null;
    this.resetDefaultPrefs();
  }

  public void setPrefs(final boolean initial) {
    this.setPrefs();
  }

  public void setPrefs() {
    // this.editorFill = this.editorFillChoices.getSelectedIndex();
    for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
      this.setSoundEnabled(x, this.sounds[x].isSelected());
    }
    // for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
    // this.setMusicEnabled(x, this.music[x].isSelected());
    // }
    // this.checkUpdatesStartupEnabled = this.checkUpdatesStartup.isSelected();
    // this.checkBetaUpdatesStartupEnabled = this.checkBetaUpdatesStartup
    // .isSelected();
    this.moveOneAtATimeEnabled = this.moveOneAtATime.isSelected();
    boolean oldMonstersVisibleEnabled = this.monstersVisibleEnabled;
    this.monstersVisibleEnabled = this.monstersVisible.isSelected();
    if (this.monstersVisibleEnabled != oldMonstersVisibleEnabled) {
      FantastleReboot.getBagOStuff().getObjects().updateMonster();
    }
    this.hidePrefs();
  }

  public void setDefaultPrefs() {
    if (!this.fileMgr.readPreferencesFile()) {
      this.resetDefaultPrefs();
    }
  }

  private void resetDefaultPrefs() {
    // this.editorFill = 0;
    this.defaultEnableSounds();
    // this.defaultEnableMusic();
    // this.checkUpdatesStartup.setSelected(true);
    // this.checkUpdatesStartupEnabled = true;
    // this.checkBetaUpdatesStartup.setSelected(true);
    // this.checkBetaUpdatesStartupEnabled = true;
    this.moveOneAtATime.setSelected(true);
    this.moveOneAtATimeEnabled = true;
    this.monstersVisible.setSelected(false);
    this.monstersVisibleEnabled = false;
    // this.updateCheckInterval.setSelectedIndex(0);
    this.lastFilterUsed = PreferencesManager.FILTER_MAZE_V5;
  }

  void handleExport() {
    final boolean result = this.eiMgr
        .exportPreferencesFile(this.eiMgr.getExportDestination());
    if (!result) {
      Messager.showErrorDialog("Export Failed!", "Preferences");
    }
  }

  void handleImport() {
    final boolean result = this.eiMgr
        .importPreferencesFile(this.eiMgr.getImportSource());
    if (!result) {
      Messager.showErrorDialog("Import Failed!", "Preferences");
    } else {
      this.prefFrame.repaint();
    }
  }

  private void setUpGUI() {
    this.handler = new EventHandler();
    this.prefFrame = new JFrame("Preferences");
    this.prefTabPane = new JTabbedPane();
    this.mainPrefPane = new Container();
    // this.editorPane = new Container();
    this.soundPane = new Container();
    // this.musicPane = new Container();
    this.miscPane = new Container();
    this.prefTabPane.setOpaque(true);
    this.buttonPane = new Container();
    this.prefsOK = new JButton("OK");
    this.prefsOK.setDefaultCapable(true);
    this.prefFrame.getRootPane().setDefaultButton(this.prefsOK);
    this.prefsCancel = new JButton("Cancel");
    this.prefsCancel.setDefaultCapable(false);
    this.prefsExport = new JButton("Export...");
    this.prefsExport.setDefaultCapable(false);
    this.prefsImport = new JButton("Import...");
    this.prefsImport.setDefaultCapable(false);
    // this.editorFillChoiceArray = new String[] { "Grass", "Dirt", "Sand",
    // "Snow", "Tile", "Tundra" };
    // this.editorFillChoices = new JComboBox<>(this.editorFillChoiceArray);
    this.sounds[PreferencesManager.SOUNDS_ALL] = new JCheckBox(
        "Enable ALL sounds", true);
    this.sounds[PreferencesManager.SOUNDS_UI] = new JCheckBox(
        "Enable user interface sounds", true);
    this.sounds[PreferencesManager.SOUNDS_GAME] = new JCheckBox(
        "Enable game sounds", true);
    this.sounds[PreferencesManager.SOUNDS_BATTLE] = new JCheckBox(
        "Enable battle sounds", true);
    this.sounds[PreferencesManager.SOUNDS_SHOP] = new JCheckBox(
        "Enable shop sounds", true);
    // this.music[PreferencesManager.MUSIC_ALL] = new JCheckBox(
    // "Enable ALL music", true);
    // this.music[PreferencesManager.MUSIC_BATTLE] = new JCheckBox(
    // "Enable battle music", true);
    // this.music[PreferencesManager.MUSIC_EXPLORING] = new JCheckBox(
    // "Enable exploring music", true);
    // this.checkUpdatesStartup = new JCheckBox("Check for Updates at Startup",
    // true);
    // this.checkBetaUpdatesStartup = new JCheckBox(
    // "Check for Beta Updates at Startup", true);
    this.moveOneAtATime = new JCheckBox("One Move at a Time", true);
    this.monstersVisible = new JCheckBox("Show monsters on map", false);
    // this.updateCheckIntervalValues = new String[] { "Daily",
    // "Every 2nd Day", "Weekly", "Every 2nd Week", "Monthly" };
    // this.updateCheckInterval = new JComboBox<>(
    // this.updateCheckIntervalValues);
    this.difficultyChoices = new JComboBox<>(DIFFICULTY_CHOICE_NAMES);
    this.prefFrame.setContentPane(this.mainPrefPane);
    this.prefFrame
        .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.prefFrame.addWindowListener(this.handler);
    this.mainPrefPane.setLayout(new BorderLayout());
    this.prefFrame.setResizable(false);
    // this.editorPane
    // .setLayout(new GridLayout(PreferencesManager.GRID_LENGTH, 1));
    // this.editorPane.add(new JLabel("Default fill for new mazes:"));
    // this.editorPane.add(this.editorFillChoices);
    this.soundPane.setLayout(new GridLayout(PreferencesManager.GRID_LENGTH, 1));
    for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
      this.soundPane.add(this.sounds[x]);
    }
    // this.musicPane
    // .setLayout(new GridLayout(PreferencesManager.GRID_LENGTH, 1));
    // for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
    // this.musicPane.add(this.music[x]);
    // }
    this.miscPane.setLayout(new GridLayout(PreferencesManager.GRID_LENGTH, 1));
    // this.miscPane.add(this.checkUpdatesStartup);
    // if (BagOStuff.isBetaModeEnabled()) {
    // this.miscPane.add(this.checkBetaUpdatesStartup);
    // }
    this.miscPane.add(this.moveOneAtATime);
    this.miscPane.add(this.monstersVisible);
    // this.miscPane.add(new JLabel("Check How Often For Updates"));
    // this.miscPane.add(this.updateCheckInterval);
    this.miscPane.add(new JLabel("Game Difficulty"));
    this.miscPane.add(this.difficultyChoices);
    final Container viewPane = new Container();
    viewPane.setLayout(new GridLayout(PreferencesManager.GRID_LENGTH, 1));
    viewPane.add(new JLabel("Viewing Window Size"));
    this.viewingWindowChoices = new JComboBox<>(
        PreferencesManager.VIEWING_WINDOW_SIZE_NAMES);
    viewPane.add(this.viewingWindowChoices);
    this.buttonPane.setLayout(new FlowLayout());
    this.buttonPane.add(this.prefsOK);
    this.buttonPane.add(this.prefsCancel);
    this.buttonPane.add(this.prefsExport);
    this.buttonPane.add(this.prefsImport);
    // this.prefTabPane.addTab("Editor", null, this.editorPane, "Editor");
    this.prefTabPane.addTab("Sounds", null, this.soundPane, "Sounds");
    // this.prefTabPane.addTab("Music", null, this.musicPane, "Music");
    this.prefTabPane.addTab("Misc.", null, this.miscPane, "Misc.");
    this.prefTabPane.addTab("View", null, viewPane, "View");
    this.mainPrefPane.add(this.prefTabPane, BorderLayout.CENTER);
    this.mainPrefPane.add(this.buttonPane, BorderLayout.SOUTH);
    this.sounds[PreferencesManager.SOUNDS_ALL].addItemListener(this.handler);
    this.prefsOK.addActionListener(this.handler);
    this.prefsCancel.addActionListener(this.handler);
    this.prefsExport.addActionListener(this.handler);
    this.prefsImport.addActionListener(this.handler);
    this.prefFrame.pack();
  }

  private class PreferencesFileManager {
    // Fields
    private static final String MAC_PREFIX = "HOME";
    private static final String WIN_PREFIX = "APPDATA";
    private static final String UNIX_PREFIX = "HOME";
    private static final String MAC_DIR = "/Library/Preferences/";
    private static final String WIN_DIR = "\\Fantastle\\";
    private static final String UNIX_DIR = "/.fantastle/";
    private static final String MAC_FILE = "net.worldwizard.fantastle.preferences";
    private static final String WIN_FILE = "FantastlePreferences";
    private static final String UNIX_FILE = "FantastlePreferences";

    // Constructors
    public PreferencesFileManager() {
      // Do nothing
    }

    // Methods
    public void deletePreferencesFile() {
      // Delete preferences file
      final File prefs = this.getPrefsFile();
      prefs.delete();
    }

    public boolean readPreferencesFile() {
      try (final BufferedReader s = new BufferedReader(
          new FileReader(this.getPrefsFile()))) {
        // Read the preferences from the file
        final PreferencesManager pm = PreferencesManager.this;
        // Read major version
        final int majorVersion = Short.parseShort(s.readLine());
        // Read minor version
        final int minorVersion = Short.parseShort(s.readLine());
        // Version check
        if (majorVersion == PreferencesManager.PREFS_VERSION_MAJOR) {
          if (minorVersion > PreferencesManager.PREFS_VERSION_MINOR) {
            throw new PreferencesException(
                "Incompatible preferences minor version, using defaults.");
          }
        } else {
          throw new PreferencesException(
              "Incompatible preferences major version, using defaults.");
        }
        // Read and discard
        s.readLine();
        // pm.editorFillChoices
        // .setSelectedIndex(Integer.parseInt(s.readLine()));
        // Read and discard
        s.readLine();
        // pm.checkUpdatesStartup
        // .setSelected(Boolean.parseBoolean(s.readLine()));
        pm.moveOneAtATime.setSelected(Boolean.parseBoolean(s.readLine()));
        for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
          pm.sounds[x].setSelected(Boolean.parseBoolean(s.readLine()));
        }
        // Read and discard
        s.readLine();
        // pm.updateCheckInterval
        // .setSelectedIndex(Integer.parseInt(s.readLine()));
        pm.lastDirOpen = s.readLine();
        pm.lastDirSave = s.readLine();
        // Read and discard
        s.readLine();
        // pm.checkBetaUpdatesStartup
        // .setSelected(Boolean.parseBoolean(s.readLine()));
        pm.lastFilterUsed = Integer.parseInt(s.readLine());
        pm.monstersVisible.setSelected(Boolean.parseBoolean(s.readLine()));
        for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
          // Read and discard
          s.readLine();
          // pm.music[x].setSelected(Boolean.parseBoolean(s.readLine()));
        }
        pm.setPrefs(true);
        return true;
      } catch (final PreferencesException pe) {
        Messager.showDialog(pe.getMessage());
        return false;
      } catch (final IOException ie) {
        return false;
      } catch (final Exception e) {
        FantastleReboot.logWarningWithMessage(e,
            "An error occurred while attempting to read the preferences file. Using defaults.");
        return false;
      }
    }

    public void writePreferencesFile() {
      // Create the needed subdirectories, if they don't already exist
      final File prefsFile = this.getPrefsFile();
      final File prefsParent = new File(this.getPrefsFile().getParent());
      if (!prefsFile.canWrite()) {
        prefsParent.mkdirs();
      }
      try (final BufferedWriter s = new BufferedWriter(
          new FileWriter(prefsFile))) {
        // Write the preferences to the file
        final PreferencesManager pm = PreferencesManager.this;
        s.write(
            Integer.toString(PreferencesManager.PREFS_VERSION_MAJOR) + "\n");
        s.write(
            Integer.toString(PreferencesManager.PREFS_VERSION_MINOR) + "\n");
        s.write("0\n");
        // s.write(Integer.toString(pm.editorFill) + "\n");
        s.write("false\n");
        // s.write(Boolean.toString(pm.checkUpdatesStartupEnabled) + "\n");
        s.write(Boolean.toString(pm.moveOneAtATimeEnabled) + "\n");
        for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
          s.write(Boolean.toString(pm.soundsEnabled[x]) + "\n");
        }
        s.write("0\n");
        // s.write(Integer.toString(
        // pm.updateCheckInterval.getSelectedIndex()) + "\n");
        s.write(pm.lastDirOpen + "\n");
        s.write(pm.lastDirSave + "\n");
        s.write("false\n");
        // s.write(Boolean.toString(pm.checkBetaUpdatesStartupEnabled)
        // + "\n");
        s.write(Integer.toString(pm.lastFilterUsed) + "\n");
        s.write(Boolean.toString(pm.monstersVisibleEnabled) + "\n");
        for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
          s.write("false\n");
          // s.write(Boolean.toString(pm.musicEnabled[x]) + "\n");
        }
        s.close();
      } catch (final IOException ie) {
        FantastleReboot.logWarning(ie);
      }
    }

    private String getPrefsDirPrefix() {
      final String osName = System.getProperty("os.name");
      if (osName.indexOf("Mac OS X") != -1) {
        // Mac OS X
        return System.getenv(PreferencesFileManager.MAC_PREFIX);
      } else if (osName.indexOf("Windows") != -1) {
        // Windows
        return System.getenv(PreferencesFileManager.WIN_PREFIX);
      } else {
        // Other - assume UNIX-like
        return System.getenv(PreferencesFileManager.UNIX_PREFIX);
      }
    }

    private String getPrefsDirectory() {
      final String osName = System.getProperty("os.name");
      if (osName.indexOf("Mac OS X") != -1) {
        // Mac OS X
        return PreferencesFileManager.MAC_DIR;
      } else if (osName.indexOf("Windows") != -1) {
        // Windows
        return PreferencesFileManager.WIN_DIR;
      } else {
        // Other - assume UNIX-like
        return PreferencesFileManager.UNIX_DIR;
      }
    }

    private String getPrefsFileExtension() {
      return "." + Extension.getPreferencesExtension();
    }

    private String getPrefsFileName() {
      final String osName = System.getProperty("os.name");
      if (osName.indexOf("Mac OS X") != -1) {
        // Mac OS X
        return PreferencesFileManager.MAC_FILE;
      } else if (osName.indexOf("Windows") != -1) {
        // Windows
        return PreferencesFileManager.WIN_FILE;
      } else {
        // Other - assume UNIX-like
        return PreferencesFileManager.UNIX_FILE;
      }
    }

    private File getPrefsFile() {
      final StringBuilder b = new StringBuilder();
      b.append(this.getPrefsDirPrefix());
      b.append(this.getPrefsDirectory());
      b.append(this.getPrefsFileName());
      b.append(this.getPrefsFileExtension());
      return new File(b.toString());
    }
  }

  private class ExportImportManager {
    // Constructors
    public ExportImportManager() {
      // Do nothing
    }

    // Methods
    public boolean importPreferencesFile(final File importFile) {
      try (final BufferedReader s = new BufferedReader(
          new FileReader(importFile))) {
        // Read the preferences from the file
        final PreferencesManager pm = PreferencesManager.this;
        // Read and discard major version
        s.readLine();
        // Read and discard minor version
        s.readLine();
        // Read and discard
        s.readLine();
        // pm.editorFillChoices
        // .setSelectedIndex(Integer.parseInt(s.readLine()));
        // Read and discard
        s.readLine();
        // pm.checkUpdatesStartup
        // .setSelected(Boolean.parseBoolean(s.readLine()));
        pm.moveOneAtATime.setSelected(Boolean.parseBoolean(s.readLine()));
        for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
          pm.sounds[x].setSelected(Boolean.parseBoolean(s.readLine()));
        }
        // Read and discard
        s.readLine();
        // pm.updateCheckInterval
        // .setSelectedIndex(Integer.parseInt(s.readLine()));
        // Read and discard
        s.readLine();
        // pm.checkBetaUpdatesStartup
        // .setSelected(Boolean.parseBoolean(s.readLine()));
        pm.lastFilterUsed = Integer.parseInt(s.readLine());
        pm.monstersVisible.setSelected(Boolean.parseBoolean(s.readLine()));
        for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
          // Read and discard
          s.readLine();
          // pm.music[x].setSelected(Boolean.parseBoolean(s.readLine()));
        }
        s.close();
        pm.setPrefs(true);
        return true;
      } catch (final IOException ie) {
        FantastleReboot.logWarning(ie);
        return false;
      }
    }

    public boolean exportPreferencesFile(final File exportFile) {
      try (final BufferedWriter s = new BufferedWriter(
          new FileWriter(exportFile))) {
        // Write the preferences to the file
        final PreferencesManager pm = PreferencesManager.this;
        s.write(
            Integer.toString(PreferencesManager.PREFS_VERSION_MAJOR) + "\n");
        s.write(
            Integer.toString(PreferencesManager.PREFS_VERSION_MINOR) + "\n");
        s.write("0\n");
        // s.write(Integer.toString(pm.editorFill) + "\n");
        s.write("false\n");
        // s.write(Boolean.toString(pm.checkUpdatesStartupEnabled) + "\n");
        s.write(Boolean.toString(pm.moveOneAtATimeEnabled) + "\n");
        for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
          s.write(Boolean.toString(pm.soundsEnabled[x]) + "\n");
        }
        s.write("0\n");
        // s.write(Integer.toString(
        // pm.updateCheckInterval.getSelectedIndex()) + "\n");
        s.write("false\n");
        // s.write(Boolean.toString(pm.checkBetaUpdatesStartupEnabled)
        // + "\n");
        s.write(Integer.toString(pm.lastFilterUsed) + "\n");
        s.write(Boolean.toString(pm.monstersVisibleEnabled) + "\n");
        for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
          s.write("false\n");
          // s.write(Boolean.toString(pm.musicEnabled[x]) + "\n");
        }
        s.close();
        return true;
      } catch (final IOException ie) {
        FantastleReboot.logWarning(ie);
        return false;
      }
    }

    public File getImportSource() {
      final FileDialog chooser = new FileDialog(
          PreferencesManager.this.prefFrame, "Import", FileDialog.LOAD);
      chooser.setVisible(true);
      return new File(chooser.getDirectory() + chooser.getFile());
    }

    public File getExportDestination() {
      final FileDialog chooser = new FileDialog(
          PreferencesManager.this.prefFrame, "Export", FileDialog.SAVE);
      chooser.setVisible(true);
      return new File(chooser.getDirectory() + chooser.getFile());
    }
  }

  private class EventHandler
      implements ActionListener, ItemListener, WindowListener {
    public EventHandler() {
      // Do nothing
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
      try {
        final PreferencesManager pm = PreferencesManager.this;
        final String cmd = e.getActionCommand();
        if (cmd.equals("OK")) {
          pm.setPrefs(false);
        } else if (cmd.equals("Cancel")) {
          pm.hidePrefs();
        } else if (cmd.equals("Export...")) {
          pm.handleExport();
        } else if (cmd.equals("Import...")) {
          pm.handleImport();
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
      try {
        final PreferencesManager pm = PreferencesManager.this;
        final Object o = e.getItem();
        if (o.getClass()
            .equals(pm.sounds[PreferencesManager.SOUNDS_ALL].getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(pm.sounds[PreferencesManager.SOUNDS_ALL])) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              for (int x = 1; x < PreferencesManager.SOUNDS_LENGTH; x++) {
                pm.sounds[x].setEnabled(true);
              }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              for (int x = 1; x < PreferencesManager.SOUNDS_LENGTH; x++) {
                pm.sounds[x].setEnabled(false);
              }
            }
          }
          // } else if (o.getClass().equals(
          // pm.music[PreferencesManager.MUSIC_ALL].getClass())) {
          // final JCheckBox check = (JCheckBox) o;
          // if (check.equals(pm.music[PreferencesManager.MUSIC_ALL])) {
          // if (e.getStateChange() == ItemEvent.SELECTED) {
          // for (int x = 1; x < PreferencesManager.MUSIC_LENGTH; x++) {
          // pm.music[x].setEnabled(true);
          // }
          // } else if (e.getStateChange() == ItemEvent.DESELECTED) {
          // for (int x = 1; x < PreferencesManager.MUSIC_LENGTH; x++) {
          // pm.music[x].setEnabled(false);
          // }
          // }
          // }
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
        final PreferencesManager pm = PreferencesManager.this;
        pm.hidePrefs();
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
