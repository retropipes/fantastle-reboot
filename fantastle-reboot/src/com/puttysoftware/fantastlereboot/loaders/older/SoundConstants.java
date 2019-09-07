package com.puttysoftware.fantastlereboot.loaders.older;

import com.puttysoftware.fantastlereboot.datamanagers.SoundDataManager;

public class SoundConstants {
    // Public Sound Constants
    public static final int SOUND_ACTION_FAILED = 0;
    public static final int SOUND_ARROW = 1;
    public static final int SOUND_ARROW_DIE = 2;
    public static final int SOUND_ATTACK = 3;
    public static final int SOUND_BARRIER = 4;
    public static final int SOUND_BATTLE = 5;
    public static final int SOUND_BIND = 6;
    public static final int SOUND_BOLT = 7;
    public static final int SOUND_BUTTON = 8;
    public static final int SOUND_CHANGE = 10;
    public static final int SOUND_COLD = 11;
    public static final int SOUND_CONFUSED = 12;
    public static final int SOUND_COUNTER = 13;
    public static final int SOUND_CRITICAL = 16;
    public static final int SOUND_DEFENSE = 18;
    public static final int SOUND_DIZZY = 20;
    public static final int SOUND_DOWN = 21;
    public static final int SOUND_DRAIN = 22;
    public static final int SOUND_DRUNK = 23;
    public static final int SOUND_EQUIP = 25;
    public static final int SOUND_EXPLODE = 26;
    public static final int SOUND_FIREBALL = 29;
    public static final int SOUND_FOCUS = 30;
    public static final int SOUND_FUMBLE = 32;
    public static final int SOUND_GAME_OVER = 33;
    public static final int SOUND_GRAB = 35;
    public static final int SOUND_HEAL = 37;
    public static final int SOUND_HIT = 39;
    public static final int SOUND_IDENTIFY = 40;
    public static final int SOUND_LEVEL_UP = 42;
    public static final int SOUND_LOGO = 45;
    public static final int SOUND_LOW_HEALTH = 46;
    public static final int SOUND_MISSED = 47;
    public static final int SOUND_NEXT_ROUND = 48;
    public static final int SOUND_PICK_LOCK = 51;
    public static final int SOUND_PLAYER_UP = 52;
    public static final int SOUND_SHOP = 55;
    public static final int SOUND_SLIME = 57;
    public static final int SOUND_SPECIAL = 58;
    public static final int SOUND_SPELL = 59;
    public static final int SOUND_TELEPORT = 64;
    public static final int SOUND_TRANSACT = 65;
    public static final int SOUND_UNLOCK = 66;
    public static final int SOUND_UP = 67;
    public static final int SOUND_VICTORY = 68;
    public static final int SOUND_WALK = 69;
    public static final int SOUND_WALK_FAILED = 72;
    public static final int SOUND_WALK_ICE = 73;
    public static final int SOUND_WEAKNESS = 78;
    private static final String[] SOUND_NAMES = SoundDataManager.getSoundData();

    // Private constructor
    private SoundConstants() {
        // Do nothing
    }

    static String getSoundName(final int ID) {
        return SOUND_NAMES[ID];
    }
}