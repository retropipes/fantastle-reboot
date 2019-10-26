/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items;

import com.puttysoftware.fantastlereboot.creatures.faiths.FaithConstants;

public class EquipmentFactory {
  // Private constructor
  private EquipmentFactory() {
    // Do nothing
  }

  // Methods
  public static Equipment createOneHandedWeapon(final int material,
      final int weaponType, final int bonus) {
    final Equipment e = new Equipment(
        WeaponMaterialConstants.MATERIAL_COMMON_NAMES[material] + " "
            + WeaponConstants.get1HWeapons()[weaponType],
        0, 0, EquipmentCategoryConstants.EQUIPMENT_CATEGORY_ONE_HANDED_WEAPON,
        material);
    e.setFirstSlotUsed(EquipmentSlotConstants.SLOT_MAINHAND);
    e.setSecondSlotUsed(EquipmentSlotConstants.SLOT_OFFHAND);
    e.setConditionalSlot(true);
    e.setPotency(
        material * WeaponMaterialConstants.MATERIALS_POWER_MULTIPLIER + bonus);
    e.setBuyPrice(Shop.getEquipmentCost(material));
    e.setSellPrice(Shop.getEquipmentCost(material) / 2);
    return e;
  }

  public static Equipment createTwoHandedWeapon(final int material,
      final int weaponType, final int bonus) {
    final Equipment e = new Equipment(
        WeaponMaterialConstants.MATERIAL_COMMON_NAMES[material] + " "
            + WeaponConstants.get2HWeapons()[weaponType],
        0, 0, EquipmentCategoryConstants.EQUIPMENT_CATEGORY_TWO_HANDED_WEAPON,
        material);
    e.setFirstSlotUsed(EquipmentSlotConstants.SLOT_MAINHAND);
    e.setSecondSlotUsed(EquipmentSlotConstants.SLOT_OFFHAND);
    e.setConditionalSlot(false);
    e.setPotency(
        material * WeaponMaterialConstants.MATERIALS_POWER_MULTIPLIER + bonus);
    e.setBuyPrice(Shop.getEquipmentCost(material) * 2);
    e.setSellPrice(Shop.getEquipmentCost(material));
    return e;
  }

  public static Equipment createArmor(final int material, final int armorType,
      final int bonus) {
    final Equipment e = new Equipment(
        ArmorMaterialConstants.MATERIAL_COMMON_NAMES[material] + " "
            + ArmorConstants.getArmor()[armorType + 1],
        0, 0, EquipmentCategoryConstants.EQUIPMENT_CATEGORY_ARMOR, material);
    e.setFirstSlotUsed(armorType + 1);
    e.setConditionalSlot(false);
    e.setPotency(
        material * ArmorMaterialConstants.MATERIALS_POWER_MULTIPLIER + bonus);
    e.setBuyPrice(Shop.getEquipmentCost(material));
    e.setSellPrice(Shop.getEquipmentCost(material) / 2);
    return e;
  }

  public static Equipment createEnhancedEquipment(final Equipment oldE,
      final int bonus) {
    final Equipment e = new Equipment(oldE);
    e.setPotency(
        oldE.getMaterial() * ArmorMaterialConstants.MATERIALS_POWER_MULTIPLIER
            + bonus);
    e.enchantName(bonus);
    return e;
  }

  public static Equipment createFaithPoweredEquipment(final Equipment oldE,
      final int faithID, final int bonus) {
    final Equipment e = new Equipment(oldE);
    final String fpName = FaithConstants.getFaithPowerName(faithID, bonus);
    e.applyFaithPower(faithID, fpName);
    return e;
  }

  public static Socks createSocks(final int actionType, final int price) {
    final String[] names = EquipmentFactory.createSocksNames();
    return new Socks(names[actionType - 1], price, actionType, 1);
  }

  public static String[] createOneHandedWeaponNames(final int weaponType) {
    final String[] res = new String[WeaponMaterialConstants.MATERIALS_COUNT];
    for (int x = 0; x < res.length; x++) {
      res[x] = WeaponMaterialConstants.MATERIAL_COMMON_NAMES[x] + " "
          + WeaponConstants.get1HWeapons()[weaponType];
    }
    return res;
  }

  public static String[] createTwoHandedWeaponNames(final int weaponType) {
    final String[] res = new String[WeaponMaterialConstants.MATERIALS_COUNT];
    for (int x = 0; x < res.length; x++) {
      res[x] = WeaponMaterialConstants.MATERIAL_COMMON_NAMES[x] + " "
          + WeaponConstants.get2HWeapons()[weaponType];
    }
    return res;
  }

  public static String[] createArmorNames(final int armorType) {
    final String[] res = new String[ArmorMaterialConstants.MATERIALS_COUNT];
    for (int x = 0; x < res.length; x++) {
      res[x] = ArmorMaterialConstants.MATERIAL_COMMON_NAMES[x] + " "
          + ArmorConstants.getArmor()[armorType + 1];
    }
    return res;
  }

  public static String[] createSocksNames() {
    return new String[] { "Heal Socks", "Regen Socks", "Experience Socks",
        "Money Socks" };
  }
}
