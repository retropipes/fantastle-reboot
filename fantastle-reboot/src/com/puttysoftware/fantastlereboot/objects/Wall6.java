package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall6 extends FantastleObject {
    public Wall6() {
        super(19, "wall", ObjectImageIndex.WALL, ColorShaders.wood(), "6",
                AttributeImageIndex.LARGE_NUMBER_6, ColorShaders.none());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
