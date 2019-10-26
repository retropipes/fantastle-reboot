package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall3 extends FantastleObject {
  public Wall3() {
    super(16, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "3",
        AttributeImageIndex.LARGE_NUMBER_3);
    this.setSolid(true);
    this.setSightBlocking(true);
  }
}
