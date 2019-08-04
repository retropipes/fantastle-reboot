/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.fantastlereboot.Application;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.generic.ArrowTypeConstants;
import com.puttysoftware.fantastlereboot.generic.GenericTransientObject;
import com.puttysoftware.fantastlereboot.generic.MazeObject;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objects.Arrow;
import com.puttysoftware.fantastlereboot.objects.Empty;
import com.puttysoftware.fantastlereboot.objects.IceArrow;
import com.puttysoftware.fantastlereboot.objects.Wall;
import com.puttysoftware.fantastlereboot.resourcemanagers.SoundManager;

public class ArrowTask extends Thread {
    // Fields
    private int x, y;
    private final int at;

    // Constructors
    public ArrowTask(final int newX, final int newY, final int newAT) {
        this.x = newX;
        this.y = newY;
        this.at = newAT;
    }

    @Override
    public void run() {
        boolean res = true;
        final Application app = FantastleReboot.getApplication();
        final PlayerLocationManager plMgr = app.getGameManager()
                .getPlayerManager();
        final ObjectInventory inv = app.getGameManager().getObjectInventory();
        final int px = plMgr.getPlayerLocationX();
        final int py = plMgr.getPlayerLocationY();
        final int pz = plMgr.getPlayerLocationZ();
        final int pw = plMgr.getPlayerLocationW();
        final int[] mod = app.getGameManager().doEffects(this.x, this.y);
        this.x = mod[0];
        this.y = mod[1];
        int cumX = this.x;
        int cumY = this.y;
        final int incX = this.x;
        final int incY = this.y;
        final Maze m = app.getMazeManager().getMaze();
        m.tickTimers(pw, pz);
        MazeObject o = null;
        try {
            o = m.getCell(px + cumX, py + cumY, pz, pw, Maze.LAYER_OBJECT);
        } catch (final ArrayIndexOutOfBoundsException ae) {
            o = new Wall();
        }
        final GenericTransientObject a = ArrowTask.createArrowForType(this.at);
        final String suffix = MazeObject.resolveDirectionConstantToName(
                MazeObject.resolveRelativeDirection(incX, incY));
        a.setNameSuffix(suffix);
        if (app.getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            SoundManager.playSoundSynchronously("arrow");
        }
        while (!o.isConditionallyDirectionallySolid(true, incX, incY, inv)) {
            res = o.arrowHitAction(px + cumX, py + cumY, pz, pw, incX, incY,
                    this.at, inv);
            if (!res) {
                break;
            }
            if (!o.isConditionallyDirectionallySolid(true, incX, incY, inv)) {
                app.getGameManager().redrawOneSquare(px + cumX, py + cumY, true,
                        a.getName());
            }
            app.getGameManager().redrawOneSquare(px + cumX, py + cumY, false,
                    new Empty().getName());
            cumX += incX;
            cumY += incY;
            try {
                o = m.getCell(px + cumX, py + cumY, pz, pw, Maze.LAYER_OBJECT);
            } catch (final ArrayIndexOutOfBoundsException ae) {
                o = new Wall();
            }
        }
        // Fire arrow hit action for final object, if it hasn't already been
        // fired
        if (res) {
            o.arrowHitAction(px + cumX, py + cumY, pz, pw, incX, incY, this.at,
                    inv);
        }
        if (app.getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            SoundManager.playSoundAsynchronously("arrowdie");
        }
        app.getGameManager().arrowDone();
    }

    private static GenericTransientObject createArrowForType(final int type) {
        switch (type) {
        case ArrowTypeConstants.ARROW_TYPE_PLAIN:
            return new Arrow();
        case ArrowTypeConstants.ARROW_TYPE_ICE:
            return new IceArrow();
        default:
            return null;
        }
    }
}
