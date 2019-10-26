package com.puttysoftware.fantastlereboot.spells;

import java.util.Objects;

import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.effects.Effect;

public class Spell {
  // Fields
  private final Effect effect;
  private final int cost;
  private final BattleTarget target;
  private final SoundIndex soundEffect;

  // Constructors
  public Spell(final Effect newEffect, final int newCost,
      final BattleTarget newTarget) {
    this.effect = newEffect;
    this.cost = newCost;
    this.target = newTarget;
    this.soundEffect = null;
  }

  public Spell(final Effect newEffect, final int newCost,
      final BattleTarget newTarget, final SoundIndex sfx) {
    this.effect = newEffect;
    this.cost = newCost;
    this.target = newTarget;
    this.soundEffect = sfx;
  }

  public Effect getEffect() {
    return this.effect;
  }

  public int getCost() {
    return this.cost;
  }

  public BattleTarget getTarget() {
    return this.target;
  }

  public SoundIndex getSound() {
    return this.soundEffect;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.cost, this.effect, this.soundEffect, this.target);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Spell)) {
      return false;
    }
    Spell other = (Spell) obj;
    return this.cost == other.cost && Objects.equals(this.effect, other.effect)
        && this.soundEffect == other.soundEffect && this.target == other.target;
  }
}