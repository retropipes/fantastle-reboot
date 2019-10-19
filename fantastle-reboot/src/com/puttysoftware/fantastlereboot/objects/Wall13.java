package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall13 extends FantastleObject {
    public Wall13() {
        super(26, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "13",
                AttributeImageIndex.LARGE_NUMBER_13);
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
