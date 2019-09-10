/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.names;

import java.util.Arrays;

public class NamesConstants {
    // Section Names
    public static final String SECTION_STATS = "Statistics";
    public static final String SECTION_EQUIP_SLOT = "EquipmentSlots";
    public static final String SECTION_EQUIP_ARMOR = "EquipmentArmor";
    public static final String SECTION_EQUIP_WEAPONS_1H = "EquipmentWeapons1H";
    public static final String SECTION_EQUIP_WEAPONS_2H = "EquipmentWeapons2H";
    public static final String SECTION_FAITHS = "Faith";
    public static final String SECTION_FAITH_POWERS_PREFIX = "FaithPower";
    // Statistics Section Keys
    private static final String STAT_STRENGTH = "Strength";
    private static final String STAT_BLOCK = "Block";
    private static final String STAT_AGILITY = "Agility";
    private static final String STAT_VITALITY = "Vitality";
    private static final String STAT_INTELLIGENCE = "Intelligence";
    private static final String STAT_LUCK = "Luck";
    private static final String STAT_CURRENT_HP = "CurrentHP";
    private static final String STAT_CURRENT_MP = "CurrentMP";
    private static final String STAT_GOLD = "Gold";
    private static final String STAT_LEVEL = "Level";
    private static final String STAT_ATTACKS_PER_ROUND = "AttacksPerRound";
    private static final String STAT_SPELLS_PER_ROUND = "SpellsPerRound";
    private static final String STAT_LOAD = "Load";
    private static final String STAT_MAXIMUM_HP = "MaximumHP";
    private static final String STAT_MAXIMUM_MP = "MaximumMP";
    private static final String STAT_ATTACK = "Attack";
    private static final String STAT_DEFENSE = "Defense";
    private static final String STAT_SPEED = "Speed";
    private static final String STAT_HIT = "Hit";
    private static final String STAT_EVADE = "Evade";
    private static final String STAT_CAPACITY = "Capacity";
    public static final String[] SECTION_ARRAY_STATS = new String[] {
            STAT_STRENGTH, STAT_BLOCK, STAT_AGILITY, STAT_VITALITY,
            STAT_INTELLIGENCE, STAT_LUCK, STAT_CURRENT_HP, STAT_CURRENT_MP,
            STAT_GOLD, STAT_LEVEL, STAT_ATTACKS_PER_ROUND,
            STAT_SPELLS_PER_ROUND, STAT_LOAD, STAT_MAXIMUM_HP, STAT_MAXIMUM_MP,
            STAT_ATTACK, STAT_DEFENSE, STAT_SPEED, STAT_HIT, STAT_EVADE,
            STAT_CAPACITY };
    // Equipment Slots Section Keys
    private static final String SLOT_MAIN_HAND = "MainHand";
    private static final String SLOT_OFF_HAND = "OffHand";
    private static final String SLOT_BODY = "Body";
    public static final String[] SECTION_ARRAY_EQUIP_SLOTS = new String[] {
            SLOT_MAIN_HAND, SLOT_OFF_HAND, SLOT_BODY };
    // Equipment Armor Section Keys
    public static final String ARMOR_SHIELD = "Shield";
    private static final String[] SECTION_ARRAY_EQUIP_ARMOR = new String[] { ARMOR_SHIELD };
    // Equipment Weapons 1H Section Keys
    private static final String WEAPON_1H_GLOVES = "Gloves";
    private static final String WEAPON_1H_SWORD = "Sword";
    private static final String WEAPON_1H_BOOMERANG = "Boomerang";
    private static final String WEAPON_1H_JAVELIN = "Javelin";
    public static final String[] SECTION_ARRAY_WEAPONS_1H = new String[] {
            WEAPON_1H_GLOVES, WEAPON_1H_SWORD, WEAPON_1H_BOOMERANG,
            WEAPON_1H_JAVELIN };
    // Equipment Weapons 2H Section Keys
    private static final String WEAPON_2H_SPEAR = "Spear";
    private static final String WEAPON_2H_AXE = "Axe";
    private static final String WEAPON_2H_BOW = "Bow";
    private static final String WEAPON_2H_WHIP = "Whip";
    public static final String[] SECTION_ARRAY_WEAPONS_2H = new String[] {
            WEAPON_2H_SPEAR, WEAPON_2H_AXE, WEAPON_2H_BOW, WEAPON_2H_WHIP };
    // Faith Section Keys
    private static final String FAITH_NONE = "None";
    private static final String FAITH_HEAT = "Heat";
    private static final String FAITH_COLD = "Cold";
    private static final String FAITH_ROCK = "Rock";
    private static final String FAITH_GUST = "Gust";
    private static final String FAITH_BEAM = "Beam";
    private static final String FAITH_DEAD = "Dead";
    private static final String FAITH_BOLT = "Bolt";
    private static final String FAITH_BOOM = "Boom";
    public static final String[] SECTION_ARRAY_FAITHS = new String[] {
            FAITH_NONE, FAITH_HEAT, FAITH_COLD, FAITH_ROCK, FAITH_GUST,
            FAITH_BEAM, FAITH_DEAD, FAITH_BOLT, FAITH_BOOM };
    // Faith Power Section Keys
    private static final String FAITH_POWER_1 = "1";
    private static final String FAITH_POWER_2 = "2";
    private static final String FAITH_POWER_3 = "3";
    private static final String FAITH_POWER_4 = "4";
    private static final String FAITH_POWER_5 = "5";
    private static final String FAITH_POWER_6 = "6";
    private static final String FAITH_POWER_7 = "7";
    private static final String FAITH_POWER_8 = "8";
    private static final String FAITH_POWER_9 = "9";
    public static final String[] SECTION_ARRAY_FAITH_POWERS = new String[] {
            FAITH_POWER_1, FAITH_POWER_2, FAITH_POWER_3, FAITH_POWER_4,
            FAITH_POWER_5, FAITH_POWER_6, FAITH_POWER_7, FAITH_POWER_8,
            FAITH_POWER_9 };
    // Names Length
    public static final int NAMES_LENGTH = SECTION_ARRAY_STATS.length
            + SECTION_ARRAY_EQUIP_SLOTS.length
            + SECTION_ARRAY_EQUIP_ARMOR.length
            + SECTION_ARRAY_WEAPONS_1H.length + SECTION_ARRAY_WEAPONS_2H.length
            + SECTION_ARRAY_FAITHS.length
            + (SECTION_ARRAY_FAITH_POWERS.length * 9);
    // Names Version
    public static final int NAMES_VERSION = 2;
    // Editor Section Names
    private static final String EDITOR_SECTION_STATS = "Statistic";
    private static final String EDITOR_SECTION_EQUIP_SLOT = "Equipment Slot";
    private static final String EDITOR_SECTION_EQUIP_ARMOR = "Armor";
    private static final String EDITOR_SECTION_WEAPONS_1H = "1-Handed Weapon";
    private static final String EDITOR_SECTION_WEAPONS_2H = "2-Handed Weapon";
    private static final String EDITOR_SECTION_FAITHS = "Faith";
    private static final String EDITOR_SECTION_FAITH_POWERS_NONE = "Faith Power of None";
    private static final String EDITOR_SECTION_FAITH_POWERS_HEAT = "Faith Power of Heat";
    private static final String EDITOR_SECTION_FAITH_POWERS_COLD = "Faith Power of Cold";
    private static final String EDITOR_SECTION_FAITH_POWERS_ROCK = "Faith Power of Rock";
    private static final String EDITOR_SECTION_FAITH_POWERS_GUST = "Faith Power of Gust";
    private static final String EDITOR_SECTION_FAITH_POWERS_BEAM = "Faith Power of Beam";
    private static final String EDITOR_SECTION_FAITH_POWERS_DEAD = "Faith Power of Dead";
    private static final String EDITOR_SECTION_FAITH_POWERS_BOLT = "Faith Power of Bolt";
    private static final String EDITOR_SECTION_FAITH_POWERS_BOOM = "Faith Power of Boom";
    // Editor Section Array
    static final String[] EDITOR_SECTION_ARRAY = new String[NAMES_LENGTH];
    private static String[] TEMP_SECTION_STATS = new String[SECTION_ARRAY_STATS.length];
    private static String[] TEMP_SECTION_EQUIP_SLOTS = new String[SECTION_ARRAY_EQUIP_SLOTS.length];
    private static String[] TEMP_SECTION_EQUIP_ARMOR = new String[SECTION_ARRAY_EQUIP_ARMOR.length];
    private static String[] TEMP_SECTION_WEAPONS_1H = new String[SECTION_ARRAY_WEAPONS_1H.length];
    private static String[] TEMP_SECTION_WEAPONS_2H = new String[SECTION_ARRAY_WEAPONS_2H.length];
    private static String[] TEMP_SECTION_FAITHS = new String[SECTION_ARRAY_FAITHS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_NONE = new String[SECTION_ARRAY_FAITH_POWERS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_HEAT = new String[SECTION_ARRAY_FAITH_POWERS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_COLD = new String[SECTION_ARRAY_FAITH_POWERS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_ROCK = new String[SECTION_ARRAY_FAITH_POWERS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_GUST = new String[SECTION_ARRAY_FAITH_POWERS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_BEAM = new String[SECTION_ARRAY_FAITH_POWERS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_DEAD = new String[SECTION_ARRAY_FAITH_POWERS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_BOLT = new String[SECTION_ARRAY_FAITH_POWERS.length];
    private static String[] TEMP_SECTION_FAITH_POWERS_BOOM = new String[SECTION_ARRAY_FAITH_POWERS.length];
    static {
        Arrays.fill(TEMP_SECTION_STATS, EDITOR_SECTION_STATS);
        Arrays.fill(TEMP_SECTION_EQUIP_SLOTS, EDITOR_SECTION_EQUIP_SLOT);
        Arrays.fill(TEMP_SECTION_EQUIP_ARMOR, EDITOR_SECTION_EQUIP_ARMOR);
        Arrays.fill(TEMP_SECTION_WEAPONS_1H, EDITOR_SECTION_WEAPONS_1H);
        Arrays.fill(TEMP_SECTION_WEAPONS_2H, EDITOR_SECTION_WEAPONS_2H);
        Arrays.fill(TEMP_SECTION_FAITHS, EDITOR_SECTION_FAITHS);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_NONE,
                EDITOR_SECTION_FAITH_POWERS_NONE);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_HEAT,
                EDITOR_SECTION_FAITH_POWERS_HEAT);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_COLD,
                EDITOR_SECTION_FAITH_POWERS_COLD);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_ROCK,
                EDITOR_SECTION_FAITH_POWERS_ROCK);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_GUST,
                EDITOR_SECTION_FAITH_POWERS_GUST);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_BEAM,
                EDITOR_SECTION_FAITH_POWERS_BEAM);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_DEAD,
                EDITOR_SECTION_FAITH_POWERS_DEAD);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_BOLT,
                EDITOR_SECTION_FAITH_POWERS_BOLT);
        Arrays.fill(TEMP_SECTION_FAITH_POWERS_BOOM,
                EDITOR_SECTION_FAITH_POWERS_BOOM);
        int counter = 0;
        System.arraycopy(TEMP_SECTION_STATS, 0, EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_STATS.length);
        counter += TEMP_SECTION_STATS.length;
        System.arraycopy(TEMP_SECTION_EQUIP_SLOTS, 0, EDITOR_SECTION_ARRAY,
                counter, TEMP_SECTION_EQUIP_SLOTS.length);
        counter += TEMP_SECTION_EQUIP_SLOTS.length;
        System.arraycopy(TEMP_SECTION_EQUIP_ARMOR, 0, EDITOR_SECTION_ARRAY,
                counter, TEMP_SECTION_EQUIP_ARMOR.length);
        counter += TEMP_SECTION_EQUIP_ARMOR.length;
        System.arraycopy(TEMP_SECTION_WEAPONS_1H, 0, EDITOR_SECTION_ARRAY,
                counter, TEMP_SECTION_WEAPONS_1H.length);
        counter += TEMP_SECTION_WEAPONS_1H.length;
        System.arraycopy(TEMP_SECTION_WEAPONS_2H, 0, EDITOR_SECTION_ARRAY,
                counter, TEMP_SECTION_WEAPONS_2H.length);
        counter += TEMP_SECTION_WEAPONS_2H.length;
        System.arraycopy(TEMP_SECTION_FAITHS, 0, EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITHS.length);
        counter += TEMP_SECTION_FAITHS.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_NONE, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_NONE.length);
        counter += TEMP_SECTION_FAITH_POWERS_NONE.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_HEAT, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_HEAT.length);
        counter += TEMP_SECTION_FAITH_POWERS_HEAT.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_COLD, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_COLD.length);
        counter += TEMP_SECTION_FAITH_POWERS_COLD.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_ROCK, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_ROCK.length);
        counter += TEMP_SECTION_FAITH_POWERS_ROCK.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_GUST, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_GUST.length);
        counter += TEMP_SECTION_FAITH_POWERS_GUST.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_BEAM, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_BEAM.length);
        counter += TEMP_SECTION_FAITH_POWERS_BEAM.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_DEAD, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_DEAD.length);
        counter += TEMP_SECTION_FAITH_POWERS_DEAD.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_BOLT, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_BOLT.length);
        counter += TEMP_SECTION_FAITH_POWERS_BOLT.length;
        System.arraycopy(TEMP_SECTION_FAITH_POWERS_BOOM, 0,
                EDITOR_SECTION_ARRAY, counter,
                TEMP_SECTION_FAITH_POWERS_BOOM.length);
    }

    // Private constructor
    private NamesConstants() {
        // Do nothing
    }
}
