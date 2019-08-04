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

import com.puttysoftware.fantastlereboot.Application;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.editor.MazeEditor;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.generic.GenericTeleport;
import com.puttysoftware.fantastlereboot.generic.MazeObject;

public class StairsDown extends GenericTeleport {
    // Constructors
    public StairsDown() {
        super(0, 0, 0, 0);
    }

    // For derived classes only
    protected StairsDown(final boolean doesAcceptPushInto) {
        super(doesAcceptPushInto);
    }

    @Override
    public String getName() {
        return "Stairs Down";
    }

    @Override
    public String getPluralName() {
        return "Sets of Stairs Down";
    }

    @Override
    public int getDestinationRow() {
        final Application app = FantastleReboot.getApplication();
        return app.getGameManager().getPlayerManager().getPlayerLocationX();
    }

    @Override
    public int getDestinationColumn() {
        final Application app = FantastleReboot.getApplication();
        return app.getGameManager().getPlayerManager().getPlayerLocationY();
    }

    @Override
    public int getDestinationFloor() {
        final Application app = FantastleReboot.getApplication();
        return app.getGameManager().getPlayerManager().getPlayerLocationZ() - 1;
    }

    @Override
    public int getDestinationLevel() {
        final Application app = FantastleReboot.getApplication();
        return app.getGameManager().getPlayerManager().getPlayerLocationW();
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY,
            final ObjectInventory inv) {
        final Application app = FantastleReboot.getApplication();
        app.getGameManager().updatePositionAbsoluteNoEvents(
                this.getDestinationRow(), this.getDestinationColumn(),
                this.getDestinationFloor(), this.getDestinationLevel());
        if (app.getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveSuccessSound();
        }
    }

    @Override
    public void editorPlaceHook() {
        final MazeEditor me = FantastleReboot.getApplication().getEditor();
        me.pairStairs(MazeEditor.STAIRS_DOWN);
    }

    @Override
    public MazeObject editorPropertiesHook() {
        return null;
    }

    @Override
    public String getMoveSuccessSoundName() {
        return "down";
    }

    @Override
    public byte getObjectID() {
        return (byte) 6;
    }

    @Override
    public String getDescription() {
        return "Stairs Down lead to the floor below.";
    }

    @Override
    public int getCustomFormat() {
        if (FantastleReboot.getApplication().getMazeManager().maze3Compatible()) {
            // Emulate older format bug
            return 4;
        } else {
            return 0;
        }
    }

    @Override
    public int getCustomProperty(final int propID) {
        return MazeObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
        // Do nothing
    }
}
