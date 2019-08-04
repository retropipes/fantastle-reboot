package net.worldwizard.fantastle5.ai;

import net.worldwizard.fantastle5.creatures.Creature;
import net.worldwizard.fantastle5.spells.Spell;

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
