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

public class RotatedCW extends Effect {
  // Constructor
  public RotatedCW(final int newRounds) {
    super("Rotated CW", newRounds);
  }

  @Override
  public int modifyMove1(final int arg) {
    switch (arg) {
    case Directions.NORTH:
      return Directions.EAST;
    case Directions.SOUTH:
      return Directions.WEST;
    case Directions.WEST:
      return Directions.NORTH;
    case Directions.EAST:
      return Directions.SOUTH;
    case Directions.NORTHWEST:
      return Directions.NORTHEAST;
    case Directions.NORTHEAST:
      return Directions.SOUTHEAST;
    case Directions.SOUTHWEST:
      return Directions.NORTHWEST;
    case Directions.SOUTHEAST:
      return Directions.SOUTHWEST;
    default:
      break;
    }
    return 0;
  }
}