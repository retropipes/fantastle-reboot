package com.puttysoftware.fantastlereboot.ai;

import com.puttysoftware.fantastlereboot.oldcreatures.Creature;
import com.puttysoftware.fantastlereboot.spells.Spell;

public abstract class AIRoutine {
    // Fields
    protected Spell spell;
    public static final int ACTION_ATTACK = 0;
    public static final int ACTION_FLEE = 1;
    public static final int ACTION_CAST_SPELL = 2;
    public static final int ACTION_STEAL = 3;
    public static final int ACTION_DRAIN = 4;
    public static final int ACTION_USE_ITEM = 5;

    // Methods
    public abstract int getNextAction(Creature c);

    public final Spell getSpellToCast() {
        return this.spell;
    }
}
