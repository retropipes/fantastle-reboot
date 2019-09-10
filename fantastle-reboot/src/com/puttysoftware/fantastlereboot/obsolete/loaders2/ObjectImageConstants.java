package com.puttysoftware.fantastlereboot.obsolete.loaders2;

import com.puttysoftware.fantastlereboot.datamanagers.GraphicsDataManager;

public class ObjectImageConstants {
    public static final int OBJECT_IMAGE_NONE = -1;
    public static final int OBJECT_IMAGE_ARMOR_SHOP = 0;
    public static final int OBJECT_IMAGE_BANK = 1;
    public static final int OBJECT_IMAGE_CCW_ROTATION_TRAP = 2;
    public static final int OBJECT_IMAGE_CLOSED_DOOR = 3;
    public static final int OBJECT_IMAGE_CONFUSION_TRAP = 4;
    public static final int OBJECT_IMAGE_CW_ROTATION_TRAP = 5;
    public static final int OBJECT_IMAGE_DARKNESS = 6;
    public static final int OBJECT_IMAGE_DIZZINESS_TRAP = 7;
    public static final int OBJECT_IMAGE_DRUNK_TRAP = 8;
    public static final int OBJECT_IMAGE_EMPTY = 9;
    public static final int OBJECT_IMAGE_ENHANCEMENT_SHOP = 10;
    public static final int OBJECT_IMAGE_FAITH_POWER_SHOP = 11;
    public static final int OBJECT_IMAGE_LIGHT_GEM = 12;
    public static final int OBJECT_IMAGE_HEAL_SHOP = 13;
    public static final int OBJECT_IMAGE_HEAL_TRAP = 14;
    public static final int OBJECT_IMAGE_HURT_TRAP = 15;
    public static final int OBJECT_IMAGE_ICE = 16;
    public static final int OBJECT_IMAGE_ITEM_SHOP = 17;
    public static final int OBJECT_IMAGE_MONSTER = 18;
    public static final int OBJECT_IMAGE_NOTE = 19;
    public static final int OBJECT_IMAGE_OPEN_DOOR = 20;
    public static final int OBJECT_IMAGE_PLAYER = 22;
    public static final int OBJECT_IMAGE_REGENERATOR = 23;
    public static final int OBJECT_IMAGE_SEALING_WALL = 24;
    public static final int OBJECT_IMAGE_SOCKS_SHOP = 25;
    public static final int OBJECT_IMAGE_SPELL_SHOP = 26;
    public static final int OBJECT_IMAGE_STAIRS_DOWN = 28;
    public static final int OBJECT_IMAGE_STAIRS_UP = 29;
    public static final int OBJECT_IMAGE_TILE = 30;
    public static final int OBJECT_IMAGE_U_TURN_TRAP = 31;
    public static final int OBJECT_IMAGE_VARIABLE_HEAL_TRAP = 32;
    public static final int OBJECT_IMAGE_VARIABLE_HURT_TRAP = 33;
    public static final int OBJECT_IMAGE_VOID = 34;
    public static final int OBJECT_IMAGE_WALL = 35;
    public static final int OBJECT_IMAGE_WARP_TRAP = 36;
    public static final int OBJECT_IMAGE_WEAPONS_SHOP = 37;
    public static final int OBJECT_IMAGE_WALL_OFF = 38;
    public static final int OBJECT_IMAGE_WALL_ON = 39;
    public static final int OBJECT_IMAGE_BUTTON = 40;
    public static final int OBJECT_IMAGE_AMULET = 41;
    public static final int OBJECT_IMAGE_DARK_GEM = 42;
    public static final int OBJECT_IMAGE_ARROW_NORTH = 43;
    public static final int OBJECT_IMAGE_ARROW_NORTHEAST = 44;
    public static final int OBJECT_IMAGE_ARROW_EAST = 45;
    public static final int OBJECT_IMAGE_ARROW_SOUTHEAST = 46;
    public static final int OBJECT_IMAGE_ARROW_SOUTH = 47;
    public static final int OBJECT_IMAGE_ARROW_SOUTHWEST = 48;
    public static final int OBJECT_IMAGE_ARROW_WEST = 49;
    public static final int OBJECT_IMAGE_ARROW_NORTHWEST = 50;
    private static final String[] OBJECT_IMAGE_NAMES = GraphicsDataManager
            .getObjectGraphicsData();

    static String getObjectImageName(final int ID) {
        if (ID == OBJECT_IMAGE_NONE) {
            return "";
        } else {
            return OBJECT_IMAGE_NAMES[ID];
        }
    }
}
