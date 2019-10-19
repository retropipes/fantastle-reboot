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
package com.puttysoftware.fantastlereboot.obsolete.objects;

import com.puttysoftware.fantastlereboot.creatures.StatConstants;

public class MinorDrainPotion extends GenericPotion {
    // Fields
    private static final int MIN_DRAIN = -1;
    private static final int MAX_DRAIN = -5;

    // Constructors
    public MinorDrainPotion() {
        super(StatConstants.STAT_CURRENT_MP, true, MinorDrainPotion.MAX_DRAIN,
                MinorDrainPotion.MIN_DRAIN);
    }

    @Override
    public String getName() {
        return "Minor Drain Potion";
    }

    @Override
    public String getPluralName() {
        return "Minor Drain Potions";
    }

    @Override
    public byte getObjectID() {
        return (byte) 11;
    }

    @Override
    public String getDescription() {
        return "Minor Drain Potions drain your magic slightly when picked up.";
    }
}