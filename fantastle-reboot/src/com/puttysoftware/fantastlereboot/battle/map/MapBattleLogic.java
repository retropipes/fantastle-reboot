/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Modes;
import com.puttysoftware.fantastlereboot.ai.AIContext;
import com.puttysoftware.fantastlereboot.ai.AIRoutine;
import com.puttysoftware.fantastlereboot.ai.map.AutoMapAI;
import com.puttysoftware.fantastlereboot.assets.MusicGroup;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.battle.BattleResults;
import com.puttysoftware.fantastlereboot.battle.BossRewards;
import com.puttysoftware.fantastlereboot.battle.damageengines.AbstractDamageEngine;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.monsters.MonsterFactory;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.items.combat.CombatItem;
import com.puttysoftware.fantastlereboot.items.combat.CombatItemChucker;
import com.puttysoftware.fantastlereboot.loaders.MusicPlayer;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.fantastlereboot.spells.Spell;
import com.puttysoftware.fantastlereboot.spells.SpellCaster;
import com.puttysoftware.randomrange.RandomRange;

public class MapBattleLogic extends Battle {
  // Fields
  private MapBattleDefinitions mbd;
  private AbstractDamageEngine pde;
  private AbstractDamageEngine ede;
  private final ExecutorService dispatch;
  private final AutoMapAI auto;
  private int damage;
  private int activeIndex;
  private long battleExp;
  private boolean newRound;
  private int[] speedArray;
  private int lastSpeed;
  private boolean[] speedMarkArray;
  private boolean resultDoneAlready;
  private boolean lastAIActionResult;
  private boolean alliesTookDamage;
  private boolean enemiesTookDamage;
  private int bx;
  private int by;
  private MapBattleGUI battleGUI;
  private List<BattleCharacter> friends;
  private List<BattleCharacter> enemies;
  private static final int ITEM_ACTION_POINTS = 6;
  private static final int STEAL_ACTION_POINTS = 3;
  private static final int DRAIN_ACTION_POINTS = 3;
  private static final int SPELL_ACTION_POINTS = 1;

  // Constructors
  public MapBattleLogic() {
    this.battleGUI = new MapBattleGUI();
    this.auto = new AutoMapAI();
    this.dispatch = Executors.newCachedThreadPool();
  }

  // Methods
  @Override
  public void doBattle(final int x, final int y) {
    this.bx = x;
    this.by = y;
    Runnable task = () -> {
      // Initialize Battle
      Game.hideOutput();
      Modes.setInBattle();
      this.alliesTookDamage = false;
      this.enemiesTookDamage = false;
      final Maze bMaze = Maze.getTemporaryBattleCopy();
      final MapBattle b = new MapBattle();
      this.mbd = new MapBattleDefinitions(this);
      this.mbd.setBattleMaze(bMaze);
      this.pde = AbstractDamageEngine.getPlayerInstance();
      this.ede = AbstractDamageEngine.getEnemyInstance();
      this.resultDoneAlready = false;
      // Generate Friends
      this.friends = PartyManager.getParty().getBattleCharacters();
      // Generate Enemies
      this.enemies = b.getBattlers();
      // Merge and Create AI Contexts
      for (final BattleCharacter friend : this.friends) {
        this.mbd.addBattler(friend);
      }
      for (final BattleCharacter enemy : this.enemies) {
        this.mbd.addBattler(enemy);
      }
      // Reset Inactive Indicators and Action Counters
      this.mbd.resetBattlers();
      // Generate Speed Array
      this.generateSpeedArray();
      // Set Character Locations
      this.setCharacterLocations();
      // Set First Active
      this.newRound = this.setNextActive(true);
      // Clear status message
      this.clearStatusMessage();
      // Start Battle
      this.battleGUI.getViewManager()
          .setViewingWindowCenterX(this.mbd.getActiveCharacter().getY());
      this.battleGUI.getViewManager()
          .setViewingWindowCenterY(this.mbd.getActiveCharacter().getX());
      SoundPlayer.playSound(SoundIndex.DRAW_SWORD, SoundGroup.BATTLE);
      MusicPlayer.playMusic(MusicIndex.NORMAL_MAP_BATTLE, MusicGroup.BATTLE);
      this.showBattle();
      this.updateStatsAndEffects();
      this.redrawBattle();
    };
    this.dispatch.submit(task);
  }

  @Override
  public void doBattleByProxy(final int x, final int y) {
    this.bx = x;
    this.by = y;
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final Creature monster = MonsterFactory.generateMonster();
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    playerCharacter.offsetExperience(monster.getExperience());
    playerCharacter.offsetGold(monster.getGold());
    // Level Up Check
    if (playerCharacter.checkLevelUp()) {
      playerCharacter.levelUp();
      Game.keepNextMessage();
      bag.showMessage("You reached level " + playerCharacter.getLevel() + ".");
    }
    final Maze m = MazeManager.getMaze();
    m.postBattle(this.bx, this.by);
  }

  @Override
  public void battleDone() {
    Runnable task = () -> {
      if (Modes.inBattle()) {
        // Leave Battle
        this.hideBattle();
        // Post-battle stuff
        final Maze m = MazeManager.getMaze();
        m.postBattle(this.bx, this.by);
        // Return to whence we came
        Modes.restore();
        Game.redrawMaze();
      }
    };
    this.dispatch.submit(task);
  }

  private void clearStatusMessage() {
    this.battleGUI.clearStatusMessage();
  }

  @Override
  public void setStatusMessage(final String msg) {
    this.battleGUI.setStatusMessage(msg);
  }

  @Override
  public BattleResults getResult() {
    MapBattleDefinitions.BattleState state = this.mbd.getBattleState();
    if (!state.areEnemiesAlive() && !state.isPartyAlive()) {
      return BattleResults.DRAW;
    } else if (state.areEnemiesDead() && !state.isPartyDead()) {
      if (!this.alliesTookDamage) {
        return BattleResults.PERFECT;
      } else {
        return BattleResults.WON;
      }
    } else if (!state.areEnemiesDead() && state.isPartyDead()) {
      if (!this.enemiesTookDamage) {
        return BattleResults.ANNIHILATED;
      } else {
        return BattleResults.LOST;
      }
    } else if (state.areEnemiesAlive() && state.areEnemiesGone()) {
      return BattleResults.ENEMY_FLED;
    } else if (state.isPartyAlive() && state.isPartyGone()) {
      return BattleResults.FLED;
    }
    return BattleResults.IN_PROGRESS;
  }

