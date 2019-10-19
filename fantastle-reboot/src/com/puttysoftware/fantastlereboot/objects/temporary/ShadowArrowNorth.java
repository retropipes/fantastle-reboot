package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class ShadowArrowNorth extends FantastleObject {
    // Constructors
    public ShadowArrowNorth() {
        super(-1, "arrow_north", ObjectImageIndex.ARROW_NORTH,
                ColorShaders.shadow());
    }
}
