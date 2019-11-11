/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map.turn;

import javax.swing.JOptionPane;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.ai.AIContext;
import com.puttysoftware.fantastlereboot.ai.AIRoutine;
import com.puttysoftware.fantastlereboot.ai.map.AutoMapAI;
import com.puttysoftware.fantastlereboot.ai.map.MapAIContext;
import com.puttysoftware.fantastlereboot.assets.MusicGroup;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.battle.BattleResults;
import com.puttysoftware.fantastlereboot.battle.BossRewards;
import com.puttysoftware.fantastlereboot.battle.damageengines.AbstractDamageEngine;
import com.puttysoftware.fantastlereboot.battle.map.MapBattle;
import com.puttysoftware.fantastlereboot.battle.map.MapBattleArrowTask;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.monsters.BossMonster;
import com.puttysoftware.fantastlereboot.creatures.monsters.MonsterFactory;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.items.combat.CombatItem;
import com.puttysoftware.fantastlereboot.items.combat.CombatItemChucker;
import com.puttysoftware.fantastlereboot.loaders.MusicPlayer;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MonsterLocationManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.fantastlereboot.spells.Spell;
import com.puttysoftware.fantastlereboot.spells.SpellCaster;
import com.puttysoftware.randomrange.RandomRange;

public class MapTurnBattleLogic extends Battle {
  // Fields
  private MapTurnBattleDefinitions bd;
  private AbstractDamageEngine pde;
  private AbstractDamageEngine ede;
  private final AutoMapAI auto;
  private int damage;
  private BattleResults result;
  private int activeIndex;
  private long battleExp;
  private boolean newRound;
  private int[] speedArray;
  private int lastSpeed;
  private boolean[] speedMarkArray;
  private boolean resultDoneAlready;
  private boolean lastAIActionResult;
  private int bx;
  private int by;
  private final MapTurnBattleAITask ait;
  private MapTurnBattleGUI battleGUI;
  private BattleCharacter enemy;
  private static final int ITEM_ACTION_POINTS = 6;
  private static final int STEAL_ACTION_POINTS = 3;
  private static final int DRAIN_ACTION_POINTS = 3;
  private static final int SPELL_ACTION_POINTS = 1;

  // Constructors
  public MapTurnBattleLogic() {
    this.battleGUI = new MapTurnBattleGUI();
    this.auto = new AutoMapAI();
    this.ait = new MapTurnBattleAITask(this);
    this.ait.start();
  }

  // Methods
  @Override
  public void doBattle(final int x, final int y) {
    this.bx = x;
    this.by = y;
    final Maze m = Maze.getTemporaryBattleCopy();
    final MapBattle b = new MapBattle();
    this.doBattleInternal(m, b);
  }

  @Override
  public void doBattleByProxy(final int x, final int y) {
    this.bx = x;
    this.by = y;
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final Creature monster = MonsterFactory.getNewMonsterInstance();
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    playerCharacter.offsetExperience(monster.getExperience());
    playerCharacter.offsetGold(monster.getGold());
    // Level Up Check
    if (playerCharacter.checkLevelUp()) {
      playerCharacter.levelUp();
      Game.keepNextMessage();
      bag.showMessage("You reached level " + playerCharacter.getLevel() + ".");
    }
    final Maze m = bag.getMazeManager().getMaze();
    MonsterLocationManager.postBattle(m, this.bx, this.by);
  }

  private void doBattleInternal(final Maze bMaze, final MapBattle b) {
    // Initialize Battle
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    Game.hideOutput();
    bag.setInBattle();
    this.bd = new MapTurnBattleDefinitions();
    this.bd.setBattleMaze(bMaze);
    this.pde = AbstractDamageEngine.getPlayerInstance();
    this.ede = AbstractDamageEngine.getEnemyInstance();
    this.resultDoneAlready = false;
    this.result = BattleResults.IN_PROGRESS;
    // Generate Friends
    final BattleCharacter friends = PartyManager.getParty()
        .getBattleCharacters();
    // Generate Enemies
    this.enemy = b.getBattlers();
    this.enemy.getCreature().healAndRegenerateFully();
    this.enemy.getCreature().loadCreature();
    // Merge and Create AI Contexts
    for (int x = 0; x < 2; x++) {
      if (x == 0) {
        this.bd.addBattler(friends);
      } else {
        this.bd.addBattler(this.enemy);
      }
      if (this.bd.getBattlers()[x] != null) {
        // Create an AI Context
        this.bd.getBattlerAIContexts()[x] = new MapAIContext(
            this.bd.getBattlers()[x], this.bd.getBattleMaze());
      }
    }
    // Reset Inactive Indicators and Action Counters
    this.bd.resetBattlers();
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
        .setViewingWindowCenterX(this.bd.getActiveCharacter().getY());
    this.battleGUI.getViewManager()
        .setViewingWindowCenterY(this.bd.getActiveCharacter().getX());
    SoundPlayer.playSound(SoundIndex.DRAW_SWORD, SoundGroup.BATTLE);
    MusicPlayer.playMusic(MusicIndex.NORMAL_MAP_BATTLE, MusicGroup.BATTLE);
    this.showBattle();
    this.updateStatsAndEffects();
    this.redrawBattle();
  }

