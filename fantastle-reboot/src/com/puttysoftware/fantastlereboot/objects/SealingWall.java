package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class SealingWall extends FantastleObject {
  public SealingWall() {
    super(54, "sealing_wall", ObjectImageIndex.SEALING_WALL);
    this.setSolid(true);
    this.setSightBlocking(true);
    this.setDestroyable(false);
  }
}
