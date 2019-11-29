/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.fantastlereboot.loaders.ogg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.puttysoftware.fantastlereboot.FantastleReboot;

class OggPlayer {
  private AudioInputStream stream;
  private AudioInputStream decodedStream;
  private AudioFormat format;
  private AudioFormat decodedFormat;
  private boolean stop;

  public OggPlayer(final AudioInputStream ais) {
    this.stream = ais;
    this.stop = false;
  }

  public void playLoop() throws IOException {
    byte[] oggData = null;
    final byte[] buf = new byte[4096];
    // Get AudioInputStream from given file.
    if (this.stream != null) {
      this.format = this.stream.getFormat();
      this.decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
          this.format.getSampleRate(), 16, this.format.getChannels(),
          this.format.getChannels() * 2, this.format.getSampleRate(), false);
      // Get AudioInputStream that will be decoded by underlying
      // VorbisSPI
      this.decodedStream = AudioSystem.getAudioInputStream(this.decodedFormat,
          this.stream);
      try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        // Start
        int nBytesRead = 0;
        while (nBytesRead != -1) {
          nBytesRead = this.decodedStream.read(buf, 0, buf.length);
          if (nBytesRead != -1) {
            baos.write(buf, 0, nBytesRead);
          }
        }
        oggData = baos.toByteArray();
      }
    }
    if (oggData != null) {
      try (SourceDataLine line = OggPlayer.getLine(this.decodedFormat)) {
        if (line != null) {
          // Start
          line.start();
          while (!this.stop) {
            if (this.stop) {
              break;
            }
            try (
                ByteArrayInputStream bais = new ByteArrayInputStream(oggData)) {
              int nBytesRead = 0;
              while (nBytesRead != -1) {
                nBytesRead = bais.read(buf, 0, buf.length);
                if (this.stop) {
                  break;
                }
                if (nBytesRead != -1) {
                  line.write(buf, 0, nBytesRead);
                }
                if (this.stop) {
                  break;
                }
              }
            }
            if (this.stop) {
              break;
            }
          }
          // Stop
          line.drain();
          line.stop();
        }
      } catch (LineUnavailableException lue) {
        FantastleReboot.exception(lue);
      }
    }
  }

  private static SourceDataLine getLine(AudioFormat audioFormat)
      throws LineUnavailableException {
    SourceDataLine res = null;
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
    res = (SourceDataLine) AudioSystem.getLine(info);
    res.open(audioFormat);
    return res;
  }

  public void stopLoop() {
    this.stop = true;
  }
}
