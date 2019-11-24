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
package com.puttysoftware.fantastlereboot.gui;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.MusicGroup;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.files.CommonPaths;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objects.Tile;
import com.puttysoftware.randomrange.RandomRange;

public class Prefs {
  // Fields
  private static MainWindow prefFrame;
  private static JTabbedPane prefTabPane;
  private static JPanel mainPrefPane, buttonPane, miscPane, soundPane,
      musicPane;
  private static JPanel editorPane;
  private static JButton prefsOK, prefsCancel;
  private static JButton prefsExport, prefsImport;
  private static JCheckBox[] sounds = new JCheckBox[Prefs.SOUNDS_LENGTH];
  private static JCheckBox[] music = new JCheckBox[Prefs.MUSIC_LENGTH];
  private static JCheckBox checkUpdatesStartup;
  private static JCheckBox moveOneAtATime;
  private static JCheckBox mapBattleEngine;
  private static JCheckBox timeBattleEngine;
  private static JComboBox<String> editorFillChoices;
  private static String[] editorFillChoiceArray;
  private static JComboBox<String> difficultyChoices;
  private static JComboBox<String> updateCheckInterval;
  private static String[] updateCheckIntervalValues;
  private static JComboBox<String> viewingWindowChoices;
  private static EventHandler handler;
  private static PreferencesFileManager fileMgr = new PreferencesFileManager();
  private static ExportImportManager eiMgr = new ExportImportManager();
  private static int editorFill;
  private static boolean checkUpdatesStartupEnabled;
  private static boolean moveOneAtATimeEnabled;
  private static boolean useMapBattleEngine;
  private static boolean useTimeBattleEngine;
  private static int difficultySetting = Prefs.DEFAULT_DIFFICULTY;
  private static int viewingWindowIndex;
  private static int updateCheckIntervalIndex;
  private static boolean[] soundsEnabled = new boolean[Prefs.SOUNDS_LENGTH];
  private static boolean[] musicEnabled = new boolean[Prefs.MUSIC_LENGTH];
  private static String lastDirOpen;
  private static String lastDirSave;
  private static int lastFilterUsed;
  private static boolean guiSetUp = false;
  private static final int[] VIEWING_WINDOW_SIZES = new int[] { 7, 9, 11, 13,
      15, 17, 19, 21, 23, 25 };
  private static final int[] MDM_MIN_TOP = new int[] { 5, 4, 3, 2, 1 };
  private static final int[] MDM_MIN_BOT = new int[] { 5, 5, 5, 5, 5 };
  private static final int[] MDM_MAX_TOP = new int[] { 5, 5, 5, 5, 5 };
  private static final int[] MDM_MAX_BOT = new int[] { 5, 4, 3, 2, 1 };
  private static final int DEFAULT_SIZE_INDEX = 2;
  private static final String[] VIEWING_WINDOW_SIZE_NAMES = new String[] {
      "Tiny", "Small", "Medium", "Large", "Huge", "Tiny HD", "Small HD",
      "Medium HD", "Large HD", "Huge HD" };
  private static final String[] DIFFICULTY_CHOICE_NAMES = new String[] {
      "Very Easy", "Easy", "Normal", "Hard", "Very Hard" };
  private static final int SOUNDS_ALL = 0;
  private static final int SOUNDS_UI = 1;
  private static final int SOUNDS_GAME = 2;
  private static final int SOUNDS_BATTLE = 3;
  private static final int SOUNDS_SHOP = 4;
  private static final int MUSIC_ALL = 0;
  private static final int MUSIC_UI = 1;
  private static final int MUSIC_GAME = 2;
  private static final int MUSIC_BATTLE = 3;
  private static final int MUSIC_SHOP = 4;
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
  private static final int DEFAULT_DIFFICULTY = Prefs.DIFFICULTY_NORMAL;
  private static final int BATTLE_SPEED = 1000;
  private static final int MUSIC_LENGTH = 5;
  private static final int SOUNDS_LENGTH = 5;
  private static final int GRID_LENGTH = 6;
  private static final int PREFS_VERSION_MAJOR = 0;
  private static final int PREFS_VERSION_MINOR = 0;

  // Constructors
  private Prefs() {
    super();
  }

