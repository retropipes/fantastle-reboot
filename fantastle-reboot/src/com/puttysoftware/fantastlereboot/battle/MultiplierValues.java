package com.puttysoftware.fantastlereboot.battle;

import com.puttysoftware.randomrange.RandomRange;

public class MultiplierValues {
  private static final double tolerance = 0.001;
  private static final double[] values = { 0.25, 1.0 / 3.0, 0.5, 2.0 / 3.0,
      0.75, 1.0, 4.0 / 3.0, 1.5, 2.0, 3.0, 4.0 };
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
    final RandomRange r = new RandomRange(0,
        MultiplierValues.bellCurve.length - 1);
    final int index = r.generate();
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
      return "pathetic ";
    case 1:
      return "one-third normal ";
    case 2:
      return "one-half normal ";
    case 3:
      return "below normal ";
    case 4:
      return "slightly below normal ";
    case 5:
      return "";
    case 6:
      return "slightly above normal ";
    case 7:
      return "above normal ";
    case 8:
      return "double ";
    case 9:
      return "triple ";
    case 10:
      return "brutal ";
    default:
      return "an unknown percentage of normal ";
    }
  }
}
