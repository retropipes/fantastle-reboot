/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.editor;

import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.maze.Maze;

final class EditorView {
  // Fields
  private static int oldLocX = 0, oldLocY = 0, locX = 0, locY = 0;
  private static int MAX_X;
  private static int MAX_Y;

  // Constructors
  private EditorView() {
    // Do nothing
  }

  // Methods
  public static int getLocX() {
    return EditorView.locX;
  }

  public static int getLocY() {
    return EditorView.locY;
  }

  public static int getUpperLeftLocX() {
    return EditorView.locX;
  }

  public static int getUpperLeftLocY() {
    return EditorView.locY;
  }

  public static int getLowerRightLocX() {
    return EditorView.locX + Prefs.getEditorWindowSize() - 1;
  }

  public static int getLowerRightLocY() {
    return EditorView.locY + Prefs.getEditorWindowSize() - 1;
  }

  public static void setLocX(final int val) {
    EditorView.locX = val;
  }

  public static void setLocY(final int val) {
    EditorView.locY = val;
  }

  public static void offsetLocX(final int val) {
    EditorView.locX += val;
  }

  public static void offsetLocY(final int val) {
    EditorView.locY += val;
  }

  public static void save() {
    EditorView.oldLocX = EditorView.locX;
    EditorView.oldLocY = EditorView.locY;
  }

  public static void restore() {
    EditorView.locX = EditorView.oldLocX;
    EditorView.locY = EditorView.oldLocY;
  }

  public static int getSizeX() {
    return Prefs.getEditorWindowSize();
  }

  public static int getSizeY() {
    return Prefs.getEditorWindowSize();
  }

  public static int getOffsetFactorX() {
    return Prefs.getEditorWindowSize() / 2;
  }

  public static int getOffsetFactorY() {
    return Prefs.getEditorWindowSize() / 2;
  }

  public static int getMinX() {
    return -(EditorView.getSizeX() / 2);
  }

  public static int getMinY() {
    return -(EditorView.getSizeY() / 2);
  }

  public static int getMaxX() {
    return EditorView.MAX_X;
  }

  public static int getMaxY() {
    return EditorView.MAX_Y;
  }

  public static void offsetMaxX(final int value) {
    EditorView.MAX_X += value;
    EditorView.checkViewingWindow();
  }

  public static void offsetMaxY(final int value) {
    EditorView.MAX_Y += value;
    EditorView.checkViewingWindow();
  }

  public static void setMaxX(final int value) {
    EditorView.MAX_X = value;
    EditorView.checkViewingWindow();
  }

  public static void setMaxY(final int value) {
    EditorView.MAX_Y = value;
    EditorView.checkViewingWindow();
  }

  public static void halfOffsetMaxFromMaze(final Maze m) {
    EditorView.setMaxX(m.getRows() + EditorView.getOffsetFactorX());
    EditorView.setMaxY(m.getColumns() + EditorView.getOffsetFactorY());
  }

  public static void halfOffsetMax(final int valueX, final int valueY) {
    EditorView.setMaxX(valueX + EditorView.getOffsetFactorX());
    EditorView.setMaxY(valueY + EditorView.getOffsetFactorY());
    EditorView.checkViewingWindow();
  }

  public static void halfOffsetMaxX(final int value) {
    EditorView.setMaxX(value + EditorView.getOffsetFactorX());
    EditorView.checkViewingWindow();
  }

  public static void halfOffsetMaxY(final int value) {
    EditorView.setMaxY(value + EditorView.getOffsetFactorY());
    EditorView.checkViewingWindow();
  }

  private static void checkViewingWindow() {
    if (!EditorView.isViewingWindowInBounds()) {
      EditorView.fixViewingWindow();
    }
  }

  private static boolean isViewingWindowInBounds() {
    if (EditorView.locX >= EditorView.getMinX()
        && EditorView.locX <= EditorView.getMaxX()
        && EditorView.locY >= EditorView.getMinY()
        && EditorView.locY <= EditorView.getMaxY()) {
      return true;
    } else {
      return false;
    }
  }

  private static void fixViewingWindow() {
    int minX = EditorView.getMinX();
    if (EditorView.locX < minX) {
      EditorView.locX = minX;
    }
    int maxX = EditorView.getMaxX();
    if (EditorView.locX > maxX) {
      EditorView.locX = maxX;
    }
    int minY = EditorView.getMinY();
    if (EditorView.locY < minY) {
      EditorView.locY = minY;
    }
    int maxY = EditorView.getMaxY();
    if (EditorView.locY > maxY) {
      EditorView.locY = maxY;
    }
  }
}