  // Methods
  public static int getMonsterCount(final int pmCount) {
    final int diff = Prefs.getGameDifficulty();
    int min = pmCount * Prefs.MDM_MIN_TOP[diff] / Prefs.MDM_MIN_BOT[diff];
    if (min < 1) {
      min = 1;
    }
    int max = pmCount * Prefs.MDM_MAX_TOP[diff] / Prefs.MDM_MAX_BOT[diff];
    if (max < 1) {
      max = 1;
    }
    return RandomRange.generate(min, max);
  }

  public static int getBattleSpeed() {
    return Prefs.BATTLE_SPEED;
  }

  public static boolean useMapBattleEngine() {
    return Prefs.useMapBattleEngine;
  }

  public static void setMapBattleEngine(final boolean value) {
    Prefs.useMapBattleEngine = value;
  }

  public static boolean useTimeBattleEngine() {
    return Prefs.useTimeBattleEngine;
  }

  public static void setTimeBattleEngine(final boolean value) {
    Prefs.useTimeBattleEngine = value;
  }

  public static int getGameDifficulty() {
    return Prefs.difficultySetting;
  }

  public static void setGameDifficulty(final int value) {
    Prefs.difficultySetting = value;
  }

  public static String getLastDirOpen() {
    return Prefs.lastDirOpen;
  }

  public static void setLastDirOpen(final String value) {
    Prefs.lastDirOpen = value;
  }

  public static String getLastDirSave() {
    return Prefs.lastDirSave;
  }

  public static void setLastDirSave(final String value) {
    Prefs.lastDirSave = value;
  }

  public static int getLastFilterUsedIndex() {
    return Prefs.lastFilterUsed;
  }

  public static void setLastFilterUsedIndex(final int value) {
    Prefs.lastFilterUsed = value;
  }

  public static boolean shouldCheckUpdatesAtStartup() {
    return Prefs.checkUpdatesStartupEnabled;
  }

  public static boolean oneMove() {
    return Prefs.moveOneAtATimeEnabled;
  }

  public static int getViewingWindowSize() {
    return Prefs.VIEWING_WINDOW_SIZES[Prefs.getViewingWindowSizeIndex()];
  }

  public static int getViewingWindowSizeIndex() {
    return Prefs.viewingWindowIndex;
  }

  public static void setViewingWindowSizeIndex(final int value) {
    Prefs.viewingWindowIndex = value;
  }

  public static boolean isSoundGroupEnabled(final SoundGroup group) {
    return Prefs.isSoundGroupEnabledImpl(group.ordinal());
  }

  private static boolean isSoundGroupEnabledImpl(final int snd) {
    if (!Prefs.soundsEnabled[Prefs.SOUNDS_ALL]) {
      return false;
    } else {
      return Prefs.soundsEnabled[snd];
    }
  }

  public static boolean isMusicGroupEnabled(final MusicGroup group) {
    return Prefs.isMusicGroupEnabledImpl(group.ordinal());
  }

  private static boolean isMusicGroupEnabledImpl(final int mus) {
    if (!Prefs.musicEnabled[Prefs.MUSIC_ALL]) {
      return false;
    } else {
      return Prefs.musicEnabled[mus];
    }
  }

  public static FantastleObjectModel getEditorDefaultFill() {
    return new Tile();
  }

  private static void defaultEnableSoundGroups() {
    for (int x = 0; x < Prefs.SOUNDS_LENGTH; x++) {
      Prefs.soundsEnabled[x] = true;
    }
  }

  private static void defaultEnableMusicGroups() {
    for (int x = 0; x < Prefs.MUSIC_LENGTH; x++) {
      Prefs.musicEnabled[x] = true;
    }
  }

  private static void setSoundGroupEnabledImpl(final int snd,
      final boolean status) {
    Prefs.soundsEnabled[snd] = status;
  }

  private static void setMusicGroupEnabled(final int mus,
      final boolean status) {
    Prefs.musicEnabled[mus] = status;
  }

  public static void showPrefs() {
    if (!Prefs.guiSetUp) {
      Prefs.setUpGUI();
      Prefs.guiSetUp = true;
    }
    if (FantastleReboot.inFantastleReboot()) {
      Prefs.prefFrame = MainWindow.getOutputFrame();
      Prefs.prefFrame.setTitle("Preferences");
      Prefs.prefFrame.setDefaultButton(Prefs.prefsOK);
      Prefs.prefFrame.attachContent(Prefs.mainPrefPane);
      Prefs.prefFrame.addWindowListener(Prefs.handler);
      Prefs.prefFrame.pack();
      final BagOStuff app = FantastleReboot.getBagOStuff();
      app.setInPrefs();
      app.getMenuManager().setPrefMenus();
    }
  }

