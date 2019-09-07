/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttmaze.objects;

import com.puttysoftware.fantastlereboot.loaders.older.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.loaders.older.SoundConstants;
import com.puttysoftware.fantastlereboot.loaders.older.SoundManager;
import com.puttysoftware.fantastlereboot.ttgame.GameLogicManager;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;
import com.puttysoftware.fantastlereboot.ttmaze.abc.AbstractTrigger;

public class ClosedDoor extends AbstractTrigger {
    // Constructors
    public ClosedDoor() {
        super();
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_CLOSED_DOOR;
    }

    // Scriptability
    @Override
    public String getName() {
        return "Closed Door";
    }

    @Override
    public String getPluralName() {
        return "Closed Doors";
    }

    @Override
    public String getDescription() {
        return "Closed Doors open when stepped on.";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
        SoundManager.playSound(SoundConstants.SOUND_PICK_LOCK);
        final GameLogicManager glm = TallerTower.getApplication()
                .getGameManager();
        GameLogicManager.morph(new OpenDoor());
        glm.redrawMaze();
    }
}
