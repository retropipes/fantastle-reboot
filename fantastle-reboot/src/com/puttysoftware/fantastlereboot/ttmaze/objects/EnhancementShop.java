/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttmaze.objects;

import com.puttysoftware.fantastlereboot.loaders.older.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.ttitems.ShopTypes;
import com.puttysoftware.fantastlereboot.ttmaze.abc.AbstractShop;

public class EnhancementShop extends AbstractShop {
    // Constructors
    public EnhancementShop() {
        super(ShopTypes.ENHANCEMENTS);
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_ENHANCEMENT_SHOP;
    }

    @Override
    public String getName() {
        return "Enhancement Shop";
    }

    @Override
    public String getPluralName() {
        return "Enhancement Shops";
    }

    @Override
    public String getDescription() {
        return "Enhancement Shops sell improvements to weapons and armor.";
    }
}
