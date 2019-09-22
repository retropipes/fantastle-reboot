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
package com.puttysoftware.fantastlereboot.effects;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.utilities.DirectionResolver;

public class EffectManager {
    // Fields
    private final Effect[] activeEffects;
    private final Container activeEffectMessageContainer;
    private final JLabel[] activeEffectMessages;
    private int newEffectIndex;
    private final int[] activeEffectIndices;
    private static final int NUM_EFFECTS = 9;
    private static final int MAX_ACTIVE_EFFECTS = 3;

    // Constructors
    public EffectManager() {
        this.activeEffects = new Effect[EffectManager.NUM_EFFECTS];
        this.activeEffects[EffectConstants.EFFECT_ROTATED_CLOCKWISE] = new RotatedCW(
                0);
        this.activeEffects[EffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE] = new RotatedCCW(
                0);
        this.activeEffects[EffectConstants.EFFECT_U_TURNED] = new UTurned(0);
        this.activeEffects[EffectConstants.EFFECT_CONFUSED] = new Confused(0);
        this.activeEffects[EffectConstants.EFFECT_DIZZY] = new Dizzy(0);
        this.activeEffects[EffectConstants.EFFECT_DRUNK] = new Drunk(0);
        this.activeEffects[EffectConstants.EFFECT_STICKY] = new Sticky(0);
        this.activeEffects[EffectConstants.EFFECT_POWER_GATHER] = new PowerGather(
                0);
        this.activeEffects[EffectConstants.EFFECT_POWER_WITHER] = new PowerWither(
                0);
        // Create GUI
        this.activeEffectMessageContainer = new Container();
        this.activeEffectMessages = new JLabel[EffectManager.MAX_ACTIVE_EFFECTS];
        this.activeEffectMessageContainer
                .setLayout(new GridLayout(EffectManager.MAX_ACTIVE_EFFECTS, 1));
        for (int z = 0; z < EffectManager.MAX_ACTIVE_EFFECTS; z++) {
            this.activeEffectMessages[z] = new JLabel("");
            this.activeEffectMessageContainer.add(this.activeEffectMessages[z]);
        }
        // Set up miscellaneous things
        this.activeEffectIndices = new int[EffectManager.MAX_ACTIVE_EFFECTS];
        for (int z = 0; z < EffectManager.MAX_ACTIVE_EFFECTS; z++) {
            this.activeEffectIndices[z] = -1;
        }
        this.newEffectIndex = -1;
    }

    // Methods
    public Container getEffectMessageContainer() {
        return this.activeEffectMessageContainer;
    }

    public void decayEffects() {
        for (int x = 0; x < EffectManager.NUM_EFFECTS; x++) {
            if (this.activeEffects[x].isActive()) {
                this.activeEffects[x].useEffect();
                // Update effect grid
                this.updateGridEntry(x);
                if (!this.activeEffects[x].isActive()) {
                    // Clear effect grid
                    this.clearGridEntry(x);
                    FantastleReboot.getBagOStuff().getGameManager()
                            .keepNextMessage();
                    Messager.showMessage("You feel normal again.");
                }
            }
        }
    }

    public void activateEffect(final int effectID, final int duration) {
        this.activeEffects[effectID].extendEffect(duration);
        this.handleMutualExclusiveEffects(effectID);
        final boolean active = this.activeEffects[effectID].isActive();
        // Update effect grid
        if (active) {
            this.updateGridEntry(effectID);
        } else {
            this.addGridEntry(effectID);
        }
    }

    private void addGridEntry(final int effectID) {
        if (this.newEffectIndex < EffectManager.MAX_ACTIVE_EFFECTS - 1) {
            this.newEffectIndex++;
            this.activeEffectIndices[this.newEffectIndex] = effectID;
            final String effectString = this.activeEffects[effectID]
                    .getEffectString();
            this.activeEffectMessages[this.newEffectIndex]
                    .setText(effectString);
        }
    }

