/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttprefs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;

import com.puttysoftware.fantastlereboot.ttmaze.Extension;

public class PreferencesManager {
    // Fields
    private static PreferencesStoreManager storeMgr = new PreferencesStoreManager();
    private static PreferencesGUIManager guiMgr = new PreferencesGUIManager();
    static final int MUSIC_ALL = 0;
    public static final int MUSIC_EXPLORING = 1;
    public static final int MUSIC_BATTLE = 2;
    static final int MUSIC_LENGTH = 3;
    public static final int DIFFICULTY_VERY_EASY = 0;
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_NORMAL = 2;
    public static final int DIFFICULTY_HARD = 3;
    public static final int DIFFICULTY_VERY_HARD = 4;
    private static final int DEFAULT_DIFFICULTY = DIFFICULTY_NORMAL;
    private static final String MAC_PREFIX = "HOME";
    private static final String WIN_PREFIX = "APPDATA";
    private static final String UNIX_PREFIX = "HOME";
    private static final String MAC_DIR = "/Library/Preferences/";
    private static final String WIN_DIR = "\\Putty Software\\TallerTower\\";
    private static final String UNIX_DIR = "/.puttysoftware/tallertower/";
    private static final String MAC_FILE = "com.puttysoftware.tallertower";
    private static final String WIN_FILE = "TallerTowerPreferences";
    private static final String UNIX_FILE = "TallerTowerPreferences";
    private static final String SOUNDS_SETTING = "SoundsEnabled";
    private static final String WINDOW_SETTING = "ViewingWindowSize";
    private static final String UPDATE_SETTING = "UpdatesStartup";
    private static final String MOVE_SETTING = "OneMove";
    private static final String BATTLE_SETTING = "UseMapBattleEngine";
    private static final String TIME_SETTING = "UseTimeBattleEngine";
    private static final String DIFFICULTY_SETTING = "GameDifficulty";
    private static final String MUSIC_SETTING = "MUSIC_";
    private static final String MUSIC_ALL_SETTING = "MUSIC_0";
    private static final int BATTLE_SPEED = 1000;

    // Private constructor
    private PreferencesManager() {
        // Do nothing
    }

    // Methods
    public static int getBattleSpeed() {
        return PreferencesManager.BATTLE_SPEED;
    }

    public static boolean useMapBattleEngine() {
        return PreferencesManager.storeMgr.getBoolean(BATTLE_SETTING, false);
    }

    static void setMapBattleEngine(final boolean value) {
        PreferencesManager.storeMgr.setBoolean(BATTLE_SETTING, value);
    }

    public static boolean useTimeBattleEngine() {
        return PreferencesManager.storeMgr.getBoolean(TIME_SETTING, false);
    }

    static void setTimeBattleEngine(final boolean value) {
        PreferencesManager.storeMgr.setBoolean(TIME_SETTING, value);
    }

    public static boolean getSoundsEnabled() {
        return PreferencesManager.storeMgr.getBoolean(SOUNDS_SETTING, true);
    }

    public static void setSoundsEnabled(final boolean value) {
        PreferencesManager.storeMgr.setBoolean(SOUNDS_SETTING, value);
    }

    public static int getViewingWindowSize() {
        return PreferencesGUIManager.VIEWING_WINDOW_SIZES[PreferencesManager
                .getViewingWindowSizeIndex()];
    }

    static int getViewingWindowSizeIndex() {
        return PreferencesManager.storeMgr.getInteger(WINDOW_SETTING,
                PreferencesGUIManager.DEFAULT_SIZE_INDEX);
    }

    static void setViewingWindowSizeIndex(final int value) {
        PreferencesManager.storeMgr.setInteger(WINDOW_SETTING, value);
    }

    public static boolean shouldCheckUpdatesAtStartup() {
        return PreferencesManager.storeMgr.getBoolean(UPDATE_SETTING, true);
    }

    static void setCheckUpdatesAtStartup(final boolean value) {
        PreferencesManager.storeMgr.setBoolean(UPDATE_SETTING, value);
    }

    public static boolean oneMove() {
        return PreferencesManager.storeMgr.getBoolean(MOVE_SETTING, true);
    }

    static void setOneMove(final boolean value) {
        PreferencesManager.storeMgr.setBoolean(MOVE_SETTING, value);
    }

