package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.puttysoftware.audio.wav.WAVFactory;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;

public class SoundLoader {
    private SoundLoader() {
        // Do nothing
    }

    private static String[] allFilenames;
    private static Properties fileExtensions;

    private static String getSoundFilename(final GameSound sound) {
        if (allFilenames == null && fileExtensions == null) {
            allFilenames = DataLoader.loadSoundData();
            try {
                fileExtensions = new Properties();
                fileExtensions.load(SoundLoader.class.getResourceAsStream(
                        "/assets/data/extensions/extensions.properties"));
            } catch (IOException e) {
                FantastleReboot.logError(e);
            }
        }
        String soundExt = fileExtensions.getProperty("sounds");
        return allFilenames[sound.ordinal()] + soundExt;
    }

    public static void playSound(final GameSound sound) {
        try {
            final String filename = getSoundFilename(sound);
            final URL url = SoundLoader.class
                    .getResource("/assets/sounds/" + filename);
            WAVFactory.playResource(url);
        } catch (IOException e) {
            FantastleReboot.logError(e);
        }
    }
}