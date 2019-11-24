/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items;

class ItemUseQuantity {
  // Fields
  private final Item item;
  private int uses;
  private int quantity;

  // Constructors
  public ItemUseQuantity() {
    this.item = null;
    this.quantity = 0;
    this.uses = 0;
  }

  ItemUseQuantity(final Item i, final int q, final int u) {
    this.item = i;
    this.quantity = q;
    this.uses = u;
  }

  // Methods
  Item getItem() {
    return this.item;
  }

  int getQuantity() {
    return this.quantity;
  }

  void setQuantity(final int newQ) {
    this.quantity = newQ;
  }

  void incrementQuantity() {
    if (this.quantity >= 0) {
      this.quantity++;
    }
  }

  int getUses() {
    return this.uses;
  }

  void setUses(final int newU) {
    this.uses = newU;
  }

  @Override
  public String toString() {
    if (this.quantity < 0) {
      return this.item + " - Quantity: Infinite - Uses: " + this.uses;
    } else {
      return this.item + " - Quantity: " + this.quantity + " - Uses: "
          + this.uses;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (this.item == null ? 0 : this.item.hashCode());
    result = prime * result + this.quantity;
    return prime * result + this.uses;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ItemUseQuantity)) {
      return false;
    }
    final ItemUseQuantity other = (ItemUseQuantity) obj;
    if (this.item == null) {
      if (other.item != null) {
        return false;
      }
    } else if (!this.item.equals(other.item)) {
      return false;
    }
    if (this.quantity != other.quantity) {
      return false;
    }
    if (this.uses != other.uses) {
      return false;
    }
    return true;
  }
}
