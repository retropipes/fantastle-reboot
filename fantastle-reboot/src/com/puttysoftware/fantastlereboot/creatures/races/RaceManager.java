/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.races;

import javax.swing.JFrame;

import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.datamanagers.RaceDataManager;

public class RaceManager {
    private static boolean CACHE_CREATED = false;
    private static Race[] CACHE;
    private static String[] DESC_CACHE;

    public static Race selectRace(final JFrame owner) {
        RaceManager.createCache();
        final String[] names = RaceConstants.getRaceNames();
        String dialogResult = null;
        dialogResult = PartyManager.showCreationDialog(owner, "Select a Race",
                "Create Character", names, RaceManager.DESC_CACHE);
        if (dialogResult != null) {
            int index;
            for (index = 0; index < names.length; index++) {
                if (dialogResult.equals(names[index])) {
                    break;
                }
            }
            return RaceManager.getRace(index);
        } else {
            return null;
        }
    }

    public static Race getRace(final int raceID) {
        RaceManager.createCache();
        return RaceManager.CACHE[raceID];
    }

    private static void createCache() {
        if (!RaceManager.CACHE_CREATED) {
            if (!RaceConstants.racesReady()) {
                RaceConstants.initRaces();
            }
            // Create cache
            if (!RaceConstants.racesReady()) {
                RaceConstants.initRaces();
            }
            RaceManager.CACHE = new Race[RaceConstants.getRacesCount()];
            RaceManager.DESC_CACHE = new String[RaceConstants.getRacesCount()];
            for (int x = 0; x < RaceConstants.getRacesCount(); x++) {
                final int[] rdata = RaceDataManager.getRaceData(x);
                RaceManager.CACHE[x] = new Race(x, rdata);
                RaceManager.DESC_CACHE[x] = RaceManager.CACHE[x]
                        .getDescription();
            }
            RaceManager.CACHE_CREATED = true;
        }
    }
}
