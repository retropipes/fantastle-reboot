package com.puttysoftware.fantastlereboot.battle.window.time;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.ai.window.AbstractWindowAIRoutine;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.battle.BattleResults;
import com.puttysoftware.fantastlereboot.battle.damageengines.AbstractDamageEngine;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.monsters.BossMonster;
import com.puttysoftware.fantastlereboot.creatures.monsters.Monster;
import com.puttysoftware.fantastlereboot.creatures.monsters.MonsterFactory;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.effects.Effect;
import com.puttysoftware.fantastlereboot.game.GameLogicManager;
import com.puttysoftware.fantastlereboot.items.combat.CombatItemChucker;
import com.puttysoftware.fantastlereboot.loaders.MusicPlayer;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.fantastlereboot.spells.SpellCaster;
import com.puttysoftware.randomrange.RandomRange;

public class WindowTimeBattleLogic extends Battle {
  // Fields
  private int stealAmount;
  private int damage;
  private boolean enemyDidDamage;
  private boolean playerDidDamage;
  private Creature enemy;
  private BattleResults result;
  private final AbstractDamageEngine pde;
  private final AbstractDamageEngine ede;
  WindowTimeBattleGUI battleGUI;
  private final Timer battleTimer;
  private static final int BASE_RUN_CHANCE = 80;
  private static final int RUN_CHANCE_DIFF_FACTOR = 5;
  private static final int ENEMY_BASE_RUN_CHANCE = 60;
  private static final int ENEMY_RUN_CHANCE_DIFF_FACTOR = 10;

  // Constructor
  public WindowTimeBattleLogic() {
    // Initialize Battle Parameters
    this.pde = AbstractDamageEngine.getPlayerInstance();
    this.ede = AbstractDamageEngine.getEnemyInstance();
    this.battleTimer = new Timer();
    this.battleTimer.schedule(new PlayerTask(), 0,
        WindowTimeBattleSpeed.getSpeed());
    this.battleTimer.schedule(new EnemyTask(), 0,
        WindowTimeBattleSpeed.getSpeed());
    this.damage = 0;
    this.stealAmount = 0;
    this.enemyDidDamage = false;
    this.playerDidDamage = false;
    // Initialize GUI
    this.battleGUI = new WindowTimeBattleGUI();
  }

  @Override
  public JFrame getOutputFrame() {
    return this.battleGUI.getOutputFrame();
  }

  @Override
  public final boolean doPlayerActions(final int actionToPerform) {
    boolean success = true;
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    if (actionToPerform == AbstractWindowAIRoutine.ACTION_ATTACK) {
      final int actions = playerCharacter.getWindowBattleActionsPerRound();
      for (int x = 0; x < actions; x++) {
        this.computePlayerDamage();
        this.displayPlayerRoundResults();
      }
    } else if (actionToPerform == AbstractWindowAIRoutine.ACTION_CAST_SPELL) {
      success = this.castSpell();
    } else if (actionToPerform == AbstractWindowAIRoutine.ACTION_FLEE) {
      final RandomRange rf = new RandomRange(0, 100);
      final int runChance = rf.generate();
      if (runChance <= this.computeRunChance()) {
        // Success
        this.setResult(BattleResults.FLED);
      } else {
        // Failure
        success = false;
        this.updateMessageAreaFleeFailed();
      }
    } else if (actionToPerform == AbstractWindowAIRoutine.ACTION_STEAL) {
      success = this.steal();
      if (success) {
        SoundPlayer.playSound(SoundIndex.DRAIN);
        this.updateMessageAreaPostSteal();
      } else {
        SoundPlayer.playSound(SoundIndex.ACTION_FAILED);
        this.updateMessageAreaStealFailed();
      }
    } else if (actionToPerform == AbstractWindowAIRoutine.ACTION_DRAIN) {
      success = this.drain();
      if (success) {
        SoundPlayer.playSound(SoundIndex.DRAIN);
        this.updateMessageAreaPostDrain();
      } else {
        SoundPlayer.playSound(SoundIndex.ACTION_FAILED);
        this.updateMessageAreaDrainFailed();
      }
    } else if (actionToPerform == AbstractWindowAIRoutine.ACTION_USE_ITEM) {
      success = this.useItem();
    }
    if (success) {
      this.battleGUI.resetPlayerActionBar();
    }
    return success;
  }

