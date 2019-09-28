package com.puttysoftware.audio.wav;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.puttysoftware.fantastlereboot.FantastleReboot;

public abstract class WAVPlayer {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb

    // Constructor
    protected WAVPlayer() {
        super();
    }

    // Methods
    public static void play(final URL soundURL) throws IOException {
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
                                    final byte[] abData = new byte[WAVPlayer.EXTERNAL_BUFFER_SIZE];
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