  private static void hidePrefs() {
    if (!Prefs.guiSetUp) {
      Prefs.setUpGUI();
      Prefs.guiSetUp = true;
    }
    if (FantastleReboot.inFantastleReboot()) {
      Prefs.prefFrame.setDefaultButton(null);
      Prefs.prefFrame.removeWindowListener(Prefs.handler);
      Prefs.fileMgr.writePreferencesFile();
      FantastleReboot.getBagOStuff().restoreFormerMode();
    }
  }

  public static void writePrefs() {
    Prefs.fileMgr.writePreferencesFile();
  }

  public static void resetPrefs() {
    Prefs.fileMgr.deletePreferencesFile();
    Prefs.lastDirOpen = null;
    Prefs.lastDirSave = null;
    Prefs.resetDefaultPrefs();
  }

  private static void loadPrefs() {
    if (!Prefs.guiSetUp) {
      Prefs.setUpGUI();
      Prefs.guiSetUp = true;
    }
    Prefs.editorFillChoices.setSelectedIndex(Prefs.editorFill);
    for (int x = 0; x < Prefs.SOUNDS_LENGTH; x++) {
      Prefs.sounds[x].setSelected(Prefs.isSoundGroupEnabledImpl(x));
    }
    for (int x = 0; x < Prefs.MUSIC_LENGTH; x++) {
      Prefs.music[x].setSelected(Prefs.isMusicGroupEnabledImpl(x));
    }
    Prefs.updateCheckInterval.setSelectedIndex(Prefs.updateCheckIntervalIndex);
    Prefs.checkUpdatesStartup.setSelected(Prefs.checkUpdatesStartupEnabled);
    Prefs.difficultyChoices.setSelectedIndex(Prefs.difficultySetting);
    Prefs.moveOneAtATime.setSelected(Prefs.moveOneAtATimeEnabled);
    Prefs.mapBattleEngine.setSelected(Prefs.useMapBattleEngine);
    Prefs.timeBattleEngine.setSelected(Prefs.useTimeBattleEngine);
    Prefs.viewingWindowChoices.setSelectedIndex(Prefs.viewingWindowIndex);
  }

  private static void savePrefs() {
    if (!Prefs.guiSetUp) {
      Prefs.setUpGUI();
      Prefs.guiSetUp = true;
    }
    Prefs.editorFill = Prefs.editorFillChoices.getSelectedIndex();
    for (int x = 0; x < Prefs.SOUNDS_LENGTH; x++) {
      Prefs.setSoundGroupEnabledImpl(x, Prefs.sounds[x].isSelected());
    }
    for (int x = 0; x < Prefs.MUSIC_LENGTH; x++) {
      Prefs.setMusicGroupEnabled(x, Prefs.music[x].isSelected());
    }
    Prefs.updateCheckIntervalIndex = Prefs.updateCheckInterval
        .getSelectedIndex();
    Prefs.checkUpdatesStartupEnabled = Prefs.checkUpdatesStartup.isSelected();
    Prefs.difficultySetting = Prefs.difficultyChoices.getSelectedIndex();
    Prefs.moveOneAtATimeEnabled = Prefs.moveOneAtATime.isSelected();
    Prefs.useMapBattleEngine = Prefs.mapBattleEngine.isSelected();
    Prefs.useTimeBattleEngine = Prefs.timeBattleEngine.isSelected();
    Prefs.viewingWindowIndex = Prefs.viewingWindowChoices.getSelectedIndex();
  }

  public static void setDefaultPrefs() {
    if (!Prefs.fileMgr.readPreferencesFile()) {
      Prefs.resetDefaultPrefs();
    }
  }

