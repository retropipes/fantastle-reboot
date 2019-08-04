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
package net.worldwizard.fantastle5.items.combat;

public class CombatItemList {
    // Fields
    private final CombatUsableItem[] allItems = { new Bomb(), new Rope() };

    // Methods
    public CombatUsableItem[] getAllItems() {
        return this.allItems;
    }

    // Methods
    public String[] getAllNames() {
        final String[] allNames = new String[this.allItems.length];
        for (int x = 0; x < this.allItems.length; x++) {
            allNames[x] = this.allItems[x].getName();
        }
        return allNames;
    }

    public int[] getAllInitialUses() {
        final int[] allUses = new int[this.allItems.length];
        for (int x = 0; x < this.allItems.length; x++) {
            allUses[x] = this.allItems[x].getInitialUses();
        }
        return allUses;
    }

    public CombatUsableItem getItemByName(final String name) {
        for (final CombatUsableItem allItem : this.allItems) {
            if (name.equals(allItem.getName())) {
                return allItem;
            }
        }
        return null;
    }
}
