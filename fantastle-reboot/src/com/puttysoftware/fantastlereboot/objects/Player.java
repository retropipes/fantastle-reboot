package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Player extends FantastleObject {
  public Player() {
    super(71, "player", ObjectImageIndex.PLAYER);
    this.setSavedObject(new OpenSpace());
  }
}
