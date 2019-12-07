package com.puttysoftware.fantastlereboot.effects;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;

public class HealingEffect extends Effect {
  // Constructor
  public HealingEffect(final String buffName, final int HPAddition,
      final int newRounds, final double factor, final int scaleStat,
      final double decay) {
    super(buffName, newRounds);
    this.setAffectedStat(StatConstants.STAT_CURRENT_HP);
    this.setEffect(Effect.EFFECT_ADD, HPAddition, factor, scaleStat);
    this.setDecayRate(decay);
  }

  @Override
  public void customUseLogic(final Creature c) {
    if (c.isAlive()) {
      c.heal((int) this.getEffect(Effect.EFFECT_ADD));
    }
  }
}
