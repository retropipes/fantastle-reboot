package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objectmodel.Layer;

public final class Tile extends FantastleObject {
    public Tile() {
        super(1, "tile", ObjectImageIndex.TILE);
    }

    @Override
    public Layer getLayer() {
        return Layer.GROUND;
    }
}
