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
package net.worldwizard.fantastle5.objects;

import net.worldwizard.fantastle5.Application;
import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.Messager;
import net.worldwizard.fantastle5.PreferencesManager;
import net.worldwizard.fantastle5.game.ObjectInventory;
import net.worldwizard.fantastle5.generic.GenericWall;
import net.worldwizard.fantastle5.generic.MazeObject;
import net.worldwizard.fantastle5.maze.Maze;

public class ExplodingWall extends GenericWall {
    // Constructors
    public ExplodingWall() {
        super(true, true);
    }

    @Override
    public boolean preMoveAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        Messager.showMessage("BOOM!");
        return true;
    }

    @Override
    public void chainReactionAction(final int x, final int y, final int z,
            final int w) {
        // Explode this wall, and any exploding walls next to this wall as well
        final Application app = Fantastle5.getApplication();
        ExplodingWall curr = null;
        try {
            curr = (ExplodingWall) app.getMazeManager().getMazeObject(x, y, z,
                    w, Maze.LAYER_OBJECT);
        } catch (final ClassCastException cce) {
            // We're not an exploding wall, so abort
            return;
        }
        String mo2Name, mo4Name, mo6Name, mo8Name, invalidName, currName;
        invalidName = new EmptyVoid().getName();
        currName = curr.getName();
        final MazeObject mo2 = app.getMazeManager().getMazeObject(x - 1, y, z,
                w, Maze.LAYER_OBJECT);
        try {
            mo2Name = mo2.getName();
        } catch (final NullPointerException np) {
            mo2Name = invalidName;
        }
        final MazeObject mo4 = app.getMazeManager().getMazeObject(x, y - 1, z,
                w, Maze.LAYER_OBJECT);
        try {
            mo4Name = mo4.getName();
        } catch (final NullPointerException np) {
            mo4Name = invalidName;
        }
        final MazeObject mo6 = app.getMazeManager().getMazeObject(x, y + 1, z,
                w, Maze.LAYER_OBJECT);
        try {
            mo6Name = mo6.getName();
        } catch (final NullPointerException np) {
            mo6Name = invalidName;
        }
        final MazeObject mo8 = app.getMazeManager().getMazeObject(x + 1, y, z,
                w, Maze.LAYER_OBJECT);
        try {
            mo8Name = mo8.getName();
        } catch (final NullPointerException np) {
            mo8Name = invalidName;
        }
        app.getGameManager().morph(new Empty(), x, y, z, w, "BOOM!");
        if (mo2Name.equals(currName)) {
            curr.chainReactionAction(x - 1, y, z, w);
        }
        if (mo4Name.equals(currName)) {
            curr.chainReactionAction(x, y - 1, z, w);
        }
        if (mo6Name.equals(currName)) {
            curr.chainReactionAction(x, y + 1, z, w);
        }
        if (mo8Name.equals(currName)) {
            curr.chainReactionAction(x + 1, y, z, w);
        }
        if (app.getPrefsManager().getSoundEnabled(
                PreferencesManager.SOUNDS_GAME)) {
            curr.playChainReactSound();
        }
    }

    @Override
    public String getName() {
        return "Exploding Wall";
    }

    @Override
    public String getGameName() {
        return "Wall";
    }

    @Override
    public String getPluralName() {
        return "Exploding Walls";
    }

    @Override
    public byte getObjectID() {
        return (byte) 2;
    }

    @Override
    public String getDescription() {
        return "Exploding Walls explode when touched, causing other Exploding Walls nearby to also explode.";
    }
}