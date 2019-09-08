/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttitems.combat.predefined;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.effects.TTEffect;
import com.puttysoftware.fantastlereboot.ttitems.combat.CombatItem;

public class Bomb extends CombatItem {
    public Bomb() {
        super("Bomb", 30, BattleTarget.ENEMY);
    }

    @Override
    protected void defineFields() {
        this.sound = GameSound.EXPLODE;
        this.e = new TTEffect("Bomb", 1);
        this.e.setEffect(TTEffect.EFFECT_ADD, StatConstants.STAT_CURRENT_HP, -5);
        this.e.setScaleStat(StatConstants.STAT_LEVEL);
        this.e.setScaleFactor(0.75);
        this.e.setMessage(TTEffect.MESSAGE_INITIAL,
                "You throw a bomb at the enemy!");
        this.e.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The bomb goes BOOM, inflicting a little damage!");
    }
}
