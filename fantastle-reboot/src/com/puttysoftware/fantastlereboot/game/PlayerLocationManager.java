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

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.maze.Maze;

public class PlayerLocationManager {
    // Fields
    private int oldLocX, oldLocY, oldLocZ, oldLocW;
    private int locX, locY, locZ, locW;

    // Constructors
    public PlayerLocationManager() {
        this.resetPlayerLocation();
    }

    // Methods
    public int getPlayerLocationX() {
        return this.locX;
    }

    public int getPlayerLocationY() {
        return this.locY;
    }

    public int getPlayerLocationZ() {
        return this.locZ;
    }

    public int getPlayerLocationW() {
        return this.locW;
    }

    public void setPlayerLocationX(final int val) {
        this.locX = val;
    }

    public void setPlayerLocationY(final int val) {
        this.locY = val;
    }

    public void setPlayerLocationZ(final int val) {
        this.locZ = val;
        this.fixLocationZ();
    }

    public void setPlayerLocationW(final int val) {
        this.locW = val;
    }

    public void setPlayerLocation(final int valX, final int valY,
            final int valZ, final int valW) {
        this.locX = valX;
        this.locY = valY;
        this.locZ = valZ;
        this.locW = valW;
        this.fixLocationZ();
    }

    public void offsetPlayerLocation(final int valX, final int valY,
            final int valZ, final int valW) {
        this.locX += valX;
        this.locY += valY;
        this.locZ += valZ;
        this.locW += valW;
        this.fixLocationZ();
    }

    public void offsetPlayerLocationX(final int val) {
        this.locX += val;
    }

    public void offsetPlayerLocationY(final int val) {
        this.locY += val;
    }

    public void offsetPlayerLocationZ(final int val) {
        this.locZ += val;
        this.fixLocationZ();
    }

    public void offsetPlayerLocationW(final int val) {
        this.locW += val;
    }

    public void resetPlayerLocation() {
        this.locX = 0;
        this.locY = 0;
        this.locZ = 0;
        this.locW = 0;
        this.oldLocX = 0;
        this.oldLocY = 0;
        this.oldLocZ = 0;
        this.oldLocW = 0;
    }

    public void savePlayerLocation() {
        this.oldLocX = this.locX;
        this.oldLocY = this.locY;
        this.oldLocZ = this.locZ;
        this.oldLocW = this.locW;
    }

    public void restorePlayerLocation() {
        this.locX = this.oldLocX;
        this.locY = this.oldLocY;
        this.locZ = this.oldLocZ;
        this.locW = this.oldLocW;
        this.fixLocationZ();
    }

    private void fixLocationZ() {
        final Maze m = FantastleReboot.getBagOStuff().getMazeManager().getMaze();
        if (m.is3rdDimensionWraparoundEnabled(this.locW)) {
            if (this.locZ < 0) {
                this.locZ = m.getFloors(this.locW) - 1;
            } else if (this.locZ > m.getFloors(this.locW) - 1) {
                this.locZ = 0;
            }
        }
    }
}
