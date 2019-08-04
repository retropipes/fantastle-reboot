package com.puttysoftware.fantastlereboot.items.combat;

import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.items.Item;
import com.puttysoftware.fantastlereboot.items.ItemCategoryConstants;

public abstract class CombatUsableItem extends Item {
    // Fields
    private final char target;
    protected Effect e;
    protected String sound;

    // Constructors
    public CombatUsableItem(final String itemName, final int itemBuyPrice,
            final char itemTarget) {
        super(itemName, ItemCategoryConstants.ITEM_CATEGORY_USABLE, 1, 0);
        this.setCombatUsable(true);
        this.setBuyPrice(itemBuyPrice);
        this.target = itemTarget;
        this.defineFields();
    }

    // Methods
    public char getTarget() {
        return this.target;
    }

    public Effect getEffect() {
        return this.e;
    }

    public String getSound() {
        return this.sound;
    }

    protected abstract void defineFields();
}