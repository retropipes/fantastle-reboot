package net.worldwizard.fantastle5.spells;

import net.worldwizard.fantastle5.creatures.StatConstants;
import net.worldwizard.fantastle5.effects.DamageEffect;
import net.worldwizard.fantastle5.effects.Effect;
import net.worldwizard.fantastle5.effects.HealingEffect;

public class DestroyerSpellBook extends SpellBook {
    // Constructor
    public DestroyerSpellBook() {
        super(6);
    }

    @Override
    protected void defineSpells() {
        final DamageEffect spell0Effect = new DamageEffect("Fireball", 10, 1,
                0.25, StatConstants.STAT_LEVEL, Effect.DEFAULT_DECAY_RATE);
        spell0Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You conjure a fireball, then throw it at the enemy!");
        spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy loses a little health from being burned!");
        final Spell spell0 = new Spell(spell0Effect, 1, 'E', "lava");
        this.spells[0] = spell0;
        final HealingEffect spell1Effect = new HealingEffect("Minor Heal", 15,
                1, 0.25, StatConstants.STAT_LEVEL, Effect.DEFAULT_DECAY_RATE);
        spell1Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You conjure a small bandage, and apply it to your wounds!");
        spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "You gain some health!");
        final Spell spell1 = new Spell(spell1Effect, 2, 'P', "heal");
        this.spells[1] = spell1;
        final DamageEffect spell2Effect = new DamageEffect("Ice Shard", 10, 1,
                0.4, StatConstants.STAT_LEVEL, Effect.DEFAULT_DECAY_RATE);
        spell2Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You conjure a shard of ice, then throw it at the enemy!");
        spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy loses some health from being frozen!");
        final Spell spell2 = new Spell(spell2Effect, 4, 'E', "iceshard");
        this.spells[2] = spell2;
        final Effect spell3Effect = new Effect("Weapon Bind", 5);
        spell3Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell3Effect.setEffect(Effect.EFFECT_MULTIPLY, 0,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell3Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You bind the enemy's weapon, rendering it useless!");
        spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy is unable to attack!");
        spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "The binding breaks!");
        final Spell spell3 = new Spell(spell3Effect, 7, 'E', "bind");
        this.spells[3] = spell3;
        final HealingEffect spell4Effect = new HealingEffect("Major Heal", 10,
                1, 0.75, StatConstants.STAT_LEVEL, Effect.DEFAULT_DECAY_RATE);
        spell4Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You summon a large bandage, and apply it to your wounds!");
        spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "You gain a LOT of health!");
        final Spell spell4 = new Spell(spell4Effect, 15, 'P', "heal");
        this.spells[4] = spell4;
        final DamageEffect spell5Effect = new DamageEffect("Lightning Bolt", 1,
                1, 0.8, StatConstants.STAT_MAXIMUM_HP,
                Effect.DEFAULT_DECAY_RATE);
        spell5Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You summon a bolt of lightning, then throw it at the enemy!");
        spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy loses a LOT of health from being shocked!");
        final Spell spell5 = new Spell(spell5Effect, 30, 'E', "bolt");
        this.spells[5] = spell5;
    }

    @Override
    public int getID() {
        return 2;
    }
}
