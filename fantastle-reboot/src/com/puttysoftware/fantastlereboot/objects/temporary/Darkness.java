package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Darkness extends FantastleObject {
  public Darkness() {
    super(-5, "darkness", ObjectImageIndex.DARKNESS);
    this.setDestroyable(false);
  }
}
