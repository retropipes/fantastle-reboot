package net.worldwizard.fantastle5.spells;

import net.worldwizard.fantastle5.creatures.StatConstants;
import net.worldwizard.fantastle5.effects.DamageEffect;
import net.worldwizard.fantastle5.effects.DrainEffect;
import net.worldwizard.fantastle5.effects.Effect;

public class YellerSpellBook extends SpellBook {
    // Constructor
    public YellerSpellBook() {
        super(6);
    }

    @Override
    protected void defineSpells() {
        final DrainEffect spell0Effect = new DrainEffect("Mana Drain", 4, 1,
                0.5, StatConstants.STAT_LEVEL, Effect.DEFAULT_DECAY_RATE);
        spell0Effect
                .setMessage(Effect.MESSAGE_INITIAL,
                        "You spin your weapon, forming a vortex that engulfs the enemy!");
        spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy loses some MP!");
        final Spell spell0 = new Spell(spell0Effect, 1, 'E', "drain");
        this.spells[0] = spell0;
        final Effect spell1Effect = new Effect("Sneak Attack", 5);
        spell1Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell1Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell1Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You sneak up behind the enemy!");
        spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy's defenses are weaker here!");
        spell1Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy spots you, and turns around!");
        final Spell spell1 = new Spell(spell1Effect, 2, 'E', "bind");
        this.spells[1] = spell1;
        final Effect spell2Effect = new Effect("Weapon Steal", 5);
        spell2Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell2Effect.setEffect(Effect.EFFECT_MULTIPLY, 0.5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell2Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You steal the enemy's weapon!");
        spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy cannot attack as well!");
        spell2Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy finds their weapon, and reclaims it!");
        final Spell spell2 = new Spell(spell2Effect, 4, 'E', "bind");
        this.spells[2] = spell2;
        final Effect spell3Effect = new Effect("Glowing Armor", 5);
        spell3Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell3Effect.setEffect(Effect.EFFECT_MULTIPLY, 3,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell3Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You inject mystical energy into your armor, making it glow!");
        spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your defense is increased!");
        spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "The glow fades!");
        final Spell spell3 = new Spell(spell3Effect, 7, 'P', "defense");
        this.spells[3] = spell3;
        final Effect spell4Effect = new Effect("Hardened Armor", 5);
        spell4Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell4Effect.setEffect(Effect.EFFECT_MULTIPLY, 10,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell4Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You harden your armor, using your magic!");
        spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your defense is GREATLY increased!");
        spell4Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "Your armor returns to normal!");
        final Spell spell4 = new Spell(spell4Effect, 10, 'P', "defense");
        this.spells[4] = spell4;
        final DamageEffect spell5Effect = new DamageEffect("Weakness Strike",
                1, 1, 0.8, StatConstants.STAT_CURRENT_HP,
                Effect.DEFAULT_DECAY_RATE);
        spell5Effect
                .setMessage(Effect.MESSAGE_INITIAL,
                        "You wait for the right moment, then suddenly attack the enemy's weak point!");
        spell5Effect
                .setMessage(Effect.MESSAGE_SUBSEQUENT,
                        "The enemy, caught off-guard by the attack, loses a LOT of health!");
        final Spell spell5 = new Spell(spell5Effect, 20, 'E', "weakness");
        this.spells[5] = spell5;
    }

    @Override
    public int getID() {
        return 3;
    }
}
