package com.puttysoftware.fantastlereboot.files.versions;

public class WorldVersionException extends VersionException {
    /**
     *
     */
    private static final long serialVersionUID = -9019684655962854211L;

    public WorldVersionException(final int actualVersion) {
	super("Unsupported world version found: " + actualVersion + " (expected " + WorldVersions.LATEST
		+ " or earlier)");
    }
}
