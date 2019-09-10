/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.names;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.datamanagers.NamesDataManager;

public class NamesManager {
    private static boolean CACHE_CREATED = false;
    private static String[] RAW_CACHE;
    private static String[][] CACHE;

    public static String getName(final String section, final String type) {
        try {
            NamesManager.createCache();
        } catch (final Exception e) {
            FantastleReboot.logError(e);
        }
        final String key = section + ":" + type;
        if (!NamesManager.containsKey(key)) {
            FantastleReboot.logError(
                    new IllegalArgumentException("No such key " + key));
        }
        return NamesManager.getValue(key);
    }

    private static boolean containsKey(final String key) {
        for (int x = 0; x < CACHE.length; x++) {
            if (CACHE[x][0].equals(key)) {
                return true;
            }
        }
        return false;
    }

    private static String getValue(final String key) {
        for (int x = 0; x < CACHE.length; x++) {
            if (CACHE[x][0].equals(key)) {
                return CACHE[x][1];
            }
        }
        return null;
    }

    private static void createCache() {
        if (!NamesManager.CACHE_CREATED) {
            // Create raw cache
            NamesManager.RAW_CACHE = NamesDataManager.getNamesData();
            if (NamesManager.RAW_CACHE != null) {
                NamesManager.CACHE = new String[NamesManager.RAW_CACHE.length][2];
                for (int x = 0; x < NamesManager.RAW_CACHE.length; x++) {
                    if (NamesManager.RAW_CACHE[x] != null
                            && !NamesManager.RAW_CACHE[x].isEmpty()) {
                        // Entry
                        final String[] splitEntry = NamesManager.RAW_CACHE[x]
                                .split("=");
                        // Sanity check
                        if (splitEntry.length < 2) {
                            throw new IllegalArgumentException(
                                    "Invalid names file format: Entry format invalid!");
                        }
                        final String key = splitEntry[0];
                        final String value = splitEntry[1];
                        NamesManager.CACHE[x][0] = key;
                        NamesManager.CACHE[x][1] = value;
                    }
                }
                NamesManager.CACHE_CREATED = true;
            } else {
                throw new IllegalArgumentException("Names file not found!");
            }
        }
    }
}
