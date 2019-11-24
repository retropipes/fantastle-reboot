package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Pit extends FantastleObject {
  public Pit() {
    super(75, "stairs_down", ObjectImageIndex.PIT);
  }

  @Override
  public boolean shouldGenerateObject(final Maze inMaze, final int inRow,
      final int inCol, final int inFloor, final int inLevel,
      final int inLayer) {
    if (inFloor < 1) {
      return false;
    }
    return super.shouldGenerateObject(inMaze, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
