package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class WallOff extends FantastleObject {
    public WallOff() {
        super(56, "wall_off", ObjectImageIndex.WALL_OFF, ColorShaders.normal());
        this.setSightBlocking(true);
    }
}
