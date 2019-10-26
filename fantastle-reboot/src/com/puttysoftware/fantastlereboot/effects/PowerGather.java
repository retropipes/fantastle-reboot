/*  FantastleReboot: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.effects;

import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;

public class PowerGather extends Effect {
  // Constants
  private static final int MP_GAINED = 3;

  // Constructor
  public PowerGather(final int newRounds) {
    super("Power Gather", newRounds);
  }

  @Override
  public int modifyMove1(final int arg) {
    PartyManager.getParty().getLeader().offsetCurrentMP(MP_GAINED);
    return arg;
  }
}