/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttspells;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.AbstractCreature;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.effects.TTEffect;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;

public class SpellCaster {
    // Fields
    private static boolean NO_SPELLS_FLAG = false;

    // Private Constructor
    private SpellCaster() {
        // Do nothing
    }

    public static boolean selectAndCastSpell(final AbstractCreature caster) {
        boolean result = false;
        SpellCaster.NO_SPELLS_FLAG = false;
        final Spell s = SpellCaster.selectSpell(caster);
        if (s != null) {
            result = SpellCaster.castSpell(s, caster);
            if (!result && !SpellCaster.NO_SPELLS_FLAG) {
                CommonDialogs
                        .showErrorDialog(
                                "You try to cast a spell, but realize you don't have enough MP!",
                                "Select Spell");
            }
        }
        return result;
    }

    public static boolean castSpell(final Spell cast,
            final AbstractCreature caster) {
        if (cast != null) {
            final int casterMP = caster.getCurrentMP();
            final int cost = cast.getCost();
            if (casterMP >= cost) {
                // Cast Spell
                caster.drain(cost);
                final TTEffect b = cast.getEffect();
                // Play spell's associated sound effect, if it has one
                final GameSound snd = cast.getSound();
                SoundLoader.playSound(snd);
                b.resetEffect();
                final AbstractCreature target = SpellCaster.resolveTarget(cast,
                        caster.getTeamID());
                if (target.isEffectActive(b)) {
                    target.extendEffect(b, b.getInitialRounds());
                } else {
                    b.restoreEffect();
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

    private static Spell selectSpell(final AbstractCreature caster) {
        final SpellBook book = caster.getSpellBook();
        if (book != null) {
            final String[] names = book.getAllSpellNames();
            final String[] displayNames = book.getAllSpellNamesWithCosts();
            if (names != null && displayNames != null) {
                // Play casting spell sound
                SoundLoader.playSound(GameSound.PARTY_SPELL);
                String dialogResult = null;
                dialogResult = CommonDialogs.showInputDialog(
                        "Select a Spell to Cast", "Select Spell", displayNames,
                        displayNames[0]);
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
                CommonDialogs
                        .showErrorDialog(
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

    private static AbstractCreature resolveTarget(final Spell cast,
            final int teamID) {
        final BattleTarget target = cast.getTarget();
        switch (target) {
        case SELF:
            if (teamID == AbstractCreature.TEAM_PARTY) {
                return PartyManager.getParty().getLeader();
            } else {
                return TallerTower.getApplication().getBattle().getEnemy();
            }
        case ENEMY:
            if (teamID == AbstractCreature.TEAM_PARTY) {
                return TallerTower.getApplication().getBattle().getEnemy();
            } else {
                return PartyManager.getParty().getLeader();
            }
        default:
            return null;
        }
    }
}
