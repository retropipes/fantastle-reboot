package com.puttysoftware.fantastlereboot.spells.books;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.effects.DamageEffect;
import com.puttysoftware.fantastlereboot.effects.DrainEffect;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.spells.Spell;
import com.puttysoftware.fantastlereboot.spells.SpellBook;

public class YellerSpellBook extends SpellBook {
    // Constructor
    public YellerSpellBook() {
        super();
        final DrainEffect spell0Effect = new DrainEffect("Mana Drain", 4, 1,
                0.5, StatConstants.STAT_LEVEL, Effect.DEFAULT_DECAY_RATE);
        spell0Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You spin your weapon, forming a vortex that engulfs the enemy!");
        spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy loses some MP!");
        final Spell spell0 = new Spell(spell0Effect, 1, BattleTarget.ENEMY,
                GameSound.DRAIN);
        this.addKnownSpell(spell0);
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
        final Spell spell1 = new Spell(spell1Effect, 2, BattleTarget.ENEMY,
                GameSound.DEBUFF_1);
        this.addKnownSpell(spell1);
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
        final Spell spell2 = new Spell(spell2Effect, 4, BattleTarget.ENEMY,
                GameSound.DEBUFF_1);
        this.addKnownSpell(spell2);
        final Effect spell3Effect = new Effect("Glowing Armor", 5);
        spell3Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell3Effect.setEffect(Effect.EFFECT_MULTIPLY, 3,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell3Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You inject mystical energy into your armor, making it glow!");
        spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your defense is increased!");
        spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "The glow fades!");
        final Spell spell3 = new Spell(spell3Effect, 7, BattleTarget.SELF,
                GameSound.BUFF_1);
        this.addKnownSpell(spell3);
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
        final Spell spell4 = new Spell(spell4Effect, 10, BattleTarget.SELF,
                GameSound.BUFF_1);
        this.addKnownSpell(spell4);
        final DamageEffect spell5Effect = new DamageEffect("Weakness Strike", 1,
                1, 0.8, StatConstants.STAT_CURRENT_HP,
                Effect.DEFAULT_DECAY_RATE);
        spell5Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You wait for the right moment, then suddenly attack the enemy's weak point!");
        spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy, caught off-guard by the attack, loses a LOT of health!");
        final Spell spell5 = new Spell(spell5Effect, 20, BattleTarget.ENEMY,
                GameSound.WEAKNESS);
        this.addKnownSpell(spell5);
    }

    @Override
    public int getID() {
        return CasteConstants.CASTE_YELLER;
    }
}
