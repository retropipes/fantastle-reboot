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

import java.util.Arrays;

import com.puttysoftware.fantastlereboot.utilities.DirectionConstants;
import com.puttysoftware.fantastlereboot.utilities.DirectionResolver;

class SolidProperties {
    // Properties
    private final boolean[] solidX;
    private final boolean[] solidI;

    // Constructors
    public SolidProperties() {
        this.solidX = new boolean[DirectionConstants.DIRECTION_COUNT];
        this.solidI = new boolean[DirectionConstants.DIRECTION_COUNT];
    }

    // Methods
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SolidProperties other = (SolidProperties) obj;
        if (!Arrays.equals(this.solidX, other.solidX)) {
            return false;
        }
        if (!Arrays.equals(this.solidI, other.solidI)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.hashCode(this.solidX);
        hash = 89 * hash + Arrays.hashCode(this.solidI);
        return hash;
    }

    @Override
    public SolidProperties clone() {
        final SolidProperties copy = new SolidProperties();
        System.arraycopy(this.solidX, 0, copy.solidX, 0, this.solidX.length);
        System.arraycopy(this.solidI, 0, copy.solidI, 0, this.solidI.length);
        return copy;
    }

    public boolean isSolid() {
        boolean result = false;
        for (int x = 0; x < DirectionConstants.DIRECTION_COUNT; x++) {
            result = result || this.solidX[x];
            result = result || this.solidI[x];
        }
        return result;
    }

    public boolean isDirectionallySolid(final boolean ie, final int dirX,
            final int dirY) {
        final int dir = DirectionResolver.resolveRelativeDirection(dirX, dirY);
        if (ie) {
            try {
                if (dir != DirectionConstants.DIRECTION_NONE) {
                    return this.solidX[dir];
                } else {
                    return false;
                }
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                return true;
            }
        } else {
            try {
                if (dir != DirectionConstants.DIRECTION_NONE) {
                    return this.solidI[dir];
                } else {
                    return false;
                }
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                return true;
            }
        }
    }

    public void setSolid(final boolean value) {
        for (int x = 0; x < DirectionConstants.DIRECTION_COUNT; x++) {
            this.solidX[x] = value;
            this.solidI[x] = value;
        }
    }

    public void setDirectionallySolid(final boolean ie, final int dir,
            final boolean value) {
        if (ie) {
            try {
                this.solidX[dir] = value;
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        } else {
            try {
                this.solidI[dir] = value;
            } catch (final ArrayIndexOutOfBoundsException aioob) {
                // Do nothing
            }
        }
    }
}
