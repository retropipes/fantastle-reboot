package net.worldwizard.fantastle5.battle;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import com.puttysoftware.randomrange.RandomRange;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.Messager;
import net.worldwizard.fantastle5.PreferencesManager;
import net.worldwizard.fantastle5.ai.AIRoutine;
import net.worldwizard.fantastle5.creatures.Creature;
import net.worldwizard.fantastle5.creatures.Monster;
import net.worldwizard.fantastle5.creatures.PCManager;
import net.worldwizard.fantastle5.creatures.PlayerCharacter;
import net.worldwizard.fantastle5.creatures.StatConstants;
import net.worldwizard.fantastle5.creatures.castes.Caste;
import net.worldwizard.fantastle5.creatures.castes.CasteConstants;
import net.worldwizard.fantastle5.effects.Effect;
import net.worldwizard.fantastle5.items.combat.CombatItemManager;
import net.worldwizard.fantastle5.resourcemanagers.MusicManager;
import net.worldwizard.fantastle5.resourcemanagers.SoundManager;
import net.worldwizard.fantastle5.spells.SpellBookManager;

public class Battle implements BattleResults, MoveTypes {
    // Fields
    protected int stealAmount;
    protected int damage;
    protected int enemyDamage;
    protected int riposteDamage;
    protected int riposteEnemyDamage;
    protected int fumbleDamage;
    protected int enemyFumbleDamage;
    protected boolean enemyDidDamage;
    protected boolean playerDidDamage;
    protected int moveType;
    protected int enemyMoveType;
    protected double multiplier;
    protected double enemyMultiplier;
    protected JFrame battleFrame;
    protected Container holderPane, iconPane, messagePane, buttonPane;
    protected JLabel iconLabel;
    protected JTextArea messageArea;
    protected JButton attack, flee, spell, steal, drain, item, done;
    protected BattleEventHandler handler;
    protected Creature enemy;
    protected int result;
    protected int enemyAction;
    protected static boolean IN_BATTLE = false;
    protected static final int PIERCE_CHANCE = 10;
    protected static final int BASE_RUN_CHANCE = 80;
    protected static final int RUN_CHANCE_DIFF_FACTOR = 5;
    protected static final int ENEMY_BASE_RUN_CHANCE = 60;
    protected static final int ENEMY_RUN_CHANCE_DIFF_FACTOR = 10;

