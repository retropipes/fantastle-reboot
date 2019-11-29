/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.gui.Prefs;

public class MapAITask extends Thread {
  // Fields
  private final Battle b;

  // Constructors
  public MapAITask(final Battle battle) {
    this.setName("Map AI Runner");
    this.b = battle;
  }

  @Override
  public void run() {
    try {
      boolean keepGoing = true;
      while (keepGoing) {
        keepGoing = this.b.executeNextAIAction();
        if (keepGoing) {
          if (this.b.getLastAIActionResult()) {
            // Delay, for animation purposes
            try {
              final int battleSpeed = Prefs.getBattleSpeed();
              Thread.sleep(battleSpeed);
            } catch (final InterruptedException i) {
              // Ignore
            }
          }
        }
      }
    } catch (final Throwable t) {
      FantastleReboot.exception(t);
    }
  }
}
