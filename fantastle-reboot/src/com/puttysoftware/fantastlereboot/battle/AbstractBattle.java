/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle;

import javax.swing.JFrame;

import com.puttysoftware.fantastlereboot.creatures.AbstractCreature;
import com.puttysoftware.fantastlereboot.ttmaze.abc.AbstractMazeObject;
import com.puttysoftware.fantastlereboot.ttmaze.objects.BattleCharacter;

public abstract class AbstractBattle {
    // Constructors
    protected AbstractBattle() {
        // Do nothing
    }

    // Generic Methods
    public abstract JFrame getOutputFrame();

    public abstract void resetGUI();

    public abstract void doBattle();

    public abstract void doBattleByProxy();

    public abstract void setStatusMessage(final String msg);

    public abstract void executeNextAIAction();

    public abstract boolean getLastAIActionResult();

    public abstract boolean castSpell();

    public abstract boolean useItem();

    public abstract boolean steal();

    public abstract boolean drain();

    public abstract void endTurn();

    public abstract AbstractCreature getEnemy();

    public abstract void battleDone();

    public abstract void displayActiveEffects();

    public abstract void displayBattleStats();

    public abstract boolean doPlayerActions(final int actionType);

    public abstract int getResult();

    public abstract void doResult();

    public abstract void setResult(final int resultCode);

    public abstract void maintainEffects(final boolean player);

    // Methods specific to map battles
    public abstract boolean updatePosition(int x, int y);

    public abstract void fireArrow(int x, int y);

    public abstract void arrowDone(BattleCharacter hit);

    public abstract void redrawOneBattleSquare(int x, int y,
            AbstractMazeObject obj3);

    public abstract boolean isWaitingForAI();
}
