package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall9 extends FantastleObject {
  public Wall9() {
    super(22, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "9",
        AttributeImageIndex.LARGE_NUMBER_9);
    this.setSolid(true);
    this.setSightBlocking(true);
  }
}
