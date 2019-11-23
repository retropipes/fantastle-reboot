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

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.battle.map.time.MapTimeBattleLogic;
import com.puttysoftware.fantastlereboot.battle.map.turn.MapTurnBattleLogic;
import com.puttysoftware.fantastlereboot.battle.window.time.WindowTimeBattleLogic;
import com.puttysoftware.fantastlereboot.battle.window.turn.WindowTurnBattleLogic;
import com.puttysoftware.fantastlereboot.editor.MazeEditor;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.AboutDialog;
import com.puttysoftware.fantastlereboot.gui.GUIManager;
import com.puttysoftware.fantastlereboot.gui.GeneralHelpManager;
import com.puttysoftware.fantastlereboot.gui.MenuManager;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.items.Shop;
import com.puttysoftware.fantastlereboot.items.ShopTypes;
import com.puttysoftware.fantastlereboot.items.combat.CombatItemList;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.updater.ProductData;

public class BagOStuff {
  // Fields
  private AboutDialog about;
  private GeneralHelpManager gHelpMgr;
  private MazeEditor editor;
  private GUIManager guiMgr;
  private final CombatItemList combatItems;
  private Shop weapons, armor, healer, bank, regenerator, spells, items, socks,
      enhancements, faiths;
  private WindowTurnBattleLogic windowTurnBattle;
  private WindowTimeBattleLogic windowTimeBattle;
  private MapTurnBattleLogic mapTurnBattle;
  private MapTimeBattleLogic mapTimeBattle;
  private int currentMode;
  private int formerMode;
  private static final String UPDATE_SITE = "http://update.puttysoftware.com/tallertower/";
  private static final String NEW_VERSION_SITE = "http://www.puttysoftware.com/tallertower/";
  private static final String PRODUCT_NAME = "FantastleReboot";
  private static final String COMPANY_NAME = "Putty Software";
  private static final String RDNS_COMPANY_NAME = "com.puttysoftware.tallertower";
  private static final ProductData pd = new ProductData(BagOStuff.UPDATE_SITE,
      BagOStuff.UPDATE_SITE, BagOStuff.NEW_VERSION_SITE,
      BagOStuff.RDNS_COMPANY_NAME, BagOStuff.COMPANY_NAME,
      BagOStuff.PRODUCT_NAME, BagOStuff.VERSION_MAJOR, BagOStuff.VERSION_MINOR,
      BagOStuff.VERSION_BUGFIX, BagOStuff.VERSION_CODE,
      BagOStuff.VERSION_PRERELEASE);
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
  public static final int STATUS_ABOUT = 5;
  public static final int STATUS_HELP = 6;
  public static final int STATUS_NULL = 7;

  // Constructors
  public BagOStuff() {
    this.combatItems = new CombatItemList();
    this.currentMode = BagOStuff.STATUS_NULL;
    this.formerMode = BagOStuff.STATUS_NULL;
  }

  // Methods
  void postConstruct() {
    this.about = new AboutDialog(BagOStuff.getVersionString());
    this.guiMgr = new GUIManager();
    this.gHelpMgr = new GeneralHelpManager();
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
  }

  public void setInGUI() {
    this.formerMode = this.currentMode;
    this.currentMode = BagOStuff.STATUS_GUI;
  }

  public void setInPrefs() {
    this.formerMode = this.currentMode;
    this.currentMode = BagOStuff.STATUS_PREFS;
  }

  public void setInGame() {
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

  public void setInAbout() {
    this.formerMode = this.currentMode;
    this.currentMode = BagOStuff.STATUS_ABOUT;
  }

  public void setInHelp() {
    this.formerMode = this.currentMode;
    this.currentMode = BagOStuff.STATUS_HELP;
  }

  public boolean inBattle() {
    return this.currentMode == BagOStuff.STATUS_BATTLE;
  }

  public int getMode() {
    return this.currentMode;
  }

  public int getFormerMode() {
    return this.formerMode;
  }

  public MenuManager getMenuManager() {
    return FantastleReboot.getMenuManager();
  }

  public GUIManager getGUIManager() {
    return this.guiMgr;
  }

  public void resetPreferences() {
    Prefs.resetPrefs();
  }

  public GeneralHelpManager getGeneralHelpManager() {
    return this.gHelpMgr;
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
    if (Prefs.useMapBattleEngine()) {
      if (Prefs.useTimeBattleEngine()) {
        return this.mapTimeBattle;
      } else {
        return this.mapTurnBattle;
      }
    } else {
      if (Prefs.useTimeBattleEngine()) {
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

  public void playHighScoreSound() {
    SoundPlayer.playSound(SoundIndex.HIGH_SCORE, SoundGroup.USER_INTERFACE);
  }

  public void playLogoSound() {
    SoundPlayer.playSound(SoundIndex.LOGO, SoundGroup.USER_INTERFACE);
  }

  public void playStartSound() {
    SoundPlayer.playSound(SoundIndex.GET_READY, SoundGroup.USER_INTERFACE);
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

  public void restoreFormerMode() {
    this.currentMode = this.formerMode;
    switch (this.currentMode) {
    case STATUS_GUI:
      this.guiMgr.showGUI();
      break;
    case STATUS_GAME:
      Game.playDungeonMusic();
      break;
    case STATUS_EDITOR:
      this.editor.showOutput();
      break;
    case STATUS_PREFS:
      Prefs.showPrefs();
      break;
    case STATUS_BATTLE:
      this.getBattle().showBattle();
      break;
    case STATUS_ABOUT:
      this.about.showAboutDialog();
      break;
    case STATUS_HELP:
      this.gHelpMgr.showHelp();
      break;
    default:
      break;
    }
  }

  public void showMessage(final String msg) {
    if (this.currentMode == BagOStuff.STATUS_GAME) {
      Game.setStatusMessage(msg);
    } else if (this.currentMode == BagOStuff.STATUS_BATTLE) {
      this.getBattle().setStatusMessage(msg);
    } else {
      CommonDialogs.showDialog(msg);
    }
  }

  public CombatItemList getCombatItems() {
    return this.combatItems;
  }

  public static boolean isBetaModeEnabled() {
    return BagOStuff.VERSION_PRERELEASE > 0;
  }
}
