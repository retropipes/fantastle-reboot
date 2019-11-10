/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;

public abstract class Battle {
  // Constructors
  protected Battle() {
    // Do nothing
  }

  // Generic Methods
  public abstract void showBattle();

  public abstract void hideBattle();

  public abstract void resetGUI();

  public abstract void doBattle(int x, int y);

  public abstract void doBattleByProxy(int x, int y);

  public abstract void setStatusMessage(final String msg);

  public abstract void executeNextAIAction();

  public abstract boolean getLastAIActionResult();

  public abstract boolean castSpell();

  public abstract boolean useItem();

  public abstract boolean steal();

  public abstract boolean drain();

  public abstract void endTurn();

  public abstract Creature getEnemy();

  public abstract void battleDone();

  public abstract void displayActiveEffects();

  public abstract void displayBattleStats();

  public abstract boolean doPlayerActions(int actionType);

  public abstract BattleResults getResult();

  public abstract void doResult();

  public abstract void setResult(BattleResults resultCode);

  public abstract void maintainEffects(boolean player);

  // Methods specific to map battles
  public abstract boolean updatePosition(int x, int y);

  public abstract void fireArrow(int x, int y);

  public abstract boolean arrowHitCheck(int x, int y);

  public abstract void arrowDone(BattleCharacter hit);

  public abstract void redrawOneBattleSquare(int x, int y,
      FantastleObjectModel obj3);

  public abstract boolean isWaitingForAI();
}
