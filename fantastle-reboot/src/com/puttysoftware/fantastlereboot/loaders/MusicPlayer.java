package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.util.Properties;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.MusicGroup;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.loaders.mod.ModuleLoader;
import com.puttysoftware.randomrange.RandomRange;

public class MusicPlayer {
  private MusicPlayer() {
    // Do nothing
  }

  private static String[] allFilenames;
  private static Properties fileExtensions;
  private static ModuleLoader MUSIC = new ModuleLoader();

  private static String getMusicFilename(final MusicIndex music) {
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
    if (music == MusicIndex.DUNGEON || music == MusicIndex._DUNGEON_RANDOM_2
        || music == MusicIndex._DUNGEON_RANDOM_3
        || music == MusicIndex._DUNGEON_RANDOM_4) {
      // Pick random dungeon music and play it
      int base = MusicIndex.DUNGEON.ordinal();
      int offset = RandomRange.generate(0, 3);
      return allFilenames[base + offset] + musicExt;
    }
    return allFilenames[music.ordinal()] + musicExt;
  }

  public static void playMusic(final MusicIndex music, final MusicGroup group) {
    PreferencesManager prefs = FantastleReboot.getBagOStuff().getPrefsManager();
    if (prefs.isMusicGroupEnabled(group)) {
      if (music != null && music != MusicIndex._NONE) {
        final String filename = getMusicFilename(music);
        if (MUSIC.isPlaying()) {
          MUSIC.stopLoop();
        }
        try {
          MUSIC.load("/assets/music/" + filename).play();
        } catch (IOException e) {
          FantastleReboot.logWarning(e);
        }
      }
    }
  }
}