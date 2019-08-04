package net.worldwizard.fantastle5.creatures;

public interface StatConstants {
    // Statistics
    public static final int STAT_NONE = -1;
    public static final int STAT_STRENGTH = 0;
    public static final int STAT_BLOCK = 1;
    public static final int STAT_AGILITY = 2;
    public static final int STAT_VITALITY = 3;
    public static final int STAT_INTELLIGENCE = 4;
    public static final int STAT_LUCK = 5;
    public static final int STAT_CURRENT_HP = 6;
    public static final int STAT_CURRENT_MP = 7;
    public static final int STAT_GOLD = 8;
    public static final int STAT_LEVEL = 9;
    public static final int STAT_MAXIMUM_HP = 10;
    public static final int STAT_MAXIMUM_MP = 11;
    public static final int STAT_ATTACK = 12;
    public static final int STAT_DEFENSE = 13;
    public static final int STAT_FUMBLE_CHANCE = 14;
    public static final int STAT_SPEED = 15;
    public static final int MAX_STORED_STATS = 10;
    public static final int MAX_STATS = 16;
    // Factors
    public static final double FACTOR_STRENGTH_ATTACK = 1.0;
    public static final double FACTOR_POWER_ATTACK = 1.0;
    public static final double FACTOR_BLOCK_DEFENSE = 1.0;
    public static final double FACTOR_ABSORB_DEFENSE = 0.1;
    public static final double FACTOR_AGILITY_ACTIONS_PER_ROUND = 0.04;
    public static final double FACTOR_AGILITY_SPEED = 1.0;
    public static final double FACTOR_WEIGHT_SPEED = 1.0;
    public static final double FACTOR_VITALITY_HEALTH = 2.0;
    public static final double FACTOR_INTELLIGENCE_MAGIC = 2.0;
    public static final double FACTOR_LUCK_FUMBLE = 0.5;
    public static final double FACTOR_AGILITY_FUMBLE = 0.5;
    public static final double FACTOR_DIFFERENTIAL_DAMAGE = 0.5;
    public static final double FACTOR_TWO_HANDED_BONUS = 1.5;
    // Base Gains Per Level
    public static final int GAIN_STRENGTH = 5;
    public static final int GAIN_BLOCK = 5;
    public static final int GAIN_AGILITY = 5;
    public static final int GAIN_VITALITY = 5;
    public static final int GAIN_INTELLIGENCE = 5;
    public static final int GAIN_LUCK = 5;
    // Base Chances
    public static final int CHANCE_STEAL = 50;
    public static final int CHANCE_DRAIN = 50;
}