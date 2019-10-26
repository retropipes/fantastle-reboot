package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class MagnetArrowEast extends FantastleObject {
  // Constructors
  public MagnetArrowEast() {
    super(-1, "arrow_east", ObjectImageIndex.ARROW_EAST, ColorShaders.magnet());
  }
}
