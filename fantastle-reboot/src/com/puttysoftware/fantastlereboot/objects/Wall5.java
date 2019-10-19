package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall5 extends FantastleObject {
    public Wall5() {
        super(18, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "5",
                AttributeImageIndex.LARGE_NUMBER_5, ColorShaders.normal());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
