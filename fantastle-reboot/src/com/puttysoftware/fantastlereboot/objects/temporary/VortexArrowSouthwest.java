package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class VortexArrowSouthwest extends FantastleObject {
    // Constructors
    public VortexArrowSouthwest() {
        super(-1, "arrow_southwest", ObjectImageIndex.ARROW_SOUTHWEST,
                ColorShaders.vortex());
    }
}
