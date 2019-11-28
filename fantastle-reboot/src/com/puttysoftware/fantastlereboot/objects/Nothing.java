package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Nothing extends FantastleObject {
  public Nothing() {
    super(71, "nothing", ObjectImageIndex.NOTHING);
    this.setSolid(true);
    this.setSightBlocking(true);
    this.setDestroyable(false);
  }
}
