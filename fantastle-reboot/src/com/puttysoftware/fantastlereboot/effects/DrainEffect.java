package com.puttysoftware.fantastlereboot.effects;

import com.puttysoftware.fantastlereboot.oldcreatures.Creature;
import com.puttysoftware.fantastlereboot.oldcreatures.StatConstants;

public class DrainEffect extends Effect {
    // Constructor
    public DrainEffect(final String buffName, final int MPReduction,
            final int newRounds, final double factor, final int scaleStat,
            final double decay) {
        super(buffName, newRounds);
        this.setAffectedStat(StatConstants.STAT_CURRENT_MP);
        this.setEffect(Effect.EFFECT_ADD, MPReduction, factor, scaleStat);
        this.setDecayRate(decay);
    }

    public DrainEffect(final String buffName, final int MPReduction,
            final int newRounds, final double factor, final int scaleStat,
            final double decay, final double rScaleFactor,
            final int rScaleStat) {
        super(buffName, newRounds, rScaleFactor, rScaleStat);
        this.setAffectedStat(StatConstants.STAT_CURRENT_MP);
        this.setEffect(Effect.EFFECT_ADD, MPReduction, factor, scaleStat);
        this.setDecayRate(decay);
    }

    @Override
    public void customUseLogic(final Creature c) {
        if (c.isAlive()) {
            c.drain((int) this.getEffect(Effect.EFFECT_ADD));
        }
    }
}
