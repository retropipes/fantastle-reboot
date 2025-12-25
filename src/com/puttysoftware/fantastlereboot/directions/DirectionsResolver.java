/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.directions;

public class DirectionsResolver {
    public static final int COUNT = 8;

    public static int resolve(final int dX, final int dY) {
	final var dirX = (int) Math.signum(dX);
	final var dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Directions.NONE;
	}
	if (dirX == 0 && dirY == -1) {
	    return Directions.NORTH;
	}
	if (dirX == 0 && dirY == 1) {
	    return Directions.SOUTH;
	} else if (dirX == -1 && dirY == 0) {
	    return Directions.WEST;
	} else if (dirX == 1 && dirY == 0) {
	    return Directions.EAST;
	} else if (dirX == 1 && dirY == 1) {
	    return Directions.SOUTHEAST;
	} else if (dirX == -1 && dirY == 1) {
	    return Directions.SOUTHWEST;
	} else if (dirX == -1 && dirY == -1) {
	    return Directions.NORTHWEST;
	} else if (dirX == 1 && dirY == -1) {
	    return Directions.NORTHEAST;
	} else {
	    return Directions.NONE;
	}
    }

    public static int resolveInvert(final int dX, final int dY) {
	final var dirX = (int) Math.signum(dX);
	final var dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Directions.NONE;
	}
	if (dirX == 0 && dirY == -1) {
	    return Directions.SOUTH;
	}
	if (dirX == 0 && dirY == 1) {
	    return Directions.NORTH;
	} else if (dirX == -1 && dirY == 0) {
	    return Directions.EAST;
	} else if (dirX == 1 && dirY == 0) {
	    return Directions.WEST;
	} else if (dirX == 1 && dirY == 1) {
	    return Directions.NORTHWEST;
	} else if (dirX == -1 && dirY == 1) {
	    return Directions.NORTHEAST;
	} else if (dirX == -1 && dirY == -1) {
	    return Directions.SOUTHEAST;
	} else if (dirX == 1 && dirY == -1) {
	    return Directions.SOUTHWEST;
	} else {
	    return Directions.NONE;
	}
    }

    public static int rotateRight45(final int input) {
	return switch (input) {
	case Directions.NONE -> Directions.NONE;
	case Directions.NORTH -> Directions.NORTHEAST;
	case Directions.NORTHEAST -> Directions.EAST;
	case Directions.EAST -> Directions.SOUTHEAST;
	case Directions.SOUTHEAST -> Directions.SOUTH;
	case Directions.SOUTH -> Directions.SOUTHWEST;
	case Directions.SOUTHWEST -> Directions.WEST;
	case Directions.WEST -> Directions.NORTHWEST;
	case Directions.NORTHWEST -> Directions.NORTH;
	default -> Directions.NONE;
	};
    }

    public static int rotateRight90(final int input) {
	return switch (input) {
	case Directions.NONE -> Directions.NONE;
	case Directions.NORTH -> Directions.EAST;
	case Directions.NORTHEAST -> Directions.SOUTHEAST;
	case Directions.EAST -> Directions.SOUTH;
	case Directions.SOUTHEAST -> Directions.SOUTHWEST;
	case Directions.SOUTH -> Directions.WEST;
	case Directions.SOUTHWEST -> Directions.NORTHWEST;
	case Directions.WEST -> Directions.NORTH;
	case Directions.NORTHWEST -> Directions.NORTHEAST;
	default -> Directions.NONE;
	};
    }

    public static int rotateLeft45(final int input) {
	return switch (input) {
	case Directions.NONE -> Directions.NONE;
	case Directions.NORTH -> Directions.NORTHWEST;
	case Directions.NORTHEAST -> Directions.NORTH;
	case Directions.EAST -> Directions.NORTHEAST;
	case Directions.SOUTHEAST -> Directions.EAST;
	case Directions.SOUTH -> Directions.SOUTHEAST;
	case Directions.SOUTHWEST -> Directions.SOUTH;
	case Directions.WEST -> Directions.SOUTHWEST;
	case Directions.NORTHWEST -> Directions.WEST;
	default -> Directions.NONE;
	};
    }

    public static int rotateLeft90(final int input) {
	return switch (input) {
	case Directions.NONE -> Directions.NONE;
	case Directions.NORTH -> Directions.WEST;
	case Directions.NORTHEAST -> Directions.NORTHWEST;
	case Directions.EAST -> Directions.NORTH;
	case Directions.SOUTHEAST -> Directions.NORTHEAST;
	case Directions.SOUTH -> Directions.EAST;
	case Directions.SOUTHWEST -> Directions.SOUTHEAST;
	case Directions.WEST -> Directions.SOUTH;
	case Directions.NORTHWEST -> Directions.SOUTHWEST;
	default -> Directions.NONE;
	};
    }

    public static int[] unresolve(final int dir) {
	var res = new int[2];
	if (dir == Directions.NONE) {
	    res[0] = 0;
	    res[1] = 0;
	} else if (dir == Directions.NORTH) {
	    res[0] = 0;
	    res[1] = -1;
	} else if (dir == Directions.SOUTH) {
	    res[0] = 0;
	    res[1] = 1;
	} else if (dir == Directions.WEST) {
	    res[0] = -1;
	    res[1] = 0;
	} else if (dir == Directions.EAST) {
	    res[0] = 1;
	    res[1] = 0;
	} else if (dir == Directions.SOUTHEAST) {
	    res[0] = 1;
	    res[1] = 1;
	} else if (dir == Directions.SOUTHWEST) {
	    res[0] = -1;
	    res[1] = 1;
	} else if (dir == Directions.NORTHWEST) {
	    res[0] = -1;
	    res[1] = -1;
	} else if (dir == Directions.NORTHEAST) {
	    res[0] = 1;
	    res[1] = -1;
	} else {
	    res = null;
	}
	return res;
    }

    private DirectionsResolver() {
	// Do nothing
    }
}
