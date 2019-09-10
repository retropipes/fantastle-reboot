/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.ttmaze.abc;

import com.puttysoftware.fantastlereboot.ttmaze.MazeConstants;
import com.puttysoftware.fantastlereboot.utilities.TypeConstants;

public abstract class AbstractMPModifier extends AbstractMazeObject {
    // Constructors
    protected AbstractMPModifier() {
        super(false, false);
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_MP_MODIFIER);
    }

    @Override
    public int getLayer() {
        return MazeConstants.LAYER_OBJECT;
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
