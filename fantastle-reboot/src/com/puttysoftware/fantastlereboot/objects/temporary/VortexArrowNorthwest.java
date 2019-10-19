package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class VortexArrowNorthwest extends FantastleObject {
    // Constructors
    public VortexArrowNorthwest() {
        super(-1, "arrow_northwest", ObjectImageIndex.ARROW_NORTHWEST,
                ColorShaders.vortex());
    }
}
