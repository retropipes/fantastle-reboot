package net.worldwizard.fantastle5.items;

public class EquipmentFactory {
    // Private constructor
    private EquipmentFactory() {
        // Do nothing
    }

    // Methods
    public static Equipment createOneHandedWeapon(final int material,
            final int weaponType) {
        final Equipment e = new Equipment(
                WeaponMaterialConstants.MATERIAL_COMMON_NAMES[material] + " "
                        + WeaponConstants.WEAPON_1H[weaponType],
                ItemCategoryConstants.ITEM_CATEGORY_NONE, 0, 0,
                EquipmentCategoryConstants.EQUIPMENT_CATEGORY_ONE_HANDED_WEAPON,
                material);
        e.setFirstSlotUsed(EquipmentSlotConstants.SLOT_MAINHAND);
        e.setSecondSlotUsed(EquipmentSlotConstants.SLOT_OFFHAND);
        e.setConditionalSlot(true);
        e.setPotency(material + WeaponMaterialConstants.MATERIALS_POWER_OFFSET);
        e.setBuyPrice(Shop.getEquipmentCost(material));
        e.setSellPrice(Shop.getEquipmentCost(material) / 2);
        return e;
    }

    public static Equipment createMonsterWeapon(final int material) {
        final Equipment e = new Equipment(
                WeaponMaterialConstants.MATERIAL_COMMON_NAMES[material]
                        + " Claws",
                ItemCategoryConstants.ITEM_CATEGORY_NONE, 0, 0,
                EquipmentCategoryConstants.EQUIPMENT_CATEGORY_ONE_HANDED_WEAPON,
                material);
        e.setFirstSlotUsed(EquipmentSlotConstants.SLOT_MAINHAND);
        e.setSecondSlotUsed(EquipmentSlotConstants.SLOT_OFFHAND);
        e.setConditionalSlot(true);
        e.setPotency(material + WeaponMaterialConstants.MATERIALS_POWER_OFFSET);
        return e;
    }

    public static Equipment createTwoHandedWeapon(final int material,
            final int weaponType) {
        final Equipment e = new Equipment(
                WeaponMaterialConstants.MATERIAL_COMMON_NAMES[material] + " "
                        + WeaponConstants.WEAPON_2H[weaponType],
                ItemCategoryConstants.ITEM_CATEGORY_NONE, 0, 0,
                EquipmentCategoryConstants.EQUIPMENT_CATEGORY_TWO_HANDED_WEAPON,
                material);
        e.setFirstSlotUsed(EquipmentSlotConstants.SLOT_MAINHAND);
        e.setSecondSlotUsed(EquipmentSlotConstants.SLOT_OFFHAND);
        e.setConditionalSlot(false);
        e.setPotency(material + WeaponMaterialConstants.MATERIALS_POWER_OFFSET);
        e.setBuyPrice(Shop.getEquipmentCost(material) * 2);
        e.setSellPrice(Shop.getEquipmentCost(material));
        return e;
    }

    public static Equipment createArmor(final int material,
            final int armorType) {
        final Equipment e = new Equipment(
                ArmorMaterialConstants.MATERIAL_COMMON_NAMES[material] + " "
                        + ArmorConstants.ARMOR[armorType],
                ItemCategoryConstants.ITEM_CATEGORY_NONE, 0, 0,
                EquipmentCategoryConstants.EQUIPMENT_CATEGORY_ARMOR, material);
        e.setFirstSlotUsed(armorType);
        e.setConditionalSlot(false);
        e.setPotency(material + ArmorMaterialConstants.MATERIALS_POWER_OFFSET);
        e.setBuyPrice(Shop.getEquipmentCost(material));
        e.setSellPrice(Shop.getEquipmentCost(material) / 2);
        return e;
    }

    public static String[] createOneHandedWeaponNames(final int weaponType) {
        final String[] res = new String[WeaponMaterialConstants.MATERIALS_COUNT];
        for (int x = 0; x < res.length; x++) {
            res[x] = WeaponMaterialConstants.MATERIAL_COMMON_NAMES[x] + " "
                    + WeaponConstants.WEAPON_1H[weaponType];
        }
        return res;
    }

    public static String[] createTwoHandedWeaponNames(final int weaponType) {
        final String[] res = new String[WeaponMaterialConstants.MATERIALS_COUNT];
        for (int x = 0; x < res.length; x++) {
            res[x] = WeaponMaterialConstants.MATERIAL_COMMON_NAMES[x] + " "
                    + WeaponConstants.WEAPON_2H[weaponType];
        }
        return res;
    }

    public static String[] createArmorNames(final int armorType) {
        final String[] res = new String[ArmorMaterialConstants.MATERIALS_COUNT];
        for (int x = 0; x < res.length; x++) {
            res[x] = ArmorMaterialConstants.MATERIAL_COMMON_NAMES[x] + " "
                    + ArmorConstants.ARMOR[armorType];
        }
        return res;
    }
}
