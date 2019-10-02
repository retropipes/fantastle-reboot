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

import com.puttysoftware.fantastlereboot.utilities.TypeConstants;

public abstract class GenericTrappedWall extends GenericWall {
    // Fields
    private int number;
    protected static final int NUMBER_MASTER = -1;

    // Constructors
    protected GenericTrappedWall(final int newNumber) {
        super();
        this.number = newNumber;
    }

    public int getNumber() {
        return this.number;
    }

    @Override
    public GenericTrappedWall clone() {
        final GenericTrappedWall copy = (GenericTrappedWall) super.clone();
        copy.number = this.number;
        return copy;
    }

    @Override
    public String getName() {
        if (this.number == GenericTrappedWall.NUMBER_MASTER) {
            return "Master Trapped Wall";
        } else {
            return "Trapped Wall " + this.number;
        }
    }

    @Override
    public String getGameName() {
        return "Wall";
    }

    @Override
    public String getPluralName() {
        if (this.number == GenericTrappedWall.NUMBER_MASTER) {
            return "Master Trapped Walls";
        } else {
            return "Trapped Walls " + this.number;
        }
    }

    @Override
    public byte getGroupID() {
        return (byte) 28;
    }

    @Override
    public byte getObjectID() {
        return (byte) this.number;
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_TRAPPED_WALL);
        this.type.set(TypeConstants.TYPE_WALL);
    }
}