/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.items;

import java.io.IOException;

import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class Item {
  // Properties
  private String name;
  private final int initialUses;
  private final int weightPerUse;
  private int uses;
  private int buyPrice;
  private int sellPrice;
  private int weight;
  private int potency;
  private boolean combatUsable;
  private boolean autoDropAtZeroUses;

  // Constructors
  public Item() {
    super();
    this.name = "Un-named Item";
    this.initialUses = 0;
    this.uses = 0;
    this.weightPerUse = 0;
    this.buyPrice = 0;
    this.sellPrice = 0;
    this.weight = 0;
    this.potency = 0;
    this.combatUsable = false;
    this.autoDropAtZeroUses = false;
  }

  public Item(final String itemName, final int itemInitialUses,
      final int itemWeightPerUse) {
    super();
    this.name = itemName;
    this.initialUses = itemInitialUses;
    this.uses = itemInitialUses;
    this.weightPerUse = itemWeightPerUse;
    this.buyPrice = 0;
    this.sellPrice = 0;
    this.weight = 0;
    this.potency = 0;
    this.combatUsable = false;
    this.autoDropAtZeroUses = false;
  }

  protected Item(final String iName, final Item i) {
    super();
    this.name = iName;
    this.initialUses = i.initialUses;
    this.uses = i.uses;
    this.weightPerUse = i.weightPerUse;
    this.buyPrice = i.buyPrice;
    this.sellPrice = i.sellPrice;
    this.weight = i.weight;
    this.potency = i.potency;
    this.combatUsable = false;
    this.autoDropAtZeroUses = false;
  }

  // Methods
  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.buyPrice;
    result = prime * result + (this.combatUsable ? 1231 : 1237);
    result = prime * result + this.initialUses;
    result = prime * result + (this.name == null ? 0 : this.name.hashCode());
    result = prime * result + this.potency;
    result = prime * result + this.sellPrice;
    result = prime * result + this.weight;
    return prime * result + this.weightPerUse;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Item other = (Item) obj;
    if (this.autoDropAtZeroUses != other.autoDropAtZeroUses) {
      return false;
    }
    if (this.buyPrice != other.buyPrice) {
      return false;
    }
    if (this.combatUsable != other.combatUsable) {
      return false;
    }
    if (this.initialUses != other.initialUses) {
      return false;
    }
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.potency != other.potency) {
      return false;
    }
    if (this.sellPrice != other.sellPrice) {
      return false;
    }
    if (this.weight != other.weight) {
      return false;
    }
    if (this.weightPerUse != other.weightPerUse) {
      return false;
    }
    return true;
  }

  public void setAutoDropAtZeroUses(final boolean doAutoDropAtZeroUses) {
    this.autoDropAtZeroUses = doAutoDropAtZeroUses;
  }

  public final void setName(final String newName) {
    this.name = newName;
  }

  public final void setPotency(final int newPotency) {
    this.potency = newPotency;
  }

  public final void setBuyPrice(final int newBuyPrice) {
    this.buyPrice = newBuyPrice;
  }

  public final void setSellPrice(final int newSellPrice) {
    this.sellPrice = newSellPrice;
  }

  public final void setCombatUsable(final boolean isCombatUsable) {
    this.combatUsable = isCombatUsable;
  }

  public String getName() {
    return this.name;
  }

  public final int getBuyPrice() {
    return this.buyPrice;
  }

  public final int getPotency() {
    return this.potency;
  }

  private final int getBaseWeight() {
    return this.weight;
  }

  public final int getWeightPerUse() {
    return this.weightPerUse;
  }

  public final int getEffectiveWeight() {
    return this.getBaseWeight() + this.getUses() * this.getWeightPerUse();
  }

  public final int getInitialUses() {
    return this.initialUses;
  }

  private final int getUses() {
    return this.uses;
  }

  public boolean shouldAutoDropAtZeroUses() {
    return this.autoDropAtZeroUses;
  }

  private final boolean isUsable() {
    return this.uses > 0;
  }

  public final boolean use() {
    if (this.uses > 0) {
      this.uses--;
      return true;
    } else {
      return false;
    }
  }

  public final boolean isCombatUsable() {
    return this.isUsable() && this.combatUsable;
  }

  protected static Item readItem(final XDataReader dr) throws IOException {
    final String itemName = dr.readString();
    if (itemName.equals("null")) {
      // Abort
      return null;
    }
    final int itemInitialUses = dr.readInt();
    final int itemWeightPerUse = dr.readInt();
    final Item i = new Item(itemName, itemInitialUses, itemWeightPerUse);
    i.uses = dr.readInt();
    i.buyPrice = dr.readInt();
    i.sellPrice = dr.readInt();
    i.weight = dr.readInt();
    i.potency = dr.readInt();
    i.combatUsable = dr.readBoolean();
    return i;
  }

  final void writeItem(final XDataWriter dw) throws IOException {
    dw.writeString(this.name);
    dw.writeInt(this.initialUses);
    dw.writeInt(this.weightPerUse);
    dw.writeInt(this.uses);
    dw.writeInt(this.buyPrice);
    dw.writeInt(this.sellPrice);
    dw.writeInt(this.weight);
    dw.writeInt(this.potency);
    dw.writeBoolean(this.combatUsable);
  }
}
