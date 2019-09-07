/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map.turn;

import com.puttysoftware.fantastlereboot.battle.AbstractBattle;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;
import com.puttysoftware.fantastlereboot.ttprefs.PreferencesManager;

public class MapTurnBattleAITask extends Thread {
    // Fields
    private final AbstractBattle b;

    // Constructors
    public MapTurnBattleAITask(final AbstractBattle battle) {
        this.setName("Map AI Runner");
        this.b = battle;
    }

    @Override
    public void run() {
        try {
            this.aiWait();
            while (true) {
                this.b.executeNextAIAction();
                if (this.b.getLastAIActionResult()) {
                    // Delay, for animation purposes
                    try {
                        final int battleSpeed = PreferencesManager
                                .getBattleSpeed();
                        Thread.sleep(battleSpeed);
                    } catch (final InterruptedException i) {
                        // Ignore
                    }
                }
            }
        } catch (final Throwable t) {
            TallerTower.getErrorLogger().logError(t);
        }
    }

    public synchronized void aiWait() {
        try {
            this.wait();
        } catch (final InterruptedException e) {
            // Ignore
        }
    }

    public synchronized void aiRun() {
        this.notify();
    }
}
