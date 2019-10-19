package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class WoodArrowSoutheast extends FantastleObject {
    // Constructors
    public WoodArrowSoutheast() {
        super(-6, "wood_arrow_southeast", ObjectImageIndex.ARROW_SOUTHEAST,
                ColorShaders.wood());
    }
}
