/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ai.map;

import com.puttysoftware.fantastlereboot.gui.Prefs;

public final class MapAIRoutinePicker {
  // Constructors
  private MapAIRoutinePicker() {
    // Do nothing
  }

  // Methods
  public static AbstractMapAIRoutine getNextRoutine() {
    final int difficulty = Prefs.getGameDifficulty();
    if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
      return new VeryEasyMapAIRoutine();
    } else {
      if (difficulty == Prefs.DIFFICULTY_EASY) {
        return new EasyMapAIRoutine();
      } else {
        if (difficulty == Prefs.DIFFICULTY_NORMAL) {
          return new NormalMapAIRoutine();
        } else {
          if (difficulty == Prefs.DIFFICULTY_HARD) {
            return new HardMapAIRoutine();
          } else {
            if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
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
