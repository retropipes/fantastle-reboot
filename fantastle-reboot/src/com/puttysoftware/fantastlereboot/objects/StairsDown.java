package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.world.World;

public final class StairsDown extends FantastleObject {
  public StairsDown() {
    super(75, "stairs_down", ObjectImageIndex.DOWN);
  }

  @Override
  public boolean shouldGenerateObject(final World inWorld, final int inRow,
      final int inCol, final int inFloor, final int inLevel,
      final int inLayer) {
    if (inFloor >= inWorld.getLevels() - 1) {
      return false;
    }
    return super.shouldGenerateObject(inWorld, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
