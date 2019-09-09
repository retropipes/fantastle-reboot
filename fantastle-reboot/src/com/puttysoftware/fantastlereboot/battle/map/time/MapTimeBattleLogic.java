/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map.time;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.ai.map.AbstractMapAIRoutine;
import com.puttysoftware.fantastlereboot.ai.map.AutoMapAI;
import com.puttysoftware.fantastlereboot.ai.map.MapAIContext;
import com.puttysoftware.fantastlereboot.assets.GameSound;
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
import com.puttysoftware.fantastlereboot.items.combat.CombatItemChucker;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.spells.Spell;
import com.puttysoftware.fantastlereboot.spells.SpellCaster;
import com.puttysoftware.fantastlereboot.ttmain.Application;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;
import com.puttysoftware.fantastlereboot.ttmaze.Maze;
import com.puttysoftware.fantastlereboot.ttmaze.MazeConstants;
import com.puttysoftware.fantastlereboot.ttmaze.abc.AbstractMazeObject;
import com.puttysoftware.fantastlereboot.ttmaze.objects.BattleCharacter;
import com.puttysoftware.fantastlereboot.ttmaze.objects.Empty;
import com.puttysoftware.randomrange.RandomRange;

public class MapTimeBattleLogic extends Battle {
    // Fields
    private Maze battleMaze;
    private AbstractDamageEngine pde;
    private AbstractDamageEngine ede;
    private final AutoMapAI auto;
    private int damage;
    private BattleResults result;
    private long battleExp;
    private boolean resultDoneAlready;
    private boolean lastAIActionResult;
    MapTimeBattleGUI battleGUI;
    private BattleCharacter me;
    private BattleCharacter enemy;
    private MapAIContext myContext;
    private MapAIContext enemyContext;
    private final Timer battleTimer;

    // Constructors
    public MapTimeBattleLogic() {
        this.battleGUI = new MapTimeBattleGUI();
        this.auto = new AutoMapAI();
        this.battleTimer = new Timer();
        this.battleTimer.schedule(new PlayerTask(), 0,
                MapTimeBattleSpeed.getSpeed());
        this.battleTimer.schedule(new EnemyTask(), 0,
                MapTimeBattleSpeed.getSpeed());
    }

    // Methods
    @Override
    public JFrame getOutputFrame() {
        return this.battleGUI.getOutputFrame();
    }

    @Override
    public void doBattle() {
        final Maze m = Maze.getTemporaryBattleCopy();
        final MapBattle b = new MapBattle();
        this.doBattleInternal(m, b);
    }

    @Override
    public void doBattleByProxy() {
        final Creature m = MonsterFactory.getNewMonsterInstance();
        final PartyMember playerCharacter = PartyManager.getParty().getLeader();
        playerCharacter.offsetExperience(m.getExperience());
        playerCharacter.offsetGold(m.getGold());
        // Level Up Check
        if (playerCharacter.checkLevelUp()) {
            playerCharacter.levelUp();
            TallerTower.getApplication().getGameManager().keepNextMessage();
            TallerTower.getApplication().showMessage(
                    "You reached level " + playerCharacter.getLevel() + ".");
        }
    }

    private void doBattleInternal(final Maze bMaze, final MapBattle b) {
        // Initialize Battle
        TallerTower.getApplication().getGameManager().hideOutput();
        TallerTower.getApplication().setMode(Application.STATUS_BATTLE);
        this.battleMaze = bMaze;
        this.pde = AbstractDamageEngine.getPlayerInstance();
        this.ede = AbstractDamageEngine.getEnemyInstance();
        this.resultDoneAlready = false;
        this.result = BattleResults.IN_PROGRESS;
        // Generate Friends
        this.me = PartyManager.getParty().getBattleCharacters();
        // Generate Enemies
        this.enemy = b.getBattlers();
        this.enemy.getTemplate().healAndRegenerateFully();
        this.enemy.getTemplate().loadCreature();
        // Create AI Contexts
        this.myContext = new MapAIContext(this.me, bMaze);
        this.enemyContext = new MapAIContext(this.enemy, bMaze);
        // Reset
        this.me.resetAll();
        this.enemy.resetAll();
        // Set Action Bars
        this.battleGUI.setMaxPlayerActionBarValue(
                PartyManager.getParty().getLeader().getActionBarSpeed());
        this.battleGUI.setMaxEnemyActionBarValue(
                this.enemy.getTemplate().getActionBarSpeed());
        // Set Character Locations
        this.setCharacterLocations();
        // Clear status message
        this.clearStatusMessage();
        // Start Battle
        this.battleGUI.getViewManager().setViewingWindowCenterX(this.me.getY());
        this.battleGUI.getViewManager().setViewingWindowCenterY(this.me.getX());
        SoundLoader.playSound(GameSound.DRAW_SWORD);
        this.showBattle();
        this.updateStatsAndEffects();
        this.redrawBattle();
    }

