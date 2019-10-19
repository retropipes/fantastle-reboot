/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ai.map;

import java.awt.Point;

import com.puttysoftware.fantastlereboot.ai.AIContext;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractMazeObject;
import com.puttysoftware.fantastlereboot.obsolete.maze2.objects.BattleCharacter;

public class MapAIContext extends AIContext {
    private final int[][] apCosts;
    private final int[][] creatureLocations;

    // Constructor
    public MapAIContext(final BattleCharacter creature, final Maze arena) {
        super(creature);
        this.apCosts = new int[arena.getRows()][arena.getColumns()];
        this.creatureLocations = new int[arena.getRows()][arena.getColumns()];
    }

    // Methods
    @Override
    public void updateContext(final Maze arena) {
        for (int x = 0; x < this.apCosts.length; x++) {
            for (int y = 0; y < this.apCosts[x].length; y++) {
                final AbstractMazeObject obj = arena.getCell(x, y, 0,
                        MazeConstants.LAYER_OBJECT);
                if (obj.isSolid()) {
                    this.apCosts[x][y] = AIContext.CANNOT_MOVE_THERE;
                } else {
                    this.apCosts[x][y] = AIContext.DEFAULT_AP_COST;
                }
            }
        }
        for (int x = 0; x < this.creatureLocations.length; x++) {
            for (int y = 0; y < this.creatureLocations[x].length; y++) {
                final AbstractMazeObject obj = arena.getCell(x, y, 0,
                        MazeConstants.LAYER_OBJECT);
                if (obj instanceof BattleCharacter) {
                    final BattleCharacter bc = (BattleCharacter) obj;
                    this.creatureLocations[x][y] = bc.getTeamID();
                } else {
                    this.creatureLocations[x][y] = AIContext.NOTHING_THERE;
                }
            }
        }
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
                try {
                    if (this.creatureLocations[u][v] != -1
                            && this.creatureLocations[u][v] != this.myTeam) {
                        return new Point(u - x, v - y);
                    }
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
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
                try {
                    if (this.creatureLocations[u][v] != -1
                            && this.creatureLocations[u][v] != this.myTeam) {
                        return new Point(u + x, v + y);
                    }
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
            }
        }
        return null;
    }
}
