package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall16 extends FantastleObject {
    public Wall16() {
        super(29, "wall", ObjectImageIndex.WALL, ColorShaders.wood(), "16",
                AttributeImageIndex.LARGE_NUMBER_16, ColorShaders.none());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
