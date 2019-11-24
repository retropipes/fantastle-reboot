package com.puttysoftware.fantastlereboot.loaders;

public class MonsterNames {
  // Package-Protected Constants
  private static String[] MONSTER_NAMES = null;

  public static final String[] getAllNames() {
    if (MonsterNames.MONSTER_NAMES == null) {
      MonsterNames.MONSTER_NAMES = DataLoader.loadMonsterData();
    }
    return MonsterNames.MONSTER_NAMES;
  }

  // Private constructor
  private MonsterNames() {
    // Do nothing
  }
}