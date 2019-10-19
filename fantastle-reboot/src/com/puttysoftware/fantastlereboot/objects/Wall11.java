package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall11 extends FantastleObject {
    public Wall11() {
        super(24, "wall", ObjectImageIndex.WALL, ColorShaders.wood(), "11",
                AttributeImageIndex.LARGE_NUMBER_11, ColorShaders.none());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
