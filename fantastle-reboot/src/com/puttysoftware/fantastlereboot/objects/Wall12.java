package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall12 extends FantastleObject {
    public Wall12() {
        super(25, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "12",
                AttributeImageIndex.LARGE_NUMBER_12);
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
