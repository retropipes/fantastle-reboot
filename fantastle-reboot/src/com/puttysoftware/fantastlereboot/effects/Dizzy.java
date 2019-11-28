/*  Fantastle: A World-Solving Game
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
import com.puttysoftware.randomrange.RandomRange;

public class Dizzy extends Effect {
  // Fields
  private int state;
  private final RandomRange r;
  private static final int DIZZY_STATE_UDRL = 1;
  private static final int DIZZY_STATE_ULDR = 2;
  private static final int DIZZY_STATE_ULRD = 3;
  private static final int DIZZY_STATE_URDL = 4;
  private static final int DIZZY_STATE_URLD = 5;
  private static final int DIZZY_STATE_DULR = 6;
  private static final int DIZZY_STATE_DURL = 7;
  private static final int DIZZY_STATE_DLUR = 8;
  private static final int DIZZY_STATE_DLRU = 9;
  private static final int DIZZY_STATE_DRUL = 10;
  private static final int DIZZY_STATE_DRLU = 11;
  private static final int DIZZY_STATE_LDUR = 12;
  private static final int DIZZY_STATE_LDRU = 13;
  private static final int DIZZY_STATE_LUDR = 14;
  private static final int DIZZY_STATE_LURD = 15;
  private static final int DIZZY_STATE_LRDU = 16;
  private static final int DIZZY_STATE_LRUD = 17;
  private static final int DIZZY_STATE_RDLU = 18;
  private static final int DIZZY_STATE_RDUL = 19;
  private static final int DIZZY_STATE_RLDU = 20;
  private static final int DIZZY_STATE_RLUD = 21;
  private static final int DIZZY_STATE_RUDL = 22;
  private static final int DIZZY_STATE_RULD = 23;
  private static final int MIN_DIZZY_STATE = 1;
  private static final int MAX_DIZZY_STATE = 23;

  // Constructor
  public Dizzy(final int newRounds) {
    super("Dizzy", newRounds);
    this.r = new RandomRange(Dizzy.MIN_DIZZY_STATE, Dizzy.MAX_DIZZY_STATE);
  }

  @Override
  public int modifyMove1(final int arg) {
    this.state = this.r.generate();
    switch (arg) {
    case Directions.NORTH:
      switch (this.state) {
      case DIZZY_STATE_UDRL:
      case DIZZY_STATE_ULDR:
      case DIZZY_STATE_ULRD:
      case DIZZY_STATE_URDL:
      case DIZZY_STATE_URLD:
        return Directions.NORTH;
      case DIZZY_STATE_DULR:
      case DIZZY_STATE_DURL:
      case DIZZY_STATE_DLUR:
      case DIZZY_STATE_DLRU:
      case DIZZY_STATE_DRUL:
      case DIZZY_STATE_DRLU:
        return Directions.SOUTH;
      case DIZZY_STATE_LDUR:
      case DIZZY_STATE_LDRU:
      case DIZZY_STATE_LUDR:
      case DIZZY_STATE_LURD:
      case DIZZY_STATE_LRDU:
      case DIZZY_STATE_LRUD:
        return Directions.WEST;
      case DIZZY_STATE_RDLU:
      case DIZZY_STATE_RDUL:
      case DIZZY_STATE_RLDU:
      case DIZZY_STATE_RLUD:
      case DIZZY_STATE_RUDL:
      case DIZZY_STATE_RULD:
        return Directions.EAST;
      default:
        break;
      }
      break;
    case Directions.SOUTH:
      switch (this.state) {
      case DIZZY_STATE_DULR:
      case DIZZY_STATE_DURL:
      case DIZZY_STATE_LUDR:
      case DIZZY_STATE_LURD:
      case DIZZY_STATE_RUDL:
      case DIZZY_STATE_RULD:
        return Directions.NORTH;
      case DIZZY_STATE_UDRL:
      case DIZZY_STATE_LDUR:
      case DIZZY_STATE_LDRU:
      case DIZZY_STATE_RDLU:
      case DIZZY_STATE_RDUL:
        return Directions.SOUTH;
      case DIZZY_STATE_ULDR:
      case DIZZY_STATE_ULRD:
      case DIZZY_STATE_DLUR:
      case DIZZY_STATE_DLRU:
      case DIZZY_STATE_RLDU:
      case DIZZY_STATE_RLUD:
        return Directions.WEST;
      case DIZZY_STATE_URDL:
      case DIZZY_STATE_URLD:
      case DIZZY_STATE_DRUL:
      case DIZZY_STATE_DRLU:
      case DIZZY_STATE_LRDU:
      case DIZZY_STATE_LRUD:
        return Directions.EAST;
      default:
        break;
      }
      break;
    case Directions.WEST:
      switch (this.state) {
      case DIZZY_STATE_DLUR:
      case DIZZY_STATE_DRUL:
      case DIZZY_STATE_LDUR:
      case DIZZY_STATE_LRUD:
      case DIZZY_STATE_RDUL:
      case DIZZY_STATE_RLUD:
        return Directions.NORTH;
      case DIZZY_STATE_ULDR:
      case DIZZY_STATE_URDL:
      case DIZZY_STATE_LUDR:
      case DIZZY_STATE_LRDU:
      case DIZZY_STATE_RLDU:
      case DIZZY_STATE_RUDL:
        return Directions.SOUTH;
      case DIZZY_STATE_URLD:
      case DIZZY_STATE_DULR:
      case DIZZY_STATE_DRLU:
      case DIZZY_STATE_RDLU:
      case DIZZY_STATE_RULD:
        return Directions.WEST;
      case DIZZY_STATE_DURL:
      case DIZZY_STATE_DLRU:
      case DIZZY_STATE_UDRL:
      case DIZZY_STATE_ULRD:
      case DIZZY_STATE_LDRU:
      case DIZZY_STATE_LURD:
        return Directions.EAST;
      default:
        break;
      }
      break;
    case Directions.EAST:
      switch (this.state) {
      case DIZZY_STATE_DLRU:
      case DIZZY_STATE_DRLU:
      case DIZZY_STATE_LDRU:
      case DIZZY_STATE_LRDU:
      case DIZZY_STATE_RDLU:
      case DIZZY_STATE_RLDU:
        return Directions.NORTH;
      case DIZZY_STATE_ULRD:
      case DIZZY_STATE_URLD:
      case DIZZY_STATE_LURD:
      case DIZZY_STATE_LRUD:
      case DIZZY_STATE_RLUD:
      case DIZZY_STATE_RULD:
        return Directions.SOUTH;
      case DIZZY_STATE_UDRL:
      case DIZZY_STATE_URDL:
      case DIZZY_STATE_DURL:
      case DIZZY_STATE_DRUL:
      case DIZZY_STATE_RDUL:
      case DIZZY_STATE_RUDL:
        return Directions.WEST;
      case DIZZY_STATE_ULDR:
      case DIZZY_STATE_DULR:
      case DIZZY_STATE_DLUR:
      case DIZZY_STATE_LDUR:
      case DIZZY_STATE_LUDR:
        return Directions.EAST;
      default:
        break;
      }
      break;
    case Directions.NORTHWEST:
      switch (this.state) {
      case DIZZY_STATE_UDRL:
      case DIZZY_STATE_ULDR:
      case DIZZY_STATE_ULRD:
      case DIZZY_STATE_URDL:
      case DIZZY_STATE_URLD:
        return Directions.NORTHWEST;
      case DIZZY_STATE_DULR:
      case DIZZY_STATE_DURL:
      case DIZZY_STATE_DLUR:
      case DIZZY_STATE_DLRU:
      case DIZZY_STATE_DRUL:
      case DIZZY_STATE_DRLU:
        return Directions.SOUTHEAST;
      case DIZZY_STATE_LDUR:
      case DIZZY_STATE_LDRU:
      case DIZZY_STATE_LUDR:
      case DIZZY_STATE_LURD:
      case DIZZY_STATE_LRDU:
      case DIZZY_STATE_LRUD:
        return Directions.SOUTHWEST;
      case DIZZY_STATE_RDLU:
      case DIZZY_STATE_RDUL:
      case DIZZY_STATE_RLDU:
      case DIZZY_STATE_RLUD:
      case DIZZY_STATE_RUDL:
      case DIZZY_STATE_RULD:
        return Directions.NORTHEAST;
      default:
        break;
      }
      break;
    case Directions.NORTHEAST:
      switch (this.state) {
      case DIZZY_STATE_DULR:
      case DIZZY_STATE_DURL:
      case DIZZY_STATE_LUDR:
      case DIZZY_STATE_LURD:
      case DIZZY_STATE_RUDL:
      case DIZZY_STATE_RULD:
        return Directions.NORTHWEST;
      case DIZZY_STATE_UDRL:
      case DIZZY_STATE_LDUR:
      case DIZZY_STATE_LDRU:
      case DIZZY_STATE_RDLU:
      case DIZZY_STATE_RDUL:
        return Directions.SOUTHEAST;
      case DIZZY_STATE_ULDR:
      case DIZZY_STATE_ULRD:
      case DIZZY_STATE_DLUR:
      case DIZZY_STATE_DLRU:
      case DIZZY_STATE_RLDU:
      case DIZZY_STATE_RLUD:
        return Directions.SOUTHWEST;
      case DIZZY_STATE_URDL:
      case DIZZY_STATE_URLD:
      case DIZZY_STATE_DRUL:
      case DIZZY_STATE_DRLU:
      case DIZZY_STATE_LRDU:
      case DIZZY_STATE_LRUD:
        return Directions.NORTHEAST;
      default:
        break;
      }
      break;
    case Directions.SOUTHWEST:
      switch (this.state) {
      case DIZZY_STATE_DLUR:
      case DIZZY_STATE_DRUL:
      case DIZZY_STATE_LDUR:
      case DIZZY_STATE_LRUD:
      case DIZZY_STATE_RDUL:
      case DIZZY_STATE_RLUD:
        return Directions.NORTHWEST;
      case DIZZY_STATE_ULDR:
      case DIZZY_STATE_URDL:
      case DIZZY_STATE_LUDR:
      case DIZZY_STATE_LRDU:
      case DIZZY_STATE_RLDU:
      case DIZZY_STATE_RUDL:
        return Directions.SOUTHEAST;
      case DIZZY_STATE_URLD:
      case DIZZY_STATE_DULR:
      case DIZZY_STATE_DRLU:
      case DIZZY_STATE_RDLU:
      case DIZZY_STATE_RULD:
        return Directions.SOUTHWEST;
      case DIZZY_STATE_DURL:
      case DIZZY_STATE_DLRU:
      case DIZZY_STATE_UDRL:
      case DIZZY_STATE_ULRD:
      case DIZZY_STATE_LDRU:
      case DIZZY_STATE_LURD:
        return Directions.NORTHEAST;
      default:
        break;
      }
      break;
    case Directions.SOUTHEAST:
      switch (this.state) {
      case DIZZY_STATE_DLRU:
      case DIZZY_STATE_DRLU:
      case DIZZY_STATE_LDRU:
      case DIZZY_STATE_LRDU:
      case DIZZY_STATE_RDLU:
      case DIZZY_STATE_RLDU:
        return Directions.NORTHWEST;
      case DIZZY_STATE_ULRD:
      case DIZZY_STATE_URLD:
      case DIZZY_STATE_LURD:
      case DIZZY_STATE_LRUD:
      case DIZZY_STATE_RLUD:
      case DIZZY_STATE_RULD:
        return Directions.SOUTHEAST;
      case DIZZY_STATE_UDRL:
      case DIZZY_STATE_URDL:
      case DIZZY_STATE_DURL:
      case DIZZY_STATE_DRUL:
      case DIZZY_STATE_RDUL:
      case DIZZY_STATE_RUDL:
        return Directions.SOUTHWEST;
      case DIZZY_STATE_ULDR:
      case DIZZY_STATE_DULR:
      case DIZZY_STATE_DLUR:
      case DIZZY_STATE_LDUR:
      case DIZZY_STATE_LUDR:
        return Directions.NORTHEAST;
      default:
        break;
      }
      break;
    default:
      break;
    }
    return 0;
  }
}