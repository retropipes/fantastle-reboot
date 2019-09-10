/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items;

public class ArmorConstants {
    private static final String[] ARMOR = { "Helmet", "Necklace", "", "Shield", "Robe", "Cape",
            "Shirt", "Bracers", "Gloves", "Ring", "Belt", "Pants", "Boots" };
    private static final String[] ARMOR_CHOICES = { "Helmet", "Necklace", "Shield", "Robe", "Cape",
            "Shirt", "Bracers", "Gloves", "Ring", "Belt", "Pants", "Boots" };

    public static synchronized String[] getArmorChoices() {
        return ARMOR_CHOICES;
    }

    public static synchronized String[] getArmor() {
        return ARMOR;
    }
}
