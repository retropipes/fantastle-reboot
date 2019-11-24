package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class SuperPit extends FantastleObject {
  public SuperPit() {
    super(77, "stairs_down", ObjectImageIndex.SUPER_PIT);
  }

  @Override
  public boolean shouldGenerateObject(final Maze inMaze, final int inRow,
      final int inCol, final int inFloor, final int inLevel,
      final int inLayer) {
    if (inFloor < 2) {
      return false;
    }
    return super.shouldGenerateObject(inMaze, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
