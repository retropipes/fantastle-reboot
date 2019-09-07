/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttmaze.objects;

import com.puttysoftware.fantastlereboot.loaders.older.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.ttmaze.abc.AbstractPassThroughObject;
import com.puttysoftware.fantastlereboot.ttmaze.utilities.TypeConstants;

public class WallOff extends AbstractPassThroughObject {
    // Constructors
    public WallOff() {
        super();
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_WALL_OFF;
    }

    @Override
    public String getName() {
        return "Wall Off";
    }

    @Override
    public String getPluralName() {
        return "Walls Off";
    }

    @Override
    public String getDescription() {
        return "Walls Off can be walked through.";
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_PASS_THROUGH);
        this.type.set(TypeConstants.TYPE_EMPTY_SPACE);
    }
}