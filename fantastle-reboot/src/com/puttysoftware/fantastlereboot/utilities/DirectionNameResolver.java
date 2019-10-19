/*  FantastleReboot: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.utilities;

import com.puttysoftware.diane.utilties.Directions;

public class DirectionNameResolver {
    static final String NAME_INVALID = "Invalid";
    static final String NAME_NONE = "None";
    static final String NAME_NORTHWEST = "Northwest";
    static final String NAME_NORTH = "North";
    static final String NAME_NORTHEAST = "Northeast";
    static final String NAME_EAST = "East";
    static final String NAME_SOUTHEAST = "Southeast";
    static final String NAME_SOUTH = "South";
    static final String NAME_SOUTHWEST = "Southwest";
    static final String NAME_WEST = "West";

    public static final String resolveToName(final int dir) {
        String res = null;
        if (dir == Directions.NORTH) {
            res = NAME_NORTH;
        } else if (dir == Directions.SOUTH) {
            res = NAME_SOUTH;
        } else if (dir == Directions.WEST) {
            res = NAME_WEST;
        } else if (dir == Directions.EAST) {
            res = NAME_EAST;
        } else if (dir == Directions.SOUTHEAST) {
            res = NAME_SOUTHEAST;
        } else if (dir == Directions.SOUTHWEST) {
            res = NAME_SOUTHWEST;
        } else if (dir == Directions.NORTHWEST) {
            res = NAME_NORTHWEST;
        } else if (dir == Directions.NORTHEAST) {
            res = NAME_NORTHEAST;
        } else if (dir == Directions.NONE) {
            res = NAME_NONE;
        } else {
            res = NAME_INVALID;
        }
        return res;
    }
}
