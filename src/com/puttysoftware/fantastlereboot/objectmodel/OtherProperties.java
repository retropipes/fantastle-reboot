/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import org.retropipes.diane.storage.FlagStorage;

class OtherProperties {
    // Private enumeration
    private enum OtherDataTypes {
	FRICTION(0),
	USABLE(1),
	DESTROYABLE(2),
	CHAINS_HORIZONTAL(3),
	CHAINS_VERTICAL(4),
	CARRYABLE(5),
	TIMER_TICKING(6);

	private int index;

	OtherDataTypes(final int value) {
	    this.index = value;
	}
    }

    private static final int OTHER_DATA_TYPES = 7;
    // Properties
    private final FlagStorage otherData;

    // Constructors
    public OtherProperties() {
	this.otherData = new FlagStorage(OtherProperties.OTHER_DATA_TYPES);
	this.setFriction(true);
	this.setDestroyable(true);
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null || this.getClass() != obj.getClass()) {
	    return false;
	}
	final var other = (OtherProperties) obj;
	if (!Objects.equals(this.otherData, other.otherData)) {
	    return false;
	}
	return true;
    }

    public boolean hasFriction() {
	return this.otherData.getCell(OtherDataTypes.FRICTION.index);
    }

    @Override
    public int hashCode() {
	final var hash = 3;
	return 89 * hash + Objects.hashCode(this.otherData);
    }

    public boolean isCarryable() {
	return this.otherData.getCell(OtherDataTypes.CARRYABLE.index);
    }

    public boolean isChainReacting() {
	return this.isChainReactingHorizontally() && this.isChainReactingVertically();
    }

    public boolean isChainReactingHorizontally() {
	return this.otherData.getCell(OtherDataTypes.CHAINS_HORIZONTAL.index);
    }

    public boolean isChainReactingVertically() {
	return this.otherData.getCell(OtherDataTypes.CHAINS_VERTICAL.index);
    }

    public boolean isDestroyable() {
	return this.otherData.getCell(OtherDataTypes.DESTROYABLE.index);
    }

    public boolean isTimerTicking() {
	return this.otherData.getCell(OtherDataTypes.TIMER_TICKING.index);
    }

    public boolean isUsable() {
	return this.otherData.getCell(OtherDataTypes.USABLE.index);
    }

    public void setCarryable(final boolean value) {
	this.otherData.setCell(value, OtherDataTypes.CARRYABLE.index);
    }

    public void setChainReactingHorizontally(final boolean value) {
	this.otherData.setCell(value, OtherDataTypes.CHAINS_HORIZONTAL.index);
    }

    public void setChainReactingVertically(final boolean value) {
	this.otherData.setCell(value, OtherDataTypes.CHAINS_VERTICAL.index);
    }

    public void setDestroyable(final boolean value) {
	this.otherData.setCell(value, OtherDataTypes.DESTROYABLE.index);
    }

    public void setFriction(final boolean value) {
	this.otherData.setCell(value, OtherDataTypes.FRICTION.index);
    }

    public void setTimerTicking(final boolean value) {
	this.otherData.setCell(value, OtherDataTypes.TIMER_TICKING.index);
    }

    public void setUsable(final boolean value) {
	this.otherData.setCell(value, OtherDataTypes.USABLE.index);
    }
}
