/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.maze;

public final class MazeManager {
  // Fields
  private Maze gameMaze;

  // Constructors
  public MazeManager() {
  }

  // Methods
  public Maze getMaze() {
    return this.gameMaze;
  }

  public void setMaze(final Maze newMaze) {
    this.gameMaze = newMaze;
  }
}