    @Override
    public void battleDone() {
        // Leave Battle
        this.hideBattle();
        TallerTower.getApplication().setMode(Application.STATUS_GAME);
        // Return to whence we came
        TallerTower.getApplication().getGameManager().showOutput();
        TallerTower.getApplication().getGameManager().redrawMaze();
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
                && !this.areTeamEnemiesDeadOrGone(
                        Creature.TEAM_PARTY)) {
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
        if (this.enemy != null && this.enemy.getTemplate() != null
                && this.enemy.getTemplate().getMapAI() != null) {
            final BattleCharacter active = this.enemy;
            if (active.getTemplate().isAlive()) {
                final int action = active.getTemplate().getMapAI()
                        .getNextAction(this.enemyContext);
                switch (action) {
                case AbstractMapAIRoutine.ACTION_MOVE:
                    final int x = active.getTemplate().getMapAI().getMoveX();
                    final int y = active.getTemplate().getMapAI().getMoveY();
                    this.lastAIActionResult = this.updatePositionInternal(x, y,
                            this.enemy, this.me, this.ede, this.enemyContext,
                            false);
                    active.getTemplate().getMapAI()
                            .setLastResult(this.lastAIActionResult);
                    break;
                case AbstractMapAIRoutine.ACTION_CAST_SPELL:
                    this.lastAIActionResult = this.castEnemySpell();
                    active.getTemplate().getMapAI()
                            .setLastResult(this.lastAIActionResult);
                    break;
                case AbstractMapAIRoutine.ACTION_DRAIN:
                    this.lastAIActionResult = this.enemyDrain();
                    active.getTemplate().getMapAI()
                            .setLastResult(this.lastAIActionResult);
                    break;
                case AbstractMapAIRoutine.ACTION_STEAL:
                    this.lastAIActionResult = this.enemySteal();
                    active.getTemplate().getMapAI()
                            .setLastResult(this.lastAIActionResult);
                    break;
                default:
                    this.lastAIActionResult = true;
                    break;
                }
            }
        }
    }

    @Override
    public boolean getLastAIActionResult() {
        return this.lastAIActionResult;
    }

    private void executeAutoAI(final BattleCharacter acting,
            final BattleCharacter theEnemy, final MapAIContext theContext) {
        final int action = this.auto.getNextAction(theContext);
        switch (action) {
        case AbstractMapAIRoutine.ACTION_MOVE:
            final int x = this.auto.getMoveX();
            final int y = this.auto.getMoveY();
            final int activeTID = acting.getTeamID();
            final AbstractDamageEngine activeDE = (activeTID == Creature.TEAM_PARTY
                    ? this.ede
                    : this.pde);
            this.updatePositionInternal(x, y, acting, theEnemy, activeDE,
                    theContext, false);
            break;
        default:
            break;
        }
    }

