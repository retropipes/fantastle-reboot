package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Wall extends FantastleObject {
    public Wall() {
        super(53, "wall", ObjectImageIndex.WALL, ColorShaders.wood());
        this.setSolid(true);
        this.setSightBlocking(true);
    }
}
