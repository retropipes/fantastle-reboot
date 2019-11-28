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

import com.puttysoftware.randomrange.RandomRange;

public class Drunk extends Effect {
  // Constructor
  public Drunk(final int newRounds) {
    super("Drunk", newRounds);
  }

  @Override
  public int[] modifyMove2(final int[] arg) {
    final RandomRange rx = new RandomRange(0, 1);
    final RandomRange ry = new RandomRange(0, 1);
    arg[0] += rx.generate();
    arg[1] += ry.generate();
    return arg;
  }
}