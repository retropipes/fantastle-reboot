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
package net.worldwizard.fantastle5.maze;

public class Extension {
    // Constants
    private static final String MAZE_2_EXTENSION = "ftlmz";
    private static final String MAZE_3_EXTENSION = "ft3mz";
    private static final String MAZE_4_EXTENSION = "ft4mz";
    private static final String MAZE_5_EXTENSION = "ft5mz";
    private static final String SAVED_GAME_EXTENSION = "ft5sg";
    private static final String SCORES_EXTENSION = "ft5sc";
    private static final String PREFERENCES_EXTENSION = "ft5prf";

    // Methods
    public static String getMaze2Extension() {
        return Extension.MAZE_2_EXTENSION;
    }

    public static String getMaze2ExtensionWithPeriod() {
        return "." + Extension.MAZE_2_EXTENSION;
    }

    public static String getMaze3Extension() {
        return Extension.MAZE_3_EXTENSION;
    }

    public static String getMaze3ExtensionWithPeriod() {
        return "." + Extension.MAZE_3_EXTENSION;
    }

    public static String getMaze4Extension() {
        return Extension.MAZE_4_EXTENSION;
    }

    public static String getMaze4ExtensionWithPeriod() {
        return "." + Extension.MAZE_4_EXTENSION;
    }

    public static String getMaze5Extension() {
        return Extension.MAZE_5_EXTENSION;
    }

    public static String getMaze5ExtensionWithPeriod() {
        return "." + Extension.MAZE_5_EXTENSION;
    }

    public static String getGameExtension() {
        return Extension.SAVED_GAME_EXTENSION;
    }

    public static String getGameExtensionWithPeriod() {
        return "." + Extension.SAVED_GAME_EXTENSION;
    }

    public static String getScoresExtension() {
        return Extension.SCORES_EXTENSION;
    }

    public static String getScoresExtensionWithPeriod() {
        return "." + Extension.SCORES_EXTENSION;
    }

    public static String getPreferencesExtension() {
        return Extension.PREFERENCES_EXTENSION;
    }

    public static String getPreferencesExtensionWithPeriod() {
        return "." + Extension.PREFERENCES_EXTENSION;
    }
}
