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

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.editor.MazeEditor;
import com.puttysoftware.fantastlereboot.game.GameManager;
import com.puttysoftware.fantastlereboot.generic.MazeObjectList;
import com.puttysoftware.fantastlereboot.items.Shop;
import com.puttysoftware.fantastlereboot.items.ShopTypes;
import com.puttysoftware.fantastlereboot.items.combat.CombatItemList;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.loaders.old.GraphicsManager;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.oldbattle.Battle;
import com.puttysoftware.fantastlereboot.oldbattle.BossBattle;
import com.puttysoftware.fantastlereboot.oldcreatures.Boss;
import com.puttysoftware.fantastlereboot.oldcreatures.PCManager;
import com.puttysoftware.images.BufferedImageIcon;

public class Application {
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
    private Shop weapons, armor, healer, bank, regenerator, spells, items;
    private Battle battle;
    private BossBattle bossBattle;
    private BufferedImageIcon microLogo;
    private boolean bossFlag;
    private boolean IN_GUI, IN_PREFS, IN_GAME;
    private static final int VERSION_MAJOR = 5;
    private static final int VERSION_MINOR = 0;
    private static final int VERSION_BUGFIX = 0;
    private static final int VERSION_BETA = 5;
    public static final int STATUS_GUI = 0;
    public static final int STATUS_GAME = 1;
    public static final int STATUS_EDITOR = 2;
    public static final int STATUS_PREFS = 3;

    // Constructors
    public Application() {
        this.objects = new MazeObjectList();
        this.combatItems = new CombatItemList();
    }

    // Methods
    void postConstruct() {
        this.prefsMgr = new PreferencesManager();
        this.about = new AboutDialog(this.getVersionString());
        this.guiMgr = new GUIManager();
        this.gameMgr = new GameManager();
        this.mazeMgr = new MazeManager();
        this.menuMgr = new MenuManager();
        this.gHelpMgr = new GeneralHelpManager();
        this.oHelpMgr = new ObjectHelpManager();
        this.editor = new MazeEditor();
        this.weapons = new Shop(ShopTypes.SHOP_TYPE_WEAPONS);
        this.armor = new Shop(ShopTypes.SHOP_TYPE_ARMOR);
        this.healer = new Shop(ShopTypes.SHOP_TYPE_HEALER);
        this.bank = new Shop(ShopTypes.SHOP_TYPE_BANK);
        this.regenerator = new Shop(ShopTypes.SHOP_TYPE_REGENERATOR);
        this.spells = new Shop(ShopTypes.SHOP_TYPE_SPELLS);
        this.items = new Shop(ShopTypes.SHOP_TYPE_ITEMS);
        this.battle = new Battle();
        this.bossBattle = new BossBattle();
        this.bossFlag = false;
        // Attempt to load extras
        final Object extras = PluginLoader.loadPlugin("ExtrasPlugin");
        PluginLoader.addPluginMenus(extras);
        // Cache Micro Logo
        this.microLogo = GraphicsManager.getMicroLogo();
    }

    public void setInGUI(final boolean value) {
        this.IN_GUI = value;
    }

    public void setInPrefs(final boolean value) {
        this.IN_PREFS = value;
    }

    public void setInGame(final boolean value) {
        this.IN_GAME = value;
    }

    public int getMode() {
        if (this.IN_PREFS) {
            return Application.STATUS_PREFS;
        } else if (this.IN_GUI) {
            return Application.STATUS_GUI;
        } else if (this.IN_GAME) {
            return Application.STATUS_GAME;
        } else {
            return Application.STATUS_EDITOR;
        }
    }

    public int getFormerMode() {
        if (this.IN_GUI) {
            return Application.STATUS_GUI;
        } else if (this.IN_GAME) {
            return Application.STATUS_GAME;
        } else {
            return Application.STATUS_EDITOR;
        }
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
        this.microLogo = GraphicsManager.getMicroLogo();
    }

    public MazeEditor getEditor() {
        return this.editor;
    }

    public AboutDialog getAboutDialog() {
        return this.about;
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
        if (PCManager.getPlayer().getLevel() == Boss.FIGHT_LEVEL) {
            if (!this.bossFlag) {
                this.battle.battleDone();
                this.bossFlag = true;
            }
            return this.bossBattle;
        } else {
            this.bossFlag = false;
            return this.battle;
        }
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

    private String getVersionString() {
        if (this.isBetaModeEnabled()) {
            return "" + Application.VERSION_MAJOR + "."
                    + Application.VERSION_MINOR + "."
                    + Application.VERSION_BUGFIX + "-dev"
                    + Application.VERSION_BETA;
        } else {
            return "" + Application.VERSION_MAJOR + "."
                    + Application.VERSION_MINOR + "."
                    + Application.VERSION_BUGFIX;
        }
    }

    public JFrame getOutputFrame() {
        try {
            if (this.getMode() == Application.STATUS_PREFS) {
                return this.getPrefsManager().getPrefFrame();
            } else if (Battle.isInBattle()) {
                return this.getBattle().getBattleFrame();
            } else if (this.getMode() == Application.STATUS_GUI) {
                return this.getGUIManager().getGUIFrame();
            } else if (this.getMode() == Application.STATUS_GAME) {
                return this.getGameManager().getOutputFrame();
            } else {
                return this.getEditor().getOutputFrame();
            }
        } catch (final NullPointerException npe) {
            return null;
        }
    }

    public MazeObjectList getObjects() {
        return this.objects;
    }

    public CombatItemList getCombatItems() {
        return this.combatItems;
    }

    public boolean isBetaModeEnabled() {
        return Application.VERSION_BETA > 0;
    }
}