    private void displayRoundResults(final Creature theEnemy,
            final Creature active,
            final AbstractDamageEngine activeDE) {
        // Display round results
        final boolean isParty = active
                .getTeamID() == Creature.TEAM_PARTY;
        final String activeName = active.getName();
        final String enemyName = theEnemy.getName();
        String damageString = Integer.toString(this.damage);
        String displayDamageString = " ";
        if (this.damage == 0) {
            if (activeDE.weaponMissed()) {
                displayDamageString = activeName + " tries to hit " + enemyName
                        + ", but MISSES!";
                SoundLoader.playSound(GameSound.MISSED);
            } else if (activeDE.enemyDodged()) {
                displayDamageString = activeName + " tries to hit " + enemyName
                        + ", but " + enemyName + " AVOIDS the attack!";
                SoundLoader.playSound(GameSound.MISSED);
            } else {
                displayDamageString = activeName + " tries to hit " + enemyName
                        + ", but the attack is BLOCKED!";
                SoundLoader.playSound(GameSound.MISSED);
            }
        } else if (this.damage < 0) {
            damageString = Integer.toString(-this.damage);
            String displayDamagePrefix = "";
            if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
                displayDamagePrefix = "PIERCING CRITICAL HIT! ";
                if (isParty) {
                    SoundLoader.playSound(GameSound.PARTY_COUNTER);
                } else {
                    SoundLoader.playSound(GameSound.MONSTER_COUNTER);
                }
                SoundLoader.playSound(GameSound.CRITICAL);
            } else if (activeDE.weaponCrit()) {
                displayDamagePrefix = "CRITICAL HIT! ";
                SoundLoader.playSound(GameSound.CRITICAL);
            } else if (activeDE.weaponPierce()) {
                displayDamagePrefix = "PIERCING HIT! ";
                if (isParty) {
                    SoundLoader.playSound(GameSound.PARTY_COUNTER);
                } else {
                    SoundLoader.playSound(GameSound.MONSTER_COUNTER);
                }
            }
            displayDamageString = displayDamagePrefix + activeName
                    + " tries to hit " + enemyName + ", but " + enemyName
                    + " RIPOSTES for " + damageString + " damage!";
            if (isParty) {
                SoundLoader.playSound(GameSound.PARTY_COUNTER);
            } else {
                SoundLoader.playSound(GameSound.MONSTER_COUNTER);
            }
        } else {
            String displayDamagePrefix = "";
            if (activeDE.weaponFumble()) {
                SoundLoader.playSound(GameSound.FUMBLE);
                displayDamageString = "FUMBLE! " + activeName
                        + " drops their weapon on themselves, doing "
                        + damageString + " damage!";
            } else {
                if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
                    displayDamagePrefix = "PIERCING CRITICAL HIT! ";
                    if (isParty) {
                        SoundLoader.playSound(GameSound.PARTY_COUNTER);
                    } else {
                        SoundLoader.playSound(GameSound.MONSTER_COUNTER);
                    }
                    SoundLoader.playSound(GameSound.CRITICAL);
                } else if (activeDE.weaponCrit()) {
                    displayDamagePrefix = "CRITICAL HIT! ";
                    SoundLoader.playSound(GameSound.CRITICAL);
                } else if (activeDE.weaponPierce()) {
                    displayDamagePrefix = "PIERCING HIT! ";
                    if (isParty) {
                        SoundLoader.playSound(GameSound.PARTY_COUNTER);
                    } else {
                        SoundLoader.playSound(GameSound.MONSTER_COUNTER);
                    }
                }
                displayDamageString = displayDamagePrefix + activeName
                        + " hits " + enemyName + " for " + damageString
                        + " damage!";
                if (isParty) {
                    SoundLoader.playSound(GameSound.PARTY_HIT);
                } else {
                    SoundLoader.playSound(GameSound.MONSTER_HIT);
                }
            }
        }
        this.setStatusMessage(displayDamageString);
    }

    private void computeDamage(final Creature theEnemy,
            final Creature acting,
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

    private void setCharacterLocations() {
        final RandomRange randX = new RandomRange(0,
                this.battleMaze.getRows() - 1);
        final RandomRange randY = new RandomRange(0,
                this.battleMaze.getColumns() - 1);
        int rx, ry;
        // Set Player Location
        if (this.me != null) {
            if (this.me.isActive() && this.me.getTemplate().getX() == -1
                    && this.me.getTemplate().getY() == -1) {
                rx = randX.generate();
                ry = randY.generate();
                AbstractMazeObject obj = this.battleMaze.getCell(rx, ry, 0,
                        MazeConstants.LAYER_OBJECT);
                while (obj.isSolidInBattle()) {
                    rx = randX.generate();
                    ry = randY.generate();
                    obj = this.battleMaze.getCell(rx, ry, 0,
                            MazeConstants.LAYER_OBJECT);
                }
                this.me.setX(rx);
                this.me.setY(ry);
                this.battleMaze.setCell(this.me, rx, ry, 0,
                        MazeConstants.LAYER_OBJECT);
            }
        }
        // Set Enemy Location
        if (this.enemy != null) {
            if (this.enemy.isActive() && this.enemy.getTemplate().getX() == -1
                    && this.enemy.getTemplate().getY() == -1) {
                rx = randX.generate();
                ry = randY.generate();
                AbstractMazeObject obj = this.battleMaze.getCell(rx, ry, 0,
                        MazeConstants.LAYER_OBJECT);
                while (obj.isSolidInBattle()) {
                    rx = randX.generate();
                    ry = randY.generate();
                    obj = this.battleMaze.getCell(rx, ry, 0,
                            MazeConstants.LAYER_OBJECT);
                }
                this.enemy.setX(rx);
                this.enemy.setY(ry);
                this.battleMaze.setCell(this.enemy, rx, ry, 0,
                        MazeConstants.LAYER_OBJECT);
            }
        }
    }

    private int getGold() {
        return this.enemy.getTemplate().getGold();
    }

    private boolean isTeamAlive(final int teamID) {
        if (teamID == Creature.TEAM_PARTY) {
            return this.me.getTemplate().isAlive();
        } else {
            return this.enemy.getTemplate().isAlive();
        }
    }

    private boolean areTeamEnemiesAlive(final int teamID) {
        if (teamID == Creature.TEAM_PARTY) {
            return this.enemy.getTemplate().isAlive();
        } else {
            return this.me.getTemplate().isAlive();
        }
    }

    private boolean areTeamEnemiesDeadOrGone(final int teamID) {
        if (teamID == Creature.TEAM_PARTY) {
            int deadCount = 0;
            if (this.enemy != null) {
                final boolean res = this.enemy.getTemplate().isAlive()
                        && this.enemy.isActive();
                if (res) {
                    return false;
                }
                if (!this.enemy.getTemplate().isAlive()) {
                    deadCount++;
                }
            }
            return (deadCount > 0);
        } else {
            int deadCount = 0;
            if (this.me != null) {
                final boolean res = this.me.getTemplate().isAlive()
                        && this.me.isActive();
                if (res) {
                    return false;
                }
                if (!this.me.getTemplate().isAlive()) {
                    deadCount++;
                }
            }
            return (deadCount > 0);
        }
    }

    private boolean areTeamEnemiesGone(final int teamID) {
        if (teamID == Creature.TEAM_PARTY) {
            boolean res = true;
            if (this.enemy != null) {
                if (this.enemy.getTemplate().isAlive()) {
                    res = res && !this.enemy.isActive();
                    if (!res) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            boolean res = true;
            if (this.me != null) {
                if (this.me.getTemplate().isAlive()) {
                    res = res && !this.me.isActive();
                    if (!res) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isTeamGone(final int teamID) {
        if (teamID == Creature.TEAM_PARTY) {
            boolean res = true;
            if (this.me != null) {
                if (this.me.getTemplate().isAlive()) {
                    res = res && !this.me.isActive();
                    if (!res) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            boolean res = true;
            if (this.enemy != null) {
                if (this.enemy.getTemplate().isAlive()) {
                    res = res && !this.enemy.isActive();
                    if (!res) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public boolean updatePosition(final int x, final int y) {
        BattleCharacter theEnemy = this.enemy;
        final AbstractDamageEngine activeDE = this.pde;
        if (x == 0 && y == 0) {
            theEnemy = this.me;
        }
        return this.updatePositionInternal(x, y, this.me, theEnemy, activeDE,
                this.myContext, true);
    }

    @Override
    public void fireArrow(final int x, final int y) {
        // Fire arrow
        this.updateStatsAndEffects();
        this.battleGUI.turnEventHandlersOff();
        final MapBattleArrowTask at = new MapBattleArrowTask(x, y,
                this.battleMaze, this.me);
        at.start();
    }

    @Override
    public void arrowDone(final BattleCharacter hit) {
        this.battleGUI.turnEventHandlersOn();
        // Handle death
        if (hit != null && !hit.getTemplate().isAlive()) {
            if (hit.getTeamID() != Creature.TEAM_PARTY) {
                // Update victory spoils
                this.battleExp = hit.getTemplate().getExperience();
            }
            // Remove effects from dead character
            hit.getTemplate().stripAllEffects();
            // Set dead character to inactive
            hit.deactivate();
            // Remove character from battle
            this.battleMaze.setCell(new Empty(), hit.getX(), hit.getY(), 0,
                    MazeConstants.LAYER_OBJECT);
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
            final BattleCharacter active, final BattleCharacter theEnemy,
            final AbstractDamageEngine activeDE,
            final MapAIContext activeContext, boolean updateView) {
        this.updateAllAIContexts();
        int px = active.getX();
        int py = active.getY();
        final Maze m = this.battleMaze;
        AbstractMazeObject next = null;
        AbstractMazeObject nextGround = null;
        AbstractMazeObject currGround = null;
        active.saveLocation();
        if (updateView) {
            this.battleGUI.getViewManager().saveViewingWindow();
        }
        try {
            next = m.getCell(px + x, py + y, 0, MazeConstants.LAYER_OBJECT);
            nextGround = m.getCell(px + x, py + y, 0,
                    MazeConstants.LAYER_GROUND);
            currGround = m.getCell(px, py, 0, MazeConstants.LAYER_GROUND);
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Ignore
        }
        if (next != null && nextGround != null && currGround != null) {
            if (!next.isSolidInBattle()) {
                // Move
                AbstractMazeObject obj1 = null;
                AbstractMazeObject obj2 = null;
                AbstractMazeObject obj3 = null;
                AbstractMazeObject obj4 = null;
                AbstractMazeObject obj6 = null;
                AbstractMazeObject obj7 = null;
                AbstractMazeObject obj8 = null;
                AbstractMazeObject obj9 = null;
                try {
                    obj1 = m.getCell(px - 1, py - 1, 0,
                            MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
                try {
                    obj2 = m.getCell(px, py - 1, 0, MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
                try {
                    obj3 = m.getCell(px + 1, py - 1, 0,
                            MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
                try {
                    obj4 = m.getCell(px - 1, py, 0, MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
                try {
                    obj6 = m.getCell(px + 1, py - 1, 0,
                            MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
                try {
                    obj7 = m.getCell(px - 1, py + 1, 0,
                            MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
                try {
                    obj8 = m.getCell(px, py + 1, 0, MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
                try {
                    obj9 = m.getCell(px + 1, py + 1, 0,
                            MazeConstants.LAYER_OBJECT);
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
                                this.executeAutoAI(bc1, active, activeContext);
                            }
                        }
                    }
                }
                if (obj2 != null) {
                    if (obj2 instanceof BattleCharacter) {
                        if (y == 1) {
                            final BattleCharacter bc2 = (BattleCharacter) obj2;
                            if (bc2.getTeamID() != active.getTeamID()) {
                                this.executeAutoAI(bc2, active, activeContext);
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
                                this.executeAutoAI(bc3, active, activeContext);
                            }
                        }
                    }
                }
                if (obj4 != null) {
                    if (obj4 instanceof BattleCharacter) {
                        if (x == 1) {
                            final BattleCharacter bc4 = (BattleCharacter) obj4;
                            if (bc4.getTeamID() != active.getTeamID()) {
                                this.executeAutoAI(bc4, active, activeContext);
                            }
                        }
                    }
                }
                if (obj6 != null) {
                    if (obj6 instanceof BattleCharacter) {
                        if (x == -1) {
                            final BattleCharacter bc6 = (BattleCharacter) obj6;
                            if (bc6.getTeamID() != active.getTeamID()) {
                                this.executeAutoAI(bc6, active, activeContext);
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
                                this.executeAutoAI(bc7, active, activeContext);
                            }
                        }
                    }
                }
                if (obj8 != null) {
                    if (obj8 instanceof BattleCharacter) {
                        if (y == -1) {
                            final BattleCharacter bc8 = (BattleCharacter) obj8;
                            if (bc8.getTeamID() != active.getTeamID()) {
                                this.executeAutoAI(bc8, active, activeContext);
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
                                this.executeAutoAI(bc9, active, activeContext);
                            }
                        }
                    }
                }
                m.setCell(active.getSavedObject(), px, py, 0,
                        MazeConstants.LAYER_OBJECT);
                active.offsetX(x);
                active.offsetY(y);
                px += x;
                py += y;
                if (updateView) {
                    this.battleGUI.getViewManager()
                            .offsetViewingWindowLocationX(y);
                    this.battleGUI.getViewManager()
                            .offsetViewingWindowLocationY(x);
                }
                active.setSavedObject(
                        m.getCell(px, py, 0, MazeConstants.LAYER_OBJECT));
                m.setCell(active, px, py, 0, MazeConstants.LAYER_OBJECT);
                SoundLoader.playSound(GameSound.WALK);
            } else {
                if (next instanceof BattleCharacter) {
                    // Attack
                    final BattleCharacter bc = (BattleCharacter) next;
                    if (bc.getTeamID() == active.getTeamID()) {
                        // Attack Friend?
                        if (!active.getTemplate().hasMapAI()) {
                            final int confirm = CommonDialogs.showConfirmDialog(
                                    "Attack Friend?", "Battle");
                            if (confirm != JOptionPane.YES_OPTION) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                    // Do damage
                    this.computeDamage(theEnemy.getTemplate(),
                            active.getTemplate(), activeDE);
                    // Handle low health for party members
                    if (theEnemy.getTemplate().isAlive()
                            && theEnemy
                                    .getTeamID() == Creature.TEAM_PARTY
                            && theEnemy.getTemplate().getCurrentHP() <= theEnemy
                                    .getTemplate().getMaximumHP() * 3 / 10) {
                        SoundLoader.playSound(GameSound.LOW_HEALTH);
                    }
                    // Handle enemy death
                    if (!theEnemy.getTemplate().isAlive()) {
                        if (theEnemy
                                .getTeamID() != Creature.TEAM_PARTY) {
                            // Update victory spoils
                            this.battleExp = theEnemy.getTemplate()
                                    .getExperience();
                        }
                        // Remove effects from dead character
                        bc.getTemplate().stripAllEffects();
                        // Set dead character to inactive
                        bc.deactivate();
                        // Remove character from battle
                        m.setCell(new Empty(), bc.getX(), bc.getY(), 0,
                                MazeConstants.LAYER_OBJECT);
                    }
                    // Handle self death
                    if (!active.getTemplate().isAlive()) {
                        // Remove effects from dead character
                        active.getTemplate().stripAllEffects();
                        // Set dead character to inactive
                        active.deactivate();
                        // Remove character from battle
                        m.setCell(new Empty(), active.getX(), active.getY(), 0,
                                MazeConstants.LAYER_OBJECT);
                    }
                } else {
                    // Move Failed
                    if (!active.getTemplate().hasMapAI()) {
                        this.setStatusMessage("Can't go that way");
                    }
                    return false;
                }
            }
        } else {
            // Confirm Flee
            if (!active.getTemplate().hasMapAI()) {
                SoundLoader.playSound(GameSound.SPECIAL);
                final int confirm = CommonDialogs
                        .showConfirmDialog("Embrace Cowardice?", "Battle");
                if (confirm != JOptionPane.YES_OPTION) {
                    if (updateView) {
                        this.battleGUI.getViewManager().restoreViewingWindow();
                    }
                    active.restoreLocation();
                    return false;
                }
            }
            // Flee
            if (updateView) {
                this.battleGUI.getViewManager().restoreViewingWindow();
            }
            active.restoreLocation();
            // Set fled character to inactive
            active.deactivate();
            // Remove character from battle
            m.setCell(new Empty(), active.getX(), active.getY(), 0,
                    MazeConstants.LAYER_OBJECT);
            // End Turn
            this.endTurn();
            this.updateStatsAndEffects();
            final BattleResults currResult = this.getResult();
            if (currResult != BattleResults.IN_PROGRESS) {
                // Battle Done
                this.result = currResult;
                this.doResult();
            }
            if (updateView) {
                this.battleGUI.getViewManager().setViewingWindowCenterX(py);
                this.battleGUI.getViewManager().setViewingWindowCenterY(px);
            }
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
        if (updateView) {
            this.battleGUI.getViewManager().setViewingWindowCenterX(py);
            this.battleGUI.getViewManager().setViewingWindowCenterY(px);
        }
        this.redrawBattle();
        return true;
    }

    @Override
    public Creature getEnemy() {
        return this.enemy.getTemplate();
    }

    private BattleCharacter getEnemyBC(final BattleCharacter acting) {
        final int px = acting.getX();
        final int py = acting.getY();
        final Maze m = this.battleMaze;
        AbstractMazeObject next = null;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                try {
                    next = m.getCell(px + x, py + y, 0,
                            MazeConstants.LAYER_OBJECT);
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // Ignore
                }
                if (next != null) {
                    if (next.isSolidInBattle()) {
                        if (next instanceof BattleCharacter) {
                            return (BattleCharacter) next;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void showBattle() {
        this.battleGUI.showBattle();
    }

    private void hideBattle() {
        this.battleGUI.hideBattle();
    }

    @Override
    public boolean castSpell() {
        final boolean success = SpellCaster
                .selectAndCastSpell(this.me.getTemplate());
        final BattleResults currResult = this.getResult();
        if (currResult != BattleResults.IN_PROGRESS) {
            // Battle Done
            this.result = currResult;
            this.doResult();
        }
        return success;
    }

    private boolean castEnemySpell() {
        // Active character has AI, and AI is turned on
        final Spell sp = this.enemy.getTemplate().getMapAI().getSpellToCast();
        final boolean success = SpellCaster.castSpell(sp,
                this.enemy.getTemplate());
        final BattleResults currResult = this.getResult();
        if (currResult != BattleResults.IN_PROGRESS) {
            // Battle Done
            this.result = currResult;
            this.doResult();
        }
        return success;
    }

    @Override
    public boolean useItem() {
        // Active character has no AI, or AI is turned off
        final boolean success = CombatItemChucker
                .selectAndUseItem(this.me.getTemplate());
        final BattleResults currResult = this.getResult();
        if (currResult != BattleResults.IN_PROGRESS) {
            // Battle Done
            this.result = currResult;
            this.doResult();
        }
        return success;
    }

    @Override
    public boolean steal() {
        Creature activeEnemy = null;
        try {
            activeEnemy = this.getEnemyBC(this.me).getTemplate();
        } catch (final NullPointerException npe) {
            // Ignore
        }
        int stealChance;
        int stealAmount = 0;
        stealChance = StatConstants.CHANCE_STEAL;
        if (activeEnemy == null) {
            // Failed - nobody to steal from
            this.setStatusMessage(this.me.getName()
                    + " tries to steal, but nobody is there to steal from!");
            return false;
        }
        if (stealChance <= 0) {
            // Failed
            this.setStatusMessage(
                    this.me.getName() + " tries to steal, but fails!");
            return false;
        } else if (stealChance >= 100) {
            // Succeeded, unless target has 0 Gold
            final RandomRange stole = new RandomRange(0, activeEnemy.getGold());
            stealAmount = stole.generate();
            if (stealAmount == 0) {
                this.setStatusMessage(this.me.getName()
                        + " tries to steal, but no Gold is left to steal!");
                return false;
            } else {
                this.me.getTemplate().offsetGold(stealAmount);
                this.setStatusMessage(this.me.getName()
                        + " tries to steal, and successfully steals "
                        + stealAmount + " gold!");
                return true;
            }
        } else {
            final RandomRange chance = new RandomRange(0, 100);
            final int randomChance = chance.generate();
            if (randomChance <= stealChance) {
                // Succeeded, unless target has 0 Gold
                final RandomRange stole = new RandomRange(0,
                        activeEnemy.getGold());
                stealAmount = stole.generate();
                if (stealAmount == 0) {
                    this.setStatusMessage(this.me.getName()
                            + " tries to steal, but no Gold is left to steal!");
                    return false;
                } else {
                    this.me.getTemplate().offsetGold(stealAmount);
                    this.setStatusMessage(this.me.getName()
                            + " tries to steal, and successfully steals "
                            + stealAmount + " gold!");
                    return true;
                }
            } else {
                // Failed
                this.setStatusMessage(
                        this.me.getName() + " tries to steal, but fails!");
                return false;
            }
        }
    }

    private boolean enemySteal() {
        Creature activeEnemy = null;
        try {
            activeEnemy = this.getEnemyBC(this.enemy).getTemplate();
        } catch (final NullPointerException npe) {
            // Ignore
        }
        int stealChance;
        int stealAmount = 0;
        stealChance = StatConstants.CHANCE_STEAL;
        if (activeEnemy == null) {
            // Failed - nobody to steal from
            this.setStatusMessage(this.enemy.getName()
                    + " tries to steal, but nobody is there to steal from!");
            return false;
        }
        if (stealChance <= 0) {
            // Failed
            this.setStatusMessage(
                    this.enemy.getName() + " tries to steal, but fails!");
            return false;
        } else if (stealChance >= 100) {
            // Succeeded, unless target has 0 Gold
            final RandomRange stole = new RandomRange(0, activeEnemy.getGold());
            stealAmount = stole.generate();
            if (stealAmount == 0) {
                this.setStatusMessage(this.enemy.getName()
                        + " tries to steal, but no Gold is left to steal!");
                return false;
            } else {
                this.enemy.getTemplate().offsetGold(stealAmount);
                this.setStatusMessage(this.enemy.getName()
                        + " tries to steal, and successfully steals "
                        + stealAmount + " gold!");
                return true;
            }
        } else {
            final RandomRange chance = new RandomRange(0, 100);
            final int randomChance = chance.generate();
            if (randomChance <= stealChance) {
                // Succeeded, unless target has 0 Gold
                final RandomRange stole = new RandomRange(0,
                        activeEnemy.getGold());
                stealAmount = stole.generate();
                if (stealAmount == 0) {
                    this.setStatusMessage(this.enemy.getName()
                            + " tries to steal, but no Gold is left to steal!");
                    return false;
                } else {
                    this.enemy.getTemplate().offsetGold(stealAmount);
                    this.setStatusMessage(this.enemy.getName()
                            + " tries to steal, and successfully steals "
                            + stealAmount + " gold!");
                    return true;
                }
            } else {
                // Failed
                this.setStatusMessage(
                        this.enemy.getName() + " tries to steal, but fails!");
                return false;
            }
        }
    }

    @Override
    public boolean drain() {
        Creature activeEnemy = null;
        try {
            activeEnemy = this.getEnemyBC(this.me).getTemplate();
        } catch (final NullPointerException npe) {
            // Ignore
        }
        int drainChance;
        int drainAmount = 0;
        drainChance = StatConstants.CHANCE_DRAIN;
        if (activeEnemy == null) {
            // Failed - nobody to drain from
            this.setStatusMessage(this.me.getName()
                    + " tries to drain, but nobody is there to drain from!");
            return false;
        }
        if (drainChance <= 0) {
            // Failed
            this.setStatusMessage(
                    this.me.getName() + " tries to drain, but fails!");
            return false;
        } else if (drainChance >= 100) {
            // Succeeded, unless target has 0 MP
            final RandomRange drained = new RandomRange(0,
                    activeEnemy.getCurrentMP());
            drainAmount = drained.generate();
            if (drainAmount == 0) {
                this.setStatusMessage(this.me.getName()
                        + " tries to drain, but no MP is left to drain!");
                return false;
            } else {
                activeEnemy.offsetCurrentMP(-drainAmount);
                this.me.getTemplate().offsetCurrentMP(drainAmount);
                this.setStatusMessage(this.me.getName()
                        + " tries to drain, and successfully drains "
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
                    this.setStatusMessage(this.me.getName()
                            + " tries to drain, but no MP is left to drain!");
                    return false;
                } else {
                    activeEnemy.offsetCurrentMP(-drainAmount);
                    this.me.getTemplate().offsetCurrentMP(drainAmount);
                    this.setStatusMessage(this.me.getName()
                            + " tries to drain, and successfully drains "
                            + drainAmount + " MP!");
                    return true;
                }
            } else {
                // Failed
                this.setStatusMessage(
                        this.me.getName() + " tries to drain, but fails!");
                return false;
            }
        }
    }

    private boolean enemyDrain() {
        Creature activeEnemy = null;
        try {
            activeEnemy = this.getEnemyBC(this.enemy).getTemplate();
        } catch (final NullPointerException npe) {
            // Ignore
        }
        int drainChance;
        int drainAmount = 0;
        drainChance = StatConstants.CHANCE_DRAIN;
        if (activeEnemy == null) {
            // Failed - nobody to drain from
            this.setStatusMessage(this.enemy.getName()
                    + " tries to drain, but nobody is there to drain from!");
            return false;
        }
        if (drainChance <= 0) {
            // Failed
            this.setStatusMessage(
                    this.enemy.getName() + " tries to drain, but fails!");
            return false;
        } else if (drainChance >= 100) {
            // Succeeded, unless target has 0 MP
            final RandomRange drained = new RandomRange(0,
                    activeEnemy.getCurrentMP());
            drainAmount = drained.generate();
            if (drainAmount == 0) {
                this.setStatusMessage(this.enemy.getName()
                        + " tries to drain, but no MP is left to drain!");
                return false;
            } else {
                activeEnemy.offsetCurrentMP(-drainAmount);
                this.enemy.getTemplate().offsetCurrentMP(drainAmount);
                this.setStatusMessage(this.enemy.getName()
                        + " tries to drain, and successfully drains "
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
                    this.setStatusMessage(this.enemy.getName()
                            + " tries to drain, but no MP is left to drain!");
                    return false;
                } else {
                    activeEnemy.offsetCurrentMP(-drainAmount);
                    this.enemy.getTemplate().offsetCurrentMP(drainAmount);
                    this.setStatusMessage(this.enemy.getName()
                            + " tries to drain, and successfully drains "
                            + drainAmount + " MP!");
                    return true;
                }
            } else {
                // Failed
                this.setStatusMessage(
                        this.enemy.getName() + " tries to drain, but fails!");
                return false;
            }
        }
    }

    @Override
    public void endTurn() {
        // Do nothing
    }

    private void redrawBattle() {
        this.battleGUI.redrawBattle(this.battleMaze);
    }

    @Override
    public void redrawOneBattleSquare(final int x, final int y,
            final AbstractMazeObject obj3) {
        this.battleGUI.redrawOneBattleSquare(this.battleMaze, x, y, obj3);
    }

    private void updateStatsAndEffects() {
        this.battleGUI.updateStatsAndEffects(this.me);
    }

    @Override
    public void maintainEffects(final boolean player) {
        if (player) {
            if (this.me != null && this.me.isActive()) {
                final Creature active = this.me.getTemplate();
                // Use Effects
                active.useEffects();
                // Display all effect messages
                final String effectMessages = this.me.getTemplate()
                        .getAllCurrentEffectMessages();
                final String[] individualEffectMessages = effectMessages
                        .split("\n");
                for (final String message : individualEffectMessages) {
                    if (!message.equals(Effect.getNullMessage())) {
                        this.setStatusMessage(message);
                        try {
                            Thread.sleep(FantastleReboot.getBagOStuff()
                                    .getPrefsManager().getBattleSpeed());
                        } catch (final InterruptedException ie) {
                            // Ignore
                        }
                    }
                }
                // Handle low health for party members
                if (active.isAlive()
                        && active.getTeamID() == Creature.TEAM_PARTY
                        && active.getCurrentHP() <= active.getMaximumHP() * 3
                                / 10) {
                    SoundLoader.playSound(GameSound.LOW_HEALTH);
                }
                // Cull Inactive Effects
                active.cullInactiveEffects();
                // Handle death caused by effects
                if (!active.isAlive()) {
                    if (this.me.getTeamID() != Creature.TEAM_PARTY) {
                        // Update victory spoils
                        this.battleExp = this.me.getTemplate().getExperience();
                    }
                    // Set dead character to inactive
                    this.me.deactivate();
                    // Remove effects from dead character
                    active.stripAllEffects();
                    // Remove character from battle
                    this.battleMaze.setCell(new Empty(), this.me.getX(),
                            this.me.getY(), 0, MazeConstants.LAYER_OBJECT);
                }
            }
        } else {
            if (this.enemy != null && this.enemy.isActive()) {
                final Creature active = this.enemy.getTemplate();
                // Use Effects
                active.useEffects();
                // Display all effect messages
                final String effectMessages = this.enemy.getTemplate()
                        .getAllCurrentEffectMessages();
                final String[] individualEffectMessages = effectMessages
                        .split("\n");
                for (final String message : individualEffectMessages) {
                    if (!message.equals(Effect.getNullMessage())) {
                        this.setStatusMessage(message);
                        try {
                            Thread.sleep(FantastleReboot.getBagOStuff()
                                    .getPrefsManager().getBattleSpeed());
                        } catch (final InterruptedException ie) {
                            // Ignore
                        }
                    }
                }
                // Handle low health for party members
                if (active.isAlive()
                        && active.getTeamID() == Creature.TEAM_PARTY
                        && active.getCurrentHP() <= active.getMaximumHP() * 3
                                / 10) {
                    SoundLoader.playSound(GameSound.LOW_HEALTH);
                }
                // Cull Inactive Effects
                active.cullInactiveEffects();
                // Handle death caused by effects
                if (!active.isAlive()) {
                    if (this.enemy.getTeamID() != Creature.TEAM_PARTY) {
                        // Update victory spoils
                        this.battleExp = this.enemy.getTemplate()
                                .getExperience();
                    }
                    // Set dead character to inactive
                    this.enemy.deactivate();
                    // Remove effects from dead character
                    active.stripAllEffects();
                    // Remove character from battle
                    this.battleMaze.setCell(new Empty(), this.enemy.getX(),
                            this.enemy.getY(), 0, MazeConstants.LAYER_OBJECT);
                }
            }
        }
    }

    private void updateAllAIContexts() {
        this.myContext.updateContext(this.battleMaze);
        this.enemyContext.updateContext(this.battleMaze);
    }

    @Override
    public void resetGUI() {
        // Destroy old GUI
        this.battleGUI.getOutputFrame().dispose();
        // Create new GUI
        this.battleGUI = new MapTimeBattleGUI();
    }

    @Override
    public boolean isWaitingForAI() {
        return !this.battleGUI.areEventHandlersOn();
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
        case AbstractMapAIRoutine.ACTION_CAST_SPELL:
            this.castSpell();
            break;
        case AbstractMapAIRoutine.ACTION_DRAIN:
            this.drain();
            break;
        case AbstractMapAIRoutine.ACTION_STEAL:
            this.steal();
            break;
        case AbstractMapAIRoutine.ACTION_USE_ITEM:
            this.useItem();
            break;
        default:
            break;
        }
        this.battleGUI.resetPlayerActionBar();
        return true;
    }

    @Override
    public void doResult() {
        if (!this.resultDoneAlready) {
            // Handle Results
            this.resultDoneAlready = true;
            boolean rewardsFlag = false;
            if (this.getEnemy() instanceof BossMonster) {
                if (this.result == BattleResults.WON
                        || this.result == BattleResults.PERFECT) {
                    this.setStatusMessage("You defeated the Boss!");
                    SoundLoader.playSound(GameSound.VICTORY);
                    rewardsFlag = true;
                } else if (this.result == BattleResults.LOST) {
                    this.setStatusMessage("The Boss defeated you...");
                    SoundLoader.playSound(GameSound.GAME_OVER);
                    PartyManager.getParty().getLeader().onDeath(-10);
                } else if (this.result == BattleResults.ANNIHILATED) {
                    this.setStatusMessage(
                            "The Boss defeated you without suffering damage... you were annihilated!");
                    SoundLoader.playSound(GameSound.GAME_OVER);
                    PartyManager.getParty().getLeader().onDeath(-20);
                } else if (this.result == BattleResults.DRAW) {
                    this.setStatusMessage(
                            "The Boss battle was a draw. You are fully healed!");
                    PartyManager.getParty().getLeader().healPercentage(
                            Creature.FULL_HEAL_PERCENTAGE);
                    PartyManager.getParty().getLeader().regeneratePercentage(
                            Creature.FULL_HEAL_PERCENTAGE);
                } else if (this.result == BattleResults.FLED) {
                    this.setStatusMessage("You ran away successfully!");
                } else if (this.result == BattleResults.ENEMY_FLED) {
                    this.setStatusMessage("The Boss ran away!");
                }
            } else {
                if (this.result == BattleResults.WON) {
                    SoundLoader.playSound(GameSound.VICTORY);
                    CommonDialogs.showTitledDialog("The party is victorious!",
                            "Victory!");
                    PartyManager.getParty().getLeader()
                            .offsetGold(this.getGold());
                    PartyManager.getParty().getLeader()
                            .offsetExperience(this.battleExp);
                } else if (this.result == BattleResults.LOST) {
                    CommonDialogs.showTitledDialog(
                            "The party has been defeated!", "Defeat...");
                } else if (this.result == BattleResults.DRAW) {
                    CommonDialogs.showTitledDialog("The battle was a draw.",
                            "Draw");
                } else if (this.result == BattleResults.FLED) {
                    CommonDialogs.showTitledDialog("The party fled!",
                            "Party Fled");
                } else if (this.result == BattleResults.ENEMY_FLED) {
                    CommonDialogs.showTitledDialog("The enemies fled!",
                            "Enemies Fled");
                } else if (this.result == BattleResults.IN_PROGRESS) {
                    CommonDialogs.showTitledDialog(
                            "The battle isn't over, but somehow the game thinks it is.",
                            "Uh-Oh!");
                } else {
                    CommonDialogs.showTitledDialog(
                            "The result of the battle is unknown!", "Uh-Oh!");
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

    private class PlayerTask extends TimerTask {
        public PlayerTask() {
            // Do nothing
        }

        @Override
        public void run() {
            try {
                final Application app = TallerTower.getApplication();
                final Battle b = app.getBattle();
                if (app.getMode() == Application.STATUS_BATTLE
                        && b instanceof MapTimeBattleLogic) {
                    final MapTimeBattleLogic logic = MapTimeBattleLogic.this;
                    final MapTimeBattleGUI gui = logic.battleGUI;
                    if (!gui.isPlayerActionBarFull()) {
                        gui.turnEventHandlersOff();
                        gui.updatePlayerActionBarValue();
                        if (gui.isPlayerActionBarFull()) {
                            SoundLoader.playSound(GameSound.PLAYER_UP);
                            gui.turnEventHandlersOn();
                        }
                    } else {
                        gui.turnEventHandlersOn();
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
                final Application app = TallerTower.getApplication();
                final Battle b = app.getBattle();
                if (app.getMode() == Application.STATUS_BATTLE
                        && b instanceof MapTimeBattleLogic) {
                    final MapTimeBattleLogic logic = MapTimeBattleLogic.this;
                    final MapTimeBattleGUI gui = logic.battleGUI;
                    if (!gui.isEnemyActionBarFull()) {
                        gui.updateEnemyActionBarValue();
                        if (gui.isEnemyActionBarFull()) {
                            this.enemyAct();
                        }
                    } else {
                        this.enemyAct();
                    }
                }
            } catch (final Throwable t) {
                FantastleReboot.logError(t);
            }
        }

        private void enemyAct() {
            final MapTimeBattleLogic logic = MapTimeBattleLogic.this;
            final MapTimeBattleGUI gui = logic.battleGUI;
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
            }
        }
    }
}