  @Override
  public void executeNextAIAction() {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    Runnable task = () -> {
      if (this.mbd != null && this.mbd.getActiveCharacter() != null
          && this.mbd.getActiveCharacter().getCreature() != null
          && this.mbd.getActiveCharacter().getCreature().getMapAI() != null) {
        final BattleCharacter active = this.mbd.getActiveCharacter();
        if (active.getCreature().isAlive()) {
          final int action = active.getCreature().getMapAI().getNextAction(
              this.mbd.getActiveCharacter().getCreature(),
              this.mbd.getActiveAIContext());
          switch (action) {
          case AIRoutine.ACTION_MOVE:
            final int x = active.getCreature().getMapAI().getMoveX();
            final int y = active.getCreature().getMapAI().getMoveY();
            final int activeTID = this.mbd.getActiveCharacter().getTeamID();
            final AbstractDamageEngine activeDE = activeTID == Creature.TEAM_PARTY
                ? this.ede
                : this.pde;
            this.lastAIActionResult = this.updatePositionInternal(x, y, true,
                this.mbd.getActiveCharacter(), activeDE);
            active.getCreature().getMapAI()
                .setLastResult(this.lastAIActionResult);
            break;
          case AIRoutine.ACTION_CAST_SPELL:
            this.lastAIActionResult = this.castSpell();
            active.getCreature().getMapAI()
                .setLastResult(this.lastAIActionResult);
            break;
          case AIRoutine.ACTION_DRAIN:
            this.lastAIActionResult = this.drain();
            active.getCreature().getMapAI()
                .setLastResult(this.lastAIActionResult);
            break;
          case AIRoutine.ACTION_STEAL:
            this.lastAIActionResult = this.steal();
            active.getCreature().getMapAI()
                .setLastResult(this.lastAIActionResult);
            break;
          default:
            this.lastAIActionResult = true;
            final MapAITask aiTask = this.mbd.getActiveAITask();
            if (aiTask != null) {
              aiTask.aiWait();
            }
            this.endTurn();
            break;
          }
        }
      }
    };
    this.dispatch.submit(task);
  }

  @Override
  public boolean getLastAIActionResult() {
    return this.lastAIActionResult;
  }

  private void executeAutoAI(final BattleCharacter acting) {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    final int action = this.auto.getNextAction(acting.getCreature(),
        this.mbd.getBattlerAI(acting));
    switch (action) {
    case AIRoutine.ACTION_MOVE:
      final int x = this.auto.getMoveX();
      final int y = this.auto.getMoveY();
      final int activeTID = acting.getTeamID();
      final AbstractDamageEngine activeDE = activeTID == Creature.TEAM_PARTY
          ? this.ede
          : this.pde;
      this.updatePositionInternal(x, y, false, acting, activeDE);
      break;
    default:
      break;
    }
  }

