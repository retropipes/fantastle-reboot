package com.puttysoftware.fantastlereboot.ai;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.spells.Spell;

public abstract class AIRoutine {
  // Fields
  protected Spell spell;
  public static final int ACTION_MOVE = 0;
  public static final int ACTION_FLEE = 1;
  public static final int ACTION_CAST_SPELL = 2;
  public static final int ACTION_STEAL = 3;
  public static final int ACTION_DRAIN = 4;
  public static final int ACTION_USE_ITEM = 5;

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