  private static void resetDefaultPrefs() {
    if (!Prefs.guiSetUp) {
      Prefs.setUpGUI();
      Prefs.guiSetUp = true;
    }
    Prefs.editorFill = 0;
    Prefs.defaultEnableSoundGroups();
    Prefs.defaultEnableMusicGroups();
    Prefs.checkUpdatesStartup.setSelected(true);
    Prefs.checkUpdatesStartupEnabled = true;
    Prefs.difficultySetting = Prefs.DEFAULT_DIFFICULTY;
    Prefs.difficultyChoices.setSelectedIndex(Prefs.difficultySetting);
    Prefs.moveOneAtATime.setSelected(true);
    Prefs.moveOneAtATimeEnabled = true;
    Prefs.mapBattleEngine.setSelected(true);
    Prefs.useMapBattleEngine = true;
    Prefs.timeBattleEngine.setSelected(false);
    Prefs.useTimeBattleEngine = false;
    Prefs.viewingWindowIndex = Prefs.DEFAULT_SIZE_INDEX;
    Prefs.viewingWindowChoices.setSelectedIndex(Prefs.viewingWindowIndex);
    Prefs.updateCheckInterval.setSelectedIndex(0);
    Prefs.lastFilterUsed = Prefs.FILTER_MAZE_V5;
  }

  static void handleExport() {
    final boolean result = Prefs.eiMgr
        .exportPreferencesFile(Prefs.eiMgr.getExportDestination());
    if (!result) {
      CommonDialogs.showErrorDialog("Export Failed!", "Preferences");
    }
  }

  static void handleImport() {
    final boolean result = Prefs.eiMgr
        .importPreferencesFile(Prefs.eiMgr.getImportSource());
    if (!result) {
      CommonDialogs.showErrorDialog("Import Failed!", "Preferences");
    }
  }

