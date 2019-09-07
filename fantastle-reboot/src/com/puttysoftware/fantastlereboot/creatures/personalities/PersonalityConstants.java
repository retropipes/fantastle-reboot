/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.personalities;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.datamanagers.PersonalityDataManager;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class PersonalityConstants {
    // Fields
    public static final int PERSONALITY_ATTRIBUTE_LEVEL_UP_SPEED = 0;
    public static final int PERSONALITY_ATTRIBUTE_ACTION_MOD = 1;
    public static final int PERSONALITY_ATTRIBUTE_CAPACITY_MOD = 2;
    public static final int PERSONALITY_ATTRIBUTE_WEALTH_MOD = 3;
    public static final int PERSONALITY_ATTRIBUTES_COUNT = 4;
    private static int PERSONALITIES_COUNT = -1;
    private static String[] PERSONALITY_NAMES = {};
    private static boolean INITED = false;
    private static final double[] LOOKUP_TABLE = { 0.5, 0.54, 0.58, 0.63, 0.67,
            0.71, 0.75, 0.79, 0.83, 0.88, 0.92, 0.96, 1.0, 1.08, 1.17, 1.25,
            1.33, 1.42, 1.5, 1.58, 1.67, 1.75, 1.83, 1.92, 2.0 };

    // Private constructor
    private PersonalityConstants() {
        // Do nothing
    }

    // Methods
    static int getPersonalitiesCount() {
        return PersonalityConstants.PERSONALITIES_COUNT;
    }

    static String[] getPersonalityNames() {
        return PersonalityConstants.PERSONALITY_NAMES;
    }

    public static String getPersonalityName(final int p) {
        return PersonalityConstants.PERSONALITY_NAMES[p];
    }

    public static double getLookupTableEntry(final int entryNum) {
        return PersonalityConstants.LOOKUP_TABLE[entryNum + 12];
    }

    static boolean personalitiesReady() {
        return PersonalityConstants.INITED;
    }

    static void initPersonalities() {
        if (!PersonalityConstants.INITED) {
            try (final ResourceStreamReader rsr = new ResourceStreamReader(
                    PersonalityDataManager.class
                            .getResourceAsStream("/com/puttysoftware/tallertower/resources/data/personality/catalog.txt"))) {
                // Fetch data
                final ArrayList<String> tempNames = new ArrayList<>();
                String input = "";
                while (input != null) {
                    input = rsr.readString();
                    if (input != null) {
                        tempNames.add(input);
                    }
                }
                PersonalityConstants.PERSONALITY_NAMES = tempNames
                        .toArray(new String[tempNames.size()]);
                PersonalityConstants.PERSONALITIES_COUNT = PersonalityConstants.PERSONALITY_NAMES.length;
                PersonalityConstants.INITED = true;
            } catch (final IOException ioe) {
                TallerTower.getErrorLogger().logError(ioe);
            }
        }
    }
}
