package com.puttysoftware.fantastlereboot.maze;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.game.FileHooks;
import com.puttysoftware.fantastlereboot.obsolete.TallerTower;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class SuffixHandler implements SuffixIO {
    @Override
    public void readSuffix(final XDataReader reader, final int formatVersion)
            throws IOException {
        TallerTower.getApplication().getGameManager();
        FileHooks.loadGameHook(reader);
    }

    @Override
    public void writeSuffix(final XDataWriter writer) throws IOException {
        TallerTower.getApplication().getGameManager();
        FileHooks.saveGameHook(writer);
    }
}