  private static void setUpGUI() {
    Prefs.handler = new EventHandler();
    Prefs.prefTabPane = new JTabbedPane();
    Prefs.mainPrefPane = new JPanel();
    Prefs.editorPane = new JPanel();
    Prefs.soundPane = new JPanel();
    Prefs.musicPane = new JPanel();
    Prefs.miscPane = new JPanel();
    Prefs.prefTabPane.setOpaque(true);
    Prefs.buttonPane = new JPanel();
    Prefs.prefsOK = new JButton("OK");
    Prefs.prefsOK.setDefaultCapable(true);
    Prefs.prefsCancel = new JButton("Cancel");
    Prefs.prefsCancel.setDefaultCapable(false);
    Prefs.prefsExport = new JButton("Export...");
    Prefs.prefsExport.setDefaultCapable(false);
    Prefs.prefsImport = new JButton("Import...");
    Prefs.prefsImport.setDefaultCapable(false);
    Prefs.editorFillChoiceArray = new String[] { "Grass", "Dirt", "Sand",
        "Snow", "Tile", "Tundra" };
    Prefs.editorFillChoices = new JComboBox<>(Prefs.editorFillChoiceArray);
    Prefs.sounds[Prefs.SOUNDS_ALL] = new JCheckBox("Enable ALL sounds", true);
    Prefs.sounds[Prefs.SOUNDS_UI] = new JCheckBox(
        "Enable user interface sounds", true);
    Prefs.sounds[Prefs.SOUNDS_GAME] = new JCheckBox("Enable game sounds", true);
    Prefs.sounds[Prefs.SOUNDS_BATTLE] = new JCheckBox("Enable battle sounds",
        true);
    Prefs.sounds[Prefs.SOUNDS_SHOP] = new JCheckBox("Enable shop sounds", true);
    Prefs.music[Prefs.MUSIC_ALL] = new JCheckBox("Enable ALL music", true);
    Prefs.music[Prefs.MUSIC_UI] = new JCheckBox("Enable user interface music",
        true);
    Prefs.music[Prefs.MUSIC_GAME] = new JCheckBox("Enable game music", true);
    Prefs.music[Prefs.MUSIC_BATTLE] = new JCheckBox("Enable battle music",
        true);
    Prefs.music[Prefs.MUSIC_SHOP] = new JCheckBox("Enable shop music", true);
    Prefs.checkUpdatesStartup = new JCheckBox("Check for Updates at Startup",
        true);
    Prefs.moveOneAtATime = new JCheckBox("One Move at a Time", true);
    Prefs.mapBattleEngine = new JCheckBox("Use map battle engine", false);
    Prefs.timeBattleEngine = new JCheckBox("Use time battle engine", false);
    Prefs.updateCheckIntervalValues = new String[] { "Daily", "Every 2nd Day",
        "Weekly", "Every 2nd Week", "Monthly" };
    Prefs.updateCheckInterval = new JComboBox<>(
        Prefs.updateCheckIntervalValues);
    Prefs.difficultyChoices = new JComboBox<>(Prefs.DIFFICULTY_CHOICE_NAMES);
    Prefs.mainPrefPane.setLayout(new BorderLayout());
    Prefs.editorPane.setLayout(new GridLayout(Prefs.GRID_LENGTH, 1));
    Prefs.editorPane.add(new JLabel("Default fill for new mazes:"));
    Prefs.editorPane.add(Prefs.editorFillChoices);
    Prefs.soundPane.setLayout(new GridLayout(Prefs.GRID_LENGTH, 1));
    for (int x = 0; x < Prefs.SOUNDS_LENGTH; x++) {
      Prefs.soundPane.add(Prefs.sounds[x]);
    }
    Prefs.musicPane.setLayout(new GridLayout(Prefs.GRID_LENGTH, 1));
    for (int x = 0; x < Prefs.MUSIC_LENGTH; x++) {
      Prefs.musicPane.add(Prefs.music[x]);
    }
    Prefs.miscPane.setLayout(new GridLayout(Prefs.GRID_LENGTH, 1));
    Prefs.miscPane.add(Prefs.checkUpdatesStartup);
    Prefs.miscPane.add(Prefs.moveOneAtATime);
    Prefs.miscPane.add(Prefs.mapBattleEngine);
    Prefs.miscPane.add(Prefs.timeBattleEngine);
    Prefs.miscPane.add(new JLabel("Check How Often For Updates"));
    Prefs.miscPane.add(Prefs.updateCheckInterval);
    Prefs.miscPane.add(new JLabel("Game Difficulty"));
    Prefs.miscPane.add(Prefs.difficultyChoices);
    final JPanel viewPane = new JPanel();
    viewPane.setLayout(new GridLayout(Prefs.GRID_LENGTH, 1));
    viewPane.add(new JLabel("Viewing Window Size"));
    Prefs.viewingWindowChoices = new JComboBox<>(
        Prefs.VIEWING_WINDOW_SIZE_NAMES);
    viewPane.add(Prefs.viewingWindowChoices);
    Prefs.buttonPane.setLayout(new FlowLayout());
    Prefs.buttonPane.add(Prefs.prefsOK);
    Prefs.buttonPane.add(Prefs.prefsCancel);
    Prefs.buttonPane.add(Prefs.prefsExport);
    Prefs.buttonPane.add(Prefs.prefsImport);
    Prefs.prefTabPane.addTab("Editor", null, Prefs.editorPane, "Editor");
    Prefs.prefTabPane.addTab("Sounds", null, Prefs.soundPane, "Sounds");
    Prefs.prefTabPane.addTab("Music", null, Prefs.musicPane, "Music");
    Prefs.prefTabPane.addTab("Misc.", null, Prefs.miscPane, "Misc.");
    Prefs.prefTabPane.addTab("View", null, viewPane, "View");
    Prefs.mainPrefPane.add(Prefs.prefTabPane, BorderLayout.CENTER);
    Prefs.mainPrefPane.add(Prefs.buttonPane, BorderLayout.SOUTH);
    Prefs.sounds[Prefs.SOUNDS_ALL].addItemListener(Prefs.handler);
    Prefs.music[Prefs.MUSIC_ALL].addItemListener(Prefs.handler);
    Prefs.prefsOK.addActionListener(Prefs.handler);
    Prefs.prefsCancel.addActionListener(Prefs.handler);
    Prefs.prefsExport.addActionListener(Prefs.handler);
    Prefs.prefsImport.addActionListener(Prefs.handler);
  }

  private static class PreferencesFileManager {
    // Constructors
    public PreferencesFileManager() {
      // Do nothing
    }

    // Methods
    public void deletePreferencesFile() {
      // Delete preferences file
      final File prefs = PreferencesFileManager.getPrefsFile();
      prefs.delete();
    }

