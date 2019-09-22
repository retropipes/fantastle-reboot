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
package com.puttysoftware.fantastlereboot.obsolete.maze1.objects;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.obsolete.maze1.Maze;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.GenericWall;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.MazeObject;

public class BreakableWallVertical extends GenericWall {
    // Constructors
    public BreakableWallVertical() {
        super(true, true);
    }

    @Override
    public void chainReactionAction(final int x, final int y, final int z,
            final int w) {
        FantastleReboot.getBagOStuff().getPrefsManager();
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playChainReactSound();
        }
        this.doChainReact(x, y, z, w);
    }

    public void doChainReact(final int x, final int y, final int z,
            final int w) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        BreakableWallVertical curr = null;
        try {
            curr = (BreakableWallVertical) app.getMazeManager().getMazeObject(x,
                    y, z, w, Maze.LAYER_OBJECT);
        } catch (final ClassCastException cce) {
            // We're not a breakable wall vertical, so abort
            return;
        }
        String mo2Name, mo8Name, invalidName, currName;
        invalidName = new EmptyVoid().getName();
        currName = curr.getName();
        final MazeObject mo2 = app.getMazeManager().getMazeObject(x, y - 1, z,
                w, Maze.LAYER_OBJECT);
        try {
            mo2Name = mo2.getName();
        } catch (final NullPointerException np) {
            mo2Name = invalidName;
        }
        final MazeObject mo8 = app.getMazeManager().getMazeObject(x, y + 1, z,
                w, Maze.LAYER_OBJECT);
        try {
            mo8Name = mo8.getName();
        } catch (final NullPointerException np) {
            mo8Name = invalidName;
        }
        app.getGameManager().morph(new Empty(), x, y, z, w);
        if (mo2Name.equals(currName)) {
            curr.doChainReact(x, y - 1, z, w);
        }
        if (mo8Name.equals(currName)) {
            curr.doChainReact(x, y + 1, z, w);
        }
    }

    @Override
    public String getName() {
        return "Breakable Wall Vertical";
    }

    @Override
    public String getGameName() {
        return "Wall";
    }

    @Override
    public String getPluralName() {
        return "Breakable Walls Vertical";
    }

    @Override
    public byte getObjectID() {
        return (byte) 0;
    }

    @Override
    public String getDescription() {
        return "Breakable Walls Vertical disintegrate when touched, causing other Breakable Walls Vertical nearby to also disintegrate.";
    }

    @Override
    public void playChainReactSound() {
        SoundLoader.playSound(GameSound.BREAK);
    }
}
