/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.items.ShopTypes;
import com.puttysoftware.fantastlereboot.obsolete.loaders.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractShop;

public class FaithPowerShop extends AbstractShop {
    // Constructors
    public FaithPowerShop() {
        super(ShopTypes.FAITH_POWERS);
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_FAITH_POWER_SHOP;
    }

    @Override
    public String getName() {
        return "Faith Power Shop";
    }

    @Override
    public String getPluralName() {
        return "Faith Power Shops";
    }

    @Override
    public String getDescription() {
        return "Faith Power Shops will imbue your equipment with the power of your faith, for a fee.";
    }
}
