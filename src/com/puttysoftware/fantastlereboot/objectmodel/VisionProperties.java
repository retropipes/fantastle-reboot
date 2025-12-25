/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import org.retropipes.diane.direction.DirectionQuery;
import org.retropipes.diane.direction.DirectionQueryResolver;
import org.retropipes.diane.storage.FlagStorage;

class VisionProperties {
    // Private enumeration
    private enum VisionDataTypes {
	EXTERNAL(0),
	INTERNAL(1);

	private int index;

	VisionDataTypes(final int value) {
	    this.index = value;
	}
    }

    private static final int VISION_DATA_TYPES = 2;
    // Properties
    private final FlagStorage visionData;

    // Constructors
    public VisionProperties() {
	this.visionData = new FlagStorage(VisionProperties.VISION_DATA_TYPES, DirectionQueryResolver.COUNT);
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null || this.getClass() != obj.getClass()) {
	    return false;
	}
	final var other = (VisionProperties) obj;
	if (!Objects.equals(this.visionData, other.visionData)) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	final var hash = 3;
	return 89 * hash + Objects.hashCode(this.visionData);
    }

    public boolean isDirectionallySightBlocking(final DirectionQuery dir) {
	return this.visionData.getCell(VisionDataTypes.EXTERNAL.index, dir.ordinal());
    }

    public boolean isInternallyDirectionallySightBlocking(final DirectionQuery dir) {
	return this.visionData.getCell(VisionDataTypes.INTERNAL.index, dir.ordinal());
    }

    public boolean isInternallySightBlocking() {
	var result = false;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result || this.visionData.getCell(VisionDataTypes.INTERNAL.index, dir);
	}
	return result;
    }

    public boolean isSightBlocking() {
	var result = false;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result || this.visionData.getCell(VisionDataTypes.EXTERNAL.index, dir);
	}
	return result;
    }

    public void setDirectionallySightBlocking(final DirectionQuery dir, final boolean value) {
	this.visionData.setCell(value, VisionDataTypes.EXTERNAL.index, dir.ordinal());
    }

    public void setInternallyDirectionallySightBlocking(final DirectionQuery dir, final boolean value) {
	this.visionData.setCell(value, VisionDataTypes.INTERNAL.index, dir.ordinal());
    }

    public void setInternallySightBlocking(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.visionData.setCell(value, VisionDataTypes.INTERNAL.index, dir);
	}
    }

    public void setSightBlocking(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.visionData.setCell(value, VisionDataTypes.EXTERNAL.index, dir);
	}
    }
}
