/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items;

public class EquipmentSlotConstants {
    static final int SLOT_SOCKS = -2;
    static final int SLOT_NONE = -1;
    public static final int SLOT_MAINHAND = 0;
    public static final int SLOT_OFFHAND = 1;
    public static final int SLOT_BODY = 2;
    static final int MAX_SLOTS = 3;
    private static final String[] SLOT_NAMES = { "Helmet", "Necklace", "Main Hand", "Off-Hand",
            "Robe", "Cape", "Shirt", "Bracers", "Gloves", "Ring", "Belt",
            "Pants", "Boots" };
    private static String[] ARMOR_SLOT_NAMES = null;

    static synchronized String[] getSlotNames() {
        return SLOT_NAMES;
    }

    static synchronized String[] getArmorSlotNames() {
        if (ARMOR_SLOT_NAMES == null) {
            if (SLOT_NAMES == null) {
                EquipmentSlotConstants.getSlotNames();
            }
            final String[] temp = SLOT_NAMES;
            final String[] temp2 = new String[temp.length - 1];
            int offset = 0;
            for (int x = 0; x < temp.length; x++) {
                if (x == SLOT_MAINHAND) {
                    offset++;
                } else {
                    temp2[x - offset] = temp[x];
                }
            }
            ARMOR_SLOT_NAMES = temp2;
        }
        return ARMOR_SLOT_NAMES;
    }
}
