package com.puttysoftware.fantastlereboot.ai;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.spells.Spell;

public abstract class AIRoutine {
  // Fields
  protected Spell spell;
  public static final int ACTION_MOVE = 0;
  public static final int ACTION_CAST_SPELL = 1;
  public static final int ACTION_STEAL = 2;
  public static final int ACTION_DRAIN = 3;
  public static final int ACTION_USE_ITEM = 4;
  public static final int ACTION_END_TURN = 5;

  // Constructor
  protected AIRoutine() {
    super();
  }

  // Methods
  public abstract int getNextAction(Creature c, AIContext ac);

  public final Spell getSpellToCast() {
    return this.spell;
  }
}
