package com.puttysoftware.fantastlereboot.creatures.faiths;

import java.awt.Color;

public class FaithConstants {
  public static final int FAITH_BLEND = 0;
  public static final int FAITH_FLAME = 1;
  public static final int FAITH_STORM = 2;
  public static final int FAITH_FLOOD = 3;
  public static final int FAITH_SHOCK = 4;
  public static final int FAITH_CHILL = 5;
  public static final int FAITH_SOUND = 6;
  public static final int FAITH_DEATH = 7;
  public static final int FAITH_GLEAM = 8;
  public static final int FAITH_QUAKE = 9;
  public static final int FAITH_CHAOS = 10;
  public static final int FAITH_PLANT = 11;
  public static final int FAITH_ORDER = 12;
  public static final int FAITH_SMOKE = 13;
  public static final int FAITH_DRAIN = 14;
  public static final int FAITH_BOOST = 15;
  public static final int FAITH_HUMID = 16;
  public static final int FAITH_HEART = 17;
  public static final int FAITH_REACT = 18;
  public static final int FAITH_TOXIN = 19;
  public static final int FAITH_SMART = 20;
  public static final int FAITH_CLOUD = 21;
  public static final int FAITH_NAKED = 22;
  public static final int FAITH_SWAMP = 23;
  public static final int FAITH_GHOST = 24;
  public static final int FAITHS_COUNT = 25;
  public static final String[] FAITH_NAMES = { "Blend", "Flame", "Storm",
      "Flood", "Shock", "Chill", "Sound", "Death", "Gleam", "Quake", "Chaos",
      "Plant", "Order", "Smoke", "Drain", "Boost", "Humid", "Heart", "React",
      "Toxin", "Smart", "Cloud", "Naked", "Swamp", "Ghost" };
  public static final String[] FAITH_DAMAGE_TYPES = { "Physical", "Hot", "Wind",
      "Water", "Electric", "Cold", "Sonic", "Dark", "Light", "Earth", "Chaos",
      "Plant", "Order", "Smoke", "Drain", "Boost", "Sweat", "Love", "Chemical",
      "Poison", "Mental", "Cloud", "Sleaze", "Swamp", "Fear" };
  public static final String[][] FAITH_POWER_NAMES = { { "Physical" },
      { "Hot" }, { "Wind" }, { "Water", "Electric" }, { "Cold" }, { "Sonic" },
      { "Dark" }, { "Light" }, { "Earth" }, { "Chaos", "Plant" }, { "Order" },
      { "Smoke" }, { "Drain" }, { "Boost" }, { "Sweat" },
      { "Love", "Chemical" }, { "Poison" }, { "Mental" }, { "Cloud" },
      { "Sleaze" }, { "Swamp", "Fear" } };
  public static final Color[] FAITH_COLORS = { new Color(127, 127, 127),
      new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255),
      new Color(255, 255, 0), new Color(0, 255, 255), new Color(255, 0, 255),
      new Color(0, 0, 0), new Color(255, 255, 255), new Color(127, 63, 0),
      new Color(127, 0, 0), new Color(0, 127, 0), new Color(0, 0, 127),
      new Color(127, 127, 0), new Color(0, 127, 127), new Color(127, 0, 127),
      new Color(255, 127, 0), new Color(255, 0, 127), new Color(127, 255, 0),
      new Color(0, 255, 127), new Color(127, 0, 255), new Color(0, 127, 255),
      new Color(255, 127, 127), new Color(127, 255, 127),
      new Color(127, 127, 255) };
  public static final boolean[] FAITH_DARK_EYES = { false, false, true, false,
      true, true, true, false, true, false, false, false, false, false, false,
      false, false, false, true, true, false, false, true, true, true };

  public static String getFaithName(final int faithID) {
    return FaithConstants.FAITH_NAMES[faithID];
  }

  public static String getFaithPowerName(final int faithID, final int powerID) {
    return FaithConstants.FAITH_POWER_NAMES[faithID][powerID];
  }
}