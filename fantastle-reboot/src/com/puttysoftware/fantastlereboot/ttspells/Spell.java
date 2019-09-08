/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttspells;

import java.util.Objects;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.effects.TTEffect;

public class Spell {
    // Fields
    private final TTEffect effect;
    private final int cost;
    private final BattleTarget target;
    private final GameSound soundEffect;

    // Constructors
    public Spell(final TTEffect newEffect, final int newCost,
            final BattleTarget newTarget, final GameSound sfx) {
        super();
        this.effect = newEffect;
        this.cost = newCost;
        this.target = newTarget;
        this.soundEffect = sfx;
    }

    public TTEffect getEffect() {
        return this.effect;
    }

    public int getCost() {
        return this.cost;
    }

    BattleTarget getTarget() {
        return this.target;
    }

    GameSound getSound() {
        return this.soundEffect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.cost, this.effect, this.soundEffect,
                this.target);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Spell)) {
            return false;
        }
        Spell other = (Spell) obj;
        return this.cost == other.cost
                && Objects.equals(this.effect, other.effect)
                && this.soundEffect == other.soundEffect
                && this.target == other.target;
    }
}
