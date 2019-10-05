/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import com.puttysoftware.storage.FlagStorage;

class OtherProperties {
    // Private enumeration
    private static enum OtherDataTypes {
        FRICTION(0), USABLE(1), DESTROYABLE(2), CHAINS_HORIZONTAL(3),
        CHAINS_VERTICAL(4), CARRYABLE(5), TIMER_TICKING(6);

        private int index;

        OtherDataTypes(final int value) {
            this.index = value;
        }
    }

    // Properties
    private final FlagStorage otherData;
    private static final int OTHER_DATA_TYPES = 7;

    // Constructors
    public OtherProperties() {
        this.otherData = new FlagStorage(OtherProperties.OTHER_DATA_TYPES);
    }

    // Methods
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final OtherProperties other = (OtherProperties) obj;
        if (!Objects.equals(this.otherData, other.otherData)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.otherData);
        return hash;
    }

    public boolean hasFriction() {
        return this.otherData.getCell(OtherDataTypes.FRICTION.index);
    }

    public void setFriction(final boolean value) {
        this.otherData.setCell(value, OtherDataTypes.FRICTION.index);
    }

    public boolean isUsable() {
        return this.otherData.getCell(OtherDataTypes.USABLE.index);
    }

    public void setUsable(final boolean value) {
        this.otherData.setCell(value, OtherDataTypes.USABLE.index);
    }

    public boolean isDestroyable() {
        return this.otherData.getCell(OtherDataTypes.DESTROYABLE.index);
    }

    public void setDestroyable(final boolean value) {
        this.otherData.setCell(value, OtherDataTypes.DESTROYABLE.index);
    }

    public boolean isChainReacting() {
        return this.isChainReactingHorizontally()
                && this.isChainReactingVertically();
    }

    public boolean isChainReactingHorizontally() {
        return this.otherData.getCell(OtherDataTypes.CHAINS_HORIZONTAL.index);
    }

    public void setChainReactingHorizontally(final boolean value) {
        this.otherData.setCell(value, OtherDataTypes.CHAINS_HORIZONTAL.index);
    }

    public boolean isChainReactingVertically() {
        return this.otherData.getCell(OtherDataTypes.CHAINS_VERTICAL.index);
    }

    public void setChainReactingVertically(final boolean value) {
        this.otherData.setCell(value, OtherDataTypes.CHAINS_VERTICAL.index);
    }

    public boolean isCarryable() {
        return this.otherData.getCell(OtherDataTypes.CARRYABLE.index);
    }

    public void setCarryable(final boolean value) {
        this.otherData.setCell(value, OtherDataTypes.CARRYABLE.index);
    }

    public boolean isTimerTicking() {
        return this.otherData.getCell(OtherDataTypes.TIMER_TICKING.index);
    }

    public void setTimerTicking(final boolean value) {
        this.otherData.setCell(value, OtherDataTypes.TIMER_TICKING.index);
    }
}
