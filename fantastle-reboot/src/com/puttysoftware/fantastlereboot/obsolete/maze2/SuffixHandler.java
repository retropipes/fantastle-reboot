package com.puttysoftware.fantastlereboot.obsolete.maze2;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.obsolete.TallerTower;
import com.puttysoftware.fantastlereboot.obsolete.game2.FileHooks;
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