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
package com.puttysoftware.fantastlereboot.obsolete.maze1.generic;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.obsolete.maze1.Maze;
import com.puttysoftware.fantastlereboot.utilities.TypeConstants;

public abstract class GenericTeleport extends MazeObject {
    // Fields
    private int destRow;
    private int destCol;
    private int destFloor;
    private int destLevel;

    // Constructors
    protected GenericTeleport(final int destinationRow,
            final int destinationColumn, final int destinationFloor,
            final int destinationLevel) {
        super(false);
        this.destRow = destinationRow;
        this.destCol = destinationColumn;
        this.destFloor = destinationFloor;
        this.destLevel = destinationLevel;
    }

    protected GenericTeleport(final boolean doesAcceptPushInto) {
        super(false, false, doesAcceptPushInto, false, false, false, false,
                true, false, 0);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final GenericTeleport other = (GenericTeleport) obj;
        if (this.destRow != other.destRow) {
            return false;
        }
        if (this.destCol != other.destCol) {
            return false;
        }
        if (this.destFloor != other.destFloor) {
            return false;
        }
        if (this.destLevel != other.destLevel) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.destRow;
        hash = 67 * hash + this.destCol;
        hash = 67 * hash + this.destFloor;
        hash = 67 * hash + this.destLevel;
        return hash;
    }

    @Override
    public GenericTeleport clone() {
        final GenericTeleport copy = (GenericTeleport) super.clone();
        copy.destCol = this.destCol;
        copy.destFloor = this.destFloor;
        copy.destLevel = this.destLevel;
        copy.destRow = this.destRow;
        return copy;
    }

    // Accessor methods
    public int getDestinationRow() {
        return this.destRow;
    }

    public int getDestinationColumn() {
        return this.destCol;
    }

    public int getDestinationFloor() {
        return this.destFloor;
    }

    public int getDestinationLevel() {
        return this.destLevel;
    }

    // Transformer methods
    public void setDestinationRow(final int destinationRow) {
        this.destRow = destinationRow;
    }

    public void setDestinationColumn(final int destinationColumn) {
        this.destCol = destinationColumn;
    }

    public void setDestinationFloor(final int destinationFloor) {
        this.destFloor = destinationFloor;
    }

    public void setDestinationLevel(final int destinationLevel) {
        this.destLevel = destinationLevel;
    }

    // Scriptability
    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY,
            final ObjectInventory inv) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        app.getGameManager().updatePositionAbsolute(this.getDestinationRow(),
                this.getDestinationColumn(), this.getDestinationFloor(),
                this.getDestinationLevel());
        if (app.getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveSuccessSound();
        }
    }

    @Override
    public abstract String getName();

    @Override
    public int getLayer() {
        return Maze.LAYER_OBJECT;
    }

    @Override
    public byte getGroupID() {
        return (byte) 14;
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_TELEPORT);
    }

    @Override
    public void editorProbeHook() {
        Messager.showMessage(this.getName() + ": Destination ("
                + (this.destCol + 1) + "," + (this.destRow + 1) + ","
                + (this.destFloor + 1) + "," + (this.destLevel + 1) + ")");
    }

    @Override
    public abstract MazeObject editorPropertiesHook();

    @Override
    public void playMoveSuccessSound() {
        SoundPlayer.playSound(SoundIndex.TELEPORT);
    }

    @Override
    public boolean defersSetProperties() {
        return true;
    }

    @Override
    public boolean hasAdditionalProperties() {
        return true;
    }

    @Override
    public int getCustomProperty(final int propID) {
        switch (propID) {
        case 1:
            return this.destRow;
        case 2:
            return this.destCol;
        case 3:
            return this.destFloor;
        case 4:
            return this.destLevel;
        default:
            return MazeObject.DEFAULT_CUSTOM_VALUE;
        }
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
        switch (propID) {
        case 1:
            this.destRow = value;
            break;
        case 2:
            this.destCol = value;
            break;
        case 3:
            this.destFloor = value;
            break;
        case 4:
            this.destLevel = value;
            break;
        default:
            break;
        }
    }

    @Override
    public int getCustomFormat() {
        return 4;
    }
}