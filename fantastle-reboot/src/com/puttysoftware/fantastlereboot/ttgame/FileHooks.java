package com.puttysoftware.fantastlereboot.ttgame;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class FileHooks {
    private FileHooks() {
        // Do nothing
    }

    public static void loadGameHook(final XDataReader mapFile)
            throws IOException {
        PartyManager.loadGameHook(mapFile);
    }

    public static void saveGameHook(final XDataWriter mapFile)
            throws IOException {
        PartyManager.saveGameHook(mapFile);
    }
}
