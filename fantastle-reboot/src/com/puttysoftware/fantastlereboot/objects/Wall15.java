package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall15 extends FantastleObject {
    public Wall15() {
        super(28, "wall", ObjectImageIndex.WALL, ColorShaders.wooden(), "15",
                AttributeImageIndex.LARGE_NUMBER_15);
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
