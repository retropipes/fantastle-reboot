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

import com.puttysoftware.randomrange.RandomRange;

import net.worldwizard.fantastle5.Application;
import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.PreferencesManager;
import net.worldwizard.fantastle5.game.ObjectInventory;
import net.worldwizard.fantastle5.generic.GenericTrap;

public class WarpTrap extends GenericTrap {
    // Fields
    private RandomRange rr, rc, rf;

    // Constructors
    public WarpTrap() {
        super();
    }

    @Override
    public String getName() {
        return "Warp Trap";
    }

    @Override
    public String getPluralName() {
        return "Warp Traps";
    }

    @Override
    public byte getObjectID() {
        return (byte) 9;
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        final Application app = Fantastle5.getApplication();
        int pw, maxRow, maxCol, maxFloor, rRow, rCol, rFloor;
        pw = app.getGameManager().getPlayerManager().getPlayerLocationW();
        maxRow = app.getMazeManager().getMaze().getRows(pw) - 1;
        this.rr = new RandomRange(0, maxRow);
        maxCol = app.getMazeManager().getMaze().getColumns(pw) - 1;
        this.rc = new RandomRange(0, maxCol);
        maxFloor = app.getMazeManager().getMaze().getFloors(pw) - 1;
        this.rf = new RandomRange(0, maxFloor);
        do {
            rRow = this.rr.generate();
            rCol = this.rc.generate();
            rFloor = this.rf.generate();
        } while (!app.getGameManager().tryUpdatePositionAbsolute(rRow, rCol,
                rFloor));
        app.getGameManager().updatePositionAbsolute(rRow, rCol, rFloor, pw);
        if (Fantastle5.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveSuccessSound();
        }
    }

    @Override
    public String getDescription() {
        return "Warp Traps send anything that steps on one to a random location.";
    }

    @Override
    public String getMoveSuccessSoundName() {
        return "teleport";
    }
}
