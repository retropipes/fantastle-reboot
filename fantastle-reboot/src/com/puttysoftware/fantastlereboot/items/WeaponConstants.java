/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items;

public class WeaponConstants {
  // Constants
  private static final String[] WEAPON_1H = { "Dagger", "Club", "Scimitar",
      "Boomerang", "Javelin", "Grenade", "Whip", "Crossbow", "Dart", "Sword",
      "Lock", "Fists", "Throwing Stars", "Pebbles", "Lockpicks", "Gun", "Flail",
      "Hammer", "Wand", "Voice" };
  private static final String[] WEAPON_2H = { "Longsword", "Mace", "Staff",
      "Slingshot", "Spear", "Bazooka", "Catapult", "Bow", "Boulder", "Axe",
      "Key", "Feet", "Halberd", "Lance", "Door", "Cannon", "Battering Ram",
      "Laser", "Scythe", "Dance" };
  private static final String[] WEAPON_CHOICES = { "One-Handed Weapons",
      "Two-Handed Weapons" };
  private static final String[] HAND_CHOICES = { "Main Hand", "Off-Hand" };

  // Private Constructor
  private WeaponConstants() {
    // Do nothing
  }

  // Methods
  public static String[] getWeaponChoices() {
    return WeaponConstants.WEAPON_CHOICES;
  }

  public static synchronized String[] getHandChoices() {
    return WeaponConstants.HAND_CHOICES;
  }

  public static synchronized String[] get1HWeapons() {
    return WeaponConstants.WEAPON_1H;
  }

  public static synchronized String[] get2HWeapons() {
    return WeaponConstants.WEAPON_2H;
  }
}
