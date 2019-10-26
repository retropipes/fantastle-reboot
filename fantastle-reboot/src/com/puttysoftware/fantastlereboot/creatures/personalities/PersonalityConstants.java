package com.puttysoftware.fantastlereboot.creatures.personalities;

public class PersonalityConstants {
  public static final int PERSONALITY_AGILE = 0;
  public static final int PERSONALITY_BULLY = 1;
  public static final int PERSONALITY_CAREFREE = 2;
  public static final int PERSONALITY_CAUTIOUS = 3;
  public static final int PERSONALITY_DARING = 4;
  public static final int PERSONALITY_FLIPPANT = 5;
  public static final int PERSONALITY_GLUTTON = 6;
  public static final int PERSONALITY_HONEST = 7;
  public static final int PERSONALITY_IMPULSIVE = 8;
  public static final int PERSONALITY_JEALOUS = 9;
  public static final int PERSONALITY_KINDLY = 10;
  public static final int PERSONALITY_LOGICAL = 11;
  public static final int PERSONALITY_LUCKY = 12;
  public static final int PERSONALITY_ORDINARY = 13;
  public static final int PERSONALITY_PROUD = 14;
  public static final int PERSONALITY_QUIET = 15;
  public static final int PERSONALITY_REBEL = 16;
  public static final int PERSONALITY_ROMANTIC = 17;
  public static final int PERSONALITY_SILLY = 18;
  public static final int PERSONALITY_SMART = 19;
  public static final int PERSONALITY_TOUGH = 20;
  public static final int PERSONALITY_UNIQUE = 21;
  public static final int PERSONALITY_VALIANT = 22;
  public static final int PERSONALITY_WEEPY = 23;
  public static final int PERSONALITY_ZEALOUS = 24;
  public static final int PERSONALITIES_COUNT = 25;
  public static final int PERSONALITY_ATTRIBUTE_RANDOM_STRENGTH = 0;
  public static final int PERSONALITY_ATTRIBUTE_RANDOM_BLOCK = 1;
  public static final int PERSONALITY_ATTRIBUTE_RANDOM_AGILITY = 2;
  public static final int PERSONALITY_ATTRIBUTE_RANDOM_VITALITY = 3;
  public static final int PERSONALITY_ATTRIBUTE_RANDOM_INTELLIGENCE = 4;
  public static final int PERSONALITY_ATTRIBUTE_RANDOM_LUCK = 5;
  public static final int PERSONALITY_ATTRIBUTE_LEVEL_UP_SPEED = 6;
  public static final int PERSONALITY_ATTRIBUTE_ACTION_MOD = 7;
  public static final int PERSONALITY_ATTRIBUTE_CAPACITY_MOD = 8;
  public static final int PERSONALITY_ATTRIBUTE_WEALTH_MOD = 9;
  public static final int PERSONALITY_ATTRIBUTES_COUNT = 10;
  public static final String[] PERSONALITY_NAMES = { "Agile", "Bully",
      "Carefree", "Cautious", "Daring", "Flippant", "Glutton", "Honest",
      "Impulsive", "Jealous", "Kindly", "Logical", "Lucky", "Ordinary", "Proud",
      "Quiet", "Rebel", "Romantic", "Silly", "Smart", "Tough", "Unique",
      "Valiant", "Weepy", "Zealous" };
  private static final double[] LOOKUP_TABLE = { 0.5, 0.54, 0.58, 0.63, 0.67,
      0.71, 0.75, 0.79, 0.83, 0.88, 0.92, 0.96, 1.0, 1.08, 1.17, 1.25, 1.33,
      1.42, 1.5, 1.58, 1.67, 1.75, 1.83, 1.92, 2.0 };

  // Private constructor
  private PersonalityConstants() {
    // Do nothing
  }

  // Methods
  public static int getPersonalitiesCount() {
    return PersonalityConstants.PERSONALITIES_COUNT;
  }

  public static String[] getPersonalityNames() {
    return PersonalityConstants.PERSONALITY_NAMES;
  }

  public static String getPersonalityName(final int p) {
    return PersonalityConstants.PERSONALITY_NAMES[p];
  }

  public static double getLookupTableEntry(final int entryNum) {
    return PersonalityConstants.LOOKUP_TABLE[entryNum + 12];
  }
}
