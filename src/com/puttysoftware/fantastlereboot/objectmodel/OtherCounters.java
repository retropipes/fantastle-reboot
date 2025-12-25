/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import org.retropipes.diane.storage.NumberStorage;

class OtherCounters {
    // Private enumeration
    private enum OtherDataTypes {
	USES(0),
	TIMER_TICKS(1),
	TIMER_RESET(2);

	private int index;

	OtherDataTypes(final int value) {
	    this.index = value;
	}
    }

    private static final int OTHER_DATA_TYPES = 3;
    // Properties
    private final NumberStorage otherData;

    // Constructors
    public OtherCounters() {
	this.otherData = new NumberStorage(OtherCounters.OTHER_DATA_TYPES);
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null || this.getClass() != obj.getClass()) {
	    return false;
	}
	final var other = (OtherCounters) obj;
	if (!Objects.equals(this.otherData, other.otherData)) {
	    return false;
	}
	return true;
    }

    public int getTimerReset() {
	return this.otherData.getCell(OtherDataTypes.TIMER_RESET.index);
    }

    public int getTimerTicks() {
	return this.otherData.getCell(OtherDataTypes.TIMER_TICKS.index);
    }

    public int getUses() {
	return this.otherData.getCell(OtherDataTypes.USES.index);
    }

    @Override
    public int hashCode() {
	final var hash = 3;
	return 89 * hash + Objects.hashCode(this.otherData);
    }

    public void resetTimer() {
	this.otherData.setCell(this.getTimerReset(), OtherDataTypes.TIMER_TICKS.index);
    }

    public void setTimerReset(final int value) {
	this.otherData.setCell(value, OtherDataTypes.TIMER_RESET.index);
    }

    public void setTimerTicks(final int value) {
	this.otherData.setCell(value, OtherDataTypes.TIMER_TICKS.index);
    }

    public void setUses(final int value) {
	this.otherData.setCell(value, OtherDataTypes.USES.index);
    }

    public void tickTimer() {
	this.otherData.setCell(this.getTimerTicks() - 1, OtherDataTypes.USES.index);
    }

    public void use() {
	this.otherData.setCell(this.getUses() - 1, OtherDataTypes.USES.index);
    }
}
