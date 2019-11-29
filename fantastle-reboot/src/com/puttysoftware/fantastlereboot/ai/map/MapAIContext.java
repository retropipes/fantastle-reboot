/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ai.map;

import java.awt.Point;

import com.puttysoftware.fantastlereboot.ai.AIContext;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.fantastlereboot.world.World;

public class MapAIContext extends AIContext {
  private final int[][] apCosts;
  private final int[][] creatureLocations;
  private final int xBound;
  private final int yBound;

  // Constructor
  public MapAIContext(final BattleCharacter creature, final World world) {
    super(creature);
    this.xBound = world.getRows();
    this.yBound = world.getColumns();
    this.apCosts = new int[this.xBound][this.yBound];
    this.creatureLocations = new int[this.xBound][this.yBound];
  }

  // Methods
  @Override
  public void updateContext(final World world) {
    for (int x = 0; x < this.apCosts.length; x++) {
      for (int y = 0; y < this.apCosts[x].length; y++) {
        final FantastleObjectModel obj = world.getCell(x, y, 0, Layers.OBJECT);
        if (obj.isSolid()) {
          this.apCosts[x][y] = AIContext.CANNOT_MOVE_THERE;
        } else {
          this.apCosts[x][y] = Battle.AP_MOVE;
        }
      }
    }
    for (int x = 0; x < this.creatureLocations.length; x++) {
      for (int y = 0; y < this.creatureLocations[x].length; y++) {
        final FantastleObjectModel obj = world.getCell(x, y, 0, Layers.OBJECT);
        if (obj instanceof BattleCharacter) {
          final BattleCharacter bc = (BattleCharacter) obj;
          this.creatureLocations[x][y] = bc.getTeamID();
        } else {
          this.creatureLocations[x][y] = AIContext.NOTHING_THERE;
        }
      }
    }
  }

  private boolean rangeCheck(final int x, final int y) {
    return x >= 0 && x < this.xBound && y >= 0 && y < this.yBound;
  }

  @Override
  public Point isEnemyNearby(final int minRadius, final int maxRadius) {
    int fMinR = minRadius;
    int fMaxR = maxRadius;
    if (fMaxR > AIContext.MAXIMUM_RADIUS) {
      fMaxR = AIContext.MAXIMUM_RADIUS;
    }
    if (fMaxR < AIContext.MINIMUM_RADIUS) {
      fMaxR = AIContext.MINIMUM_RADIUS;
    }
    if (fMinR > AIContext.MAXIMUM_RADIUS) {
      fMinR = AIContext.MAXIMUM_RADIUS;
    }
    if (fMinR < AIContext.MINIMUM_RADIUS) {
      fMinR = AIContext.MINIMUM_RADIUS;
    }
    final int x = this.aiCreature.getX();
    final int y = this.aiCreature.getY();
    int u, v;
    for (u = x - fMaxR; u <= x + fMaxR; u++) {
      for (v = y - fMaxR; v <= y + fMaxR; v++) {
        if (Math.abs(u - x) < fMinR && Math.abs(v - y) < fMinR) {
          continue;
        }
        if (this.rangeCheck(u, v) && this.rangeCheck(u - x, v - y)) {
          if (this.creatureLocations[u][v] != -1
              && this.creatureLocations[u][v] != this.myTeam) {
            return new Point(u - x, v - y);
          }
        }
      }
    }
    return null;
  }

  @Override
  public Point runAway() {
    final int fMinR = AIContext.MAXIMUM_RADIUS;
    final int fMaxR = AIContext.MAXIMUM_RADIUS;
    final int x = this.aiCreature.getX();
    final int y = this.aiCreature.getY();
    int u, v;
    for (u = x - fMaxR; u <= x + fMaxR; u++) {
      for (v = y - fMaxR; v <= y + fMaxR; v++) {
        if (Math.abs(u - x) < fMinR && Math.abs(v - y) < fMinR) {
          continue;
        }
        if (this.rangeCheck(u, v)) {
          if (this.creatureLocations[u][v] != -1
              && this.creatureLocations[u][v] != this.myTeam) {
            return new Point(u + x, v + y);
          }
        }
      }
    }
    return null;
  }
}
