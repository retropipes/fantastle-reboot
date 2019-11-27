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

public class MazePrefs {
  // Fields
  private static MainWindow prefFrame;
  private static JTabbedPane prefTabPane;
  private static JPanel mainPrefPane, buttonPane, miscPane, soundPane,
      musicPane;
  private static JPanel editorPane;
  private static JButton prefsOK, prefsCancel;
  private static JCheckBox[] sounds = new JCheckBox[MazePrefs.SOUNDS_LENGTH];
  private static JCheckBox[] music = new JCheckBox[MazePrefs.MUSIC_LENGTH];
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
  private static MazePrefsStorageManager fileMgr = new MazePrefsStorageManager();
  private static int editorFill;
  private static boolean checkUpdatesStartupEnabled;
  private static boolean moveOneAtATimeEnabled;
  private static int difficultySetting = MazePrefs.DEFAULT_DIFFICULTY;
  private static int viewingWindowIndex;
  private static int editorWindowIndex;
  private static int updateCheckIntervalIndex;
  private static int minRandomRoomSizeXIndex;
  private static int maxRandomRoomSizeXIndex;
  private static int minRandomRoomSizeYIndex;
  private static int maxRandomRoomSizeYIndex;
  private static boolean[] soundsEnabled = new boolean[MazePrefs.SOUNDS_LENGTH];
  private static boolean[] musicEnabled = new boolean[MazePrefs.MUSIC_LENGTH];
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
  private static final int DEFAULT_DIFFICULTY = MazePrefs.DIFFICULTY_NORMAL;
  private static final int BATTLE_SPEED = 1000;
  private static final int MUSIC_LENGTH = 5;
  private static final int SOUNDS_LENGTH = 5;
  private static final int GRID_LENGTH = 8;
  private static final int PREFS_VERSION_MAJOR = 0;
  private static final int PREFS_VERSION_MINOR = 0;
  private static final String DOC_TAG = "settings";

  // Constructors
  private MazePrefs() {
    super();
  }

  // Methods
  public static int getMonsterCount(final int pmCount) {
    final int diff = MazePrefs.getGameDifficulty();
    int min = pmCount * MazePrefs.MDM_MIN_TOP[diff]
        / MazePrefs.MDM_MIN_BOT[diff];
    if (min < 1) {
      min = 1;
    }
    int max = pmCount * MazePrefs.MDM_MAX_TOP[diff]
        / MazePrefs.MDM_MAX_BOT[diff];
    if (max < 1) {
      max = 1;
    }
    return RandomRange.generate(min, max);
  }

  public static int getMinimumRandomRoomSizeWide() {
    return MazePrefs.minRandomRoomSizeXIndex;
  }

  public static int getMaximumRandomRoomSizeWide() {
    return MazePrefs.maxRandomRoomSizeXIndex;
  }

  public static int getMinimumRandomRoomSizeTall() {
    return MazePrefs.minRandomRoomSizeYIndex;
  }

  public static int getMaximumRandomRoomSizeTall() {
    return MazePrefs.maxRandomRoomSizeYIndex;
  }

  public static int getBattleSpeed() {
    return MazePrefs.BATTLE_SPEED;
  }

  public static int getGameDifficulty() {
    return MazePrefs.difficultySetting;
  }

  public static String getLastDirOpen() {
    return MazePrefs.lastDirOpen;
  }

  public static void setLastDirOpen(final String value) {
    MazePrefs.lastDirOpen = value;
  }

  public static String getLastDirSave() {
    return MazePrefs.lastDirSave;
  }

  public static void setLastDirSave(final String value) {
    MazePrefs.lastDirSave = value;
  }

  public static int getLastFilterUsedIndex() {
    return MazePrefs.lastFilterUsed;
  }

  public static void setLastFilterUsedIndex(final int value) {
    MazePrefs.lastFilterUsed = value;
  }

