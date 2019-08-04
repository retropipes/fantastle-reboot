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
package net.worldwizard.fantastle5.objects;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.Messager;
import net.worldwizard.fantastle5.PreferencesManager;
import net.worldwizard.fantastle5.creatures.PCManager;
import net.worldwizard.fantastle5.game.ObjectInventory;
import net.worldwizard.fantastle5.generic.GenericTrap;
import net.worldwizard.fantastle5.resourcemanagers.SoundManager;

public class HarderTrap extends GenericTrap {
    // Constructors
    public HarderTrap() {
        super();
    }

    @Override
    public String getName() {
        return "Harder Trap";
    }

    @Override
    public String getPluralName() {
        return "Harder Traps";
    }

    @Override
    public byte getObjectID() {
        return (byte) 13;
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        Messager.showMessage("The monsters get stronger...");
        PCManager.getPlayer().incrementMonsterLevel();
        if (Fantastle5.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            SoundManager.playSoundAsynchronously("harder");
        }
    }

    @Override
    public String getDescription() {
        return "Harder Traps make the monsters harder to defeat.";
    }
}