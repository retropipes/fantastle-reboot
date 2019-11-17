/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze;

public final class MazeManager {
  // Fields
  private static Maze gameMaze;

  // Constructors
  private MazeManager() {
  }

  // Methods
  public static Maze getMaze() {
    return gameMaze;
  }

  public static void setMaze(final Maze newMaze) {
    gameMaze = newMaze;
  }
}
