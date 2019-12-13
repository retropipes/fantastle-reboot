package com.puttysoftware.fantastlereboot.files.versions;

public class CharacterVersions {
  public static final int V2 = 2;
  public static final int LATEST = 2;
  private static final int MINIMUM = 1;

  private CharacterVersions() {
    // Do nothing
  }

  public static boolean isCompatible(final int version) {
    return version >= MINIMUM && version <= LATEST;
  }
}