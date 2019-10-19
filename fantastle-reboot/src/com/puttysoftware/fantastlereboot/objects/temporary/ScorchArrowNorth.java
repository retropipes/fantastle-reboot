package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class ScorchArrowNorth extends FantastleObject {
    // Constructors
    public ScorchArrowNorth() {
        super(-1, "arrow_north", ObjectImageIndex.ARROW_NORTH,
                ColorShaders.scorch());
    }
}