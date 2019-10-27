package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.randomrange.RandomRange;

public final class Ice extends FantastleObject {
  public Ice() {
    super(72, "ice", ObjectImageIndex.ICE);
    this.setFriction(false);
  }

  @Override
  public int getLayer() {
    return Layers.GROUND;
  }

  @Override
  public boolean shouldGenerateObject(Maze inMaze, int inRow, int inCol,
      int inFloor, int inLevel, int inLayer) {
    // Generate Ice 20% of the time
    int dieRoll = RandomRange.generate(0, 4);
    if (dieRoll != 0)
      return false;
    return super.shouldGenerateObject(inMaze, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
