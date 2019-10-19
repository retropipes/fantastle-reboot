package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.fantastlereboot.objectmodel.Layer;

public final class Ice extends FantastleObject {
    public Ice() {
        super(72, "ice", ObjectImageIndex.ICE);
        this.setFriction(false);
    }

    @Override
    public Layer getLayer() {
        return Layer.GROUND;
    }
}
