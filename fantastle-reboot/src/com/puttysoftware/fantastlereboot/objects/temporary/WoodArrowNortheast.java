package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodArrowNortheast extends FantastleObject {
    // Constructors
    public WoodArrowNortheast() {
        super(-4, "wood_arrow_northeast", ObjectImageIndex.ARROW_NORTHEAST,
                ColorShaders.wooden());
    }
}
