/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;

public class SpellCaster {
  // Fields
  private static boolean NO_SPELLS_FLAG = false;

  // Private Constructor
  private SpellCaster() {
    // Do nothing
  }

  public static boolean selectAndCastSpell(final Creature caster) {
    boolean result = false;
    SpellCaster.NO_SPELLS_FLAG = false;
    final Spell s = SpellCaster.selectSpell(caster);
    if (s != null) {
      result = SpellCaster.castSpell(s, caster);
      if (!result && !SpellCaster.NO_SPELLS_FLAG) {
        CommonDialogs.showErrorDialog(
            "You try to cast a spell, but realize you don't have enough MP!",
            "Select Spell");
      }
    }
    return result;
  }

  public static boolean castSpell(final Spell cast, final Creature caster) {
    if (cast != null) {
      final int casterMP = caster.getCurrentMP();
      final int cost = cast.getCost();
      if (casterMP >= cost) {
        // Cast Spell
        caster.drain(cost);
        final Effect b = cast.getEffect();
        // Play spell's associated sound effect, if it has one
        final SoundIndex snd = cast.getSound();
        SoundPlayer.playSound(snd, SoundGroup.BATTLE);
        b.resetEffect();
        final Creature target = SpellCaster.resolveTarget(cast,
            caster.getTeamID());
        if (target.isEffectActive(b)) {
          target.extendEffect(b, b.getInitialRounds());
        } else {
          b.restoreEffect(target);
          target.applyEffect(b);
        }
        return true;
      } else {
        // Not enough MP
        return false;
      }
    } else {
      return false;
    }
  }

  private static Spell selectSpell(final Creature caster) {
    final SpellBook book = caster.getSpellBook();
    if (book != null) {
      final String[] names = book.getAllSpellNames();
      final String[] displayNames = book.getAllSpellNamesWithCosts();
      if (names != null && displayNames != null) {
        // Play casting spell sound
        SoundPlayer.playSound(SoundIndex.PARTY_SPELL, SoundGroup.BATTLE);
        String dialogResult = null;
        dialogResult = CommonDialogs.showInputDialog("Select a Spell to Cast",
            "Select Spell", displayNames, displayNames[0]);
        if (dialogResult != null) {
          int index;
          for (index = 0; index < displayNames.length; index++) {
            if (dialogResult.equals(displayNames[index])) {
              break;
            }
          }
          return book.getSpellByName(names[index]);
        } else {
          return null;
        }
      } else {
        SpellCaster.NO_SPELLS_FLAG = true;
        CommonDialogs.showErrorDialog(
            "You try to cast a spell, but realize you don't know any!",
            "Select Spell");
        return null;
      }
    } else {
      SpellCaster.NO_SPELLS_FLAG = true;
      CommonDialogs.showErrorDialog(
          "You try to cast a spell, but realize you don't know any!",
          "Select Spell");
      return null;
    }
  }

  private static Creature resolveTarget(final Spell cast, final int teamID) {
    final BattleTarget target = cast.getTarget();
    switch (target) {
    case SELF:
      if (teamID == Creature.TEAM_PARTY) {
        return PartyManager.getParty().getLeader();
      } else {
        return FantastleReboot.getBagOStuff().getBattle().getEnemy();
      }
    case ENEMY:
      if (teamID == Creature.TEAM_PARTY) {
        return FantastleReboot.getBagOStuff().getBattle().getEnemy();
      } else {
        return PartyManager.getParty().getLeader();
      }
    default:
      return null;
    }
  }
}
