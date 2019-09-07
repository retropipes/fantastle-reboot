package com.puttysoftware.fantastlereboot.ai;

import com.puttysoftware.fantastlereboot.oldcreatures.Creature;
import com.puttysoftware.fantastlereboot.spells.Spell;
import com.puttysoftware.randomrange.RandomRange;

public class PoisonThenAttackAIRoutine extends AIRoutine {
    // Fields
    private int poisonRounds;
    private static final int POISON_CHANCE = 75;

    // Constructors
    public PoisonThenAttackAIRoutine() {
        this.poisonRounds = 0;
    }

    @Override
    public int getNextAction(final Creature c) {
        if (this.poisonRounds > 0) {
            this.poisonRounds--;
        }
        final Spell poison = c.getSpellBook().getSpellByID(0);
        final int cost = poison.getCost();
        final int currMP = c.getCurrentMP();
        if (cost <= currMP && this.poisonRounds == 0) {
            final RandomRange chance = new RandomRange(1, 100);
            if (chance.generate() <= PoisonThenAttackAIRoutine.POISON_CHANCE) {
                this.poisonRounds = poison.getEffect().getInitialRounds();
                this.spell = poison;
                return AIRoutine.ACTION_CAST_SPELL;
            } else {
                this.spell = null;
                return AIRoutine.ACTION_ATTACK;
            }
        } else {
            this.spell = null;
            return AIRoutine.ACTION_ATTACK;
        }
    }
}
