package com.puttysoftware.fantastlereboot.files;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;

import com.puttysoftware.fantastlereboot.files.versions.WorldVersionException;
import com.puttysoftware.fantastlereboot.files.versions.WorldVersions;

public class PrefixHandler implements PrefixIO {
    @Override
    public int readPrefix(final DataIOReader reader) throws IOException {
	final int formatVer = PrefixHandler.readFormatVersion(reader);
	final boolean res = WorldVersions.isCompatible(formatVer);
	if (!res) {
	    throw new WorldVersionException(formatVer);
	}
	return formatVer;
    }

    @Override
    public void writePrefix(final DataIOWriter writer) throws IOException {
	PrefixHandler.writeFormatVersion(writer);
    }

    private static int readFormatVersion(final DataIOReader reader) throws IOException {
	return reader.readInt();
    }

    private static void writeFormatVersion(final DataIOWriter writer) throws IOException {
	writer.writeInt(WorldVersions.LATEST);
    }
}
