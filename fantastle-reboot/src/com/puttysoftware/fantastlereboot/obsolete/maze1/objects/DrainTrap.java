/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.obsolete.maze1.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.GenericTrap;
import com.puttysoftware.randomrange.RandomRange;

public class DrainTrap extends GenericTrap {
    // Fields
    private RandomRange amountDrained;
    private static final int MIN_DRAIN = -1;
    private int maxDrain;

    // Constructors
    public DrainTrap() {
        super();
    }

    @Override
    public String getName() {
        return "Drain Trap";
    }

    @Override
    public String getPluralName() {
        return "Drain Traps";
    }

    @Override
    public byte getObjectID() {
        return (byte) 11;
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY,
            final ObjectInventory inv) {
        this.maxDrain = PartyManager.getParty().getLeader().getMaximumMP() / 10;
        this.amountDrained = new RandomRange(this.maxDrain,
                DrainTrap.MIN_DRAIN);
        PartyManager.getParty().getLeader()
                .regenerate(this.amountDrained.generate());
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            SoundPlayer.playSound(SoundIndex.DRAIN);
        }
        FantastleReboot.getBagOStuff().getGameManager().decay();
    }

    @Override
    public String getDescription() {
        return "Drain Traps drain your magic when stepped on, then disappear.";
    }
}