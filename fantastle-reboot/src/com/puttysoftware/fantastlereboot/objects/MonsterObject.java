package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objectmodel.MonsterObjectModel;
import com.puttysoftware.images.BufferedImageIcon;

public final class MonsterObject extends FantastleObject
    implements MonsterObjectModel {
  public MonsterObject() {
    super(55, "monster", ObjectImageIndex.SHADOW_MONSTER);
    this.setDestroyable(false);
    this.setSavedObject(new OpenSpace());
  }

  @Override
  public BufferedImageIcon getGameImageHook() {
    return this.getSavedObject().getImage();
  }
}
