/*  FantastleReboot: An RPG
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
  private static final String[] SLOT_NAMES = { "Helmet", "Necklace",
      "Main Hand", "Off-Hand", "Robe", "Cape", "Shirt", "Bracers", "Gloves",
      "Ring", "Belt", "Pants", "Boots" };

  static synchronized String[] getSlotNames() {
    return EquipmentSlotConstants.SLOT_NAMES;
  }
}
