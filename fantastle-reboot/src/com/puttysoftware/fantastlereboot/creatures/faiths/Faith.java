package com.puttysoftware.fantastlereboot.creatures.faiths;

import java.awt.Color;

import com.puttysoftware.fantastlereboot.loaders.DataLoader;

public final class Faith {
  private final int faithID;
  private final double[] multipliers;

  Faith(final int fid) {
    this.multipliers = DataLoader.loadFaithData(fid);
    this.faithID = fid;
  }

  public double getMultiplierForOtherFaith(final int fid) {
    return this.multipliers[fid];
  }

  public int getFaithID() {
    return this.faithID;
  }

  public String getName() {
    return FaithConstants.FAITH_NAMES[this.faithID];
  }

  public String getDamageType() {
    return FaithConstants.FAITH_DAMAGE_TYPES[this.faithID];
  }

  public Color getColor() {
    return FaithConstants.FAITH_COLORS[this.faithID];
  }
}