  @Override
  public final void executeNextAIAction() {
    final int actionToPerform = this.enemy.getWindowAI()
        .getNextAction(this.enemy);
    if (actionToPerform == AbstractWindowAIRoutine.ACTION_ATTACK) {
      final int actions = this.enemy.getWindowBattleActionsPerRound();
      for (int x = 0; x < actions; x++) {
        this.computeEnemyDamage();
        this.displayEnemyRoundResults();
      }
    } else if (actionToPerform == AbstractWindowAIRoutine.ACTION_CAST_SPELL) {
      SpellCaster.castSpell(this.enemy.getWindowAI().getSpellToCast(),
          this.enemy);
    } else if (actionToPerform == AbstractWindowAIRoutine.ACTION_FLEE) {
      final RandomRange rf = new RandomRange(0, 100);
      final int runChance = rf.generate();
      if (runChance <= this.computeEnemyRunChance()) {
        // Success
        this.setResult(BattleResults.ENEMY_FLED);
      } else {
        // Failure
        this.updateMessageAreaEnemyFleeFailed();
      }
    }
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
    // Check damage
    if (acting instanceof PartyMember) {
      if (this.damage > 0) {
        this.playerDidDamage = true;
      } else if (this.damage < 0) {
        this.enemyDidDamage = true;
      }
    } else if (acting instanceof Monster || acting instanceof BossMonster) {
      if (this.damage > 0) {
        this.enemyDidDamage = true;
      } else if (this.damage < 0) {
        this.playerDidDamage = true;
      }
    }
  }

  final void computePlayerDamage() {
    // Compute Player Damage
    this.computeDamage(this.enemy, PartyManager.getParty().getLeader(),
        this.pde);
  }

  final void computeEnemyDamage() {
    // Compute Enemy Damage
    this.computeDamage(PartyManager.getParty().getLeader(), this.enemy,
        this.ede);
  }

  final int computeRunChance() {
    return WindowTimeBattleLogic.BASE_RUN_CHANCE
        - this.enemy.getLevelDifference()
            * WindowTimeBattleLogic.RUN_CHANCE_DIFF_FACTOR;
  }

  final int computeEnemyRunChance() {
    return WindowTimeBattleLogic.ENEMY_BASE_RUN_CHANCE
        + this.enemy.getLevelDifference()
            * WindowTimeBattleLogic.ENEMY_RUN_CHANCE_DIFF_FACTOR;
  }

