/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.loaders2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;

import com.puttysoftware.audio.mod.MicroMod;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.obsolete.maze2.Extension;
import com.puttysoftware.fantastlereboot.obsolete.maze2.Maze;
import com.puttysoftware.fileutils.FileUtilities;

public class MusicManager {
    private static final String DEFAULT_LOAD_PATH = "/com/puttysoftware/tallertower/resources/music/";
    private static String LOAD_PATH = MusicManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = MusicManager.class;
    private static MicroMod CURRENT_MUSIC;

    private static MicroMod getMusic(final String filename) {
        try {
            final File modFile = new File(Maze.getMazeTempFolder()
                    + File.separator + "MusicTemp" + File.separator + filename
                    + Extension.getMusicExtensionWithPeriod());
            if (!modFile.exists()) {
                final File modParent = modFile.getParentFile();
                if (!modParent.exists()) {
                    final boolean result = modParent.mkdirs();
                    if (!result) {
                        throw new IOException();
                    }
                }
                try (final InputStream is = MusicManager.LOAD_CLASS
                        .getResourceAsStream(MusicManager.LOAD_PATH + filename
                                + Extension.getMusicExtensionWithPeriod())) {
                    FileUtilities.copyRAMFile(is, modFile);
                }
            }
            final MicroMod mm = new MicroMod();
            mm.loadModule(modFile);
            return mm;
        } catch (final NullPointerException np) {
            return null;
        } catch (final IOException io) {
            return null;
        }
    }

    public static void playMusic(final int musicID) {
        MusicManager.CURRENT_MUSIC = MusicManager.getMusic(MusicConstants
                .getMusicName(musicID));
        if (MusicManager.CURRENT_MUSIC != null) {
            // Play the music
            MusicManager.CURRENT_MUSIC.playModule();
        }
    }

    public static void stopMusic() {
        if (MusicManager.CURRENT_MUSIC != null) {
            // Stop the music
            try {
                MusicManager.CURRENT_MUSIC.stopModule();
            } catch (final BufferUnderflowException bue) {
                // Ignore
            } catch (final NullPointerException np) {
                // Ignore
            } catch (final Throwable t) {
                FantastleReboot.logError(t);
            }
        }
    }

    public static boolean isMusicPlaying() {
        if (MusicManager.CURRENT_MUSIC != null) {
            if (MusicManager.CURRENT_MUSIC.isPlayThreadAlive()) {
                return true;
            }
        }
        return false;
    }
}