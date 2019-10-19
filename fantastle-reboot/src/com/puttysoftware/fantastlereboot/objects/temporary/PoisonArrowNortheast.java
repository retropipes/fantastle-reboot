package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public class PoisonArrowNortheast extends FantastleObject {
    // Constructors
    public PoisonArrowNortheast() {
        super(-1, "arrow_northeast", ObjectImageIndex.ARROW_NORTHEAST,
                ColorShaders.poison());
    }
}
