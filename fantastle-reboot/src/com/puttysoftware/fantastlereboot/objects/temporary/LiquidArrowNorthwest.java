package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class LiquidArrowNorthwest extends FantastleObject {
    // Constructors
    public LiquidArrowNorthwest() {
        super(-1, "arrow_northwest", ObjectImageIndex.ARROW_NORTHWEST,
                ColorShaders.liquid());
    }
}
