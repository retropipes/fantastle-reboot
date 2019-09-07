/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.ttmaze.effects;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.ttmain.TallerTower;
import com.puttysoftware.fantastlereboot.ttmaze.utilities.DirectionResolver;

public class MazeEffectManager {
    // Fields
    private final MazeEffect[] activeEffects;
    private static final int NUM_EFFECTS = 9;
    private static final int MAX_ACTIVE_EFFECTS = 3;
    private final Container activeEffectMessageContainer;
    private final JLabel[] activeEffectMessages;
    private int newEffectIndex;
    private final int[] activeEffectIndices;

    // Constructors
    public MazeEffectManager() {
        // Create effects array
        this.activeEffects = new MazeEffect[MazeEffectManager.NUM_EFFECTS];
        this.activeEffects[MazeEffectConstants.EFFECT_ROTATED_CLOCKWISE] = new RotatedCW(
                0);
        this.activeEffects[MazeEffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE] = new RotatedCCW(
                0);
        this.activeEffects[MazeEffectConstants.EFFECT_U_TURNED] = new UTurned(0);
        this.activeEffects[MazeEffectConstants.EFFECT_CONFUSED] = new Confused(
                0);
        this.activeEffects[MazeEffectConstants.EFFECT_DIZZY] = new Dizzy(0);
        this.activeEffects[MazeEffectConstants.EFFECT_DRUNK] = new Drunk(0);
        this.activeEffects[MazeEffectConstants.EFFECT_STICKY] = new Sticky(0);
        this.activeEffects[MazeEffectConstants.EFFECT_POWER_GATHER] = new PowerGather(
                0);
        this.activeEffects[MazeEffectConstants.EFFECT_POWER_WITHER] = new PowerWither(
                0);
        // Create GUI
        this.activeEffectMessageContainer = new Container();
        this.activeEffectMessages = new JLabel[MazeEffectManager.MAX_ACTIVE_EFFECTS];
        this.activeEffectMessageContainer.setLayout(new GridLayout(
                MazeEffectManager.MAX_ACTIVE_EFFECTS, 1));
        for (int z = 0; z < MazeEffectManager.MAX_ACTIVE_EFFECTS; z++) {
            this.activeEffectMessages[z] = new JLabel("");
            this.activeEffectMessageContainer.add(this.activeEffectMessages[z]);
        }
        // Set up miscellaneous things
        this.activeEffectIndices = new int[MazeEffectManager.MAX_ACTIVE_EFFECTS];
        for (int z = 0; z < MazeEffectManager.MAX_ACTIVE_EFFECTS; z++) {
            this.activeEffectIndices[z] = -1;
        }
        this.newEffectIndex = -1;
    }

    // Methods
    public Container getEffectMessageContainer() {
        return this.activeEffectMessageContainer;
    }

    public void decayEffects() {
        for (int x = 0; x < MazeEffectManager.NUM_EFFECTS; x++) {
            if (this.activeEffects[x].isActive()) {
                this.activeEffects[x].useEffect();
                // Update effect grid
                this.updateGridEntry(x);
                if (!this.activeEffects[x].isActive()) {
                    TallerTower.getApplication().showMessage(
                            "You feel normal again.");
                    // Clear effect grid
                    this.clearGridEntry(x);
                    // Pack
                    TallerTower.getApplication().getGameManager()
                            .getOutputFrame().pack();
                }
            }
        }
    }

    public void activateEffect(final int effectID) {
        this.activateEffectInternal(effectID,
                MazeEffectConstants.DURATIONS[FantastleReboot.getBagOStuff().getPrefsManager()
                        .getGameDifficulty()][effectID]);
    }

    private void activateEffectInternal(final int effectID, final int duration) {
        this.handleMutualExclusiveEffects(effectID);
        final boolean active = this.activeEffects[effectID].isActive();
        this.activeEffects[effectID].extendEffect(duration);
        // Update effect grid
        if (active) {
            this.updateGridEntry(effectID);
        } else {
            this.addGridEntry(effectID);
        }
        // Keep effect message
        TallerTower.getApplication().getGameManager().keepNextMessage();
    }

