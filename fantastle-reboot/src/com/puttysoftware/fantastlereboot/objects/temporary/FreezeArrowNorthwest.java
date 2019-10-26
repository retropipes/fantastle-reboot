package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class FreezeArrowNorthwest extends FantastleObject {
  // Constructors
  public FreezeArrowNorthwest() {
    super(-1, "arrow_northwest", ObjectImageIndex.ARROW_NORTHWEST,
        ColorShaders.freeze());
  }
}
