package com.puttysoftware.fantastlereboot.game;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;

import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;

public class FileHooks {
    private FileHooks() {
	// Do nothing
    }

    public static void loadGameHook(final DataIOReader mapFile) throws IOException {
	PartyManager.loadGameHook(mapFile);
    }

    public static void saveGameHook(final DataIOWriter mapFile) throws IOException {
	PartyManager.saveGameHook(mapFile);
    }
}
