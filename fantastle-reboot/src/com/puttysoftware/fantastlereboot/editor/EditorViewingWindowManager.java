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

import com.puttysoftware.fantastlereboot.obsolete.maze1.Maze;

public class EditorViewingWindowManager {
    // Fields
    private int oldLocX, oldLocY, locX, locY;
    private static final int VIEWING_WINDOW_SIZE_X = 13;
    private static final int VIEWING_WINDOW_SIZE_Y = 13;
    private static final int MIN_VIEWING_WINDOW_X = -(EditorViewingWindowManager.VIEWING_WINDOW_SIZE_X
            / 2);
    private static final int MIN_VIEWING_WINDOW_Y = -(EditorViewingWindowManager.VIEWING_WINDOW_SIZE_Y
            / 2);
    private int MAX_VIEWING_WINDOW_X;
    private int MAX_VIEWING_WINDOW_Y;

    // Constructors
    public EditorViewingWindowManager() {
        this.locX = EditorViewingWindowManager.MIN_VIEWING_WINDOW_X;
        this.locY = EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y;
        this.oldLocX = this.locX;
        this.oldLocY = this.locY;
        this.MAX_VIEWING_WINDOW_X = 0;
        this.MAX_VIEWING_WINDOW_Y = 0;
    }

    // Methods
    public int getViewingWindowLocationX() {
        return this.locX;
    }

    public int getViewingWindowLocationY() {
        return this.locY;
    }

    public int getUpperLeftViewingWindowLocationX() {
        return this.locX;
    }

    public int getUpperLeftViewingWindowLocationY() {
        return this.locY;
    }

    public int getLowerRightViewingWindowLocationX() {
        return this.locX + EditorViewingWindowManager.VIEWING_WINDOW_SIZE_X - 1;
    }

    public int getLowerRightViewingWindowLocationY() {
        return this.locY + EditorViewingWindowManager.VIEWING_WINDOW_SIZE_Y - 1;
    }

    public void setViewingWindowLocationX(final int val) {
        this.locX = val;
        this.checkViewingWindow();
    }

    public void setViewingWindowLocationY(final int val) {
        this.locY = val;
        this.checkViewingWindow();
    }

    public void offsetViewingWindowLocationX(final int val) {
        this.locX += val;
        this.checkViewingWindow();
    }

    public void offsetViewingWindowLocationY(final int val) {
        this.locY += val;
        this.checkViewingWindow();
    }

    public void saveViewingWindow() {
        this.oldLocX = this.locX;
        this.oldLocY = this.locY;
    }

    public void restoreViewingWindow() {
        this.locX = this.oldLocX;
        this.locY = this.oldLocY;
    }

    public static int getViewingWindowSizeX() {
        return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_X;
    }

    public static int getViewingWindowSizeY() {
        return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_Y;
    }

    public static int getMinimumViewingWindowLocationX() {
        return EditorViewingWindowManager.MIN_VIEWING_WINDOW_X;
    }

    public static int getMinimumViewingWindowLocationY() {
        return EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y;
    }

    public int getMaximumViewingWindowLocationX() {
        return this.MAX_VIEWING_WINDOW_X;
    }

    public int getMaximumViewingWindowLocationY() {
        return this.MAX_VIEWING_WINDOW_Y;
    }

    public void setMaximumViewingWindowLocationX(final int value) {
        this.MAX_VIEWING_WINDOW_X = value;
        this.checkViewingWindow();
    }

    public void setMaximumViewingWindowLocationY(final int value) {
        this.MAX_VIEWING_WINDOW_Y = value;
        this.checkViewingWindow();
    }

    public void halfOffsetMaximumViewingWindowLocationsFromMaze(final Maze m) {
        for (int x = 0; x < m.getLevels(); x++) {
            this.MAX_VIEWING_WINDOW_X = m.getRows(x) + EditorViewingWindowManager.getOffsetFactorX();
            this.MAX_VIEWING_WINDOW_Y = m.getColumns(x)
                    + EditorViewingWindowManager.getOffsetFactorY();
        }
    }

    public void halfOffsetMaximumViewingWindowLocation(final int valueX,
            final int valueY) {
        this.MAX_VIEWING_WINDOW_X = valueX + EditorViewingWindowManager.getOffsetFactorX();
        this.MAX_VIEWING_WINDOW_Y = valueY + EditorViewingWindowManager.getOffsetFactorY();
        this.checkViewingWindow();
    }

    public void halfOffsetMaximumViewingWindowLocationX(final int value) {
        this.MAX_VIEWING_WINDOW_X = value + EditorViewingWindowManager.getOffsetFactorX();
        this.checkViewingWindow();
    }

    public void halfOffsetMaximumViewingWindowLocationY(final int value) {
        this.MAX_VIEWING_WINDOW_Y = value + EditorViewingWindowManager.getOffsetFactorY();
        this.checkViewingWindow();
    }

    public void offsetMaximumViewingWindowLocationX(final int value) {
        this.MAX_VIEWING_WINDOW_X += value;
        this.checkViewingWindow();
    }

    public void offsetMaximumViewingWindowLocationY(final int value) {
        this.MAX_VIEWING_WINDOW_Y += value;
        this.checkViewingWindow();
    }

    public static int getOffsetFactorX() {
        return (EditorViewingWindowManager.VIEWING_WINDOW_SIZE_X - 1) / 2;
    }

    public static int getOffsetFactorY() {
        return (EditorViewingWindowManager.VIEWING_WINDOW_SIZE_Y - 1) / 2;
    }

    private void checkViewingWindow() {
        if (!this.isViewingWindowInBounds()) {
            this.fixViewingWindow();
        }
    }

    private boolean isViewingWindowInBounds() {
        if (this.locX >= EditorViewingWindowManager.MIN_VIEWING_WINDOW_X
                && this.locX <= this.MAX_VIEWING_WINDOW_X
                && this.locY >= EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y
                && this.locY <= this.MAX_VIEWING_WINDOW_Y) {
            return true;
        } else {
            return false;
        }
    }

    private void fixViewingWindow() {
        if (this.locX < EditorViewingWindowManager.MIN_VIEWING_WINDOW_X) {
            this.locX = EditorViewingWindowManager.MIN_VIEWING_WINDOW_X;
        }
        if (this.locX > this.MAX_VIEWING_WINDOW_X) {
            this.locX = this.MAX_VIEWING_WINDOW_X;
        }
        if (this.locY < EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y) {
            this.locY = EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y;
        }
        if (this.locY > this.MAX_VIEWING_WINDOW_Y) {
            this.locY = this.MAX_VIEWING_WINDOW_Y;
        }
    }
}
