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

import com.puttysoftware.fantastlereboot.generic.GenericPotion;
import com.puttysoftware.fantastlereboot.oldcreatures.StatConstants;

public class MajorRegenPotion extends GenericPotion {
    // Fields
    private static final int MIN_REGEN = 6;
    private static final int MAX_REGEN = 50;

    // Constructors
    public MajorRegenPotion() {
        super(StatConstants.STAT_CURRENT_MP, true, MajorRegenPotion.MIN_REGEN,
                MajorRegenPotion.MAX_REGEN);
    }

    @Override
    public String getName() {
        return "Major Regen Potion";
    }

    @Override
    public String getPluralName() {
        return "Major Regen Potions";
    }

    @Override
    public byte getObjectID() {
        return (byte) 12;
    }

    @Override
    public String getDescription() {
        return "Major Regen Potions regenerate your magic significantly when picked up.";
    }
}