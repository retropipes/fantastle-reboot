/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttitems.combat;

import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.effects.TTEffect;
import com.puttysoftware.fantastlereboot.ttitems.Item;

public class CombatItem extends Item {
    // Fields
    private final BattleTarget target;
    protected TTEffect e;
    protected int sound;

    // Constructors
    public CombatItem() {
        super();
        this.target = null;
    }

    public CombatItem(final String itemName, final int itemBuyPrice,
            final BattleTarget itemTarget) {
        super(itemName, 1, 0);
        this.setCombatUsable(true);
        this.setBuyPrice(itemBuyPrice);
        this.target = itemTarget;
        this.defineFields();
    }

    // Methods
    final BattleTarget getTarget() {
        return this.target;
    }

    final TTEffect getEffect() {
        return this.e;
    }

    final int getSound() {
        return this.sound;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.e == null) ? 0 : this.e.hashCode());
        result = prime * result + this.sound;
        return prime * result
                + ((this.target == null) ? 0 : this.target.hashCode());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CombatItem)) {
            return false;
        }
        final CombatItem other = (CombatItem) obj;
        if (this.e == null) {
            if (other.e != null) {
                return false;
            }
        } else if (!this.e.equals(other.e)) {
            return false;
        }
        if (this.sound != other.sound) {
            return false;
        }
        if (this.target != other.target) {
            return false;
        }
        return true;
    }

    protected void defineFields() {
        // Do nothing
    }
}