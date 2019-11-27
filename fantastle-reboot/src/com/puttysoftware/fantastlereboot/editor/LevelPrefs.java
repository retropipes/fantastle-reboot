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
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.MusicGroup;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.files.CommonPaths;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objects.Tile;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class LevelPrefs {
  // Fields
  private static MainWindow prefFrame;
  private static JTabbedPane prefTabPane;
  private static JPanel mainPrefPane, buttonPane, miscPane, soundPane,
      musicPane;
  private static JPanel editorPane;
  private static JButton prefsOK, prefsCancel;
  private static JCheckBox[] sounds = new JCheckBox[LevelPrefs.SOUNDS_LENGTH];
  private static JCheckBox[] music = new JCheckBox[LevelPrefs.MUSIC_LENGTH];
  private static JCheckBox checkUpdatesStartup;
  private static JCheckBox moveOneAtATime;
  private static JComboBox<String> editorFillChoices;
  private static String[] editorFillChoiceArray;
  private static JComboBox<String> difficultyChoices;
  private static JComboBox<String> updateCheckInterval;
  private static String[] updateCheckIntervalValues;
  private static JComboBox<String> viewingWindowChoices;
  private static JComboBox<String> editorWindowChoices;
  private static JSlider minRandomRoomSizeX;
  private static JSlider maxRandomRoomSizeX;
  private static JSlider minRandomRoomSizeY;
  private static JSlider maxRandomRoomSizeY;
  private static EventHandler handler;
  private static LevelPrefsStorageManager fileMgr = new LevelPrefsStorageManager();
  private static int editorFill;
  private static boolean checkUpdatesStartupEnabled;
  private static boolean moveOneAtATimeEnabled;
  private static int difficultySetting = LevelPrefs.DEFAULT_DIFFICULTY;
  private static int viewingWindowIndex;
  private static int editorWindowIndex;
  private static int updateCheckIntervalIndex;
  private static int minRandomRoomSizeXIndex;
  private static int maxRandomRoomSizeXIndex;
  private static int minRandomRoomSizeYIndex;
  private static int maxRandomRoomSizeYIndex;
  private static boolean[] soundsEnabled = new boolean[LevelPrefs.SOUNDS_LENGTH];
  private static boolean[] musicEnabled = new boolean[LevelPrefs.MUSIC_LENGTH];
  private static String lastDirOpen;
  private static String lastDirSave;
  private static int lastFilterUsed;
  private static boolean guiSetUp = false;
  private static final int[] VIEWING_WINDOW_SIZES = new int[] { 7, 9, 11, 13,
      15, 17, 19, 21, 23, 25 };
  private static final int MIN_ROOM_SIZE = 3;
  private static final int DEFAULT_ROOM_SIZE = 9;
  private static final int MAX_ROOM_SIZE = 15;
  private static final int[] MDM_MIN_TOP = new int[] { 5, 4, 3, 2, 1 };
  private static final int[] MDM_MIN_BOT = new int[] { 5, 5, 5, 5, 5 };
  private static final int[] MDM_MAX_TOP = new int[] { 5, 5, 5, 5, 5 };
  private static final int[] MDM_MAX_BOT = new int[] { 5, 4, 3, 2, 1 };
  private static final int DEFAULT_GAME_VIEW_SIZE_INDEX = 2;
  private static final int DEFAULT_EDITOR_VIEW_SIZE_INDEX = 2;
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
  private static final int DEFAULT_DIFFICULTY = LevelPrefs.DIFFICULTY_NORMAL;
  private static final int BATTLE_SPEED = 1000;
  private static final int MUSIC_LENGTH = 5;
  private static final int SOUNDS_LENGTH = 5;
  private static final int GRID_LENGTH = 8;
  private static final int PREFS_VERSION_MAJOR = 0;
  private static final int PREFS_VERSION_MINOR = 0;
  private static final String DOC_TAG = "settings";

  // Constructors
  private LevelPrefs() {
    super();
  }

  // Methods
  public static int getMonsterCount(final int pmCount) {
    final int diff = LevelPrefs.getGameDifficulty();
    int min = pmCount * LevelPrefs.MDM_MIN_TOP[diff]
        / LevelPrefs.MDM_MIN_BOT[diff];
    if (min < 1) {
      min = 1;
    }
    int max = pmCount * LevelPrefs.MDM_MAX_TOP[diff]
        / LevelPrefs.MDM_MAX_BOT[diff];
    if (max < 1) {
      max = 1;
    }
    return RandomRange.generate(min, max);
  }

  public static int getMinimumRandomRoomSizeWide() {
    return LevelPrefs.minRandomRoomSizeXIndex;
  }

  public static int getMaximumRandomRoomSizeWide() {
    return LevelPrefs.maxRandomRoomSizeXIndex;
  }

  public static int getMinimumRandomRoomSizeTall() {
    return LevelPrefs.minRandomRoomSizeYIndex;
  }

  public static int getMaximumRandomRoomSizeTall() {
    return LevelPrefs.maxRandomRoomSizeYIndex;
  }

  public static int getBattleSpeed() {
    return LevelPrefs.BATTLE_SPEED;
  }

  public static int getGameDifficulty() {
    return LevelPrefs.difficultySetting;
  }

  public static String getLastDirOpen() {
    return LevelPrefs.lastDirOpen;
  }

  public static void setLastDirOpen(final String value) {
    LevelPrefs.lastDirOpen = value;
  }

  public static String getLastDirSave() {
    return LevelPrefs.lastDirSave;
  }

  public static void setLastDirSave(final String value) {
    LevelPrefs.lastDirSave = value;
  }

  public static int getLastFilterUsedIndex() {
    return LevelPrefs.lastFilterUsed;
  }

  public static void setLastFilterUsedIndex(final int value) {
    LevelPrefs.lastFilterUsed = value;
  }

  public static boolean shouldCheckUpdatesAtStartup() {
    return LevelPrefs.checkUpdatesStartupEnabled;
  }

  public static boolean oneMove() {
    return LevelPrefs.moveOneAtATimeEnabled;
  }

  public static int getViewingWindowSize() {
    return LevelPrefs.VIEWING_WINDOW_SIZES[LevelPrefs
        .getViewingWindowSizeIndex()];
  }

  public static int getViewingWindowSizeIndex() {
    return LevelPrefs.viewingWindowIndex;
  }

  public static int getEditorWindowSize() {
    return LevelPrefs.VIEWING_WINDOW_SIZES[LevelPrefs
        .getEditorWindowSizeIndex()];
  }

  public static int getEditorWindowSizeIndex() {
    return LevelPrefs.editorWindowIndex;
  }

  public static boolean isSoundGroupEnabled(final SoundGroup group) {
    return LevelPrefs.isSoundGroupEnabledImpl(group.ordinal());
  }

  private static boolean isSoundGroupEnabledImpl(final int snd) {
    if (!LevelPrefs.soundsEnabled[LevelPrefs.SOUNDS_ALL]) {
      return false;
    } else {
      return LevelPrefs.soundsEnabled[snd];
    }
  }

  public static boolean isMusicGroupEnabled(final MusicGroup group) {
    return LevelPrefs.isMusicGroupEnabledImpl(group.ordinal());
  }

  private static boolean isMusicGroupEnabledImpl(final int mus) {
    if (!LevelPrefs.musicEnabled[LevelPrefs.MUSIC_ALL]) {
      return false;
    } else {
      return LevelPrefs.musicEnabled[mus];
    }
  }

  public static FantastleObjectModel getEditorDefaultFill() {
    return new Tile();
  }

  private static void defaultEnableSoundGroups() {
    for (int x = 0; x < LevelPrefs.SOUNDS_LENGTH; x++) {
      LevelPrefs.soundsEnabled[x] = true;
    }
  }

  private static void defaultEnableMusicGroups() {
    for (int x = 0; x < LevelPrefs.MUSIC_LENGTH; x++) {
      LevelPrefs.musicEnabled[x] = true;
    }
  }

  private static void setSoundGroupEnabledImpl(final int snd,
      final boolean status) {
    LevelPrefs.soundsEnabled[snd] = status;
  }

  private static void setMusicGroupEnabled(final int mus,
      final boolean status) {
    LevelPrefs.musicEnabled[mus] = status;
  }

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
    app.setInLevelPrefs();
    app.getMenuManager().setPrefMenus();
  }

  private static void hidePrefs() {
    if (!LevelPrefs.guiSetUp) {
      LevelPrefs.setUpGUI();
      LevelPrefs.guiSetUp = true;
    }
    LevelPrefs.prefFrame.setDefaultButton(null);
    LevelPrefs.prefFrame.removeWindowListener(LevelPrefs.handler);
    LevelPrefs.fileMgr.writeToMaze();
    FantastleReboot.getBagOStuff().restoreFormerMode();
  }

  public static void writePrefs() {
    LevelPrefs.fileMgr.writeToMaze();
  }

  public static void resetPrefs() {
    LevelPrefs.lastDirOpen = null;
    LevelPrefs.lastDirSave = null;
    LevelPrefs.resetDefaultPrefs();
  }

  private static void loadPrefs() {
    if (!LevelPrefs.guiSetUp) {
      LevelPrefs.setUpGUI();
      LevelPrefs.guiSetUp = true;
    }
    LevelPrefs.editorFillChoices.setSelectedIndex(LevelPrefs.editorFill);
    for (int x = 0; x < LevelPrefs.SOUNDS_LENGTH; x++) {
      LevelPrefs.sounds[x].setSelected(LevelPrefs.isSoundGroupEnabledImpl(x));
    }
    for (int x = 0; x < LevelPrefs.MUSIC_LENGTH; x++) {
      LevelPrefs.music[x].setSelected(LevelPrefs.isMusicGroupEnabledImpl(x));
    }
    LevelPrefs.updateCheckInterval
        .setSelectedIndex(LevelPrefs.updateCheckIntervalIndex);
    LevelPrefs.checkUpdatesStartup
        .setSelected(LevelPrefs.checkUpdatesStartupEnabled);
    LevelPrefs.difficultyChoices.setSelectedIndex(LevelPrefs.difficultySetting);
    LevelPrefs.moveOneAtATime.setSelected(LevelPrefs.moveOneAtATimeEnabled);
    LevelPrefs.viewingWindowChoices
        .setSelectedIndex(LevelPrefs.viewingWindowIndex);
    LevelPrefs.editorWindowChoices
        .setSelectedIndex(LevelPrefs.editorWindowIndex);
    LevelPrefs.minRandomRoomSizeX.setValue(LevelPrefs.minRandomRoomSizeXIndex);
    LevelPrefs.maxRandomRoomSizeX.setValue(LevelPrefs.maxRandomRoomSizeXIndex);
    LevelPrefs.minRandomRoomSizeY.setValue(LevelPrefs.minRandomRoomSizeYIndex);
    LevelPrefs.maxRandomRoomSizeY.setValue(LevelPrefs.maxRandomRoomSizeYIndex);
  }

  private static void savePrefs() {
    if (!LevelPrefs.guiSetUp) {
      LevelPrefs.setUpGUI();
      LevelPrefs.guiSetUp = true;
    }
    LevelPrefs.editorFill = LevelPrefs.editorFillChoices.getSelectedIndex();
    for (int x = 0; x < LevelPrefs.SOUNDS_LENGTH; x++) {
      LevelPrefs.setSoundGroupEnabledImpl(x, LevelPrefs.sounds[x].isSelected());
    }
    for (int x = 0; x < LevelPrefs.MUSIC_LENGTH; x++) {
      LevelPrefs.setMusicGroupEnabled(x, LevelPrefs.music[x].isSelected());
    }
    LevelPrefs.updateCheckIntervalIndex = LevelPrefs.updateCheckInterval
        .getSelectedIndex();
    LevelPrefs.checkUpdatesStartupEnabled = LevelPrefs.checkUpdatesStartup
        .isSelected();
    LevelPrefs.difficultySetting = LevelPrefs.difficultyChoices
        .getSelectedIndex();
    LevelPrefs.moveOneAtATimeEnabled = LevelPrefs.moveOneAtATime.isSelected();
    LevelPrefs.viewingWindowIndex = LevelPrefs.viewingWindowChoices
        .getSelectedIndex();
    LevelPrefs.editorWindowIndex = LevelPrefs.editorWindowChoices
        .getSelectedIndex();
    LevelPrefs.minRandomRoomSizeXIndex = LevelPrefs.minRandomRoomSizeX
        .getValue();
    LevelPrefs.maxRandomRoomSizeXIndex = LevelPrefs.maxRandomRoomSizeX
        .getValue();
    LevelPrefs.minRandomRoomSizeYIndex = LevelPrefs.minRandomRoomSizeY
        .getValue();
    LevelPrefs.maxRandomRoomSizeYIndex = LevelPrefs.maxRandomRoomSizeY
        .getValue();
  }

  public static void setDefaultPrefs() {
    if (!LevelPrefs.fileMgr.readFromMaze()) {
      LevelPrefs.resetDefaultPrefs();
    }
  }

  private static void resetDefaultPrefs() {
    if (!LevelPrefs.guiSetUp) {
      LevelPrefs.setUpGUI();
      LevelPrefs.guiSetUp = true;
    }
    LevelPrefs.editorFill = 0;
    LevelPrefs.defaultEnableSoundGroups();
    LevelPrefs.defaultEnableMusicGroups();
    LevelPrefs.checkUpdatesStartup.setSelected(true);
    LevelPrefs.checkUpdatesStartupEnabled = true;
    LevelPrefs.difficultySetting = LevelPrefs.DEFAULT_DIFFICULTY;
    LevelPrefs.difficultyChoices.setSelectedIndex(LevelPrefs.difficultySetting);
    LevelPrefs.moveOneAtATime.setSelected(true);
    LevelPrefs.moveOneAtATimeEnabled = true;
    LevelPrefs.viewingWindowIndex = LevelPrefs.DEFAULT_GAME_VIEW_SIZE_INDEX;
    LevelPrefs.viewingWindowChoices
        .setSelectedIndex(LevelPrefs.viewingWindowIndex);
    LevelPrefs.editorWindowIndex = LevelPrefs.DEFAULT_EDITOR_VIEW_SIZE_INDEX;
    LevelPrefs.editorWindowChoices
        .setSelectedIndex(LevelPrefs.editorWindowIndex);
    LevelPrefs.updateCheckInterval.setSelectedIndex(0);
    LevelPrefs.lastFilterUsed = LevelPrefs.FILTER_MAZE_V5;
    LevelPrefs.minRandomRoomSizeXIndex = LevelPrefs.DEFAULT_ROOM_SIZE;
    LevelPrefs.maxRandomRoomSizeXIndex = LevelPrefs.DEFAULT_ROOM_SIZE;
    LevelPrefs.minRandomRoomSizeYIndex = LevelPrefs.DEFAULT_ROOM_SIZE;
    LevelPrefs.maxRandomRoomSizeYIndex = LevelPrefs.DEFAULT_ROOM_SIZE;
    LevelPrefs.minRandomRoomSizeX.setValue(LevelPrefs.DEFAULT_ROOM_SIZE);
    LevelPrefs.maxRandomRoomSizeX.setValue(LevelPrefs.DEFAULT_ROOM_SIZE);
    LevelPrefs.minRandomRoomSizeY.setValue(LevelPrefs.DEFAULT_ROOM_SIZE);
    LevelPrefs.maxRandomRoomSizeY.setValue(LevelPrefs.DEFAULT_ROOM_SIZE);
  }

  private static void setUpGUI() {
    LevelPrefs.handler = new EventHandler();
    LevelPrefs.prefTabPane = new JTabbedPane();
    LevelPrefs.mainPrefPane = new JPanel();
    LevelPrefs.editorPane = new JPanel();
    LevelPrefs.soundPane = new JPanel();
    LevelPrefs.musicPane = new JPanel();
    LevelPrefs.miscPane = new JPanel();
    LevelPrefs.prefTabPane.setOpaque(true);
    LevelPrefs.buttonPane = new JPanel();
    LevelPrefs.prefsOK = new JButton("OK");
    LevelPrefs.prefsOK.setDefaultCapable(true);
    LevelPrefs.prefsCancel = new JButton("Cancel");
    LevelPrefs.prefsCancel.setDefaultCapable(false);
    LevelPrefs.editorFillChoiceArray = new String[] { "Grass", "Dirt", "Sand",
        "Snow", "Tile", "Tundra" };
    LevelPrefs.editorFillChoices = new JComboBox<>(
        LevelPrefs.editorFillChoiceArray);
    LevelPrefs.sounds[LevelPrefs.SOUNDS_ALL] = new JCheckBox(
        "Enable ALL sounds", true);
    LevelPrefs.sounds[LevelPrefs.SOUNDS_UI] = new JCheckBox(
        "Enable user interface sounds", true);
    LevelPrefs.sounds[LevelPrefs.SOUNDS_GAME] = new JCheckBox(
        "Enable game sounds", true);
    LevelPrefs.sounds[LevelPrefs.SOUNDS_BATTLE] = new JCheckBox(
        "Enable battle sounds", true);
    LevelPrefs.sounds[LevelPrefs.SOUNDS_SHOP] = new JCheckBox(
        "Enable shop sounds", true);
    LevelPrefs.music[LevelPrefs.MUSIC_ALL] = new JCheckBox("Enable ALL music",
        true);
    LevelPrefs.music[LevelPrefs.MUSIC_UI] = new JCheckBox(
        "Enable user interface music", true);
    LevelPrefs.music[LevelPrefs.MUSIC_GAME] = new JCheckBox("Enable game music",
        true);
    LevelPrefs.music[LevelPrefs.MUSIC_BATTLE] = new JCheckBox(
        "Enable battle music", true);
    LevelPrefs.music[LevelPrefs.MUSIC_SHOP] = new JCheckBox("Enable shop music",
        true);
    LevelPrefs.checkUpdatesStartup = new JCheckBox(
        "Check for Updates at Startup", true);
    LevelPrefs.moveOneAtATime = new JCheckBox("One Move at a Time", true);
    LevelPrefs.updateCheckIntervalValues = new String[] { "Daily",
        "Every 2nd Day", "Weekly", "Every 2nd Week", "Monthly" };
    LevelPrefs.updateCheckInterval = new JComboBox<>(
        LevelPrefs.updateCheckIntervalValues);
    LevelPrefs.difficultyChoices = new JComboBox<>(
        LevelPrefs.DIFFICULTY_CHOICE_NAMES);
    LevelPrefs.minRandomRoomSizeX = new JSlider(LevelPrefs.MIN_ROOM_SIZE,
        LevelPrefs.MAX_ROOM_SIZE);
    LevelPrefs.minRandomRoomSizeX
        .setLabelTable(LevelPrefs.minRandomRoomSizeX.createStandardLabels(1));
    LevelPrefs.minRandomRoomSizeX.setPaintLabels(true);
    LevelPrefs.maxRandomRoomSizeX = new JSlider(LevelPrefs.MIN_ROOM_SIZE,
        LevelPrefs.MAX_ROOM_SIZE);
    LevelPrefs.maxRandomRoomSizeX
        .setLabelTable(LevelPrefs.maxRandomRoomSizeX.createStandardLabels(1));
    LevelPrefs.maxRandomRoomSizeX.setPaintLabels(true);
    LevelPrefs.minRandomRoomSizeY = new JSlider(LevelPrefs.MIN_ROOM_SIZE,
        LevelPrefs.MAX_ROOM_SIZE);
    LevelPrefs.minRandomRoomSizeY
        .setLabelTable(LevelPrefs.minRandomRoomSizeY.createStandardLabels(1));
    LevelPrefs.minRandomRoomSizeY.setPaintLabels(true);
    LevelPrefs.maxRandomRoomSizeY = new JSlider(LevelPrefs.MIN_ROOM_SIZE,
        LevelPrefs.MAX_ROOM_SIZE);
    LevelPrefs.maxRandomRoomSizeY
        .setLabelTable(LevelPrefs.maxRandomRoomSizeY.createStandardLabels(1));
    LevelPrefs.maxRandomRoomSizeY.setPaintLabels(true);
    LevelPrefs.mainPrefPane.setLayout(new BorderLayout());
    LevelPrefs.editorPane.setLayout(new GridLayout(LevelPrefs.GRID_LENGTH, 1));
    LevelPrefs.editorPane.add(new JLabel("Default fill for new mazes:"));
    LevelPrefs.editorPane.add(LevelPrefs.editorFillChoices);
    LevelPrefs.editorPane.add(new JLabel("Editor Window Size"));
    LevelPrefs.editorWindowChoices = new JComboBox<>(
        LevelPrefs.VIEWING_WINDOW_SIZE_NAMES);
    LevelPrefs.editorPane.add(LevelPrefs.editorWindowChoices);
    LevelPrefs.soundPane.setLayout(new GridLayout(LevelPrefs.GRID_LENGTH, 1));
    for (int x = 0; x < LevelPrefs.SOUNDS_LENGTH; x++) {
      LevelPrefs.soundPane.add(LevelPrefs.sounds[x]);
    }
    LevelPrefs.musicPane.setLayout(new GridLayout(LevelPrefs.GRID_LENGTH, 1));
    for (int x = 0; x < LevelPrefs.MUSIC_LENGTH; x++) {
      LevelPrefs.musicPane.add(LevelPrefs.music[x]);
    }
    LevelPrefs.miscPane.setLayout(new GridLayout(LevelPrefs.GRID_LENGTH, 1));
    LevelPrefs.miscPane.add(LevelPrefs.checkUpdatesStartup);
    LevelPrefs.miscPane.add(LevelPrefs.moveOneAtATime);
    LevelPrefs.miscPane.add(new JLabel("Check How Often For Updates"));
    LevelPrefs.miscPane.add(LevelPrefs.updateCheckInterval);
    LevelPrefs.miscPane.add(new JLabel("Game Difficulty"));
    LevelPrefs.miscPane.add(LevelPrefs.difficultyChoices);
    final JPanel viewPane = new JPanel();
    viewPane.setLayout(new GridLayout(LevelPrefs.GRID_LENGTH, 1));
    viewPane.add(new JLabel("Viewing Window Size"));
    LevelPrefs.viewingWindowChoices = new JComboBox<>(
        LevelPrefs.VIEWING_WINDOW_SIZE_NAMES);
    viewPane.add(LevelPrefs.viewingWindowChoices);
    final JPanel generatorPane = new JPanel();
    generatorPane.setLayout(new GridLayout(LevelPrefs.GRID_LENGTH, 1));
    generatorPane
        .add(new JLabel("Minimum Room Size for Maze Generator (Wide)"));
    generatorPane.add(LevelPrefs.minRandomRoomSizeX);
    generatorPane
        .add(new JLabel("Maximum Room Size for Maze Generator (Wide)"));
    generatorPane.add(LevelPrefs.maxRandomRoomSizeX);
    generatorPane
        .add(new JLabel("Minimum Room Size for Maze Generator (Tall)"));
    generatorPane.add(LevelPrefs.minRandomRoomSizeY);
    generatorPane
        .add(new JLabel("Maximum Room Size for Maze Generator (Tall)"));
    generatorPane.add(LevelPrefs.maxRandomRoomSizeY);
    LevelPrefs.buttonPane.setLayout(new FlowLayout());
    LevelPrefs.buttonPane.add(LevelPrefs.prefsOK);
    LevelPrefs.buttonPane.add(LevelPrefs.prefsCancel);
    LevelPrefs.prefTabPane.addTab("Editor", null, LevelPrefs.editorPane,
        "Editor");
    LevelPrefs.prefTabPane.addTab("Generator", null, generatorPane,
        "Generator");
    LevelPrefs.prefTabPane.addTab("Sounds", null, LevelPrefs.soundPane,
        "Sounds");
    LevelPrefs.prefTabPane.addTab("Music", null, LevelPrefs.musicPane, "Music");
    LevelPrefs.prefTabPane.addTab("Misc.", null, LevelPrefs.miscPane, "Misc.");
    LevelPrefs.prefTabPane.addTab("View", null, viewPane, "View");
    LevelPrefs.mainPrefPane.add(LevelPrefs.prefTabPane, BorderLayout.CENTER);
    LevelPrefs.mainPrefPane.add(LevelPrefs.buttonPane, BorderLayout.SOUTH);
    LevelPrefs.sounds[LevelPrefs.SOUNDS_ALL]
        .addItemListener(LevelPrefs.handler);
    LevelPrefs.music[LevelPrefs.MUSIC_ALL].addItemListener(LevelPrefs.handler);
    LevelPrefs.prefsOK.addActionListener(LevelPrefs.handler);
    LevelPrefs.prefsCancel.addActionListener(LevelPrefs.handler);
  }

  private static class LevelPrefsStorageManager {
    // Constructors
    public LevelPrefsStorageManager() {
      // Do nothing
    }

    public boolean readFromMaze() {
      try (final XDataReader reader = new XDataReader(
          CommonPaths.getPrefsFile().getAbsolutePath(), LevelPrefs.DOC_TAG)) {
        // Read the preferences from the file
        // Read major version
        reader.readInt();
        // Read minor version
        reader.readInt();
        LevelPrefs.editorFill = reader.readInt();
        LevelPrefs.checkUpdatesStartupEnabled = reader.readBoolean();
        LevelPrefs.moveOneAtATimeEnabled = reader.readBoolean();
        for (int x = 0; x < LevelPrefs.SOUNDS_LENGTH; x++) {
          LevelPrefs.soundsEnabled[x] = reader.readBoolean();
        }
        LevelPrefs.updateCheckIntervalIndex = reader.readInt();
        LevelPrefs.lastDirOpen = reader.readString();
        LevelPrefs.lastDirSave = reader.readString();
        LevelPrefs.lastFilterUsed = reader.readInt();
        LevelPrefs.difficultySetting = reader.readInt();
        LevelPrefs.viewingWindowIndex = reader.readInt();
        for (int x = 0; x < LevelPrefs.MUSIC_LENGTH; x++) {
          LevelPrefs.musicEnabled[x] = reader.readBoolean();
        }
        LevelPrefs.editorWindowIndex = reader.readInt();
        LevelPrefs.minRandomRoomSizeXIndex = reader.readInt();
        LevelPrefs.maxRandomRoomSizeXIndex = reader.readInt();
        LevelPrefs.minRandomRoomSizeYIndex = reader.readInt();
        LevelPrefs.maxRandomRoomSizeYIndex = reader.readInt();
        LevelPrefs.loadPrefs();
        return true;
      } catch (final Exception e) {
        FantastleReboot.logWarningWithMessage(e,
            "An error occurred while attempting to read the preferences file. Using defaults.");
        return false;
      }
    }

    public void writeToMaze() {
      final File prefsFile = CommonPaths.getPrefsFile();
      try (final XDataWriter writer = new XDataWriter(
          prefsFile.getAbsolutePath(), LevelPrefs.DOC_TAG)) {
        // Write the preferences to the file
        writer.writeInt(LevelPrefs.PREFS_VERSION_MAJOR);
        writer.writeInt(LevelPrefs.PREFS_VERSION_MINOR);
        writer.writeInt(LevelPrefs.editorFill);
        writer.writeBoolean(LevelPrefs.checkUpdatesStartupEnabled);
        writer.writeBoolean(LevelPrefs.moveOneAtATimeEnabled);
        for (int x = 0; x < LevelPrefs.SOUNDS_LENGTH; x++) {
          writer.writeBoolean(LevelPrefs.soundsEnabled[x]);
        }
        writer.writeInt(LevelPrefs.updateCheckIntervalIndex);
        writer.writeString(LevelPrefs.lastDirOpen);
        writer.writeString(LevelPrefs.lastDirSave);
        writer.writeInt(LevelPrefs.lastFilterUsed);
        writer.writeInt(LevelPrefs.difficultySetting);
        writer.writeInt(LevelPrefs.viewingWindowIndex);
        for (int x = 0; x < LevelPrefs.MUSIC_LENGTH; x++) {
          writer.writeBoolean(LevelPrefs.musicEnabled[x]);
        }
        writer.writeInt(LevelPrefs.editorWindowIndex);
        writer.writeInt(LevelPrefs.minRandomRoomSizeXIndex);
        writer.writeInt(LevelPrefs.maxRandomRoomSizeXIndex);
        writer.writeInt(LevelPrefs.minRandomRoomSizeYIndex);
        writer.writeInt(LevelPrefs.maxRandomRoomSizeYIndex);
      } catch (final IOException ie) {
        FantastleReboot.logWarning(ie);
      }
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
        if (o.getClass()
            .equals(LevelPrefs.sounds[LevelPrefs.SOUNDS_ALL].getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(LevelPrefs.sounds[LevelPrefs.SOUNDS_ALL])) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              for (int x = 1; x < LevelPrefs.SOUNDS_LENGTH; x++) {
                LevelPrefs.sounds[x].setEnabled(true);
              }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              for (int x = 1; x < LevelPrefs.SOUNDS_LENGTH; x++) {
                LevelPrefs.sounds[x].setEnabled(false);
              }
            }
          }
        } else if (o.getClass()
            .equals(LevelPrefs.music[LevelPrefs.MUSIC_ALL].getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(LevelPrefs.music[LevelPrefs.MUSIC_ALL])) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              for (int x = 1; x < LevelPrefs.MUSIC_LENGTH; x++) {
                LevelPrefs.music[x].setEnabled(true);
              }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              for (int x = 1; x < LevelPrefs.MUSIC_LENGTH; x++) {
                LevelPrefs.music[x].setEnabled(false);
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
