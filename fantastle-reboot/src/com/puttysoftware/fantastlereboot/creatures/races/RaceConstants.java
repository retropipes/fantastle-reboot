package com.puttysoftware.fantastlereboot.creatures.races;

public class RaceConstants {
  public static final int CYBORG = 0;
  public static final int DEMON = 1;
  public static final int DWARF = 2;
  public static final int EAGLE = 3;
  public static final int ELF = 4;
  public static final int GHOST = 5;
  public static final int GNOME = 6;
  public static final int GOBLIN = 7;
  public static final int HUMAN = 8;
  public static final int LIZARD = 9;
  public static final int MUSH = 10;
  public static final int ORC = 11;
  public static final int PENGUIN = 12;
  public static final int PIXIE = 13;
  public static final int ROBOT = 14;
  public static final int SHADE = 15;
  public static final int SLIME = 16;
  public static final int TROLL = 17;
  public static final int TURTLE = 18;
  public static final int WOLF = 19;
  public static final int RACES_COUNT = 20;
  public static final int ATTRIBUTE_STRENGTH_PER_LEVEL = 0;
  public static final int ATTRIBUTE_BLOCK_PER_LEVEL = 1;
  public static final int ATTRIBUTE_AGILITY_PER_LEVEL = 2;
  public static final int ATTRIBUTE_VITALITY_PER_LEVEL = 3;
  public static final int ATTRIBUTE_INTELLIGENCE_PER_LEVEL = 4;
  public static final int ATTRIBUTE_LUCK_PER_LEVEL = 5;
  public static final int ATTRIBUTE_RANDOM_STRENGTH = 6;
  public static final int ATTRIBUTE_RANDOM_BLOCK = 7;
  public static final int ATTRIBUTE_RANDOM_AGILITY = 8;
  public static final int ATTRIBUTE_RANDOM_VITALITY = 9;
  public static final int ATTRIBUTE_RANDOM_INTELLIGENCE = 10;
  public static final int ATTRIBUTE_RANDOM_LUCK = 11;
  public static final int ATTRIBUTES_COUNT = 12;
  public static final String[] NAMES = { "Cyborg", "Demon", "Dwarf", "Eagle",
      "Elf", "Ghost", "Gnome", "Goblin", "Human", "Lizard", "Mush", "Orc",
      "Penguin", "Pixie", "Robot", "Shade", "Slime", "Troll", "Turtle",
      "Wolf" };
  private static final double[] LOOKUP_TABLE = { 0.5, 0.54, 0.58, 0.63, 0.67,
      0.71, 0.75, 0.79, 0.83, 0.88, 0.92, 0.96, 1.0, 1.08, 1.17, 1.25, 1.33,
      1.42, 1.5, 1.58, 1.67, 1.75, 1.83, 1.92, 2.0 };

  public static final String getRaceName(final int index) {
    return RaceConstants.NAMES[index];
  }

  public static String[] getRaceNames() {
    return RaceConstants.NAMES;
  }

  public static double getLookupTableEntry(final int entryNum) {
    return RaceConstants.LOOKUP_TABLE[entryNum + 12];
  }
}