  public static boolean shouldCheckUpdatesAtStartup() {
    return MazePrefs.checkUpdatesStartupEnabled;
  }

  public static boolean oneMove() {
    return MazePrefs.moveOneAtATimeEnabled;
  }

  public static int getViewingWindowSize() {
    return MazePrefs.VIEWING_WINDOW_SIZES[MazePrefs
        .getViewingWindowSizeIndex()];
  }

  public static int getViewingWindowSizeIndex() {
    return MazePrefs.viewingWindowIndex;
  }

  public static int getEditorWindowSize() {
    return MazePrefs.VIEWING_WINDOW_SIZES[MazePrefs
        .getEditorWindowSizeIndex()];
  }

  public static int getEditorWindowSizeIndex() {
    return MazePrefs.editorWindowIndex;
  }

  public static boolean isSoundGroupEnabled(final SoundGroup group) {
    return MazePrefs.isSoundGroupEnabledImpl(group.ordinal());
  }

  private static boolean isSoundGroupEnabledImpl(final int snd) {
    if (!MazePrefs.soundsEnabled[MazePrefs.SOUNDS_ALL]) {
      return false;
    } else {
      return MazePrefs.soundsEnabled[snd];
    }
  }

  public static boolean isMusicGroupEnabled(final MusicGroup group) {
    return MazePrefs.isMusicGroupEnabledImpl(group.ordinal());
  }

  private static boolean isMusicGroupEnabledImpl(final int mus) {
    if (!MazePrefs.musicEnabled[MazePrefs.MUSIC_ALL]) {
      return false;
    } else {
      return MazePrefs.musicEnabled[mus];
    }
  }

  public static FantastleObjectModel getEditorDefaultFill() {
    return new Tile();
  }

  private static void defaultEnableSoundGroups() {
    for (int x = 0; x < MazePrefs.SOUNDS_LENGTH; x++) {
      MazePrefs.soundsEnabled[x] = true;
    }
  }

  private static void defaultEnableMusicGroups() {
    for (int x = 0; x < MazePrefs.MUSIC_LENGTH; x++) {
      MazePrefs.musicEnabled[x] = true;
    }
  }

  private static void setSoundGroupEnabledImpl(final int snd,
      final boolean status) {
    MazePrefs.soundsEnabled[snd] = status;
  }

