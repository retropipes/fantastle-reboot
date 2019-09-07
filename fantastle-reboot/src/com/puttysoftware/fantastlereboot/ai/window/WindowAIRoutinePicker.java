package com.puttysoftware.fantastlereboot.ai.window;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;

public final class WindowAIRoutinePicker {
    // Methods
    public static AbstractWindowAIRoutine getNextRoutine() {
        final int difficulty = FantastleReboot.getBagOStuff().getPrefsManager().getGameDifficulty();
        FantastleReboot.getBagOStuff().getPrefsManager();
        if (difficulty == PreferencesManager.DIFFICULTY_VERY_EASY) {
            return new VeryEasyWindowAIRoutine();
        } else {
            FantastleReboot.getBagOStuff().getPrefsManager();
            if (difficulty == PreferencesManager.DIFFICULTY_EASY) {
                return new EasyWindowAIRoutine();
            } else {
                FantastleReboot.getBagOStuff().getPrefsManager();
                if (difficulty == PreferencesManager.DIFFICULTY_NORMAL) {
                    return new NormalWindowAIRoutine();
                } else {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    if (difficulty == PreferencesManager.DIFFICULTY_HARD) {
                        return new HardWindowAIRoutine();
                    } else {
                        FantastleReboot.getBagOStuff().getPrefsManager();
                        if (difficulty == PreferencesManager.DIFFICULTY_VERY_HARD) {
                            return new VeryHardWindowAIRoutine();
                        } else {
                            return new NormalWindowAIRoutine();
                        }
                    }
                }
            }
        }
    }
}