    // Constructor
    public Battle() {
        // Initialize Battle Parameters
        this.damage = 0;
        this.enemyDamage = 0;
        this.riposteDamage = 0;
        this.riposteEnemyDamage = 0;
        this.stealAmount = 0;
        this.enemyDidDamage = false;
        this.playerDidDamage = false;
        this.moveType = MoveTypes.NORMAL;
        this.enemyMoveType = MoveTypes.NORMAL;
        // Initialize GUI
        this.battleFrame = new JFrame("Battle");
        this.holderPane = new Container();
        this.iconPane = new Container();
        this.messagePane = new Container();
        this.buttonPane = new Container();
        this.iconLabel = new JLabel("");
        this.messageArea = new JTextArea();
        this.messageArea.setOpaque(true);
        this.messageArea.setEditable(false);
        this.messageArea.setBackground(this.battleFrame.getBackground());
        this.attack = new JButton("Attack");
        this.flee = new JButton("Flee");
        this.spell = new JButton("Cast Spell");
        this.steal = new JButton("Steal Money");
        this.drain = new JButton("Drain Enemy");
        this.item = new JButton("Use Item");
        this.done = new JButton("Continue");
        this.battleFrame.getRootPane().setDefaultButton(this.done);
        this.iconPane.setLayout(new FlowLayout());
        this.messagePane.setLayout(new FlowLayout());
        this.buttonPane.setLayout(new GridLayout(2, 4));
        this.holderPane.setLayout(new BorderLayout());
        this.iconPane.add(this.iconLabel);
        this.messagePane.add(this.messageArea);
        this.buttonPane.add(this.attack);
        this.buttonPane.add(this.flee);
        this.buttonPane.add(this.spell);
        this.buttonPane.add(this.steal);
        this.buttonPane.add(this.drain);
        this.buttonPane.add(this.item);
        this.buttonPane.add(this.done);
        this.holderPane.add(this.iconPane, BorderLayout.WEST);
        this.holderPane.add(this.messagePane, BorderLayout.CENTER);
        this.holderPane.add(this.buttonPane, BorderLayout.SOUTH);
        this.battleFrame.setContentPane(this.holderPane);
        this.battleFrame.setResizable(false);
        this.battleFrame.pack();
        // Initialize Event Handlers
        this.handler = new BattleEventHandler();
        this.attack.addActionListener(this.handler);
        this.flee.addActionListener(this.handler);
        this.spell.addActionListener(this.handler);
        this.steal.addActionListener(this.handler);
        this.drain.addActionListener(this.handler);
        this.done.addActionListener(this.handler);
        this.attack.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "Attack");
        this.attack.getActionMap().put("Attack", this.handler);
        this.flee.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "Flee");
        this.flee.getActionMap().put("Flee", this.handler);
        this.spell.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "Cast Spell");
        this.spell.getActionMap().put("Cast Spell", this.handler);
        this.steal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "Steal Money");
        this.steal.getActionMap().put("Steal Money", this.handler);
        this.drain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "Drain Enemy");
        this.drain.getActionMap().put("Drain Enemy", this.handler);
        this.item.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "Use Item");
        this.item.getActionMap().put("Use Item", this.handler);
    }

    public JFrame getBattleFrame() {
        return this.battleFrame;
    }

    protected final boolean doPlayerActions(final int actionToPerform) {
        boolean success = true;
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        if (actionToPerform == AIRoutine.ACTION_ATTACK) {
            final int actions = playerCharacter.getActionsPerRound();
            for (int x = 0; x < actions; x++) {
                this.computePlayerDamage();
                this.displayPlayerRoundResults();
            }
        } else if (actionToPerform == AIRoutine.ACTION_CAST_SPELL) {
            success = SpellBookManager.selectAndCastSpell(playerCharacter);
        } else if (actionToPerform == AIRoutine.ACTION_FLEE) {
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
        } else if (actionToPerform == AIRoutine.ACTION_STEAL) {
            success = this.stealMoney();
            if (success) {
                if (Fantastle5.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously("drain");
                }
                this.updateMessageAreaPostSteal();
            } else {
                if (Fantastle5.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously("actfail");
                }
                this.updateMessageAreaStealFailed();
            }
        } else if (actionToPerform == AIRoutine.ACTION_DRAIN) {
            success = this.drainEnemy();
            if (success) {
                if (Fantastle5.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously("drain");
                }
                this.updateMessageAreaPostDrain();
            } else {
                if (Fantastle5.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously("actfail");
                }
                this.updateMessageAreaDrainFailed();
            }
        } else if (actionToPerform == AIRoutine.ACTION_USE_ITEM) {
            success = CombatItemManager.selectAndUseItem(playerCharacter);
        }
        return success;
    }

    protected final void doEnemyActions() {
        final int actionToPerform = this.enemy.getAI()
                .getNextAction(this.enemy);
        if (actionToPerform == AIRoutine.ACTION_ATTACK) {
            final int actions = this.enemy.getActionsPerRound();
            for (int x = 0; x < actions; x++) {
                this.computeEnemyDamage();
                this.displayEnemyRoundResults();
            }
        } else if (actionToPerform == AIRoutine.ACTION_CAST_SPELL) {
            SpellBookManager.castSpell(this.enemy.getAI().getSpellToCast(),
                    this.enemy);
        } else if (actionToPerform == AIRoutine.ACTION_FLEE) {
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

    protected final void computePlayerDamage() {
        // Compute Player Damage
        if (this.result != BattleResults.ENEMY_FLED) {
            this.fumbleDamage = 0;
            final PlayerCharacter playerCharacter = PCManager.getPlayer();
            if (Battle.fumble(playerCharacter)) {
                this.moveType = MoveTypes.FUMBLE;
                final RandomRange fumDamage = new RandomRange(1,
                        Math.max(playerCharacter.getWeaponPower(), 1));
                this.fumbleDamage = fumDamage.generate();
                playerCharacter.doDamage(this.fumbleDamage);
            } else {
                this.moveType = MoveTypes.NORMAL;
                final RandomRange randomAttackGenerator = new RandomRange(0,
                        (int) playerCharacter
                                .getEffectedStat(StatConstants.STAT_ATTACK));
                final RandomRange randomEnemyDefenseGenerator = new RandomRange(
                        0, (int) this.enemy
                                .getEffectedStat(StatConstants.STAT_DEFENSE));
                int currDamage = 0;
                int currRiposteEnemyDamage = 0;
                this.multiplier = MultiplierValues.getRandomNormalValue();
                final boolean didPierce = Battle.pierce();
                final int randomAttack = (int) (randomAttackGenerator.generate()
                        * this.multiplier);
                final int randomEnemyDefense = randomEnemyDefenseGenerator
                        .generate();
                if (didPierce) {
                    this.moveType += MoveTypes.PIERCING;
                    currDamage = randomAttack;
                } else {
                    if (randomAttack - randomEnemyDefense < 0) {
                        currRiposteEnemyDamage = randomEnemyDefense
                                - randomAttack;
                    } else {
                        currDamage = randomAttack - randomEnemyDefense;
                    }
                }
                this.damage = (int) (currDamage
                        * StatConstants.FACTOR_DIFFERENTIAL_DAMAGE);
                this.riposteEnemyDamage = (int) (currRiposteEnemyDamage
                        * StatConstants.FACTOR_DIFFERENTIAL_DAMAGE);
                if (currRiposteEnemyDamage > 0) {
                    this.enemyDidDamage = true;
                }
                if (currDamage > 0) {
                    this.playerDidDamage = true;
                }
                playerCharacter.doDamage(this.riposteEnemyDamage);
                this.enemy.doDamage(this.damage);
            }
        }
    }

    protected final void computeEnemyDamage() {
        // Compute Enemy Damage
        if (this.result != BattleResults.ENEMY_FLED) {
            this.enemyFumbleDamage = 0;
            final PlayerCharacter playerCharacter = PCManager.getPlayer();
            if (Battle.fumble(this.enemy)) {
                this.enemyMoveType = MoveTypes.FUMBLE;
                final RandomRange fumDamage = new RandomRange(1,
                        Math.max(this.enemy.getAttack(), 1));
                this.enemyFumbleDamage = fumDamage.generate();
                this.enemy.doDamage(this.enemyFumbleDamage);
            } else {
                this.enemyMoveType = MoveTypes.NORMAL;
                final RandomRange randomDefenseGenerator = new RandomRange(0,
                        (int) playerCharacter
                                .getEffectedStat(StatConstants.STAT_DEFENSE));
                final RandomRange randomEnemyAttackGenerator = new RandomRange(
                        0, (int) this.enemy
                                .getEffectedStat(StatConstants.STAT_ATTACK));
                int currEnemyDamage = 0;
                int currRiposteDamage = 0;
                this.enemyMultiplier = MultiplierValues.getRandomNormalValue();
                final boolean didPierce = Battle.pierce();
                final int randomEnemyAttack = (int) (randomEnemyAttackGenerator
                        .generate() * this.enemyMultiplier);
                final int randomDefense = randomDefenseGenerator.generate();
                if (didPierce) {
                    this.enemyMoveType += MoveTypes.PIERCING;
                    currEnemyDamage = randomEnemyAttack;
                } else {
                    if (randomEnemyAttack - randomDefense < 0) {
                        currRiposteDamage = randomDefense - randomEnemyAttack;
                    } else {
                        currEnemyDamage = randomEnemyAttack - randomDefense;
                    }
                }
                this.enemyDamage = (int) (currEnemyDamage
                        * StatConstants.FACTOR_DIFFERENTIAL_DAMAGE);
                this.riposteDamage = (int) (currRiposteDamage
                        * StatConstants.FACTOR_DIFFERENTIAL_DAMAGE);
                if (currEnemyDamage > 0) {
                    this.enemyDidDamage = true;
                }
                if (currRiposteDamage > 0) {
                    this.playerDidDamage = true;
                }
                playerCharacter.doDamage(this.enemyDamage);
                this.enemy.doDamage(this.riposteDamage);
            }
        }
    }

    protected final int computeRunChance() {
        return Battle.BASE_RUN_CHANCE - this.enemy.getLevelDifference()
                * Battle.RUN_CHANCE_DIFF_FACTOR;
    }

    public final int computeEnemyRunChance() {
        return Battle.ENEMY_BASE_RUN_CHANCE + this.enemy.getLevelDifference()
                * Battle.ENEMY_RUN_CHANCE_DIFF_FACTOR;
    }

    protected final void displayBattleStats() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final String enemyName = this.enemy.getName();
        final String fightingWhat = this.enemy.getFightingWhatString();
        final String monsterLevelString = enemyName + "'s Level: "
                + Integer.toString(this.enemy.getLevel());
        final String monsterHPString = this.enemy.getEffectedHPString();
        final String monsterMPString = this.enemy.getEffectedMPString();
        final String playerHPString = playerCharacter.getEffectedHPString();
        final String playerMPString = playerCharacter.getEffectedMPString();
        final String displayMonsterHPString = enemyName + "'s HP: "
                + monsterHPString;
        final String displayMonsterMPString = enemyName + "'s MP: "
                + monsterMPString;
        final String displayPlayerHPString = "Your HP: " + playerHPString;
        final String displayPlayerMPString = "Your MP: " + playerMPString;
        final String displayString = fightingWhat + "\n" + monsterLevelString
                + "\n" + displayMonsterHPString + "\n" + displayMonsterMPString
                + "\n" + displayPlayerHPString + "\n" + displayPlayerMPString;
        this.appendToMessageArea(displayString);
    }

    protected final void displayPlayerRoundResults() {
        // Display player round results
        if (this.result != BattleResults.ENEMY_FLED) {
            final String enemyName = this.enemy.getName();
            final String playerDamageString = Integer.toString(this.damage);
            final String playerFumbleDamageString = Integer
                    .toString(this.fumbleDamage);
            String displayPlayerDamageString = null;
            String playerWhackString = "";
            if (this.fumbleDamage != 0) {
                displayPlayerDamageString = "FUMBLE! You drop your weapon, doing "
                        + playerFumbleDamageString + " damage to yourself!";
                if (Fantastle5.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously("fumble");
                }
            } else {
                if (this.damage == 0 && this.riposteEnemyDamage == 0) {
                    displayPlayerDamageString = "You try to hit the "
                            + enemyName + ", but MISS!";
                    if (Fantastle5.getApplication().getPrefsManager()
                            .getSoundEnabled(
                                    PreferencesManager.SOUNDS_BATTLE)) {
                        SoundManager.playSoundAsynchronously("missed");
                    }
                } else if (this.damage == 0 && this.riposteEnemyDamage != 0) {
                    displayPlayerDamageString = "You try to hit the "
                            + enemyName + ", but are RIPOSTED for "
                            + this.riposteEnemyDamage + " damage!";
                    if (Fantastle5.getApplication().getPrefsManager()
                            .getSoundEnabled(
                                    PreferencesManager.SOUNDS_BATTLE)) {
                        SoundManager.playSoundAsynchronously("counter");
                    }
                } else {
                    displayPlayerDamageString = "You hit the " + enemyName
                            + " for " + playerDamageString + " damage!";
                    if (Fantastle5.getApplication().getPrefsManager()
                            .getSoundEnabled(
                                    PreferencesManager.SOUNDS_BATTLE)) {
                        SoundManager.playSoundAsynchronously("hit");
                    }
                }
                final String whack = MultiplierValues
                        .getTextForValue(this.multiplier);
                if (!whack.equals("")) {
                    playerWhackString = "Your attack hits with " + whack
                            + "force!\n";
                }
                if ((this.moveType | MoveTypes.PIERCING) == this.moveType) {
                    playerWhackString += "Your attack pierces the " + enemyName
                            + "'s armor!\n";
                }
            }
            final String displayString = playerWhackString
                    + displayPlayerDamageString;
            this.appendToMessageArea(displayString);
        }
    }

    protected final void displayEnemyRoundResults() {
        // Display enemy round results
        if (this.result != BattleResults.ENEMY_FLED) {
            final String enemyName = this.enemy.getName();
            final String enemyDamageString = Integer.toString(this.enemyDamage);
            final String enemyFumbleDamageString = Integer
                    .toString(this.enemyFumbleDamage);
            String displayEnemyDamageString = null;
            String enemyWhackString = "";
            if (this.enemyFumbleDamage != 0) {
                displayEnemyDamageString = "FUMBLE! The " + enemyName
                        + " drops its weapon, doing " + enemyFumbleDamageString
                        + " damage to itself!";
                if (Fantastle5.getApplication().getPrefsManager()
                        .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                    SoundManager.playSoundAsynchronously("fumble");
                }
                enemyWhackString = "";
            } else {
                if (this.enemyDamage == 0 && this.riposteDamage == 0) {
                    displayEnemyDamageString = "The " + enemyName
                            + " tries to hit you, but MISSES!";
                    if (Fantastle5.getApplication().getPrefsManager()
                            .getSoundEnabled(
                                    PreferencesManager.SOUNDS_BATTLE)) {
                        SoundManager.playSoundAsynchronously("missed");
                    }
                } else if (this.enemyDamage == 0 && this.riposteDamage != 0) {
                    displayEnemyDamageString = "The " + enemyName
                            + " tries to hit you, but you RIPOSTE for "
                            + this.riposteDamage + " damage!";
                    if (Fantastle5.getApplication().getPrefsManager()
                            .getSoundEnabled(
                                    PreferencesManager.SOUNDS_BATTLE)) {
                        SoundManager.playSoundAsynchronously("counter");
                    }
                } else {
                    displayEnemyDamageString = "The " + enemyName
                            + " hits you for " + enemyDamageString + " damage!";
                    if (Fantastle5.getApplication().getPrefsManager()
                            .getSoundEnabled(
                                    PreferencesManager.SOUNDS_BATTLE)) {
                        SoundManager.playSoundAsynchronously("hit");
                    }
                }
                final String whack = MultiplierValues
                        .getTextForValue(this.multiplier);
                if (!whack.equals("")) {
                    enemyWhackString = "The " + enemyName
                            + "'s attack hits with " + whack + "force!\n";
                }
                if ((this.enemyMoveType
                        | MoveTypes.PIERCING) == this.enemyMoveType) {
                    enemyWhackString += "The " + enemyName
                            + "'s attack pierces YOUR armor!\n";
                }
            }
            final String displayString = enemyWhackString
                    + displayEnemyDamageString;
            this.appendToMessageArea(displayString);
        }
    }

    // Methods
    public void doBattle() {
        Battle.IN_BATTLE = true;
        Fantastle5.getApplication().getGameManager().hideOutput();
        if (Fantastle5.getApplication().getPrefsManager()
                .getMusicEnabled(PreferencesManager.MUSIC_BATTLE)) {
            MusicManager.playMusic("battle");
        }
        if (Fantastle5.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
            SoundManager.playSoundAsynchronously("battle");
        }
        this.enemy = new Monster();
        this.iconLabel.setIcon(this.enemy.getImage());
        this.enemyDidDamage = false;
        this.playerDidDamage = false;
        this.setResult(BattleResults.IN_PROGRESS);
        this.attack.setVisible(true);
        this.flee.setVisible(true);
        this.spell.setVisible(true);
        this.steal.setVisible(true);
        this.drain.setVisible(true);
        this.done.setVisible(false);
        this.attack.setEnabled(true);
        this.flee.setEnabled(true);
        this.spell.setEnabled(true);
        this.steal.setEnabled(true);
        this.drain.setEnabled(true);
        this.done.setEnabled(false);
        this.firstUpdateMessageArea();
        this.battleFrame.setVisible(true);
    }

    public void doBattleByProxy() {
        this.enemy = new Monster();
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final Monster m = (Monster) this.enemy;
        playerCharacter.offsetExperience(m.getExperience());
        playerCharacter.offsetGold(m.getGold());
        Fantastle5.getApplication().getGameManager().getScoreTracker()
                .updateScore(m.getExperience() + m.getGold());
        // Level Up Check
        if (playerCharacter.checkLevelUp()) {
            playerCharacter.levelUp();
            Fantastle5.getApplication().getGameManager().keepNextMessage();
            Messager.showMessage(
                    "You reached level " + playerCharacter.getLevel() + ".");
        }
    }

    public final Creature getEnemy() {
        return this.enemy;
    }

    protected final int getResult() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        int currResult;
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

    protected final void maintainEffects() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        playerCharacter.useEffects();
        playerCharacter.cullInactiveEffects();
        this.enemy.useEffects();
        this.enemy.cullInactiveEffects();
    }

    protected final static boolean pierce() {
        final RandomRange prc = new RandomRange(1, 100);
        final int prcChance = prc.generate();
        return prcChance <= Battle.PIERCE_CHANCE;
    }

    protected final static boolean fumble(final Creature fumbler) {
        final RandomRange fum = new RandomRange(1, 100);
        final int fumChance = fum.generate();
        return fumChance <= fumbler
                .getEffectedStat(StatConstants.STAT_FUMBLE_CHANCE);
    }

    protected final void displayActiveEffects() {
        boolean flag1 = false, flag2 = false, flag3 = false;
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final String effectString = playerCharacter.getCompleteEffectString();
        final String effectMessages = playerCharacter
                .getAllCurrentEffectMessages();
        final String enemyEffectMessages = this.enemy
                .getAllCurrentEffectMessages();
        if (!effectString.equals(Effect.getNullEffectString())) {
            flag1 = true;
        }
        if (!effectMessages.equals(Effect.getNullMessage())) {
            flag2 = true;
        }
        if (!enemyEffectMessages.equals(Effect.getNullMessage())) {
            flag3 = true;
        }
        if (flag1) {
            this.appendToMessageArea(effectString);
        }
        if (flag2) {
            this.appendToMessageArea(effectMessages);
        }
        if (flag3) {
            this.appendToMessageArea(enemyEffectMessages);
        }
    }

    protected final void clearMessageArea() {
        this.messageArea.setText("");
    }

    protected final void appendToMessageArea(final String s) {
        this.messageArea.setText(this.messageArea.getText() + s + "\n");
    }

    protected final void stripExtraNewLine() {
        final String currText = this.messageArea.getText();
        this.messageArea.setText(currText.substring(0, currText.length() - 1));
    }

    protected final void firstUpdateMessageArea() {
        this.clearMessageArea();
        this.appendToMessageArea("*** Beginning of Round ***");
        this.displayBattleStats();
        this.appendToMessageArea("*** Beginning of Round ***\n");
        // Determine initiative
        boolean enemyGotJump = false;
        if (this.enemy.getSpeed() > PCManager.getPlayer().getSpeed()) {
            // Enemy acts first!
            enemyGotJump = true;
        } else if (this.enemy.getSpeed() < PCManager.getPlayer().getSpeed()) {
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
            this.appendToMessageArea("The enemy acts first!");
            this.doEnemyActions();
            // Display Active Effects
            this.displayActiveEffects();
            // Maintain Effects
            this.maintainEffects();
        } else {
            this.appendToMessageArea("You act first!");
        }
        this.appendToMessageArea("\n*** End of Round ***");
        this.displayBattleStats();
        this.appendToMessageArea("*** End of Round ***");
        this.stripExtraNewLine();
        this.battleFrame.pack();
    }

    protected final void updateMessageAreaEnemyFleeFailed() {
        this.appendToMessageArea(
                "The enemy tries to run away, but doesn't quite make it!");
    }

    protected final void updateMessageAreaPostSteal() {
        this.appendToMessageArea(
                "You try to steal money, and successfully steal "
                        + this.stealAmount + " Gold!");
    }

    protected final void updateMessageAreaPostDrain() {
        this.appendToMessageArea("You try to drain the enemy, and succeed!");
    }

    protected final void updateMessageAreaFleeFailed() {
        this.appendToMessageArea(
                "You try to run away, but don't quite make it!");
    }

    protected final boolean stealMoney() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final Caste caste = playerCharacter.getCaste();
        final int stealChance = StatConstants.CHANCE_STEAL + caste.getAttribute(
                CasteConstants.CASTE_ATTRIBUTE_STEAL_SUCCESS_MODIFIER);
        if (stealChance <= 0) {
            // Failed
            this.stealAmount = 0;
            return false;
        } else if (stealChance >= 100) {
            // Succeeded
            final RandomRange stole = new RandomRange(0, this.enemy.getGold());
            this.stealAmount = stole.generate();
            playerCharacter.offsetGold(this.stealAmount);
            return true;
        } else {
            final RandomRange chance = new RandomRange(0, 100);
            final int randomChance = chance.generate();
            if (randomChance <= stealChance) {
                // Succeeded
                final RandomRange stole = new RandomRange(0,
                        this.enemy.getGold());
                this.stealAmount = stole.generate();
                playerCharacter.offsetGold(this.stealAmount);
                return true;
            } else {
                // Failed
                this.stealAmount = 0;
                return false;
            }
        }
    }

    protected final boolean drainEnemy() {
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final Caste caste = playerCharacter.getCaste();
        final int drainChance = StatConstants.CHANCE_DRAIN + caste.getAttribute(
                CasteConstants.CASTE_ATTRIBUTE_DRAIN_SUCCESS_MODIFIER);
        if (drainChance <= 0) {
            // Failed
            return false;
        } else if (drainChance >= 100) {
            // Succeeded
            final RandomRange drained = new RandomRange(0,
                    this.enemy.getCurrentMP());
            final int newDrain = drained.generate();
            this.enemy.offsetCurrentMP(-newDrain);
            playerCharacter.offsetCurrentMP(newDrain);
            return true;
        } else {
            final RandomRange chance = new RandomRange(0, 100);
            final int randomChance = chance.generate();
            if (randomChance <= drainChance) {
                // Succeeded
                final RandomRange drained = new RandomRange(0,
                        this.enemy.getCurrentMP());
                final int newDrain = drained.generate();
                this.enemy.offsetCurrentMP(-newDrain);
                playerCharacter.offsetCurrentMP(newDrain);
                return true;
            } else {
                // Failed
                return false;
            }
        }
    }

    protected final void updateMessageAreaStealFailed() {
        this.appendToMessageArea(
                "You try to steal money from the enemy, but the attempt fails!");
    }

    protected final void updateMessageAreaDrainFailed() {
        this.appendToMessageArea(
                "You try to drain the enemy's MP, but the attempt fails!");
    }

    protected void doResult() {
        if (Fantastle5.getApplication().getPrefsManager()
                .getMusicEnabled(PreferencesManager.MUSIC_BATTLE)) {
            MusicManager.stopMusic();
        }
        final PlayerCharacter playerCharacter = PCManager.getPlayer();
        final Monster m = (Monster) this.enemy;
        if (this.result == BattleResults.WON) {
            this.appendToMessageArea("You gain " + m.getExperience()
                    + " experience and " + m.getGold() + " Gold.");
            playerCharacter.offsetExperience(m.getExperience());
            playerCharacter.offsetGold(m.getGold());
            if (Fantastle5.getApplication().getPrefsManager()
                    .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                SoundManager.playSoundAsynchronously("victory");
            }
            Fantastle5.getApplication().getGameManager().getScoreTracker()
                    .updateScore(m.getExperience() + m.getGold());
        } else if (this.result == BattleResults.PERFECT) {
            this.appendToMessageArea("You gain " + m.getExperience()
                    + " experience and " + m.getGold() + " Gold,\nplus "
                    + m.getPerfectBonusGold()
                    + " extra gold for a perfect fight!");
            playerCharacter.offsetExperience(m.getExperience());
            playerCharacter.offsetGold(m.getGold() + m.getPerfectBonusGold());
            if (Fantastle5.getApplication().getPrefsManager()
                    .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                SoundManager.playSoundAsynchronously("victory");
            }
            Fantastle5.getApplication().getGameManager().getScoreTracker()
                    .updateScore(m.getExperience() + m.getGold()
                            + m.getPerfectBonusGold());
        } else if (this.result == BattleResults.LOST) {
            this.appendToMessageArea("You lost...");
        } else if (this.result == BattleResults.ANNIHILATED) {
            this.appendToMessageArea(
                    "You lost without hurting your foe... you were annihilated!");
        } else if (this.result == BattleResults.DRAW) {
            this.appendToMessageArea(
                    "The battle was a draw. You are fully healed!");
            playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
            playerCharacter.regeneratePercentage(Creature.FULL_HEAL_PERCENTAGE);
        } else if (this.result == BattleResults.FLED) {
            this.appendToMessageArea("You ran away successfully!");
        } else if (this.result == BattleResults.ENEMY_FLED) {
            this.appendToMessageArea("The enemy runs away!");
            this.appendToMessageArea(
                    "Since the enemy ran away, you gain nothing for this battle.");
        }
        // Cleanup
        this.attack.setVisible(false);
        this.flee.setVisible(false);
        this.spell.setVisible(false);
        this.steal.setVisible(false);
        this.drain.setVisible(false);
        this.done.setVisible(true);
        this.attack.setEnabled(false);
        this.flee.setEnabled(false);
        this.spell.setEnabled(false);
        this.steal.setEnabled(false);
        this.drain.setEnabled(false);
        this.done.setEnabled(true);
        playerCharacter.stripAllEffects();
        this.enemy.stripAllEffects();
        // Level Up Check
        if (playerCharacter.checkLevelUp()) {
            playerCharacter.levelUp();
            if (Fantastle5.getApplication().getPrefsManager()
                    .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
                SoundManager.playSoundAsynchronously("levelup");
            }
            this.appendToMessageArea(
                    "You reached level " + playerCharacter.getLevel() + ".");
        }
        // Final Cleanup
        this.stripExtraNewLine();
        this.battleFrame.pack();
    }

    protected final void setResult(final int newResult) {
        this.result = newResult;
    }

    public final void battleDone() {
        this.battleFrame.setVisible(false);
        Battle.IN_BATTLE = false;
        if (this.result == BattleResults.LOST
                || this.result == BattleResults.ANNIHILATED) {
            Fantastle5.getApplication().getGameManager()
                    .gameOverWithMessage("You fell in battle... Game Over!");
        }
        Fantastle5.getApplication().getGameManager().showOutput();
        Fantastle5.getApplication().getGameManager().redrawMaze();
    }

    public final static boolean isInBattle() {
        return Battle.IN_BATTLE;
    }

    protected class BattleEventHandler extends AbstractAction {
        private static final long serialVersionUID = 20239525230523523L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                boolean success = true;
                final String cmd = e.getActionCommand();
                final Battle b = Battle.this;
                // Clear Message Area
                b.clearMessageArea();
                // Display Beginning Stats
                b.appendToMessageArea("*** Beginning of Round ***");
                b.displayBattleStats();
                b.appendToMessageArea("*** Beginning of Round ***\n");
                // Do Player Actions
                if (cmd.equals("Attack") || cmd.equals("a")) {
                    // Attack
                    success = b.doPlayerActions(AIRoutine.ACTION_ATTACK);
                } else if (cmd.equals("Flee") || cmd.equals("f")) {
                    // Try to Flee
                    success = b.doPlayerActions(AIRoutine.ACTION_FLEE);
                    if (success) {
                        // Strip Extra Newline Character
                        b.stripExtraNewLine();
                        // Pack Battle Frame
                        b.battleFrame.pack();
                        // Get out of here
                        b.doResult();
                        return;
                    } else {
                        success = b.doPlayerActions(AIRoutine.ACTION_ATTACK);
                    }
                } else if (cmd.equals("Continue")) {
                    // Battle Done
                    b.battleDone();
                    return;
                } else if (cmd.equals("Cast Spell") || cmd.equals("c")) {
                    // Cast Spell
                    success = b.doPlayerActions(AIRoutine.ACTION_CAST_SPELL);
                    if (!success) {
                        // Strip Two Extra Newline Characters
                        b.stripExtraNewLine();
                        b.stripExtraNewLine();
                        // Pack Battle Frame
                        b.battleFrame.pack();
                        // Get out of here
                        return;
                    }
                } else if (cmd.equals("Steal Money") || cmd.equals("s")) {
                    // Steal Money
                    success = b.doPlayerActions(AIRoutine.ACTION_STEAL);
                } else if (cmd.equals("Drain Enemy") || cmd.equals("d")) {
                    // Drain Enemy
                    success = b.doPlayerActions(AIRoutine.ACTION_DRAIN);
                } else if (cmd.equals("Use Item") || cmd.equals("i")) {
                    // Use Item
                    success = b.doPlayerActions(AIRoutine.ACTION_USE_ITEM);
                    if (!success) {
                        // Strip Two Extra Newline Characters
                        b.stripExtraNewLine();
                        b.stripExtraNewLine();
                        // Pack Battle Frame
                        b.battleFrame.pack();
                        // Get out of here
                        return;
                    }
                }
                // Do Enemy Actions
                b.doEnemyActions();
                // Display Active Effects
                b.displayActiveEffects();
                // Maintain Effects
                b.maintainEffects();
                // Display End Stats
                b.appendToMessageArea("\n*** End of Round ***");
                b.displayBattleStats();
                b.appendToMessageArea("*** End of Round ***");
                // Check Result
                final int newResult = b.getResult();
                if (newResult != BattleResults.IN_PROGRESS) {
                    b.setResult(newResult);
                    b.doResult();
                } else {
                    // Strip Extra Newline Character
                    b.stripExtraNewLine();
                    // Pack Battle Frame
                    b.battleFrame.pack();
                }
            } catch (final Throwable t) {
                Fantastle5.logError(t);
            }
        }
    }
}
