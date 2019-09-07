/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.castes.predefined;

import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fantastlereboot.effects.TTEffect;
import com.puttysoftware.fantastlereboot.loaders.older.SoundConstants;
import com.puttysoftware.fantastlereboot.ttspells.Spell;
import com.puttysoftware.fantastlereboot.ttspells.SpellBook;

public class AnnihilatorSpellBook extends SpellBook {
    // Constructor
    public AnnihilatorSpellBook() {
        super(8, false);
        this.setName(CasteConstants.CASTE_NAMES[this.getLegacyID()]);
    }

    @Override
    protected void defineSpells() {
        final TTEffect spell0Effect = new TTEffect("Force Ball", 1);
        spell0Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -1);
        spell0Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell0Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You conjure a force ball, and throw it at an enemy!");
        spell0Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy recoils, and is hurt!");
        final Spell spell0 = new Spell(spell0Effect, 1, BattleTarget.ENEMY,
                SoundConstants.SOUND_COLD);
        this.spells[0] = spell0;
        final TTEffect spell1Effect = new TTEffect("Super Force Ball", 1);
        spell1Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -1.25);
        spell1Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell1Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You conjure a super force balls, and throw it at the enemy!");
        spell1Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy recoils, and is hurt!");
        final Spell spell1 = new Spell(spell1Effect, 2, BattleTarget.ENEMY,
                SoundConstants.SOUND_COLD);
        this.spells[1] = spell1;
        final TTEffect spell2Effect = new TTEffect("Cutter Cloud", 1);
        spell2Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -1.5);
        spell2Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell2Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You conjure a cloud of cutters!");
        spell2Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy gets cut!");
        final Spell spell2 = new Spell(spell2Effect, 3, BattleTarget.ENEMY,
                SoundConstants.SOUND_MISSED);
        this.spells[2] = spell2;
        final TTEffect spell3Effect = new TTEffect("Super Cutter Cloud", 1);
        spell3Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -1.75);
        spell3Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell3Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You conjure a super cutter cloud!");
        spell3Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy gets cut!");
        final Spell spell3 = new Spell(spell3Effect, 5, BattleTarget.ENEMY,
                SoundConstants.SOUND_MISSED);
        this.spells[3] = spell3;
        final TTEffect spell4Effect = new TTEffect("Vortex", 1);
        spell4Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -2);
        spell4Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell4Effect
                .setMessage(TTEffect.MESSAGE_INITIAL, "You conjure a vortex!");
        spell4Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy is engulfed!");
        final Spell spell4 = new Spell(spell4Effect, 7, BattleTarget.ENEMY,
                SoundConstants.SOUND_FIREBALL);
        this.spells[4] = spell4;
        final TTEffect spell5Effect = new TTEffect("Super Vortex", 1);
        spell5Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -2.5);
        spell5Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell5Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You conjure a super vortex!");
        spell5Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy is engulfed!");
        final Spell spell5 = new Spell(spell5Effect, 11, BattleTarget.ENEMY,
                SoundConstants.SOUND_FIREBALL);
        this.spells[5] = spell5;
        final TTEffect spell6Effect = new TTEffect("Air Tear", 1);
        spell6Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, -3);
        spell6Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell6Effect
                .setMessage(TTEffect.MESSAGE_INITIAL,
                        "You focus all your might into a blast powerful enough to rip the air apart!");
        spell6Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy is devastated!");
        final Spell spell6 = new Spell(spell6Effect, 13, BattleTarget.ENEMY,
                SoundConstants.SOUND_WEAKNESS);
        this.spells[6] = spell6;
        final TTEffect spell7Effect = new TTEffect("Power Drain", 1);
        spell7Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_MP, -1);
        spell7Effect.setScaleFactor(0.4);
        spell7Effect.setScaleStat(StatConstants.STAT_MAXIMUM_MP);
        spell7Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You conjure a draining vortex around the enemy!");
        spell7Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy loses some magic!");
        final Spell spell7 = new Spell(spell7Effect, 17, BattleTarget.ENEMY,
                SoundConstants.SOUND_DRAIN);
        this.spells[7] = spell7;
    }

    @Override
    public int getLegacyID() {
        return CasteConstants.CASTE_ANNIHILATOR;
    }
}