    public static int getGameDifficulty() {
        return PreferencesManager.storeMgr.getInteger(DIFFICULTY_SETTING,
                DEFAULT_DIFFICULTY);
    }

    static void setGameDifficulty(final int value) {
        PreferencesManager.storeMgr.setInteger(DIFFICULTY_SETTING, value);
    }

    public static boolean getMusicEnabled(final int mus) {
        if (!PreferencesManager.storeMgr.getBoolean(MUSIC_ALL_SETTING, false)) {
            return false;
        } else {
            return PreferencesManager.storeMgr.getBoolean(MUSIC_SETTING + mus,
                    true);
        }
    }

    static void setMusicEnabled(final int mus, final boolean status) {
        PreferencesManager.storeMgr.setBoolean(MUSIC_SETTING + mus, status);
    }

    public static JFrame getPrefFrame() {
        return PreferencesManager.guiMgr.getPrefFrame();
    }

    public static void showPrefs() {
        PreferencesManager.guiMgr.showPrefs();
    }

    private static String getPrefsDirPrefix() {
        final String osName = System.getProperty("os.name");
        if (osName.indexOf("Mac OS X") != -1) {
            // Mac OS X
            return System.getenv(PreferencesManager.MAC_PREFIX);
        } else if (osName.indexOf("Windows") != -1) {
            // Windows
            return System.getenv(PreferencesManager.WIN_PREFIX);
        } else {
            // Other - assume UNIX-like
            return System.getenv(PreferencesManager.UNIX_PREFIX);
        }
    }

    private static String getPrefsDirectory() {
        final String osName = System.getProperty("os.name");
        if (osName.indexOf("Mac OS X") != -1) {
            // Mac OS X
            return PreferencesManager.MAC_DIR;
        } else if (osName.indexOf("Windows") != -1) {
            // Windows
            return PreferencesManager.WIN_DIR;
        } else {
            // Other - assume UNIX-like
            return PreferencesManager.UNIX_DIR;
        }
    }

    private static String getPrefsFileExtension() {
        return "." + Extension.getPreferencesExtension();
    }

    private static String getPrefsFileName() {
        final String osName = System.getProperty("os.name");
        if (osName.indexOf("Mac OS X") != -1) {
            // Mac OS X
            return PreferencesManager.MAC_FILE;
        } else if (osName.indexOf("Windows") != -1) {
            // Windows
            return PreferencesManager.WIN_FILE;
        } else {
            // Other - assume UNIX-like
            return PreferencesManager.UNIX_FILE;
        }
    }

    private static String getPrefsFile() {
        final StringBuilder b = new StringBuilder();
        b.append(PreferencesManager.getPrefsDirPrefix());
        b.append(PreferencesManager.getPrefsDirectory());
        b.append(PreferencesManager.getPrefsFileName());
        b.append(PreferencesManager.getPrefsFileExtension());
        return b.toString();
    }

    public static void writePrefs() {
        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(PreferencesManager.getPrefsFile()))) {
            PreferencesManager.storeMgr.saveStore(bos);
        } catch (final IOException io) {
            // Ignore
        }
    }

    static void readPrefs() {
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(PreferencesManager.getPrefsFile()))) {
            // Read new preferences
            PreferencesManager.storeMgr.loadStore(bis);
        } catch (final IOException io) {
            // Populate store with defaults
            PreferencesManager.storeMgr.setBoolean(UPDATE_SETTING, true);
            PreferencesManager.storeMgr.setBoolean(MOVE_SETTING, true);
            for (int x = 0; x < PreferencesManager.MUSIC_LENGTH; x++) {
                PreferencesManager.storeMgr.setBoolean(MUSIC_SETTING + x, true);
            }
            PreferencesManager.storeMgr.setInteger(WINDOW_SETTING,
                    PreferencesGUIManager.DEFAULT_VIEWING_WINDOW_SIZE);
            PreferencesManager.storeMgr.setBoolean(SOUNDS_SETTING, true);
            PreferencesManager.storeMgr.setBoolean(BATTLE_SETTING, false);
            PreferencesManager.storeMgr.setBoolean(TIME_SETTING, false);
            PreferencesManager.storeMgr.setInteger(DIFFICULTY_SETTING,
                    DEFAULT_DIFFICULTY);
        }
    }
}
