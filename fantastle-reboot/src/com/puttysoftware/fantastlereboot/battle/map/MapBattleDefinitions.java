/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.puttysoftware.fantastlereboot.ai.map.MapAIContext;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.randomrange.RandomRange;

public class MapBattleDefinitions {
  // Fields
  private List<BattleCharacter> battlers;
  private final List<MapAIContext> aiContexts;
  private Maze battleMaze;
  private int battlerCount;
  private int activeID;
  private static final int MAX_BATTLERS = 100;

  // Constructors
  public MapBattleDefinitions() {
    this.battlers = new ArrayList<>();
    this.aiContexts = new ArrayList<>();
    this.battlerCount = 0;
  }

  // Methods
  public Iterator<BattleCharacter> battlerIterator() {
    return this.battlers.iterator();
  }

  public void setLocations() {
    final RandomRange randX = new RandomRange(0, this.battleMaze.getRows() - 1);
    final RandomRange randY = new RandomRange(0,
        this.battleMaze.getColumns() - 1);
    int rx, ry;
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getCreature().isAlive() && !battler.isLocationSet()) {
          rx = randX.generate();
          ry = randY.generate();
          FantastleObjectModel obj = this.battleMaze.getCell(rx, ry, 0,
              Layers.OBJECT);
          while (obj.isSolid()) {
            rx = randX.generate();
            ry = randY.generate();
            obj = this.battleMaze.getCell(rx, ry, 0, Layers.OBJECT);
          }
          battler.setX(rx);
          battler.setY(ry);
        }
      }
    }
  }

  public void updateBattlerAIContexts() {
    for (MapAIContext maic : this.aiContexts) {
      if (maic != null) {
        if (maic.getCharacter().getCreature().isAlive()) {
          maic.updateContext(this.battleMaze);
        }
      }
    }
  }

  public void runNewRoundHooks() {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getCreature().isAlive()) {
          battler.getCreature().getMapAI().newRoundHook();
        }
      }
    }
  }

  public boolean areTeamEnemiesAlive(final int teamID) {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.isActive() && battler.getCreature().isAlive()
            && battler.getTeamID() != teamID) {
          return true;
        }
      }
    }
    return false;
  }

  public int getTeamGold(final int teamID) {
    int teamGold = 0;
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getTeamID() == teamID) {
          teamGold += battler.getCreature().getGold();
        }
      }
    }
    return teamGold;
  }

  public int getTeamEnemyGold(final int teamID) {
    int teamEnemyGold = 0;
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getTeamID() != teamID) {
          teamEnemyGold += battler.getCreature().getGold();
        }
      }
    }
    return teamEnemyGold;
  }

  public boolean isTeamAlive(final int teamID) {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.isActive() && battler.getCreature().isAlive()
            && battler.getTeamID() == teamID) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean areTeamEnemiesGone(final int teamID) {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (!battler.isActive() && battler.getTeamID() != teamID) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isTeamGone(final int teamID) {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (!battler.isActive() && battler.getTeamID() == teamID) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean areTeamEnemiesDeadOrGone(final int teamID) {
    int deadCount = 0;
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getTeamID() != teamID) {
          final boolean res = battler.getCreature().isAlive()
              && battler.isActive();
          if (res) {
            return false;
          }
          if (!battler.getCreature().isAlive()) {
            deadCount++;
          }
        }
      }
    }
    return (deadCount > 0);
  }

  public void resetBattlers() {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getCreature().isAlive()) {
          battler.activate();
          battler.resetActions();
          battler.resetLocation();
        }
      }
    }
  }

  public void roundResetBattlers() {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getCreature().isAlive()) {
          battler.resetActions();
        }
      }
    }
  }

  public boolean addBattler(final BattleCharacter battler) {
    if (this.battlerCount < MapBattleDefinitions.MAX_BATTLERS) {
      battler.getCreature().loadCreature();
      this.battlers.add(battler);
      this.battlerCount++;
      if (battler.getCreature().hasMapAI()) {
        this.aiContexts.add(new MapAIContext(battler, this.battleMaze));
      } else {
        this.aiContexts.add(null);
      }
      return true;
    } else {
      return false;
    }
  }

  public int getBattlerCount() {
    return this.battlerCount;
  }

  public BattleCharacter getActiveCharacter() {
    return this.battlers.get(this.activeID);
  }

  public MapAIContext getActiveAIContext() {
    return this.aiContexts.get(this.activeID);
  }

  public MapAIContext getBattlerAI(final BattleCharacter bc) {
    for (MapAIContext maic : this.aiContexts) {
      if (maic.getCharacter().equals(bc)) {
        return maic;
      }
    }
    return null;
  }

  public void setActiveCharacterIndex(final int index) {
    this.activeID = index;
  }

  public Maze getBattleMaze() {
    return this.battleMaze;
  }

  public void setBattleMaze(final Maze bMaze) {
    this.battleMaze = bMaze;
  }

  public BattleCharacter getBattler(final String name) {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getName().equals(name)) {
          return battler;
        }
      }
    }
    return null;
  }

  public BattleCharacter getFirstBattlerOnTeam(final int teamID) {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getTeamID() == teamID) {
          return battler;
        }
      }
    }
    return null;
  }

  public BattleCharacter getFirstBattlerNotOnTeam(final int teamID) {
    for (BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getTeamID() != teamID) {
          return battler;
        }
      }
    }
    return null;
  }
}
