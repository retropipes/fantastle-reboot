package com.puttysoftware.fantastlereboot.oldbattle;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.battle.BattleResults;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;

public class BossBattle extends Battle {
    // Fields
    protected final BossRewards rewards;

    // Constructor
    public BossBattle() {
        this.rewards = new BossRewards();
    }

    // Method
    @Override
    public void doBattle() {
        Battle.IN_BATTLE = true;
        FantastleReboot.getBagOStuff().getGameManager().hideOutput();
        FantastleReboot.getBagOStuff().getPrefsManager();
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_BATTLE)) {
            SoundLoader.playSound(GameSound.DRAW_SWORD);
        }
        this.enemy = new Boss();
        this.iconLabel.setIcon(this.enemy.getImage());
        this.enemyDidDamage = false;
        this.playerDidDamage = false;
        this.setResult(BattleResults.IN_PROGRESS);
        this.attack.setVisible(true);
        this.flee.setVisible(true);
        this.spell.setVisible(true);
        this.steal.setVisible(true);
        this.drain.setVisible(true);
        this.item.setVisible(true);
        this.done.setVisible(false);
        this.attack.setEnabled(true);
        this.flee.setEnabled(true);
        this.spell.setEnabled(true);
        this.steal.setEnabled(false);
        this.drain.setEnabled(false);
        this.item.setEnabled(true);
        this.done.setEnabled(false);
        this.firstUpdateMessageArea();
        this.battleFrame.setVisible(true);
    }

    @Override
    public void doResult() {
        final PartyMember playerCharacter = PartyManager.getParty().getLeader();
        this.appendToMessageArea("\n");
        // Cleanup
        playerCharacter.stripAllEffects();
        this.attack.setVisible(false);
        this.flee.setVisible(false);
        this.spell.setVisible(false);
        this.steal.setVisible(false);
        this.drain.setVisible(false);
        this.item.setVisible(false);
        this.done.setVisible(true);
        this.attack.setEnabled(false);
        this.flee.setEnabled(false);
        this.spell.setEnabled(false);
        this.item.setEnabled(false);
        this.done.setEnabled(true);
        if (this.result == BattleResults.WON) {
            this.appendToMessageArea("You beat the Boss!");
            this.rewards.doRewards();
            this.battleDone();
        } else if (this.result == BattleResults.PERFECT) {
            this.appendToMessageArea(
                    "You beat the Boss, and didn't suffer any damage!");
            this.rewards.doRewards();
            this.battleDone();
        } else if (this.result == BattleResults.LOST) {
            this.appendToMessageArea("The Boss beat you...");
            playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
        } else if (this.result == BattleResults.ANNIHILATED) {
            this.appendToMessageArea(
                    "The Boss beat you... and you didn't even hurt him!");
            playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
        } else if (this.result == BattleResults.DRAW) {
            this.appendToMessageArea("The battle was a draw!");
            playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
        } else if (this.result == BattleResults.FLED) {
            this.appendToMessageArea("Come back when you're ready...");
            playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
        }
        this.battleFrame.pack();
    }
}