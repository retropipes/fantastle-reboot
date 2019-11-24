/*  FantastleReboot: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.objectmodel;

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
      res = DirectionNameResolver.NAME_NORTH;
    } else if (dir == Directions.SOUTH) {
      res = DirectionNameResolver.NAME_SOUTH;
    } else if (dir == Directions.WEST) {
      res = DirectionNameResolver.NAME_WEST;
    } else if (dir == Directions.EAST) {
      res = DirectionNameResolver.NAME_EAST;
    } else if (dir == Directions.SOUTHEAST) {
      res = DirectionNameResolver.NAME_SOUTHEAST;
    } else if (dir == Directions.SOUTHWEST) {
      res = DirectionNameResolver.NAME_SOUTHWEST;
    } else if (dir == Directions.NORTHWEST) {
      res = DirectionNameResolver.NAME_NORTHWEST;
    } else if (dir == Directions.NORTHEAST) {
      res = DirectionNameResolver.NAME_NORTHEAST;
    } else if (dir == Directions.NONE) {
      res = DirectionNameResolver.NAME_NONE;
    } else {
      res = DirectionNameResolver.NAME_INVALID;
    }
    return res;
  }
}