  @Override
  public void battleDone() {
    BagOStuff bag = FantastleReboot.getBagOStuff();
    // Leave Battle
    this.hideBattle();
    // Post-battle stuff
    final Maze m = bag.getMazeManager().getMaze();
    MonsterLocationManager.postBattle(m, this.bx, this.by);
    // Return to whence we came
    bag.restoreFormerMode();
    Game.redrawMaze();
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
    BattleResults currResult;
    if (this.result != BattleResults.IN_PROGRESS) {
      return this.result;
    }
    if (this.areTeamEnemiesAlive(Creature.TEAM_PARTY)
        && !this.isTeamAlive(Creature.TEAM_PARTY)) {
      currResult = BattleResults.LOST;
    } else if (!this.areTeamEnemiesAlive(Creature.TEAM_PARTY)
        && this.isTeamAlive(Creature.TEAM_PARTY)) {
      currResult = BattleResults.WON;
    } else if (!this.areTeamEnemiesAlive(Creature.TEAM_PARTY)
        && !this.isTeamAlive(Creature.TEAM_PARTY)) {
      currResult = BattleResults.DRAW;
    } else if (this.isTeamAlive(Creature.TEAM_PARTY)
        && !this.isTeamGone(Creature.TEAM_PARTY)
        && this.areTeamEnemiesDeadOrGone(Creature.TEAM_PARTY)) {
      currResult = BattleResults.WON;
    } else if (!this.isTeamAlive(Creature.TEAM_PARTY)
        && !this.isTeamGone(Creature.TEAM_PARTY)
        && !this.areTeamEnemiesDeadOrGone(Creature.TEAM_PARTY)) {
      currResult = BattleResults.LOST;
    } else if (this.areTeamEnemiesGone(Creature.TEAM_PARTY)) {
      currResult = BattleResults.ENEMY_FLED;
    } else if (this.isTeamGone(Creature.TEAM_PARTY)) {
      currResult = BattleResults.FLED;
    } else {
      currResult = BattleResults.IN_PROGRESS;
    }
    return currResult;
  }

  @Override
  public void executeNextAIAction() {
    if (this.bd != null && this.bd.getActiveCharacter() != null
        && this.bd.getActiveCharacter().getCreature() != null
        && this.bd.getActiveCharacter().getCreature().getMapAI() != null) {
      final BattleCharacter active = this.bd.getActiveCharacter();
      if (active.getCreature().isAlive()) {
        final int action = active.getCreature().getMapAI().getNextAction(
            this.bd.getActiveCharacter().getCreature(),
            this.bd.getBattlerAIContexts()[this.activeIndex]);
        switch (action) {
        case AIRoutine.ACTION_MOVE:
          final int x = active.getCreature().getMapAI().getMoveX();
          final int y = active.getCreature().getMapAI().getMoveY();
          this.lastAIActionResult = this.updatePosition(x, y);
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
          this.endTurn();
          this.stopWaitingForAI();
          this.ait.aiWait();
          break;
        }
      }
    }
  }

  @Override
  public boolean getLastAIActionResult() {
    return this.lastAIActionResult;
  }

  private void executeAutoAI(final BattleCharacter acting) {
    final int index = this.bd.findBattler(acting.getName());
    final int action = this.auto.getNextAction(acting.getCreature(),
        this.bd.getBattlerAIContexts()[index]);
    switch (action) {
    case AIRoutine.ACTION_MOVE:
      final int x = this.auto.getMoveX();
      final int y = this.auto.getMoveY();
      final int activeTID = this.bd.getActiveCharacter().getTeamID();
      final BattleCharacter theEnemy = (activeTID == Creature.TEAM_PARTY
          ? this.enemy
          : this.bd.getBattlers()[this.bd
              .findFirstBattlerOnTeam(Creature.TEAM_PARTY)]);
      final AbstractDamageEngine activeDE = (activeTID == Creature.TEAM_PARTY
          ? this.ede
          : this.pde);
      this.updatePositionInternal(x, y, false, acting, theEnemy, activeDE);
      break;
    default:
      break;
    }
  }

