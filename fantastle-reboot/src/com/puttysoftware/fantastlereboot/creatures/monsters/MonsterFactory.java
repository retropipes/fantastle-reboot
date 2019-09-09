/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.ttmaze.Maze;

public class MonsterFactory {
    private MonsterFactory() {
        // Do nothing
    }

    public static Creature getNewMonsterInstance() {
        if (PartyManager.getParty().getMonsterLevel() == Maze.getMaxLevels() - 1) {
            return new BossMonster();
        } else {
            return new BothRandomScalingStaticMonster();
        }
    }
}