  @Override
  public final void displayBattleStats() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    final String enemyName = this.enemy.getName();
    final String fightingWhat = this.enemy.getFightingWhatString();
    final String monsterLevelString = enemyName + "'s Level: "
        + Integer.toString(this.enemy.getLevel());
    final String monsterHPString = this.enemy.getHPString();
    final String monsterMPString = this.enemy.getMPString();
    final String playerHPString = playerCharacter.getHPString();
    final String playerMPString = playerCharacter.getMPString();
    final String displayMonsterHPString = enemyName + "'s HP: "
        + monsterHPString;
    final String displayMonsterMPString = enemyName + "'s MP: "
        + monsterMPString;
    final String displayPlayerHPString = "Your HP: " + playerHPString;
    final String displayPlayerMPString = "Your MP: " + playerMPString;
    final String displayString = fightingWhat + "\n" + monsterLevelString + "\n"
        + displayMonsterHPString + "\n" + displayMonsterMPString + "\n"
        + displayPlayerHPString + "\n" + displayPlayerMPString;
    this.setStatusMessage(displayString);
  }

  final void displayPlayerRoundResults() {
    // Display player round results
    if (this.result != BattleResults.ENEMY_FLED) {
      final String enemyName = this.enemy.getName();
      final String playerDamageString = Integer.toString(this.damage);
      final String playerFumbleDamageString = Integer.toString(this.damage);
      String displayPlayerDamageString = null;
      String playerWhackString = "";
      if (this.pde.weaponFumble()) {
        displayPlayerDamageString = "FUMBLE! You drop your weapon, doing "
            + playerFumbleDamageString + " damage to yourself!";
        SoundPlayer.playSound(SoundIndex.FUMBLE);
      } else {
        if (this.damage == 0) {
          displayPlayerDamageString = "You try to hit the " + enemyName
              + ", but MISS!";
          SoundPlayer.playSound(SoundIndex.MISSED);
        } else if (this.damage < 0) {
          displayPlayerDamageString = "You try to hit the " + enemyName
              + ", but are RIPOSTED for " + (-this.damage) + " damage!";
          SoundPlayer.playSound(SoundIndex.PARTY_COUNTER);
        } else {
          displayPlayerDamageString = "You hit the " + enemyName + " for "
              + playerDamageString + " damage!";
          SoundPlayer.playSound(SoundIndex.PARTY_HIT);
        }
        if (this.pde.weaponCrit()) {
          playerWhackString += "CRITICAL HIT!\n";
          SoundPlayer.playSound(SoundIndex.CRITICAL);
        }
        if (this.pde.weaponPierce()) {
          playerWhackString += "Your attack pierces the " + enemyName
              + "'s armor!\n";
        }
      }
      final String displayString = playerWhackString
          + displayPlayerDamageString;
      this.setStatusMessage(displayString);
    }
  }

  final void displayEnemyRoundResults() {
    // Display enemy round results
    if (this.result != BattleResults.ENEMY_FLED) {
      final String enemyName = this.enemy.getName();
      final String enemyDamageString = Integer.toString(this.damage);
      final String enemyFumbleDamageString = Integer.toString(this.damage);
      String displayEnemyDamageString = null;
      String enemyWhackString = "";
      if (this.ede.weaponFumble()) {
        displayEnemyDamageString = "FUMBLE! The " + enemyName
            + " drops its weapon, doing " + enemyFumbleDamageString
            + " damage to itself!";
        SoundPlayer.playSound(SoundIndex.FUMBLE);
        enemyWhackString = "";
      } else {
        if (this.damage == 0) {
          displayEnemyDamageString = "The " + enemyName
              + " tries to hit you, but MISSES!";
          SoundPlayer.playSound(SoundIndex.MISSED);
        } else if (this.damage < 0) {
          displayEnemyDamageString = "The " + enemyName
              + " tries to hit you, but you RIPOSTE for " + (-this.damage)
              + " damage!";
          SoundPlayer.playSound(SoundIndex.MONSTER_COUNTER);
        } else {
          displayEnemyDamageString = "The " + enemyName + " hits you for "
              + enemyDamageString + " damage!";
          SoundPlayer.playSound(SoundIndex.MONSTER_HIT);
        }
        if (this.ede.weaponCrit()) {
          enemyWhackString += "CRITICAL HIT!\n";
          SoundPlayer.playSound(SoundIndex.CRITICAL);
        }
        if (this.ede.weaponPierce()) {
          enemyWhackString += "The " + enemyName
              + "'s attack pierces YOUR armor!\n";
        }
      }
      final String displayString = enemyWhackString + displayEnemyDamageString;
      this.setStatusMessage(displayString);
    }
  }

  // Methods
  @Override
  public void doBattle() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    final BagOStuff app = FantastleReboot.getBagOStuff();
    try {
      final GameLogicManager gm = app.getGameManager();
      if (bag.getMode() != BagOStuff.STATUS_BATTLE) {
        SoundPlayer.playSound(SoundIndex.DRAW_SWORD);
        MusicPlayer.playMusic(MusicIndex.NORMAL_WINDOW_BATTLE);
      }
      bag.setInBattle();
      gm.hideOutput();
      gm.stopMovement();
      this.enemy = MonsterFactory.getNewMonsterInstance();
      this.enemy.loadCreature();
      this.battleGUI.setMaxPlayerActionBarValue(
          PartyManager.getParty().getLeader().getActionBarSpeed());
      this.battleGUI.setMaxEnemyActionBarValue(this.enemy.getActionBarSpeed());
      this.enemyDidDamage = false;
      this.playerDidDamage = false;
      this.setResult(BattleResults.IN_PROGRESS);
      this.battleGUI.initBattle(this.enemy.getImage());
      this.firstUpdateMessageArea();
    } catch (final Throwable t) {
      FantastleReboot.logError(t);
    }
  }

  @Override
  public void doBattleByProxy() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    this.enemy = MonsterFactory.getNewMonsterInstance();
    this.enemy.loadCreature();
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    final Creature m = this.enemy;
    playerCharacter.offsetExperience(m.getExperience());
    playerCharacter.offsetGold(m.getGold());
    // Level Up Check
    if (playerCharacter.checkLevelUp()) {
      playerCharacter.levelUp();
      FantastleReboot.getBagOStuff().getGameManager().keepNextMessage();
      bag.showMessage("You reached level " + playerCharacter.getLevel() + ".");
    }
  }

  @Override
  public final BattleResults getResult() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    BattleResults currResult;
    if (this.result != BattleResults.IN_PROGRESS) {
      return this.result;
    }
    if (this.enemy.isAlive() && !playerCharacter.isAlive()) {
      if (!this.playerDidDamage) {
        currResult = BattleResults.ANNIHILATED;
      } else {
        currResult = BattleResults.LOST;
      }
    } else if (!this.enemy.isAlive() && playerCharacter.isAlive()) {
      if (!this.enemyDidDamage) {
        currResult = BattleResults.PERFECT;
      } else {
        currResult = BattleResults.WON;
      }
    } else if (!this.enemy.isAlive() && !playerCharacter.isAlive()) {
      currResult = BattleResults.DRAW;
    } else {
      currResult = BattleResults.IN_PROGRESS;
    }
    return currResult;
  }

  @Override
  public final void maintainEffects(final boolean player) {
    if (player) {
      final PartyMember playerCharacter = PartyManager.getParty().getLeader();
      playerCharacter.useEffects();
      playerCharacter.cullInactiveEffects();
    } else {
      this.enemy.useEffects();
      this.enemy.cullInactiveEffects();
    }
  }

  @Override
  public final void displayActiveEffects() {
    boolean flag1 = false, flag2 = false, flag3 = false;
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    final String effectString = playerCharacter.getCompleteEffectString();
    final String effectMessages = playerCharacter.getAllCurrentEffectMessages();
    final String enemyEffectMessages = this.enemy.getAllCurrentEffectMessages();
    final String nMsg = Effect.getNullMessage();
    if (!(effectString.equals(nMsg))) {
      flag1 = true;
    }
    if (!(effectMessages.equals(nMsg))) {
      flag2 = true;
    }
    if (!(enemyEffectMessages.equals(nMsg))) {
      flag3 = true;
    }
    if (flag1) {
      this.setStatusMessage(effectString);
    }
    if (flag2) {
      this.setStatusMessage(effectMessages);
    }
    if (flag3) {
      this.setStatusMessage(enemyEffectMessages);
    }
  }

  final void clearMessageArea() {
    this.battleGUI.clearMessageArea();
  }

  @Override
  public final void setStatusMessage(final String s) {
    this.battleGUI.setStatusMessage(s);
  }

  final void stripExtraNewLine() {
    this.battleGUI.stripExtraNewLine();
  }

  final void firstUpdateMessageArea() {
    this.clearMessageArea();
    this.setStatusMessage("*** Beginning of Round ***");
    this.displayBattleStats();
    this.setStatusMessage("*** Beginning of Round ***\n");
    // Determine initiative
    boolean enemyGotJump = false;
    if (this.enemy.getSpeed() > PartyManager.getParty().getLeader()
        .getSpeed()) {
      // Enemy acts first!
      enemyGotJump = true;
    } else if (this.enemy.getSpeed() < PartyManager.getParty().getLeader()
        .getSpeed()) {
      // You act first!
      enemyGotJump = false;
    } else {
      // Equal, decide randomly
      final RandomRange jump = new RandomRange(0, 1);
      final int whoFirst = jump.generate();
      if (whoFirst == 1) {
        // Enemy acts first!
        enemyGotJump = true;
      } else {
        // You act first!
        enemyGotJump = false;
      }
    }
    if (enemyGotJump) {
      this.setStatusMessage("The enemy acts first!");
      this.executeNextAIAction();
      // Display Active Effects
      this.displayActiveEffects();
      // Maintain Effects
      this.maintainEffects(true);
      this.maintainEffects(false);
      // Check result
      this.setResult(this.getResult());
      if (this.result != BattleResults.IN_PROGRESS) {
        this.doResult();
        return;
      }
    } else {
      this.setStatusMessage("You act first!");
    }
    this.setStatusMessage("\n*** End of Round ***");
    this.displayBattleStats();
    this.setStatusMessage("*** End of Round ***");
    this.stripExtraNewLine();
    this.battleGUI.getOutputFrame().pack();
  }

  final void updateMessageAreaEnemyFleeFailed() {
    this.setStatusMessage(
        "The enemy tries to run away, but doesn't quite make it!");
  }

  final void updateMessageAreaPostSteal() {
    this.setStatusMessage("You try to steal money, and successfully steal "
        + this.stealAmount + " Gold!");
  }

  final void updateMessageAreaPostDrain() {
    this.setStatusMessage("You try to drain the enemy, and succeed!");
  }

  final void updateMessageAreaFleeFailed() {
    this.setStatusMessage("You try to run away, but don't quite make it!");
  }

  @Override
  public final boolean steal() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    final int stealChance = StatConstants.CHANCE_STEAL;
    final RandomRange chance = new RandomRange(0, 100);
    final int randomChance = chance.generate();
    if (randomChance <= stealChance) {
      // Succeeded
      final RandomRange stole = new RandomRange(0, this.enemy.getGold());
      this.stealAmount = stole.generate();
      playerCharacter.offsetGold(this.stealAmount);
      return true;
    } else {
      // Failed
      this.stealAmount = 0;
      return false;
    }
  }

  @Override
  public final boolean drain() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    final int drainChance = StatConstants.CHANCE_DRAIN;
    final RandomRange chance = new RandomRange(0, 100);
    final int randomChance = chance.generate();
    if (randomChance <= drainChance) {
      // Succeeded
      final RandomRange drained = new RandomRange(0, this.enemy.getCurrentMP());
      final int drainAmount = drained.generate();
      this.enemy.offsetCurrentMP(-drainAmount);
      playerCharacter.offsetCurrentMP(drainAmount);
      return true;
    } else {
      // Failed
      return false;
    }
  }

  final void updateMessageAreaStealFailed() {
    this.setStatusMessage(
        "You try to steal money from the enemy, but the attempt fails!");
  }

  final void updateMessageAreaDrainFailed() {
    this.setStatusMessage(
        "You try to drain the enemy's MP, but the attempt fails!");
  }

  @Override
  public void resetGUI() {
    // Destroy old GUI
    this.battleGUI.getOutputFrame().dispose();
    // Create new GUI
    this.battleGUI = new WindowTimeBattleGUI();
  }

  @Override
  public void doResult() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    final Creature m = this.enemy;
    boolean rewardsFlag = false;
    if (m instanceof BossMonster) {
      if (this.result == BattleResults.WON
          || this.result == BattleResults.PERFECT) {
        this.setStatusMessage("You defeated the Boss!");
        SoundPlayer.playSound(SoundIndex.VICTORY);
        rewardsFlag = true;
      } else if (this.result == BattleResults.LOST) {
        this.setStatusMessage("The Boss defeated you...");
        SoundPlayer.playSound(SoundIndex.GAME_OVER);
        PartyManager.getParty().getLeader().onDeath(-10);
      } else if (this.result == BattleResults.ANNIHILATED) {
        this.setStatusMessage(
            "The Boss defeated you without suffering damage... you were annihilated!");
        SoundPlayer.playSound(SoundIndex.GAME_OVER);
        PartyManager.getParty().getLeader().onDeath(-20);
      } else if (this.result == BattleResults.DRAW) {
        this.setStatusMessage(
            "The Boss battle was a draw. You are fully healed!");
        playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
        playerCharacter.regeneratePercentage(Creature.FULL_HEAL_PERCENTAGE);
      } else if (this.result == BattleResults.FLED) {
        this.setStatusMessage("You ran away successfully!");
      } else if (this.result == BattleResults.ENEMY_FLED) {
        this.setStatusMessage("The Boss ran away!");
      }
    } else {
      if (this.result == BattleResults.WON) {
        this.setStatusMessage("You gain " + m.getExperience()
            + " experience and " + m.getGold() + " Gold.");
        playerCharacter.offsetExperience(m.getExperience());
        playerCharacter.offsetGold(m.getGold());
        SoundPlayer.playSound(SoundIndex.VICTORY);
      } else if (this.result == BattleResults.PERFECT) {
        this.setStatusMessage("You gain " + m.getExperience()
            + " experience and " + m.getGold() + " Gold,\nplus "
            + m.getPerfectBonusGold() + " extra gold for a perfect fight!");
        playerCharacter.offsetExperience(m.getExperience());
        playerCharacter.offsetGold(m.getGold() + m.getPerfectBonusGold());
        SoundPlayer.playSound(SoundIndex.VICTORY);
      } else if (this.result == BattleResults.LOST) {
        this.setStatusMessage("You lost...");
        SoundPlayer.playSound(SoundIndex.GAME_OVER);
        PartyManager.getParty().getLeader().onDeath(-10);
      } else if (this.result == BattleResults.ANNIHILATED) {
        this.setStatusMessage(
            "You lost without hurting your foe... you were annihilated!");
        SoundPlayer.playSound(SoundIndex.GAME_OVER);
        PartyManager.getParty().getLeader().onDeath(-20);
      } else if (this.result == BattleResults.DRAW) {
        this.setStatusMessage("The battle was a draw. You are fully healed!");
        playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
        playerCharacter.regeneratePercentage(Creature.FULL_HEAL_PERCENTAGE);
      } else if (this.result == BattleResults.FLED) {
        this.setStatusMessage("You ran away successfully!");
      } else if (this.result == BattleResults.ENEMY_FLED) {
        this.setStatusMessage("The enemy runs away!");
        this.setStatusMessage(
            "Since the enemy ran away, you gain nothing for this battle.");
      }
    }
    // Cleanup
    this.battleGUI.doResultCleanup();
    playerCharacter.stripAllEffects();
    this.enemy.stripAllEffects();
    // Level Up Check
    if (playerCharacter.checkLevelUp()) {
      playerCharacter.levelUp();
      if (FantastleReboot.getBagOStuff().getPrefsManager()
          .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
        SoundPlayer.playSound(SoundIndex.LEVEL_UP);
      }
      this.setStatusMessage(
          "You reached level " + playerCharacter.getLevel() + ".");
    }
    // Final Cleanup
    this.battleGUI.doResultFinalCleanup(rewardsFlag);
  }

  @Override
  public final void setResult(final BattleResults newResult) {
    this.result = newResult;
  }

  @Override
  public final void battleDone() {
    BagOStuff bag = FantastleReboot.getBagOStuff();
    // Leave Battle
    this.battleGUI.getOutputFrame().setVisible(false);
    // Post-battle stuff
    Maze m = bag.getMazeManager().getMaze();
    m.postBattle(m.getPlayerLocationX(), m.getPlayerLocationY());
    // Return to whence we came
    bag.getGameManager().showOutput();
    bag.getGameManager().redrawMaze();
  }

  @Override
  public boolean getLastAIActionResult() {
    return true;
  }

  @Override
  public boolean castSpell() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    return SpellCaster.selectAndCastSpell(playerCharacter);
  }

  @Override
  public boolean useItem() {
    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
    return CombatItemChucker.selectAndUseItem(playerCharacter);
  }

  @Override
  public void endTurn() {
    // Do nothing
  }

  @Override
  public Creature getEnemy() {
    return this.enemy;
  }

  @Override
  public boolean updatePosition(final int x, final int y) {
    return false;
  }

  @Override
  public void fireArrow(final int x, final int y) {
    // Do nothing
  }

  @Override
  public void arrowDone(final BattleCharacter hit) {
    // Do nothing
  }

  @Override
  public void redrawOneBattleSquare(final int x, final int y,
      final FantastleObjectModel obj3) {
    // Do nothing
  }

  @Override
  public boolean isWaitingForAI() {
    return false;
  }

  private class PlayerTask extends TimerTask {
    public PlayerTask() {
      // Do nothing
    }

    @Override
    public void run() {
      try {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        final Battle b = app.getBattle();
        if (bag.getMode() == BagOStuff.STATUS_BATTLE
            && b instanceof WindowTimeBattleLogic) {
          final WindowTimeBattleLogic logic = WindowTimeBattleLogic.this;
          final WindowTimeBattleGUI gui = logic.battleGUI;
          if (!gui.isPlayerActionBarFull()) {
            gui.disableActionButtons();
            gui.updatePlayerActionBarValue();
            if (gui.isPlayerActionBarFull()) {
              SoundPlayer.playSound(SoundIndex.PLAYER_UP);
              gui.enableActionButtons();
            }
          } else {
            gui.enableActionButtons();
          }
        }
      } catch (final Throwable t) {
        FantastleReboot.logError(t);
      }
    }
  }

  private class EnemyTask extends TimerTask {
    public EnemyTask() {
      // Do nothing
    }

    @Override
    public void run() {
      try {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        final Battle b = app.getBattle();
        if (bag.getMode() == BagOStuff.STATUS_BATTLE
            && b instanceof WindowTimeBattleLogic) {
          final WindowTimeBattleLogic logic = WindowTimeBattleLogic.this;
          final WindowTimeBattleGUI gui = logic.battleGUI;
          if (!gui.isEnemyActionBarFull()) {
            gui.updateEnemyActionBarValue();
            if (gui.isEnemyActionBarFull()) {
              this.enemyAct();
            }
          }
        }
      } catch (final Throwable t) {
        FantastleReboot.logError(t);
      }
    }

    private void enemyAct() {
      final WindowTimeBattleLogic logic = WindowTimeBattleLogic.this;
      final WindowTimeBattleGUI gui = logic.battleGUI;
      // Do Enemy Actions
      logic.executeNextAIAction();
      gui.resetEnemyActionBar();
      // Maintain Enemy Effects
      logic.maintainEffects(false);
      // Display Active Effects
      logic.displayActiveEffects();
      // Display End Stats
      logic.displayBattleStats();
      // Check Result
      final BattleResults bResult = logic.getResult();
      if (bResult != BattleResults.IN_PROGRESS) {
        logic.setResult(bResult);
        logic.doResult();
      } else {
        // Strip Extra Newline Character
        gui.stripExtraNewLine();
        // Pack Battle Frame
        gui.battleFrame.pack();
      }
    }
  }

  @Override
  public boolean arrowHitCheck(int inX, int inY) {
    // Meaningless in Window Battles
    return false;
  }
}
