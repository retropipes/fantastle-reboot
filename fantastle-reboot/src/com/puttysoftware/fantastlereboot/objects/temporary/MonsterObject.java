package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objectmodel.ObjectAppearance;

public abstract class MonsterObject extends FantastleObject {
  protected MonsterObject(final boolean visible) {
    super(55, "monster", ObjectImageIndex.NORMAL_MONSTER);
    this.setDestroyable(false);
    if (!visible) {
      this.setGameLook(new ObjectAppearance("invisible_monster", ObjectImageIndex.OPEN_SPACE));
    }
  }
}
