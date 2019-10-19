package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall7 extends FantastleObject {
    public Wall7() {
        super(20, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "7",
                AttributeImageIndex.LARGE_NUMBER_7);
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
