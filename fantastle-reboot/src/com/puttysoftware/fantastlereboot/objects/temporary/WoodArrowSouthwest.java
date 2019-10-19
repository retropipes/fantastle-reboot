package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodArrowSouthwest extends FantastleObject {
    // Constructors
    public WoodArrowSouthwest() {
        super(-8, "wood_arrow_southwest", ObjectImageIndex.ARROW_SOUTHWEST,
                ColorShaders.wood());
    }
}
