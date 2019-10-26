package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class VortexArrowWest extends FantastleObject {
  // Constructors
  public VortexArrowWest() {
    super(-1, "arrow_west", ObjectImageIndex.ARROW_WEST, ColorShaders.vortex());
  }
}
