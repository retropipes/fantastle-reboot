/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.personalities;

import java.util.Arrays;

import com.puttysoftware.fantastlereboot.datamanagers.PersonalityDataManager;
import com.puttysoftware.fantastlereboot.descriptionmanagers.PersonalityDescriptionManager;

public final class Personality {
    private final int personalityID;
    private final double[] data;
    private final String desc;

    Personality(final int pid) {
        this.data = PersonalityDataManager.getPersonalityData(pid);
        this.desc = PersonalityDescriptionManager
                .getPersonalityDescription(pid);
        this.personalityID = pid;
    }

    public double getAttribute(final int aid) {
        return this.data[aid];
    }

    public String getDescription() {
        return this.desc;
    }

    public int getPersonalityID() {
        return this.personalityID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.personalityID;
        return prime * result + Arrays.hashCode(this.data);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Personality)) {
            return false;
        }
        final Personality other = (Personality) obj;
        if (this.personalityID != other.personalityID) {
            return false;
        }
        if (!Arrays.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }
}
