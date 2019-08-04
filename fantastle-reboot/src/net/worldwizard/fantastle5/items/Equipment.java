package net.worldwizard.fantastle5.items;

import java.io.IOException;

import net.worldwizard.io.DataReader;
import net.worldwizard.io.DataWriter;

public class Equipment extends Item {
    // Properties
    private final int equipCat;
    private final int materialID;
    private int firstSlotUsed;
    private int secondSlotUsed;
    private boolean conditionalSlot;

    // Constructors
    private Equipment(final Item i, final int equipCategory,
            final int newMaterialID) {
        super(i.getName(), i.getCategory(), i.getInitialUses(),
                i.getWeightPerUse());
        this.equipCat = equipCategory;
        this.materialID = newMaterialID;
        this.firstSlotUsed = EquipmentSlotConstants.SLOT_NONE;
        this.secondSlotUsed = EquipmentSlotConstants.SLOT_NONE;
        this.conditionalSlot = false;
    }

    Equipment(final String itemName, final int itemCat,
            final int itemInitialUses, final int itemWeightPerUse,
            final int equipCategory, final int newMaterialID) {
        super(itemName, itemCat | ItemCategoryConstants.ITEM_CATEGORY_EQUIPMENT,
                itemInitialUses, itemWeightPerUse);
        this.equipCat = equipCategory;
        this.materialID = newMaterialID;
        this.firstSlotUsed = EquipmentSlotConstants.SLOT_NONE;
        this.secondSlotUsed = EquipmentSlotConstants.SLOT_NONE;
        this.conditionalSlot = false;
    }

    // Methods
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (this.conditionalSlot ? 1231 : 1237);
        result = prime * result + this.equipCat;
        result = prime * result + this.firstSlotUsed;
        result = prime * result + this.materialID;
        result = prime * result + this.secondSlotUsed;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Equipment other = (Equipment) obj;
        if (this.conditionalSlot != other.conditionalSlot) {
            return false;
        }
        if (this.equipCat != other.equipCat) {
            return false;
        }
        if (this.firstSlotUsed != other.firstSlotUsed) {
            return false;
        }
        if (this.materialID != other.materialID) {
            return false;
        }
        if (this.secondSlotUsed != other.secondSlotUsed) {
            return false;
        }
        return true;
    }

    public int getFirstSlotUsed() {
        return this.firstSlotUsed;
    }

    public void setFirstSlotUsed(final int newFirstSlotUsed) {
        this.firstSlotUsed = newFirstSlotUsed;
    }

    public int getSecondSlotUsed() {
        return this.secondSlotUsed;
    }

    public void setSecondSlotUsed(final int newSecondSlotUsed) {
        this.secondSlotUsed = newSecondSlotUsed;
    }

    public boolean isConditionalSlot() {
        return this.conditionalSlot;
    }

    public void setConditionalSlot(final boolean newConditionalSlot) {
        this.conditionalSlot = newConditionalSlot;
    }

    public int getEquipCategory() {
        return this.equipCat;
    }

    public int getMaterialID() {
        return this.materialID;
    }

    public static Equipment readEquipment(final DataReader dr)
            throws IOException {
        final Item i = Item.readItem(dr);
        if (i == null) {
            // Abort
            return null;
        }
        final int matID = dr.readInt();
        final int eCat = dr.readInt();
        final Equipment ei = new Equipment(i, eCat, matID);
        ei.firstSlotUsed = dr.readInt();
        ei.secondSlotUsed = dr.readInt();
        ei.conditionalSlot = dr.readBoolean();
        return ei;
    }

    public void writeEquipment(final DataWriter dw) throws IOException {
        super.writeItem(dw);
        dw.writeInt(this.materialID);
        dw.writeInt(this.equipCat);
        dw.writeInt(this.firstSlotUsed);
        dw.writeInt(this.secondSlotUsed);
        dw.writeBoolean(this.conditionalSlot);
    }
}
