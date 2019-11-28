package com.puttysoftware.fantastlereboot.files.versions;

public class MazeVersions {
  private static final int MINIMUM = 1;
  public static final int LATEST = 1;

  private MazeVersions() {
    // Do nothing
  }

  public static boolean isCompatible(final int version) {
    return version >= MINIMUM && version <= LATEST;
  }
}