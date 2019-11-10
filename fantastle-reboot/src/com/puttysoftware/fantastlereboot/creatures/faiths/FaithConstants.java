package com.puttysoftware.fantastlereboot.creatures.faiths;

import java.awt.Color;
import java.util.Hashtable;

import com.puttysoftware.diane.loaders.ColorShader;

class FaithConstants {
  static final int[] FAITH_DAMAGE_NUMERATORS = new int[] { 1 };
  static final int[] FAITH_DAMAGE_DENOMINATORS = new int[] { 1 };
  static Hashtable<Integer, Color> FAITH_COLOR_CACHE;
  static Hashtable<Integer, ColorShader> FAITH_SHADER_CACHE;
  static String[] FAITH_NAME_CACHE;
  static int[][] FAITH_NUMERATOR_CACHE;
  static int[][] FAITH_DENOMINATOR_CACHE;
}