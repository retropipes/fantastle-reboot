package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.world.World;
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
  public boolean shouldGenerateObject(final World inWorld, final int inRow,
      final int inCol, final int inFloor, final int inLevel,
      final int inLayer) {
    // Generate Ice 20% of the time
    final int dieRoll = RandomRange.generate(0, 4);
    if (dieRoll != 0) {
      return false;
    }
    return super.shouldGenerateObject(inWorld, inRow, inCol, inFloor, inLevel,
        inLayer);
  }
}
