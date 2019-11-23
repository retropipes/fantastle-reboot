package com.puttysoftware.fantastlereboot.ai.window;

import com.puttysoftware.fantastlereboot.gui.Prefs;

public final class WindowAIRoutinePicker {
  // Methods
  public static AbstractWindowAIRoutine getNextRoutine() {
    
    final int difficulty = Prefs
        .getGameDifficulty();
    if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
      return new VeryEasyWindowAIRoutine();
    } else {
      if (difficulty == Prefs.DIFFICULTY_EASY) {
        return new EasyWindowAIRoutine();
      } else {
        if (difficulty == Prefs.DIFFICULTY_NORMAL) {
          return new NormalWindowAIRoutine();
        } else {
          if (difficulty == Prefs.DIFFICULTY_HARD) {
            return new HardWindowAIRoutine();
          } else {
            if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
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
