package com.puttysoftware.fantastlereboot.loaders;

import org.retropipes.diane.asset.sound.DianeSoundPlayer;

import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.gui.Prefs;

public class SoundPlayer {
    private SoundPlayer() {
	// Do nothing
    }

    public static void playSound(final SoundIndex sound, final SoundGroup group) {
	if (Prefs.isSoundGroupEnabled(group)) {
	    if (sound != null && sound != SoundIndex._NONE) {
		DianeSoundPlayer.play(sound);
	    }
	}
    }
}