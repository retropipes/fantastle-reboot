package com.puttysoftware.fantastlereboot.files.versions;

@SuppressWarnings("serial")
public class WorldVersionException extends VersionException {
  public WorldVersionException(final int actualVersion) {
    super("Unsupported world version found: " + actualVersion + " (expected "
        + WorldVersions.LATEST + " or earlier)");
  }
}
