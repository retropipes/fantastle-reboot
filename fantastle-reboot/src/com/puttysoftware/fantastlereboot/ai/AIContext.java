/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ai;

import java.awt.Point;

import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.fantastlereboot.world.World;

public abstract class AIContext {
  protected final BattleCharacter aiCreature;
  protected final int myTeam;
  protected static final int MINIMUM_RADIUS = 1;
  protected static final int MAXIMUM_RADIUS = 16;
  protected static final int NOTHING_THERE = -1;
  protected static final int CANNOT_MOVE_THERE = -1;
  protected static final int DEFAULT_AP_COST = 1;

  // Constructor
  protected AIContext(final BattleCharacter creature) {
    this.aiCreature = creature;
    this.myTeam = creature.getTeamID();
  }

  // Static method
  public static int getDefaultAPCost() {
    return AIContext.DEFAULT_AP_COST;
  }

  // Methods
  public abstract void updateContext(final World world);

  public final BattleCharacter getCharacter() {
    return this.aiCreature;
  }

  public final Point isEnemyNearby() {
    return this.isEnemyNearby(1, 1);
  }

  public abstract Point isEnemyNearby(final int minRadius, final int maxRadius);

  public abstract Point runAway();
}
