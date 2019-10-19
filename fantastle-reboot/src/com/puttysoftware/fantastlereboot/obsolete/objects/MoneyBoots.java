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
package com.puttysoftware.fantastlereboot.obsolete.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;

public class MoneyBoots extends GenericBoots {
    // Constants
    private static final int GOLD_AMOUNT = 1;

    // Constructors
    public MoneyBoots() {
        super();
    }

    @Override
    public String getName() {
        return "Money Boots";
    }

    @Override
    public String getPluralName() {
        return "Pairs of Money Boots";
    }

    @Override
    public byte getObjectID() {
        return (byte) 10;
    }

    @Override
    public String getDescription() {
        return "Money Boots give you money as you walk. Note that you can only wear one pair of boots at once.";
    }

    @Override
    public void stepAction() {
        PartyManager.getParty().getLeader().offsetGold(MoneyBoots.GOLD_AMOUNT);
        FantastleReboot.getBagOStuff().getGameManager().getScoreTracker()
                .incrementScore();
    }
}
