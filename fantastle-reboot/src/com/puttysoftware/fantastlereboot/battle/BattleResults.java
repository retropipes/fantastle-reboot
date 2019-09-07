/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle;

public class BattleResults {
    public static final int IN_PROGRESS = 0;
    public static final int WON = 1;
    public static final int LOST = 2;
    public static final int DRAW = 3;
    public static final int FLED = 4;
    public static final int ENEMY_FLED = 5;
    public static final int PERFECT = 6;
    public static final int ANNIHILATED = 7;

    private BattleResults() {
        // Do nothing
    }
}