    public void deactivateEffect(final int effectID) {
        if (this.activeEffects[effectID].isActive()) {
            this.activeEffects[effectID].deactivateEffect();
            this.clearGridEntry(effectID);
        }
    }

    public void deactivateAllEffects() {
        for (int effectID = 0; effectID < MazeEffectManager.NUM_EFFECTS; effectID++) {
            if (this.activeEffects[effectID].isActive()) {
                this.activeEffects[effectID].deactivateEffect();
                this.clearGridEntry(effectID);
            }
        }
    }

    public boolean isEffectActive(final int effectID) {
        return this.activeEffects[effectID].isActive();
    }

    private void handleMutualExclusiveEffects(final int effectID) {
        if (effectID == MazeEffectConstants.EFFECT_ROTATED_CLOCKWISE) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE);
            this.deactivateEffect(MazeEffectConstants.EFFECT_U_TURNED);
        } else if (effectID == MazeEffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_ROTATED_CLOCKWISE);
            this.deactivateEffect(MazeEffectConstants.EFFECT_U_TURNED);
        } else if (effectID == MazeEffectConstants.EFFECT_U_TURNED) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_ROTATED_CLOCKWISE);
            this.deactivateEffect(MazeEffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE);
        } else if (effectID == MazeEffectConstants.EFFECT_CONFUSED) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_DIZZY);
            this.deactivateEffect(MazeEffectConstants.EFFECT_DRUNK);
        } else if (effectID == MazeEffectConstants.EFFECT_DIZZY) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_CONFUSED);
            this.deactivateEffect(MazeEffectConstants.EFFECT_DRUNK);
        } else if (effectID == MazeEffectConstants.EFFECT_DRUNK) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_CONFUSED);
            this.deactivateEffect(MazeEffectConstants.EFFECT_DIZZY);
        } else if (effectID == MazeEffectConstants.EFFECT_STICKY) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_POWER_GATHER);
            this.deactivateEffect(MazeEffectConstants.EFFECT_POWER_WITHER);
        } else if (effectID == MazeEffectConstants.EFFECT_POWER_GATHER) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_STICKY);
            this.deactivateEffect(MazeEffectConstants.EFFECT_POWER_WITHER);
        } else if (effectID == MazeEffectConstants.EFFECT_POWER_WITHER) {
            this.deactivateEffect(MazeEffectConstants.EFFECT_STICKY);
            this.deactivateEffect(MazeEffectConstants.EFFECT_POWER_GATHER);
        }
    }

    public int[] doEffects(final int x, final int y) {
        int[] res = new int[] { x, y };
        int dir = DirectionResolver.resolveRelativeDirection(x, y);
        for (int z = 0; z < MazeEffectManager.NUM_EFFECTS; z++) {
            if (this.activeEffects[z].isActive()) {
                dir = this.activeEffects[z].modifyMove1(dir);
                res = DirectionResolver.unresolveRelativeDirection(dir);
                res = this.activeEffects[z].modifyMove2(res);
            }
        }
        return res;
    }

    private void addGridEntry(final int effectID) {
        if (this.newEffectIndex < MazeEffectManager.MAX_ACTIVE_EFFECTS - 1) {
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
            for (int z = index; z < MazeEffectManager.MAX_ACTIVE_EFFECTS - 1; z++) {
                this.activeEffectMessages[z]
                        .setText(this.activeEffectMessages[z + 1].getText());
                this.activeEffectIndices[z] = this.activeEffectIndices[z + 1];
            }
            // Clear last entry
            this.clearGridEntryText(MazeEffectManager.MAX_ACTIVE_EFFECTS - 1);
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

    private int lookupEffect(final int effectID) {
        for (int z = 0; z < MazeEffectManager.MAX_ACTIVE_EFFECTS; z++) {
            if (this.activeEffectIndices[z] == effectID) {
                return z;
            }
        }
        return -1;
    }
}