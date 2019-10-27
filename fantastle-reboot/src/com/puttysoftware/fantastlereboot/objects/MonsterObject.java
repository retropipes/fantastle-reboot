package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objectmodel.MonsterObjectModel;

public abstract class MonsterObject extends FantastleObject implements MonsterObjectModel {
  protected MonsterObject(final boolean visible) {
    super(55, "monster", ObjectImageIndex.NORMAL_MONSTER);
    this.setDestroyable(false);
    if (!visible) {
      this.setGameLook("invisible_monster", ObjectImageIndex.OPEN_SPACE);
    }
    this.setSavedObject(new OpenSpace());
  }
}
