/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.fantastlereboot.loaders.ogg;

import java.net.URL;

public abstract class OggLoader extends Thread {
  // Constants
  protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb
  private static int ACTIVE_MEDIA_COUNT = 0;
  private static int MAX_MEDIA_ACTIVE = 5;
  private static OggLoader[] ACTIVE_MEDIA = new OggLoader[OggLoader.MAX_MEDIA_ACTIVE];
  private static ThreadGroup MEDIA_GROUP = new ThreadGroup("Ogg Media Players");
  private static OggExceptionHandler meh = new OggExceptionHandler();

  // Constructor
  protected OggLoader(final ThreadGroup group) {
    super(group, "Ogg Media Player " + OggLoader.ACTIVE_MEDIA_COUNT);
  }

  // Methods
  public abstract void stopLoop();

  public abstract boolean isPlaying();

  protected abstract void updateNumber(int newNumber);

  abstract int getNumber();

  // Factories
  public static OggLoader loadFile(final String file) {
    return OggLoader.provisionMedia(new OggFile(OggLoader.MEDIA_GROUP, file,
        OggLoader.ACTIVE_MEDIA_COUNT));
  }

  public static OggLoader loadResource(final URL resource) {
    return OggLoader.provisionMedia(new OggResource(OggLoader.MEDIA_GROUP,
        resource, OggLoader.ACTIVE_MEDIA_COUNT));
  }

  private static OggLoader provisionMedia(final OggLoader src) {
    if (OggLoader.ACTIVE_MEDIA_COUNT >= OggLoader.MAX_MEDIA_ACTIVE) {
      OggLoader.killAllMediaPlayers();
    }
    try {
      if (src != null) {
        src.setUncaughtExceptionHandler(OggLoader.meh);
        OggLoader.ACTIVE_MEDIA[OggLoader.ACTIVE_MEDIA_COUNT] = src;
        OggLoader.ACTIVE_MEDIA_COUNT++;
      }
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      // Do nothing
    }
    return src;
  }

  private static void killAllMediaPlayers() {
    OggLoader.MEDIA_GROUP.interrupt();
  }

  static synchronized void taskCompleted(final int taskNum) {
    OggLoader.ACTIVE_MEDIA[taskNum] = null;
    for (int z = taskNum + 1; z < OggLoader.ACTIVE_MEDIA.length; z++) {
      if (OggLoader.ACTIVE_MEDIA[z] != null) {
        OggLoader.ACTIVE_MEDIA[z - 1] = OggLoader.ACTIVE_MEDIA[z];
        if (OggLoader.ACTIVE_MEDIA[z - 1].isAlive()) {
          OggLoader.ACTIVE_MEDIA[z - 1].updateNumber(z - 1);
        }
      }
    }
    OggLoader.ACTIVE_MEDIA_COUNT--;
  }
}
