package com.puttysoftware.fantastlereboot.files.versions;

public class WorldVersions {
  private static final int MINIMUM = 1;
  public static final int LATEST = 1;

  private WorldVersions() {
    // Do nothing
  }

  public static boolean isCompatible(final int version) {
    return version >= MINIMUM && version <= LATEST;
  }
}