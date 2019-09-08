/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttmaze.objects;

import com.puttysoftware.fantastlereboot.loaders.older.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.ttitems.ShopTypes;
import com.puttysoftware.fantastlereboot.ttmaze.abc.AbstractShop;

public class SpellShop extends AbstractShop {
    // Constructors
    public SpellShop() {
        super(ShopTypes.SPELLS);
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_SPELL_SHOP;
    }

    @Override
    public String getName() {
        return "Spell Shop";
    }

    @Override
    public String getPluralName() {
        return "Spell Shops";
    }

    @Override
    public String getDescription() {
        return "Spell Shops teach spells, for a fee.";
    }
}
