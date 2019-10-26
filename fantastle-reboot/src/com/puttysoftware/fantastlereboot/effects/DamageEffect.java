package com.puttysoftware.fantastlereboot.effects;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;

public class DamageEffect extends Effect {
  // Constructor
  public DamageEffect(final String buffName, final int HPReduction,
      final int newRounds, final double factor, final int scaleStat,
      final double decay) {
    super(buffName, newRounds);
    this.setAffectedStat(StatConstants.STAT_CURRENT_HP);
    this.setEffect(Effect.EFFECT_ADD, HPReduction, factor, scaleStat);
    this.setDecayRate(decay);
  }

  public DamageEffect(final String buffName, final int HPReduction,
      final int newRounds, final double factor, final int scaleStat,
      final double decay, final double rScaleFactor, final int rScaleStat) {
    super(buffName, newRounds, rScaleFactor, rScaleStat);
    this.setAffectedStat(StatConstants.STAT_CURRENT_HP);
    this.setEffect(Effect.EFFECT_ADD, HPReduction, factor, scaleStat);
    this.setDecayRate(decay);
  }

  @Override
  public void customUseLogic(final Creature c) {
    if (c.isAlive()) {
      c.doDamage((int) this.getEffect(Effect.EFFECT_ADD));
    }
  }
}
