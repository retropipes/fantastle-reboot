/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.ttmaze.objects;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.loaders.older.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;
import com.puttysoftware.fantastlereboot.ttmaze.abc.AbstractTrap;
import com.puttysoftware.fantastlereboot.ttmaze.effects.MazeEffectConstants;

public class UTurnTrap extends AbstractTrap {
    // Constructors
    public UTurnTrap() {
        super(ObjectImageConstants.OBJECT_IMAGE_U_TURN_TRAP);
    }

    @Override
    public String getName() {
        return "U Turn Trap";
    }

    @Override
    public String getPluralName() {
        return "U Turn Traps";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
        TallerTower.getApplication().showMessage(
                "Your controls are turned around!");
        TallerTower.getApplication().getGameManager()
                .activateEffect(MazeEffectConstants.EFFECT_U_TURNED);
        SoundLoader.playSound(GameSound.CHANGE);
    }

    @Override
    public String getDescription() {
        return "U Turn Traps invert your controls for 6 steps when stepped on.";
    }
}