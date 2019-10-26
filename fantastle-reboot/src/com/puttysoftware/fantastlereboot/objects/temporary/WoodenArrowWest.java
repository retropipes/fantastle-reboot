package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class WoodenArrowWest extends FantastleObject {
  // Constructors
  public WoodenArrowWest() {
    super(-1, "arrow_west", ObjectImageIndex.ARROW_WEST, ColorShaders.wooden());
  }
}
