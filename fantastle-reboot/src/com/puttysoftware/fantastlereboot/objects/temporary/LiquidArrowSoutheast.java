package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class LiquidArrowSoutheast extends FantastleObject {
  // Constructors
  public LiquidArrowSoutheast() {
    super(-1, "arrow_southeast", ObjectImageIndex.ARROW_SOUTHEAST,
        ColorShaders.liquid());
  }
}
