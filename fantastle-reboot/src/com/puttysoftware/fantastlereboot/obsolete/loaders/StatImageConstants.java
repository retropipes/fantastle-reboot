package com.puttysoftware.fantastlereboot.obsolete.loaders;

import com.puttysoftware.fantastlereboot.datamanagers.GraphicsDataManager;

@Deprecated(forRemoval = true)
public class StatImageConstants {
    public static final int STAT_IMAGE_ACTIONS = 0;
    public static final int STAT_IMAGE_ATTACK = 1;
    public static final int STAT_IMAGE_ATTACKS = 2;
    public static final int STAT_IMAGE_DEFENSE = 3;
    public static final int STAT_IMAGE_GOLD = 4;
    public static final int STAT_IMAGE_HEALTH = 5;
    public static final int STAT_IMAGE_MAGIC = 7;
    public static final int STAT_IMAGE_NAME = 10;
    public static final int STAT_IMAGE_SPELLS = 12;
    public static final int STAT_IMAGE_TEAM = 14;
    public static final int STAT_IMAGE_XP = 16;
    public static final int STAT_IMAGE_LEVEL = 17;
    private static final String[] STAT_IMAGE_NAMES = GraphicsDataManager
            .getStatGraphicsData();

    static String getStatImageName(final int ID) {
        return STAT_IMAGE_NAMES[ID];
    }
}
