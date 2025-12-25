package com.puttysoftware.fantastlereboot.files;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;

public interface SuffixIO {
    void writeSuffix(DataIOWriter writer) throws IOException;

    void readSuffix(DataIOReader reader, int formatVersion) throws IOException;
}
