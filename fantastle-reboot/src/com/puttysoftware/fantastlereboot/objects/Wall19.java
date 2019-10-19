package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall19 extends FantastleObject {
    public Wall19() {
        super(32, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "19",
                AttributeImageIndex.LARGE_NUMBER_19);
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
