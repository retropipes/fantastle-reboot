/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete;

import javax.swing.JFrame;

import com.puttysoftware.commondialogs.CommonDialogs;
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
    private int currentMode;
    private int formerMode;
    public static final int STATUS_GUI = 0;
    public static final int STATUS_GAME = 1;
    public static final int STATUS_BATTLE = 2;
    public static final int STATUS_PREFS = 3;
    public static final int STATUS_NULL = 4;

    // Constructors
    public Application() {
        this.objects = new MazeObjectList();
        this.currentMode = Application.STATUS_NULL;
        this.formerMode = Application.STATUS_NULL;
        this.oHelpMgr = new ObjectHelpManager();
    }

    // Methods
    public void setMode(final int newMode) {
        this.formerMode = this.currentMode;
        this.currentMode = newMode;
    }

    public int getMode() {
        return this.currentMode;
    }

    public int getFormerMode() {
        return this.formerMode;
    }

    public boolean modeChanged() {
        return this.formerMode != this.currentMode;
    }

    public void saveFormerMode() {
        this.formerMode = this.currentMode;
    }

    public void showMessage(final String msg) {
        if (this.currentMode == Application.STATUS_GAME) {
            this.getGameManager().setStatusMessage(msg);
        } else if (this.currentMode == Application.STATUS_BATTLE) {
            this.getBattle().setStatusMessage(msg);
        } else {
            CommonDialogs.showDialog(msg);
        }
    }

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

    public JFrame getOutputFrame() {
        try {
            if (this.getMode() == Application.STATUS_PREFS) {
                return FantastleReboot.getBagOStuff().getPrefsManager()
                        .getPrefFrame();
            } else if (this.getMode() == Application.STATUS_GUI) {
                return this.getGUIManager().getGUIFrame();
            } else if (this.getMode() == Application.STATUS_GAME) {
                return this.getGameManager().getOutputFrame();
            } else if (this.getMode() == Application.STATUS_BATTLE) {
                return this.getBattle().getOutputFrame();
            } else {
                return null;
            }
        } catch (final NullPointerException npe) {
            return null;
        }
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
