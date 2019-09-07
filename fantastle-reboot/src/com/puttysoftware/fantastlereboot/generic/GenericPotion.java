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

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.objects.Empty;
import com.puttysoftware.fantastlereboot.oldcreatures.PCManager;
import com.puttysoftware.fantastlereboot.oldcreatures.StatConstants;
import com.puttysoftware.randomrange.RandomRange;

public abstract class GenericPotion extends MazeObject
        implements StatConstants {
    // Fields
    private int effectValue;
    private RandomRange effect;
    private int statAffected;
    private boolean effectValueIsPercentage;

    // Constructors
    protected GenericPotion(final int stat, final boolean usePercent) {
        super(false);
        this.statAffected = stat;
        this.effectValueIsPercentage = usePercent;
    }

    protected GenericPotion(final int stat, final boolean usePercent,
            final int min, final int max) {
        super(false);
        this.statAffected = stat;
        this.effectValueIsPercentage = usePercent;
        this.effect = new RandomRange(min, max);
    }

    @Override
    public GenericPotion clone() {
        final GenericPotion copy = (GenericPotion) super.clone();
        copy.effectValue = this.effectValue;
        copy.effectValueIsPercentage = this.effectValueIsPercentage;
        copy.statAffected = this.statAffected;
        copy.effect = this.effect;
        return copy;
    }

    @Override
    public abstract String getName();

    @Override
    public int getLayer() {
        return Maze.LAYER_OBJECT;
    }

    @Override
    public byte getGroupID() {
        return (byte) 30;
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_POTION);
        this.type.set(TypeConstants.TYPE_CONTAINABLE);
    }

    @Override
    public final void postMoveAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        if (this.effect != null) {
            this.effectValue = this.effect.generate();
        } else {
            this.effectValue = this.getEffectValue();
        }
        if (this.effectValueIsPercentage) {
            if (this.statAffected == StatConstants.STAT_CURRENT_HP) {
                if (this.effectValue >= 0) {
                    PCManager.getPlayer().healPercentage(this.effectValue);
                } else {
                    PCManager.getPlayer().doDamagePercentage(-this.effectValue);
                }
            } else if (this.statAffected == StatConstants.STAT_CURRENT_MP) {
                if (this.effectValue >= 0) {
                    PCManager.getPlayer()
                            .regeneratePercentage(this.effectValue);
                } else {
                    PCManager.getPlayer().drainPercentage(-this.effectValue);
                }
            }
        } else {
            if (this.statAffected == StatConstants.STAT_CURRENT_HP) {
                if (this.effectValue >= 0) {
                    PCManager.getPlayer().heal(this.effectValue);
                } else {
                    PCManager.getPlayer().doDamage(-this.effectValue);
                }
            } else if (this.statAffected == StatConstants.STAT_CURRENT_MP) {
                if (this.effectValue >= 0) {
                    PCManager.getPlayer().regenerate(this.effectValue);
                } else {
                    PCManager.getPlayer().drain(-this.effectValue);
                }
            }
        }
        FantastleReboot.getApplication().getGameManager().decay();
        if (this.effectValue >= 0) {
            if (FantastleReboot.getApplication().getPrefsManager()
                    .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
                SoundLoader.playSound(GameSound.HEAL);
            }
        } else {
            if (FantastleReboot.getApplication().getPrefsManager()
                    .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
                SoundLoader.playSound(GameSound.HURT);
            }
        }
    }

    @Override
    public boolean arrowHitAction(final int locX, final int locY,
            final int locZ, final int locW, final int dirX, final int dirY,
            final int arrowType, final ObjectInventory inv) {
        FantastleReboot.getApplication().getGameManager().morph(new Empty(), locX,
                locY, locZ, locW);
        if (FantastleReboot.getApplication().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            SoundLoader.playSound(GameSound.CRUSH);
        }
        return false;
    }

    public int getEffectValue() {
        if (this.effect != null) {
            return this.effect.generate();
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