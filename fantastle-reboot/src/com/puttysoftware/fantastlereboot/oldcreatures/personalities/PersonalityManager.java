package com.puttysoftware.fantastlereboot.oldcreatures.personalities;

import com.puttysoftware.fantastlereboot.Messager;

public class PersonalityManager implements PersonalityConstants {
    private static boolean CACHE_CREATED = false;
    private static Personality[] CACHE;

    public static Personality selectPersonality() {
        final String[] names = PersonalityConstants.PERSONALITY_NAMES;
        String dialogResult = null;
        dialogResult = Messager.showInputDialog("Select a Personality",
                "Select Personality", names, names[0]);
        if (dialogResult != null) {
            int index;
            for (index = 0; index < names.length; index++) {
                if (dialogResult.equals(names[index])) {
                    break;
                }
            }
            return PersonalityManager.getPersonality(index);
        } else {
            return null;
        }
    }

    public static Personality getPersonality(final int personalityID) {
        if (!PersonalityManager.CACHE_CREATED) {
            // Create cache
            PersonalityManager.CACHE = new Personality[PersonalityConstants.PERSONALITIES_COUNT];
            for (int x = 0; x < PersonalityConstants.PERSONALITIES_COUNT; x++) {
                PersonalityManager.CACHE[x] = new Personality(x);
            }
            PersonalityManager.CACHE_CREATED = true;
        }
        return PersonalityManager.CACHE[personalityID];
    }
}
