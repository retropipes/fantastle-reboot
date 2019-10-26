package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall4 extends FantastleObject {
  public Wall4() {
    super(17, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "4",
        AttributeImageIndex.LARGE_NUMBER_4);
    this.setSolid(true);
    this.setSightBlocking(true);
  }
}
