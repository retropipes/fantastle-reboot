package net.worldwizard.fantastle5.creatures.faiths;

import com.puttysoftware.randomrange.RandomRange;

import net.worldwizard.fantastle5.Messager;

public class FaithManager implements FaithConstants {
    private static boolean CACHE_CREATED = false;
    private static Faith[] CACHE;

    public static Faith selectFaith() {
        final String[] names = FaithConstants.FAITH_NAMES;
        String dialogResult = null;
        dialogResult = Messager.showInputDialog("Select a Faith",
                "Select Faith", names, names[0]);
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
        if (!FaithManager.CACHE_CREATED) {
            // Create cache
            FaithManager.CACHE = new Faith[FaithConstants.FAITHS_COUNT];
            for (int x = 0; x < FaithConstants.FAITHS_COUNT; x++) {
                FaithManager.CACHE[x] = new Faith(x);
            }
            FaithManager.CACHE_CREATED = true;
        }
        return FaithManager.CACHE[faithID];
    }

    public static Faith getRandomFaith() {
        if (!FaithManager.CACHE_CREATED) {
            // Create cache
            FaithManager.CACHE = new Faith[FaithConstants.FAITHS_COUNT];
            for (int x = 0; x < FaithConstants.FAITHS_COUNT; x++) {
                FaithManager.CACHE[x] = new Faith(x);
            }
            FaithManager.CACHE_CREATED = true;
        }
        final int faithID = new RandomRange(0, FaithManager.CACHE.length - 1)
                .generate();
        return FaithManager.CACHE[faithID];
    }
}
