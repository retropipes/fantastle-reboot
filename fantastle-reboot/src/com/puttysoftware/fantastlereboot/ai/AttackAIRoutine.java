package com.puttysoftware.fantastlereboot.ai;

import com.puttysoftware.fantastlereboot.creatures.Creature;

public class AttackAIRoutine extends AIRoutine {
    @Override
    public int getNextAction(final Creature c) {
        return AIRoutine.ACTION_ATTACK;
    }
}
