/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import java.util.ArrayList;
import java.util.List;

import com.puttysoftware.fantastlereboot.creatures.monsters.MonsterFactory;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;

public class MapBattle {
  // Fields
  private final List<BattleCharacter> monsters;

  // Constructors
  public MapBattle() {
    super();
    final int monsterCount = PreferencesManager
        .getMonsterCount(PartyManager.getParty().getMembers());
    this.monsters = new ArrayList<>();
    for (int m = 0; m < monsterCount; m++) {
      this.monsters
          .add(new BattleCharacter(MonsterFactory.getNewMonsterInstance()));
    }
  }

  // Methods
  public List<BattleCharacter> getBattlers() {
    return this.monsters;
  }
}