  private static void setMusicGroupEnabled(final int mus,
      final boolean status) {
    MazePrefs.musicEnabled[mus] = status;
  }

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
    app.setInMazePrefs();
    app.getMenuManager().setPrefMenus();
  }

  private static void hidePrefs() {
    if (!MazePrefs.guiSetUp) {
      MazePrefs.setUpGUI();
      MazePrefs.guiSetUp = true;
    }
    MazePrefs.prefFrame.setDefaultButton(null);
    MazePrefs.prefFrame.removeWindowListener(MazePrefs.handler);
    MazePrefs.fileMgr.writeToMaze();
    FantastleReboot.getBagOStuff().restoreFormerMode();
  }

  public static void writePrefs() {
    MazePrefs.fileMgr.writeToMaze();
  }

  public static void resetPrefs() {
    MazePrefs.lastDirOpen = null;
    MazePrefs.lastDirSave = null;
    MazePrefs.resetDefaultPrefs();
  }

  private static void loadPrefs() {
    if (!MazePrefs.guiSetUp) {
      MazePrefs.setUpGUI();
      MazePrefs.guiSetUp = true;
    }
    MazePrefs.editorFillChoices.setSelectedIndex(MazePrefs.editorFill);
    for (int x = 0; x < MazePrefs.SOUNDS_LENGTH; x++) {
      MazePrefs.sounds[x].setSelected(MazePrefs.isSoundGroupEnabledImpl(x));
    }
    for (int x = 0; x < MazePrefs.MUSIC_LENGTH; x++) {
      MazePrefs.music[x].setSelected(MazePrefs.isMusicGroupEnabledImpl(x));
    }
    MazePrefs.updateCheckInterval
        .setSelectedIndex(MazePrefs.updateCheckIntervalIndex);
    MazePrefs.checkUpdatesStartup
        .setSelected(MazePrefs.checkUpdatesStartupEnabled);
    MazePrefs.difficultyChoices.setSelectedIndex(MazePrefs.difficultySetting);
    MazePrefs.moveOneAtATime.setSelected(MazePrefs.moveOneAtATimeEnabled);
    MazePrefs.viewingWindowChoices
        .setSelectedIndex(MazePrefs.viewingWindowIndex);
    MazePrefs.editorWindowChoices
        .setSelectedIndex(MazePrefs.editorWindowIndex);
    MazePrefs.minRandomRoomSizeX.setValue(MazePrefs.minRandomRoomSizeXIndex);
    MazePrefs.maxRandomRoomSizeX.setValue(MazePrefs.maxRandomRoomSizeXIndex);
    MazePrefs.minRandomRoomSizeY.setValue(MazePrefs.minRandomRoomSizeYIndex);
    MazePrefs.maxRandomRoomSizeY.setValue(MazePrefs.maxRandomRoomSizeYIndex);
  }

  private static void savePrefs() {
    if (!MazePrefs.guiSetUp) {
      MazePrefs.setUpGUI();
      MazePrefs.guiSetUp = true;
    }
    MazePrefs.editorFill = MazePrefs.editorFillChoices.getSelectedIndex();
    for (int x = 0; x < MazePrefs.SOUNDS_LENGTH; x++) {
      MazePrefs.setSoundGroupEnabledImpl(x, MazePrefs.sounds[x].isSelected());
    }
    for (int x = 0; x < MazePrefs.MUSIC_LENGTH; x++) {
      MazePrefs.setMusicGroupEnabled(x, MazePrefs.music[x].isSelected());
    }
    MazePrefs.updateCheckIntervalIndex = MazePrefs.updateCheckInterval
        .getSelectedIndex();
    MazePrefs.checkUpdatesStartupEnabled = MazePrefs.checkUpdatesStartup
        .isSelected();
    MazePrefs.difficultySetting = MazePrefs.difficultyChoices
        .getSelectedIndex();
    MazePrefs.moveOneAtATimeEnabled = MazePrefs.moveOneAtATime.isSelected();
    MazePrefs.viewingWindowIndex = MazePrefs.viewingWindowChoices
        .getSelectedIndex();
    MazePrefs.editorWindowIndex = MazePrefs.editorWindowChoices
        .getSelectedIndex();
    MazePrefs.minRandomRoomSizeXIndex = MazePrefs.minRandomRoomSizeX
        .getValue();
    MazePrefs.maxRandomRoomSizeXIndex = MazePrefs.maxRandomRoomSizeX
        .getValue();
    MazePrefs.minRandomRoomSizeYIndex = MazePrefs.minRandomRoomSizeY
        .getValue();
    MazePrefs.maxRandomRoomSizeYIndex = MazePrefs.maxRandomRoomSizeY
        .getValue();
  }

  public static void setDefaultPrefs() {
    if (!MazePrefs.fileMgr.readFromMaze()) {
      MazePrefs.resetDefaultPrefs();
    }
  }

  private static void resetDefaultPrefs() {
    if (!MazePrefs.guiSetUp) {
      MazePrefs.setUpGUI();
      MazePrefs.guiSetUp = true;
    }
    MazePrefs.editorFill = 0;
    MazePrefs.defaultEnableSoundGroups();
    MazePrefs.defaultEnableMusicGroups();
    MazePrefs.checkUpdatesStartup.setSelected(true);
    MazePrefs.checkUpdatesStartupEnabled = true;
    MazePrefs.difficultySetting = MazePrefs.DEFAULT_DIFFICULTY;
    MazePrefs.difficultyChoices.setSelectedIndex(MazePrefs.difficultySetting);
    MazePrefs.moveOneAtATime.setSelected(true);
    MazePrefs.moveOneAtATimeEnabled = true;
    MazePrefs.viewingWindowIndex = MazePrefs.DEFAULT_GAME_VIEW_SIZE_INDEX;
    MazePrefs.viewingWindowChoices
        .setSelectedIndex(MazePrefs.viewingWindowIndex);
    MazePrefs.editorWindowIndex = MazePrefs.DEFAULT_EDITOR_VIEW_SIZE_INDEX;
    MazePrefs.editorWindowChoices
        .setSelectedIndex(MazePrefs.editorWindowIndex);
    MazePrefs.updateCheckInterval.setSelectedIndex(0);
    MazePrefs.lastFilterUsed = MazePrefs.FILTER_MAZE_V5;
    MazePrefs.minRandomRoomSizeXIndex = MazePrefs.DEFAULT_ROOM_SIZE;
    MazePrefs.maxRandomRoomSizeXIndex = MazePrefs.DEFAULT_ROOM_SIZE;
    MazePrefs.minRandomRoomSizeYIndex = MazePrefs.DEFAULT_ROOM_SIZE;
    MazePrefs.maxRandomRoomSizeYIndex = MazePrefs.DEFAULT_ROOM_SIZE;
    MazePrefs.minRandomRoomSizeX.setValue(MazePrefs.DEFAULT_ROOM_SIZE);
    MazePrefs.maxRandomRoomSizeX.setValue(MazePrefs.DEFAULT_ROOM_SIZE);
    MazePrefs.minRandomRoomSizeY.setValue(MazePrefs.DEFAULT_ROOM_SIZE);
    MazePrefs.maxRandomRoomSizeY.setValue(MazePrefs.DEFAULT_ROOM_SIZE);
  }

  private static void setUpGUI() {
    MazePrefs.handler = new EventHandler();
    MazePrefs.prefTabPane = new JTabbedPane();
    MazePrefs.mainPrefPane = new JPanel();
    MazePrefs.editorPane = new JPanel();
    MazePrefs.soundPane = new JPanel();
    MazePrefs.musicPane = new JPanel();
    MazePrefs.miscPane = new JPanel();
    MazePrefs.prefTabPane.setOpaque(true);
    MazePrefs.buttonPane = new JPanel();
    MazePrefs.prefsOK = new JButton("OK");
    MazePrefs.prefsOK.setDefaultCapable(true);
    MazePrefs.prefsCancel = new JButton("Cancel");
    MazePrefs.prefsCancel.setDefaultCapable(false);
    MazePrefs.editorFillChoiceArray = new String[] { "Grass", "Dirt", "Sand",
        "Snow", "Tile", "Tundra" };
    MazePrefs.editorFillChoices = new JComboBox<>(
        MazePrefs.editorFillChoiceArray);
    MazePrefs.sounds[MazePrefs.SOUNDS_ALL] = new JCheckBox(
        "Enable ALL sounds", true);
    MazePrefs.sounds[MazePrefs.SOUNDS_UI] = new JCheckBox(
        "Enable user interface sounds", true);
    MazePrefs.sounds[MazePrefs.SOUNDS_GAME] = new JCheckBox(
        "Enable game sounds", true);
    MazePrefs.sounds[MazePrefs.SOUNDS_BATTLE] = new JCheckBox(
        "Enable battle sounds", true);
    MazePrefs.sounds[MazePrefs.SOUNDS_SHOP] = new JCheckBox(
        "Enable shop sounds", true);
    MazePrefs.music[MazePrefs.MUSIC_ALL] = new JCheckBox("Enable ALL music",
        true);
    MazePrefs.music[MazePrefs.MUSIC_UI] = new JCheckBox(
        "Enable user interface music", true);
    MazePrefs.music[MazePrefs.MUSIC_GAME] = new JCheckBox("Enable game music",
        true);
    MazePrefs.music[MazePrefs.MUSIC_BATTLE] = new JCheckBox(
        "Enable battle music", true);
    MazePrefs.music[MazePrefs.MUSIC_SHOP] = new JCheckBox("Enable shop music",
        true);
    MazePrefs.checkUpdatesStartup = new JCheckBox(
        "Check for Updates at Startup", true);
    MazePrefs.moveOneAtATime = new JCheckBox("One Move at a Time", true);
    MazePrefs.updateCheckIntervalValues = new String[] { "Daily",
        "Every 2nd Day", "Weekly", "Every 2nd Week", "Monthly" };
    MazePrefs.updateCheckInterval = new JComboBox<>(
        MazePrefs.updateCheckIntervalValues);
    MazePrefs.difficultyChoices = new JComboBox<>(
        MazePrefs.DIFFICULTY_CHOICE_NAMES);
    MazePrefs.minRandomRoomSizeX = new JSlider(MazePrefs.MIN_ROOM_SIZE,
        MazePrefs.MAX_ROOM_SIZE);
    MazePrefs.minRandomRoomSizeX
        .setLabelTable(MazePrefs.minRandomRoomSizeX.createStandardLabels(1));
    MazePrefs.minRandomRoomSizeX.setPaintLabels(true);
    MazePrefs.maxRandomRoomSizeX = new JSlider(MazePrefs.MIN_ROOM_SIZE,
        MazePrefs.MAX_ROOM_SIZE);
    MazePrefs.maxRandomRoomSizeX
        .setLabelTable(MazePrefs.maxRandomRoomSizeX.createStandardLabels(1));
    MazePrefs.maxRandomRoomSizeX.setPaintLabels(true);
    MazePrefs.minRandomRoomSizeY = new JSlider(MazePrefs.MIN_ROOM_SIZE,
        MazePrefs.MAX_ROOM_SIZE);
    MazePrefs.minRandomRoomSizeY
        .setLabelTable(MazePrefs.minRandomRoomSizeY.createStandardLabels(1));
    MazePrefs.minRandomRoomSizeY.setPaintLabels(true);
    MazePrefs.maxRandomRoomSizeY = new JSlider(MazePrefs.MIN_ROOM_SIZE,
        MazePrefs.MAX_ROOM_SIZE);
    MazePrefs.maxRandomRoomSizeY
        .setLabelTable(MazePrefs.maxRandomRoomSizeY.createStandardLabels(1));
    MazePrefs.maxRandomRoomSizeY.setPaintLabels(true);
    MazePrefs.mainPrefPane.setLayout(new BorderLayout());
    MazePrefs.editorPane.setLayout(new GridLayout(MazePrefs.GRID_LENGTH, 1));
    MazePrefs.editorPane.add(new JLabel("Default fill for new mazes:"));
    MazePrefs.editorPane.add(MazePrefs.editorFillChoices);
    MazePrefs.editorPane.add(new JLabel("Editor Window Size"));
    MazePrefs.editorWindowChoices = new JComboBox<>(
        MazePrefs.VIEWING_WINDOW_SIZE_NAMES);
    MazePrefs.editorPane.add(MazePrefs.editorWindowChoices);
    MazePrefs.soundPane.setLayout(new GridLayout(MazePrefs.GRID_LENGTH, 1));
    for (int x = 0; x < MazePrefs.SOUNDS_LENGTH; x++) {
      MazePrefs.soundPane.add(MazePrefs.sounds[x]);
    }
    MazePrefs.musicPane.setLayout(new GridLayout(MazePrefs.GRID_LENGTH, 1));
    for (int x = 0; x < MazePrefs.MUSIC_LENGTH; x++) {
      MazePrefs.musicPane.add(MazePrefs.music[x]);
    }
    MazePrefs.miscPane.setLayout(new GridLayout(MazePrefs.GRID_LENGTH, 1));
    MazePrefs.miscPane.add(MazePrefs.checkUpdatesStartup);
    MazePrefs.miscPane.add(MazePrefs.moveOneAtATime);
    MazePrefs.miscPane.add(new JLabel("Check How Often For Updates"));
    MazePrefs.miscPane.add(MazePrefs.updateCheckInterval);
    MazePrefs.miscPane.add(new JLabel("Game Difficulty"));
    MazePrefs.miscPane.add(MazePrefs.difficultyChoices);
    final JPanel viewPane = new JPanel();
    viewPane.setLayout(new GridLayout(MazePrefs.GRID_LENGTH, 1));
    viewPane.add(new JLabel("Viewing Window Size"));
    MazePrefs.viewingWindowChoices = new JComboBox<>(
        MazePrefs.VIEWING_WINDOW_SIZE_NAMES);
    viewPane.add(MazePrefs.viewingWindowChoices);
    final JPanel generatorPane = new JPanel();
    generatorPane.setLayout(new GridLayout(MazePrefs.GRID_LENGTH, 1));
    generatorPane
        .add(new JLabel("Minimum Room Size for Maze Generator (Wide)"));
    generatorPane.add(MazePrefs.minRandomRoomSizeX);
    generatorPane
        .add(new JLabel("Maximum Room Size for Maze Generator (Wide)"));
    generatorPane.add(MazePrefs.maxRandomRoomSizeX);
    generatorPane
        .add(new JLabel("Minimum Room Size for Maze Generator (Tall)"));
    generatorPane.add(MazePrefs.minRandomRoomSizeY);
    generatorPane
        .add(new JLabel("Maximum Room Size for Maze Generator (Tall)"));
    generatorPane.add(MazePrefs.maxRandomRoomSizeY);
    MazePrefs.buttonPane.setLayout(new FlowLayout());
    MazePrefs.buttonPane.add(MazePrefs.prefsOK);
    MazePrefs.buttonPane.add(MazePrefs.prefsCancel);
    MazePrefs.prefTabPane.addTab("Editor", null, MazePrefs.editorPane,
        "Editor");
    MazePrefs.prefTabPane.addTab("Generator", null, generatorPane,
        "Generator");
    MazePrefs.prefTabPane.addTab("Sounds", null, MazePrefs.soundPane,
        "Sounds");
    MazePrefs.prefTabPane.addTab("Music", null, MazePrefs.musicPane, "Music");
    MazePrefs.prefTabPane.addTab("Misc.", null, MazePrefs.miscPane, "Misc.");
    MazePrefs.prefTabPane.addTab("View", null, viewPane, "View");
    MazePrefs.mainPrefPane.add(MazePrefs.prefTabPane, BorderLayout.CENTER);
    MazePrefs.mainPrefPane.add(MazePrefs.buttonPane, BorderLayout.SOUTH);
    MazePrefs.sounds[MazePrefs.SOUNDS_ALL]
        .addItemListener(MazePrefs.handler);
    MazePrefs.music[MazePrefs.MUSIC_ALL].addItemListener(MazePrefs.handler);
    MazePrefs.prefsOK.addActionListener(MazePrefs.handler);
    MazePrefs.prefsCancel.addActionListener(MazePrefs.handler);
  }

  private static class MazePrefsStorageManager {
    // Constructors
    public MazePrefsStorageManager() {
      // Do nothing
    }

    public boolean readFromMaze() {
      try (final XDataReader reader = new XDataReader(
          CommonPaths.getPrefsFile().getAbsolutePath(), MazePrefs.DOC_TAG)) {
        // Read the preferences from the file
        // Read major version
        reader.readInt();
        // Read minor version
        reader.readInt();
        MazePrefs.editorFill = reader.readInt();
        MazePrefs.checkUpdatesStartupEnabled = reader.readBoolean();
        MazePrefs.moveOneAtATimeEnabled = reader.readBoolean();
        for (int x = 0; x < MazePrefs.SOUNDS_LENGTH; x++) {
          MazePrefs.soundsEnabled[x] = reader.readBoolean();
        }
        MazePrefs.updateCheckIntervalIndex = reader.readInt();
        MazePrefs.lastDirOpen = reader.readString();
        MazePrefs.lastDirSave = reader.readString();
        MazePrefs.lastFilterUsed = reader.readInt();
        MazePrefs.difficultySetting = reader.readInt();
        MazePrefs.viewingWindowIndex = reader.readInt();
        for (int x = 0; x < MazePrefs.MUSIC_LENGTH; x++) {
          MazePrefs.musicEnabled[x] = reader.readBoolean();
        }
        MazePrefs.editorWindowIndex = reader.readInt();
        MazePrefs.minRandomRoomSizeXIndex = reader.readInt();
        MazePrefs.maxRandomRoomSizeXIndex = reader.readInt();
        MazePrefs.minRandomRoomSizeYIndex = reader.readInt();
        MazePrefs.maxRandomRoomSizeYIndex = reader.readInt();
        MazePrefs.loadPrefs();
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
          prefsFile.getAbsolutePath(), MazePrefs.DOC_TAG)) {
        // Write the preferences to the file
        writer.writeInt(MazePrefs.PREFS_VERSION_MAJOR);
        writer.writeInt(MazePrefs.PREFS_VERSION_MINOR);
        writer.writeInt(MazePrefs.editorFill);
        writer.writeBoolean(MazePrefs.checkUpdatesStartupEnabled);
        writer.writeBoolean(MazePrefs.moveOneAtATimeEnabled);
        for (int x = 0; x < MazePrefs.SOUNDS_LENGTH; x++) {
          writer.writeBoolean(MazePrefs.soundsEnabled[x]);
        }
        writer.writeInt(MazePrefs.updateCheckIntervalIndex);
        writer.writeString(MazePrefs.lastDirOpen);
        writer.writeString(MazePrefs.lastDirSave);
        writer.writeInt(MazePrefs.lastFilterUsed);
        writer.writeInt(MazePrefs.difficultySetting);
        writer.writeInt(MazePrefs.viewingWindowIndex);
        for (int x = 0; x < MazePrefs.MUSIC_LENGTH; x++) {
          writer.writeBoolean(MazePrefs.musicEnabled[x]);
        }
        writer.writeInt(MazePrefs.editorWindowIndex);
        writer.writeInt(MazePrefs.minRandomRoomSizeXIndex);
        writer.writeInt(MazePrefs.maxRandomRoomSizeXIndex);
        writer.writeInt(MazePrefs.minRandomRoomSizeYIndex);
        writer.writeInt(MazePrefs.maxRandomRoomSizeYIndex);
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
            .equals(MazePrefs.sounds[MazePrefs.SOUNDS_ALL].getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(MazePrefs.sounds[MazePrefs.SOUNDS_ALL])) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              for (int x = 1; x < MazePrefs.SOUNDS_LENGTH; x++) {
                MazePrefs.sounds[x].setEnabled(true);
              }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              for (int x = 1; x < MazePrefs.SOUNDS_LENGTH; x++) {
                MazePrefs.sounds[x].setEnabled(false);
              }
            }
          }
        } else if (o.getClass()
            .equals(MazePrefs.music[MazePrefs.MUSIC_ALL].getClass())) {
          final JCheckBox check = (JCheckBox) o;
          if (check.equals(MazePrefs.music[MazePrefs.MUSIC_ALL])) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              for (int x = 1; x < MazePrefs.MUSIC_LENGTH; x++) {
                MazePrefs.music[x].setEnabled(true);
              }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
              for (int x = 1; x < MazePrefs.MUSIC_LENGTH; x++) {
                MazePrefs.music[x].setEnabled(false);
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
