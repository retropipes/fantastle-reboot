package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall14 extends FantastleObject {
    public Wall14() {
        super(27, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "14",
                AttributeImageIndex.LARGE_NUMBER_14, ColorShaders.normal());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
