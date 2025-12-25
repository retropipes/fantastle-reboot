package com.puttysoftware.fantastlereboot.files;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;

import com.puttysoftware.fantastlereboot.game.FileHooks;

public class SuffixHandler implements SuffixIO {
    @Override
    public void readSuffix(final DataIOReader reader, final int formatVersion) throws IOException {
	FileHooks.loadGameHook(reader);
    }

    @Override
    public void writeSuffix(final DataIOWriter writer) throws IOException {
	FileHooks.saveGameHook(writer);
    }
}
