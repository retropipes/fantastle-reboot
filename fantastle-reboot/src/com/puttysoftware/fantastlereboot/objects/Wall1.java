package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall1 extends FantastleObject {
    public Wall1() {
        super(14, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "1",
                AttributeImageIndex.LARGE_NUMBER_1);
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
