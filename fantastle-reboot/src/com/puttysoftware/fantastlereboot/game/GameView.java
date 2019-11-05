/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.fantastlereboot.PreferencesManager;

public final class GameView {
  // Fields
  private static int oldLocX = 0, oldLocY = 0, locX = 0, locY = 0;

  // Constructors
  private GameView() {
    // Do nothing
  }

  // Methods
  public static int getViewingWindowLocationX() {
    return GameView.locX;
  }

  public static int getViewingWindowLocationY() {
    return GameView.locY;
  }

  public static int getUpperLeftViewingWindowLocationX() {
    return GameView.locX;
  }

  public static int getUpperLeftViewingWindowLocationY() {
    return GameView.locY;
  }

  public static int getLowerRightViewingWindowLocationX() {
    return GameView.locX
        + PreferencesManager.getViewingWindowSize() - 1;
  }

  public static int getLowerRightViewingWindowLocationY() {
    return GameView.locY
        + PreferencesManager.getViewingWindowSize() - 1;
  }

  public static void setViewingWindowLocationX(final int val) {
    GameView.locX = val;
  }

  public static void setViewingWindowLocationY(final int val) {
    GameView.locY = val;
  }

  public static void offsetViewingWindowLocationX(final int val) {
    GameView.locX += val;
  }

  public static void offsetViewingWindowLocationY(final int val) {
    GameView.locY += val;
  }

  public static void saveViewingWindow() {
    GameView.oldLocX = GameView.locX;
    GameView.oldLocY = GameView.locY;
  }

  public static void restoreViewingWindow() {
    GameView.locX = GameView.oldLocX;
    GameView.locY = GameView.oldLocY;
  }

  public static int getViewingWindowSizeX() {
    return PreferencesManager.getViewingWindowSize();
  }

  public static int getViewingWindowSizeY() {
    return PreferencesManager.getViewingWindowSize();
  }

  public static int getOffsetFactorX() {
    return PreferencesManager.getViewingWindowSize() / 2;
  }

  public static int getOffsetFactorY() {
    return PreferencesManager.getViewingWindowSize() / 2;
  }
}
