/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.puttysoftware.fantastlereboot.ai.map.MapAIContext;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.randomrange.RandomRange;

public class MapBattleDefinitions {
  // Fields
  private final List<BattleCharacter> battlers;
  private final List<MapAIContext> aiContexts;
  private final List<MapAITask> aiTasks;
  private Maze battleMaze;
  private final Battle battle;
  private int battlerCount;
  private int activeID;
  private static final int MAX_BATTLERS = 100;

  // Constructors
  public MapBattleDefinitions(final Battle b) {
    this.battle = b;
    this.battlerCount = 0;
    this.battlers = new ArrayList<>();
    this.aiContexts = new ArrayList<>();
    this.aiTasks = new ArrayList<>();
  }

  // Nested class
  public class BattleState {
    private boolean partyAlive;
    private boolean partyGone;
    private boolean partyDead;
    private boolean enemiesAlive;
    private boolean enemiesGone;
    private boolean enemiesDead;

    public BattleState() {
      super();
      this.partyAlive = false;
      this.partyGone = true;
      this.enemiesAlive = false;
      this.enemiesGone = true;
      int totalParty = 0;
      int totalEnemies = 0;
      int partyDeadOrGone = 0;
      int enemiesDeadOrGone = 0;
      MapBattleDefinitions mbd = MapBattleDefinitions.this;
      Iterator<BattleCharacter> iter = mbd.battlerIterator();
      while (iter.hasNext()) {
        BattleCharacter battler = iter.next();
        if (battler != null) {
          int teamID = battler.getTeamID();
          boolean alive = battler.getCreature().isAlive();
          boolean active = battler.isActive();
          if (teamID == Creature.TEAM_PARTY) {
            if (alive && active) {
              this.partyAlive = true;
            }
            this.partyGone &= !active;
            if (!alive || !active) {
              partyDeadOrGone++;
            }
            totalParty++;
          } else {
            if (alive && active) {
              this.enemiesAlive = true;
            }
            this.enemiesGone &= !active;
            if (!alive || !active) {
              enemiesDeadOrGone++;
            }
            totalEnemies++;
          }
        }
      }
      this.partyDead = (partyDeadOrGone == totalParty);
      this.enemiesDead = (enemiesDeadOrGone == totalEnemies);
    }

    public boolean isPartyAlive() {
      return this.partyAlive;
    }

    public boolean isPartyGone() {
      return this.partyGone;
    }

    public boolean isPartyDead() {
      return this.partyDead;
    }

    public boolean areEnemiesAlive() {
      return this.enemiesAlive;
    }

    public boolean areEnemiesGone() {
      return this.enemiesGone;
    }

    public boolean areEnemiesDead() {
      return this.enemiesDead;
    }
  }

  // Methods
  public Iterator<BattleCharacter> battlerIterator() {
    return this.battlers.iterator();
  }

  public BattleState getBattleState() {
    return new BattleState();
  }

  public void setLocations() {
    final RandomRange randX = new RandomRange(0, this.battleMaze.getRows() - 1);
    final RandomRange randY = new RandomRange(0,
        this.battleMaze.getColumns() - 1);
    int rx, ry;
    for (final BattleCharacter battler : this.battlers) {
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
    for (final MapAIContext maic : this.aiContexts) {
      if (maic != null) {
        if (maic.getCharacter().getCreature().isAlive()) {
          maic.updateContext(this.battleMaze);
        }
      }
    }
  }

  public void runNewRoundHooks() {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getCreature().isAlive()) {
          battler.getCreature().getMapAI().newRoundHook();
        }
      }
    }
  }

  public int getTeamGold(final int teamID) {
    int teamGold = 0;
    for (final BattleCharacter battler : this.battlers) {
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
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getTeamID() != teamID) {
          teamEnemyGold += battler.getCreature().getGold();
        }
      }
    }
    return teamEnemyGold;
  }

  public void resetBattlers() {
    for (final BattleCharacter battler : this.battlers) {
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
    for (final BattleCharacter battler : this.battlers) {
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
        MapAITask task = new MapAITask(this.battle);
        this.aiTasks.add(task);
        task.start();
      } else {
        this.aiContexts.add(null);
        this.aiTasks.add(null);
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

  public MapAITask getActiveAITask() {
    return this.aiTasks.get(this.activeID);
  }

  public MapAIContext getBattlerAI(final BattleCharacter bc) {
    for (final MapAIContext maic : this.aiContexts) {
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
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getName().equals(name)) {
          return battler;
        }
      }
    }
    return null;
  }

  public BattleCharacter getFirstBattlerOnTeam(final int teamID) {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getTeamID() == teamID) {
          return battler;
        }
      }
    }
    return null;
  }

  public BattleCharacter getFirstBattlerNotOnTeam(final int teamID) {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getTeamID() != teamID) {
          return battler;
        }
      }
    }
    return null;
  }
}
