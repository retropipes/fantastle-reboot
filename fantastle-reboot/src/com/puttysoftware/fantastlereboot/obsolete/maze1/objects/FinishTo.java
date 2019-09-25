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

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.editor.MazeEditor;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.MazeObject;

public class FinishTo extends Finish {
    // Constructors
    public FinishTo() {
        super(0);
    }

    public FinishTo(final int newDestinationLevel) {
        super(newDestinationLevel);
    }

    // Scriptability
    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY,
            final ObjectInventory inv) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        if (app.getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveSuccessSound();
        }
        if (app.getGameManager().doesLevelExist(this.getDestinationLevel())) {
            Messager.showDialog("Level Solved!");
            app.getGameManager().solvedLevelWarp(this.getDestinationLevel());
        } else {
            Messager.showDialog("Maze Solved!");
            app.getGameManager().solvedMaze();
        }
    }

    @Override
    public String getName() {
        return "Finish To";
    }

    @Override
    public String getPluralName() {
        return "Finishes To";
    }

    @Override
    public void gameProbeHook() {
        Messager.showMessage(
                "Finish To Level " + (this.getDestinationLevel() + 1));
    }

    @Override
    public void editorProbeHook() {
        Messager.showMessage(
                "Finish To Level " + (this.getDestinationLevel() + 1));
    }

    @Override
    public MazeObject editorPropertiesHook() {
        final MazeEditor me = FantastleReboot.getBagOStuff().getEditor();
        final MazeObject mo = me.editFinishToDestination();
        return mo;
    }

    @Override
    public byte getObjectID() {
        return (byte) 2;
    }

    @Override
    public String getDescription() {
        return "Finishes To behave like regular Finishes, except that the level they send you to might not be the next one.";
    }

    @Override
    public int getCustomFormat() {
        return 1;
    }

    @Override
    public int getCustomProperty(final int propID) {
        return this.getDestinationLevel();
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
        this.setDestinationLevel(value);
    }
}