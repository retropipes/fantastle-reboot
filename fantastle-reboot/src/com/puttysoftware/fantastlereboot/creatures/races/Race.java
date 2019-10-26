package com.puttysoftware.fantastlereboot.creatures.races;

import com.puttysoftware.fantastlereboot.loaders.DataLoader;

public class Race {
  private final int[] data;
  private final int raceID;

  Race(final int rid) {
    this.data = DataLoader.loadRaceData(rid);
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
