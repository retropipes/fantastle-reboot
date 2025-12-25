package com.puttysoftware.fantastlereboot.files.versions;

public class CharacterVersionException extends VersionException {
    /**
     *
     */
    private static final long serialVersionUID = 3035517090820820740L;

    public CharacterVersionException(final int actualVersion) {
	super("Unsupported character version found: " + actualVersion + " (expected " + CharacterVersions.LATEST
		+ " or earlier)");
    }
}
