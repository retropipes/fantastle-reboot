package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class OpenDoor extends FantastleObject {
  public OpenDoor() {
    super(80, "open_door", ObjectImageIndex.OPEN_DOOR, ColorShaders.wooden());
  }
}
