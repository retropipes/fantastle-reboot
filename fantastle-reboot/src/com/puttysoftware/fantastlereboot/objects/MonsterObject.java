package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objectmodel.MonsterObjectModel;
import com.puttysoftware.images.BufferedImageIcon;

public abstract class MonsterObject extends FantastleObject
    implements MonsterObjectModel {
  private final boolean isVisible;

  protected MonsterObject(final boolean visible) {
    super(55, "monster", ObjectImageIndex.SHADOW_MONSTER);
    this.isVisible = visible;
    this.setDestroyable(false);
    this.setSavedObject(new OpenSpace());
  }

  @Override
  public BufferedImageIcon getGameImageHook() {
    if (!this.isVisible) {
      return this.getSavedObject().getImage();
    }
    return super.getGameImageHook();
  }
}
