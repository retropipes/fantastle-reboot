/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.faiths;

import javax.swing.JFrame;

import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.randomrange.RandomRange;

public class FaithManager {
    private static boolean CACHE_CREATED = false;
    private static Faith[] CACHE;
    private static String[] DESC_CACHE;

    public static Faith selectFaith(final JFrame owner) {
        FaithManager.createCache();
        final String[] names = FaithConstants.getFaithNames();
        String dialogResult = null;
        dialogResult = PartyManager.showCreationDialog(owner, "Select a Faith",
                "Create Character", names, FaithManager.DESC_CACHE);
        if (dialogResult != null) {
            int index;
            for (index = 0; index < names.length; index++) {
                if (dialogResult.equals(names[index])) {
                    break;
                }
            }
            return FaithManager.getFaith(index);
        } else {
            return null;
        }
    }

    public static Faith getFaith(final int faithID) {
        FaithManager.createCache();
        return FaithManager.CACHE[faithID];
    }

    public static Faith getRandomFaith() {
        FaithManager.createCache();
        final int faithID = new RandomRange(0, FaithManager.CACHE.length - 1)
                .generate();
        return FaithManager.CACHE[faithID];
    }

    private static void createCache() {
        if (!FaithManager.CACHE_CREATED) {
            // Create cache
            if (!FaithConstants.faithsReady()) {
                FaithConstants.initFaiths();
            }
            final int fc = FaithConstants.getFaithsCount();
            FaithManager.CACHE = new Faith[fc];
            FaithManager.DESC_CACHE = new String[fc];
            for (int x = 0; x < fc; x++) {
                FaithManager.CACHE[x] = new Faith(x);
                FaithManager.DESC_CACHE[x] = FaithManager.CACHE[x]
                        .getDescription();
            }
            FaithManager.CACHE_CREATED = true;
        }
    }
}
