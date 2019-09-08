/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items;

import com.puttysoftware.fantastlereboot.oldnames.NamesConstants;
import com.puttysoftware.fantastlereboot.oldnames.NamesManager;

public class ArmorConstants {
    private static String[] ARMOR = null;
    private static String[] ARMOR_CHOICES = null;

    public static synchronized String[] getArmorChoices() {
        if (ARMOR_CHOICES == null) {
            final String[] temp1 = EquipmentSlotConstants.getArmorSlotNames();
            final String[] temp2 = new String[temp1.length];
            System.arraycopy(temp1, 0, temp2, 0, temp1.length);
            temp2[EquipmentSlotConstants.SLOT_OFFHAND - 1] = NamesManager
                    .getName(NamesConstants.SECTION_EQUIP_ARMOR,
                            NamesConstants.ARMOR_SHIELD);
            ARMOR_CHOICES = temp2;
        }
        return ARMOR_CHOICES;
    }

    public static synchronized String[] getArmor() {
        if (ARMOR == null) {
            final String[] temp1 = ArmorConstants.getArmorChoices();
            final String[] temp2 = new String[temp1.length + 1];
            temp2[EquipmentSlotConstants.SLOT_MAINHAND] = "";
            temp2[EquipmentSlotConstants.SLOT_OFFHAND] = temp1[0];
            temp2[EquipmentSlotConstants.SLOT_BODY] = temp1[1];
            ARMOR = temp2;
        }
        return ARMOR;
    }
}
