package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class BeautyArrowSoutheast extends FantastleObject {
  // Constructors
  public BeautyArrowSoutheast() {
    super(-1, "arrow_southeast", ObjectImageIndex.ARROW_SOUTHEAST,
        ColorShaders.beauty());
  }
}
