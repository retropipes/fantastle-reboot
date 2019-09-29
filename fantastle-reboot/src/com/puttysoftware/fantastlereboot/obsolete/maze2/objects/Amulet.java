/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.game.GameLogicManager;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.obsolete.TallerTower;
import com.puttysoftware.fantastlereboot.obsolete.loaders.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractTrap;

public class Amulet extends AbstractTrap {
    // Constructors
    public Amulet() {
        super(ObjectImageConstants.OBJECT_IMAGE_AMULET);
    }

    @Override
    public String getName() {
        return "Amulet";
    }

    @Override
    public String getPluralName() {
        return "Amulets";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX,
            final int dirY) {
        final BagOStuff bag = FantastleReboot.getBagOStuff();
        bag.showMessage("You no longer slide on ice!");
        final GameLogicManager glm = TallerTower.getApplication()
                .getGameManager();
        glm.activateEffect(EffectConstants.EFFECT_STICKY);
        SoundPlayer.playSound(GameSound.GRAB);
        GameLogicManager.decay();
    }

    @Override
    public String getDescription() {
        return "Amulets make you not slide on ice for 15 steps when stepped on.";
    }
}