package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class FreezeArrowWest extends FantastleObject {
    // Constructors
    public FreezeArrowWest() {
        super(-1, "arrow_west", ObjectImageIndex.ARROW_WEST,
                ColorShaders.freeze());
    }
}
