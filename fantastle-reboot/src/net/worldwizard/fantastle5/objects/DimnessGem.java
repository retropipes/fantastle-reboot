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
import net.worldwizard.fantastle5.PreferencesManager;
import net.worldwizard.fantastle5.generic.GenericGem;
import net.worldwizard.fantastle5.generic.MazeObject;

public class DimnessGem extends GenericGem {
    // Constructors
    public DimnessGem() {
        super();
    }

    @Override
    public String getName() {
        return "Dimness Gem";
    }

    @Override
    public String getPluralName() {
        return "Dimness Gems";
    }

    @Override
    public byte getObjectID() {
        return (byte) 1;
    }

    @Override
    public void postMoveActionHook() {
        final int currLevel = Fantastle5.getApplication().getGameManager()
                .getPlayerManager().getPlayerLocationW();
        Fantastle5.getApplication().getMazeManager().getMaze()
                .decrementVisionRadius(currLevel);
        if (Fantastle5.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            MazeObject.playDarknessSound();
        }
    }

    @Override
    public String getMoveSuccessSoundName() {
        return "darkness";
    }

    @Override
    public String getDescription() {
        return "Dimness Gems decrease the visible area by 1.";
    }
}
