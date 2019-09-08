/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttmaze.abc;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;
import com.puttysoftware.fantastlereboot.ttmaze.MazeConstants;
import com.puttysoftware.fantastlereboot.ttmaze.utilities.TypeConstants;

public abstract class AbstractWall extends AbstractMazeObject {
    // Constructors
    protected AbstractWall() {
        super(true, true);
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
        // Do nothing
    }

    @Override
    public void moveFailedAction(final boolean ie, final int dirX,
            final int dirY) {
        TallerTower.getApplication().showMessage("Can't go that way");
        // Play move failed sound, if it's enabled
        SoundLoader.playSound(GameSound.WALK_FAILED);
    }

    @Override
    public abstract String getName();

    @Override
    public int getLayer() {
        return MazeConstants.LAYER_OBJECT;
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_WALL);
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