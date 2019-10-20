/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze;

public class Extension {
    // Constants
    private static final String GAME_EXTENSION = "frgame";
    private static final String CHARACTER_EXTENSION = "frchar";
    private static final String REGISTRY_EXTENSION = "frregi";
    private static final String PREFERENCES_EXTENSION = "frpref";
    private static final String SCORES_EXTENSION = "scores";
    private static final String INTERNAL_DATA_EXTENSION = "dat";

    // Methods
    public static String getGameExtension() {
        return Extension.GAME_EXTENSION;
    }

    public static String getGameExtensionWithPeriod() {
        return "." + Extension.GAME_EXTENSION;
    }

    public static String getCharacterExtension() {
        return Extension.CHARACTER_EXTENSION;
    }

    public static String getPreferencesExtension() {
        return Extension.PREFERENCES_EXTENSION;
    }

    public static String getCharacterExtensionWithPeriod() {
        return "." + Extension.CHARACTER_EXTENSION;
    }

    public static String getScoresExtensionWithPeriod() {
        return "." + Extension.SCORES_EXTENSION;
    }

    public static String getRegistryExtensionWithPeriod() {
        return "." + Extension.REGISTRY_EXTENSION;
    }

    public static String getInternalDataExtensionWithPeriod() {
        return "." + Extension.INTERNAL_DATA_EXTENSION;
    }
}
