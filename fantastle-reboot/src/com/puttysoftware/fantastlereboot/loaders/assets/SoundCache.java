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
package com.puttysoftware.fantastlereboot.loaders.assets;

import com.puttysoftware.audio.wav.WAVFactory;

@Deprecated(forRemoval = true)
public class SoundCache {
    // Fields
    private static WAVFactory[] cache;
    private static String[] nameCache;
    private static boolean cacheCreated = false;

    // Methods
    public static WAVFactory getCachedSound(final String name) {
        if (!SoundCache.cacheCreated) {
            SoundCache.createCache();
        }
        for (int x = 0; x < SoundCache.nameCache.length; x++) {
            if (name.equals(SoundCache.nameCache[x])) {
                return SoundCache.cache[x];
            }
        }
        return null;
    }

    private static void createCache() {
        if (!SoundCache.cacheCreated) {
            // Create the cache
            SoundCache.nameCache = SoundList.getAllSoundNames();
            SoundCache.cache = new WAVFactory[SoundCache.nameCache.length];
            for (int x = 0; x < SoundCache.nameCache.length; x++) {
                SoundCache.cache[x] = SoundManager
                        .getUncachedSound(SoundCache.nameCache[x]);
            }
            SoundCache.cacheCreated = true;
        }
    }

    public static void recreateCache() {
        SoundCache.cacheCreated = false;
        SoundCache.createCache();
    }
}