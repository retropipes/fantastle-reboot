/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import org.retropipes.diane.direction.DirectionQuery;
import org.retropipes.diane.direction.DirectionQueryResolver;
import org.retropipes.diane.storage.FlagStorage;

class SolidProperties {
    // Private enumeration
    private enum SolidDataTypes {
	EXTERNAL(0),
	INTERNAL(1);

	private int index;

	SolidDataTypes(final int value) {
	    this.index = value;
	}
    }

    private static final int SOLID_DATA_TYPES = 2;
    // Properties
    private final FlagStorage solidData;

    // Constructors
    public SolidProperties() {
	this.solidData = new FlagStorage(SolidProperties.SOLID_DATA_TYPES, DirectionQueryResolver.COUNT);
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null || this.getClass() != obj.getClass()) {
	    return false;
	}
	final var other = (SolidProperties) obj;
	if (!Objects.equals(this.solidData, other.solidData)) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	final var hash = 3;
	return 89 * hash + Objects.hashCode(this.solidData);
    }

    public boolean isDirectionallySolid(final DirectionQuery dir) {
	return this.solidData.getCell(SolidDataTypes.EXTERNAL.index, dir.ordinal());
    }

    public boolean isInternallyDirectionallySolid(final DirectionQuery dir) {
	return this.solidData.getCell(SolidDataTypes.INTERNAL.index, dir.ordinal());
    }

    public boolean isInternallySolid() {
	var result = false;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result || this.solidData.getCell(SolidDataTypes.INTERNAL.index, dir);
	}
	return result;
    }

    public boolean isSolid() {
	var result = false;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result || this.solidData.getCell(SolidDataTypes.EXTERNAL.index, dir);
	}
	return result;
    }

    public void setDirectionallySolid(final DirectionQuery dir, final boolean value) {
	this.solidData.setCell(value, SolidDataTypes.EXTERNAL.index, dir.ordinal());
    }

    public void setInternallyDirectionallySolid(final DirectionQuery dir, final boolean value) {
	this.solidData.setCell(value, SolidDataTypes.INTERNAL.index, dir.ordinal());
    }

    public void setInternallySolid(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.solidData.setCell(value, SolidDataTypes.INTERNAL.index, dir);
	}
    }

    public void setSolid(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.solidData.setCell(value, SolidDataTypes.EXTERNAL.index, dir);
	}
    }
}
