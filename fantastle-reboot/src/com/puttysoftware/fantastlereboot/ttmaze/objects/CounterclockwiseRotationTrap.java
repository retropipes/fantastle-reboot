/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.ttmaze.objects;

import com.puttysoftware.fantastlereboot.loaders.older.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.loaders.older.SoundConstants;
import com.puttysoftware.fantastlereboot.loaders.older.SoundManager;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;
import com.puttysoftware.fantastlereboot.ttmaze.abc.AbstractTrap;
import com.puttysoftware.fantastlereboot.ttmaze.effects.MazeEffectConstants;

public class CounterclockwiseRotationTrap extends AbstractTrap {
    // Constructors
    public CounterclockwiseRotationTrap() {
        super(ObjectImageConstants.OBJECT_IMAGE_CCW_ROTATION_TRAP);
    }

    @Override
    public String getName() {
        return "Counterclockwise Rotation Trap";
    }

    @Override
    public String getPluralName() {
        return "Counterclockwise Rotation Traps";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
        SoundManager.playSound(SoundConstants.SOUND_CHANGE);
        TallerTower.getApplication().showMessage("Your controls are rotated!");
        TallerTower
                .getApplication()
                .getGameManager()
                .activateEffect(
                        MazeEffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE);
    }

    @Override
    public String getDescription() {
        return "Counterclockwise Rotation Traps rotate your controls counterclockwise for 6 steps when stepped on.";
    }
}