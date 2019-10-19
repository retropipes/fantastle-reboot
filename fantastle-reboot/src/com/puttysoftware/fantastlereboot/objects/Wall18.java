package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall18 extends FantastleObject {
    public Wall18() {
        super(31, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "18",
                AttributeImageIndex.LARGE_NUMBER_18, ColorShaders.normal());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
