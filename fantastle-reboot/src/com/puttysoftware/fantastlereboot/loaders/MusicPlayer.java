package com.puttysoftware.fantastlereboot.loaders;

import java.io.IOException;
import java.util.Properties;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.MusicGroup;
import com.puttysoftware.fantastlereboot.assets.MusicIndex;
import com.puttysoftware.fantastlereboot.gui.Prefs;
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
    if (MusicPlayer.allFilenames == null
        && MusicPlayer.fileExtensions == null) {
      MusicPlayer.allFilenames = DataLoader.loadMusicData();
      try {
        MusicPlayer.fileExtensions = new Properties();
        MusicPlayer.fileExtensions.load(MusicPlayer.class.getResourceAsStream(
            "/assets/data/extensions/extensions.properties"));
      } catch (final IOException e) {
        FantastleReboot.exception(e);
      }
    }
    final String musicExt = MusicPlayer.fileExtensions.getProperty("music");
    if (music == MusicIndex.DUNGEON || music == MusicIndex._DUNGEON_RANDOM_2
        || music == MusicIndex._DUNGEON_RANDOM_3
        || music == MusicIndex._DUNGEON_RANDOM_4) {
      // Pick random dungeon music and play it
      final int base = MusicIndex.DUNGEON.ordinal();
      final int offset = RandomRange.generate(0, 3);
      return MusicPlayer.allFilenames[base + offset] + musicExt;
    }
    return MusicPlayer.allFilenames[music.ordinal()] + musicExt;
  }

  public static void playMusic(final MusicIndex music, final MusicGroup group) {
    if (Prefs.isMusicGroupEnabled(group)) {
      if (music != null && music != MusicIndex._NONE) {
        final String filename = MusicPlayer.getMusicFilename(music);
        if (MusicPlayer.MUSIC.isPlaying()) {
          MusicPlayer.MUSIC.stopLoop();
        }
        try {
          MusicPlayer.MUSIC.load("/assets/music/" + filename).play();
        } catch (final IOException e) {
          FantastleReboot.exception(e);
        }
      }
    }
  }
}