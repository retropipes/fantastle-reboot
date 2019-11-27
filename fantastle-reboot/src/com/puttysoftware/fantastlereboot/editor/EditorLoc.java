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

import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;

class EditorLoc {
  // Fields
  private static int oldLocX, oldLocY, oldLocZ;
  private static int oldLocW, oldLocE;
  private static int locX, locY, locZ;
  private static int locW, locE;
  private static int cameFromW, oldCameFromW;
  private static int cameFromZ, oldCameFromZ;
  private static int minX, minY, minZ, minW, minE;
  private static int maxW, maxE;
  private static int maxX, maxY, maxZ;

  // Constructors
  private EditorLoc() {
    super();
  }

  // Methods
  public static int getLocX() {
    return EditorLoc.locX;
  }

  public static int getLocY() {
    return EditorLoc.locY;
  }

  public static int getLocZ() {
    return EditorLoc.locZ;
  }

  public static int getLocW() {
    return EditorLoc.locW;
  }

  public static int getLocE() {
    return EditorLoc.locE;
  }

  public static int getMaxLocX() {
    return EditorLoc.maxX;
  }

  public static int getMaxLocY() {
    return EditorLoc.maxY;
  }

  public static int getMaxLocZ() {
    return EditorLoc.maxZ;
  }

  public static int getMaxLocW() {
    return EditorLoc.maxW;
  }

  public static int getMaxLocE() {
    return EditorLoc.maxE;
  }

  public static int getMinLocX() {
    return EditorLoc.minX;
  }

  public static int getMinLocY() {
    return EditorLoc.minY;
  }

  public static int getMinLocZ() {
    return EditorLoc.minZ;
  }

  public static int getMinLocW() {
    return EditorLoc.minW;
  }

  public static int getMinLocE() {
    return EditorLoc.minE;
  }

  public static int getCameFromZ() {
    return EditorLoc.cameFromZ;
  }

  public static int getCameFromW() {
    return EditorLoc.cameFromW;
  }

  public static void setLocX(final int val) {
    EditorLoc.locX = val;
    EditorLoc.checkLimits();
  }

  public static void setLocY(final int val) {
    EditorLoc.locY = val;
    EditorLoc.checkLimits();
  }

  public static void setLocZ(final int val) {
    EditorLoc.locZ = val;
    EditorLoc.checkLimits();
  }

  public static void setLocW(final int val) {
    EditorLoc.locW = val;
    EditorLoc.checkLimits();
  }

  public static void setLocE(final int val) {
    EditorLoc.locE = val;
    EditorLoc.checkLimits();
  }

  public static void setCameFromZ(final int val) {
    EditorLoc.cameFromZ = val;
  }

  public static void setCameFromW(final int val) {
    EditorLoc.cameFromW = val;
  }

  public static void setLoc(final int valX, final int valY, final int valZ,
      final int valW, final int valE) {
    EditorLoc.locW = valW;
    EditorLoc.locE = valE;
    EditorLoc.locX = valX;
    EditorLoc.locY = valY;
    EditorLoc.locZ = valZ;
    EditorLoc.checkLimits();
  }

  public static void offsetLoc(final int valX, final int valY, final int valZ,
      final int valW, final int valE) {
    EditorLoc.locW += valW;
    EditorLoc.locE += valE;
    EditorLoc.locX += valX;
    EditorLoc.locY += valY;
    EditorLoc.locZ += valZ;
    EditorLoc.checkLimits();
  }

  public static void setCameFrom(final int valZ, final int valW) {
    EditorLoc.cameFromW = valW;
    EditorLoc.cameFromZ = valZ;
    EditorLoc.checkLimits();
  }

  public static void offsetCameFrom(final int valZ, final int valW) {
    EditorLoc.cameFromW += valW;
    EditorLoc.cameFromZ += valZ;
    EditorLoc.checkLimits();
  }

  public static void offsetLocX(final int val) {
    EditorLoc.locX += val;
    EditorLoc.checkLimits();
  }

  public static void offsetLocY(final int val) {
    EditorLoc.locY += val;
    EditorLoc.checkLimits();
  }

  public static void offsetLocZ(final int val) {
    EditorLoc.locZ += val;
    EditorLoc.checkLimits();
  }

  public static void offsetLocW(final int val) {
    EditorLoc.locW += val;
    EditorLoc.checkLimits();
  }

