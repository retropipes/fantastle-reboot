package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.world.World;

public final class SuperPit extends FantastleObject {
  public SuperPit() {
    super(77, "super_pit", ObjectImageIndex.SUPER_PIT);
  }

  @Override
  public boolean shouldGenerateObject(final World inWorld, final int inRow,
      final int inCol, final int inFloor, final int inLevel,
      final int inLayer) {
    if (inFloor < 2) {
      return false;
    }
    return super.shouldGenerateObject(inWorld, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
