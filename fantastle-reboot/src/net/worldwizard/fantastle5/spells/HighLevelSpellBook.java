package net.worldwizard.fantastle5.spells;

import net.worldwizard.fantastle5.creatures.StatConstants;
import net.worldwizard.fantastle5.effects.DamageEffect;
import net.worldwizard.fantastle5.effects.Effect;
import net.worldwizard.fantastle5.effects.HealingEffect;

public class HighLevelSpellBook extends SpellBook {
    // Constructor
    public HighLevelSpellBook() {
        super(6, true);
    }

    @Override
    protected void defineSpells() {
        final DamageEffect spell0Effect = new DamageEffect(
                "Dangerously Poisoned", 5, 8, Effect.DEFAULT_SCALE_FACTOR,
                StatConstants.STAT_NONE, 0.5);
        spell0Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy breathes highly poisonous breath at you!");
        spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "You lose quite a bit of health from being poisoned!");
        spell0Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "You are no longer poisoned!");
        final Spell spell0 = new Spell(spell0Effect, 3, 'P');
        this.spells[0] = spell0;
        final HealingEffect spell1Effect = new HealingEffect("Mega Recover",
                50, 1, Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE,
                Effect.DEFAULT_DECAY_RATE);
        spell1Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy applies a large bandage to its wounds!");
        spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy regains a LOT of health!");
        final Spell spell1 = new Spell(spell1Effect, 3, 'E');
        this.spells[1] = spell1;
        final Effect spell2Effect = new Effect("Mega Weapon Drain", 8);
        spell2Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell2Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.6,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell2Effect
                .setMessage(Effect.MESSAGE_INITIAL,
                        "The enemy drains your weapon of a significant amount of its power!");
        spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your attack is significantly decreased!");
        spell2Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your weapon's power has returned!");
        final Spell spell2 = new Spell(spell2Effect, 6, 'P');
        this.spells[2] = spell2;
        final Effect spell3Effect = new Effect("Mega Armor Drain", 8);
        spell3Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell3Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.6,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell3Effect
                .setMessage(Effect.MESSAGE_INITIAL,
                        "The enemy drains your armor of a significant amount of its power!");
        spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your defense is significantly decreased!");
        spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your armor's power has returned!");
        final Spell spell3 = new Spell(spell3Effect, 6, 'P');
        this.spells[3] = spell3;
        final Effect spell4Effect = new Effect("Mega Weapon Charge", 8);
        spell4Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell4Effect.setEffect(Effect.EFFECT_MULTIPLY, 1.5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell4Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy charges its weapon with super power!");
        spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy's attack is significantly increased!");
        spell4Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy's weapon returns to normal!");
        final Spell spell4 = new Spell(spell4Effect, 12, 'E');
        this.spells[4] = spell4;
        final Effect spell5Effect = new Effect("Mega Armor Charge", 8);
        spell5Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell5Effect.setEffect(Effect.EFFECT_MULTIPLY, 1.5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell5Effect.setMessage(Effect.MESSAGE_INITIAL,
                "The enemy charges its armor with super power!");
        spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy's defense is significantly increased!");
        spell5Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy's armor returns to normal!");
        final Spell spell5 = new Spell(spell5Effect, 12, 'E');
        this.spells[5] = spell5;
    }

    @Override
    public int getID() {
        return 3;
    }
}