  public static void offsetLocE(final int val) {
    EditorLoc.locE += val;
    EditorLoc.checkLimits();
  }

  public static void offsetCameFromZ(final int val) {
    EditorLoc.cameFromZ += val;
  }

  public static void offsetCameFromW(final int val) {
    EditorLoc.cameFromW += val;
  }

  public static void setLimitsFromMaze(final Maze m) {
    EditorLoc.minX = 0;
    EditorLoc.minY = 0;
    EditorLoc.minZ = 0;
    EditorLoc.minW = 0;
    EditorLoc.minE = 0;
    EditorLoc.maxW = Maze.getMaxLevels();
    EditorLoc.maxE = Layers.COUNT - 1;
    EditorLoc.maxX = m.getRows();
    EditorLoc.maxY = m.getColumns();
    EditorLoc.maxZ = m.getFloors() - 1;
  }

  public static void reset() {
    EditorLoc.locX = 0;
    EditorLoc.locY = 0;
    EditorLoc.locZ = 0;
    EditorLoc.locW = 0;
    EditorLoc.locE = 0;
    EditorLoc.oldLocX = 0;
    EditorLoc.oldLocY = 0;
    EditorLoc.oldLocZ = 0;
    EditorLoc.oldLocW = 0;
    EditorLoc.oldLocE = 0;
    EditorLoc.cameFromZ = 0;
    EditorLoc.cameFromW = 0;
    EditorLoc.oldCameFromZ = 0;
    EditorLoc.oldCameFromW = 0;
    EditorLoc.maxX = 0;
    EditorLoc.maxY = 0;
    EditorLoc.maxZ = 0;
    EditorLoc.maxW = 0;
    EditorLoc.maxE = 0;
    EditorLoc.minX = 0;
    EditorLoc.minY = 0;
    EditorLoc.minZ = 0;
    EditorLoc.minW = 0;
    EditorLoc.minE = 0;
  }

  private static void checkLimits() {
    // Check for limits out of bounds
    if (EditorLoc.locW < EditorLoc.minW) {
      EditorLoc.locW = EditorLoc.minW;
    }
    if (EditorLoc.locW > EditorLoc.maxW) {
      EditorLoc.locW = EditorLoc.maxW;
    }
    if (EditorLoc.locX < EditorLoc.minX) {
      EditorLoc.locX = EditorLoc.minX;
    }
    if (EditorLoc.locX > EditorLoc.maxX) {
      EditorLoc.locX = EditorLoc.maxX;
    }
    if (EditorLoc.locY < EditorLoc.minY) {
      EditorLoc.locY = EditorLoc.minY;
    }
    if (EditorLoc.locY > EditorLoc.maxY) {
      EditorLoc.locY = EditorLoc.maxY;
    }
    if (EditorLoc.locZ < EditorLoc.minZ) {
      EditorLoc.locZ = EditorLoc.minZ;
    }
    if (EditorLoc.locZ > EditorLoc.maxZ) {
      EditorLoc.locZ = EditorLoc.maxZ;
    }
    if (EditorLoc.locE < EditorLoc.minE) {
      EditorLoc.locE = EditorLoc.minE;
    }
    if (EditorLoc.locE > EditorLoc.maxE) {
      EditorLoc.locE = EditorLoc.maxE;
    }
  }

  public void save() {
    EditorLoc.oldLocX = EditorLoc.locX;
    EditorLoc.oldLocY = EditorLoc.locY;
    EditorLoc.oldLocZ = EditorLoc.locZ;
    EditorLoc.oldLocW = EditorLoc.locW;
    EditorLoc.oldLocE = EditorLoc.locE;
  }

  public void restore() {
    EditorLoc.locX = EditorLoc.oldLocX;
    EditorLoc.locY = EditorLoc.oldLocY;
    EditorLoc.locZ = EditorLoc.oldLocZ;
    EditorLoc.locW = EditorLoc.oldLocW;
    EditorLoc.locE = EditorLoc.oldLocE;
  }

  public void saveCameFrom() {
    EditorLoc.oldCameFromZ = EditorLoc.cameFromZ;
    EditorLoc.oldCameFromW = EditorLoc.cameFromW;
  }

  public void restoreCameFrom() {
    EditorLoc.cameFromZ = EditorLoc.oldCameFromZ;
    EditorLoc.cameFromW = EditorLoc.oldCameFromW;
  }
}
