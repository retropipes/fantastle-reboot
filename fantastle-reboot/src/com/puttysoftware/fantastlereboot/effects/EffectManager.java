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

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.generic.MazeObject;

public class EffectManager {
    // Fields
    private final Effect[] activeEffects;
    private static final int NUM_EFFECTS = 6;

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
    }

    // Methods
    public void decayEffects() {
        for (int x = 0; x < EffectManager.NUM_EFFECTS; x++) {
            if (this.activeEffects[x].isActive()) {
                this.activeEffects[x].useEffect();
                if (!this.activeEffects[x].isActive()) {
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
    }

    private void deactivateEffect(final int effectID) {
        this.activeEffects[effectID].deactivateEffect();
    }

    public void deactivateAllEffects() {
        for (int x = 0; x < EffectManager.NUM_EFFECTS; x++) {
            this.activeEffects[x].deactivateEffect();
        }
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
        }
    }

    public int[] doEffects(final int x, final int y) {
        int[] res = new int[] { x, y };
        int dir = MazeObject.resolveRelativeDirection(x, y);
        for (int z = 0; z < EffectManager.NUM_EFFECTS; z++) {
            if (this.activeEffects[z].isActive()) {
                dir = this.activeEffects[z].modifyMove1(dir);
                res = MazeObject.unresolveRelativeDirection(dir);
                res = this.activeEffects[z].modifyMove2(res);
            }
        }
        return res;
    }
}