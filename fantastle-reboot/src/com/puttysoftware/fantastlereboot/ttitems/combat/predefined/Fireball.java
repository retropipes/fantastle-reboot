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

public class Fireball extends CombatItem {
    public Fireball() {
        super("Fireball", 500, BattleTarget.ENEMY);
    }

    @Override
    protected void defineFields() {
        this.sound = GameSound.BOLT;
        this.e = new TTEffect("Fireball", 1);
        this.e.setEffect(TTEffect.EFFECT_ADD, StatConstants.STAT_CURRENT_HP, -3);
        this.e.setScaleStat(StatConstants.STAT_LEVEL);
        this.e.setScaleFactor(1.5);
        this.e.setMessage(TTEffect.MESSAGE_INITIAL,
                "You throw a fireball at the enemy!");
        this.e.setMessage(TTEffect.MESSAGE_SUBSEQUENT,
                "The fireball sears the enemy badly, dealing LOTS of damage!");
    }
}
