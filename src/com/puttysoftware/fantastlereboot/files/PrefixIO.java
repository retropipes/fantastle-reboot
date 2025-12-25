package com.puttysoftware.fantastlereboot.files;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;

public interface PrefixIO {
    void writePrefix(DataIOWriter writer) throws IOException;

    int readPrefix(DataIOReader reader) throws IOException;
}
