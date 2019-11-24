package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class SuperSpring extends FantastleObject {
  public SuperSpring() {
    super(78, "stairs_up", ObjectImageIndex.SUPER_SPRING);
  }

  @Override
  public boolean shouldGenerateObject(final Maze inMaze, final int inRow,
      final int inCol, final int inFloor, final int inLevel,
      final int inLayer) {
    if (inFloor >= inMaze.getFloors() - 2) {
      return false;
    }
    return super.shouldGenerateObject(inMaze, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
