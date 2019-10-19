package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class Amulet extends FantastleObject {
    public Amulet() {
        super(12, "amulet", ObjectImageIndex.AMULET, ColorShaders.love());
    }
}
