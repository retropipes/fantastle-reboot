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

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.obsolete.maze1.Maze;

public class PullableBlockThrice extends GenericMovableObject {
    // Constructors
    public PullableBlockThrice() {
        super(false, true);
    }

    @Override
    public String getName() {
        return "Pullable Block Thrice";
    }

    @Override
    public String getPluralName() {
        return "Pullable Blocks Thrice";
    }

    @Override
    public void pullAction(final ObjectInventory inv, final MazeObject mo,
            final int x, final int y, final int pushX, final int pushY) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        app.getGameManager().updatePulledPosition(x, y, pushX, pushY, this);
        if (app.getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            MazeObject.playPullSuccessSound();
        }
        app.getGameManager().morphOther(new PullableBlockTwice(), pushX, pushY,
                Maze.LAYER_OBJECT);
    }

    @Override
    public byte getObjectID() {
        return (byte) 9;
    }

    @Override
    public String getDescription() {
        return "Pullable Blocks Thrice can only be pulled three times, before turning into a wall.";
    }
}