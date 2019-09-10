/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.obsolete.loaders2.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractWall;
import com.puttysoftware.fantastlereboot.utilities.TypeConstants;

public class WallOn extends AbstractWall {
    // Constructors
    public WallOn() {
        super();
    }

    @Override
    public String getName() {
        return "Wall On";
    }

    @Override
    public String getPluralName() {
        return "Walls On";
    }

    @Override
    public String getDescription() {
        return "Walls On are impassable - you'll need to go around them.";
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_WALL_ON;
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_PLAIN_WALL);
        this.type.set(TypeConstants.TYPE_WALL);
    }
}