package com.puttysoftware.fantastlereboot.objectmodel;

import com.puttysoftware.fantastlereboot.world.World;

public interface RandomGenerationRule {
  int NO_LIMIT = 0;

  boolean shouldGenerateObject(World world, int row, int col, int floor,
      int level, int layer);

  int getMinimumRequiredQuantity(World world);

  int getMaximumRequiredQuantity(World world);

  boolean isRequired();
}
