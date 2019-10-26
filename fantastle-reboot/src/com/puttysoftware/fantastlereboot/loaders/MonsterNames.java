package com.puttysoftware.fantastlereboot.loaders;

public class MonsterNames {
  // Package-Protected Constants
  private static String[] MONSTER_NAMES = null;

  public static final String[] getAllNames() {
    if (MONSTER_NAMES == null) {
      MONSTER_NAMES = DataLoader.loadMonsterData();
    }
    return MONSTER_NAMES;
  }

  // Private constructor
  private MonsterNames() {
    // Do nothing
  }
}