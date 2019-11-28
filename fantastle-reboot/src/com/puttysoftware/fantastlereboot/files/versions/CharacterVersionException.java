package com.puttysoftware.fantastlereboot.files.versions;

@SuppressWarnings("serial")
public class CharacterVersionException extends VersionException {
  public CharacterVersionException(final int actualVersion) {
    super("Unsupported character version found: " + actualVersion
        + " (expected " + CharacterVersions.LATEST + " or earlier)");
  }
}
