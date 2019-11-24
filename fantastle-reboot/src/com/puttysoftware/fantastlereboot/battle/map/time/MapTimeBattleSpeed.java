/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map.time;

import com.puttysoftware.fantastlereboot.gui.Prefs;

class MapTimeBattleSpeed {
  // Constants
  private static int SPEED_FACTOR = 20;

  // Constructor
  private MapTimeBattleSpeed() {
    // Do nothing
  }

  // Method
  static int getSpeed() {
    return Prefs.getBattleSpeed() / MapTimeBattleSpeed.SPEED_FACTOR;
  }
}