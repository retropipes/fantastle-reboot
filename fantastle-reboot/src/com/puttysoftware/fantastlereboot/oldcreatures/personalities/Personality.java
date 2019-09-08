package com.puttysoftware.fantastlereboot.oldcreatures.personalities;

import com.puttysoftware.fantastlereboot.loaders.DataLoader;

public class Personality {
    private final int[] data;
    private final int personalityID;

    Personality(final int pid) {
        this.data = DataLoader.loadPersonalityData(pid);
        this.personalityID = pid;
    }

    public int getAttribute(final int aid) {
        return this.data[aid];
    }

    public String getName() {
        return PersonalityConstants.PERSONALITY_NAMES[this.personalityID];
    }

    public int getPersonalityID() {
        return this.personalityID;
    }
}