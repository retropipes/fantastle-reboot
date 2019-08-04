package net.worldwizard.fantastle5.ai;

import com.puttysoftware.randomrange.RandomRange;

import net.worldwizard.fantastle5.creatures.Creature;

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
