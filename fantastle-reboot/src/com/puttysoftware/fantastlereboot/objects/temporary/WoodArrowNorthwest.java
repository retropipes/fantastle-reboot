package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodArrowNorthwest extends FantastleObject {
    // Constructors
    public WoodArrowNorthwest() {
        super(-2, "wood_arrow_northwest", ObjectImageIndex.ARROW_NORTHWEST,
                ColorShaders.wooden());
    }
}