  private void displayRoundResults(final Creature theEnemy,
      final Creature active, final AbstractDamageEngine activeDE) {
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
    } else {
      if (this.damage < 0) {
        acting.doDamage(-this.damage);
      } else {
        theEnemy.doDamage(this.damage);
      }
    }
    this.displayRoundResults(theEnemy, acting, activeDE);
  }

  private void generateSpeedArray() {
    this.speedArray = new int[this.bd.getBattlers().length];
    this.speedMarkArray = new boolean[this.speedArray.length];
    this.resetSpeedArray();
  }

  private void resetSpeedArray() {
    for (int x = 0; x < this.speedArray.length; x++) {
      if (this.bd.getBattlers()[x] != null
          && this.bd.getBattlers()[x].getCreature().isAlive()) {
        this.speedArray[x] = (int) this.bd.getBattlers()[x].getCreature()
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
    final RandomRange randX = new RandomRange(0,
        this.bd.getBattleMaze().getRows() - 1);
    final RandomRange randY = new RandomRange(0,
        this.bd.getBattleMaze().getColumns() - 1);
    int rx, ry;
    // Set Character Locations
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        if (this.bd.getBattlers()[x].isActive()
            && this.bd.getBattlers()[x].getCreature().getX() == -1
            && this.bd.getBattlers()[x].getCreature().getY() == -1) {
          rx = randX.generate();
          ry = randY.generate();
          FantastleObjectModel obj = this.bd.getBattleMaze().getCell(rx, ry, 0,
              Layers.OBJECT);
          while (obj.isSolid()) {
            rx = randX.generate();
            ry = randY.generate();
            obj = this.bd.getBattleMaze().getCell(rx, ry, 0, Layers.OBJECT);
          }
          this.bd.getBattlers()[x].setX(rx);
          this.bd.getBattlers()[x].setY(ry);
          this.bd.getBattleMaze().setCell(this.bd.getBattlers()[x], rx, ry, 0,
              Layers.OBJECT);
        }
      }
    }
  }

  private boolean setNextActive(final boolean isNewRound) {
    int res = 0;
    if (isNewRound) {
      res = this.findNextSmallestSpeed(Integer.MAX_VALUE);
    } else {
      res = this.findNextSmallestSpeed(this.lastSpeed);
    }
    if (res != -1) {
      this.lastSpeed = this.speedArray[res];
      this.activeIndex = res;
      this.bd.setActiveCharacter(this.bd.getBattlers()[this.activeIndex]);
      // Check
      if (!this.bd.getActiveCharacter().isActive()) {
        // Inactive, pick new active character
        return this.setNextActive(isNewRound);
      }
      // AI Check
      if (this.bd.getActiveCharacter().getCreature().hasMapAI()) {
        // Run AI
        this.waitForAI();
        this.ait.aiRun();
      } else {
        // No AI
        SoundPlayer.playSound(SoundIndex.PLAYER_UP, SoundGroup.BATTLE);
      }
      return false;
    } else {
      // Reset Speed Array
      this.resetSpeedArray();
      // Reset Action Counters
      this.bd.roundResetBattlers();
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
    int res = 0;
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        if (this.bd.getBattlers()[x].getTeamID() != 0) {
          res += this.bd.getBattlers()[x].getCreature().getGold();
        }
      }
    }
    return res;
  }

  private boolean isTeamAlive(final int teamID) {
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        if (this.bd.getBattlers()[x].getTeamID() == teamID) {
          final boolean res = this.bd.getBattlers()[x].getCreature().isAlive();
          if (res) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean areTeamEnemiesAlive(final int teamID) {
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        if (this.bd.getBattlers()[x].getTeamID() != teamID) {
          final boolean res = this.bd.getBattlers()[x].getCreature().isAlive();
          if (res) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean areTeamEnemiesDeadOrGone(final int teamID) {
    int deadCount = 0;
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        if (this.bd.getBattlers()[x].getTeamID() != teamID) {
          final boolean res = this.bd.getBattlers()[x].getCreature().isAlive()
              && this.bd.getBattlers()[x].isActive();
          if (res) {
            return false;
          }
          if (!this.bd.getBattlers()[x].getCreature().isAlive()) {
            deadCount++;
          }
        }
      }
    }
    return (deadCount > 0);
  }

  private boolean areTeamEnemiesGone(final int teamID) {
    boolean res = true;
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        if (this.bd.getBattlers()[x].getTeamID() != teamID) {
          if (this.bd.getBattlers()[x].getCreature().isAlive()) {
            res = res && !this.bd.getBattlers()[x].isActive();
            if (!res) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  private boolean isTeamGone(final int teamID) {
    boolean res = true;
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        if (this.bd.getBattlers()[x].getTeamID() == teamID) {
          if (this.bd.getBattlers()[x].getCreature().isAlive()) {
            res = res && !this.bd.getBattlers()[x].isActive();
            if (!res) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean updatePosition(final int x, final int y) {
    final int activeTID = this.bd.getActiveCharacter().getTeamID();
    BattleCharacter theEnemy = (activeTID == Creature.TEAM_PARTY ? this.enemy
        : this.bd.getBattlers()[this.bd
            .findFirstBattlerOnTeam(Creature.TEAM_PARTY)]);
    final AbstractDamageEngine activeDE = (activeTID == Creature.TEAM_PARTY
        ? this.ede
        : this.pde);
    if (x == 0 && y == 0) {
      theEnemy = this.bd.getActiveCharacter();
    }
    return this.updatePositionInternal(x, y, true, this.bd.getActiveCharacter(),
        theEnemy, activeDE);
  }

  @Override
  public void fireArrow(final int x, final int y) {
    if (this.bd.getActiveCharacter().getCurrentActions() > 0) {
      // Has actions left
      this.bd.getActiveCharacter().act(1);
      this.updateStatsAndEffects();
      this.battleGUI.turnEventHandlersOff();
      final MapBattleArrowTask at = new MapBattleArrowTask(x, y,
          this.bd.getBattleMaze(), this.bd.getActiveCharacter());
      at.start();
    } else {
      // Deny arrow - out of actions
      if (!this.bd.getActiveCharacter().getCreature().hasMapAI()) {
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
      this.bd.getBattleMaze().setCell(new OpenSpace(), hit.getX(), hit.getY(),
          0, Layers.OBJECT);
    }
    // Check result
    final BattleResults currResult = this.getResult();
    if (currResult != BattleResults.IN_PROGRESS) {
      // Battle Done
      this.result = currResult;
      this.doResult();
    }
  }

  private boolean updatePositionInternal(final int x, final int y,
      final boolean useAP, final BattleCharacter active,
      final BattleCharacter theEnemy, final AbstractDamageEngine activeDE) {
    this.updateAllAIContexts();
    int px = active.getX();
    int py = active.getY();
    final Maze m = this.bd.getBattleMaze();
    FantastleObjectModel next = null;
    FantastleObjectModel nextGround = null;
    FantastleObjectModel currGround = null;
    active.saveLocation();
    this.battleGUI.getViewManager().saveViewingWindow();
    try {
      next = m.getCell(px + x, py + y, 0, Layers.OBJECT);
      nextGround = m.getCell(px + x, py + y, 0, Layers.GROUND);
      currGround = m.getCell(px, py, 0, Layers.GROUND);
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      // Ignore
    }
    if (next != null && nextGround != null && currGround != null) {
      if (!next.isSolid()) {
        if ((useAP
            && this.getActiveActionCounter() >= AIContext.getDefaultAPCost())
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
          try {
            obj1 = m.getCell(px - 1, py - 1, 0, Layers.OBJECT);
          } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
          }
          try {
            obj2 = m.getCell(px, py - 1, 0, Layers.OBJECT);
          } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
          }
          try {
            obj3 = m.getCell(px + 1, py - 1, 0, Layers.OBJECT);
          } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
          }
          try {
            obj4 = m.getCell(px - 1, py, 0, Layers.OBJECT);
          } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
          }
          try {
            obj6 = m.getCell(px + 1, py - 1, 0, Layers.OBJECT);
          } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
          }
          try {
            obj7 = m.getCell(px - 1, py + 1, 0, Layers.OBJECT);
          } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
          }
          try {
            obj8 = m.getCell(px, py + 1, 0, Layers.OBJECT);
          } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
          }
          try {
            obj9 = m.getCell(px + 1, py + 1, 0, Layers.OBJECT);
          } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
          }
          // Auto-attack check
          if (obj1 != null) {
            if (obj1 instanceof BattleCharacter) {
              if (!((x == -1 && y == 0) || (x == -1 && y == -1)
                  || (x == 0 && y == -1))) {
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
              if (!((x == 0 && y == -1) || (x == 1 && y == -1)
                  || (x == 1 && y == 0))) {
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
              if (!((x == -1 && y == 0) || (x == -1 && y == 1)
                  || (x == 0 && y == 1))) {
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
              if (!((x == 0 && y == 1) || (x == 1 && y == 1)
                  || (x == 1 && y == 0))) {
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
          if (!this.bd.getActiveCharacter().getCreature().hasMapAI()) {
            this.setStatusMessage("Out of moves!");
          }
          return false;
        }
      } else {
        if (next instanceof BattleCharacter) {
          if ((useAP && this.getActiveAttackCounter() > 0) || !useAP) {
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
            this.computeDamage(theEnemy.getCreature(), active.getCreature(),
                activeDE);
            // Handle low health for party members
            if (theEnemy.getCreature().isAlive()
                && theEnemy.getTeamID() == Creature.TEAM_PARTY
                && theEnemy.getCreature()
                    .getCurrentHP() <= theEnemy.getCreature().getMaximumHP() * 3
                        / 10) {
              SoundPlayer.playSound(SoundIndex.LOW_HEALTH, SoundGroup.BATTLE);
            }
            // Handle enemy death
            if (!theEnemy.getCreature().isAlive()) {
              if (theEnemy.getTeamID() != Creature.TEAM_PARTY) {
                // Update victory spoils
                this.battleExp = theEnemy.getCreature().getExperience();
              }
              // Remove effects from dead character
              bc.getCreature().stripAllEffects();
              // Set dead character to inactive
              bc.deactivate();
              // Remove character from battle
              this.bd.getBattleMaze().setCell(new OpenSpace(), bc.getX(),
                  bc.getY(), 0, Layers.OBJECT);
            }
            // Handle self death
            if (!active.getCreature().isAlive()) {
              // Remove effects from dead character
              active.getCreature().stripAllEffects();
              // Set dead character to inactive
              active.deactivate();
              // Remove character from battle
              this.bd.getBattleMaze().setCell(new OpenSpace(), active.getX(),
                  active.getY(), 0, Layers.OBJECT);
              // End turn
              this.endTurn();
            }
          } else {
            // Deny attack - out of actions
            if (!this.bd.getActiveCharacter().getCreature().hasMapAI()) {
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
      // Flee
      this.battleGUI.getViewManager().restoreViewingWindow();
      active.restoreLocation();
      // Set fled character to inactive
      active.deactivate();
      // Remove character from battle
      m.setCell(new OpenSpace(), active.getX(), active.getY(), 0,
          Layers.OBJECT);
      // End Turn
      this.endTurn();
      this.updateStatsAndEffects();
      final BattleResults currResult = this.getResult();
      if (currResult != BattleResults.IN_PROGRESS) {
        // Battle Done
        this.result = currResult;
        this.doResult();
      }
      this.battleGUI.getViewManager().setViewingWindowCenterX(py);
      this.battleGUI.getViewManager().setViewingWindowCenterY(px);
      this.redrawBattle();
      return true;
    }
    this.updateStatsAndEffects();
    final BattleResults currResult = this.getResult();
    if (currResult != BattleResults.IN_PROGRESS) {
      // Battle Done
      this.result = currResult;
      this.doResult();
    }
    this.battleGUI.getViewManager().setViewingWindowCenterX(py);
    this.battleGUI.getViewManager().setViewingWindowCenterY(px);
    this.redrawBattle();
    return true;
  }

  @Override
  public Creature getEnemy() {
    return this.enemy.getCreature();
  }

  private BattleCharacter getEnemyBC() {
    final int px = this.bd.getActiveCharacter().getX();
    final int py = this.bd.getActiveCharacter().getY();
    final Maze m = this.bd.getBattleMaze();
    FantastleObjectModel next = null;
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        if (x == 0 && y == 0) {
          continue;
        }
        try {
          next = m.getCell(px + x, py + y, 0, Layers.OBJECT);
        } catch (final ArrayIndexOutOfBoundsException aioob) {
          // Ignore
        }
        if (next != null) {
          if (next.isSolid()) {
            if (next instanceof BattleCharacter) {
              return (BattleCharacter) next;
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public void showBattle() {
    this.battleGUI.showBattle();
  }

  @Override
  public void hideBattle() {
    this.battleGUI.hideBattle();
  }

  @Override
  public boolean castSpell() {
    BattleCharacter activeBC = this.bd.getActiveCharacter();
    Creature active = null;
    if (activeBC != null) {
      active = activeBC.getCreature();
    }
    BattleCharacter enemyBC = this.getEnemyBC();
    Creature activeEnemy = null;
    if (enemyBC != null) {
      activeEnemy = enemyBC.getCreature();
    }
    if (activeBC == null || active == null) {
      // Abort
      return false;
    }
    // Check Action Counter
    if (activeBC.canAct(MapTurnBattleLogic.SPELL_ACTION_POINTS)) {
      if (enemyBC == null || activeEnemy == null) {
        // Failed - nobody to use on
        this.setStatusMessage(activeBC.getName()
            + " tries to cast a spell, but nobody is there to cast it on!");
        return false;
      }
      if (!active.hasMapAI()) {
        // Active character has no AI, or AI is turned off
        final boolean success = SpellCaster.selectAndCastSpell(active);
        if (success) {
          activeBC.act(MapTurnBattleLogic.SPELL_ACTION_POINTS);
        }
        return success;
      } else {
        // Active character has AI, and AI is turned on
        final Spell spell = active.getMapAI().getSpellToCast();
        final boolean success = SpellCaster.castSpell(spell, active);
        if (success) {
          activeBC.act(MapTurnBattleLogic.SPELL_ACTION_POINTS);
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
  }

  @Override
  public boolean useItem() {
    BattleCharacter activeBC = this.bd.getActiveCharacter();
    Creature active = null;
    if (activeBC != null) {
      active = activeBC.getCreature();
    }
    BattleCharacter enemyBC = this.getEnemyBC();
    Creature activeEnemy = null;
    if (enemyBC != null) {
      activeEnemy = enemyBC.getCreature();
    }
    if (activeBC == null || active == null) {
      // Abort
      return false;
    }
    // Check Action Counter
    if (activeBC.canAct(MapTurnBattleLogic.ITEM_ACTION_POINTS)) {
      if (enemyBC == null || activeEnemy == null) {
        // Failed - nobody to use on
        this.setStatusMessage(activeBC.getName()
            + " tries to use an item, but nobody is there to use it on!");
        return false;
      }
      if (!active.hasMapAI()) {
        // Active character has no AI, or AI is turned off
        final boolean success = CombatItemChucker.selectAndUseItem(active);
        if (success) {
          activeBC.act(MapTurnBattleLogic.ITEM_ACTION_POINTS);
        }
        return success;
      } else {
        // Active character has AI, and AI is turned on
        final CombatItem cui = active.getMapAI().getItemToUse();
        final boolean success = CombatItemChucker.useItem(cui, active);
        if (success) {
          activeBC.act(MapTurnBattleLogic.ITEM_ACTION_POINTS);
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
  }

  @Override
  public boolean steal() {
    BattleCharacter activeBC = this.bd.getActiveCharacter();
    Creature active = null;
    if (activeBC != null) {
      active = activeBC.getCreature();
    }
    BattleCharacter enemyBC = this.getEnemyBC();
    Creature activeEnemy = null;
    if (enemyBC != null) {
      activeEnemy = enemyBC.getCreature();
    }
    if (activeBC == null || active == null) {
      // Abort
      return false;
    }
    // Check Action Counter
    if (activeBC.canAct(MapTurnBattleLogic.STEAL_ACTION_POINTS)) {
      int stealAmount = 0;
      int stealChance = StatConstants.CHANCE_STEAL;
      activeBC.act(MapTurnBattleLogic.STEAL_ACTION_POINTS);
      if (enemyBC == null || activeEnemy == null) {
        // Failed - nobody to steal from
        this.setStatusMessage(activeBC.getName()
            + " tries to steal, but nobody is there to steal from!");
        return false;
      }
      if (stealChance <= 0) {
        // Failed
        this.setStatusMessage(
            activeBC.getName() + " tries to steal, but fails!");
        return false;
      } else if (stealChance >= 100) {
        // Succeeded, unless target has 0 Gold
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
      }
    } else {
      // Deny steal - out of actions
      if (!active.hasMapAI()) {
        this.setStatusMessage("Out of actions!");
      }
      return false;
    }
  }

  @Override
  public boolean drain() {
    BattleCharacter activeBC = this.bd.getActiveCharacter();
    Creature active = null;
    if (activeBC != null) {
      active = activeBC.getCreature();
    }
    BattleCharacter enemyBC = this.getEnemyBC();
    Creature activeEnemy = null;
    if (enemyBC != null) {
      activeEnemy = enemyBC.getCreature();
    }
    if (activeBC == null || active == null) {
      // Abort
      return false;
    }
    // Check Action Counter
    if (activeBC.canAct(MapTurnBattleLogic.DRAIN_ACTION_POINTS)) {
      int drainChance;
      int drainAmount = 0;
      drainChance = StatConstants.CHANCE_DRAIN;
      activeBC.act(MapTurnBattleLogic.DRAIN_ACTION_POINTS);
      if (enemyBC == null || activeEnemy == null) {
        // Failed - nobody to drain from
        this.setStatusMessage(activeBC.getName()
            + " tries to drain, but nobody is there to drain from!");
        return false;
      }
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
            this.setStatusMessage(
                activeBC.getName() + " tries to drain, and successfully drains "
                    + drainAmount + " MP!");
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
  }

  @Override
  public void endTurn() {
    this.newRound = this.setNextActive(this.newRound);
    if (this.newRound) {
      this.setStatusMessage("New Round");
      this.newRound = this.setNextActive(this.newRound);
      // Check result
      this.result = this.getResult();
      if (this.result != BattleResults.IN_PROGRESS) {
        this.doResult();
        return;
      }
    }
    this.updateStatsAndEffects();
    this.battleGUI.getViewManager()
        .setViewingWindowCenterX(this.bd.getActiveCharacter().getY());
    this.battleGUI.getViewManager()
        .setViewingWindowCenterY(this.bd.getActiveCharacter().getX());
    this.redrawBattle();
  }

  private void redrawBattle() {
    this.battleGUI.redrawBattle(this.bd);
  }

  @Override
  public void redrawOneBattleSquare(final int x, final int y,
      final FantastleObjectModel obj3) {
    this.battleGUI.redrawOneBattleSquare(this.bd, x, y, obj3);
  }

  private void updateStatsAndEffects() {
    this.battleGUI.updateStatsAndEffects(this.bd);
  }

  private int getActiveActionCounter() {
    return this.bd.getActiveCharacter().getCurrentActions();
  }

  private int getActiveAttackCounter() {
    return this.bd.getActiveCharacter().getCurrentActions();
  }

  private void decrementActiveActionCounterBy(final int amount) {
    this.bd.getActiveCharacter().act(amount);
  }

  private void decrementActiveAttackCounter() {
    this.bd.getActiveCharacter().act(1);
  }

  @Override
  public void maintainEffects(final boolean player) {
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      // Maintain Effects
      if (this.bd.getBattlers()[x] != null
          && this.bd.getBattlers()[x].isActive()) {
        final Creature active = this.bd.getBattlers()[x].getCreature();
        // Use Effects
        active.useEffects();
        // Display all effect messages
        final String effectMessages = this.bd.getBattlers()[x].getCreature()
            .getAllCurrentEffectMessages();
        final String[] individualEffectMessages = effectMessages.split("\n");
        for (final String message : individualEffectMessages) {
          if (!message.equals(Effect.getNullMessage())) {
            this.setStatusMessage(message);
            try {
              Thread.sleep(PreferencesManager.getBattleSpeed());
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
          if (this.bd.getBattlers()[x].getTeamID() != Creature.TEAM_PARTY) {
            // Update victory spoils
            this.battleExp = this.bd.getBattlers()[x].getCreature()
                .getExperience();
          }
          // Set dead character to inactive
          this.bd.getBattlers()[x].deactivate();
          // Remove effects from dead character
          active.stripAllEffects();
          // Remove character from battle
          this.bd.getBattleMaze().setCell(new OpenSpace(),
              this.bd.getBattlers()[x].getX(), this.bd.getBattlers()[x].getY(),
              0, Layers.OBJECT);
          if (this.bd.getActiveCharacter().getName()
              .equals(this.bd.getBattlers()[x].getName())) {
            // Active character died, end turn
            this.endTurn();
          }
        }
      }
    }
  }

  private void updateAllAIContexts() {
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        // Update all AI Contexts
        if (this.bd.getBattlerAIContexts()[x] != null) {
          this.bd.getBattlerAIContexts()[x]
              .updateContext(this.bd.getBattleMaze());
        }
      }
    }
  }

  private void performNewRoundActions() {
    for (int x = 0; x < this.bd.getBattlers().length; x++) {
      if (this.bd.getBattlers()[x] != null) {
        // Perform New Round Actions
        if (this.bd.getBattlerAIContexts()[x] != null
            && this.bd.getBattlerAIContexts()[x].getCharacter().getCreature()
                .hasMapAI()
            && this.bd.getBattlers()[x].isActive()
            && this.bd.getBattlers()[x].getCreature().isAlive()) {
          this.bd.getBattlerAIContexts()[x].getCharacter().getCreature()
              .getMapAI().newRoundHook();
        }
      }
    }
  }

  @Override
  public void resetGUI() {
    // Create new GUI
    this.battleGUI = new MapTurnBattleGUI();
  }

  @Override
  public boolean isWaitingForAI() {
    return !this.battleGUI.areEventHandlersOn();
  }

  private void waitForAI() {
    this.battleGUI.turnEventHandlersOff();
  }

  private void stopWaitingForAI() {
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
  public void doResult() {
    this.stopWaitingForAI();
    if (!this.resultDoneAlready) {
      // Handle Results
      this.resultDoneAlready = true;
      boolean rewardsFlag = false;
      if (this.getEnemy() instanceof BossMonster) {
        if (this.result == BattleResults.WON
            || this.result == BattleResults.PERFECT) {
          this.setStatusMessage("You defeated the Boss!");
          SoundPlayer.playSound(SoundIndex.VICTORY, SoundGroup.BATTLE);
          rewardsFlag = true;
        } else if (this.result == BattleResults.LOST) {
          this.setStatusMessage("The Boss defeated you...");
          SoundPlayer.playSound(SoundIndex.GAME_OVER, SoundGroup.BATTLE);
          PartyManager.getParty().getLeader().onDeath(-10);
        } else if (this.result == BattleResults.ANNIHILATED) {
          this.setStatusMessage(
              "The Boss defeated you without suffering damage... you were annihilated!");
          SoundPlayer.playSound(SoundIndex.GAME_OVER, SoundGroup.BATTLE);
          PartyManager.getParty().getLeader().onDeath(-20);
        } else if (this.result == BattleResults.DRAW) {
          this.setStatusMessage(
              "The Boss battle was a draw. You are fully healed!");
          PartyManager.getParty().getLeader()
              .healPercentage(Creature.FULL_HEAL_PERCENTAGE);
          PartyManager.getParty().getLeader()
              .regeneratePercentage(Creature.FULL_HEAL_PERCENTAGE);
        } else if (this.result == BattleResults.FLED) {
          this.setStatusMessage("You ran away successfully!");
        } else if (this.result == BattleResults.ENEMY_FLED) {
          this.setStatusMessage("The Boss ran away!");
        }
      } else {
        if (this.result == BattleResults.WON) {
          SoundPlayer.playSound(SoundIndex.VICTORY, SoundGroup.BATTLE);
          CommonDialogs.showTitledDialog("The party is victorious!",
              "Victory!");
          PartyManager.getParty().getLeader().offsetGold(this.getGold());
          PartyManager.getParty().getLeader().offsetExperience(this.battleExp);
        } else if (this.result == BattleResults.LOST) {
          CommonDialogs.showTitledDialog("The party has been defeated!",
              "Defeat...");
        } else if (this.result == BattleResults.DRAW) {
          CommonDialogs.showTitledDialog("The battle was a draw.", "Draw");
        } else if (this.result == BattleResults.FLED) {
          CommonDialogs.showTitledDialog("The party fled!", "Party Fled");
        } else if (this.result == BattleResults.ENEMY_FLED) {
          CommonDialogs.showTitledDialog("The enemies fled!", "Enemies Fled");
        } else if (this.result == BattleResults.IN_PROGRESS) {
          CommonDialogs.showTitledDialog(
              "The battle isn't over, but somehow the game thinks it is.",
              "Uh-Oh!");
        } else {
          CommonDialogs.showTitledDialog("The result of the battle is unknown!",
              "Uh-Oh!");
        }
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
  }

  @Override
  public void setResult(final BattleResults resultCode) {
    // Do nothing
  }

  @Override
  public boolean arrowHitCheck(int inX, int inY) {
    return !this.bd.getBattleMaze().getCell(inX, inY, 0, Layers.OBJECT)
        .isSolid();
  }
}
