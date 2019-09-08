/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.effects.TTEffect;
import com.puttysoftware.fantastlereboot.ttspells.Spell;
import com.puttysoftware.fantastlereboot.ttspells.SpellBook;

class SystemMonsterSpellBook extends SpellBook {
    // Constructor
    public SystemMonsterSpellBook() {
        super(6, false);
        this.setName("System Monster Spell Book");
    }

    @Override
    protected void defineSpells() {
        final TTEffect spell0Effect = new TTEffect("Poisoned", 3);
        spell0Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_CURRENT_HP, 0.8);
        spell0Effect.setDecayRate(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_CURRENT_HP, 0.875);
        spell0Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "The enemy breathes poisonous breath at you!");
        spell0Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "You lose some health from being poisoned!");
        spell0Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF,
                "You are no longer poisoned!");
        final Spell spell0 = new Spell(spell0Effect, 1, BattleTarget.ENEMY,
                GameSound.SLIME);
        this.spells[0] = spell0;
        final TTEffect spell1Effect = new TTEffect("Recover", 1);
        spell1Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, 15);
        spell1Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "The enemy applies a bandage to its wounds!");
        spell1Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy regains some health!");
        final Spell spell1 = new Spell(spell1Effect, 2, BattleTarget.SELF,
                GameSound.HEAL);
        this.spells[1] = spell1;
        final TTEffect spell2Effect = new TTEffect("Weapon Drain", 5);
        spell2Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_ATTACK, 0.8, TTEffect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE);
        spell2Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "The enemy drains your weapon of some of its power!");
        spell2Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "Your attack is decreased!");
        spell2Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF,
                "Your weapon's power has returned!");
        final Spell spell2 = new Spell(spell2Effect, 3, BattleTarget.ENEMY,
                GameSound.DRAIN);
        this.spells[2] = spell2;
        final TTEffect spell3Effect = new TTEffect("Armor Drain", 5);
        spell3Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_DEFENSE, 0.8, TTEffect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE);
        spell3Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "The enemy drains your armor of some of its power!");
        spell3Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "Your defense is decreased!");
        spell3Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF,
                "Your armor's power has returned!");
        final Spell spell3 = new Spell(spell3Effect, 5, BattleTarget.ENEMY,
                GameSound.DRAIN);
        this.spells[3] = spell3;
        final TTEffect spell4Effect = new TTEffect("Weapon Charge", 5);
        spell4Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_ATTACK, 1.25, TTEffect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE);
        spell4Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "The enemy charges its weapon with power!");
        spell4Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy's attack is increased!");
        spell4Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF,
                "The enemy's weapon returns to normal!");
        final Spell spell4 = new Spell(spell4Effect, 7, BattleTarget.SELF,
                GameSound.BUFF_2);
        this.spells[4] = spell4;
        final TTEffect spell5Effect = new TTEffect("Armor Charge", 5);
        spell5Effect.setEffect(TTEffect.EFFECT_MULTIPLY,
                StatConstants.STAT_DEFENSE, 1.25, TTEffect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE);
        spell5Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "The enemy charges its armor with power!");
        spell5Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy's defense is increased!");
        spell5Effect.setMessage(TTEffect.MESSAGE_WEAR_OFF,
                "The enemy's armor returns to normal!");
        final Spell spell5 = new Spell(spell5Effect, 11, BattleTarget.SELF,
                GameSound.BUFF_1);
        this.spells[5] = spell5;
    }
}
