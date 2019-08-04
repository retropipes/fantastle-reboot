/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package net.worldwizard.fantastle5.resourcemanagers;

import java.net.URL;
import java.nio.BufferUnderflowException;

import com.puttysoftware.audio.wav.WAVFactory;

import net.worldwizard.fantastle5.Fantastle5;

public class SoundManager {
    private static WAVFactory getSound(final String filename) {
        // Get it from the cache
        return SoundCache.getCachedSound(filename);
    }

    static WAVFactory getUncachedSound(final String filename) {
        try {
            final URL url = SoundManager.class
                    .getResource("/net/worldwizard/fantastle5/resources/sounds/"
                            + filename.toLowerCase() + ".wav");
            final WAVFactory snd = WAVFactory.loadResource(url);
            return snd;
        } catch (final NullPointerException np) {
            return null;
        }
    }

    public static void playSoundAsynchronously(final String soundName) {
        final WAVFactory snd = SoundManager.getSound(soundName);
        if (snd != null) {
            // Play the sound asynchronously
            try {
                snd.start();
            } catch (final BufferUnderflowException bue) {
                // Ignore
            } catch (final NullPointerException np) {
                // Ignore
            } catch (final Throwable t) {
                Fantastle5.logError(t);
            }
        }
    }

    public static void playSoundSynchronously(final String soundName) {
        final WAVFactory snd = SoundManager.getSound(soundName);
        if (snd != null) {
            // Play the sound synchronously
            try {
                snd.start();
            } catch (final BufferUnderflowException bue) {
                // Ignore
            } catch (final NullPointerException np) {
                // Ignore
            } catch (final Throwable t) {
                Fantastle5.logError(t);
            }
        }
    }
}