package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodArrowEast extends FantastleObject {
    // Constructors
    public WoodArrowEast() {
        super(-5, "wood_arrow_east", ObjectImageIndex.ARROW_EAST,
                ColorShaders.wood());
    }
}
