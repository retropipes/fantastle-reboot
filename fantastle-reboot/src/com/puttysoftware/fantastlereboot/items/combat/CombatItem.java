/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items.combat;

import java.util.Objects;

import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.items.Item;

public class CombatItem extends Item {
    // Fields
    private final BattleTarget target;
    protected Effect e;
    protected SoundIndex sound;

    // Constructors
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

    final Effect getEffect() {
        return this.e;
    }

    final SoundIndex getSound() {
        return this.sound;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(this.e, this.sound, this.target);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CombatItem)) {
            return false;
        }
        CombatItem other = (CombatItem) obj;
        return Objects.equals(this.e, other.e) && this.sound == other.sound
                && this.target == other.target;
    }

    protected void defineFields() {
        // Do nothing
    }
}