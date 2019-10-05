/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import com.puttysoftware.storage.NumberStorage;

class OtherCounters {
    // Private enumeration
    private static enum OtherDataTypes {
        USES(0), TIMER_TICKS(1), TIMER_RESET(2);

        private int index;

        OtherDataTypes(final int value) {
            this.index = value;
        }
    }

    // Properties
    private final NumberStorage otherData;
    private static final int OTHER_DATA_TYPES = 3;

    // Constructors
    public OtherCounters() {
        this.otherData = new NumberStorage(OtherCounters.OTHER_DATA_TYPES);
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
        final OtherCounters other = (OtherCounters) obj;
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

    public int getUses() {
        return this.otherData.getCell(OtherDataTypes.USES.index);
    }

    public void setUses(final int value) {
        this.otherData.setCell(value, OtherDataTypes.USES.index);
    }

    public void use() {
        this.otherData.setCell(this.getUses() - 1, OtherDataTypes.USES.index);
    }

    public int getTimerTicks() {
        return this.otherData.getCell(OtherDataTypes.TIMER_TICKS.index);
    }

    public void setTimerTicks(final int value) {
        this.otherData.setCell(value, OtherDataTypes.TIMER_TICKS.index);
    }

    public void tickTimer() {
        this.otherData.setCell(this.getTimerTicks() - 1,
                OtherDataTypes.USES.index);
    }

    public int getTimerReset() {
        return this.otherData.getCell(OtherDataTypes.TIMER_RESET.index);
    }

    public void setTimerReset(final int value) {
        this.otherData.setCell(value, OtherDataTypes.TIMER_RESET.index);
    }

    public void resetTimer() {
        this.otherData.setCell(this.getTimerReset(),
                OtherDataTypes.TIMER_TICKS.index);
    }
}
