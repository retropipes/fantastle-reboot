package com.puttysoftware.fantastlereboot.battle.damageengines;

import com.puttysoftware.randomrange.RandomRange;

class MultiplierValues {
  private static final double tolerance = 0.001;
  private static final double[] values = { 0.1, 0.25, 0.33, 0.5, 0.67, 0.75,
      1.0, 1.33, 1.5, 2.0, 3.0, 4.0, 10.0 };
  private static final int[] bellCurve = { 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2,
      2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
      4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6,
      6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8,
      8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10 };

  private MultiplierValues() {
    // Do nothing
  }

  private static double getValue(final int type) {
    return MultiplierValues.values[type];
  }

  public static double getRandomNormalValue() {
    final int index = RandomRange.generate(0,
        MultiplierValues.bellCurve.length - 1);
    return MultiplierValues.getValue(MultiplierValues.bellCurve[index]);
  }

  public static String getTextForValue(final double value) {
    int index;
    for (index = 0; index < MultiplierValues.values.length; index++) {
      if (Math.abs(value
          - MultiplierValues.values[index]) < MultiplierValues.tolerance) {
        break;
      }
    }
    switch (index) {
    case 0:
      return " (minimal 0.1x)";
    case 1:
      return " (tiny 0.25x)";
    case 2:
      return " (small 0.33x)";
    case 3:
      return " (weak 0.5x)";
    case 4:
      return " (lowered 0.67x)";
    case 5:
      return " (reduced 0.75x)";
    case 6:
      return " (normal 1x)";
    case 7:
      return " (increased 1.33x)";
    case 8:
      return " (raised 1.5x)";
    case 9:
      return " (strong 2x)";
    case 10:
      return " (large 3x)";
    case 11:
      return " (huge 4x)";
    case 12:
      return " (brutal 10x)";
    default:
      return " (unknown ??x)";
    }
  }
}
