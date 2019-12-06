package com.puttysoftware.fantastlereboot.files.versions;

public class PrefsVersions {
  private static final int MINIMUM = 1;
  public static final int LATEST = 2;

  private PrefsVersions() {
    // Do nothing
  }

  public static boolean isCompatible(final int version) {
    return version >= MINIMUM && version <= LATEST;
  }
}