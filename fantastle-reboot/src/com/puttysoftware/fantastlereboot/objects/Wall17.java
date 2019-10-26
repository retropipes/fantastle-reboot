package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall17 extends FantastleObject {
  public Wall17() {
    super(30, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "17",
        AttributeImageIndex.LARGE_NUMBER_17);
    this.setSolid(true);
    this.setSightBlocking(true);
  }
}
