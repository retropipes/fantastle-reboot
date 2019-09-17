/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.obsolete.maze2.objects;

import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.obsolete.Application;
import com.puttysoftware.fantastlereboot.obsolete.TallerTower;
import com.puttysoftware.fantastlereboot.obsolete.loaders.ObjectImageConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractTrap;
import com.puttysoftware.randomrange.RandomRange;

public class WarpTrap extends AbstractTrap {
    // Constructors
    public WarpTrap() {
        super(ObjectImageConstants.OBJECT_IMAGE_WARP_TRAP);
    }

    @Override
    public String getName() {
        return "Warp Trap";
    }

    @Override
    public String getPluralName() {
        return "Warp Traps";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
        RandomRange rr, rc, rf;
        final Application app = TallerTower.getApplication();
        int maxRow, maxCol, maxFloor, rRow, rCol, rFloor;
        maxRow = app.getMazeManager().getMaze().getRows() - 1;
        rr = new RandomRange(0, maxRow);
        maxCol = app.getMazeManager().getMaze().getColumns() - 1;
        rc = new RandomRange(0, maxCol);
        maxFloor = app.getMazeManager().getMaze().getFloors() - 1;
        rf = new RandomRange(0, maxFloor);
        app.getGameManager();
        do {
            rRow = rr.generate();
            rCol = rc.generate();
            rFloor = rf.generate();
        } while (app.getGameManager().tryUpdatePositionAbsolute(rRow, rCol,
                rFloor));
        app.getGameManager().updatePositionAbsolute(rRow, rCol, rFloor);
        SoundLoader.playSound(GameSound.TELEPORT);
    }

    @Override
    public String getDescription() {
        return "Warp Traps send anything that steps on one to a random location.";
    }
}
