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

public class DizzinessTrap extends AbstractTrap {
    // Constructors
    public DizzinessTrap() {
        super(ObjectImageConstants.OBJECT_IMAGE_DIZZINESS_TRAP);
    }

    @Override
    public String getName() {
        return "Dizziness Trap";
    }

    @Override
    public String getPluralName() {
        return "Dizziness Traps";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
        TallerTower.getApplication().showMessage("You feel dizzy!");
        TallerTower.getApplication().getGameManager()
                .activateEffect(MazeEffectConstants.EFFECT_DIZZY);
        SoundManager.playSound(SoundConstants.SOUND_DIZZY);
    }

    @Override
    public String getDescription() {
        return "Dizziness Traps randomly alter your controls each step for 3 steps when stepped on.";
    }
}