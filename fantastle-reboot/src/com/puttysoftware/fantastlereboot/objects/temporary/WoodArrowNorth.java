package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodArrowNorth extends FantastleObject {
    // Constructors
    public WoodArrowNorth() {
        super(-3, "wood_arrow_north", ObjectImageIndex.ARROW_NORTH,
                ColorShaders.wood());
    }
}
