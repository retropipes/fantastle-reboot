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

import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.generic.GenericPassThroughObject;

public class Door extends GenericPassThroughObject {
    // Constructors
    public Door() {
        super();
    }

    // Scriptability
    @Override
    public String getName() {
        return "Door";
    }

    @Override
    public String getPluralName() {
        return "Doors";
    }

    @Override
    public byte getObjectID() {
        return (byte) 3;
    }

    @Override
    public String getDescription() {
        return "Doors are purely decorative, but they do stop arrows from passing through.";
    }

    @Override
    public boolean arrowHitAction(final int locX, final int locY,
            final int locZ, final int locW, final int dirX, final int dirY,
            final int arrowType, final ObjectInventory inv) {
        return false;
    }
}