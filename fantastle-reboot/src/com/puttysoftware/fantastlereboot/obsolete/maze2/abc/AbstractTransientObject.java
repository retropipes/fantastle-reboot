/*  MazeRunnerII: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.abc;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.fantastlereboot.maze.MazeConstants;
import com.puttysoftware.fantastlereboot.obsolete.loaders.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.utilities.DirectionNameResolver;

public abstract class AbstractTransientObject extends AbstractMazeObject {
    // Fields
    private final String name;
    private int dir;

    // Constructors
    protected AbstractTransientObject(final String newName) {
        super(true, false);
        this.name = newName;
        this.dir = Directions.NONE;
    }

    // Methods
    @Override
    public void postMoveAction(final boolean ie, final int dirX,
            final int dirY) {
        // Do nothing
    }

    @Override
    public final int getBaseID() {
        switch (this.dir) {
        case Directions.NORTH:
            return ObjectImageConstants.OBJECT_IMAGE_ARROW_NORTH;
        case Directions.NORTHEAST:
            return ObjectImageConstants.OBJECT_IMAGE_ARROW_NORTHEAST;
        case Directions.EAST:
            return ObjectImageConstants.OBJECT_IMAGE_ARROW_EAST;
        case Directions.SOUTHEAST:
            return ObjectImageConstants.OBJECT_IMAGE_ARROW_SOUTHEAST;
        case Directions.SOUTH:
            return ObjectImageConstants.OBJECT_IMAGE_ARROW_SOUTH;
        case Directions.SOUTHWEST:
            return ObjectImageConstants.OBJECT_IMAGE_ARROW_SOUTHWEST;
        case Directions.WEST:
            return ObjectImageConstants.OBJECT_IMAGE_ARROW_WEST;
        case Directions.NORTHWEST:
            return ObjectImageConstants.OBJECT_IMAGE_ARROW_NORTHWEST;
        default:
            return ObjectImageConstants.OBJECT_IMAGE_EMPTY;
        }
    }

    @Override
    public final String getName() {
        return this.name + " "
                + DirectionNameResolver.resolveToName(this.dir);
    }

    @Override
    public String getPluralName() {
        return this.name + "s "
                + DirectionNameResolver.resolveToName(this.dir);
    }

    @Override
    public String getDescription() {
        return "";
    }

    public final void setDirection(final int newDir) {
        this.dir = newDir;
    }

    @Override
    public int getLayer() {
        return MazeConstants.LAYER_OBJECT;
    }

    @Override
    protected final void setTypes() {
        // Do nothing
    }

    @Override
    public int getCustomProperty(final int propID) {
        return AbstractMazeObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
        // Do nothing
    }
}
