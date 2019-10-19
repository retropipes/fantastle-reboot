package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class PlasmaArrowNorthwest extends FantastleObject {
    // Constructors
    public PlasmaArrowNorthwest() {
        super(-1, "arrow_northwest", ObjectImageIndex.ARROW_NORTHWEST,
                ColorShaders.plasma());
    }
}