    private void clearGridEntry(final int effectID) {
        final int index = this.lookupEffect(effectID);
        if (index != -1) {
            this.clearGridEntryText(index);
            // Compact grid
            for (int z = index; z < EffectManager.MAX_ACTIVE_EFFECTS - 1; z++) {
                this.activeEffectMessages[z]
                        .setText(this.activeEffectMessages[z + 1].getText());
                this.activeEffectIndices[z] = this.activeEffectIndices[z + 1];
            }
            // Clear last entry
            this.clearGridEntryText(EffectManager.MAX_ACTIVE_EFFECTS - 1);
            this.newEffectIndex--;
        }
    }

    private void clearGridEntryText(final int index) {
        this.activeEffectIndices[index] = -1;
        this.activeEffectMessages[index].setText("");
    }

    private void updateGridEntry(final int effectID) {
        final int index = this.lookupEffect(effectID);
        if (index != -1) {
            final String effectString = this.activeEffects[effectID]
                    .getEffectString();
            this.activeEffectMessages[index].setText(effectString);
        }
    }

    private void deactivateEffect(final int effectID) {
        this.activeEffects[effectID].deactivateEffect();
        this.clearGridEntry(effectID);
    }

    public void deactivateAllEffects() {
        for (int x = 0; x < EffectManager.NUM_EFFECTS; x++) {
            this.activeEffects[x].deactivateEffect();
            this.clearGridEntry(x);
        }
    }

    public boolean isEffectActive(final int effectID) {
        return this.activeEffects[effectID].isActive();
    }

    private int lookupEffect(final int effectID) {
        for (int z = 0; z < EffectManager.MAX_ACTIVE_EFFECTS; z++) {
            if (this.activeEffectIndices[z] == effectID) {
                return z;
            }
        }
        return -1;
    }

    private void handleMutualExclusiveEffects(final int effectID) {
        if (effectID == EffectConstants.EFFECT_ROTATED_CLOCKWISE) {
            this.deactivateEffect(
                    EffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE);
            this.deactivateEffect(EffectConstants.EFFECT_U_TURNED);
        } else if (effectID == EffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE) {
            this.deactivateEffect(EffectConstants.EFFECT_ROTATED_CLOCKWISE);
            this.deactivateEffect(EffectConstants.EFFECT_U_TURNED);
        } else if (effectID == EffectConstants.EFFECT_U_TURNED) {
            this.deactivateEffect(EffectConstants.EFFECT_ROTATED_CLOCKWISE);
            this.deactivateEffect(
                    EffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE);
        } else if (effectID == EffectConstants.EFFECT_CONFUSED) {
            this.deactivateEffect(EffectConstants.EFFECT_DIZZY);
            this.deactivateEffect(EffectConstants.EFFECT_DRUNK);
        } else if (effectID == EffectConstants.EFFECT_DIZZY) {
            this.deactivateEffect(EffectConstants.EFFECT_CONFUSED);
            this.deactivateEffect(EffectConstants.EFFECT_DRUNK);
        } else if (effectID == EffectConstants.EFFECT_DRUNK) {
            this.deactivateEffect(EffectConstants.EFFECT_CONFUSED);
            this.deactivateEffect(EffectConstants.EFFECT_DIZZY);
        } else if (effectID == EffectConstants.EFFECT_STICKY) {
            this.deactivateEffect(EffectConstants.EFFECT_POWER_GATHER);
            this.deactivateEffect(EffectConstants.EFFECT_POWER_WITHER);
        } else if (effectID == EffectConstants.EFFECT_POWER_GATHER) {
            this.deactivateEffect(EffectConstants.EFFECT_STICKY);
            this.deactivateEffect(EffectConstants.EFFECT_POWER_WITHER);
        } else if (effectID == EffectConstants.EFFECT_POWER_WITHER) {
            this.deactivateEffect(EffectConstants.EFFECT_STICKY);
            this.deactivateEffect(EffectConstants.EFFECT_POWER_GATHER);
        }
    }

    public int[] doEffects(final int x, final int y) {
        int[] res = new int[] { x, y };
        int dir = DirectionResolver.resolveRelativeDirection(x, y);
        for (int z = 0; z < EffectManager.NUM_EFFECTS; z++) {
            if (this.activeEffects[z].isActive()) {
                dir = this.activeEffects[z].modifyMove1(dir);
                res = DirectionResolver.unresolveRelativeDirection(dir);
                res = this.activeEffects[z].modifyMove2(res);
            }
        }
        return res;
    }
}