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

public class CurerSpellBook extends SpellBook {
    // Constructor
    public CurerSpellBook() {
        super(8, false);
        this.setName(CasteConstants.CASTE_NAMES[this.getLegacyID()]);
    }

    @Override
    protected void defineSpells() {
        final TTEffect spell0Effect = new TTEffect("Bandage", 1);
        spell0Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, 3);
        spell0Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell0Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You bandage up your wounds!");
        spell0Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "You feel a little better!");
        final Spell spell0 = new Spell(spell0Effect, 1, BattleTarget.SELF,
                SoundConstants.SOUND_HEAL);
        this.spells[0] = spell0;
        final TTEffect spell1Effect = new TTEffect("Gather", 1);
        spell1Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_MP, 3);
        spell1Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell1Effect.setMessage(TTEffect.MESSAGE_INITIAL, "You gather power!");
        spell1Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "You gain a little MP!");
        final Spell spell1 = new Spell(spell1Effect, 2, BattleTarget.SELF,
                SoundConstants.SOUND_FOCUS);
        this.spells[1] = spell1;
        final TTEffect spell2Effect = new TTEffect("Recover", 1);
        spell2Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, 5);
        spell2Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell2Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You magically recover your stamina!");
        spell2Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT, "You feel better!");
        final Spell spell2 = new Spell(spell2Effect, 3, BattleTarget.SELF,
                SoundConstants.SOUND_HEAL);
        this.spells[2] = spell2;
        final TTEffect spell3Effect = new TTEffect("Bolt", 1);
        spell3Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_MP, 5);
        spell3Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell3Effect.setMessage(TTEffect.MESSAGE_INITIAL, "You zap yourself!");
        spell3Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT, "You feel charged!");
        final Spell spell3 = new Spell(spell3Effect, 5, BattleTarget.SELF,
                SoundConstants.SOUND_FOCUS);
        this.spells[3] = spell3;
        final TTEffect spell4Effect = new TTEffect("Heal", 1);
        spell4Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, 8);
        spell4Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell4Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You heal an ally's wounds!");
        spell4Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The ally feels much better!");
        final Spell spell4 = new Spell(spell4Effect, 7, BattleTarget.SELF,
                SoundConstants.SOUND_HEAL);
        this.spells[4] = spell4;
        final TTEffect spell5Effect = new TTEffect("Big Bolt", 1);
        spell5Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_MP, 8);
        spell5Effect.setScaleStat(StatConstants.STAT_LEVEL);
        spell5Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You electrify yourself!");
        spell5Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "You gain lots of MP!");
        final Spell spell5 = new Spell(spell5Effect, 11, BattleTarget.SELF,
                SoundConstants.SOUND_FOCUS);
        this.spells[5] = spell5;
        final TTEffect spell6Effect = new TTEffect("Full Heal", 1);
        spell6Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_HP, 1);
        spell6Effect.setScaleFactor(1);
        spell6Effect.setScaleStat(StatConstants.STAT_MAXIMUM_HP);
        spell6Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You fully heal yourself!");
        spell6Effect.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "You feel completely refreshed!");
        final Spell spell6 = new Spell(spell6Effect, 13, BattleTarget.SELF,
                SoundConstants.SOUND_HEAL);
        this.spells[6] = spell6;
        final TTEffect spell7Effect = new TTEffect("Power Surge", 1);
        spell7Effect.setEffect(TTEffect.EFFECT_ADD,
                StatConstants.STAT_CURRENT_MP, 1);
        spell7Effect.setScaleFactor(0.4);
        spell7Effect.setScaleStat(StatConstants.STAT_MAXIMUM_MP);
        spell7Effect.setMessage(TTEffect.MESSAGE_INITIAL,
                "You zap an ally with a bolt of energy!");
        spell7Effect
                .setMessage(TTEffect.MESSAGE_SUBSEQUENT, "The ally gains MP!");
        final Spell spell7 = new Spell(spell7Effect, 17, BattleTarget.SELF,
                SoundConstants.SOUND_FOCUS);
        this.spells[7] = spell7;
    }

    @Override
    public int getLegacyID() {
        return CasteConstants.CASTE_CURER;
    }
}
