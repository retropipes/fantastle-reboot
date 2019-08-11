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
package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.creatures.PCManager;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.generic.GenericTrap;
import com.puttysoftware.fantastlereboot.loaders.assets.SoundManager;

public class EasierTrap extends GenericTrap {
    // Constructors
    public EasierTrap() {
        super();
    }

    @Override
    public String getName() {
        return "Easier Trap";
    }

    @Override
    public String getPluralName() {
        return "Easier Traps";
    }

    @Override
    public byte getObjectID() {
        return (byte) 12;
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY,
            final ObjectInventory inv) {
        Messager.showMessage("The monsters get weaker...");
        PCManager.getPlayer().decrementMonsterLevel();
        if (FantastleReboot.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            SoundManager.playSoundAsynchronously("easier");
        }
    }

    @Override
    public String getDescription() {
        return "Easier Traps make the monsters easier to defeat.";
    }
}