/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.effects.EffectConstants;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.obsolete.TallerTower;
import com.puttysoftware.fantastlereboot.obsolete.game2.GameLogicManager;
import com.puttysoftware.fantastlereboot.obsolete.loaders2.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.Maze;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractMPModifier;
import com.puttysoftware.randomrange.RandomRange;

public class DarkGem extends AbstractMPModifier {
    // Constructors
    public DarkGem() {
        super();
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_DARK_GEM;
    }

    @Override
    public String getName() {
        return "Dark Gem";
    }

    @Override
    public String getPluralName() {
        return "Dark Gems";
    }

    @Override
    public String getDescription() {
        return "Dark Gems take MP away.";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
        TallerTower.getApplication().showMessage("Your power withers!");
        TallerTower.getApplication().getGameManager()
                .activateEffect(EffectConstants.EFFECT_POWER_WITHER);
        SoundLoader.playSound(GameSound.FOCUS);
        GameLogicManager.decay();
    }

    @Override
    public boolean shouldGenerateObject(final Maze maze, final int row,
            final int col, final int floor, final int level, final int layer) {
        // Generate Dark Gems at 30% rate
        final RandomRange reject = new RandomRange(1, 100);
        return reject.generate() < 30;
    }
}