package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.util.Properties;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.loaders.mod.ModuleLoader;

public class MusicPlayer {
  private MusicPlayer() {
    // Do nothing
  }

  private static String[] allFilenames;
  private static Properties fileExtensions;

  private static String getMusicFilename(final MusicIndex sound) {
    if (allFilenames == null && fileExtensions == null) {
      allFilenames = DataLoader.loadMusicData();
      try {
        fileExtensions = new Properties();
        fileExtensions.load(MusicPlayer.class.getResourceAsStream(
            "/assets/data/extensions/extensions.properties"));
      } catch (IOException e) {
        FantastleReboot.logError(e);
      }
    }
    String musicExt = fileExtensions.getProperty("music");
    return allFilenames[sound.ordinal()] + musicExt;
  }

  public static void playMusic(final MusicIndex sound) {
    if (sound != null && sound != MusicIndex._NONE) {
      final String filename = getMusicFilename(sound);
      try {
        new ModuleLoader().load("/assets/music/" + filename).play();
      } catch (IOException e) {
        FantastleReboot.logWarning(e);
      }
    }
  }
}