    public boolean readPreferencesFile() {
      try (final BufferedReader s = new BufferedReader(
          new FileReader(PreferencesFileManager.getPrefsFile()))) {
        // Read the preferences from the file
        // Read major version
        final int majorVersion = Short.parseShort(s.readLine());
        // Read minor version
        final int minorVersion = Short.parseShort(s.readLine());
        // Version check
        if (majorVersion == Prefs.PREFS_VERSION_MAJOR) {
          if (minorVersion > Prefs.PREFS_VERSION_MINOR) {
            throw new PrefsException(
                "Incompatible preferences minor version, using defaults.");
          }
        } else {
          throw new PrefsException(
              "Incompatible preferences major version, using defaults.");
        }
        Prefs.editorFill = Integer.parseInt(s.readLine());
        Prefs.checkUpdatesStartupEnabled = Boolean.parseBoolean(s.readLine());
        Prefs.moveOneAtATimeEnabled = Boolean.parseBoolean(s.readLine());
        for (int x = 0; x < Prefs.SOUNDS_LENGTH; x++) {
          Prefs.soundsEnabled[x] = Boolean.parseBoolean(s.readLine());
        }
        Prefs.updateCheckIntervalIndex = Integer.parseInt(s.readLine());
        Prefs.lastDirOpen = s.readLine();
        Prefs.lastDirSave = s.readLine();
        Prefs.lastFilterUsed = Integer.parseInt(s.readLine());
        Prefs.difficultySetting = Integer.parseInt(s.readLine());
        Prefs.useMapBattleEngine = Boolean.parseBoolean(s.readLine());
        Prefs.useTimeBattleEngine = Boolean.parseBoolean(s.readLine());
        Prefs.viewingWindowIndex = Integer.parseInt(s.readLine());
        for (int x = 0; x < Prefs.MUSIC_LENGTH; x++) {
          Prefs.musicEnabled[x] = Boolean.parseBoolean(s.readLine());
        }
        Prefs.loadPrefs();
        return true;
      } catch (final PrefsException pe) {
        CommonDialogs.showDialog(pe.getMessage());
        return false;
      } catch (final Exception e) {
        FantastleReboot.logWarningWithMessage(e,
            "An error occurred while attempting to read the preferences file. Using defaults.");
        return false;
      }
    }

    public void writePreferencesFile() {
      // Create the needed subdirectories, if they don't already exist
      final File prefsFile = PreferencesFileManager.getPrefsFile();
      final File prefsParent = new File(
          PreferencesFileManager.getPrefsFile().getParent());
      if (!prefsFile.canWrite()) {
        prefsParent.mkdirs();
      }
      try (final BufferedWriter s = new BufferedWriter(
          new FileWriter(prefsFile))) {
        // Write the preferences to the file
        s.write(Integer.toString(Prefs.PREFS_VERSION_MAJOR) + "\n");
        s.write(Integer.toString(Prefs.PREFS_VERSION_MINOR) + "\n");
        s.write(Integer.toString(Prefs.editorFill) + "\n");
        s.write(Boolean.toString(Prefs.checkUpdatesStartupEnabled) + "\n");
        s.write(Boolean.toString(Prefs.moveOneAtATimeEnabled) + "\n");
        for (int x = 0; x < Prefs.SOUNDS_LENGTH; x++) {
          s.write(Boolean.toString(Prefs.soundsEnabled[x]) + "\n");
        }
        s.write(Integer.toString(Prefs.updateCheckIntervalIndex) + "\n");
        s.write(Prefs.lastDirOpen + "\n");
        s.write(Prefs.lastDirSave + "\n");
        s.write(Integer.toString(Prefs.lastFilterUsed) + "\n");
        s.write(Integer.toString(Prefs.difficultySetting) + "\n");
        s.write(Boolean.toString(Prefs.useMapBattleEngine) + "\n");
        s.write(Boolean.toString(Prefs.useTimeBattleEngine) + "\n");
        s.write(Integer.toString(Prefs.viewingWindowIndex) + "\n");
        for (int x = 0; x < Prefs.MUSIC_LENGTH; x++) {
          s.write(Boolean.toString(Prefs.musicEnabled[x]) + "\n");
        }
        s.close();
      } catch (final IOException ie) {
        FantastleReboot.logWarning(ie);
      }
    }

    private static File getPrefsFile() {
      return CommonPaths.getPrefsFile();
    }
  }

  private static class ExportImportManager {
    // Constructors
    public ExportImportManager() {
      // Do nothing
    }

