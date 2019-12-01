/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import java.util.ArrayList;
import java.util.List;

import com.puttysoftware.fantastlereboot.ai.map.MapAIContext;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.fantastlereboot.world.World;
import com.puttysoftware.randomrange.RandomRange;

public class MapBattleDefinitions {
  // Fields
  private final List<BattleCharacter> battlers;
  private World battleWorld;
  private final Battle battle;
  private int battlerCount;
  private BattleCharacter active;
  static final int MAX_BATTLERS = 100;

  // Constructors
  public MapBattleDefinitions(final Battle b) {
    this.battle = b;
    this.battlerCount = 0;
    this.battlers = new ArrayList<>();
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
      this.partyGone = false;
      this.enemiesAlive = false;
      this.enemiesGone = false;
      int totalParty = 0;
      int totalEnemies = 0;
      int partyHasGone = 0;
      int enemyHasGone = 0;
      int partyDeadOrGone = 0;
      int enemiesDeadOrGone = 0;
      MapBattleDefinitions mbd = MapBattleDefinitions.this;
      for (final BattleCharacter battler : mbd.battlers) {
        if (battler != null) {
          int teamID = battler.getTeamID();
          boolean alive = battler.getCreature().isAlive();
          boolean isActive = battler.isActive();
          if (teamID == Creature.TEAM_PARTY) {
            if (alive && isActive) {
              this.partyAlive = true;
            }
            if (alive && !isActive) {
              partyHasGone++;
            }
            if (!alive || !isActive) {
              partyDeadOrGone++;
            }
            totalParty++;
          } else {
            if (alive && isActive) {
              this.enemiesAlive = true;
            }
            if (alive && !isActive) {
              enemyHasGone++;
            }
            if (!alive || !isActive) {
              enemiesDeadOrGone++;
            }
            totalEnemies++;
          }
        }
      }
      this.partyGone = (partyHasGone == totalParty);
      this.enemiesGone = (enemyHasGone == totalEnemies);
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
  public BattleState getBattleState() {
    return new BattleState();
  }

  public void setLocations() {
    final RandomRange randX = new RandomRange(0,
        this.battleWorld.getRows() - 1);
    final RandomRange randY = new RandomRange(0,
        this.battleWorld.getColumns() - 1);
    int rx, ry;
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getCreature().isAlive() && !battler.isLocationSet()) {
          rx = randX.generate();
          ry = randY.generate();
          FantastleObjectModel obj = this.battleWorld.getCell(rx, ry, 0,
              Layers.OBJECT);
          while (obj.isSolid()) {
            rx = randX.generate();
            ry = randY.generate();
            obj = this.battleWorld.getCell(rx, ry, 0, Layers.OBJECT);
          }
          battler.setX(rx);
          battler.setY(ry);
          this.battleWorld.setCell(battler, rx, ry, 0, Layers.OBJECT);
        }
      }
    }
  }

  String[] buildTargetNameList() {
    final String[] tempNames = new String[MapBattleDefinitions.MAX_BATTLERS];
    int nnc = 0;
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null && battler.isActive()
          && battler.getCreature().isAlive()) {
        tempNames[nnc] = battler.getName();
        nnc++;
      }
    }
    final String[] names = new String[nnc];
    nnc = 0;
    for (final String tempName : tempNames) {
      if (tempName != null) {
        names[nnc] = tempName;
        nnc++;
      }
    }
    return names;
  }

  void maintainEffects() {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null && battler.isActive()
          && battler.getCreature().isAlive()) {
        final Creature creature = battler.getCreature();
        // Use Effects
        creature.useEffects();
        // Display all effect messages
        final String effectMessages = battler.getCreature()
            .getAllCurrentEffectMessages();
        final String[] individualEffectMessages = effectMessages.split("\n");
        for (final String message : individualEffectMessages) {
          if (!message.equals(Effect.getNullMessage())) {
            this.battle.setStatusMessage(message);
            try {
              Thread.sleep(Prefs.getBattleSpeed());
            } catch (final InterruptedException ie) {
              // Ignore
            }
          }
        }
        // Handle low health for party members
        if (creature.isAlive() && creature.getTeamID() == Creature.TEAM_PARTY
            && creature.getCurrentHP() <= creature.getMaximumHP() * 3 / 10) {
          SoundPlayer.playSound(SoundIndex.LOW_HEALTH, SoundGroup.BATTLE);
        }
        // Cull Inactive Effects
        creature.cullInactiveEffects();
        // Handle death caused by effects
        this.battle.handleDeath(battler, null);
      }
    }
  }

  Creature getCreatureWithName(final String response) {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null && battler.isActive()
          && battler.getCreature().isAlive()) {
        if (battler.getName().equals(response)) {
          return battler.getCreature();
        }
      }
    }
    return null;
  }

  boolean setNextActive() {
    int highestSpeed = Integer.MIN_VALUE;
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null && battler.isActive()
          && battler.getCreature().isAlive() && !battler.isMarked()) {
        Creature creature = battler.getCreature();
        int creatureSpeed = (int) creature
            .getEffectedStat(StatConstants.STAT_AGILITY);
        if (creatureSpeed > highestSpeed) {
          highestSpeed = creatureSpeed;
          this.active = battler;
        }
      }
    }
    if (highestSpeed != Integer.MIN_VALUE) {
      this.active.mark();
    } else {
      this.active = null;
    }
    return highestSpeed != Integer.MIN_VALUE;
  }

  public void updateBattlerAIContexts() {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null && battler.isActive()) {
        final MapAIContext maic = battler.getAIContext();
        if (maic != null) {
          if (maic.getCharacter().getCreature().isAlive()) {
            maic.updateContext(this.battleWorld);
          }
        }
      }
    }
  }

  public void runNewRoundHooks() {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        Creature creature = battler.getCreature();
        if (battler.isActive() && creature.isAlive() && creature.hasMapAI()) {
          battler.getCreature().getMapAI().newRoundHook();
        }
      }
    }
  }

  public void resetBattlers() {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        battler.activate();
        battler.resetActions();
        battler.resetLocation();
        battler.unmark();
      }
    }
  }

  public void roundResetBattlers() {
    for (final BattleCharacter battler : this.battlers) {
      if (battler != null) {
        if (battler.getCreature().isAlive() && battler.isActive()) {
          battler.resetActions();
          battler.unmark();
        }
      }
    }
  }

  public boolean addBattler(final BattleCharacter battler) {
    if (this.battlerCount < MapBattleDefinitions.MAX_BATTLERS) {
      battler.getCreature().loadCreature();
      this.battlers.add(battler);
      this.battlerCount++;
      battler.setAIContext(new MapAIContext(battler, this.battleWorld));
      return true;
    } else {
      return false;
    }
  }

  public int getBattlerCount() {
    return this.battlerCount;
  }

  public BattleCharacter getActiveCharacter() {
    return this.active;
  }

  public MapAIContext getActiveAIContext() {
    return this.active.getAIContext();
  }

  public MapAITask getActiveAITask() {
    return new MapAITask(this.battle);
  }

  public World getBattleWorld() {
    return this.battleWorld;
  }

  public void setBattleWorld(final World bWorld) {
    this.battleWorld = bWorld;
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
