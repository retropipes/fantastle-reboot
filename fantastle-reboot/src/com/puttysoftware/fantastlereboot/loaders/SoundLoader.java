package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.randomrange.RandomRange;

public class SoundLoader {
    private SoundLoader() {
        // Do nothing
    }

    private static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb
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
            SoundLoader.playWAV(url);
        } catch (IOException e) {
            FantastleReboot.logError(e);
        }
    }

    private static void playWAV(final URL soundURL) throws IOException {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (soundURL != null) {
                        try (InputStream inputStream = soundURL.openStream()) {
                            try (AudioInputStream audioInputStream = AudioSystem
                                    .getAudioInputStream(inputStream)) {
                                final AudioFormat format = audioInputStream
                                        .getFormat();
                                final DataLine.Info info = new DataLine.Info(
                                        SourceDataLine.class, format);
                                try (SourceDataLine auline = (SourceDataLine) AudioSystem
                                        .getLine(info)) {
                                    auline.open(format);
                                    auline.start();
                                    int nBytesRead = 0;
                                    final byte[] abData = new byte[SoundLoader.EXTERNAL_BUFFER_SIZE];
                                    try {
                                        while (nBytesRead != -1) {
                                            nBytesRead = audioInputStream.read(
                                                    abData, 0, abData.length);
                                            if (nBytesRead >= 0) {
                                                auline.write(abData, 0,
                                                        nBytesRead);
                                            }
                                        }
                                    } catch (final IOException e) {
                                        FantastleReboot.logError(e);
                                    } finally {
                                        auline.drain();
                                    }
                                } catch (final LineUnavailableException e) {
                                    FantastleReboot.logError(e);
                                }
                            } catch (final UnsupportedAudioFileException e) {
                                FantastleReboot.logError(e);
                            }
                        }
                    }
                } catch (IOException e) {
                    FantastleReboot.logError(e);
                }
            }
        }.start();
    }
}