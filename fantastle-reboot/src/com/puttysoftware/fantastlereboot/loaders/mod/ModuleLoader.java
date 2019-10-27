package com.puttysoftware.fantastlereboot.loaders.mod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class ModuleLoader {
  private static final int SAMPLE_RATE = 41000;
  private Module module;
  IBXM ibxm;
  volatile boolean playing;
  private int interpolation;
  private Thread playThread;

  public ModuleLoader() {
    super();
  }

  public boolean isPlayerAlive() {
    return this.playThread != null && this.playThread.isAlive();
  }

  public synchronized ModuleLoader load(final String modRes)
      throws IOException {
    byte[] moduleData = null;
    try (
        final InputStream is = ModuleLoader.class.getResourceAsStream(modRes)) {
      try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
        // Copy the bits
        final byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
          os.write(buf, 0, len);
        }
        moduleData = os.toByteArray();
        this.module = new Module(moduleData);
        this.ibxm = new IBXM(this.module, ModuleLoader.SAMPLE_RATE);
        this.ibxm.setInterpolation(this.interpolation);
      }
    }
    return this;
  }

  public synchronized void play() {
    if (this.ibxm != null) {
      this.playing = true;
      this.playThread = new Thread(new Runnable() {
        @Override
        public void run() {
          final int[] mixBuf = new int[ModuleLoader.this.ibxm
              .getMixBufferLength()];
          final byte[] outBuf = new byte[mixBuf.length * 4];
          AudioFormat audioFormat = null;
          audioFormat = new AudioFormat(ModuleLoader.SAMPLE_RATE, 16, 2, true,
              true);
          try (SourceDataLine audioLine = AudioSystem
              .getSourceDataLine(audioFormat)) {
            audioLine.open();
            audioLine.start();
            while (ModuleLoader.this.playing) {
              final int count = ModuleLoader.this.getAudio(mixBuf);
              int outIdx = 0;
              for (int mixIdx = 0, mixEnd = count
                  * 2; mixIdx < mixEnd; mixIdx++) {
                int ampl = mixBuf[mixIdx];
                if (ampl > 32767) {
                  ampl = 32767;
                }
                if (ampl < -32768) {
                  ampl = -32768;
                }
                outBuf[outIdx++] = (byte) (ampl >> 8);
                outBuf[outIdx++] = (byte) ampl;
              }
              audioLine.write(outBuf, 0, outIdx);
            }
            audioLine.drain();
          } catch (final Exception e) {
            // Ignore
          }
        }
      });
      this.playThread.start();
    }
  }

  public synchronized boolean isPlaying() {
    return this.playing;
  }

  public synchronized void stopLoop() {
    this.playing = false;
    try {
      if (this.playThread != null) {
        this.playThread.join();
      }
    } catch (final InterruptedException e) {
    }
  }

  synchronized int getAudio(final int[] mixBuf) {
    final int count = this.ibxm.getAudio(mixBuf);
    return count;
  }
}
