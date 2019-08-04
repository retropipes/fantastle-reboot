package net.worldwizard.fantastle5.ai;

import com.puttysoftware.randomrange.RandomRange;

import net.worldwizard.fantastle5.creatures.Creature;
import net.worldwizard.fantastle5.spells.Spell;

public class BossAIRoutine extends AIRoutine {
    // Fields
    private int delevelRounds;
    private int chargeRounds;
    private int poisonRounds;
    private static final int DELEVEL_CHANCE = 90;
    private static final int CHARGE_CHANCE = 90;
    private static final int POISON_CHANCE = 90;
    private static final int HEAL_CHANCE = 99;
    private static final double HEAL_PERCENT = 0.4;

    // Constructors
    public BossAIRoutine() {
        this.delevelRounds = 0;
        this.chargeRounds = 0;
        this.poisonRounds = 0;
    }

    @Override
    public int getNextAction(final Creature c) {
        if (this.delevelRounds > 0) {
            this.delevelRounds--;
        }
        if (this.chargeRounds > 0) {
            this.chargeRounds--;
        }
        if (this.poisonRounds > 0) {
            this.poisonRounds--;
        }
        Spell which = null;
        final RandomRange whichAction = new RandomRange(1, 4);
        final RandomRange whichSpell = new RandomRange(1, 2);
        final int action = whichAction.generate();
        if (action == 1) {
            if (whichSpell.generate() == 1) {
                which = c.getSpellBook().getSpellByID(2);
            } else {
                which = c.getSpellBook().getSpellByID(3);
            }
        } else if (action == 2) {
            if (whichSpell.generate() == 1) {
                which = c.getSpellBook().getSpellByID(4);
            } else {
                which = c.getSpellBook().getSpellByID(5);
            }
        } else if (action == 3) {
            which = c.getSpellBook().getSpellByID(0);
        } else {
            which = c.getSpellBook().getSpellByID(1);
        }
        final int cost = which.getCost();
        final int currMP = c.getCurrentMP();
        if (cost <= currMP) {
            final RandomRange chance = new RandomRange(1, 100);
            if (action == 1) {
                if (this.delevelRounds == 0) {
                    if (chance.generate() <= BossAIRoutine.DELEVEL_CHANCE) {
                        this.delevelRounds = which.getEffect()
                                .getInitialRounds();
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
            } else if (action == 2) {
                if (this.chargeRounds == 0) {
                    if (chance.generate() <= BossAIRoutine.CHARGE_CHANCE) {
                        this.chargeRounds = which.getEffect()
                                .getInitialRounds();
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
            } else if (action == 3) {
                if (cost <= currMP && this.poisonRounds == 0) {
                    if (chance.generate() <= BossAIRoutine.POISON_CHANCE) {
                        this.poisonRounds = which.getEffect()
                                .getInitialRounds();
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
                    final int targetHP = (int) (currHP * BossAIRoutine.HEAL_PERCENT);
                    if (currHP <= targetHP) {
                        if (chance.generate() <= BossAIRoutine.HEAL_CHANCE) {
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
        } else {
            this.spell = null;
            return AIRoutine.ACTION_ATTACK;
        }
    }
}
