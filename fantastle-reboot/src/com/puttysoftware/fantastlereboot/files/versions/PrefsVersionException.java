package com.puttysoftware.fantastlereboot.files.versions;

@SuppressWarnings("serial")
public class PrefsVersionException extends VersionException {
  public PrefsVersionException(final int actualVersion) {
    super("Unsupported settings version found: " + actualVersion + " (expected "
        + PrefsVersions.LATEST + " or earlier)");
  }
}
