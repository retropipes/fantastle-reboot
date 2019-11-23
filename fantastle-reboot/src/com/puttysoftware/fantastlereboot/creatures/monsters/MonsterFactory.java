/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import java.util.ArrayList;

import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.randomrange.RandomRange;

public class MonsterFactory {
  private static final int MAX_MONSTERS = 50;

  private MonsterFactory() {
    // Do nothing
  }

  public static ArrayList<BattleCharacter> generateMapMonsters() {
    final int partyCount = PartyManager.getParty().getMembers();
    final int monsterCount = PreferencesManager.getMonsterCount(partyCount);
    final int minTeamID = Creature.TEAM_ENEMY_FIRST;
    final int maxTeamID = Creature.TEAM_ENEMY_LAST
        - ((monsterCount - MAX_MONSTERS) / 5);
    ArrayList<BattleCharacter> monsters = new ArrayList<>();
    if (PartyManager.getParty().getMonsterLevel() == Maze.getMaxLevels() - 1) {
      monsters.add(new BattleCharacter(new BossMonster()));
    } else {
      for (int m = 0; m < monsterCount; m++) {
        int teamID = RandomRange.generate(minTeamID, maxTeamID);
        monsters.add(new BattleCharacter(new Monster(teamID)));
      }
    }
    return monsters;
  }

  public static Creature generateMonster() {
    if (PartyManager.getParty().getMonsterLevel() == Maze.getMaxLevels() - 1) {
      return new BossMonster();
    } else {
      return new Monster(Creature.TEAM_ENEMY_FIRST);
    }
  }
}
