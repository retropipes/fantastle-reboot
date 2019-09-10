package com.puttysoftware.fantastlereboot.spells;

public final class SpellBookPage {
    // Fields
    private final Spell spell;
    private boolean known;

    // Constructor
    public SpellBookPage(final Spell sp) {
        this.spell = sp;
        this.known = false;
    }

    public Spell getSpell() {
        return this.spell;
    }

    public boolean isKnown() {
        return this.known;
    }

    public SpellBookPage learn() {
        this.known = true;
        return this;
    }

    public SpellBookPage forget() {
        this.known = false;
        return this;
    }
}
