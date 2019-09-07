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

public class Rope extends CombatItem {
    public Rope() {
        super("Rope", 50, BattleTarget.ENEMY);
    }

    @Override
    protected void defineFields() {
        this.sound = SoundConstants.SOUND_BIND;
        this.e = new TTEffect("Roped", 4);
        this.e.setEffect(TTEffect.EFFECT_MULTIPLY, StatConstants.STAT_AGILITY, 0,
                TTEffect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
        this.e.setMessage(TTEffect.MESSAGE_INITIAL,
                "You wind a rope around the enemy!");
        this.e.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The enemy is tied up, and unable to act!");
        this.e.setMessage(TTEffect.MESSAGE_WEAR_OFF, "The rope falls off!");
    }
}
