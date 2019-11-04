package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.objects.MonsterObject;

public class MonsterObjectFactory {
  private MonsterObjectFactory() {
    // Do nothing
  }

  public static MonsterObject createMonster() {
    if (PreferencesManager.monstersVisible()) {
      return new VisibleMonsterObject();
    } else {
      return new InvisibleMonsterObject();
    }
  }
}
