package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class PlasmaArrowWest extends FantastleObject {
    // Constructors
    public PlasmaArrowWest() {
        super(-1, "arrow_west", ObjectImageIndex.ARROW_WEST,
                ColorShaders.plasma());
    }
}
