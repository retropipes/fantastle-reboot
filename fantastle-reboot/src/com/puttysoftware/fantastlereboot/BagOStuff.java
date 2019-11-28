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

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.battle.map.MapBattleLogic;
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
  private GUIManager guiMgr;
  private final CombatItemList combatItems;
  private Shop weapons, armor, healer, bank, regenerator, spells, items, socks,
      enhancements, faiths;
  private MapBattleLogic mapTurnBattle;
  private static final String UPDATE_SITE = "https://puttysoftware.com/updater/fantastle-reboot/";
  private static final String NEW_VERSION_SITE = "https://puttysoftware.github.com/fantastle-reboot/";
  private static final String PRODUCT_NAME = "FantastleReboot";
  private static final String COMPANY_NAME = "Putty Software";
  private static final String RDNS_COMPANY_NAME = "com.puttysoftware.fantastlereboot";
  private static final ProductData pd = new ProductData(BagOStuff.UPDATE_SITE,
      BagOStuff.UPDATE_SITE, BagOStuff.NEW_VERSION_SITE,
      BagOStuff.RDNS_COMPANY_NAME, BagOStuff.COMPANY_NAME,
      BagOStuff.PRODUCT_NAME, BagOStuff.VERSION_MAJOR, BagOStuff.VERSION_MINOR,
      BagOStuff.VERSION_BUGFIX, BagOStuff.VERSION_CODE,
      BagOStuff.VERSION_PRERELEASE);
  private static final int VERSION_MAJOR = 0;
  private static final int VERSION_MINOR = 4;
  private static final int VERSION_BUGFIX = 0;
  private static final int VERSION_CODE = ProductData.CODE_ALPHA;
  private static final int VERSION_PRERELEASE = 0;

  // Constructors
  public BagOStuff() {
    this.combatItems = new CombatItemList();
  }

  // Methods
  void postConstruct() {
    this.about = new AboutDialog(BagOStuff.getVersionString());
    this.guiMgr = new GUIManager();
    this.gHelpMgr = new GeneralHelpManager();
    this.mapTurnBattle = new MapBattleLogic();
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
    return this.mapTurnBattle;
  }

  public void resetBattleGUI() {
    this.mapTurnBattle.resetGUI();
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
    return BagOStuff.pd.getVersionString();
  }

  public void showMessage(final String msg) {
    if (Modes.inGame()) {
      Game.setStatusMessage(msg);
    } else if (Modes.inBattle()) {
      this.getBattle().setStatusMessage(msg);
    } else {
      CommonDialogs.showDialog(msg);
    }
  }

  public CombatItemList getCombatItems() {
    return this.combatItems;
  }
}
