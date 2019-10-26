package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall8 extends FantastleObject {
  public Wall8() {
    super(21, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "8",
        AttributeImageIndex.LARGE_NUMBER_8);
    this.setSolid(true);
    this.setSightBlocking(true);
  }
}
