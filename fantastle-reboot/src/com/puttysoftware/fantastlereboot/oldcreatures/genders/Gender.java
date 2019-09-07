package com.puttysoftware.fantastlereboot.oldcreatures.genders;

import com.puttysoftware.fantastlereboot.loaders.DataLoader;

public class Gender {
    private final int[] data;
    private final int genderID;

    Gender(final int gid) {
        this.data = DataLoader.loadGenderData(gid);
        this.genderID = gid;
    }

    public int getAttribute(final int aid) {
        return this.data[aid];
    }

    public String getName() {
        return GenderConstants.GENDER_NAMES[this.genderID];
    }

    public int getGenderID() {
        return this.genderID;
    }
}
