package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class MagnetArrowNorth extends FantastleObject {
    // Constructors
    public MagnetArrowNorth() {
        super(-1, "arrow_north", ObjectImageIndex.ARROW_NORTH,
                ColorShaders.magnet());
    }
}
