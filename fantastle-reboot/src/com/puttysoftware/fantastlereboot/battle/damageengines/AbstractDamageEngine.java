/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.damageengines;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.creatures.Creature;

public abstract class AbstractDamageEngine {
    // Methods
    public abstract int computeDamage(Creature enemy,
            Creature acting);

    public abstract boolean enemyDodged();

    public abstract boolean weaponMissed();

    public abstract boolean weaponCrit();

    public abstract boolean weaponPierce();

    public abstract boolean weaponFumble();

    public static AbstractDamageEngine getPlayerInstance() {
        final int difficulty = FantastleReboot.getBagOStuff().getPrefsManager().getGameDifficulty();
        FantastleReboot.getBagOStuff().getPrefsManager();
        if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
            return new VeryEasyDamageEngine();
        } else {
            FantastleReboot.getBagOStuff().getPrefsManager();
            if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
                return new EasyDamageEngine();
            } else {
                FantastleReboot.getBagOStuff().getPrefsManager();
                if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
                    return new NormalDamageEngine();
                } else {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
                        return new HardDamageEngine();
                    } else {
                        FantastleReboot.getBagOStuff().getPrefsManager();
                        if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
                            return new VeryHardDamageEngine();
                        } else {
                            return new NormalDamageEngine();
                        }
                    }
                }
            }
        }
    }

    public static AbstractDamageEngine getEnemyInstance() {
        final int difficulty = FantastleReboot.getBagOStuff().getPrefsManager().getGameDifficulty();
        FantastleReboot.getBagOStuff().getPrefsManager();
        if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
            return new VeryHardDamageEngine();
        } else {
            FantastleReboot.getBagOStuff().getPrefsManager();
            if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
                return new HardDamageEngine();
            } else {
                FantastleReboot.getBagOStuff().getPrefsManager();
                if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
                    return new NormalDamageEngine();
                } else {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
                        return new EasyDamageEngine();
                    } else {
                        FantastleReboot.getBagOStuff().getPrefsManager();
                        if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
                            return new VeryEasyDamageEngine();
                        } else {
                            return new NormalDamageEngine();
                        }
                    }
                }
            }
        }
    }
}