    // Methods
    public boolean importPreferencesFile(final File importFile) {
      try (final BufferedReader s = new BufferedReader(
          new FileReader(importFile))) {
        // Read the preferences from the file
        // Read and discard major version
        s.readLine();
        // Read and discard minor version
        s.readLine();
        Prefs.editorFill = Integer.parseInt(s.readLine());
        Prefs.checkUpdatesStartupEnabled = Boolean.parseBoolean(s.readLine());
        Prefs.moveOneAtATime.setSelected(Boolean.parseBoolean(s.readLine()));
        for (int x = 0; x < Prefs.SOUNDS_LENGTH; x++) {
          Prefs.soundsEnabled[x] = Boolean.parseBoolean(s.readLine());
        }
        Prefs.updateCheckIntervalIndex = Integer.parseInt(s.readLine());
        Prefs.lastFilterUsed = Integer.parseInt(s.readLine());
        Prefs.difficultySetting = Integer.parseInt(s.readLine());
        Prefs.mapBattleEngine.setSelected(Boolean.parseBoolean(s.readLine()));
        Prefs.timeBattleEngine.setSelected(Boolean.parseBoolean(s.readLine()));
        Prefs.viewingWindowIndex = Integer.parseInt(s.readLine());
        for (int x = 0; x < Prefs.MUSIC_LENGTH; x++) {
          Prefs.musicEnabled[x] = Boolean.parseBoolean(s.readLine());
        }
        Prefs.loadPrefs();
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
        s.write(Integer.toString(Prefs.PREFS_VERSION_MAJOR) + "\n");
        s.write(Integer.toString(Prefs.PREFS_VERSION_MINOR) + "\n");
        s.write(Integer.toString(Prefs.editorFill) + "\n");
        s.write(Boolean.toString(Prefs.checkUpdatesStartupEnabled) + "\n");
        s.write(Boolean.toString(Prefs.moveOneAtATimeEnabled) + "\n");
        for (int x = 0; x < Prefs.SOUNDS_LENGTH; x++) {
          s.write(Boolean.toString(Prefs.soundsEnabled[x]) + "\n");
        }
        s.write(Integer.toString(Prefs.updateCheckIntervalIndex) + "\n");
        s.write(Integer.toString(Prefs.lastFilterUsed) + "\n");
        s.write(Integer.toString(Prefs.difficultySetting) + "\n");
        s.write(Boolean.toString(Prefs.useMapBattleEngine) + "\n");
        s.write(Boolean.toString(Prefs.useTimeBattleEngine) + "\n");
        s.write(Integer.toString(Prefs.viewingWindowIndex) + "\n");
        for (int x = 0; x < Prefs.MUSIC_LENGTH; x++) {
          s.write(Boolean.toString(Prefs.musicEnabled[x]) + "\n");
        }
        return true;
      } catch (final IOException ie) {
        FantastleReboot.logWarning(ie);
        return false;
      }
    }

    public File getImportSource() {
      final FileDialog chooser = new FileDialog((java.awt.Frame) null, "Import",
          FileDialog.LOAD);
      chooser.setVisible(true);
      return new File(chooser.getDirectory() + chooser.getFile());
    }

    public File getExportDestination() {
      final FileDialog chooser = new FileDialog((java.awt.Frame) null, "Export",
          FileDialog.SAVE);
      chooser.setVisible(true);
      return new File(chooser.getDirectory() + chooser.getFile());
    }
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
          Prefs.savePrefs();
          Prefs.hidePrefs();
        } else if (cmd.equals("Cancel")) {
          Prefs.loadPrefs();
          Prefs.hidePrefs();
        } else if (cmd.equals("Export...")) {
          Prefs.handleExport();
        } else if (cmd.equals("Import...")) {
          Prefs.handleImport();
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
      try {
        final Object o = e.getItem();
        if (o.getClass().equals(Prefs.sounds[Prefs.SOUNDS_ALL].getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(Prefs.sounds[Prefs.SOUNDS_ALL])) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              for (int x = 1; x < Prefs.SOUNDS_LENGTH; x++) {
                Prefs.sounds[x].setEnabled(true);
              }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              for (int x = 1; x < Prefs.SOUNDS_LENGTH; x++) {
                Prefs.sounds[x].setEnabled(false);
              }
            }
          }
        } else if (o.getClass()
            .equals(Prefs.music[Prefs.MUSIC_ALL].getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(Prefs.music[Prefs.MUSIC_ALL])) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              for (int x = 1; x < Prefs.MUSIC_LENGTH; x++) {
                Prefs.music[x].setEnabled(true);
              }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              for (int x = 1; x < Prefs.MUSIC_LENGTH; x++) {
                Prefs.music[x].setEnabled(false);
              }
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
        Prefs.hidePrefs();
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
