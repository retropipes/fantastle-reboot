package com.puttysoftware.fantastlereboot.ai;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.randomrange.RandomRange;

public class RandomlyFleeAIRoutine extends AIRoutine {
    @Override
    public int getNextAction(final Creature c) {
        final RandomRange chance = new RandomRange(1, 100);
        final RandomRange flee = new RandomRange(1, 100);
        if (chance.generate() <= flee.generate()) {
            return AIRoutine.ACTION_FLEE;
        } else {
            return AIRoutine.ACTION_ATTACK;
        }
    }
}
