/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttmain;

import java.awt.Image;

import javax.swing.JFrame;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.battle.AbstractBattle;
import com.puttysoftware.fantastlereboot.battle.map.time.MapTimeBattleLogic;
import com.puttysoftware.fantastlereboot.battle.map.turn.MapTurnBattleLogic;
import com.puttysoftware.fantastlereboot.battle.window.time.WindowTimeBattleLogic;
import com.puttysoftware.fantastlereboot.battle.window.turn.WindowTurnBattleLogic;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.loaders.older.LogoManager;
import com.puttysoftware.fantastlereboot.ttgame.GameLogicManager;
import com.puttysoftware.fantastlereboot.ttmaze.MazeManager;
import com.puttysoftware.fantastlereboot.ttmaze.utilities.MazeObjectList;
import com.puttysoftware.fantastlereboot.ttshops.Shop;
import com.puttysoftware.fantastlereboot.ttshops.ShopTypes;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.updater.ProductData;

public final class Application {
    // Fields
    private AboutDialog about;
    private GameLogicManager gameMgr;
    private MazeManager mazeMgr;
    private MenuManager menuMgr;
    private ObjectHelpManager oHelpMgr;
    private GUIManager guiMgr;
    private final MazeObjectList objects;
    private Shop weapons, armor, healer, bank, regenerator, spells, items,
            socks, enhancements, faiths;
    private WindowTurnBattleLogic windowTurnBattle;
    private WindowTimeBattleLogic windowTimeBattle;
    private MapTurnBattleLogic mapTurnBattle;
    private MapTimeBattleLogic mapTimeBattle;
    private int currentMode;
    private int formerMode;
    private static final String UPDATE_SITE = "http://update.puttysoftware.com/tallertower/";
    private static final String NEW_VERSION_SITE = "http://www.puttysoftware.com/tallertower/";
    private static final String PRODUCT_NAME = "TallerTower";
    private static final String COMPANY_NAME = "Putty Software";
    private static final String RDNS_COMPANY_NAME = "com.puttysoftware.tallertower";
    private static final ProductData pd = new ProductData(
            Application.UPDATE_SITE, Application.UPDATE_SITE,
            Application.NEW_VERSION_SITE, Application.RDNS_COMPANY_NAME,
            Application.COMPANY_NAME, Application.PRODUCT_NAME,
            Application.VERSION_MAJOR, Application.VERSION_MINOR,
            Application.VERSION_BUGFIX, Application.VERSION_CODE,
            Application.VERSION_PRERELEASE);
    private static final int VERSION_MAJOR = 5;
    private static final int VERSION_MINOR = 1;
    private static final int VERSION_BUGFIX = 0;
    private static final int VERSION_CODE = ProductData.CODE_BETA;
    private static final int VERSION_PRERELEASE = 2;
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
    }

    // Methods
    void postConstruct() {
        // Create Managers
        this.about = new AboutDialog(Application.getVersionString());
        this.guiMgr = new GUIManager();
        this.menuMgr = new MenuManager();
        this.oHelpMgr = new ObjectHelpManager();
        this.windowTurnBattle = new WindowTurnBattleLogic();
        this.windowTimeBattle = new WindowTimeBattleLogic();
        this.mapTurnBattle = new MapTurnBattleLogic();
        this.mapTimeBattle = new MapTimeBattleLogic();
        this.weapons = new Shop(ShopTypes.SHOP_TYPE_WEAPONS);
        this.armor = new Shop(ShopTypes.SHOP_TYPE_ARMOR);
        this.healer = new Shop(ShopTypes.SHOP_TYPE_HEALER);
        this.bank = new Shop(ShopTypes.SHOP_TYPE_BANK);
        this.regenerator = new Shop(ShopTypes.SHOP_TYPE_REGENERATOR);
        this.spells = new Shop(ShopTypes.SHOP_TYPE_SPELLS);
        this.items = new Shop(ShopTypes.SHOP_TYPE_ITEMS);
        this.socks = new Shop(ShopTypes.SHOP_TYPE_SOCKS);
        this.enhancements = new Shop(ShopTypes.SHOP_TYPE_ENHANCEMENTS);
        this.faiths = new Shop(ShopTypes.SHOP_TYPE_FAITH_POWERS);
        // Cache Logo
        this.guiMgr.updateLogo();
    }

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

    public void restoreFormerMode() {
        this.currentMode = this.formerMode;
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

    public MenuManager getMenuManager() {
        return this.menuMgr;
    }

    public GUIManager getGUIManager() {
        return this.guiMgr;
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

    public AboutDialog getAboutDialog() {
        return this.about;
    }

    public static BufferedImageIcon getMicroLogo() {
        return LogoManager.getMicroLogo();
    }

    public static Image getIconLogo() {
        return LogoManager.getIconLogo();
    }

    public static void playLogoSound() {
        SoundLoader.playSound(GameSound.LOGO);
    }

    private static String getVersionString() {
        final int code = pd.getCodeVersion();
        String rt;
        if (code < ProductData.CODE_STABLE) {
            rt = "-beta" + Application.VERSION_PRERELEASE;
        } else {
            rt = "";
        }
        return Application.VERSION_MAJOR + "." + Application.VERSION_MINOR
                + "." + Application.VERSION_BUGFIX + rt;
    }

    public JFrame getOutputFrame() {
        try {
            if (this.getMode() == Application.STATUS_PREFS) {
                return FantastleReboot.getBagOStuff().getPrefsManager().getPrefFrame();
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
        switch (shopType) {
        case ShopTypes.SHOP_TYPE_ARMOR:
            return this.armor;
        case ShopTypes.SHOP_TYPE_BANK:
            return this.bank;
        case ShopTypes.SHOP_TYPE_ENHANCEMENTS:
            return this.enhancements;
        case ShopTypes.SHOP_TYPE_FAITH_POWERS:
            return this.faiths;
        case ShopTypes.SHOP_TYPE_HEALER:
            return this.healer;
        case ShopTypes.SHOP_TYPE_ITEMS:
            return this.items;
        case ShopTypes.SHOP_TYPE_REGENERATOR:
            return this.regenerator;
        case ShopTypes.SHOP_TYPE_SOCKS:
            return this.socks;
        case ShopTypes.SHOP_TYPE_SPELLS:
            return this.spells;
        case ShopTypes.SHOP_TYPE_WEAPONS:
            return this.weapons;
        default:
            // Invalid shop type
            return null;
        }
    }

    public AbstractBattle getBattle() {
        if (FantastleReboot.getBagOStuff().getPrefsManager().useMapBattleEngine()) {
            if (FantastleReboot.getBagOStuff().getPrefsManager().useTimeBattleEngine()) {
                return this.mapTimeBattle;
            } else {
                return this.mapTurnBattle;
            }
        } else {
            if (FantastleReboot.getBagOStuff().getPrefsManager().useTimeBattleEngine()) {
                return this.windowTimeBattle;
            } else {
                return this.windowTurnBattle;
            }
        }
    }

    public void resetBattleGUI() {
        this.mapTimeBattle.resetGUI();
        this.windowTimeBattle.resetGUI();
        this.mapTurnBattle.resetGUI();
        this.windowTurnBattle.resetGUI();
    }
}
