package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall0 extends FantastleObject {
  public Wall0() {
    super(13, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "0",
        AttributeImageIndex.LARGE_NUMBER_0);
    this.setSolid(true);
    this.setSightBlocking(true);
  }
}
