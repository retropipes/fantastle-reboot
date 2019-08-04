package com.puttysoftware.fantastlereboot.spells;

public abstract class SpellBook {
    // Fields
    protected Spell[] spells;
    protected boolean[] known;

    // Constructor
    protected SpellBook(final int numSpells) {
        this.spells = new Spell[numSpells];
        this.known = new boolean[numSpells];
        this.forgetAllSpells();
        this.defineSpells();
    }

    protected SpellBook(final int numSpells, final boolean flag) {
        this.spells = new Spell[numSpells];
        this.known = new boolean[numSpells];
        if (flag) {
            this.learnAllSpells();
        } else {
            this.forgetAllSpells();
        }
        this.defineSpells();
    }

    protected abstract void defineSpells();

    public abstract int getID();

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
        for (final boolean element : this.known) {
            if (element) {
                k++;
            }
        }
        return k;
    }

    public final int getMaximumSpellsKnownCount() {
        return this.known.length;
    }

    public String getNextSpellToLearnName() {
        final int numKnown = this.getSpellsKnownCount();
        final int max = this.getMaximumSpellsKnownCount();
        if (numKnown == max) {
            return null;
        } else {
            return this.spells[numKnown].getEffect().getName();
        }
    }

    public String[] getAllSpellsToLearnNames() {
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

    public final Spell getSpellByName(final String name) {
        int x;
        for (x = 0; x < this.spells.length; x++) {
            final String currName = this.spells[x].getEffect().getName();
            if (currName.equals(name)) {
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

    public final void learnSpellByName(final String name) {
        final int ID = this.getSpellIDByName(name);
        this.learnSpellByID(ID);
    }

    public final void learnAllSpells() {
        for (int x = 0; x < this.spells.length; x++) {
            this.known[x] = true;
        }
    }

    public final void forgetAllSpells() {
        for (int x = 0; x < this.spells.length; x++) {
            this.known[x] = false;
        }
    }

    public final void forgetSpellByID(final int ID) {
        if (ID != -1) {
            this.known[ID] = false;
        }
    }

    public final void forgetSpellByName(final String name) {
        final int ID = this.getSpellIDByName(name);
        this.forgetSpellByID(ID);
    }

    public final String[] getAllSpellNames() {
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

    public final String[] getAllSpellNamesWithCosts() {
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

    public final int getSpellIDByName(final String name) {
        int x;
        for (x = 0; x < this.spells.length; x++) {
            final String currName = this.spells[x].getEffect().getName();
            if (currName.equals(name)) {
                // Found it
                return x;
            }
        }
        // Didn't find it
        return -1;
    }
}
