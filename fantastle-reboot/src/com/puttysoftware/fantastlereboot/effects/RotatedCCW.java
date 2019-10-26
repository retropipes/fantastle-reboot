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

import com.puttysoftware.diane.utilties.Directions;

public class RotatedCCW extends Effect {
  // Constructor
  public RotatedCCW(final int newRounds) {
    super("Rotated CCW", newRounds);
  }

  @Override
  public int modifyMove1(final int arg) {
    switch (arg) {
    case Directions.NORTH:
      return Directions.WEST;
    case Directions.SOUTH:
      return Directions.EAST;
    case Directions.WEST:
      return Directions.SOUTH;
    case Directions.EAST:
      return Directions.NORTH;
    case Directions.NORTHWEST:
      return Directions.SOUTHWEST;
    case Directions.NORTHEAST:
      return Directions.NORTHWEST;
    case Directions.SOUTHWEST:
      return Directions.SOUTHEAST;
    case Directions.SOUTHEAST:
      return Directions.NORTHEAST;
    default:
      break;
    }
    return 0;
  }
}