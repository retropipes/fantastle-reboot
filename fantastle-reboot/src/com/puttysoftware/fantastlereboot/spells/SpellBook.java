/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.spells;

import java.util.ArrayList;
import java.util.Objects;

public abstract class SpellBook {
    // Fields
    private final ArrayList<Spell> spells;
    private final ArrayList<Boolean> known;

    // Constructors
    protected SpellBook() {
        super();
        this.spells = new ArrayList<>();
        this.known = new ArrayList<>();
    }

    protected void addKnownSpell(final Spell sp) {
        this.spells.add(sp);
        this.known.add(true);
    }

    protected void addUnknownSpell(final Spell sp) {
        this.spells.add(sp);
        this.known.add(false);
    }

    public abstract int getID();

    public final int getSpellCount() {
        return this.spells.size();
    }

    public final boolean isSpellKnown(final int ID) {
        return this.known.get(ID);
    }

    public final Spell getSpellByID(final int ID) {
        return this.spells.get(ID);
    }

    public final int getSpellsKnownCount() {
        int k = 0;
        for (final boolean element : this.known) {
            if (element) {
                k++;
            }
        }
        return k;
    }

    public final int getMaximumSpellsKnownCount() {
        return this.known.size();
    }

    public String getNextSpellToLearnName() {
        final int numKnown = this.getSpellsKnownCount();
        final int max = this.getMaximumSpellsKnownCount();
        if (numKnown == max) {
            return null;
        } else {
            return this.spells.get(numKnown).getEffect().getName();
        }
    }

    public final String[] getAllSpellsToLearnNames() {
        final int numKnown = this.getSpellsKnownCount();
        final int max = this.getMaximumSpellsKnownCount();
        if (numKnown == max) {
            return null;
        } else {
            int counter = 0;
            final String[] res = new String[max - numKnown];
            final int spellSize = this.spells.size();
            for (int x = 0; x < spellSize; x++) {
                if (!this.known.get(x)) {
                    res[counter] = this.spells.get(x).getEffect().getName();
                    counter++;
                }
            }
            return res;
        }
    }

    final Spell getSpellByName(final String sname) {
        int x;
        final int spellSize = this.spells.size();
        for (x = 0; x < spellSize; x++) {
            final String currName = this.spells.get(x).getEffect().getName();
            if (currName.equals(sname)) {
                // Found it
                return this.spells.get(x);
            }
        }
        // Didn't find it
        return null;
    }

    public final void learnSpellByID(final int ID) {
        if (ID != -1) {
            this.known.set(ID, true);
        }
    }

    public final void learnAllSpells() {
        final int spellSize = this.spells.size();
        for (int x = 0; x < spellSize; x++) {
            this.known.set(x, true);
        }
    }

    public final void forgetAllSpells() {
        final int spellSize = this.spells.size();
        for (int x = 0; x < spellSize; x++) {
            this.known.set(x, false);
        }
    }

    final String[] getAllSpellNames() {
        int x;
        int k = 0;
        String[] names;
        final int spellSize = this.spells.size();
        final ArrayList<String> tempnames = new ArrayList<>();
        for (x = 0; x < spellSize; x++) {
            if (this.known.get(x)) {
                tempnames.add(this.spells.get(x).getEffect().getName());
                k++;
            }
        }
        if (k != 0) {
            names = new String[k];
            k = 0;
            for (x = 0; x < spellSize; x++) {
                if (this.known.get(x)) {
                    names[k] = this.spells.get(x).getEffect().getName();
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
        final int spellSize = this.spells.size();
        final ArrayList<String> tempnames = new ArrayList<>();
        for (x = 0; x < spellSize; x++) {
            if (this.known.get(x)) {
                tempnames.add(this.spells.get(x).getEffect().getName());
                k++;
            }
        }
        if (k != 0) {
            names = new String[k];
            k = 0;
            for (x = 0; x < spellSize; x++) {
                if (this.known.get(x)) {
                    names[k] = this.spells.get(x).getEffect().getName();
                    k++;
                }
            }
        } else {
            names = null;
        }
        if (names != null) {
            k = 0;
            for (x = 0; x < spellSize; x++) {
                if (this.known.get(x)) {
                    final int cost = this.spells.get(x).getCost();
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
        final int spellSize = this.spells.size();
        for (x = 0; x < spellSize; x++) {
            if (this.known.get(x)) {
                k++;
            }
        }
        if (k != 0) {
            costs = new int[k];
            k = 0;
            for (x = 0; x < spellSize; x++) {
                if (this.known.get(x)) {
                    costs[k] = this.spells.get(x).getCost();
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
        final int spellSize = this.spells.size();
        for (x = 0; x < spellSize; x++) {
            final String currName = this.spells.get(x).getEffect().getName();
            if (currName.equals(sname)) {
                // Found it
                return x;
            }
        }
        // Didn't find it
        return -1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.known, this.spells);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SpellBook)) {
            return false;
        }
        SpellBook other = (SpellBook) obj;
        return Objects.equals(this.known, other.known)
                && Objects.equals(this.spells, other.spells);
    }
}
