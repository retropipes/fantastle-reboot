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
package com.puttysoftware.fantastlereboot.resourcemanagers;

public class SoundList {
    // Fields
    private static final String[] allSounds = { "actfail", "barrier", "bomb",
            "button", "change", "confused", "create", "darkness", "destroy",
            "dizzy", "down", "drunk", "explode", "finish", "forcefld",
            "gameover", "generate", "grab", "heal", "highscor", "hurt",
            "identify", "intopit", "lava", "light", "logo", "pushpull",
            "sinkblck", "slime", "start", "teleport", "unlock", "up", "walk",
            "walkfail", "walkice", "walklava", "walkslim", "walkwatr",
            "walltrap", "water", "battle", "counter", "hit", "levelup",
            "missed", "victory", "easier", "harder", "arrow", "arrowdie",
            "shatter", "shop", "transact", "spell", "attack", "bind", "bolt",
            "defense", "drain", "focus", "ghostaxe", "iceshard", "tornado",
            "windswrd", "weakness", "potion", "fumble", "crack" };

    public static String[] getAllSoundNames() {
        return SoundList.allSounds;
    }
}
