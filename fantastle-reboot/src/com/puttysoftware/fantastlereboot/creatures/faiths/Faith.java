package com.puttysoftware.fantastlereboot.creatures.faiths;

import java.awt.Color;

import com.puttysoftware.diane.loaders.ColorShader;

public final class Faith {
  private final int faithID;

  Faith(final int fid) {
    this.faithID = fid;
  }

  public int getFaithID() {
    return this.faithID;
  }

  public int getFaithAdjustedDamage(final int otherFaithID,
      final int rawDamage) {
    return FaithManager.getFaithAdjustedDamage(otherFaithID, this.faithID,
        rawDamage);
  }

  public String getName() {
    return FaithManager.getFaithName(this.faithID);
  }

  public Color getColor() {
    return FaithManager.getFaithColor(this.faithID);
  }

  public ColorShader getShader() {
    return FaithManager.getFaithShader(this.faithID);
  }
}
