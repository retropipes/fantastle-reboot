package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodArrowWest extends FantastleObject {
    // Constructors
    public WoodArrowWest() {
        super(-9, "wood_arrow_west", ObjectImageIndex.ARROW_WEST,
                ColorShaders.wood());
    }
}
