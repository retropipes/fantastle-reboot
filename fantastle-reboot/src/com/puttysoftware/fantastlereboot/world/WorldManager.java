/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.world;

public final class WorldManager {
  // Fields
  private static World gameWorld;

  // Constructors
  private WorldManager() {
  }

  // Methods
  public static World getWorld() {
    return WorldManager.gameWorld;
  }

  public static void setWorld(final World newWorld) {
    WorldManager.gameWorld = newWorld;
  }
}
