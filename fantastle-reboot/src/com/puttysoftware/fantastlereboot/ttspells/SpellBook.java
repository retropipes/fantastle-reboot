/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttspells;

import java.util.Arrays;

public class SpellBook {
    // Fields
    private String name;
    protected final Spell[] spells;
    private final boolean[] known;

    // Constructors
    protected SpellBook(final int numSpells, final boolean flag) {
        super();
        this.name = "No Name";
        this.spells = new Spell[numSpells];
        this.known = new boolean[numSpells];
        if (flag) {
            this.learnAllSpells();
        } else {
            this.forgetAllSpells();
        }
        this.defineSpells();
    }

    protected void defineSpells() {
        // Do nothing
    }

    public int getLegacyID() {
        return -1;
    }

    public final int getSpellCount() {
        return this.spells.length;
    }

    public final boolean isSpellKnown(final int ID) {
        return this.known[ID];
    }

    public final Spell getSpellByID(final int ID) {
        return this.spells[ID];
    }

    public final int getSpellsKnownCount() {
        int k = 0;
        for (int x = 0; x < this.known.length; x++) {
            if (this.known[x]) {
                k++;
            }
        }
        return k;
    }

    public final int getMaximumSpellsKnownCount() {
        return this.known.length;
    }

    public final String[] getAllSpellsToLearnNames() {
        final int numKnown = this.getSpellsKnownCount();
        final int max = this.getMaximumSpellsKnownCount();
        if (numKnown == max) {
            return null;
        } else {
            int counter = 0;
            final String[] res = new String[max - numKnown];
            for (int x = 0; x < this.spells.length; x++) {
                if (!this.known[x]) {
                    res[counter] = this.spells[x].getEffect().getName();
                    counter++;
                }
            }
            return res;
        }
    }

    final Spell getSpellByName(final String sname) {
        int x;
        for (x = 0; x < this.spells.length; x++) {
            final String currName = this.spells[x].getEffect().getName();
            if (currName.equals(sname)) {
                // Found it
                return this.spells[x];
            }
        }
        // Didn't find it
        return null;
    }

    public final void learnSpellByID(final int ID) {
        if (ID != -1) {
            this.known[ID] = true;
        }
    }

    public final void learnAllSpells() {
        for (int x = 0; x < this.spells.length; x++) {
            this.known[x] = true;
        }
    }

    private final void forgetAllSpells() {
        for (int x = 0; x < this.spells.length; x++) {
            this.known[x] = false;
        }
    }

    final String[] getAllSpellNames() {
        int x;
        int k = 0;
        String[] names;
        final String[] tempnames = new String[this.spells.length];
        for (x = 0; x < this.spells.length; x++) {
            if (this.known[x]) {
                tempnames[x] = this.spells[x].getEffect().getName();
                k++;
            }
        }
        if (k != 0) {
            names = new String[k];
            k = 0;
            for (x = 0; x < this.spells.length; x++) {
                if (this.known[x]) {
                    names[k] = this.spells[x].getEffect().getName();
                    k++;
                }
            }
        } else {
            names = null;
        }
        return names;
    }

    final String[] getAllSpellNamesWithCosts() {
        int x;
        int k = 0;
        String[] names;
        final String[] tempnames = new String[this.spells.length];
        for (x = 0; x < this.spells.length; x++) {
            if (this.known[x]) {
                tempnames[x] = this.spells[x].getEffect().getName();
                k++;
            }
        }
        if (k != 0) {
            names = new String[k];
            k = 0;
            for (x = 0; x < this.spells.length; x++) {
                if (this.known[x]) {
                    names[k] = this.spells[x].getEffect().getName();
                    k++;
                }
            }
        } else {
            names = null;
        }
        if (names != null) {
            k = 0;
            for (x = 0; x < this.spells.length; x++) {
                if (this.known[x]) {
                    final int cost = this.spells[x].getCost();
                    final String costStr = Integer.toString(cost);
                    names[k] += " (" + costStr + " MP)";
                    k++;
                }
            }
        }
        return names;
    }

    public final int[] getAllSpellCosts() {
        int x;
        int k = 0;
        int[] costs;
        for (x = 0; x < this.spells.length; x++) {
            if (this.known[x]) {
                k++;
            }
        }
        if (k != 0) {
            costs = new int[k];
            k = 0;
            for (x = 0; x < this.spells.length; x++) {
                if (this.known[x]) {
                    costs[k] = this.spells[x].getCost();
                    k++;
                }
            }
        } else {
            costs = null;
        }
        return costs;
    }

    public final int getSpellIDByName(final String sname) {
        int x;
        for (x = 0; x < this.spells.length; x++) {
            final String currName = this.spells[x].getEffect().getName();
            if (currName.equals(sname)) {
                // Found it
                return x;
            }
        }
        // Didn't find it
        return -1;
    }

    public final void setName(final String n) {
        this.name = n;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.known);
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        return prime * result + Arrays.hashCode(this.spells);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SpellBook)) {
            return false;
        }
        final SpellBook other = (SpellBook) obj;
        if (!Arrays.equals(this.known, other.known)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (!Arrays.equals(this.spells, other.spells)) {
            return false;
        }
        return true;
    }
}
