package com.puttysoftware.fantastlereboot.spells;

import com.puttysoftware.fantastlereboot.effects.Effect;

public class Spell {
    // Fields
    private final Effect effect;
    private final int cost;
    private final char target;
    private final String soundEffect;

    // Constructors
    public Spell(final Effect newEffect, final int newCost,
            final char newTarget) {
        this.effect = newEffect;
        this.cost = newCost;
        this.target = newTarget;
        this.soundEffect = null;
    }

    public Spell(final Effect newEffect, final int newCost,
            final char newTarget, final String sfx) {
        this.effect = newEffect;
        this.cost = newCost;
        this.target = newTarget;
        this.soundEffect = sfx;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getCost() {
        return this.cost;
    }

    public char getTarget() {
        return this.target;
    }

    public String getSound() {
        return this.soundEffect;
    }
}