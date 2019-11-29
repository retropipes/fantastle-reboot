/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ai.map;

import java.awt.Point;

import com.puttysoftware.fantastlereboot.ai.AIContext;
import com.puttysoftware.randomrange.RandomRange;

class CommonMapAIRoutines {
  // Constants
  static final int STUCK_THRESHOLD = 16;
  static final int MIN_VISION = 1;
  static final int SPELL_INDEX_HEAL = 1;

  // Constructor
  private CommonMapAIRoutines() {
    // Do nothing
  }

  static Point turnRight45(final int x, final int y) {
    if (x == -1 && y == -1) {
      return new Point(-1, 0);
    } else if (x == -1 && y == 0) {
      return new Point(-1, -1);
    } else if (x == -1 && y == 1) {
      return new Point(-1, 0);
    } else if (x == 0 && y == -1) {
      return new Point(1, -1);
    } else if (x == 0 && y == 1) {
      return new Point(-1, 1);
    } else if (x == 1 && y == -1) {
      return new Point(1, 0);
    } else if (x == 1 && y == 0) {
      return new Point(1, 1);
    } else if (x == 1 && y == 1) {
      return new Point(0, 1);
    } else {
      return new Point(x, y);
    }
  }

  static Point turnLeft45(final int x, final int y) {
    if (x == -1 && y == -1) {
      return new Point(-1, 0);
    } else if (x == -1 && y == 0) {
      return new Point(-1, 1);
    } else if (x == -1 && y == 1) {
      return new Point(0, 1);
    } else if (x == 0 && y == -1) {
      return new Point(-1, -1);
    } else if (x == 0 && y == 1) {
      return new Point(1, 1);
    } else if (x == 1 && y == -1) {
      return new Point(0, -1);
    } else if (x == 1 && y == 0) {
      return new Point(1, -1);
    } else if (x == 1 && y == 1) {
      return new Point(0, -1);
    } else {
      return new Point(x, y);
    }
  }

  static int getMaxCastIndex(final AIContext ac) {
    final int currMP = ac.getCharacter().getCreature().getCurrentMP();
    final int[] allCosts = ac.getCharacter().getCreature().getSpellBook()
        .getAllSpellCosts();
    int result = -1;
    if (currMP > 0) {
      for (int x = 0; x < allCosts.length; x++) {
        if (currMP >= allCosts[x]) {
          result = x;
        }
      }
    }
    return result;
  }

  static boolean check(final AIContext ac, final int effChance,
      final int apCost) {
    final RandomRange random = new RandomRange(1, 100);
    final int chance = random.generate();
    if (chance <= effChance) {
      if (ac.getCharacter().canAct(apCost)) {
        return true;
      } else {
        // Can't act any more times
        return false;
      }
    } else {
      // Not acting
      return false;
    }
  }
}
