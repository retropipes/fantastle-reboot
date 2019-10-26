package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;

public class MonsterObjectFactory {
  private MonsterObjectFactory() {
    // Do nothing
  }

  public static MonsterObject createMonster() {
    PreferencesManager prefs = FantastleReboot.getBagOStuff().getPrefsManager();
    if (prefs.monstersVisible()) {
      return new VisibleMonsterObject();
    } else {
      return new InvisibleMonsterObject();
    }
  }
}
