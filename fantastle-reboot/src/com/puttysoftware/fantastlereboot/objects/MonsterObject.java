package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class MonsterObject extends FantastleObject {
  public MonsterObject() {
    super(55, "monster", ObjectImageIndex.NORMAL_MONSTER);
    this.setDestroyable(false);
  }
}
