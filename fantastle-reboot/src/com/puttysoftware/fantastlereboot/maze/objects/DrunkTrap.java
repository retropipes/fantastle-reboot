/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.maze.objects;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.maze.abc.AbstractTrap;

public class DrunkTrap extends AbstractTrap {
    // Constructors
    public DrunkTrap() {
        super(ObjectImageConstants.OBJECT_IMAGE_DRUNK_TRAP);
    }

    @Override
    public String getName() {
        return "Drunk Trap";
    }

    @Override
    public String getPluralName() {
        return "Drunk Traps";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX,
            final int dirY) {
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        bag.showMessage("You stumble around drunkenly!");
        FantastleReboot.getBagOStuff().getGameManager()
                .activateEffect(EffectConstants.EFFECT_DRUNK);
        SoundPlayer.playSound(GameSound.DRUNK);
    }

    @Override
    public String getDescription() {
        return "Drunk Traps alter your movement in a way that resembles being intoxicated for 9 steps when stepped on.";
    }
}