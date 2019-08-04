package net.worldwizard.fantastle5.creatures.races;

import net.worldwizard.fantastle5.resourcemanagers.datamanagers.RaceDataManager;

public class Race {
    private final int[] data;
    private final int raceID;

    Race(final int rid) {
        this.data = RaceDataManager.getRaceData(rid);
        this.raceID = rid;
    }

    public int getAttribute(final int aid) {
        return this.data[aid];
    }

    public String getName() {
        return RaceConstants.RACE_NAMES[this.raceID];
    }

    public int getRaceID() {
        return this.raceID;
    }
}
