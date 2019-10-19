package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodArrowSouth extends FantastleObject {
    // Constructors
    public WoodArrowSouth() {
        super(-7, "wood_arrow_south", ObjectImageIndex.ARROW_SOUTH,
                ColorShaders.wood());
    }
}
