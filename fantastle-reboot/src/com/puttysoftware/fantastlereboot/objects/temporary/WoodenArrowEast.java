package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodenArrowEast extends FantastleObject {
    // Constructors
    public WoodenArrowEast() {
        super(-1, "arrow_east", ObjectImageIndex.ARROW_EAST,
                ColorShaders.wooden());
    }
}
