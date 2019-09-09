/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ai.map;

import com.puttysoftware.fantastlereboot.items.combat.CombatItem;
import com.puttysoftware.fantastlereboot.spells.Spell;

public abstract class AbstractMapAIRoutine {
    // Fields
    protected Spell spell;
    private final CombatItem item;
    protected int moveX;
    protected int moveY;
    protected boolean lastResult;
    public static final int ACTION_MOVE = 0;
    public static final int ACTION_CAST_SPELL = 1;
    public static final int ACTION_STEAL = 2;
    public static final int ACTION_DRAIN = 3;
    public static final int ACTION_USE_ITEM = 4;
    static final int ACTION_END_TURN = 5;

    // Constructor
    protected AbstractMapAIRoutine() {
        this.spell = null;
        this.item = null;
        this.moveX = 0;
        this.moveY = 0;
        this.lastResult = true;
    }

    // Methods
    public abstract int getNextAction(MapAIContext ac);

    public void newRoundHook() {
        // Do nothing
    }

    public final int getMoveX() {
        return this.moveX;
    }

    public final int getMoveY() {
        return this.moveY;
    }

    public final Spell getSpellToCast() {
        return this.spell;
    }

    public final CombatItem getItemToUse() {
        return this.item;
    }

    public final void setLastResult(final boolean res) {
        this.lastResult = res;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.item == null) ? 0 : this.item.hashCode());
        result = prime * result + (this.lastResult ? 1231 : 1237);
        result = prime * result + this.moveX;
        result = prime * result + this.moveY;
        return prime * result
                + ((this.spell == null) ? 0 : this.spell.hashCode());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractMapAIRoutine)) {
            return false;
        }
        final AbstractMapAIRoutine other = (AbstractMapAIRoutine) obj;
        if (this.item == null) {
            if (other.item != null) {
                return false;
            }
        } else if (!this.item.equals(other.item)) {
            return false;
        }
        if (this.lastResult != other.lastResult) {
            return false;
        }
        if (this.moveX != other.moveX) {
            return false;
        }
        if (this.moveY != other.moveY) {
            return false;
        }
        if (this.spell == null) {
            if (other.spell != null) {
                return false;
            }
        } else if (!this.spell.equals(other.spell)) {
            return false;
        }
        return true;
    }
}
