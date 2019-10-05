package com.puttysoftware.fantastlereboot.spells.books;

import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.effects.DamageEffect;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.spells.Spell;
import com.puttysoftware.fantastlereboot.spells.SpellBook;

public class BasherSpellBook extends SpellBook {
    // Constructor
    public BasherSpellBook() {
        super();
        final DamageEffect spell0Effect = new DamageEffect("Poison", 1, 1, 1.0,
                StatConstants.STAT_LEVEL, 1.0, 1.0, StatConstants.STAT_LEVEL);
        spell0Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You spray poison on your weapon!");
        spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy loses some health from being poisoned!");
        spell0Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The enemy is no longer poisoned!");
        final Spell spell0 = new Spell(spell0Effect, 1, BattleTarget.ENEMY,
                SoundIndex.SLIME);
        this.addKnownSpell(spell0);
        final Effect spell1Effect = new Effect("Turtle Shell", 5);
        spell1Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell1Effect.setEffect(Effect.EFFECT_MULTIPLY, 1.5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell1Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You conjure a turtle shell around yourself!");
        spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your defense is increased!");
        spell1Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The shell has dissipated!");
        final Spell spell1 = new Spell(spell1Effect, 1, BattleTarget.SELF,
                SoundIndex.BUFF_1);
        this.addKnownSpell(spell1);
        final Effect spell2Effect = new Effect("Charged Up", 5);
        spell2Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell2Effect.setEffect(Effect.EFFECT_MULTIPLY, 3,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell2Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You charge your weapon!");
        spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your attack is increased!");
        spell2Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The charge has dissipated!");
        final Spell spell2 = new Spell(spell2Effect, 2, BattleTarget.SELF,
                SoundIndex.BUFF_2);
        this.addKnownSpell(spell2);
        final DamageEffect spell3Effect = new DamageEffect("Ghostly Axe", 20, 5,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE, 4.0);
        spell3Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You summon a ghostly axe!");
        spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The axe attacks the enemy, hurting it somewhat!");
        spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "The axe disappears!");
        final Spell spell3 = new Spell(spell3Effect, 4, BattleTarget.ENEMY,
                SoundIndex.PUNCH);
        this.addKnownSpell(spell3);
        final Effect spell4Effect = new Effect("Armor Bind", 5);
        spell4Effect.setAffectedStat(StatConstants.STAT_DEFENSE);
        spell4Effect.setEffect(Effect.EFFECT_MULTIPLY, 0,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell4Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You bind the enemy's armor, rendering it useless!");
        spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "The enemy is unable to defend!");
        spell4Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "The binding breaks!");
        final Spell spell4 = new Spell(spell4Effect, 7, BattleTarget.ENEMY,
                SoundIndex.DEBUFF_1);
        this.addKnownSpell(spell4);
        final Effect spell5Effect = new Effect("Supercharged", 10);
        spell5Effect.setAffectedStat(StatConstants.STAT_ATTACK);
        spell5Effect.setEffect(Effect.EFFECT_MULTIPLY, 10,
                Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        spell5Effect.setMessage(Effect.MESSAGE_INITIAL,
                "You supercharge your weapon!");
        spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
                "Your attack is GREATLY increased!");
        spell5Effect.setMessage(Effect.MESSAGE_WEAR_OFF,
                "The supercharge has dissipated!");
        final Spell spell5 = new Spell(spell5Effect, 10, BattleTarget.SELF,
                SoundIndex.BUFF_2);
        this.addKnownSpell(spell5);
    }

    @Override
    public int getID() {
        return CasteConstants.CASTE_BASHER;
    }
}
