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
package com.puttysoftware.fantastlereboot.effects;

import com.puttysoftware.fantastlereboot.generic.DirectionConstants;

public class RotatedCW extends Effect {
    // Constructor
    public RotatedCW(final int newRounds) {
        super("Rotated CW", newRounds);
    }

    @Override
    public int modifyMove1(final int arg) {
        switch (arg) {
        case DirectionConstants.DIRECTION_NORTH:
            return DirectionConstants.DIRECTION_EAST;
        case DirectionConstants.DIRECTION_SOUTH:
            return DirectionConstants.DIRECTION_WEST;
        case DirectionConstants.DIRECTION_WEST:
            return DirectionConstants.DIRECTION_NORTH;
        case DirectionConstants.DIRECTION_EAST:
            return DirectionConstants.DIRECTION_SOUTH;
        case DirectionConstants.DIRECTION_NORTHWEST:
            return DirectionConstants.DIRECTION_NORTHEAST;
        case DirectionConstants.DIRECTION_NORTHEAST:
            return DirectionConstants.DIRECTION_SOUTHEAST;
        case DirectionConstants.DIRECTION_SOUTHWEST:
            return DirectionConstants.DIRECTION_NORTHWEST;
        case DirectionConstants.DIRECTION_SOUTHEAST:
            return DirectionConstants.DIRECTION_SOUTHWEST;
        default:
            break;
        }
        return 0;
    }
}