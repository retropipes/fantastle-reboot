/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;

import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractTrap;

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
    public void postMoveAction(final boolean ie, final int dirX,
            final int dirY) {
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        bag.showMessage("Your controls are turned around!");
        FantastleReboot.getBagOStuff().getGameManager()
                .activateEffect(EffectConstants.EFFECT_U_TURNED);
        SoundPlayer.playSound(GameSound.CHANGE);
    }

    @Override
    public String getDescription() {
        return "U Turn Traps invert your controls for 6 steps when stepped on.";
    }
}