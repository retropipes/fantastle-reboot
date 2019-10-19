package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodenArrowSouthwest extends FantastleObject {
    // Constructors
    public WoodenArrowSouthwest() {
        super(-1, "arrow_southwest", ObjectImageIndex.ARROW_SOUTHWEST,
                ColorShaders.wooden());
    }
}
