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
package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.generic.GenericWall;

public class DamagedWall extends GenericWall {
    // Constructors
    public DamagedWall() {
        super();
    }

    @Override
    public boolean arrowHitAction(final int locX, final int locY,
            final int locZ, final int locW, final int dirX, final int dirY,
            final int arrowType, final ObjectInventory inv) {
        this.moveFailedAction(true, locX, locY, inv);
        return false;
    }

    @Override
    public void moveFailedAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        // Destroy the wall
        final int pz = FantastleReboot.getApplication().getGameManager()
                .getPlayerManager().getPlayerLocationZ();
        final int pw = FantastleReboot.getApplication().getGameManager()
                .getPlayerManager().getPlayerLocationW();
        FantastleReboot.getApplication().getGameManager().morph(new Empty(), dirX,
                dirY, pz, pw);
        // Play move failed sound, if it's enabled
        if (FantastleReboot.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveFailedSound();
        }
    }

    @Override
    public String getName() {
        return "Damaged Wall";
    }

    @Override
    public String getPluralName() {
        return "Damaged Walls";
    }

    @Override
    public String getMoveFailedSoundName() {
        return "crack";
    }

    @Override
    public byte getObjectID() {
        return (byte) 0;
    }

    @Override
    public String getDescription() {
        return "Damaged Walls crumble to nothing when hit.";
    }
}