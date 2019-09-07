/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.faiths;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.datamanagers.FaithDataManager;
import com.puttysoftware.fantastlereboot.oldnames.NamesConstants;
import com.puttysoftware.fantastlereboot.oldnames.NamesManager;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class FaithConstants {
    // Fields
    private static int FAITHS_COUNT = -1;
    private static String[] FAITH_NAMES = {};
    private static String[] FAITH_DISPLAY_NAMES = null;
    private static Color[] FAITH_COLORS = {};
    private static boolean INITED = false;
    private static final double[] LOOKUP_TABLE = { 0.0, 0.5, 1.0, 2.0 };

    // Private constructor
    private FaithConstants() {
        // Do nothing
    }

    // Methods
    public static int getFaithsCount() {
        if (!INITED) {
            FaithConstants.initFaiths();
        }
        return FaithConstants.FAITHS_COUNT;
    }

    static String[] getFaithNames() {
        if (!INITED) {
            FaithConstants.initFaiths();
        }
        return FaithConstants.FAITH_NAMES;
    }

    public static String getFaithName(final int f) {
        if (!INITED) {
            FaithConstants.initFaiths();
        }
        return FaithConstants.FAITH_DISPLAY_NAMES[f];
    }

    public static String getFaithPowerName(final int f, final int p) {
        return NamesManager.getName(NamesConstants.SECTION_FAITH_POWERS_PREFIX
                + FaithConstants.FAITH_NAMES[f],
                NamesConstants.SECTION_ARRAY_FAITH_POWERS[p]);
    }

    public static Color getFaithColor(final int f) {
        if (!INITED) {
            FaithConstants.initFaiths();
        }
        return FaithConstants.FAITH_COLORS[f];
    }

    public static double getLookupTableEntry(final int entryNum) {
        return FaithConstants.LOOKUP_TABLE[entryNum];
    }

    static boolean faithsReady() {
        return FaithConstants.INITED;
    }

    static synchronized void initFaiths() {
        if (!FaithConstants.INITED) {
            try {
                // Fetch data
                final ArrayList<String> tempNames = new ArrayList<>();
                try (final ResourceStreamReader rsr1 = new ResourceStreamReader(
                        FaithDataManager.class
                                .getResourceAsStream("/com/puttysoftware/tallertower/resources/data/faith/catalog.txt"))) {
                    String input1 = "";
                    while (input1 != null) {
                        input1 = rsr1.readString();
                        if (input1 != null) {
                            tempNames.add(input1);
                        }
                    }
                }
                FaithConstants.FAITH_NAMES = tempNames
                        .toArray(new String[tempNames.size()]);
                FaithConstants.FAITHS_COUNT = FaithConstants.FAITH_NAMES.length;
                final ArrayList<String> tempColors = new ArrayList<>();
                try (final ResourceStreamReader rsr2 = new ResourceStreamReader(
                        FaithDataManager.class
                                .getResourceAsStream("/com/puttysoftware/tallertower/resources/data/faith/colors.txt"))) {
                    String input2 = "";
                    while (input2 != null) {
                        input2 = rsr2.readString();
                        if (input2 != null) {
                            tempColors.add(input2);
                        }
                    }
                }
                final String[] tempColors2 = tempColors
                        .toArray(new String[tempColors.size()]);
                FaithConstants.FAITH_COLORS = new Color[tempColors2.length];
                final int[][] tempColors3 = new int[tempColors2.length][3];
                for (int x = 0; x < tempColors2.length; x++) {
                    final String[] tempColorSplit = tempColors2[x].split(",");
                    for (int y = 0; y < 3; y++) {
                        tempColors3[x][y] = Integer.parseInt(tempColorSplit[y]);
                    }
                    FaithConstants.FAITH_COLORS[x] = new Color(
                            tempColors3[x][0], tempColors3[x][1],
                            tempColors3[x][2]);
                }
                if (FaithConstants.FAITH_DISPLAY_NAMES == null) {
                    final String[] temp = new String[FaithConstants.FAITHS_COUNT];
                    for (int x = 0; x < temp.length; x++) {
                        temp[x] = NamesManager.getName(
                                NamesConstants.SECTION_FAITHS,
                                NamesConstants.SECTION_ARRAY_FAITHS[x]);
                    }
                    FaithConstants.FAITH_DISPLAY_NAMES = temp;
                }
                FaithConstants.INITED = true;
            } catch (final IOException ioe) {
                FantastleReboot.logError(ioe);
            }
        }
    }
}
