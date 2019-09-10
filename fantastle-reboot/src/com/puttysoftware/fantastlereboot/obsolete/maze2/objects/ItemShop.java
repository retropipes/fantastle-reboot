/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.items.ShopTypes;
import com.puttysoftware.fantastlereboot.obsolete.loaders2.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractShop;

public class ItemShop extends AbstractShop {
    // Constructors
    public ItemShop() {
        super(ShopTypes.ITEMS);
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_ITEM_SHOP;
    }

    @Override
    public String getName() {
        return "Item Shop";
    }

    @Override
    public String getPluralName() {
        return "Item Shops";
    }

    @Override
    public String getDescription() {
        return "Item Shops sell items used in battle.";
    }
}
