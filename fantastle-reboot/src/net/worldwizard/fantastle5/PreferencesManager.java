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
package net.worldwizard.fantastle5;

import java.awt.BorderLayout;
import java.awt.Container;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import net.worldwizard.fantastle5.battle.Battle;
import net.worldwizard.fantastle5.generic.MazeObject;
import net.worldwizard.fantastle5.maze.Extension;
import net.worldwizard.fantastle5.objects.Dirt;
import net.worldwizard.fantastle5.objects.Grass;
import net.worldwizard.fantastle5.objects.Sand;
import net.worldwizard.fantastle5.objects.Snow;
import net.worldwizard.fantastle5.objects.Tile;
import net.worldwizard.fantastle5.objects.Tundra;

public class PreferencesManager {
    // Fields
    JFrame prefFrame;
    private JFrame waitFrame;
    private JTabbedPane prefTabPane;
    private Container mainPrefPane, buttonPane, editorPane, miscPane,
            soundPane, musicPane;
    private JButton prefsOK, prefsCancel;
    private JButton prefsExport, prefsImport;
    final JCheckBox[] sounds;
    final JCheckBox[] music;
    JCheckBox checkUpdatesStartup;
    JCheckBox checkBetaUpdatesStartup;
    JCheckBox moveOneAtATime;
    JCheckBox mobileMode;
    JComboBox<String> editorFillChoices;
    private String[] editorFillChoiceArray;
    JComboBox<String> updateCheckInterval;
    private String[] updateCheckIntervalValues;
    private JLabel waitLabel;
    private JProgressBar waitProgress;
    int editorFill;
    private EventHandler handler;
    private final PreferencesFileManager fileMgr;
    private final ExportImportManager eiMgr;
    boolean checkUpdatesStartupEnabled;
    boolean checkBetaUpdatesStartupEnabled;
    boolean moveOneAtATimeEnabled;
    boolean mobileModeEnabled;
    final boolean[] soundsEnabled;
    final boolean[] musicEnabled;
    String lastDirOpen;
    String lastDirSave;
    int lastFilterUsed;
    private static final int SOUNDS_ALL = 0;
    public static final int SOUNDS_UI = 1;
    public static final int SOUNDS_GAME = 2;
    public static final int SOUNDS_BATTLE = 3;
    public static final int SOUNDS_SHOP = 4;
    public static final int MUSIC_ALL = 0;
    public static final int MUSIC_BATTLE = 1;
    public static final int MUSIC_EXPLORING = 2;
    public static final int FILTER_MAZE_V2 = 1;
    public static final int FILTER_MAZE_V3 = 2;
    public static final int FILTER_MAZE_V4 = 3;
    public static final int FILTER_GAME = 4;
    public static final int FILTER_MAZE_V5 = 5;
    private static final int MUSIC_LENGTH = 3;
    private static final int SOUNDS_LENGTH = 5;
    private static final int GRID_LENGTH = 6;
    private static final int PREFS_VERSION_MAJOR = 5;
    private static final int PREFS_VERSION_MINOR = 0;

    // Constructors
    public PreferencesManager() {
        this.sounds = new JCheckBox[PreferencesManager.SOUNDS_LENGTH];
        this.soundsEnabled = new boolean[PreferencesManager.SOUNDS_LENGTH];
        this.music = new JCheckBox[PreferencesManager.MUSIC_LENGTH];
        this.musicEnabled = new boolean[PreferencesManager.MUSIC_LENGTH];
        this.setUpGUI();
        this.fileMgr = new PreferencesFileManager();
        this.eiMgr = new ExportImportManager();
        this.setDefaultPrefs();
    }

    // Methods
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

    public boolean shouldCheckUpdatesAtStartup() {
        return this.checkUpdatesStartupEnabled;
    }

    public boolean shouldCheckBetaUpdatesAtStartup() {
        return this.checkBetaUpdatesStartupEnabled;
    }

    public boolean oneMove() {
        return this.moveOneAtATimeEnabled;
    }

    public boolean isMobileModeEnabled() {
        return this.mobileModeEnabled;
    }

    public boolean getSoundEnabled(final int snd) {
        if (!this.soundsEnabled[PreferencesManager.SOUNDS_ALL]) {
            return false;
        } else {
            return this.soundsEnabled[snd];
        }
    }

