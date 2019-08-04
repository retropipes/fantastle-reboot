package net.worldwizard.fantastle5.ai;

import net.worldwizard.fantastle5.creatures.Creature;

public class AttackAIRoutine extends AIRoutine {
    @Override
    public int getNextAction(final Creature c) {
        return AIRoutine.ACTION_ATTACK;
    }
}
