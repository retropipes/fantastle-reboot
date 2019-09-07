package com.puttysoftware.fantastlereboot.oldcreatures.castes;

import com.puttysoftware.fantastlereboot.loaders.DataLoader;

public class Caste {
    private final int[] data;
    private final int casteID;

    Caste(final int cid) {
        this.data = DataLoader.loadCasteData(cid);
        this.casteID = cid;
    }

    public int getAttribute(final int aid) {
        return this.data[aid];
    }

    public String getName() {
        return CasteConstants.CASTE_NAMES[this.casteID];
    }

    public int getCasteID() {
        return this.casteID;
    }
}
