package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.world.World;

public final class Spring extends FantastleObject {
  public Spring() {
    super(74, "spring", ObjectImageIndex.SPRING);
  }

  @Override
  public boolean shouldGenerateObject(final World inWorld, final int inRow,
      final int inCol, final int inFloor, final int inLevel,
      final int inLayer) {
    if (inFloor >= World.getMaxFloors() - 1) {
      return false;
    }
    return super.shouldGenerateObject(inWorld, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
