/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.obsolete.TallerTower;
import com.puttysoftware.fantastlereboot.obsolete.loaders2.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractTrap;

public class ClockwiseRotationTrap extends AbstractTrap {
    // Constructors
    public ClockwiseRotationTrap() {
        super(ObjectImageConstants.OBJECT_IMAGE_CW_ROTATION_TRAP);
    }

    @Override
    public String getName() {
        return "Clockwise Rotation Trap";
    }

    @Override
    public String getPluralName() {
        return "Clockwise Rotation Traps";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
        SoundLoader.playSound(GameSound.CHANGE);
        TallerTower.getApplication().showMessage("Your controls are rotated!");
        TallerTower.getApplication().getGameManager()
                .activateEffect(EffectConstants.EFFECT_ROTATED_CLOCKWISE);
    }

    @Override
    public String getDescription() {
        return "Clockwise Rotation Traps rotate your controls clockwise for 6 steps when stepped on.";
    }
}