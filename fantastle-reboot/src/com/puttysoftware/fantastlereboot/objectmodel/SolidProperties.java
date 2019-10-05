/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import com.puttysoftware.fantastlereboot.utilities.DirectionConstants;
import com.puttysoftware.fantastlereboot.utilities.DirectionResolver;
import com.puttysoftware.storage.FlagStorage;

class SolidProperties {
    // Private enumeration
    private static enum SolidDataTypes {
        EXTERNAL(0), INTERNAL(1);

        private int index;

        SolidDataTypes(final int value) {
            this.index = value;
        }
    }

    // Properties
    private final FlagStorage solidData;
    private static final int SOLID_DATA_TYPES = 2;

    // Constructors
    public SolidProperties() {
        this.solidData = new FlagStorage(SolidProperties.SOLID_DATA_TYPES,
                DirectionConstants.DIRECTION_COUNT);
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
        final SolidProperties other = (SolidProperties) obj;
        if (!Objects.equals(this.solidData, other.solidData)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.solidData);
        return hash;
    }

    public boolean isSolid() {
        boolean result = false;
        for (int dir = 0; dir < DirectionConstants.DIRECTION_COUNT; dir++) {
            result = result || this.solidData
                    .getCell(SolidDataTypes.EXTERNAL.index, dir);
        }
        return result;
    }

    public boolean isInternallySolid() {
        boolean result = false;
        for (int dir = 0; dir < DirectionConstants.DIRECTION_COUNT; dir++) {
            result = result || this.solidData
                    .getCell(SolidDataTypes.INTERNAL.index, dir);
        }
        return result;
    }

    public boolean isDirectionallySolid(final int dirX, final int dirY) {
        final int dir = DirectionResolver.resolveRelativeDirection(dirX, dirY);
        return this.solidData.getCell(SolidDataTypes.EXTERNAL.index, dir);
    }

    public boolean isInternallyDirectionallySolid(final int dirX,
            final int dirY) {
        final int dir = DirectionResolver.resolveRelativeDirection(dirX, dirY);
        return this.solidData.getCell(SolidDataTypes.INTERNAL.index, dir);
    }

    public void setSolid(final boolean value) {
        for (int dir = 0; dir < DirectionConstants.DIRECTION_COUNT; dir++) {
            this.solidData.setCell(value, SolidDataTypes.EXTERNAL.index, dir);
        }
    }

    public void setInternallySolid(final boolean value) {
        for (int dir = 0; dir < DirectionConstants.DIRECTION_COUNT; dir++) {
            this.solidData.setCell(value, SolidDataTypes.INTERNAL.index, dir);
        }
    }

    public void setDirectionallySolid(final int dir, final boolean value) {
        this.solidData.setCell(value, SolidDataTypes.EXTERNAL.index, dir);
    }

    public void setInternallyDirectionallySolid(final int dir, final boolean value) {
        this.solidData.setCell(value, SolidDataTypes.INTERNAL.index, dir);
    }
}
