package com.puttysoftware.fantastlereboot.items;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.legacyio.DataReader;
import com.puttysoftware.fantastlereboot.legacyio.DataWriter;

public class Item {
    // Properties
    private final String name;
    private final int category;
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
    public Item(final String itemName, final int itemCat,
            final int itemInitialUses, final int itemWeightPerUse) {
        this.name = itemName;
        this.category = itemCat;
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

    // Methods
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.autoDropAtZeroUses ? 1231 : 1237);
        result = prime * result + this.buyPrice;
        result = prime * result + this.category;
        result = prime * result + (this.combatUsable ? 1231 : 1237);
        result = prime * result + this.initialUses;
        result = prime * result
                + (this.name == null ? 0 : this.name.hashCode());
        result = prime * result + this.potency;
        result = prime * result + this.sellPrice;
        result = prime * result + this.weight;
        result = prime * result + this.weightPerUse;
        return result;
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
        if (this.category != other.category) {
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

    public void setWeight(final int newWeight) {
        this.weight = newWeight;
    }

    public void setPotency(final int newPotency) {
        this.potency = newPotency;
    }

    public void setAutoDropAtZeroUses(final boolean doAutoDropAtZeroUses) {
        this.autoDropAtZeroUses = doAutoDropAtZeroUses;
    }

    public void setUses(final int newUses) {
        this.uses = newUses;
    }

    public void setBuyPrice(final int newBuyPrice) {
        this.buyPrice = newBuyPrice;
    }

    public void setSellPrice(final int newSellPrice) {
        this.sellPrice = newSellPrice;
    }

    public void setCombatUsable(final boolean newCombatUsable) {
        this.combatUsable = newCombatUsable;
    }

    public String getName() {
        return this.name;
    }

    public int getCategory() {
        return this.category;
    }

    public boolean isOfCategory(final int testCategory) {
        return (this.category | testCategory) == this.category;
    }

    public int getBuyPrice() {
        return this.buyPrice;
    }

    public int getSellPrice() {
        return this.sellPrice;
    }

    public int getPotency() {
        return this.potency;
    }

    public int getBaseWeight() {
        return this.weight;
    }

    public int getWeightPerUse() {
        return this.weightPerUse;
    }

    public int getEffectiveWeight() {
        return this.getBaseWeight() + this.getUses() * this.getWeightPerUse();
    }

    public int getInitialUses() {
        return this.initialUses;
    }

    public int getUses() {
        return this.uses;
    }

    public boolean isUsable() {
        return this.uses > 0;
    }

    public boolean use() {
        if (this.uses > 0) {
            this.uses--;
            return true;
        } else {
            return false;
        }
    }

    public boolean isCombatUsable() {
        return this.isUsable() && this.combatUsable;
    }

    public boolean shouldAutoDropAtZeroUses() {
        return this.autoDropAtZeroUses;
    }

    public static Item readItem(final DataReader dr) throws IOException {
        final String itemName = dr.readString();
        if (itemName.equals("null")) {
            // Abort
            return null;
        }
        final int itemCat = dr.readInt();
        final int itemInitialUses = dr.readInt();
        final int itemWeightPerUse = dr.readInt();
        final Item i = new Item(itemName, itemCat, itemInitialUses,
                itemWeightPerUse);
        i.uses = dr.readInt();
        i.buyPrice = dr.readInt();
        i.sellPrice = dr.readInt();
        i.weight = dr.readInt();
        i.potency = dr.readInt();
        i.combatUsable = dr.readBoolean();
        i.autoDropAtZeroUses = dr.readBoolean();
        return i;
    }

    public void writeItem(final DataWriter dw) throws IOException {
        dw.writeString(this.name);
        dw.writeInt(this.category);
        dw.writeInt(this.initialUses);
        dw.writeInt(this.weightPerUse);
        dw.writeInt(this.uses);
        dw.writeInt(this.buyPrice);
        dw.writeInt(this.sellPrice);
        dw.writeInt(this.weight);
        dw.writeInt(this.potency);
        dw.writeBoolean(this.combatUsable);
        dw.writeBoolean(this.autoDropAtZeroUses);
    }
}
