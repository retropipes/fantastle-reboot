/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ai.window;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.randomrange.RandomRange;

class CommonWindowAIRoutines {
    // Constants
    static final int SPELL_INDEX_HEAL = 1;

    // Constructor
    private CommonWindowAIRoutines() {
        // Do nothing
    }

    static int getMaxCastIndex(final Creature c) {
        final int currMP = c.getCurrentMP();
        final int[] allCosts = c.getSpellBook().getAllSpellCosts();
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

    static boolean check(final int effChance) {
        final RandomRange random = new RandomRange(1, 100);
        final int chance = random.generate();
        if (chance <= effChance) {
            return true;
        } else {
            // Not acting
            return false;
        }
    }
}
