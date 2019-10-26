/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items.combat.predefined;

import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.BattleTarget;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.items.combat.CombatItem;

public class Fireball extends CombatItem {
  public Fireball() {
    super("Fireball", 500, BattleTarget.ENEMY);
  }

  @Override
  protected void defineFields() {
    this.sound = SoundIndex.BOLT;
    this.e = new Effect("Fireball", 1);
    this.e.setAffectedStat(StatConstants.STAT_CURRENT_HP);
    this.e.setEffect(Effect.EFFECT_ADD, -3);
    this.e.setScaleStat(StatConstants.STAT_LEVEL);
    this.e.setScaleFactor(1.5);
    this.e.setMessage(Effect.MESSAGE_INITIAL,
        "You throw a fireball at the enemy!");
    this.e.setMessage(Effect.MESSAGE_SUBSEQUENT,
        "The fireball sears the enemy badly, dealing LOTS of damage!");
  }
}
