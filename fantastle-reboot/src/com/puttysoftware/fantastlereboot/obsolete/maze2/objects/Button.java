/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;

import com.puttysoftware.fantastlereboot.obsolete.maze2.Maze;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractTrigger;
import com.puttysoftware.randomrange.RandomRange;

public class Button extends AbstractTrigger {
    // Constructors
    public Button() {
        super();
    }

    @Override
    public int getBaseID() {
        return ObjectImageConstants.OBJECT_IMAGE_BUTTON;
    }

    @Override
    public String getName() {
        return "Button";
    }

    @Override
    public String getPluralName() {
        return "Buttons";
    }

    @Override
    public String getDescription() {
        return "Buttons toggle Walls On and Walls Off.";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX,
            final int dirY) {
        SoundPlayer.playSound(GameSound.BUTTON);
        final BagOStuff app = FantastleReboot.getBagOStuff();
        app.getMazeManager().getMaze().fullScanButton(this.getLayer());
        app.getGameManager().redrawMaze();
    }

    @Override
    public boolean shouldGenerateObject(final Maze maze, final int row,
            final int col, final int floor, final int level, final int layer) {
        // Generate Buttons at 50% rate
        final RandomRange reject = new RandomRange(1, 100);
        return reject.generate() < 50;
    }
}