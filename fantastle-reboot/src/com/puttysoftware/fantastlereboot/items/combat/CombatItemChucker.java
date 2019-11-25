/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items.combat;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.items.ItemInventory;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;

public class CombatItemChucker {
  // Fields
  private static final CombatItemList COMBAT_ITEMS = new CombatItemList();

  // Private Constructor
  private CombatItemChucker() {
    // Do nothing
  }

  public static boolean selectAndUseItem(final Creature user) {
    boolean result = false;
    final CombatItem i = CombatItemChucker.selectItem(user);
    if (i != null) {
      result = CombatItemChucker.useItem(i, user);
    }
    return result;
  }

  public static boolean useItem(final CombatItem used, final Creature user) {
    if (used != null) {
      final Effect e = used.getEffect();
      // Play spell's associated sound effect, if it has one
      final SoundIndex snd = used.getSound();
      SoundPlayer.playSound(snd, SoundGroup.BATTLE);
      e.resetEffect();
      final Creature target = CombatItemChucker.resolveTarget(used,
          user.getTeamID());
      used.use();
      if (target.isEffectActive(e)) {
        target.extendEffect(e, e.getInitialRounds());
      } else {
        e.restoreEffect(target);
        target.applyEffect(e);
      }
      return true;
    } else {
      return false;
    }
  }

  private static Creature resolveTarget(final CombatItem cast,
      final int teamID) {
    final BattleTarget target = cast.getTarget();
    switch (target) {
    case SELF:
      return FantastleReboot.getBagOStuff().getBattle().getActive();
    case ENEMY:
      if (teamID == Creature.TEAM_PARTY) {
        return FantastleReboot.getBagOStuff().getBattle().pickTarget();
      } else {
        return FantastleReboot.getBagOStuff().getBattle().getEnemy(teamID);
      }
    default:
      return null;
    }
  }

  private static CombatItem selectItem(final Creature user) {
    final ItemInventory ii = user.getItems();
    if (ii != null) {
      final String[] names = ii.generateCombatUsableStringArray();
      final String[] displayNames = ii.generateCombatUsableDisplayStringArray();
      if (names != null && displayNames != null) {
        // Play casting spell sound
        SoundPlayer.playSound(SoundIndex.PARTY_SPELL, SoundGroup.BATTLE);
        String dialogResult = null;
        dialogResult = CommonDialogs.showInputDialog("Select an Item to Use",
            "Select Item", displayNames, displayNames[0]);
        if (dialogResult != null) {
          int index;
          for (index = 0; index < displayNames.length; index++) {
            if (dialogResult.equals(displayNames[index])) {
              break;
            }
          }
          final CombatItem i = CombatItemChucker.COMBAT_ITEMS
              .getItemByName(names[index]);
          if (ii.getUses(i) > 0) {
            return i;
          } else {
            CommonDialogs.showErrorDialog(
                "You try to use an item, but realize you've run out!",
                "Select Item");
            return null;
          }
        } else {
          return null;
        }
      } else {
        CommonDialogs.showErrorDialog(
            "You try to use an item, but realize you don't have any!",
            "Select Item");
        return null;
      }
    } else {
      CommonDialogs.showErrorDialog(
          "You try to use an item, but realize you don't have any!",
          "Select Item");
      return null;
    }
  }
}
