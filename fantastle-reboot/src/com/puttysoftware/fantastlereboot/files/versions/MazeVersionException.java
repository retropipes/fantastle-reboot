package com.puttysoftware.fantastlereboot.files.versions;

@SuppressWarnings("serial")
public class MazeVersionException extends VersionException {
  public MazeVersionException(final int actualVersion) {
    super("Unsupported maze version found: " + actualVersion + " (expected "
        + MazeVersions.LATEST + " or earlier)");
  }
}
