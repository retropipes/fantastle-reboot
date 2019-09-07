/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.ttmaze.effects;

public class MazeEffectConstants {
    public static final int EFFECT_ROTATED_CLOCKWISE = 0;
    public static final int EFFECT_ROTATED_COUNTERCLOCKWISE = 1;
    public static final int EFFECT_U_TURNED = 2;
    public static final int EFFECT_CONFUSED = 3;
    public static final int EFFECT_DIZZY = 4;
    public static final int EFFECT_DRUNK = 5;
    public static final int EFFECT_STICKY = 6;
    public static final int EFFECT_POWER_GATHER = 7;
    public static final int EFFECT_POWER_WITHER = 8;
    private static final int[] DURATIONS_VERY_EASY = new int[] { 2, 2, 2, 2, 1,
            3, 25, 12, 2 };
    private static final int[] DURATIONS_EASY = new int[] { 4, 4, 4, 4, 2, 6,
            20, 9, 4 };
    private static final int[] DURATIONS_NORMAL = new int[] { 6, 6, 6, 6, 3, 9,
            15, 6, 6 };
    private static final int[] DURATIONS_HARD = new int[] { 8, 8, 8, 8, 6, 12,
            10, 3, 9 };
    private static final int[] DURATIONS_VERY_HARD = new int[] { 10, 10, 10,
            10, 9, 15, 5, 2, 12 };
    static final int[][] DURATIONS = new int[][] { DURATIONS_VERY_EASY,
            DURATIONS_EASY, DURATIONS_NORMAL, DURATIONS_HARD,
            DURATIONS_VERY_HARD };

    private MazeEffectConstants() {
        // Do nothing
    }
}
