/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;

public class MapBattleArrowSpeedConstants {
  // Constants
  private static int ARROW_SPEED_FACTOR = 8;

  // Constructor
  private MapBattleArrowSpeedConstants() {
    // Do nothing
  }

  // Method
  public static int getArrowSpeed() {
    FantastleReboot.getBagOStuff().getPrefsManager();
    return PreferencesManager.getBattleSpeed()
        / MapBattleArrowSpeedConstants.ARROW_SPEED_FACTOR;
  }
}