package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.effects.DamageEffect;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.effects.HealingEffect;

public class BossSpellBook extends SpellBook {
    // Constructor
    public BossSpellBook() {
        super(6, true);
    }

    @Override
    protected void defineSpells() {
        final DamageEffect spell0Effect = new DamageEffect("Lethally Poisoned",
                10, 10, Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE,
                1);
        spell0Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The Boss breathes extremely poisonous breath at you!");
        spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "You lose a LOT of health from being poisoned!");
        spell0Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "You are no longer poisoned!");
        final Spell spell0 = new Spell(spell0Effect, 4, BattleTarget.ENEMY);
        this.addKnownSpell(spell0);
        final HealingEffect spell1Effect = new HealingEffect("Ultra Recover",
                100, 1, Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE,
                Effect.DEFAULT_DECAY_RATE);
        spell1Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The Boss applies a very large bandage to its wounds!");
        spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The Boss regains a LOT of health!");
        final Spell spell1 = new Spell(spell1Effect, 4, BattleTarget.SELF);
        this.addKnownSpell(spell1);
        final Effect spell2Effect = new Effect("Ultra Weapon Drain", 10);
        spell2Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell2Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell2Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The Boss drains your weapon of half of its power!");
        spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your attack is significantly decreased!");
        spell2Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your weapon's power has returned!");
        final Spell spell2 = new Spell(spell2Effect, 8, BattleTarget.ENEMY);
        this.addKnownSpell(spell2);
        final Effect spell3Effect = new Effect("Ultra Armor Drain", 10);
        spell3Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell3Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell3Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The Boss drains your armor of half of its power!");
        spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your defense is significantly decreased!");
        spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your armor's power has returned!");
        final Spell spell3 = new Spell(spell3Effect, 8, BattleTarget.ENEMY);
        this.addKnownSpell(spell3);
        final Effect spell4Effect = new Effect("Ultra Weapon Charge", 10);
        spell4Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell4Effect.setEffect(Effect.EFFECT_MULTIPLY, 2,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell4Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The Boss charges its weapon with godlike power!");
        spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The Boss's attack is drastically increased!");
        spell4Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The Boss's weapon returns to normal!");
        final Spell spell4 = new Spell(spell4Effect, 16, BattleTarget.SELF);
        this.addKnownSpell(spell4);
        final Effect spell5Effect = new Effect("Ultra Armor Charge", 10);
        spell5Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell5Effect.setEffect(Effect.EFFECT_MULTIPLY, 2,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell5Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The Boss charges its armor with godlike power!");
        spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The Boss's defense is drastically increased!");
        spell5Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The Boss's armor returns to normal!");
        final Spell spell5 = new Spell(spell5Effect, 16, BattleTarget.SELF);
        this.addKnownSpell(spell5);
    }

    @Override
    public int getID() {
        return 5;
    }
}
