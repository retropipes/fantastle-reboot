/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot;

import javax.swing.JFrame;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.assets.GameUserInterfaceImage;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.battle.map.time.MapTimeBattleLogic;
import com.puttysoftware.fantastlereboot.battle.map.turn.MapTurnBattleLogic;
import com.puttysoftware.fantastlereboot.battle.window.time.WindowTimeBattleLogic;
import com.puttysoftware.fantastlereboot.battle.window.turn.WindowTurnBattleLogic;
import com.puttysoftware.fantastlereboot.editor.MazeEditor;
import com.puttysoftware.fantastlereboot.game.GameManager;
import com.puttysoftware.fantastlereboot.items.Shop;
import com.puttysoftware.fantastlereboot.items.ShopTypes;
import com.puttysoftware.fantastlereboot.items.combat.CombatItemList;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.loaders.UserInterfaceImageLoader;
import com.puttysoftware.fantastlereboot.obsolete.maze1.MazeManager;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.MazeObjectList;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.updater.ProductData;

public class BagOStuff {
    // Fields
    private AboutDialog about;
    private GameManager gameMgr;
    private MazeManager mazeMgr;
    private MenuManager menuMgr;
    private PreferencesManager prefsMgr;
    private GeneralHelpManager gHelpMgr;
    private ObjectHelpManager oHelpMgr;
    private MazeEditor editor;
    private GUIManager guiMgr;
    private final MazeObjectList objects;
    private final CombatItemList combatItems;
    private Shop weapons, armor, healer, bank, regenerator, spells, items,
    socks, enhancements, faiths;
    private BufferedImageIcon microLogo;
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
    private static final ProductData pd = new ProductData(BagOStuff.UPDATE_SITE,
            BagOStuff.UPDATE_SITE, BagOStuff.NEW_VERSION_SITE,
            BagOStuff.RDNS_COMPANY_NAME, BagOStuff.COMPANY_NAME,
            BagOStuff.PRODUCT_NAME, BagOStuff.VERSION_MAJOR,
            BagOStuff.VERSION_MINOR, BagOStuff.VERSION_BUGFIX,
            BagOStuff.VERSION_CODE, BagOStuff.VERSION_PRERELEASE);
    private static final int VERSION_MAJOR = 5;
    private static final int VERSION_MINOR = 1;
    private static final int VERSION_BUGFIX = 0;
    private static final int VERSION_CODE = ProductData.CODE_BETA;
    private static final int VERSION_PRERELEASE = 2;
    public static final int STATUS_GUI = 0;
    public static final int STATUS_GAME = 1;
    public static final int STATUS_EDITOR = 2;
    public static final int STATUS_PREFS = 3;
    public static final int STATUS_BATTLE = 4;
    public static final int STATUS_NULL = 5;

    // Constructors
    public BagOStuff() {
        this.objects = new MazeObjectList();
        this.combatItems = new CombatItemList();
        this.currentMode = BagOStuff.STATUS_NULL;
        this.formerMode = BagOStuff.STATUS_NULL;
    }

    // Methods
    void postConstruct() {
        this.prefsMgr = new PreferencesManager();
        this.about = new AboutDialog(BagOStuff.getVersionString());
        this.guiMgr = new GUIManager();
        this.gameMgr = new GameManager();
        this.mazeMgr = new MazeManager();
        this.menuMgr = new MenuManager();
        this.gHelpMgr = new GeneralHelpManager();
        this.oHelpMgr = new ObjectHelpManager();
        this.windowTurnBattle = new WindowTurnBattleLogic();
        this.windowTimeBattle = new WindowTimeBattleLogic();
        this.mapTurnBattle = new MapTurnBattleLogic();
        this.mapTimeBattle = new MapTimeBattleLogic();
        this.editor = new MazeEditor();
        this.weapons = new Shop(ShopTypes.WEAPONS);
        this.armor = new Shop(ShopTypes.ARMOR);
        this.healer = new Shop(ShopTypes.HEALER);
        this.bank = new Shop(ShopTypes.BANK);
        this.regenerator = new Shop(ShopTypes.REGENERATOR);
        this.spells = new Shop(ShopTypes.SPELLS);
        this.items = new Shop(ShopTypes.ITEMS);
        this.socks = new Shop(ShopTypes.SOCKS);
        this.enhancements = new Shop(ShopTypes.ENHANCEMENTS);
        this.faiths = new Shop(ShopTypes.FAITH_POWERS);
        // Attempt to load extras
        final Object extras = PluginLoader.loadPlugin("ExtrasPlugin");
        PluginLoader.addPluginMenus(extras);
        // Cache Micro Logo
        this.microLogo = UserInterfaceImageLoader
                .load(GameUserInterfaceImage.MICRO_LOGO);
    }

    public void setInGUI(final boolean value) {
        this.formerMode = this.currentMode;
        this.currentMode = BagOStuff.STATUS_GUI;
    }

    public void setInPrefs(final boolean value) {
        this.formerMode = this.currentMode;
        this.currentMode = BagOStuff.STATUS_PREFS;
    }

    public void setInGame(final boolean value) {
        this.formerMode = this.currentMode;
        this.currentMode = BagOStuff.STATUS_GAME;
    }

