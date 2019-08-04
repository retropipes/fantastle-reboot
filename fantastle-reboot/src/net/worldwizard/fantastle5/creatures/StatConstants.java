package net.worldwizard.fantastle5.creatures;

public interface StatConstants {
    // Statistics
    int STAT_NONE = -1;
    int STAT_STRENGTH = 0;
    int STAT_BLOCK = 1;
    int STAT_AGILITY = 2;
    int STAT_VITALITY = 3;
    int STAT_INTELLIGENCE = 4;
    int STAT_LUCK = 5;
    int STAT_CURRENT_HP = 6;
    int STAT_CURRENT_MP = 7;
    int STAT_GOLD = 8;
    int STAT_LEVEL = 9;
    int STAT_MAXIMUM_HP = 10;
    int STAT_MAXIMUM_MP = 11;
    int STAT_ATTACK = 12;
    int STAT_DEFENSE = 13;
    int STAT_FUMBLE_CHANCE = 14;
    int STAT_SPEED = 15;
    int MAX_STORED_STATS = 10;
    int MAX_STATS = 16;
    // Factors
    double FACTOR_STRENGTH_ATTACK = 1.0;
    double FACTOR_POWER_ATTACK = 1.0;
    double FACTOR_BLOCK_DEFENSE = 1.0;
    double FACTOR_ABSORB_DEFENSE = 0.1;
    double FACTOR_AGILITY_ACTIONS_PER_ROUND = 0.04;
    double FACTOR_AGILITY_SPEED = 1.0;
    double FACTOR_WEIGHT_SPEED = 1.0;
    double FACTOR_VITALITY_HEALTH = 2.0;
    double FACTOR_INTELLIGENCE_MAGIC = 2.0;
    double FACTOR_LUCK_FUMBLE = 0.5;
    double FACTOR_AGILITY_FUMBLE = 0.5;
    double FACTOR_DIFFERENTIAL_DAMAGE = 0.5;
    double FACTOR_TWO_HANDED_BONUS = 1.5;
    // Base Gains Per Level
    int GAIN_STRENGTH = 5;
    int GAIN_BLOCK = 5;
    int GAIN_AGILITY = 5;
    int GAIN_VITALITY = 5;
    int GAIN_INTELLIGENCE = 5;
    int GAIN_LUCK = 5;
    // Base Chances
    int CHANCE_STEAL = 50;
    int CHANCE_DRAIN = 50;
}