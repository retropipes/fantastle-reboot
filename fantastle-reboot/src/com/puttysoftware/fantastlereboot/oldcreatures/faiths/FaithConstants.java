package com.puttysoftware.fantastlereboot.oldcreatures.faiths;

import java.awt.Color;

public interface FaithConstants {
    int FAITH_BLEND = 0;
    int FAITH_FLAME = 1;
    int FAITH_STORM = 2;
    int FAITH_FLOOD = 3;
    int FAITH_SHOCK = 4;
    int FAITH_CHILL = 5;
    int FAITH_SOUND = 6;
    int FAITH_DEATH = 7;
    int FAITH_GLEAM = 8;
    int FAITH_QUAKE = 9;
    int FAITH_CHAOS = 10;
    int FAITH_PLANT = 11;
    int FAITH_ORDER = 12;
    int FAITH_SMOKE = 13;
    int FAITH_DRAIN = 14;
    int FAITH_BOOST = 15;
    int FAITH_HUMID = 16;
    int FAITH_HEART = 17;
    int FAITH_REACT = 18;
    int FAITH_TOXIN = 19;
    int FAITH_SMART = 20;
    int FAITH_CLOUD = 21;
    int FAITH_NAKED = 22;
    int FAITH_SWAMP = 23;
    int FAITH_GHOST = 24;
    int FAITHS_COUNT = 25;
    String[] FAITH_NAMES = { "Blend", "Flame", "Storm", "Flood", "Shock",
            "Chill", "Sound", "Death", "Gleam", "Quake", "Chaos", "Plant",
            "Order", "Smoke", "Drain", "Boost", "Humid", "Heart", "React",
            "Toxin", "Smart", "Cloud", "Naked", "Swamp", "Ghost" };
    String[] FAITH_DAMAGE_TYPES = { "Physical", "Hot", "Wind", "Water",
            "Electric", "Cold", "Sonic", "Dark", "Light", "Earth", "Chaos",
            "Plant", "Order", "Smoke", "Drain", "Boost", "Sweat", "Love",
            "Chemical", "Poison", "Mental", "Cloud", "Sleaze", "Swamp",
            "Fear" };
    Color[] FAITH_COLORS = { new Color(127, 127, 127), new Color(255, 0, 0),
            new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 255, 0),
            new Color(0, 255, 255), new Color(255, 0, 255), new Color(0, 0, 0),
            new Color(255, 255, 255), new Color(127, 63, 0),
            new Color(127, 0, 0), new Color(0, 127, 0), new Color(0, 0, 127),
            new Color(127, 127, 0), new Color(0, 127, 127),
            new Color(127, 0, 127), new Color(255, 127, 0),
            new Color(255, 0, 127), new Color(127, 255, 0),
            new Color(0, 255, 127), new Color(127, 0, 255),
            new Color(0, 127, 255), new Color(255, 127, 127),
            new Color(127, 255, 127), new Color(127, 127, 255) };
    boolean[] FAITH_DARK_EYES = { false, false, true, false, true, true, true,
            false, true, false, false, false, false, false, false, false, false,
            false, true, true, false, false, true, true, true };
}