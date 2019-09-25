/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import com.puttysoftware.fantastlereboot.FantastleReboot;

public class MapBattleViewingWindowManager {
    // Fields
    private int oldLocX, oldLocY, locX, locY;
    private static final int VIEWING_WINDOW_SIZE_MULTIPLIER = 1;
    private static final int VIEWING_WINDOW_SIZE_ADDITION = 0;

    // Constructors
    public MapBattleViewingWindowManager() {
        this.locX = 0;
        this.locY = 0;
        this.oldLocX = 0;
        this.oldLocY = 0;
    }

    // Methods
    public int getViewingWindowLocationX() {
        return this.locX;
    }

    public int getViewingWindowLocationY() {
        return this.locY;
    }

    public int getLowerRightViewingWindowLocationX() {
        return this.locX + MapBattleViewingWindowManager.getViewingWindowSize()
                - 1;
    }

    public int getLowerRightViewingWindowLocationY() {
        return this.locY + MapBattleViewingWindowManager.getViewingWindowSize()
                - 1;
    }

    public void setViewingWindowCenterX(final int val) {
        this.locX = val
                - (MapBattleViewingWindowManager.getViewingWindowSize() / 2);
    }

    public void setViewingWindowCenterY(final int val) {
        this.locY = val
                - (MapBattleViewingWindowManager.getViewingWindowSize() / 2);
    }

    public void offsetViewingWindowLocationX(final int val) {
        this.locX += val;
    }

    public void offsetViewingWindowLocationY(final int val) {
        this.locY += val;
    }

    public void saveViewingWindow() {
        this.oldLocX = this.locX;
        this.oldLocY = this.locY;
    }

    public void restoreViewingWindow() {
        this.locX = this.oldLocX;
        this.locY = this.oldLocY;
    }

    public static int getViewingWindowSize() {
        return (FantastleReboot.getBagOStuff().getPrefsManager()
                .getViewingWindowSize() * VIEWING_WINDOW_SIZE_MULTIPLIER)
                + VIEWING_WINDOW_SIZE_ADDITION;
    }
}
