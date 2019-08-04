package com.puttysoftware.fantastlereboot.creatures.races;

import com.puttysoftware.fantastlereboot.Messager;

public class RaceManager implements RaceConstants {
    private static boolean CACHE_CREATED = false;
    private static Race[] CACHE;

    public static Race selectRace() {
        final String[] names = RaceConstants.RACE_NAMES;
        String dialogResult = null;
        dialogResult = Messager.showInputDialog("Select a Race", "Select Race",
                names, names[0]);
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

    public static Race getRace(final int casteID) {
        if (!RaceManager.CACHE_CREATED) {
            // Create cache
            RaceManager.CACHE = new Race[RaceConstants.RACES_COUNT];
            for (int x = 0; x < RaceConstants.RACES_COUNT; x++) {
                RaceManager.CACHE[x] = new Race(x);
            }
            RaceManager.CACHE_CREATED = true;
        }
        return RaceManager.CACHE[casteID];
    }
}
