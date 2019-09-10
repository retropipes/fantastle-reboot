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
package com.puttysoftware.fantastlereboot.obsolete.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.obsolete.game1.ObjectInventory;
import com.puttysoftware.fantastlereboot.obsolete.generic.GenericUsableObject;
import com.puttysoftware.fantastlereboot.obsolete.generic.MazeObject;
import com.puttysoftware.fantastlereboot.obsolete.maze1.Maze;

public class SmokeBomb extends GenericUsableObject {
    // Constants
    private static final int EFFECT_RADIUS = 3;
    private static final int STUN_DURATION = 10;

    // Constructors
    public SmokeBomb() {
        super(1);
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }

    @Override
    public String getPluralName() {
        return "Smoke Bombs";
    }

    @Override
    public byte getObjectID() {
        return (byte) 0;
    }

    @Override
    public String getDescription() {
        return "Smoke Bombs temporarily stun Monsters in an area of radius 3 centered on the target point.";
    }

    @Override
    public boolean arrowHitAction(final int locX, final int locY,
            final int locZ, final int locW, final int dirX, final int dirY,
            final int arrowType, final ObjectInventory inv) {
        // Act as if smoke bomb was used
        this.useAction(null, locX, locY, locZ, locW);
        // Destroy smoke bomb
        FantastleReboot.getBagOStuff().getGameManager().morph(new Empty(), locX,
                locY, locZ, locW);
        // Stop arrow
        return false;
    }

    @Override
    public void useAction(final MazeObject mo, final int x, final int y,
            final int z, final int w) {
        FantastleReboot.getBagOStuff().getPrefsManager();
        // Paralyze any monsters nearby
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playUseSound();
        }
        FantastleReboot.getBagOStuff().getMazeManager().getMaze()
                .radialScanTimerAction(x, y, z, w, Maze.LAYER_OBJECT,
                        SmokeBomb.EFFECT_RADIUS, "Monster",
                        SmokeBomb.STUN_DURATION);
    }

    @Override
    public void useHelper(final int x, final int y, final int z, final int w) {
        this.useAction(null, x, y, z, w);
    }

    @Override
    public void playUseSound() {
        SoundLoader.playSound(GameSound.EXPLODE);
    }
}