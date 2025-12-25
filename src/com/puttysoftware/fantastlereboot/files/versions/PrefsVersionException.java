package com.puttysoftware.fantastlereboot.files.versions;

public class PrefsVersionException extends VersionException {
    /**
     *
     */
    private static final long serialVersionUID = 5865177350171161530L;

    public PrefsVersionException(final int actualVersion) {
	super("Unsupported settings version found: " + actualVersion + " (expected " + PrefsVersions.LATEST
		+ " or earlier)");
    }
}
