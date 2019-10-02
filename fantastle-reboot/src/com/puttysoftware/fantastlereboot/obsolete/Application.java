/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.GUIManager;
import com.puttysoftware.fantastlereboot.MenuManager;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.game.GameLogicManager;
import com.puttysoftware.fantastlereboot.items.Shop;
import com.puttysoftware.fantastlereboot.obsolete.maze2.MazeManager;
import com.puttysoftware.fantastlereboot.utilities.MazeObjectList;

public final class Application {
    // Fields
    private GameLogicManager gameMgr;
    private MazeManager mazeMgr;
    private ObjectHelpManager oHelpMgr;
    private final MazeObjectList objects;

    // Constructors
    public Application() {
        this.objects = new MazeObjectList();
        this.oHelpMgr = new ObjectHelpManager();
    }

    // Methods
    @SuppressWarnings("static-method")
    public MenuManager getMenuManager() {
        return FantastleReboot.getBagOStuff().getMenuManager();
    }

    @SuppressWarnings("static-method")
    public GUIManager getGUIManager() {
        return FantastleReboot.getBagOStuff().getGUIManager();
    }

    public GameLogicManager getGameManager() {
        if (this.gameMgr == null) {
            this.gameMgr = new GameLogicManager();
        }
        return this.gameMgr;
    }

    public MazeManager getMazeManager() {
        if (this.mazeMgr == null) {
            this.mazeMgr = new MazeManager();
        }
        return this.mazeMgr;
    }

    public ObjectHelpManager getObjectHelpManager() {
        return this.oHelpMgr;
    }

    public MazeObjectList getObjects() {
        return this.objects;
    }

    public Shop getGenericShop(final int shopType) {
        this.getGameManager().stopMovement();
        return FantastleReboot.getBagOStuff().getShop(shopType);
    }

    @SuppressWarnings("static-method")
    public Battle getBattle() {
        return FantastleReboot.getBagOStuff().getBattle();
    }
}

