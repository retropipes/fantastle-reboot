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
package com.puttysoftware.fantastlereboot.obsolete.generic;

import java.util.Random;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.editor.MazeEditor;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.obsolete.maze1.Maze;
import com.puttysoftware.fantastlereboot.utilities.TypeConstants;

public abstract class GenericRandomTeleport extends MazeObject {
    // Fields
    private int randomRangeX;
    private int randomRangeY;
    private final Random generator;

    // Constructors
    public GenericRandomTeleport(final int newRandomRangeY,
            final int newRandomRangeX) {
        super(false);
        this.randomRangeX = newRandomRangeX;
        this.randomRangeY = newRandomRangeY;
        this.generator = new Random();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final GenericRandomTeleport other = (GenericRandomTeleport) obj;
        if (this.randomRangeX != other.randomRangeX) {
            return false;
        }
        if (this.randomRangeY != other.randomRangeY) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.randomRangeX;
        hash = 89 * hash + this.randomRangeY;
        return hash;
    }

    @Override
    public GenericRandomTeleport clone() {
        final GenericRandomTeleport copy = (GenericRandomTeleport) super.clone();
        copy.randomRangeX = this.randomRangeX;
        copy.randomRangeY = this.randomRangeY;
        return copy;
    }

    // Methods
    public int getDestinationRow() {
        if (this.randomRangeY == 0) {
            return 0;
        } else {
            int sign = this.generator.nextInt(2);
            final int value = this.generator.nextInt(this.randomRangeY + 1);
            if (sign == 0) {
                sign = -1;
            }
            return sign * value;
        }
    }

    public int getDestinationColumn() {
        if (this.randomRangeX == 0) {
            return 0;
        } else {
            int sign = this.generator.nextInt(2);
            final int value = this.generator.nextInt(this.randomRangeX + 1);
            if (sign == 0) {
                sign = -1;
            }
            return sign * value;
        }
    }

    public int getDestinationFloor() {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        return app.getGameManager().getPlayerManager().getPlayerLocationZ();
    }

    public int getDestinationLevel() {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        return app.getGameManager().getPlayerManager().getPlayerLocationW();
    }

    public int getRandomRowRange() {
        return this.randomRangeY;
    }

    public int getRandomColumnRange() {
        return this.randomRangeX;
    }

    @Override
    public int getLayer() {
        return Maze.LAYER_OBJECT;
    }

    // Scriptability
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
        FantastleReboot.getBagOStuff().getPrefsManager();
        if (app.getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveSuccessSound();
        }
    }

    @Override
    public void editorProbeHook() {
        Messager.showMessage(this.getName() + ": Row Radius "
                + this.randomRangeY + ", Column Radius " + this.randomRangeX);
    }

    @Override
    public MazeObject editorPropertiesHook() {
        final MazeEditor me = FantastleReboot.getBagOStuff().getEditor();
        final MazeObject mo = me
                .editTeleportDestination(MazeEditor.TELEPORT_TYPE_RANDOM);
        return mo;
    }

    @Override
    public byte getGroupID() {
        return (byte) 16;
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_RANDOM_TELEPORT);
    }

    @Override
    public void playMoveSuccessSound() {
        SoundLoader.playSound(GameSound.TELEPORT);
    }

    @Override
    public boolean hasAdditionalProperties() {
        return true;
    }

    @Override
    public int getCustomProperty(final int propID) {
        switch (propID) {
        case 1:
            return this.randomRangeX;
        case 2:
            return this.randomRangeY;
        default:
            return MazeObject.DEFAULT_CUSTOM_VALUE;
        }
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
        switch (propID) {
        case 1:
            this.randomRangeX = value;
            break;
        case 2:
            this.randomRangeY = value;
            break;
        default:
            break;
        }
    }

    @Override
    public int getCustomFormat() {
        return 2;
    }
}