  private void displayRoundResults(final Creature theEnemy,
      final Creature active, final AbstractDamageEngine activeDE) {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    // Display round results
    final boolean isParty = active.getTeamID() == Creature.TEAM_PARTY;
    final String activeName = active.getName();
    final String enemyName = theEnemy.getName();
    String damageString = Integer.toString(this.damage);
    String displayDamageString = " ";
    if (this.damage == 0) {
      if (activeDE.weaponMissed()) {
        displayDamageString = activeName + " tries to hit " + enemyName
            + ", but MISSES!";
        SoundPlayer.playSound(SoundIndex.MISSED, SoundGroup.BATTLE);
      } else if (activeDE.enemyDodged()) {
        displayDamageString = activeName + " tries to hit " + enemyName
            + ", but " + enemyName + " AVOIDS the attack!";
        SoundPlayer.playSound(SoundIndex.MISSED, SoundGroup.BATTLE);
      } else {
        displayDamageString = activeName + " tries to hit " + enemyName
            + ", but the attack is BLOCKED!";
        SoundPlayer.playSound(SoundIndex.MISSED, SoundGroup.BATTLE);
      }
    } else if (this.damage < 0) {
      damageString = Integer.toString(-this.damage);
      String displayDamagePrefix = "";
      if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
        displayDamagePrefix = "PIERCING CRITICAL HIT! ";
        if (isParty) {
          SoundPlayer.playSound(SoundIndex.PARTY_COUNTER, SoundGroup.BATTLE);
        } else {
          SoundPlayer.playSound(SoundIndex.MONSTER_COUNTER, SoundGroup.BATTLE);
        }
        SoundPlayer.playSound(SoundIndex.CRITICAL, SoundGroup.BATTLE);
      } else if (activeDE.weaponCrit()) {
        displayDamagePrefix = "CRITICAL HIT! ";
        SoundPlayer.playSound(SoundIndex.CRITICAL, SoundGroup.BATTLE);
      } else if (activeDE.weaponPierce()) {
        displayDamagePrefix = "PIERCING HIT! ";
        if (isParty) {
          SoundPlayer.playSound(SoundIndex.PARTY_COUNTER, SoundGroup.BATTLE);
        } else {
          SoundPlayer.playSound(SoundIndex.MONSTER_COUNTER, SoundGroup.BATTLE);
        }
      }
      displayDamageString = displayDamagePrefix + activeName + " tries to hit "
          + enemyName + ", but " + enemyName + " RIPOSTES for " + damageString
          + " damage!";
      if (isParty) {
        SoundPlayer.playSound(SoundIndex.PARTY_COUNTER, SoundGroup.BATTLE);
      } else {
        SoundPlayer.playSound(SoundIndex.MONSTER_COUNTER, SoundGroup.BATTLE);
      }
    } else {
      String displayDamagePrefix = "";
      if (activeDE.weaponFumble()) {
        SoundPlayer.playSound(SoundIndex.FUMBLE, SoundGroup.BATTLE);
        displayDamageString = "FUMBLE! " + activeName
            + " drops their weapon on themselves, doing " + damageString
            + " damage!";
      } else {
        if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
          displayDamagePrefix = "PIERCING CRITICAL HIT! ";
          if (isParty) {
            SoundPlayer.playSound(SoundIndex.PARTY_COUNTER, SoundGroup.BATTLE);
          } else {
            SoundPlayer.playSound(SoundIndex.MONSTER_COUNTER,
                SoundGroup.BATTLE);
          }
          SoundPlayer.playSound(SoundIndex.CRITICAL, SoundGroup.BATTLE);
        } else if (activeDE.weaponCrit()) {
          displayDamagePrefix = "CRITICAL HIT! ";
          SoundPlayer.playSound(SoundIndex.CRITICAL, SoundGroup.BATTLE);
        } else if (activeDE.weaponPierce()) {
          displayDamagePrefix = "PIERCING HIT! ";
          if (isParty) {
            SoundPlayer.playSound(SoundIndex.PARTY_COUNTER, SoundGroup.BATTLE);
          } else {
            SoundPlayer.playSound(SoundIndex.MONSTER_COUNTER,
                SoundGroup.BATTLE);
          }
        }
        displayDamageString = displayDamagePrefix + activeName + " hits "
            + enemyName + " for " + damageString + " damage!";
        if (isParty) {
          SoundPlayer.playSound(SoundIndex.PARTY_HIT, SoundGroup.BATTLE);
        } else {
          SoundPlayer.playSound(SoundIndex.MONSTER_HIT, SoundGroup.BATTLE);
        }
      }
    }
    this.setStatusMessage(displayDamageString);
  }

  private void computeDamage(final Creature theEnemy, final Creature acting,
      final AbstractDamageEngine activeDE) {
    // Compute Damage
    this.damage = 0;
    final int actual = activeDE.computeDamage(theEnemy, acting);
    // Hit or Missed
    this.damage = actual;
    if (activeDE.weaponFumble()) {
      acting.doDamage(this.damage);
      if (acting.getTeamID() == Creature.TEAM_PARTY) {
        this.alliesTookDamage = true;
      } else {
        this.enemiesTookDamage = true;
      }
    } else {
      if (this.damage > 0) {
        theEnemy.doDamage(this.damage);
        if (theEnemy.getTeamID() == Creature.TEAM_PARTY) {
          this.alliesTookDamage = true;
        } else {
          this.enemiesTookDamage = true;
        }
      }
    }
    this.displayRoundResults(theEnemy, acting, activeDE);
  }

  private void generateSpeedArray() {
    this.speedArray = new int[this.mbd.getBattlerCount()];
    this.speedMarkArray = new boolean[this.speedArray.length];
    this.resetSpeedArray();
  }

  private void resetSpeedArray() {
    final Iterator<BattleCharacter> iter = this.mbd.battlerIterator();
    for (int x = 0; x < this.speedArray.length; x++) {
      final BattleCharacter battler = iter.next();
      if (battler != null && battler.getCreature().isAlive()
          && battler.isActive()) {
        this.speedArray[x] = (int) battler.getCreature()
            .getEffectedStat(StatConstants.STAT_AGILITY);
      } else {
        this.speedArray[x] = Integer.MIN_VALUE;
      }
    }
    for (int x = 0; x < this.speedMarkArray.length; x++) {
      if (this.speedArray[x] != Integer.MIN_VALUE) {
        this.speedMarkArray[x] = false;
      } else {
        this.speedMarkArray[x] = true;
      }
    }
  }

  private void setCharacterLocations() {
    this.mbd.setLocations();
  }

  private boolean setNextActive(final boolean isNewRound) {
    if (!Modes.inBattle()) {
      // Abort
      return false;
    }
    int res = 0;
    if (isNewRound) {
      res = this.findNextSmallestSpeed(Integer.MAX_VALUE);
    } else {
      res = this.findNextSmallestSpeed(this.lastSpeed);
    }
    if (res != -1) {
      this.lastSpeed = this.speedArray[res];
      this.activeIndex = res;
      this.mbd.setActiveCharacterIndex(this.activeIndex);
      // AI Check
      if (this.mbd.getActiveCharacter().getCreature().hasMapAI()) {
        // Run AI
        this.waitForAI();
        final MapAITask task = this.mbd.getActiveAITask();
        if (task != null) {
          task.aiRun();
        }
      } else {
        // No AI
        this.stopWaitingForAI();
        SoundPlayer.playSound(SoundIndex.PLAYER_UP, SoundGroup.BATTLE);
      }
      return false;
    } else {
      // Reset Speed Array
      this.resetSpeedArray();
      // Reset Action Counters
      this.mbd.roundResetBattlers();
      // Maintain effects
      this.maintainEffects(true);
      this.updateStatsAndEffects();
      // Perform new round actions
      this.performNewRoundActions();
      // Play new round sound
      SoundPlayer.playSound(SoundIndex.NEXT_ROUND, SoundGroup.BATTLE);
      // Nobody to act next, set new round flag
      return true;
    }
  }

  private int findNextSmallestSpeed(final int max) {
    int res = -1;
    int found = 0;
    for (int x = 0; x < this.speedArray.length; x++) {
      if (!this.speedMarkArray[x]) {
        if (this.speedArray[x] <= max && this.speedArray[x] > found) {
          res = x;
          found = this.speedArray[x];
        }
      }
    }
    if (res != -1) {
      this.speedMarkArray[res] = true;
    }
    return res;
  }

  private int getGold() {
    return this.mbd.getTeamEnemyGold(Creature.TEAM_PARTY);
  }

  @Override
  public void updatePosition(final int x, final int y) {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    Runnable task = () -> {
      final int activeTID = this.mbd.getActiveCharacter().getTeamID();
      final AbstractDamageEngine activeDE = activeTID == Creature.TEAM_PARTY
          ? this.ede
          : this.pde;
      this.updatePositionInternal(x, y, true, this.mbd.getActiveCharacter(),
          activeDE);
    };
    this.dispatch.submit(task);
  }

  @Override
  public void fireArrow(final int x, final int y) {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    if (this.mbd.getActiveCharacter().getCurrentActions() > 0) {
      // Has actions left
      this.mbd.getActiveCharacter().act(1);
      this.updateStatsAndEffects();
      this.battleGUI.turnEventHandlersOff();
      final MapBattleArrowTask at = new MapBattleArrowTask(x, y,
          this.mbd.getBattleMaze(), this.mbd.getActiveCharacter());
      at.start();
    } else {
      // Deny arrow - out of actions
      if (!this.mbd.getActiveCharacter().getCreature().hasMapAI()) {
        this.setStatusMessage("Out of actions!");
      }
    }
  }

  @Override
  public void arrowDone(final BattleCharacter hit) {
    this.battleGUI.turnEventHandlersOn();
    // Handle death
    if (hit != null && !hit.getCreature().isAlive()) {
      if (hit.getTeamID() != Creature.TEAM_PARTY) {
        // Update victory spoils
        this.battleExp = hit.getCreature().getExperience();
      }
      // Remove effects from dead character
      hit.getCreature().stripAllEffects();
      // Set dead character to inactive
      hit.deactivate();
      // Remove character from battle
      this.mbd.getBattleMaze().setCell(new OpenSpace(), hit.getX(), hit.getY(),
          0, Layers.OBJECT);
    }
    // Check result
    final BattleResults currResult = this.getResult();
    if (currResult != BattleResults.IN_PROGRESS) {
      // Battle Done
      this.doResult(currResult);
    }
  }

  private boolean updatePositionInternal(final int x, final int y,
      final boolean useAP, final BattleCharacter active,
      final AbstractDamageEngine activeDE) {
    if (active == null || activeDE == null) {
      // Abort
      return false;
    }
    this.updateAllAIContexts();
    int px = active.getX();
    int py = active.getY();
    final Maze m = this.mbd.getBattleMaze();
    FantastleObjectModel next = null;
    FantastleObjectModel nextGround = null;
    FantastleObjectModel currGround = null;
    active.saveLocation();
    this.battleGUI.getViewManager().saveViewingWindow();
    if (this.mbd.getBattleMaze().cellRangeCheck(px + x, py + y, 0)) {
      next = m.getCell(px + x, py + y, 0, Layers.OBJECT);
      nextGround = m.getCell(px + x, py + y, 0, Layers.GROUND);
    }
    if (this.mbd.getBattleMaze().cellRangeCheck(px, py, 0)) {
      currGround = m.getCell(px, py, 0, Layers.GROUND);
    }
    if (next != null && nextGround != null && currGround != null) {
      if (!next.isSolid()) {
        if (useAP
            && this.getActiveActionCounter() >= AIContext.getDefaultAPCost()
            || !useAP) {
          // Move
          FantastleObjectModel obj1 = null;
          FantastleObjectModel obj2 = null;
          FantastleObjectModel obj3 = null;
          FantastleObjectModel obj4 = null;
          FantastleObjectModel obj6 = null;
          FantastleObjectModel obj7 = null;
          FantastleObjectModel obj8 = null;
          FantastleObjectModel obj9 = null;
          if (this.mbd.getBattleMaze().cellRangeCheck(px - 1, py - 1, 0)) {
            obj1 = m.getCell(px - 1, py - 1, 0, Layers.OBJECT);
          }
          if (this.mbd.getBattleMaze().cellRangeCheck(px, py - 1, 0)) {
            obj2 = m.getCell(px, py - 1, 0, Layers.OBJECT);
          }
          if (this.mbd.getBattleMaze().cellRangeCheck(px + 1, py - 1, 0)) {
            obj3 = m.getCell(px + 1, py - 1, 0, Layers.OBJECT);
          }
          if (this.mbd.getBattleMaze().cellRangeCheck(px - 1, py, 0)) {
            obj4 = m.getCell(px - 1, py, 0, Layers.OBJECT);
          }
          if (this.mbd.getBattleMaze().cellRangeCheck(px + 1, py - 1, 0)) {
            obj6 = m.getCell(px + 1, py - 1, 0, Layers.OBJECT);
          }
          if (this.mbd.getBattleMaze().cellRangeCheck(px - 1, py + 1, 0)) {
            obj7 = m.getCell(px - 1, py + 1, 0, Layers.OBJECT);
          }
          if (this.mbd.getBattleMaze().cellRangeCheck(px, py + 1, 0)) {
            obj8 = m.getCell(px, py + 1, 0, Layers.OBJECT);
          }
          if (this.mbd.getBattleMaze().cellRangeCheck(px + 1, py + 1, 0)) {
            obj9 = m.getCell(px + 1, py + 1, 0, Layers.OBJECT);
          }
          // Auto-attack check
          if (obj1 != null) {
            if (obj1 instanceof BattleCharacter) {
              if (!(x == -1 && y == 0 || x == -1 && y == -1
                  || x == 0 && y == -1)) {
                final BattleCharacter bc1 = (BattleCharacter) obj1;
                if (bc1.getTeamID() != active.getTeamID()) {
                  this.executeAutoAI(bc1);
                }
              }
            }
          }
          if (obj2 != null) {
            if (obj2 instanceof BattleCharacter) {
              if (y == 1) {
                final BattleCharacter bc2 = (BattleCharacter) obj2;
                if (bc2.getTeamID() != active.getTeamID()) {
                  this.executeAutoAI(bc2);
                }
              }
            }
          }
          if (obj3 != null) {
            if (obj3 instanceof BattleCharacter) {
              if (!(x == 0 && y == -1 || x == 1 && y == -1
                  || x == 1 && y == 0)) {
                final BattleCharacter bc3 = (BattleCharacter) obj3;
                if (bc3.getTeamID() != active.getTeamID()) {
                  this.executeAutoAI(bc3);
                }
              }
            }
          }
          if (obj4 != null) {
            if (obj4 instanceof BattleCharacter) {
              if (x == 1) {
                final BattleCharacter bc4 = (BattleCharacter) obj4;
                if (bc4.getTeamID() != active.getTeamID()) {
                  this.executeAutoAI(bc4);
                }
              }
            }
          }
          if (obj6 != null) {
            if (obj6 instanceof BattleCharacter) {
              if (x == -1) {
                final BattleCharacter bc6 = (BattleCharacter) obj6;
                if (bc6.getTeamID() != active.getTeamID()) {
                  this.executeAutoAI(bc6);
                }
              }
            }
          }
          if (obj7 != null) {
            if (obj7 instanceof BattleCharacter) {
              if (!(x == -1 && y == 0 || x == -1 && y == 1
                  || x == 0 && y == 1)) {
                final BattleCharacter bc7 = (BattleCharacter) obj7;
                if (bc7.getTeamID() != active.getTeamID()) {
                  this.executeAutoAI(bc7);
                }
              }
            }
          }
          if (obj8 != null) {
            if (obj8 instanceof BattleCharacter) {
              if (y == -1) {
                final BattleCharacter bc8 = (BattleCharacter) obj8;
                if (bc8.getTeamID() != active.getTeamID()) {
                  this.executeAutoAI(bc8);
                }
              }
            }
          }
          if (obj9 != null) {
            if (obj9 instanceof BattleCharacter) {
              if (!(x == 0 && y == 1 || x == 1 && y == 1 || x == 1 && y == 0)) {
                final BattleCharacter bc9 = (BattleCharacter) obj9;
                if (bc9.getTeamID() != active.getTeamID()) {
                  this.executeAutoAI(bc9);
                }
              }
            }
          }
          m.setCell(active.getSavedObject(), px, py, 0, Layers.OBJECT);
          active.offsetX(x);
          active.offsetY(y);
          px += x;
          py += y;
          this.battleGUI.getViewManager().offsetViewingWindowLocationX(y);
          this.battleGUI.getViewManager().offsetViewingWindowLocationY(x);
          active.setSavedObject(m.getCell(px, py, 0, Layers.OBJECT));
          m.setCell(active, px, py, 0, Layers.OBJECT);
          this.decrementActiveActionCounterBy(AIContext.getDefaultAPCost());
          SoundPlayer.playSound(SoundIndex.WALK, SoundGroup.BATTLE);
        } else {
          // Deny move - out of actions
          if (!this.mbd.getActiveCharacter().getCreature().hasMapAI()) {
            this.setStatusMessage("Out of moves!");
          }
          return false;
        }
      } else {
        if (next instanceof BattleCharacter) {
          if (useAP && this.getActiveAttackCounter() > 0 || !useAP) {
            // Attack
            final BattleCharacter bc = (BattleCharacter) next;
            if (bc.getTeamID() == active.getTeamID()) {
              // Attack Friend?
              if (!active.getCreature().hasMapAI()) {
                final int confirm = CommonDialogs
                    .showConfirmDialog("Attack Friend?", "Battle");
                if (confirm != JOptionPane.YES_OPTION) {
                  return false;
                }
              } else {
                return false;
              }
            }
            if (useAP) {
              this.decrementActiveAttackCounter();
            }
            // Do damage
            this.computeDamage(bc.getCreature(), active.getCreature(),
                activeDE);
            // Handle low health for party members
            if (bc.getCreature().isAlive()
                && bc.getTeamID() == Creature.TEAM_PARTY
                && bc.getCreature()
                    .getCurrentHP() <= bc.getCreature().getMaximumHP() * 3
                        / 10) {
              SoundPlayer.playSound(SoundIndex.LOW_HEALTH, SoundGroup.BATTLE);
            }
            // Handle enemy death
            if (!bc.getCreature().isAlive()) {
              if (bc.getTeamID() != Creature.TEAM_PARTY) {
                // Update victory spoils
                this.battleExp = bc.getCreature().getExperience();
              }
              // Remove effects from dead character
              bc.getCreature().stripAllEffects();
              // Set dead character to inactive
              bc.deactivate();
              // Remove dead character from battle
              this.mbd.getBattleMaze().setCell(new OpenSpace(), bc.getX(),
                  bc.getY(), 0, Layers.OBJECT);
            }
            // Handle self death
            if (!active.getCreature().isAlive()) {
              // Remove effects from dead character
              active.getCreature().stripAllEffects();
              // Set dead character to inactive
              active.deactivate();
              // Remove dead character from battle
              this.mbd.getBattleMaze().setCell(new OpenSpace(), active.getX(),
                  active.getY(), 0, Layers.OBJECT);
              // We're dead - end our turn
              this.endTurn();
            }
          } else {
            // Deny attack - out of actions
            if (!this.mbd.getActiveCharacter().getCreature().hasMapAI()) {
              this.setStatusMessage("Out of attacks!");
            }
            return false;
          }
        } else {
          // Move Failed
          if (!active.getCreature().hasMapAI()) {
            this.setStatusMessage("Can't go that way");
          }
          return false;
        }
      }
    } else {
      // Confirm Flee
      if (!active.getCreature().hasMapAI()) {
        SoundPlayer.playSound(SoundIndex.SPECIAL, SoundGroup.BATTLE);
        final int confirm = CommonDialogs
            .showConfirmDialog("Embrace Cowardice?", "Battle");
        if (confirm != JOptionPane.YES_OPTION) {
          this.battleGUI.getViewManager().restoreViewingWindow();
          SoundPlayer.playSound(SoundIndex.RUN, SoundGroup.BATTLE);
          active.restoreLocation();
          return false;
        }
      }
      // Fled
      this.battleGUI.getViewManager().restoreViewingWindow();
      active.restoreLocation();
      // Set fled character to inactive
      active.deactivate();
      // Remove fled character from battle
      m.setCell(new OpenSpace(), active.getX(), active.getY(), 0,
          Layers.OBJECT);
      // We fled - end our turn
      this.endTurn();
      this.updateStatsAndEffects();
      this.battleGUI.getViewManager().setViewingWindowCenterX(py);
      this.battleGUI.getViewManager().setViewingWindowCenterY(px);
      this.redrawBattle();
      return true;
    }
    this.updateStatsAndEffects();
    this.battleGUI.getViewManager().setViewingWindowCenterX(py);
    this.battleGUI.getViewManager().setViewingWindowCenterY(px);
    this.redrawBattle();
    return true;
  }

  @Override
  public Creature pickTarget() {
    final String[] pickNames = this.buildTargetNameList();
    final String response = CommonDialogs.showInputDialog("Pick A Target",
        "Target", pickNames, pickNames[0]);
    if (response != null) {
      final Iterator<BattleCharacter> iter = this.mbd.battlerIterator();
      while (iter.hasNext()) {
        BattleCharacter battler = iter.next();
        if (battler != null) {
          if (battler.getName().equals(response)) {
            return battler.getCreature();
          }
        }
      }
    }
    return null;
  }

  private String[] buildTargetNameList() {
    final String[] tempNames = new String[MapBattleDefinitions.MAX_BATTLERS];
    int nnc = 0;
    final Iterator<BattleCharacter> iter = this.mbd.battlerIterator();
    while (iter.hasNext()) {
      BattleCharacter battler = iter.next();
      if (battler != null) {
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

  @Override
  public Creature getActive() {
    return this.mbd.getActiveCharacter().getCreature();
  }

  @Override
  public Creature getEnemy(int teamID) {
    return this.mbd.getFirstBattlerNotOnTeam(teamID).getCreature();
  }

  @Override
  public void showBattle() {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    this.battleGUI.showBattle();
  }

  @Override
  public void hideBattle() {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    this.battleGUI.hideBattle();
  }

  @Override
  public boolean castSpell() {
    if (!Modes.inBattle()) {
      // Abort
      return false;
    }
    Callable<Boolean> task = () -> {
      final BattleCharacter activeBC = this.mbd.getActiveCharacter();
      Creature active = null;
      if (activeBC != null) {
        active = activeBC.getCreature();
      }
      if (activeBC == null || active == null) {
        // Abort
        return false;
      }
      // Check Action Counter
      if (activeBC.canAct(MapBattleLogic.SPELL_ACTION_POINTS)) {
        BattleCharacter anyEnemy = this.mbd
            .getFirstBattlerNotOnTeam(activeBC.getTeamID());
        if (anyEnemy == null) {
          // Failed - nobody to use on
          this.setStatusMessage(activeBC.getName()
              + " tries to cast a spell, but nobody is there to cast it on!");
          return false;
        }
        if (!active.hasMapAI()) {
          // Active character has no AI, or AI is turned off
          final boolean success = SpellCaster.selectAndCastSpell(active);
          if (success) {
            activeBC.act(MapBattleLogic.SPELL_ACTION_POINTS);
          }
          return success;
        } else {
          // Active character has AI, and AI is turned on
          final Spell spell = active.getMapAI().getSpellToCast();
          final boolean success = SpellCaster.castSpell(spell, active);
          if (success) {
            activeBC.act(MapBattleLogic.SPELL_ACTION_POINTS);
          }
          return success;
        }
      } else {
        // Deny cast - out of actions
        if (!active.hasMapAI()) {
          this.setStatusMessage("Out of actions!");
        }
        return false;
      }
    };
    try {
      return this.dispatch.submit(task).get();
    } catch (InterruptedException | ExecutionException e) {
      FantastleReboot.logError(e);
      return false;
    }
  }

  @Override
  public boolean useItem() {
    if (!Modes.inBattle()) {
      // Abort
      return false;
    }
    Callable<Boolean> task = () -> {
      final BattleCharacter activeBC = this.mbd.getActiveCharacter();
      Creature active = null;
      if (activeBC != null) {
        active = activeBC.getCreature();
      }
      if (activeBC == null || active == null) {
        // Abort
        return false;
      }
      // Check Action Counter
      if (activeBC.canAct(MapBattleLogic.ITEM_ACTION_POINTS)) {
        BattleCharacter anyEnemy = this.mbd
            .getFirstBattlerNotOnTeam(activeBC.getTeamID());
        if (anyEnemy == null) {
          // Failed - nobody to use on
          this.setStatusMessage(activeBC.getName()
              + " tries to use an item, but nobody is there to use it on!");
          return false;
        }
        if (!active.hasMapAI()) {
          // Active character has no AI, or AI is turned off
          final boolean success = CombatItemChucker.selectAndUseItem(active);
          if (success) {
            activeBC.act(MapBattleLogic.ITEM_ACTION_POINTS);
          }
          return success;
        } else {
          // Active character has AI, and AI is turned on
          final CombatItem cui = active.getMapAI().getItemToUse();
          final boolean success = CombatItemChucker.useItem(cui, active);
          if (success) {
            activeBC.act(MapBattleLogic.ITEM_ACTION_POINTS);
          }
          return success;
        }
      } else {
        // Deny use - out of actions
        if (!active.hasMapAI()) {
          this.setStatusMessage("Out of actions!");
        }
        return false;
      }
    };
    try {
      return this.dispatch.submit(task).get();
    } catch (InterruptedException | ExecutionException e) {
      FantastleReboot.logError(e);
      return false;
    }
  }

  @Override
  public boolean steal() {
    if (!Modes.inBattle()) {
      // Abort
      return false;
    }
    Callable<Boolean> task = () -> {
      final BattleCharacter activeBC = this.mbd.getActiveCharacter();
      Creature active = null;
      if (activeBC != null) {
        active = activeBC.getCreature();
      }
      if (activeBC == null || active == null) {
        // Abort
        return false;
      }
      // Check Action Counter
      if (activeBC.canAct(MapBattleLogic.STEAL_ACTION_POINTS)) {
        int stealAmount = 0;
        final int stealChance = StatConstants.CHANCE_STEAL;
        activeBC.act(MapBattleLogic.STEAL_ACTION_POINTS);
        BattleCharacter anyEnemy = this.mbd
            .getFirstBattlerNotOnTeam(activeBC.getTeamID());
        if (anyEnemy == null) {
          // Failed - nobody to steal from
          this.setStatusMessage(activeBC.getName()
              + " tries to steal, but nobody is there to steal from!");
          return false;
        }
        Creature activeEnemy = anyEnemy.getCreature();
        final RandomRange chance = new RandomRange(0, 100);
        final int randomChance = chance.generate();
        if (randomChance <= stealChance) {
          // Succeeded
          final RandomRange stole = new RandomRange(0, activeEnemy.getGold());
          stealAmount = stole.generate();
          if (stealAmount == 0) {
            this.setStatusMessage(activeBC.getName()
                + " tries to steal, but no Gold is left to steal!");
            return false;
          } else {
            activeEnemy.offsetGold(-stealAmount);
            active.offsetGold(stealAmount);
            this.setStatusMessage(
                activeBC.getName() + " tries to steal, and successfully stole "
                    + stealAmount + " Gold!");
            return true;
          }
        } else {
          // Failed
          this.setStatusMessage(
              activeBC.getName() + " tries to steal, but fails!");
          return false;
        }
      } else {
        // Deny steal - out of actions
        if (!active.hasMapAI()) {
          this.setStatusMessage("Out of actions!");
        }
        return false;
      }
    };
    try {
      return this.dispatch.submit(task).get();
    } catch (InterruptedException | ExecutionException e) {
      FantastleReboot.logError(e);
      return false;
    }
  }

  @Override
  public boolean drain() {
    if (!Modes.inBattle()) {
      // Abort
      return false;
    }
    Callable<Boolean> task = () -> {
      final BattleCharacter activeBC = this.mbd.getActiveCharacter();
      Creature active = null;
      if (activeBC != null) {
        active = activeBC.getCreature();
      }
      if (activeBC == null || active == null) {
        // Abort
        return false;
      }
      // Check Action Counter
      if (activeBC.canAct(MapBattleLogic.DRAIN_ACTION_POINTS)) {
        int drainChance;
        int drainAmount = 0;
        drainChance = StatConstants.CHANCE_DRAIN;
        activeBC.act(MapBattleLogic.DRAIN_ACTION_POINTS);
        BattleCharacter anyEnemy = this.mbd
            .getFirstBattlerNotOnTeam(activeBC.getTeamID());
        if (anyEnemy == null) {
          // Failed - nobody to drain from
          this.setStatusMessage(activeBC.getName()
              + " tries to drain, but nobody is there to drain from!");
          return false;
        }
        Creature activeEnemy = anyEnemy.getCreature();
        if (drainChance <= 0) {
          // Failed
          this.setStatusMessage(
              activeBC.getName() + " tries to drain, but fails!");
          return false;
        } else if (drainChance >= 100) {
          // Succeeded, unless target has 0 MP
          final RandomRange drained = new RandomRange(0,
              activeEnemy.getCurrentMP());
          drainAmount = drained.generate();
          if (drainAmount == 0) {
            this.setStatusMessage(activeBC.getName()
                + " tries to drain, but no MP is left to drain!");
            return false;
          } else {
            activeEnemy.offsetCurrentMP(-drainAmount);
            active.offsetCurrentMP(drainAmount);
            this.setStatusMessage(
                activeBC.getName() + " tries to drain, and successfully drains "
                    + drainAmount + " MP!");
            return true;
          }
        } else {
          final RandomRange chance = new RandomRange(0, 100);
          final int randomChance = chance.generate();
          if (randomChance <= drainChance) {
            // Succeeded
            final RandomRange drained = new RandomRange(0,
                activeEnemy.getCurrentMP());
            drainAmount = drained.generate();
            if (drainAmount == 0) {
              this.setStatusMessage(activeBC.getName()
                  + " tries to drain, but no MP is left to drain!");
              return false;
            } else {
              activeEnemy.offsetCurrentMP(-drainAmount);
              active.offsetCurrentMP(drainAmount);
              this.setStatusMessage(activeBC.getName()
                  + " tries to drain, and successfully drains " + drainAmount
                  + " MP!");
              return true;
            }
          } else {
            // Failed
            this.setStatusMessage(
                activeBC.getName() + " tries to drain, but fails!");
            return false;
          }
        }
      } else {
        // Deny drain - out of actions
        if (!active.hasMapAI()) {
          this.setStatusMessage("Out of actions!");
        }
        return false;
      }
    };
    try {
      return this.dispatch.submit(task).get();
    } catch (InterruptedException | ExecutionException e) {
      FantastleReboot.logError(e);
      return false;
    }
  }

  @Override
  public void endTurn() {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    Runnable task = () -> {
      this.newRound = this.setNextActive(this.newRound);
      if (this.newRound) {
        this.setStatusMessage("New Round");
        this.newRound = this.setNextActive(this.newRound);
        // Check result
        BattleResults currResult = this.getResult();
        if (currResult != BattleResults.IN_PROGRESS) {
          this.doResult(currResult);
          return;
        }
      }
      this.updateStatsAndEffects();
      this.battleGUI.getViewManager()
          .setViewingWindowCenterX(this.mbd.getActiveCharacter().getY());
      this.battleGUI.getViewManager()
          .setViewingWindowCenterY(this.mbd.getActiveCharacter().getX());
      this.redrawBattle();
    };
    this.dispatch.submit(task);
  }

  private void redrawBattle() {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    this.battleGUI.redrawBattle(this.mbd);
  }

  @Override
  public void redrawOneBattleSquare(final int x, final int y,
      final FantastleObjectModel obj3) {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    this.battleGUI.redrawOneBattleSquare(this.mbd, x, y, obj3);
  }

  private void updateStatsAndEffects() {
    this.battleGUI.updateStatsAndEffects(this.mbd);
  }

  private int getActiveActionCounter() {
    return this.mbd.getActiveCharacter().getCurrentActions();
  }

  private int getActiveAttackCounter() {
    return this.mbd.getActiveCharacter().getCurrentActions();
  }

  private void decrementActiveActionCounterBy(final int amount) {
    this.mbd.getActiveCharacter().act(amount);
  }

  private void decrementActiveAttackCounter() {
    this.mbd.getActiveCharacter().act(1);
  }

  @Override
  public void maintainEffects(final boolean player) {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    final Iterator<BattleCharacter> iter = this.mbd.battlerIterator();
    while (iter.hasNext()) {
      final BattleCharacter battler = iter.next();
      // Maintain Effects
      if (battler != null && battler.isActive()) {
        final Creature active = battler.getCreature();
        // Use Effects
        active.useEffects();
        // Display all effect messages
        final String effectMessages = battler.getCreature()
            .getAllCurrentEffectMessages();
        final String[] individualEffectMessages = effectMessages.split("\n");
        for (final String message : individualEffectMessages) {
          if (!message.equals(Effect.getNullMessage())) {
            this.setStatusMessage(message);
            try {
              Thread.sleep(Prefs.getBattleSpeed());
            } catch (final InterruptedException ie) {
              // Ignore
            }
          }
        }
        // Handle low health for party members
        if (active.isAlive() && active.getTeamID() == Creature.TEAM_PARTY
            && active.getCurrentHP() <= active.getMaximumHP() * 3 / 10) {
          SoundPlayer.playSound(SoundIndex.LOW_HEALTH, SoundGroup.BATTLE);
        }
        // Cull Inactive Effects
        active.cullInactiveEffects();
        // Handle death caused by effects
        if (!active.isAlive()) {
          if (battler.getTeamID() != Creature.TEAM_PARTY) {
            // Update victory spoils
            this.battleExp = battler.getCreature().getExperience();
          }
          // Set dead character to inactive
          battler.deactivate();
          // Remove effects from dead character
          active.stripAllEffects();
          // Remove character from battle
          this.mbd.getBattleMaze().setCell(new OpenSpace(), battler.getX(),
              battler.getY(), 0, Layers.OBJECT);
          if (this.mbd.getActiveCharacter().equals(battler)) {
            // Active character died, end turn
            this.endTurn();
          }
        }
      }
    }
  }

  private void updateAllAIContexts() {
    this.mbd.updateBattlerAIContexts();
  }

  private void performNewRoundActions() {
    this.mbd.runNewRoundHooks();
  }

  @Override
  public void resetGUI() {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    // Create new GUI
    this.battleGUI = new MapBattleGUI();
  }

  @Override
  public boolean isWaitingForAI() {
    if (!Modes.inBattle()) {
      // Abort
      return false;
    }
    return !this.battleGUI.areEventHandlersOn();
  }

  private void waitForAI() {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    this.battleGUI.turnEventHandlersOff();
  }

  private void stopWaitingForAI() {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    this.battleGUI.turnEventHandlersOn();
  }

  @Override
  public void displayActiveEffects() {
    // Do nothing
  }

  @Override
  public void displayBattleStats() {
    // Do nothing
  }

  @Override
  public boolean doPlayerActions(final int action) {
    if (!Modes.inBattle()) {
      // Abort
      return false;
    }
    switch (action) {
    case AIRoutine.ACTION_CAST_SPELL:
      this.castSpell();
      break;
    case AIRoutine.ACTION_DRAIN:
      this.drain();
      break;
    case AIRoutine.ACTION_STEAL:
      this.steal();
      break;
    case AIRoutine.ACTION_USE_ITEM:
      this.useItem();
      break;
    default:
      this.endTurn();
      break;
    }
    return true;
  }

  @Override
  public void doResult(BattleResults result) {
    if (!Modes.inBattle()) {
      // Abort
      return;
    }
    Runnable task = () -> {
      this.stopWaitingForAI();
      if (!this.resultDoneAlready) {
        // Handle Results
        this.resultDoneAlready = true;
        boolean bossFlag = this.mbd
            .getFirstBattlerOnTeam(Creature.TEAM_BOSS) != null;
        boolean rewardsFlag = false;
        if (result == BattleResults.WON) {
          SoundPlayer.playSound(SoundIndex.VICTORY, SoundGroup.BATTLE);
          CommonDialogs.showTitledDialog("The party is victorious!",
              "Victory!");
          PartyManager.getParty().getLeader().offsetGold(this.getGold());
          PartyManager.getParty().getLeader().offsetExperience(this.battleExp);
          if (bossFlag) {
            rewardsFlag = true;
          }
        } else if (result == BattleResults.PERFECT) {
          SoundPlayer.playSound(SoundIndex.VICTORY, SoundGroup.BATTLE);
          CommonDialogs.showTitledDialog(
              "The party is victorious, and escaped unharmed!",
              "Perfect Victory!");
          PartyManager.getParty().getLeader().offsetGold(this.getGold());
          PartyManager.getParty().getLeader().offsetExperience(this.battleExp);
          if (bossFlag) {
            rewardsFlag = true;
          }
        } else if (result == BattleResults.LOST) {
          CommonDialogs.showTitledDialog("The party has been defeated...",
              "Defeat...");
        } else if (result == BattleResults.ANNIHILATED) {
          CommonDialogs.showTitledDialog("The party has been annihilated!",
              "Annihilation!");
        } else if (result == BattleResults.DRAW) {
          CommonDialogs.showTitledDialog("The battle was a draw.", "Draw");
        } else if (result == BattleResults.FLED) {
          CommonDialogs.showTitledDialog("The party fled!", "Party Fled");
        } else if (result == BattleResults.ENEMY_FLED) {
          CommonDialogs.showTitledDialog("The enemies fled!", "Enemies Fled");
        } else if (result == BattleResults.IN_PROGRESS) {
          CommonDialogs.showTitledDialog(
              "The battle isn't over, but somehow the game thinks it is.",
              "Uh-Oh!");
        } else {
          CommonDialogs.showTitledDialog("The result of the battle is unknown!",
              "Uh-Oh!");
        }
        // Strip effects
        PartyManager.getParty().getLeader().stripAllEffects();
        // Level Up Check
        PartyManager.getParty().checkPartyLevelUp();
        // Battle Done
        this.battleDone();
        if (rewardsFlag) {
          BossRewards.doRewards();
        }
      }
    };
    this.dispatch.submit(task);
  }

  @Override
  public boolean arrowHitCheck(final int inX, final int inY) {
    return !this.mbd.getBattleMaze().getCell(inX, inY, 0, Layers.OBJECT)
        .isSolid();
  }
}
