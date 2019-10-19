package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class BeautyArrowEast extends FantastleObject {
    // Constructors
    public BeautyArrowEast() {
        super(-1, "arrow_east", ObjectImageIndex.ARROW_EAST,
                ColorShaders.wooden());
    }
}