    public boolean getMusicEnabled(final int mus) {
        if (!this.musicEnabled[PreferencesManager.MUSIC_ALL]) {
            return false;
        } else {
            return this.musicEnabled[mus];
        }
    }

    public MazeObject getEditorDefaultFill() {
        final String choice = this.editorFillChoiceArray[this.editorFill];
        if (choice.equals("Tile")) {
            return new Tile();
        } else if (choice.equals("Grass")) {
            return new Grass();
        } else if (choice.equals("Dirt")) {
            return new Dirt();
        } else if (choice.equals("Snow")) {
            return new Snow();
        } else if (choice.equals("Tundra")) {
            return new Tundra();
        } else if (choice.equals("Sand")) {
            return new Sand();
        } else {
            return null;
        }
    }

    private void defaultEnableSounds() {
        for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
            this.soundsEnabled[x] = true;
        }
    }

    private void defaultEnableMusic() {
        for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
            this.musicEnabled[x] = true;
        }
    }

    public void setSoundEnabled(final int snd, final boolean status) {
        this.soundsEnabled[snd] = status;
    }

    public void setMusicEnabled(final int mus, final boolean status) {
        this.musicEnabled[mus] = status;
    }

    public JFrame getPrefFrame() {
        if (this.prefFrame != null && this.prefFrame.isVisible()) {
            return this.prefFrame;
        } else {
            return null;
        }
    }

    public void showPrefs() {
        if (Fantastle5.inFantastle5()) {
            final Application app = Fantastle5.getApplication();
            app.setInPrefs(true);
            this.prefFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
            app.getMenuManager().setPrefMenus();
            this.prefFrame.setVisible(true);
            if (Battle.isInBattle()) {
                app.getBattle().getBattleFrame().setVisible(false);
            } else {
                final int formerMode = app.getFormerMode();
                if (formerMode == Application.STATUS_GUI) {
                    app.getGUIManager().hideGUITemporarily();
                } else if (formerMode == Application.STATUS_GAME) {
                    app.getGameManager().hideOutput();
                } else if (formerMode == Application.STATUS_EDITOR) {
                    app.getEditor().hideOutput();
                }
            }
        }
    }

    public void hidePrefs() {
        if (Fantastle5.inFantastle5()) {
            final Application app = Fantastle5.getApplication();
            app.setInPrefs(false);
            this.prefFrame.setVisible(false);
            this.fileMgr.writePreferencesFile();
            if (Battle.isInBattle()) {
                app.getBattle().getBattleFrame().setVisible(true);
            } else {
                final int formerMode = app.getFormerMode();
                if (formerMode == Application.STATUS_GUI) {
                    app.getGUIManager().showGUI();
                } else if (formerMode == Application.STATUS_GAME) {
                    app.getGameManager().showOutput();
                } else if (formerMode == Application.STATUS_EDITOR) {
                    app.getEditor().showOutput();
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
        this.editorFill = this.editorFillChoices.getSelectedIndex();
        for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
            this.setSoundEnabled(x, this.sounds[x].isSelected());
        }
        for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
            this.setMusicEnabled(x, this.music[x].isSelected());
        }
        this.checkUpdatesStartupEnabled = this.checkUpdatesStartup.isSelected();
        this.checkBetaUpdatesStartupEnabled = this.checkBetaUpdatesStartup
                .isSelected();
        this.moveOneAtATimeEnabled = this.moveOneAtATime.isSelected();
        final boolean oldMobile = this.mobileModeEnabled;
        this.mobileModeEnabled = this.mobileMode.isSelected();
        if (!initial) {
            if (oldMobile != this.mobileModeEnabled) {
                // Graphics mode changed
                final CacheTask ct = new CacheTask();
                ct.start();
            } else {
                this.hidePrefs();
            }
        }
    }

    public void setDefaultPrefs() {
        if (!this.fileMgr.readPreferencesFile()) {
            this.resetDefaultPrefs();
        }
    }

    void enterWaitMode() {
        this.waitFrame.setVisible(true);
    }

    void updateWaitProgress(final int value) {
        this.waitProgress.setValue(value);
    }

    void exitWaitMode() {
        this.waitFrame.setVisible(false);
    }

    private void resetDefaultPrefs() {
        this.editorFill = 0;
        this.defaultEnableSounds();
        this.defaultEnableMusic();
        this.checkUpdatesStartup.setSelected(true);
        this.checkUpdatesStartupEnabled = true;
        this.checkBetaUpdatesStartup.setSelected(true);
        this.checkBetaUpdatesStartupEnabled = true;
        this.moveOneAtATime.setSelected(true);
        this.moveOneAtATimeEnabled = true;
        this.mobileMode.setSelected(false);
        this.mobileModeEnabled = false;
        this.updateCheckInterval.setSelectedIndex(0);
        this.lastFilterUsed = PreferencesManager.FILTER_MAZE_V5;
    }

    void handleExport() {
        final boolean result = this.eiMgr.exportPreferencesFile(this.eiMgr
                .getExportDestination());
        if (!result) {
            Messager.showErrorDialog("Export Failed!", "Preferences");
        }
    }

    void handleImport() {
        final boolean result = this.eiMgr.importPreferencesFile(this.eiMgr
                .getImportSource());
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
        this.editorPane = new Container();
        this.soundPane = new Container();
        this.musicPane = new Container();
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
        this.editorFillChoiceArray = new String[] { "Grass", "Dirt", "Sand",
                "Snow", "Tile", "Tundra" };
        this.editorFillChoices = new JComboBox<>(this.editorFillChoiceArray);
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
        this.music[PreferencesManager.MUSIC_ALL] = new JCheckBox(
                "Enable ALL music", true);
        this.music[PreferencesManager.MUSIC_BATTLE] = new JCheckBox(
                "Enable battle music", true);
        this.music[PreferencesManager.MUSIC_EXPLORING] = new JCheckBox(
                "Enable exploring music", true);
        this.checkUpdatesStartup = new JCheckBox(
                "Check for Updates at Startup", true);
        this.checkBetaUpdatesStartup = new JCheckBox(
                "Check for Beta Updates at Startup", true);
        this.moveOneAtATime = new JCheckBox("One Move at a Time", true);
        this.mobileMode = new JCheckBox("Enable mobile mode", false);
        this.updateCheckIntervalValues = new String[] { "Daily",
                "Every 2nd Day", "Weekly", "Every 2nd Week", "Monthly" };
        this.updateCheckInterval = new JComboBox<>(
                this.updateCheckIntervalValues);
        this.prefFrame.setContentPane(this.mainPrefPane);
        this.prefFrame
                .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.prefFrame.addWindowListener(this.handler);
        this.mainPrefPane.setLayout(new BorderLayout());
        this.prefFrame.setResizable(false);
        this.editorPane.setLayout(new GridLayout(
                PreferencesManager.GRID_LENGTH, 1));
        this.editorPane.add(new JLabel("Default fill for new mazes:"));
        this.editorPane.add(this.editorFillChoices);
        this.soundPane.setLayout(new GridLayout(PreferencesManager.GRID_LENGTH,
                1));
        for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
            this.soundPane.add(this.sounds[x]);
        }
        this.musicPane.setLayout(new GridLayout(PreferencesManager.GRID_LENGTH,
                1));
        for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
            this.musicPane.add(this.music[x]);
        }
        this.miscPane.setLayout(new GridLayout(PreferencesManager.GRID_LENGTH,
                1));
        this.miscPane.add(this.checkUpdatesStartup);
        if (Fantastle5.getApplication().isBetaModeEnabled()) {
            this.miscPane.add(this.checkBetaUpdatesStartup);
        }
        this.miscPane.add(this.moveOneAtATime);
        this.miscPane.add(this.mobileMode);
        this.miscPane.add(new JLabel("Check How Often For Updates"));
        this.miscPane.add(this.updateCheckInterval);
        this.buttonPane.setLayout(new FlowLayout());
        this.buttonPane.add(this.prefsOK);
        this.buttonPane.add(this.prefsCancel);
        this.buttonPane.add(this.prefsExport);
        this.buttonPane.add(this.prefsImport);
        this.prefTabPane.addTab("Editor", null, this.editorPane, "Editor");
        this.prefTabPane.addTab("Sounds", null, this.soundPane, "Sounds");
        this.prefTabPane.addTab("Music", null, this.musicPane, "Music");
        this.prefTabPane.addTab("Misc.", null, this.miscPane, "Misc.");
        this.mainPrefPane.add(this.prefTabPane, BorderLayout.CENTER);
        this.mainPrefPane.add(this.buttonPane, BorderLayout.SOUTH);
        this.sounds[PreferencesManager.SOUNDS_ALL]
                .addItemListener(this.handler);
        this.prefsOK.addActionListener(this.handler);
        this.prefsCancel.addActionListener(this.handler);
        this.prefsExport.addActionListener(this.handler);
        this.prefsImport.addActionListener(this.handler);
        this.prefFrame.pack();
        // Set up wait frame
        this.waitFrame = new JFrame("Please Wait...");
        this.waitLabel = new JLabel("Please wait, updating image caches...");
        this.waitProgress = new JProgressBar();
        this.waitProgress.setMinimum(0);
        this.waitProgress.setMaximum(100);
        this.waitProgress.setValue(0);
        this.waitFrame.getContentPane().setLayout(new GridLayout(2, 1));
        this.waitFrame.getContentPane().add(this.waitLabel);
        this.waitFrame.getContentPane().add(this.waitProgress);
        this.waitFrame
                .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.waitFrame.setResizable(false);
        this.waitFrame.setAlwaysOnTop(true);
        this.waitFrame.pack();
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
            try (final BufferedReader s = new BufferedReader(new FileReader(
                    this.getPrefsFile()))) {
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
                pm.editorFillChoices.setSelectedIndex(Integer.parseInt(s
                        .readLine()));
                pm.checkUpdatesStartup.setSelected(Boolean.parseBoolean(s
                        .readLine()));
                pm.moveOneAtATime
                        .setSelected(Boolean.parseBoolean(s.readLine()));
                for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
                    pm.sounds[x]
                            .setSelected(Boolean.parseBoolean(s.readLine()));
                }
                pm.updateCheckInterval.setSelectedIndex(Integer.parseInt(s
                        .readLine()));
                pm.lastDirOpen = s.readLine();
                pm.lastDirSave = s.readLine();
                pm.checkBetaUpdatesStartup.setSelected(Boolean.parseBoolean(s
                        .readLine()));
                pm.lastFilterUsed = Integer.parseInt(s.readLine());
                pm.mobileMode.setSelected(Boolean.parseBoolean(s.readLine()));
                for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
                    pm.music[x].setSelected(Boolean.parseBoolean(s.readLine()));
                }
                pm.setPrefs(true);
                return true;
            } catch (final PreferencesException pe) {
                Messager.showDialog(pe.getMessage());
                return false;
            } catch (final IOException ie) {
                return false;
            } catch (final Exception e) {
                Messager.showDialog("An error occurred while attempting to read the preferences file. Using defaults.");
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
            try (final BufferedWriter s = new BufferedWriter(new FileWriter(
                    prefsFile))) {
                // Write the preferences to the file
                final PreferencesManager pm = PreferencesManager.this;
                s.write(Integer
                        .toString(PreferencesManager.PREFS_VERSION_MAJOR)
                        + "\n");
                s.write(Integer
                        .toString(PreferencesManager.PREFS_VERSION_MINOR)
                        + "\n");
                s.write(Integer.toString(pm.editorFill) + "\n");
                s.write(Boolean.toString(pm.checkUpdatesStartupEnabled) + "\n");
                s.write(Boolean.toString(pm.moveOneAtATimeEnabled) + "\n");
                for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
                    s.write(Boolean.toString(pm.soundsEnabled[x]) + "\n");
                }
                s.write(Integer.toString(pm.updateCheckInterval
                        .getSelectedIndex()) + "\n");
                s.write(pm.lastDirOpen + "\n");
                s.write(pm.lastDirSave + "\n");
                s.write(Boolean.toString(pm.checkBetaUpdatesStartupEnabled)
                        + "\n");
                s.write(Integer.toString(pm.lastFilterUsed) + "\n");
                s.write(Boolean.toString(pm.mobileModeEnabled) + "\n");
                for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
                    s.write(Boolean.toString(pm.musicEnabled[x]) + "\n");
                }
                s.close();
            } catch (final IOException ie) {
                // Do nothing
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
            try (final BufferedReader s = new BufferedReader(new FileReader(
                    importFile))) {
                // Read the preferences from the file
                final PreferencesManager pm = PreferencesManager.this;
                // Read and discard major version
                s.readLine();
                // Read and discard minor version
                s.readLine();
                pm.editorFillChoices.setSelectedIndex(Integer.parseInt(s
                        .readLine()));
                pm.checkUpdatesStartup.setSelected(Boolean.parseBoolean(s
                        .readLine()));
                pm.moveOneAtATime
                        .setSelected(Boolean.parseBoolean(s.readLine()));
                for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
                    pm.sounds[x]
                            .setSelected(Boolean.parseBoolean(s.readLine()));
                }
                pm.updateCheckInterval.setSelectedIndex(Integer.parseInt(s
                        .readLine()));
                pm.checkBetaUpdatesStartup.setSelected(Boolean.parseBoolean(s
                        .readLine()));
                pm.lastFilterUsed = Integer.parseInt(s.readLine());
                pm.mobileMode.setSelected(Boolean.parseBoolean(s.readLine()));
                for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
                    pm.music[x].setSelected(Boolean.parseBoolean(s.readLine()));
                }
                s.close();
                pm.setPrefs(true);
                return true;
            } catch (final IOException ie) {
                return false;
            }
        }

        public boolean exportPreferencesFile(final File exportFile) {
            try (final BufferedWriter s = new BufferedWriter(new FileWriter(
                    exportFile))) {
                // Write the preferences to the file
                final PreferencesManager pm = PreferencesManager.this;
                s.write(Integer
                        .toString(PreferencesManager.PREFS_VERSION_MAJOR)
                        + "\n");
                s.write(Integer
                        .toString(PreferencesManager.PREFS_VERSION_MINOR)
                        + "\n");
                s.write(Integer.toString(pm.editorFill) + "\n");
                s.write(Boolean.toString(pm.checkUpdatesStartupEnabled) + "\n");
                s.write(Boolean.toString(pm.moveOneAtATimeEnabled) + "\n");
                for (int x = 0; x < PreferencesManager.SOUNDS_LENGTH; x++) {
                    s.write(Boolean.toString(pm.soundsEnabled[x]) + "\n");
                }
                s.write(Integer.toString(pm.updateCheckInterval
                        .getSelectedIndex()) + "\n");
                s.write(Boolean.toString(pm.checkBetaUpdatesStartupEnabled)
                        + "\n");
                s.write(Integer.toString(pm.lastFilterUsed) + "\n");
                s.write(Boolean.toString(pm.mobileModeEnabled) + "\n");
                for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
                    s.write(Boolean.toString(pm.musicEnabled[x]) + "\n");
                }
                s.close();
                return true;
            } catch (final IOException ie) {
                return false;
            }
        }

        public File getImportSource() {
            final FileDialog chooser = new FileDialog(
                    PreferencesManager.this.prefFrame, "Import",
                    FileDialog.LOAD);
            chooser.setVisible(true);
            return new File(chooser.getDirectory() + chooser.getFile());
        }

        public File getExportDestination() {
            final FileDialog chooser = new FileDialog(
                    PreferencesManager.this.prefFrame, "Export",
                    FileDialog.SAVE);
            chooser.setVisible(true);
            return new File(chooser.getDirectory() + chooser.getFile());
        }
    }

    private class EventHandler implements ActionListener, ItemListener,
            WindowListener {
        public EventHandler() {
            // TODO Auto-generated constructor stub
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
                Fantastle5.logError(ex);
            }
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            try {
                final PreferencesManager pm = PreferencesManager.this;
                final Object o = e.getItem();
                if (o.getClass().equals(
                        pm.sounds[PreferencesManager.SOUNDS_ALL].getClass())) {
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
                } else if (o.getClass().equals(
                        pm.music[PreferencesManager.MUSIC_ALL].getClass())) {
                    final JCheckBox check = (JCheckBox) o;
                    if (check.equals(pm.music[PreferencesManager.MUSIC_ALL])) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            for (int x = 1; x < PreferencesManager.MUSIC_LENGTH; x++) {
                                pm.music[x].setEnabled(true);
                            }
                        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                            for (int x = 1; x < PreferencesManager.MUSIC_LENGTH; x++) {
                                pm.music[x].setEnabled(false);
                            }
                        }
                    }
                }
            } catch (final Exception ex) {
                Fantastle5.logError(ex);
            }
        }

        @Override
        public void windowOpened(final WindowEvent e) {
            // Do nothing
        }

        @Override
        public void windowClosing(final WindowEvent e) {
            final PreferencesManager pm = PreferencesManager.this;
            pm.hidePrefs();
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
