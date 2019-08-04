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
package net.worldwizard.fantastle5.objects;

import net.worldwizard.fantastle5.creatures.StatConstants;
import net.worldwizard.fantastle5.generic.GenericPotion;

public class MinorRegenPotion extends GenericPotion {
    // Fields
    private static final int MIN_REGEN = 1;
    private static final int MAX_REGEN = 5;

    // Constructors
    public MinorRegenPotion() {
        super(StatConstants.STAT_CURRENT_MP, true, MinorRegenPotion.MIN_REGEN,
                MinorRegenPotion.MAX_REGEN);
    }

    @Override
    public String getName() {
        return "Minor Regen Potion";
    }

    @Override
    public String getPluralName() {
        return "Minor Regen Potions";
    }

    @Override
    public byte getObjectID() {
        return (byte) 10;
    }

    @Override
    public String getDescription() {
        return "Minor Regen Potions regenerate your magic slightly when picked up.";
    }
}