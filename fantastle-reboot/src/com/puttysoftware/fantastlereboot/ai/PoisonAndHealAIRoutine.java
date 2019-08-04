package com.puttysoftware.fantastlereboot.ai;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.spells.Spell;
import com.puttysoftware.randomrange.RandomRange;

public class PoisonAndHealAIRoutine extends AIRoutine {
    // Fields
    private int poisonRounds;
    private static final int POISON_CHANCE = 75;
    private static final int HEAL_CHANCE = 95;
    private static final double HEAL_PERCENT = 0.25;

    // Constructors
    public PoisonAndHealAIRoutine() {
        this.poisonRounds = 0;
    }

    @Override
    public int getNextAction(final Creature c) {
        if (this.poisonRounds > 0) {
            this.poisonRounds--;
        }
        final RandomRange whichAction = new RandomRange(1, 2);
        final int action = whichAction.generate();
        Spell which = null;
        if (action == 1) {
            which = c.getSpellBook().getSpellByID(0);
        } else {
            which = c.getSpellBook().getSpellByID(1);
        }
        final int cost = which.getCost();
        final int currMP = c.getCurrentMP();
        if (action == 1) {
            if (cost <= currMP && this.poisonRounds == 0) {
                final RandomRange chance = new RandomRange(1, 100);
                if (chance.generate() <= PoisonAndHealAIRoutine.POISON_CHANCE) {
                    this.poisonRounds = which.getEffect().getInitialRounds();
                    this.spell = which;
                    return AIRoutine.ACTION_CAST_SPELL;
                } else {
                    this.spell = null;
                    return AIRoutine.ACTION_ATTACK;
                }
            } else {
                this.spell = null;
                return AIRoutine.ACTION_ATTACK;
            }
        } else {
            if (cost <= currMP) {
                final int currHP = c.getCurrentHP();
                final int targetHP = (int) (currHP
                        * PoisonAndHealAIRoutine.HEAL_PERCENT);
                if (currHP <= targetHP) {
                    final RandomRange chance = new RandomRange(1, 100);
                    if (chance
                            .generate() <= PoisonAndHealAIRoutine.HEAL_CHANCE) {
                        this.spell = which;
                        return AIRoutine.ACTION_CAST_SPELL;
                    } else {
                        this.spell = null;
                        return AIRoutine.ACTION_ATTACK;
                    }
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
}
