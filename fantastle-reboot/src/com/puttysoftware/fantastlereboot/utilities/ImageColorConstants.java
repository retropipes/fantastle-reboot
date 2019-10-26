/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.utilities;

import java.awt.Color;

public class ImageColorConstants {
  public static final int COLOR_NONE = -1;
  private static final int COLOR_00 = new Color(127, 127, 255).getRGB();
  private static final int COLOR_01 = new Color(127, 159, 255).getRGB();
  private static final int COLOR_02 = new Color(159, 127, 255).getRGB();
  private static final int COLOR_03 = new Color(159, 159, 255).getRGB();
  private static final int COLOR_04 = new Color(127, 191, 255).getRGB();
  private static final int COLOR_05 = new Color(159, 191, 255).getRGB();
  private static final int COLOR_06 = new Color(191, 191, 255).getRGB();
  private static final int COLOR_07 = new Color(127, 223, 255).getRGB();
  private static final int COLOR_08 = new Color(159, 223, 255).getRGB();
  private static final int COLOR_09 = new Color(191, 223, 255).getRGB();
  private static final int COLOR_10 = new Color(223, 191, 255).getRGB();
  private static final int COLOR_11 = new Color(223, 223, 255).getRGB();
  private static final int COLOR_12 = new Color(0, 255, 255).getRGB();
  private static final int COLOR_13 = new Color(127, 255, 255).getRGB();
  private static final int COLOR_14 = new Color(159, 255, 255).getRGB();
  private static final int COLOR_15 = new Color(191, 255, 255).getRGB();
  private static final int COLOR_16 = new Color(223, 255, 255).getRGB();
  private static final int COLOR_17 = new Color(0, 255, 0).getRGB();
  private static final int COLOR_18 = new Color(0, 255, 63).getRGB();
  private static final int COLOR_19 = new Color(0, 255, 127).getRGB();
  private static final int COLOR_20 = new Color(0, 255, 191).getRGB();
  private static final int COLOR_21 = new Color(63, 255, 0).getRGB();
  private static final int COLOR_22 = new Color(127, 255, 0).getRGB();
  private static final int COLOR_23 = new Color(191, 255, 0).getRGB();
  private static final int COLOR_24 = new Color(63, 255, 63).getRGB();
  private static final int COLOR_25 = new Color(63, 255, 127).getRGB();
  private static final int COLOR_26 = new Color(127, 255, 63).getRGB();
  private static final int COLOR_27 = new Color(127, 255, 127).getRGB();
  private static final int COLOR_28 = new Color(63, 255, 191).getRGB();
  private static final int COLOR_29 = new Color(191, 255, 63).getRGB();
  private static final int COLOR_30 = new Color(255, 255, 0).getRGB();
  private static final int COLOR_31 = new Color(255, 255, 63).getRGB();
  private static final int COLOR_32 = new Color(255, 255, 127).getRGB();
  private static final int COLOR_33 = new Color(255, 255, 191).getRGB();
  private static final int COLOR_34 = new Color(255, 0, 0).getRGB();
  private static final int COLOR_35 = new Color(255, 63, 0).getRGB();
  private static final int COLOR_36 = new Color(255, 127, 0).getRGB();
  private static final int COLOR_37 = new Color(255, 191, 0).getRGB();
  private static final int COLOR_38 = new Color(255, 0, 63).getRGB();
  private static final int COLOR_39 = new Color(255, 0, 127).getRGB();
  private static final int COLOR_40 = new Color(255, 0, 191).getRGB();
  private static final int COLOR_41 = new Color(255, 63, 63).getRGB();
  private static final int COLOR_42 = new Color(255, 63, 127).getRGB();
  private static final int COLOR_43 = new Color(255, 127, 63).getRGB();
  private static final int COLOR_44 = new Color(255, 127, 127).getRGB();
  private static final int COLOR_45 = new Color(255, 63, 191).getRGB();
  private static final int COLOR_46 = new Color(255, 127, 191).getRGB();
  private static final int COLOR_47 = new Color(255, 191, 63).getRGB();
  private static final int COLOR_48 = new Color(255, 191, 127).getRGB();
  private static final int COLOR_49 = new Color(255, 191, 191).getRGB();
  private static final int COLOR_50 = new Color(255, 0, 255).getRGB();
  private static final int COLOR_51 = new Color(255, 63, 255).getRGB();
  private static final int COLOR_52 = new Color(255, 127, 255).getRGB();
  private static final int COLOR_53 = new Color(255, 191, 255).getRGB();
  private static final int COLOR_54 = new Color(255, 255, 255).getRGB();
  private static final int[] LEVEL_COLORS = new int[] { COLOR_00, COLOR_01,
      COLOR_02, COLOR_03, COLOR_04, COLOR_05, COLOR_06, COLOR_07, COLOR_08,
      COLOR_09, COLOR_10, COLOR_11, COLOR_12, COLOR_13, COLOR_14, COLOR_15,
      COLOR_16, COLOR_17, COLOR_18, COLOR_19, COLOR_20, COLOR_21, COLOR_22,
      COLOR_23, COLOR_24, COLOR_25, COLOR_26, COLOR_27, COLOR_28, COLOR_29,
      COLOR_30, COLOR_31, COLOR_32, COLOR_33, COLOR_34, COLOR_35, COLOR_36,
      COLOR_37, COLOR_38, COLOR_39, COLOR_40, COLOR_41, COLOR_42, COLOR_43,
      COLOR_44, COLOR_45, COLOR_46, COLOR_47, COLOR_48, COLOR_49, COLOR_50,
      COLOR_51, COLOR_52, COLOR_53, COLOR_54 };

  public static int getColorForLevel(final int level) {
    return LEVEL_COLORS[level];
  }
}
