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
package com.puttysoftware.fantastlereboot.generic;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.editor.MazeEditor;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;

public abstract class GenericRandomInvisibleTeleport
        extends GenericRandomTeleport {
    // Constructors
    public GenericRandomInvisibleTeleport(final int newRandomRangeY,
            final int newRandomRangeX) {
        super(newRandomRangeY, newRandomRangeX);
    }

    // Scriptability
    @Override
    abstract public String getName();

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY,
            final ObjectInventory inv) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        int dr, dc;
        do {
            dr = this.getDestinationRow();
            dc = this.getDestinationColumn();
        } while (!app.getGameManager().tryUpdatePositionRelative(dr, dc));
        app.getGameManager().updatePositionRelative(dr, dc);
        Messager.showMessage("Invisible Teleport!");
        if (app.getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveSuccessSound();
        }
    }

    @Override
    public MazeObject editorPropertiesHook() {
        final MazeEditor me = FantastleReboot.getBagOStuff().getEditor();
        final MazeObject mo = me.editTeleportDestination(
                MazeEditor.TELEPORT_TYPE_RANDOM_INVISIBLE);
        return mo;
    }

    @Override
    public byte getGroupID() {
        return (byte) 17;
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_RANDOM_INVISIBLE_TELEPORT);
        this.type.set(TypeConstants.TYPE_RANDOM_TELEPORT);
    }
}