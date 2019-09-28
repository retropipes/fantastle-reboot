package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.puttysoftware.audio.wav.WAVPlayer;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.randomrange.RandomRange;

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
        if (sound == GameSound.WALK || sound == GameSound.WALK_2
                || sound == GameSound.WALK_3 || sound == GameSound.WALK_4
                || sound == GameSound.WALK_5 || sound == GameSound.WALK_6) {
            // Pick a random walk sound and play it
            int base = GameSound.WALK.ordinal();
            int offset = RandomRange.generate(0, 5);
            return allFilenames[base + offset] + soundExt;
        }
        return allFilenames[sound.ordinal()] + soundExt;
    }

    public static void playSound(final GameSound sound) {
        try {
            final String filename = getSoundFilename(sound);
            final URL url = SoundLoader.class
                    .getResource("/assets/sounds/" + filename);
            WAVPlayer.play(url);
        } catch (IOException e) {
            FantastleReboot.logError(e);
        }
    }
}