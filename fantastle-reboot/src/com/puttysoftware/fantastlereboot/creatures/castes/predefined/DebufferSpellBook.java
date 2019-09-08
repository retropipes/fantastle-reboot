/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.castes.predefined;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.effects.TTEffect;
import com.puttysoftware.fantastlereboot.ttspells.Spell;
import com.puttysoftware.fantastlereboot.ttspells.SpellBook;

public class DebufferSpellBook extends SpellBook {
    // Constructor
    public DebufferSpellBook() {
        super(8, false);
        this.setName(CasteConstants.CASTE_NAMES[this.getLegacyID()]);
    }

    @Override
    protected void defineSpells() {
        final TTEffect spell0Effect = new TTEffect("Damage Lock", 5);
        spell0Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -1.5);
        spell0Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You perpetrate some locksmithery on your enemy!");
        spell0Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy recoils, taking a little damage!");
        spell0Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF, "The enemy recovers!");
        final Spell spell0 = new Spell(spell0Effect, 1, BattleTarget.ENEMY,
                GameSound.UNLOCK);
        this.spells[0] = spell0;
        final TTEffect spell1Effect = new TTEffect("Speed Down", 5);
        spell1Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_AGILITY, 0.5, TTEffect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE);
        spell1Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You take out a whip, and tangle the enemy with it!");
        spell1Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy's speed is reduced!");
        spell1Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF,
                "The enemy breaks free of the tangle!");
        final Spell spell1 = new Spell(spell1Effect, 2, BattleTarget.ENEMY,
                GameSound.DEBUFF_1);
        this.spells[1] = spell1;
        final TTEffect spell2Effect = new TTEffect("Power Lock", 5);
        spell2Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -10);
        spell2Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You lock your enemy into a damage trap!");
        spell2Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy recoils, taking damage!");
        spell2Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF, "The trap vanishes!");
        final Spell spell2 = new Spell(spell2Effect, 3, BattleTarget.ENEMY,
                GameSound.UNLOCK);
        this.spells[2] = spell2;
        final TTEffect spell3Effect = new TTEffect("Attack Lock", 10);
        spell3Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_ATTACK, 0, TTEffect.DEFAULT_SCALE_FACTOR,
                TTEffect.DEFAULT_SCALE_STAT);
        spell3Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You lock your enemy's weapon!");
        spell3Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy cannot attack!");
        spell3Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF, "The lock breaks!");
        final Spell spell3 = new Spell(spell3Effect, 5, BattleTarget.ENEMY,
                GameSound.UNLOCK);
        this.spells[3] = spell3;
        final TTEffect spell4Effect = new TTEffect("Weapon Steal", 5);
        spell4Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_ATTACK, 0.5, TTEffect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE);
        spell4Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You steal the enemy's weapon!");
        spell4Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy's attack is significantly reduced!");
        spell4Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF,
                "The enemy recovers their weapon!");
        final Spell spell4 = new Spell(spell4Effect, 7, BattleTarget.ENEMY,
                GameSound.DEBUFF_1);
        this.spells[4] = spell4;
        final TTEffect spell5Effect = new TTEffect("Armor Bind", 5);
        spell5Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_DEFENSE, 0, TTEffect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE);
        spell5Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You bind the enemy's armor, rendering it useless!");
        spell5Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy is unable to defend!");
        spell5Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF, "The binding breaks!");
        final Spell spell5 = new Spell(spell5Effect, 11, BattleTarget.ENEMY,
                GameSound.DEBUFF_1);
        this.spells[5] = spell5;
        final TTEffect spell6Effect = new TTEffect("Killer Poison", 10);
        spell6Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -5, TTEffect.DEFAULT_SCALE_FACTOR,
                TTEffect.DEFAULT_SCALE_STAT);
        spell6Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You profusely poison the enemy!");
        spell6Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy is badly hurt by the poison!");
        spell6Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF, "The poison fades!");
        final Spell spell6 = new Spell(spell6Effect, 13, BattleTarget.ENEMY,
                GameSound.UNLOCK);
        this.spells[6] = spell6;
        final TTEffect spell7Effect = new TTEffect("Blindness", 10);
        spell7Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_EVADE, 0, TTEffect.DEFAULT_SCALE_FACTOR,
                TTEffect.DEFAULT_SCALE_STAT);
        spell7Effect.setMessage(TTEffect.MESSAGE_INITIAL, "You blind an enemy!");
        spell7Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy cannot dodge attacks!");
        spell7Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF,
                "The enemy's vision returns to normal!");
        final Spell spell7 = new Spell(spell7Effect, 17, BattleTarget.ENEMY,
                GameSound.UNLOCK);
        this.spells[7] = spell7;
    }

    @Override
    public int getLegacyID() {
        return CasteConstants.CASTE_DEBUFFER;
    }
}
