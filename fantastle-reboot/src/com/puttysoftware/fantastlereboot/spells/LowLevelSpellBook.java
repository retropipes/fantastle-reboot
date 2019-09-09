package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.effects.DamageEffect;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.effects.HealingEffect;

public class LowLevelSpellBook extends SpellBook {
    // Constructor
    public LowLevelSpellBook() {
        super(6, true);
    }

    @Override
    protected void defineSpells() {
        final DamageEffect spell0Effect = new DamageEffect("Slightly Poisoned",
                1, 3, Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE,
                Effect.DEFAULT_DECAY_RATE);
        spell0Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy breathes slightly poisonous breath at you!");
        spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "You lose a little health from being poisoned!");
        spell0Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "You are no longer poisoned!");
        final Spell spell0 = new Spell(spell0Effect, 1, BattleTarget.ENEMY);
        this.addKnownSpell(spell0);
        final HealingEffect spell1Effect = new HealingEffect("Minor Recover", 3,
                1, Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE,
                Effect.DEFAULT_DECAY_RATE);
        spell1Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy applies a small bandage to its wounds!");
        spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy regains a little health!");
        final Spell spell1 = new Spell(spell1Effect, 1, BattleTarget.SELF);
        this.addKnownSpell(spell1);
        final Effect spell2Effect = new Effect("Minor Weapon Drain", 3);
        spell2Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell2Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.9,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell2Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy drains your weapon of a little of its power!");
        spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your attack is slightly decreased!");
        spell2Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your weapon's power has returned!");
        final Spell spell2 = new Spell(spell2Effect, 2, BattleTarget.ENEMY);
        this.addKnownSpell(spell2);
        final Effect spell3Effect = new Effect("Minor Armor Drain", 3);
        spell3Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell3Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.9,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell3Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy drains your armor of a little of its power!");
        spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your defense is slightly decreased!");
        spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your armor's power has returned!");
        final Spell spell3 = new Spell(spell3Effect, 2, BattleTarget.ENEMY);
        this.addKnownSpell(spell3);
        final Effect spell4Effect = new Effect("Minor Weapon Charge", 3);
        spell4Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell4Effect.setEffect(Effect.EFFECT_MULTIPLY, 1.1,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell4Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy charges its weapon with power!");
        spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy's attack is slightly increased!");
        spell4Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy's weapon returns to normal!");
        final Spell spell4 = new Spell(spell4Effect, 4, BattleTarget.SELF);
        this.addKnownSpell(spell4);
        final Effect spell5Effect = new Effect("Minor Armor Charge", 3);
        spell5Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell5Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.9,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell5Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy charges its armor with power!");
        spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy's defense is slightly increased!");
        spell5Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy's armor returns to normal!");
        final Spell spell5 = new Spell(spell5Effect, 4, BattleTarget.SELF);
        this.addKnownSpell(spell5);
    }

    @Override
    public int getID() {
        return 1;
    }
}