    public void setInEditor() {
        this.formerMode = this.currentMode;
        this.currentMode = BagOStuff.STATUS_EDITOR;
    }

    public void setInBattle() {
        this.formerMode = this.currentMode;
        this.currentMode = BagOStuff.STATUS_BATTLE;
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

    public MenuManager getMenuManager() {
        return this.menuMgr;
    }

    public GUIManager getGUIManager() {
        return this.guiMgr;
    }

    public PreferencesManager getPrefsManager() {
        return this.prefsMgr;
    }

    public void resetPreferences() {
        this.prefsMgr.resetPrefs();
    }

    public GameManager getGameManager() {
        return this.gameMgr;
    }

    public MazeManager getMazeManager() {
        return this.mazeMgr;
    }

    public GeneralHelpManager getGeneralHelpManager() {
        return this.gHelpMgr;
    }

    public ObjectHelpManager getObjectHelpManager() {
        return this.oHelpMgr;
    }

    public void updateMicroLogo() {
        // Cache Micro Logo
        this.microLogo = UserInterfaceImageLoader
                .load(GameUserInterfaceImage.MICRO_LOGO);
    }

    public MazeEditor getEditor() {
        return this.editor;
    }

    public AboutDialog getAboutDialog() {
        return this.about;
    }

    public Shop getShop(final int shopType) {
        switch (shopType) {
        case ShopTypes.ARMOR:
            return this.armor;
        case ShopTypes.BANK:
            return this.bank;
        case ShopTypes.ENHANCEMENTS:
            return this.enhancements;
        case ShopTypes.FAITH_POWERS:
            return this.faiths;
        case ShopTypes.HEALER:
            return this.healer;
        case ShopTypes.ITEMS:
            return this.items;
        case ShopTypes.REGENERATOR:
            return this.regenerator;
        case ShopTypes.SOCKS:
            return this.socks;
        case ShopTypes.SPELLS:
            return this.spells;
        case ShopTypes.WEAPONS:
            return this.weapons;
        default:
            // Invalid shop type
            return null;
        }
    }

    public Shop getArmor() {
        return this.armor;
    }

    public Shop getBank() {
        return this.bank;
    }

    public Shop getHealer() {
        return this.healer;
    }

    public Shop getItems() {
        return this.items;
    }

    public Shop getRegenerator() {
        return this.regenerator;
    }

    public Shop getSpells() {
        return this.spells;
    }

    public Shop getWeapons() {
        return this.weapons;
    }

    public Battle getBattle() {
        PreferencesManager prefs = this.getPrefsManager();
        if (prefs.useMapBattleEngine()) {
            if (prefs.useTimeBattleEngine()) {
                return this.mapTimeBattle;
            } else {
                return this.mapTurnBattle;
            }
        } else {
            if (prefs.useTimeBattleEngine()) {
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

    public BufferedImageIcon getMicroLogo() {
        return this.microLogo;
    }

    public void playHighScoreSound() {
        if (this.prefsMgr.getSoundEnabled(PreferencesManager.SOUNDS_UI)) {
            SoundLoader.playSound(GameSound.HIGH_SCORE);
        }
    }

    public void playLogoSound() {
        if (this.prefsMgr.getSoundEnabled(PreferencesManager.SOUNDS_UI)) {
            SoundLoader.playSound(GameSound.LOGO);
        }
    }

    public void playStartSound() {
        if (this.prefsMgr.getSoundEnabled(PreferencesManager.SOUNDS_UI)) {
            SoundLoader.playSound(GameSound.GET_READY);
        }
    }

    private static String getVersionString() {
        final int code = pd.getCodeVersion();
        String rt;
        if (code < ProductData.CODE_STABLE) {
            rt = "-beta" + BagOStuff.VERSION_PRERELEASE;
        } else {
            rt = "";
        }
        return BagOStuff.VERSION_MAJOR + "." + BagOStuff.VERSION_MINOR + "."
                + BagOStuff.VERSION_BUGFIX + rt;
    }

    public JFrame getOutputFrame() {
        try {
            if (this.getMode() == BagOStuff.STATUS_PREFS) {
                return this.getPrefsManager().getPrefFrame();
            } else if (this.getMode() == BagOStuff.STATUS_BATTLE) {
                return this.getBattle().getOutputFrame();
            } else if (this.getMode() == BagOStuff.STATUS_GUI) {
                return this.getGUIManager().getGUIFrame();
            } else if (this.getMode() == BagOStuff.STATUS_GAME) {
                return this.getGameManager().getOutputFrame();
            } else {
                return this.getEditor().getOutputFrame();
            }
        } catch (final NullPointerException npe) {
            return null;
        }
    }

    public void showMessage(final String msg) {
        if (this.currentMode == BagOStuff.STATUS_GAME) {
            this.getGameManager().setStatusMessage(msg);
        } else if (this.currentMode == BagOStuff.STATUS_BATTLE) {
            this.getBattle().setStatusMessage(msg);
        } else {
            CommonDialogs.showDialog(msg);
        }
    }

    public MazeObjectList getObjects() {
        return this.objects;
    }

    public CombatItemList getCombatItems() {
        return this.combatItems;
    }

    public boolean isBetaModeEnabled() {
        return BagOStuff.VERSION_PRERELEASE > 0;
    }
}
