package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class ClosedDoor extends FantastleObject {
  public ClosedDoor() {
    super(79, "closed_door", ObjectImageIndex.DOOR, ColorShaders.door());
    this.setSightBlocking(true);
  }
}
