package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class StairsDown extends FantastleObject {
  public StairsDown() {
    super(69, "stairs_down", ObjectImageIndex.DOWN);
  }

  @Override
  public boolean shouldGenerateObject(final Maze inMaze, final int inRow,
      final int inCol, final int inFloor, final int inLevel,
      final int inLayer) {
    if (inFloor >= inMaze.getLevels() - 1) {
      return false;
    }
    return super.shouldGenerateObject(inMaze, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
