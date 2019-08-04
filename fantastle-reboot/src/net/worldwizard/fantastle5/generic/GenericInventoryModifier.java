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
package net.worldwizard.fantastle5.generic;

import net.worldwizard.fantastle5.game.ObjectInventory;
import net.worldwizard.fantastle5.maze.Maze;

public abstract class GenericInventoryModifier extends MazeObject {
    // Constructors
    protected GenericInventoryModifier() {
        super(false);
    }

    @Override
    public abstract void postMoveAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv);

    @Override
    public byte getGroupID() {
        return (byte) 23;
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_INVENTORY_MODIFIER);
    }

    @Override
    public int getLayer() {
        return Maze.LAYER_OBJECT;
    }

    @Override
    public String getMoveSuccessSoundName() {
        return "grab";
    }

    @Override
    public int getCustomProperty(final int propID) {
        return MazeObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
        // Do nothing
    }
}
