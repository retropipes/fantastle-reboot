package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall10 extends FantastleObject {
    public Wall10() {
        super(23, "wall", ObjectImageIndex.WALL, ColorShaders.wood(), "10",
                AttributeImageIndex.LARGE_NUMBER_10, ColorShaders.none());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
