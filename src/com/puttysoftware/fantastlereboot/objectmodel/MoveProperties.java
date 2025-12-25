/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import org.retropipes.diane.direction.DirectionQuery;
import org.retropipes.diane.direction.DirectionQueryResolver;
import org.retropipes.diane.storage.FlagStorage;

class MoveProperties {
    // Private enumeration
    private enum MoveDataTypes {
	PUSH(0),
	PULL(1),
	PUSH_INTO(2),
	PULL_INTO(3),
	PUSH_OUT(4),
	PULL_OUT(5);

	private int index;

	MoveDataTypes(final int value) {
	    this.index = value;
	}
    }

    private static final int MOVE_DATA_TYPES = 6;
    // Properties
    private final FlagStorage moveData;

    // Constructors
    public MoveProperties() {
	this.moveData = new FlagStorage(MoveProperties.MOVE_DATA_TYPES, DirectionQueryResolver.COUNT);
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null || this.getClass() != obj.getClass()) {
	    return false;
	}
	final var other = (MoveProperties) obj;
	if (!Objects.equals(this.moveData, other.moveData)) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	final var hash = 7;
	return 17 * hash + Objects.hashCode(this.moveData);
    }

    public boolean isDirectionallyPullable(final DirectionQuery dir) {
	try {
	    if (dir != DirectionQuery.NONE) {
		return this.moveData.getCell(MoveDataTypes.PULL.index, dir.ordinal());
	    }
	    return false;
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    return false;
	}
    }

    public boolean isDirectionallyPullableInto(final DirectionQuery dir) {
	try {
	    if (dir != DirectionQuery.NONE) {
		return this.moveData.getCell(MoveDataTypes.PULL_INTO.index, dir.ordinal());
	    }
	    return false;
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    return false;
	}
    }

    public boolean isDirectionallyPullableOut(final DirectionQuery dir) {
	try {
	    if (dir != DirectionQuery.NONE) {
		return this.moveData.getCell(MoveDataTypes.PULL_OUT.index, dir.ordinal());
	    }
	    return false;
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    return false;
	}
    }

    public boolean isDirectionallyPushable(final DirectionQuery dir) {
	try {
	    if (dir != DirectionQuery.NONE) {
		return this.moveData.getCell(MoveDataTypes.PUSH.index, dir.ordinal());
	    }
	    return false;
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    return false;
	}
    }

    public boolean isDirectionallyPushableInto(final DirectionQuery dir) {
	try {
	    if (dir != DirectionQuery.NONE) {
		return this.moveData.getCell(MoveDataTypes.PUSH_INTO.index, dir.ordinal());
	    }
	    return false;
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    return false;
	}
    }

    public boolean isDirectionallyPushableOut(final DirectionQuery dir) {
	try {
	    if (dir != DirectionQuery.NONE) {
		return this.moveData.getCell(MoveDataTypes.PUSH_OUT.index, dir.ordinal());
	    }
	    return false;
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    return false;
	}
    }

    public boolean isPullable() {
	var result = true;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result && this.moveData.getCell(MoveDataTypes.PULL.index, dir);
	}
	return result;
    }

    public boolean isPullableInto() {
	var result = true;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result && this.moveData.getCell(MoveDataTypes.PULL_INTO.index, dir);
	}
	return result;
    }

    public boolean isPullableOut() {
	var result = true;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result && this.moveData.getCell(MoveDataTypes.PULL_OUT.index, dir);
	}
	return result;
    }

    public boolean isPushable() {
	var result = true;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result && this.moveData.getCell(MoveDataTypes.PUSH.index, dir);
	}
	return result;
    }

    public boolean isPushableInto() {
	var result = true;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result && this.moveData.getCell(MoveDataTypes.PUSH_INTO.index, dir);
	}
	return result;
    }

    public boolean isPushableOut() {
	var result = true;
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    result = result && this.moveData.getCell(MoveDataTypes.PUSH_OUT.index, dir);
	}
	return result;
    }

    public void setDirectionallyPullable(final DirectionQuery dir, final boolean value) {
	this.moveData.setCell(value, MoveDataTypes.PULL.index, dir.ordinal());
    }

    public void setDirectionallyPullableInto(final DirectionQuery dir, final boolean value) {
	this.moveData.setCell(value, MoveDataTypes.PULL_INTO.index, dir.ordinal());
    }

    public void setDirectionallyPullableOut(final DirectionQuery dir, final boolean value) {
	this.moveData.setCell(value, MoveDataTypes.PULL_OUT.index, dir.ordinal());
    }

    public void setDirectionallyPushable(final DirectionQuery dir, final boolean value) {
	this.moveData.setCell(value, MoveDataTypes.PUSH.index, dir.ordinal());
    }

    public void setDirectionallyPushableInto(final DirectionQuery dir, final boolean value) {
	this.moveData.setCell(value, MoveDataTypes.PUSH_INTO.index, dir.ordinal());
    }

    public void setDirectionallyPushableOut(final DirectionQuery dir, final boolean value) {
	this.moveData.setCell(value, MoveDataTypes.PUSH_OUT.index, dir.ordinal());
    }

    public void setPullable(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.setDirectionallyPullable(DirectionQuery.values()[dir], value);
	}
    }

    public void setPullableInto(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.setDirectionallyPullableInto(DirectionQuery.values()[dir], value);
	}
    }

    public void setPullableOut(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.setDirectionallyPullableOut(DirectionQuery.values()[dir], value);
	}
    }

    public void setPushable(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.setDirectionallyPushable(DirectionQuery.values()[dir], value);
	}
    }

    public void setPushableInto(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.setDirectionallyPushableInto(DirectionQuery.values()[dir], value);
	}
    }

    public void setPushableOut(final boolean value) {
	for (var dir = 0; dir < DirectionQueryResolver.COUNT; dir++) {
	    this.setDirectionallyPushableOut(DirectionQuery.values()[dir], value);
	}
    }
}
