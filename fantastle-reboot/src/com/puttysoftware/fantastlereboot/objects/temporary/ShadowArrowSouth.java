package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class ShadowArrowSouth extends FantastleObject {
    // Constructors
    public ShadowArrowSouth() {
        super(-1, "arrow_south", ObjectImageIndex.ARROW_SOUTH,
                ColorShaders.shadow());
    }
}
