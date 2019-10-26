package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class WallOn extends FantastleObject {
  public WallOn() {
    super(57, "wall", ObjectImageIndex.WALL, ColorShaders.normal());
    this.setSolid(true);
    this.setSightBlocking(true);
  }
}
