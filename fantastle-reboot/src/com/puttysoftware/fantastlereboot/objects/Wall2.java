package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall2 extends FantastleObject {
    public Wall2() {
        super(15, "wall", ObjectImageIndex.WALL, ColorShaders.wood(), "2",
                AttributeImageIndex.LARGE_NUMBER_2, ColorShaders.none());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}