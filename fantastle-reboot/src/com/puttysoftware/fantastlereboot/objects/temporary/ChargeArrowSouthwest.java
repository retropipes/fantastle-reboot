package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.ColorShaders;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

class ChargeArrowSouthwest extends FantastleObject {
    // Constructors
    public ChargeArrowSouthwest() {
        super(-1, "arrow_southwest", ObjectImageIndex.ARROW_SOUTHWEST,
                ColorShaders.charge());
    }
}
