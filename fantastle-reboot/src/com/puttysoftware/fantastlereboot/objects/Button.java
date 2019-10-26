package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Button extends FantastleObject {
  public Button() {
    super(55, "button", ObjectImageIndex.BUTTON, ColorShaders.normal());
  }
}
