package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class BeautyArrowNorthwest extends FantastleObject {
  // Constructors
  public BeautyArrowNorthwest() {
    super(-1, "arrow_northwest", ObjectImageIndex.ARROW_NORTHWEST,
        ColorShaders.beauty());
  }
}
