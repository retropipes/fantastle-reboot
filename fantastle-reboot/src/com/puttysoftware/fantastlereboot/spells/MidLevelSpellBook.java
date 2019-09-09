package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.effects.DamageEffect;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.effects.HealingEffect;

public class MidLevelSpellBook extends SpellBook {
    // Constructor
    public MidLevelSpellBook() {
        super(6, true);
    }

    @Override
    protected void defineSpells() {
        final DamageEffect spell0Effect = new DamageEffect("Poisoned", 3, 5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE, 0.5);
        spell0Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy breathes poisonous breath at you!");
        spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "You lose some health from being poisoned!");
        spell0Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "You are no longer poisoned!");
        final Spell spell0 = new Spell(spell0Effect, 2, BattleTarget.ENEMY);
        this.addKnownSpell(spell0);
        final HealingEffect spell1Effect = new HealingEffect("Recover", 15, 1,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE,
                Effect.DEFAULT_DECAY_RATE);
        spell1Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy applies a bandage to its wounds!");
        spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy regains some health!");
        final Spell spell1 = new Spell(spell1Effect, 2, BattleTarget.SELF);
        this.addKnownSpell(spell1);
        final Effect spell2Effect = new Effect("Weapon Drain", 5);
        spell2Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell2Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.8,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell2Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy drains your weapon of some of its power!");
        spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your attack is decreased!");
        spell2Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your weapon's power has returned!");
        final Spell spell2 = new Spell(spell2Effect, 4, BattleTarget.ENEMY);
        this.addKnownSpell(spell2);
        final Effect spell3Effect = new Effect("Armor Drain", 5);
        spell3Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell3Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.8,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell3Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy drains your armor of some of its power!");
        spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your defense is decreased!");
        spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your armor's power has returned!");
        final Spell spell3 = new Spell(spell3Effect, 4, BattleTarget.ENEMY);
        this.addKnownSpell(spell3);
        final Effect spell4Effect = new Effect("Weapon Charge", 5);
        spell4Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell4Effect.setEffect(Effect.EFFECT_MULTIPLY, 1.25,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell4Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy charges its weapon with power!");
        spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy's attack is increased!");
        spell4Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy's weapon returns to normal!");
        final Spell spell4 = new Spell(spell4Effect, 8, BattleTarget.SELF);
        this.addKnownSpell(spell4);
        final Effect spell5Effect = new Effect("Armor Charge", 5);
        spell5Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell5Effect.setEffect(Effect.EFFECT_MULTIPLY, 1.25,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell5Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy charges its armor with power!");
        spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy's defense is increased!");
        spell5Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy's armor returns to normal!");
        final Spell spell5 = new Spell(spell5Effect, 8, BattleTarget.SELF);
        this.addKnownSpell(spell5);
    }

    @Override
    public int getID() {
        return 2;
    }
}
