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
package com.puttysoftware.fantastlereboot.editor;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.maze.Maze;

public class EditorLocationManager {
    // Fields
    private int oldLocX, oldLocY, oldLocZ;
    private int oldLocW, oldLocE;
    private int locX, locY, locZ;
    private int locW, locE;
    private int cameFromW, oldCameFromW;
    private int cameFromZ, oldCameFromZ;
    private int minX, minY, minZ, minW, minE;
    private int maxW, maxE;
    private int maxX, maxY, maxZ;

    // Constructors
    public EditorLocationManager() {
        this.resetEditorLocation();
    }

    // Methods
    public int getEditorLocationX() {
        return this.locX;
    }

    public int getEditorLocationY() {
        return this.locY;
    }

    public int getEditorLocationZ() {
        return this.locZ;
    }

    public int getEditorLocationW() {
        return this.locW;
    }

    public int getEditorLocationE() {
        return this.locE;
    }

    public int getMaxEditorLocationX() {
        return this.maxX;
    }

    public int getMaxEditorLocationY() {
        return this.maxY;
    }

    public int getMaxEditorLocationZ() {
        return this.maxZ;
    }

    public int getMaxEditorLocationW() {
        return this.maxW;
    }

    public int getMaxEditorLocationE() {
        return this.maxE;
    }

    public int getMinEditorLocationX() {
        return this.minX;
    }

    public int getMinEditorLocationY() {
        return this.minY;
    }

    public int getMinEditorLocationZ() {
        return this.minZ;
    }

    public int getMinEditorLocationW() {
        return this.minW;
    }

    public int getMinEditorLocationE() {
        return this.minE;
    }

    public int getCameFromZ() {
        return this.cameFromZ;
    }

    public int getCameFromW() {
        return this.cameFromW;
    }

    public void setEditorLocationX(final int val) {
        this.locX = val;
        this.checkLimits();
    }

    public void setEditorLocationY(final int val) {
        this.locY = val;
        this.checkLimits();
    }

    public void setEditorLocationZ(final int val) {
        this.locZ = val;
        this.checkLimits();
    }

    public void setEditorLocationW(final int val) {
        this.locW = val;
        this.checkLimits();
    }

    public void setEditorLocationE(final int val) {
        this.locE = val;
        this.checkLimits();
    }

    public void setCameFromZ(final int val) {
        this.cameFromZ = val;
    }

    public void setCameFromW(final int val) {
        this.cameFromW = val;
    }

    public void setEditorLocation(final int valX, final int valY,
            final int valZ, final int valW, final int valE) {
        this.locW = valW;
        this.locE = valE;
        this.locX = valX;
        this.locY = valY;
        this.locZ = valZ;
        this.checkLimits();
    }

    public void offsetEditorLocation(final int valX, final int valY,
            final int valZ, final int valW, final int valE) {
        this.locW += valW;
        this.locE += valE;
        this.locX += valX;
        this.locY += valY;
        this.locZ += valZ;
        this.checkLimits();
    }

    public void setCameFrom(final int valZ, final int valW) {
        this.cameFromW = valW;
        this.cameFromZ = valZ;
        this.checkLimits();
    }

    public void offsetCameFrom(final int valZ, final int valW) {
        this.cameFromW += valW;
        this.cameFromZ += valZ;
        this.checkLimits();
    }

    public void offsetEditorLocationX(final int val) {
        this.locX += val;
        this.checkLimits();
    }

    public void offsetEditorLocationY(final int val) {
        this.locY += val;
        this.checkLimits();
    }

    public void offsetEditorLocationZ(final int val) {
        this.locZ += val;
        this.checkLimits();
    }

    public void offsetEditorLocationW(final int val) {
        this.locW += val;
        this.checkLimits();
    }

    public void offsetEditorLocationE(final int val) {
        this.locE += val;
        this.checkLimits();
    }

    public void offsetCameFromZ(final int val) {
        this.cameFromZ += val;
    }

    public void offsetCameFromW(final int val) {
        this.cameFromW += val;
    }

    public void setLimitsFromMaze(final Maze m) {
        this.minX = 0;
        this.minY = 0;
        this.minZ = 0;
        this.minW = 0;
        this.minE = 0;
        this.maxW = m.getLevels() - 1;
        this.maxE = Maze.LAYER_COUNT - 1;
        this.maxX = m.getRows(this.locW);
        this.maxY = m.getColumns(this.locW);
        this.maxZ = m.getFloors(this.locW) - 1;
    }

    public void resetEditorLocation() {
        this.locX = 0;
        this.locY = 0;
        this.locZ = 0;
        this.locW = 0;
        this.locE = 0;
        this.oldLocX = 0;
        this.oldLocY = 0;
        this.oldLocZ = 0;
        this.oldLocW = 0;
        this.oldLocE = 0;
        this.cameFromZ = 0;
        this.cameFromW = 0;
        this.oldCameFromZ = 0;
        this.oldCameFromW = 0;
        this.maxX = 0;
        this.maxY = 0;
        this.maxZ = 0;
        this.maxW = 0;
        this.maxE = 0;
        this.minX = 0;
        this.minY = 0;
        this.minZ = 0;
        this.minW = 0;
        this.minE = 0;
    }

    private void checkLimits() {
        // Check for limits out of bounds
        final Maze m = FantastleReboot.getApplication().getMazeManager().getMaze();
        if (this.locW < this.minW) {
            this.locW = this.minW;
        }
        if (this.locW > this.maxW) {
            this.locW = this.maxW;
        }
        if (this.locX < this.minX) {
            this.locX = this.minX;
        }
        if (this.locX > this.maxX) {
            this.locX = this.maxX;
        }
        if (this.locY < this.minY) {
            this.locY = this.minY;
        }
        if (this.locY > this.maxY) {
            this.locY = this.maxY;
        }
        if (this.locZ < this.minZ) {
            if (m.is3rdDimensionWraparoundEnabled(this.locW)) {
                this.locZ = this.maxZ;
            } else {
                this.locZ = this.minZ;
            }
        }
        if (this.locZ > this.maxZ) {
            if (m.is3rdDimensionWraparoundEnabled(this.locW)) {
                this.locZ = this.minZ;
            } else {
                this.locZ = this.maxZ;
            }
        }
        if (this.locE < this.minE) {
            this.locE = this.minE;
        }
        if (this.locE > this.maxE) {
            this.locE = this.maxE;
        }
    }

    public void saveEditorLocation() {
        this.oldLocX = this.locX;
        this.oldLocY = this.locY;
        this.oldLocZ = this.locZ;
        this.oldLocW = this.locW;
        this.oldLocE = this.locE;
    }

    public void restoreEditorLocation() {
        this.locX = this.oldLocX;
        this.locY = this.oldLocY;
        this.locZ = this.oldLocZ;
        this.locW = this.oldLocW;
        this.locE = this.oldLocE;
    }

    public void saveCameFrom() {
        this.oldCameFromZ = this.cameFromZ;
        this.oldCameFromW = this.cameFromW;
    }

    public void restoreCameFrom() {
        this.cameFromZ = this.oldCameFromZ;
        this.cameFromW = this.oldCameFromW;
    }
}
