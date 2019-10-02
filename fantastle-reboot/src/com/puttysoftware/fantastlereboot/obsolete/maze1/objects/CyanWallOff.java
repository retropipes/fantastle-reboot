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
package com.puttysoftware.fantastlereboot.obsolete.maze1.objects;

import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.GenericToggleWall;

public class CyanWallOff extends GenericToggleWall {
    // Constructors
    public CyanWallOff() {
        super(false);
    }

    // Scriptability
    @Override
    public String getName() {
        return "Cyan Wall Off";
    }

    @Override
    public String getPluralName() {
        return "Cyan Walls Off";
    }

    @Override
    public byte getObjectID() {
        return (byte) 17;
    }

    @Override
    public String getDescription() {
        return "Cyan Walls Off can be walked through, and will change to Cyan Walls On when a Cyan Button is pressed.";
    }
}