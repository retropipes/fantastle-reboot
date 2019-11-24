/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.spells;

import java.util.ArrayList;
import java.util.Objects;

public abstract class SpellBook {
  // Fields
  private final ArrayList<SpellBookPage> pages;

  // Constructors
  protected SpellBook() {
    super();
    this.pages = new ArrayList<>();
  }

  protected final SpellBookPage addSpell(final Spell sp) {
    final SpellBookPage sbp = new SpellBookPage(sp);
    this.pages.add(sbp);
    return sbp;
  }

  protected final void addKnownSpell(final Spell sp) {
    this.pages.add(new SpellBookPage(sp).learn());
  }

  protected final void addUnknownSpell(final Spell sp) {
    this.pages.add(new SpellBookPage(sp).forget());
  }

  public abstract int getID();

  public final int getSpellCount() {
    return this.pages.size();
  }

  public final boolean isSpellKnown(final int ID) {
    return this.pages.get(ID).isKnown();
  }

  public final Spell getSpellByID(final int ID) {
    return this.pages.get(ID).getSpell();
  }

  public final int getSpellsKnownCount() {
    int k = 0;
    for (final SpellBookPage element : this.pages) {
      if (element.isKnown()) {
        k++;
      }
    }
    return k;
  }

  public String getNextSpellToLearnName() {
    final int numKnown = this.getSpellsKnownCount();
    final int max = this.getSpellCount();
    if (numKnown == max) {
      return null;
    } else {
      return this.pages.get(numKnown).getSpell().getEffect().getName();
    }
  }

  public final String[] getAllSpellsToLearnNames() {
    final int numKnown = this.getSpellsKnownCount();
    final int max = this.getSpellCount();
    if (numKnown == max) {
      return null;
    } else {
      int counter = 0;
      final String[] res = new String[max - numKnown];
      for (int x = 0; x < max; x++) {
        final SpellBookPage page = this.pages.get(x);
        if (!page.isKnown()) {
          res[counter] = page.getSpell().getEffect().getName();
          counter++;
        }
      }
      return res;
    }
  }

  final Spell getSpellByName(final String sname) {
    int x;
    final int spellSize = this.pages.size();
    for (x = 0; x < spellSize; x++) {
      final String currName = this.pages.get(x).getSpell().getEffect()
          .getName();
      if (currName.equals(sname)) {
        // Found it
        return this.pages.get(x).getSpell();
      }
    }
    // Didn't find it
    return null;
  }

  public final void learnSpellByID(final int ID) {
    this.pages.get(ID).learn();
  }

  public final void learnAllSpells() {
    final int spellSize = this.pages.size();
    for (int x = 0; x < spellSize; x++) {
      this.pages.get(x).learn();
    }
  }

  public final void forgetAllSpells() {
    final int spellSize = this.pages.size();
    for (int x = 0; x < spellSize; x++) {
      this.pages.get(x).forget();
    }
  }

  final String[] getAllSpellNames() {
    int x;
    int k = 0;
    String[] names;
    final int spellSize = this.pages.size();
    final ArrayList<String> tempnames = new ArrayList<>();
    for (x = 0; x < spellSize; x++) {
      final SpellBookPage page = this.pages.get(x);
      if (page.isKnown()) {
        tempnames.add(page.getSpell().getEffect().getName());
        k++;
      }
    }
    if (k != 0) {
      names = new String[k];
      k = 0;
      for (x = 0; x < spellSize; x++) {
        final SpellBookPage page = this.pages.get(x);
        if (page.isKnown()) {
          names[k] = page.getSpell().getEffect().getName();
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
    final int spellSize = this.pages.size();
    final ArrayList<String> tempnames = new ArrayList<>();
    for (x = 0; x < spellSize; x++) {
      final SpellBookPage page = this.pages.get(x);
      if (page.isKnown()) {
        tempnames.add(page.getSpell().getEffect().getName());
        k++;
      }
    }
    if (k != 0) {
      names = new String[k];
      k = 0;
      for (x = 0; x < spellSize; x++) {
        final SpellBookPage page = this.pages.get(x);
        if (page.isKnown()) {
          names[k] = page.getSpell().getEffect().getName();
          k++;
        }
      }
    } else {
      names = null;
    }
    if (names != null) {
      k = 0;
      for (x = 0; x < spellSize; x++) {
        final SpellBookPage page = this.pages.get(x);
        if (page.isKnown()) {
          final int cost = page.getSpell().getCost();
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
    final int spellSize = this.pages.size();
    for (x = 0; x < spellSize; x++) {
      if (this.pages.get(x).isKnown()) {
        k++;
      }
    }
    if (k != 0) {
      costs = new int[k];
      k = 0;
      for (x = 0; x < spellSize; x++) {
        final SpellBookPage page = this.pages.get(x);
        if (page.isKnown()) {
          costs[k] = page.getSpell().getCost();
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
    final int spellSize = this.pages.size();
    for (x = 0; x < spellSize; x++) {
      final String currName = this.pages.get(x).getSpell().getEffect()
          .getName();
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
    return Objects.hash(this.pages);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SpellBook)) {
      return false;
    }
    final SpellBook other = (SpellBook) obj;
    return Objects.equals(this.pages, other.pages);
  }
}
