/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttitems.combat.predefined;

import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.effects.TTEffect;
import com.puttysoftware.fantastlereboot.loaders.older.SoundConstants;
import com.puttysoftware.fantastlereboot.ttitems.combat.CombatItem;

public class Potion extends CombatItem {
    public Potion() {
        super("Potion", 250, BattleTarget.SELF);
    }

    @Override
    protected void defineFields() {
        this.sound = SoundConstants.SOUND_HEAL;
        this.e = new TTEffect("Potion", 1);
        this.e.setEffect(TTEffect.EFFECT_ADD, StatConstants.STAT_CURRENT_HP, 5);
        this.e.setScaleStat(StatConstants.STAT_LEVEL);
        this.e.setScaleFactor(1.25);
        this.e.setMessage(TTEffect.MESSAGE_INITIAL, "You drink a healing potion!");
        this.e.setMessage(TTEffect.MESSAGE_SUBSEQUENT, "You feel better!");
    }
}
