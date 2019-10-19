/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ai.map;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;

public final class MapAIRoutinePicker {
    // Constructors
    private MapAIRoutinePicker() {
        // Do nothing
    }

    // Methods
    public static AbstractMapAIRoutine getNextRoutine() {
        final int difficulty = FantastleReboot.getBagOStuff().getPrefsManager()
                .getGameDifficulty();
        if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
            return new VeryEasyMapAIRoutine();
        } else {
            if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
                return new EasyMapAIRoutine();
            } else {
                if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
                    return new NormalMapAIRoutine();
                } else {
                    if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
                        return new HardMapAIRoutine();
                    } else {
                        if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
                            return new VeryHardMapAIRoutine();
                        } else {
                            return new NormalMapAIRoutine();
                        }
                    }
                }
            }
        }
    }
}
