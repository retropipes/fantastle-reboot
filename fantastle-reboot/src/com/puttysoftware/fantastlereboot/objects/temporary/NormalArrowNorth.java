package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class NormalArrowNorth extends FantastleObject {
    // Constructors
    public NormalArrowNorth() {
        super(-1, "arrow_north", ObjectImageIndex.ARROW_NORTH,
                ColorShaders.normal());
    }
}
