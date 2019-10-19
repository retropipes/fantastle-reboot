package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class VortexArrowNortheast extends FantastleObject {
    // Constructors
    public VortexArrowNortheast() {
        super(-1, "arrow_northeast", ObjectImageIndex.ARROW_NORTHEAST,
                ColorShaders.vortex());
    }
}
