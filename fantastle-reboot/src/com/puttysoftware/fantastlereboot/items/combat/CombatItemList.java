/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items.combat;

import com.puttysoftware.fantastlereboot.items.combat.predefined.Bolt;
import com.puttysoftware.fantastlereboot.items.combat.predefined.Bomb;
import com.puttysoftware.fantastlereboot.items.combat.predefined.Fireball;
import com.puttysoftware.fantastlereboot.items.combat.predefined.Potion;
import com.puttysoftware.fantastlereboot.items.combat.predefined.Rope;

public class CombatItemList {
    // Fields
    private final CombatItem[] allItems;

    // Constructor
    public CombatItemList() {
        this.allItems = new CombatItem[] { new Bomb(), new Rope(), new Bolt(),
                new Potion(), new Fireball() };
    }

    // Methods
    public CombatItem[] getAllItems() {
        return this.allItems;
    }

    public String[] getAllNames() {
        final String[] allNames = new String[this.allItems.length];
        for (int x = 0; x < this.allItems.length; x++) {
            allNames[x] = this.allItems[x].getName();
        }
        return allNames;
    }

    CombatItem getItemByName(final String name) {
        for (int x = 0; x < this.allItems.length; x++) {
            if (name.equals(this.allItems[x].getName())) {
                return this.allItems[x];
            }
        }
        return null;
    }
}
