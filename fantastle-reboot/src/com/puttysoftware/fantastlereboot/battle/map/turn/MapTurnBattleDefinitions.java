/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map.turn;

import com.puttysoftware.fantastlereboot.ai.map.MapAIContext;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.objects.BattleCharacter;

public class MapTurnBattleDefinitions {
    // Fields
    private BattleCharacter activeCharacter;
    private final BattleCharacter[] battlers;
    private final MapAIContext[] aiContexts;
    private Maze battleMaze;
    private int battlerCount;
    private static final int MAX_BATTLERS = 100;

    // Constructors
    public MapTurnBattleDefinitions() {
        this.battlers = new BattleCharacter[MapTurnBattleDefinitions.MAX_BATTLERS];
        this.aiContexts = new MapAIContext[MapTurnBattleDefinitions.MAX_BATTLERS];
        this.battlerCount = 0;
    }

    // Methods
    public BattleCharacter[] getBattlers() {
        return this.battlers;
    }

    public void resetBattlers() {
        for (int x = 0; x < this.battlers.length; x++) {
            if (this.battlers[x] != null) {
                if (this.battlers[x].getTemplate().isAlive()) {
                    this.battlers[x].activate();
                    this.battlers[x].resetAP();
                    this.battlers[x].resetAttacks();
                    this.battlers[x].resetSpells();
                    this.battlers[x].resetLocation();
                }
            }
        }
    }

    public void roundResetBattlers() {
        for (int x = 0; x < this.battlers.length; x++) {
            if (this.battlers[x] != null) {
                if (this.battlers[x].getTemplate().isAlive()) {
                    this.battlers[x].resetAP();
                    this.battlers[x].resetAttacks();
                    this.battlers[x].resetSpells();
                }
            }
        }
    }

    public boolean addBattler(final BattleCharacter battler) {
        if (this.battlerCount < MapTurnBattleDefinitions.MAX_BATTLERS) {
            this.battlers[this.battlerCount] = battler;
            this.battlerCount++;
            return true;
        } else {
            return false;
        }
    }

    public MapAIContext[] getBattlerAIContexts() {
        return this.aiContexts;
    }

    public BattleCharacter getActiveCharacter() {
        return this.activeCharacter;
    }

    public void setActiveCharacter(final BattleCharacter bc) {
        this.activeCharacter = bc;
    }

    public Maze getBattleMaze() {
        return this.battleMaze;
    }

    public void setBattleMaze(final Maze bMaze) {
        this.battleMaze = bMaze;
    }

    public int findBattler(final String name) {
        return this.findBattler(name, 0, this.battlers.length);
    }

    private int findBattler(final String name, final int start,
            final int limit) {
        for (int x = start; x < limit; x++) {
            if (this.battlers[x] != null) {
                if (this.battlers[x].getName().equals(name)) {
                    return x;
                }
            }
        }
        return -1;
    }

    public int findFirstBattlerOnTeam(final int teamID) {
        return this.findFirstBattlerOnTeam(teamID, 0, this.battlers.length);
    }

    private int findFirstBattlerOnTeam(final int teamID, final int start,
            final int limit) {
        for (int x = start; x < limit; x++) {
            if (this.battlers[x] != null) {
                if (this.battlers[x].getTeamID() == teamID) {
                    return x;
                }
            }
        }
        return -1;
    }
}
