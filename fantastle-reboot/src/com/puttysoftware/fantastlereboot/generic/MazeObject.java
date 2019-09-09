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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.assets.GameSound;
import com.puttysoftware.fantastlereboot.game.ObjectInventory;
import com.puttysoftware.fantastlereboot.loaders.SoundLoader;
import com.puttysoftware.fantastlereboot.maze.FormatConstants;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public abstract class MazeObject implements DirectionConstants, TypeConstants,
        ArrowTypeConstants, UniqueID5 {
    // Properties
    private SolidProperties sp;
    private MoveProperties mp;
    private boolean friction;
    private boolean usable;
    private int uses;
    private boolean destroyable;
    private boolean chainReacts;
    private boolean isInventoryable;
    protected BitSet type;
    private int timerValue;
    private int initialTimerValue;
    private boolean timerActive;
    public static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;

    // Old-Style Constructors
    public MazeObject(final boolean isSolid) {
        this.sp = new SolidProperties();
        this.sp.setSolid(isSolid);
        this.mp = new MoveProperties();
        this.friction = true;
        this.usable = false;
        this.uses = 0;
        this.destroyable = true;
        this.chainReacts = false;
        this.isInventoryable = false;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    public MazeObject(final boolean isSolid, final boolean isDestroyable) {
        this.sp = new SolidProperties();
        this.sp.setSolid(isSolid);
        this.mp = new MoveProperties();
        this.friction = true;
        this.usable = false;
        this.uses = 0;
        this.destroyable = isDestroyable;
        this.chainReacts = false;
        this.isInventoryable = false;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    public MazeObject(final boolean isSolidXN, final boolean isSolidXS,
            final boolean isSolidXE, final boolean isSolidXW,
            final boolean isSolidIN, final boolean isSolidIS,
            final boolean isSolidIE, final boolean isSolidIW) {
        this.sp = new SolidProperties();
        this.sp.setDirectionallySolid(true, DirectionConstants.DIRECTION_NORTH,
                isSolidXN);
        this.sp.setDirectionallySolid(true, DirectionConstants.DIRECTION_SOUTH,
                isSolidXS);
        this.sp.setDirectionallySolid(true, DirectionConstants.DIRECTION_EAST,
                isSolidXE);
        this.sp.setDirectionallySolid(true, DirectionConstants.DIRECTION_WEST,
                isSolidXW);
        this.sp.setDirectionallySolid(false, DirectionConstants.DIRECTION_NORTH,
                isSolidIN);
        this.sp.setDirectionallySolid(false, DirectionConstants.DIRECTION_SOUTH,
                isSolidIS);
        this.sp.setDirectionallySolid(false, DirectionConstants.DIRECTION_EAST,
                isSolidIE);
        this.sp.setDirectionallySolid(false, DirectionConstants.DIRECTION_WEST,
                isSolidIW);
        this.sp.setDirectionallySolid(true,
                DirectionConstants.DIRECTION_NORTHEAST, true);
        this.sp.setDirectionallySolid(true,
                DirectionConstants.DIRECTION_SOUTHEAST, true);
        this.sp.setDirectionallySolid(true,
                DirectionConstants.DIRECTION_NORTHWEST, true);
        this.sp.setDirectionallySolid(true,
                DirectionConstants.DIRECTION_SOUTHWEST, true);
        this.sp.setDirectionallySolid(false,
                DirectionConstants.DIRECTION_NORTHEAST, true);
        this.sp.setDirectionallySolid(false,
                DirectionConstants.DIRECTION_SOUTHEAST, true);
        this.sp.setDirectionallySolid(false,
                DirectionConstants.DIRECTION_NORTHWEST, true);
        this.sp.setDirectionallySolid(false,
                DirectionConstants.DIRECTION_SOUTHWEST, true);
        this.mp = new MoveProperties();
        this.friction = true;
        this.usable = false;
        this.uses = 0;
        this.destroyable = true;
        this.chainReacts = false;
        this.isInventoryable = false;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    public MazeObject(final boolean isSolidXN, final boolean isSolidXS,
            final boolean isSolidXE, final boolean isSolidXW,
            final boolean isSolidIN, final boolean isSolidIS,
            final boolean isSolidIE, final boolean isSolidIW,
            final boolean isSolidXNW, final boolean isSolidXSE,
            final boolean isSolidXNE, final boolean isSolidXSW,
            final boolean isSolidINW, final boolean isSolidISE,
            final boolean isSolidINE, final boolean isSolidISW) {
        this.sp = new SolidProperties();
        this.sp.setDirectionallySolid(true, DirectionConstants.DIRECTION_NORTH,
                isSolidXN);
        this.sp.setDirectionallySolid(true, DirectionConstants.DIRECTION_SOUTH,
                isSolidXS);
        this.sp.setDirectionallySolid(true, DirectionConstants.DIRECTION_EAST,
                isSolidXE);
        this.sp.setDirectionallySolid(true, DirectionConstants.DIRECTION_WEST,
                isSolidXW);
        this.sp.setDirectionallySolid(false, DirectionConstants.DIRECTION_NORTH,
                isSolidIN);
        this.sp.setDirectionallySolid(false, DirectionConstants.DIRECTION_SOUTH,
                isSolidIS);
        this.sp.setDirectionallySolid(false, DirectionConstants.DIRECTION_EAST,
                isSolidIE);
        this.sp.setDirectionallySolid(false, DirectionConstants.DIRECTION_WEST,
                isSolidIW);
        this.sp.setDirectionallySolid(true,
                DirectionConstants.DIRECTION_NORTHEAST, isSolidXNE);
        this.sp.setDirectionallySolid(true,
                DirectionConstants.DIRECTION_SOUTHEAST, isSolidXSE);
        this.sp.setDirectionallySolid(true,
                DirectionConstants.DIRECTION_NORTHWEST, isSolidXNW);
        this.sp.setDirectionallySolid(true,
                DirectionConstants.DIRECTION_SOUTHWEST, isSolidXSW);
        this.sp.setDirectionallySolid(false,
                DirectionConstants.DIRECTION_NORTHEAST, isSolidINE);
        this.sp.setDirectionallySolid(false,
                DirectionConstants.DIRECTION_SOUTHEAST, isSolidISE);
        this.sp.setDirectionallySolid(false,
                DirectionConstants.DIRECTION_NORTHWEST, isSolidINW);
        this.sp.setDirectionallySolid(false,
                DirectionConstants.DIRECTION_SOUTHWEST, isSolidISW);
        this.mp = new MoveProperties();
        this.friction = true;
        this.usable = false;
        this.uses = 0;
        this.destroyable = true;
        this.chainReacts = false;
        this.isInventoryable = false;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    public MazeObject(final boolean isSolid, final boolean pushable,
            final boolean doesAcceptPushInto, final boolean doesAcceptPushOut,
            final boolean pullable, final boolean doesAcceptPullInto,
            final boolean doesAcceptPullOut, final boolean hasFriction,
            final boolean isUsable, final int newUses) {
        this.sp = new SolidProperties();
        this.sp.setSolid(isSolid);
        this.mp = new MoveProperties();
        this.mp.setPushable(pushable);
        this.mp.setPushableInto(doesAcceptPushInto);
        this.mp.setPushableOut(doesAcceptPushOut);
        this.mp.setPullable(pullable);
        this.mp.setPullableInto(doesAcceptPullInto);
        this.mp.setPullableOut(doesAcceptPullOut);
        this.friction = hasFriction;
        this.usable = isUsable;
        this.uses = newUses;
        this.destroyable = true;
        this.chainReacts = false;
        this.isInventoryable = false;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    public MazeObject(final boolean isSolid, final boolean pushable,
            final boolean doesAcceptPushInto, final boolean doesAcceptPushOut,
            final boolean pullable, final boolean doesAcceptPullInto,
            final boolean doesAcceptPullOut, final boolean hasFriction,
            final boolean isUsable, final int newUses,
            final boolean isDestroyable, final boolean doesChainReact) {
        this.sp = new SolidProperties();
        this.sp.setSolid(isSolid);
        this.mp = new MoveProperties();
        this.mp.setPushable(pushable);
        this.mp.setPushableInto(doesAcceptPushInto);
        this.mp.setPushableOut(doesAcceptPushOut);
        this.mp.setPullable(pullable);
        this.mp.setPullableInto(doesAcceptPullInto);
        this.mp.setPullableOut(doesAcceptPullOut);
        this.friction = hasFriction;
        this.usable = isUsable;
        this.uses = newUses;
        this.destroyable = isDestroyable;
        this.chainReacts = doesChainReact;
        this.isInventoryable = false;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    public MazeObject(final boolean isSolid, final boolean isUsable,
            final int newUses, final boolean canBeInventoried) {
        this.sp = new SolidProperties();
        this.sp.setSolid(isSolid);
        this.mp = new MoveProperties();
        this.friction = true;
        this.usable = isUsable;
        this.uses = newUses;
        this.destroyable = true;
        this.chainReacts = false;
        this.isInventoryable = canBeInventoried;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    // New-Style Constructors
    public MazeObject() {
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.friction = true;
        this.usable = false;
        this.uses = 0;
        this.destroyable = true;
        this.chainReacts = false;
        this.isInventoryable = false;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    public MazeObject(final boolean hasFriction, final boolean isUsable,
            final int startingUses, final boolean destroy, final boolean chain,
            final boolean inventory) {
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.friction = hasFriction;
        this.usable = isUsable;
        this.uses = startingUses;
        this.destroyable = destroy;
        this.chainReacts = chain;
        this.isInventoryable = inventory;
        this.type = new BitSet(TypeConstants.TYPES_COUNT);
        this.timerValue = 0;
        this.timerActive = false;
        this.setTypes();
    }

    // Methods
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final MazeObject other = (MazeObject) obj;
        if (this.sp != other.sp
                && (this.sp == null || !this.sp.equals(other.sp))) {
            return false;
        }
        if (this.mp != other.mp
                && (this.mp == null || !this.mp.equals(other.mp))) {
            return false;
        }
        if (this.friction != other.friction) {
            return false;
        }
        if (this.usable != other.usable) {
            return false;
        }
        if (this.uses != other.uses) {
            return false;
        }
        if (this.destroyable != other.destroyable) {
            return false;
        }
        if (this.chainReacts != other.chainReacts) {
            return false;
        }
        if (this.isInventoryable != other.isInventoryable) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.sp != null ? this.sp.hashCode() : 0);
        hash = 59 * hash + (this.mp != null ? this.mp.hashCode() : 0);
        hash = 59 * hash + (this.friction ? 1 : 0);
        hash = 59 * hash + (this.usable ? 1 : 0);
        hash = 59 * hash + this.uses;
        hash = 59 * hash + (this.destroyable ? 1 : 0);
        hash = 59 * hash + (this.chainReacts ? 1 : 0);
        hash = 59 * hash + (this.isInventoryable ? 1 : 0);
        return hash;
    }

    @Override
    public MazeObject clone() {
        try {
            final MazeObject copy = this.getClass().getConstructor()
                    .newInstance();
            copy.sp = this.sp.clone();
            copy.mp = this.mp.clone();
            copy.friction = this.friction;
            copy.usable = this.usable;
            copy.uses = this.uses;
            copy.destroyable = this.destroyable;
            copy.chainReacts = this.chainReacts;
            copy.isInventoryable = this.isInventoryable;
            copy.type = (BitSet) this.type.clone();
            copy.timerValue = this.timerValue;
            copy.initialTimerValue = this.initialTimerValue;
            copy.timerActive = this.timerActive;
            return copy;
        } catch (final InstantiationException e) {
            return null;
        } catch (final IllegalAccessException e) {
            return null;
        } catch (final IllegalArgumentException e) {
            return null;
        } catch (final InvocationTargetException e) {
            return null;
        } catch (final NoSuchMethodException e) {
            return null;
        } catch (final SecurityException e) {
            return null;
        }
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallySolid(final ObjectInventory inv) {
        return this.sp.isSolid();
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallySolid(final boolean ie,
            final int dirX, final int dirY, final ObjectInventory inv) {
        return this.sp.isDirectionallySolid(ie, dirX, dirY);
    }

    public boolean isSolid() {
        return this.sp.isSolid();
    }

    public boolean isDirectionallySolid(final boolean ie, final int dirX,
            final int dirY) {
        return this.sp.isDirectionallySolid(ie, dirX, dirY);
    }

    protected void setSolid(final boolean value) {
        this.sp.setSolid(value);
    }

    protected void setDirectionallySolid(final boolean ie, final int dir,
            final boolean value) {
        this.sp.setDirectionallySolid(ie, dir, value);
    }

    public boolean isOfType(final int testType) {
        return this.type.get(testType);
    }

    protected abstract void setTypes();

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPushable(final ObjectInventory inv) {
        return this.mp.isPushable();
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPushable(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPushable(dirX, dirY);
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPullable(final ObjectInventory inv) {
        return this.mp.isPullable();
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPullable(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPullable(dirX, dirY);
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPullableInto(final ObjectInventory inv) {
        return this.mp.isPullableInto();
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPushableInto(final ObjectInventory inv) {
        return this.mp.isPushableInto();
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPushableInto(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPushableInto(dirX, dirY);
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPullableInto(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPullableInto(dirX, dirY);
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPullableOut(final ObjectInventory inv) {
        return this.mp.isPullableOut();
    }

    /**
     *
     * @param inv
     * @return
     */
    public boolean isConditionallyPushableOut(final ObjectInventory inv) {
        return this.mp.isPushableOut();
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPushableOut(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPushableOut(dirX, dirY);
    }

    /**
     *
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean isConditionallyDirectionallyPullableOut(final int dirX,
            final int dirY, final ObjectInventory inv) {
        return this.mp.isDirectionallyPullableOut(dirX, dirY);
    }

    public boolean isPushable() {
        return this.mp.isPushable();
    }

    public boolean isDirectionallyPushable(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPushable(dirX, dirY);
    }

    public boolean isPullable() {
        return this.mp.isPullable();
    }

    public boolean isDirectionallyPullable(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPullable(dirX, dirY);
    }

    public boolean isPullableInto() {
        return this.mp.isPullableInto();
    }

    public boolean isPushableInto() {
        return this.mp.isPushableInto();
    }

    public boolean isDirectionallyPushableInto(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPushableInto(dirX, dirY);
    }

    public boolean isDirectionallyPullableInto(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPullableInto(dirX, dirY);
    }

    public boolean isPullableOut() {
        return this.mp.isPullableOut();
    }

    public boolean isPushableOut() {
        return this.mp.isPushableOut();
    }

    public boolean isDirectionallyPushableOut(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPushableOut(dirX, dirY);
    }

    public boolean isDirectionallyPullableOut(final int dirX, final int dirY) {
        return this.mp.isDirectionallyPullableOut(dirX, dirY);
    }

    protected void setPushable(final boolean value) {
        this.mp.setPushable(value);
    }

    protected void setDirectionallyPushable(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushable(dir, value);
    }

    protected void setPullable(final boolean value) {
        this.mp.setPullable(value);
    }

    protected void setDirectionallyPullable(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullable(dir, value);
    }

    protected void setPushableInto(final boolean value) {
        this.mp.setPushableInto(value);
    }

    protected void setDirectionallyPushableInto(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushableInto(dir, value);
    }

    protected void setPullableInto(final boolean value) {
        this.mp.setPullableInto(value);
    }

    protected void setDirectionallyPullableInto(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullableInto(dir, value);
    }

    protected void setPushableOut(final boolean value) {
        this.mp.setPushableOut(value);
    }

    protected void setDirectionallyPushableOut(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushableOut(dir, value);
    }

    protected void setPullableOut(final boolean value) {
        this.mp.setPullableOut(value);
    }

    protected void setDirectionallyPullableOut(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullableOut(dir, value);
    }

    public boolean hasFriction() {
        return this.friction;
    }

    public boolean isUsable() {
        return this.usable;
    }

    public int getUses() {
        return this.uses;
    }

    public boolean isDestroyable() {
        return this.destroyable;
    }

    public boolean doesChainReact() {
        return this.chainReacts;
    }

    public boolean isInventoryable() {
        return this.isInventoryable;
    }

    // Scriptability
    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean preMoveAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        return true;
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void postMoveAction(final boolean ie, final int dirX, final int dirY,
            final ObjectInventory inv) {
        FantastleReboot.getBagOStuff().getPrefsManager();
        // Play move success sound, if it's enabled
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveSuccessSound();
        }
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void moveFailedAction(final boolean ie, final int dirX,
            final int dirY, final ObjectInventory inv) {
        FantastleReboot.getBagOStuff().getPrefsManager();
        // Play move failed sound, if it's enabled
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            this.playMoveFailedSound();
        }
        Messager.showMessage("Can't go that way");
    }

    /**
     *
     * @param inv
     * @param moving
     * @return
     */
    public boolean hasFrictionConditionally(final ObjectInventory inv,
            final boolean moving) {
        return this.hasFriction();
    }

    public void gameProbeHook() {
        Messager.showMessage(this.getName());
    }

    public void editorPlaceHook() {
        // Do nothing
    }

    public void editorProbeHook() {
        Messager.showMessage(this.getName());
    }

    public MazeObject editorPropertiesHook() {
        return null;
    }

    public void playMoveFailedSound() {
        SoundLoader.playSound(GameSound.OOF);
    }

    public void playMoveSuccessSound() {
        SoundLoader.playSound(GameSound.WALK);
    }

    public final static void playPushSuccessSound() {
        SoundLoader.playSound(GameSound.PUSH);
    }

    public final static void playPushFailedSound() {
        SoundLoader.playSound(GameSound.PUSH_PULL_FAILED);
    }

    public final static void playPullFailedSound() {
        SoundLoader.playSound(GameSound.PUSH_PULL_FAILED);
    }

    public final static void playPullSuccessSound() {
        SoundLoader.playSound(GameSound.PULL);
    }

    public void playUseSound() {
        // Do nothing
    }

    public void playChainReactSound() {
        // Do nothing
    }

    public final static void playIdentifySound() {
        SoundLoader.playSound(GameSound.IDENTIFY);
    }

    public final static void playRotatedSound() {
        SoundLoader.playSound(GameSound.ROTATED);
    }

    public final static void playFallSound() {
        SoundLoader.playSound(GameSound.FALLING);
    }

    public final static void playButtonSound() {
        SoundLoader.playSound(GameSound.BUTTON);
    }

    public final static void playConfusedSound() {
        SoundLoader.playSound(GameSound.CONFUSED);
    }

    public final static void playDarknessSound() {
        SoundLoader.playSound(GameSound.DARKNESS);
    }

    public final static void playDizzySound() {
        SoundLoader.playSound(GameSound.DIZZY);
    }

    public final static void playDrunkSound() {
        SoundLoader.playSound(GameSound.DRUNK);
    }

    public final static void playFinishSound() {
        SoundLoader.playSound(GameSound.FINISH);
    }

    public final static void playLightSound() {
        SoundLoader.playSound(GameSound.LIGHT);
    }

    public final static void playSinkBlockSound() {
        SoundLoader.playSound(GameSound.SINK);
    }

    public final static void playWallTrapSound() {
        SoundLoader.playSound(GameSound.TRAP);
    }

    /**
     *
     * @param inv
     * @param mo
     * @param x
     * @param y
     * @param pushX
     * @param pushY
     */
    public void pushAction(final ObjectInventory inv, final MazeObject mo,
            final int x, final int y, final int pushX, final int pushY) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param pushed
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void pushIntoAction(final ObjectInventory inv,
            final MazeObject pushed, final int x, final int y, final int z,
            final int w) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param pushed
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void pushOutAction(final ObjectInventory inv,
            final MazeObject pushed, final int x, final int y, final int z,
            final int w) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param x
     * @param y
     * @param pushX
     * @param pushY
     */
    public void pushFailedAction(final ObjectInventory inv, final int x,
            final int y, final int pushX, final int pushY) {
        FantastleReboot.getBagOStuff().getPrefsManager();
        // Play push failed sound, if it's enabled
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            MazeObject.playPushFailedSound();
        }
        FantastleReboot.getBagOStuff().getGameManager().keepNextMessage();
        Messager.showMessage("Can't push that");
    }

    /**
     *
     * @param inv
     * @param mo
     * @param x
     * @param y
     * @param pullX
     * @param pullY
     */
    public void pullAction(final ObjectInventory inv, final MazeObject mo,
            final int x, final int y, final int pullX, final int pullY) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param pulled
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void pullIntoAction(final ObjectInventory inv,
            final MazeObject pulled, final int x, final int y, final int z,
            final int w) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param pulled
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void pullOutAction(final ObjectInventory inv,
            final MazeObject pulled, final int x, final int y, final int z,
            final int w) {
        // Do nothing
    }

    /**
     *
     * @param inv
     * @param x
     * @param y
     * @param pullX
     * @param pullY
     */
    public void pullFailedAction(final ObjectInventory inv, final int x,
            final int y, final int pullX, final int pullY) {
        FantastleReboot.getBagOStuff().getPrefsManager();
        // Play pull failed sound, if it's enabled
        if (FantastleReboot.getBagOStuff().getPrefsManager()
                .getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
            MazeObject.playPullFailedSound();
        }
        FantastleReboot.getBagOStuff().getGameManager().keepNextMessage();
        Messager.showMessage("Can't pull that");
    }

    /**
     *
     * @param mo
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void useAction(final MazeObject mo, final int x, final int y,
            final int z, final int w) {
        // Do nothing
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void useHelper(final int x, final int y, final int z, final int w) {
        // Do nothing
    }

    public final boolean isTimerActive() {
        return this.timerActive;
    }

    public final void activateTimer(final int ticks) {
        this.timerActive = true;
        this.timerValue = ticks;
        this.initialTimerValue = ticks;
    }

    public final void extendTimer(final int ticks) {
        if (this.timerActive) {
            this.timerValue += ticks;
        }
    }

    public final void extendTimerByInitialValue() {
        if (this.timerActive) {
            this.timerValue += this.initialTimerValue;
        }
    }

    public final void tickTimer(final int dirX, final int dirY) {
        if (this.timerActive) {
            this.timerValue--;
            if (this.timerValue == 0) {
                this.timerActive = false;
                this.initialTimerValue = 0;
                this.timerExpiredAction(dirX, dirY);
            }
        }
    }

    /**
     *
     * @param dirX
     * @param dirY
     */
    public void timerExpiredAction(final int dirX, final int dirY) {
        // Do nothing
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param locW
     * @param dirX
     * @param dirY
     * @param arrowType
     * @param inv
     * @return
     */
    public boolean arrowHitAction(final int locX, final int locY,
            final int locZ, final int locW, final int dirX, final int dirY,
            final int arrowType, final ObjectInventory inv) {
        // Default do-nothing, return true
        return true;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     * @return
     */
    public String gameRenderHook(final int x, final int y, final int z,
            final int w) {
        return this.getGameName();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     * @return
     */
    public String editorRenderHook(final int x, final int y, final int z,
            final int w) {
        return this.getName();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void chainReactionAction(final int x, final int y, final int z,
            final int w) {
        // Do nothing
    }

    public boolean defersSetProperties() {
        return false;
    }

    public boolean hasAdditionalProperties() {
        return false;
    }

    public boolean overridesDefaultPostMove() {
        return false;
    }

    public String getGameName() {
        return this.getName();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void determineCurrentAppearance(final int x, final int y,
            final int z, final int w) {
        // Do nothing
    }

    public void stepAction() {
        // Do nothing
    }

    abstract public String getName();

    public String getIdentifier4() {
        return this.getName();
    }

    @Override
    public final int getIdentifier5() {
        return this.getName().hashCode();
    }

    abstract public String getPluralName();

    abstract public String getDescription();

    abstract public byte getGroupID();

    abstract public byte getObjectID();

    abstract public int getLayer();

    abstract public int getCustomProperty(int propID);

    abstract public void setCustomProperty(int propID, int value);

    public int getCustomFormat() {
        return 0;
    }

    public final void writeMazeObject(final XDataWriter writer)
            throws IOException {
        writer.writeInt(this.getIdentifier5());
        final int cc = this.getCustomFormat();
        if (cc == MazeObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
            this.writeMazeObjectHook(writer);
        } else {
            for (int x = 0; x < cc; x++) {
                final int cx = this.getCustomProperty(x + 1);
                writer.writeInt(cx);
            }
        }
    }

    public final MazeObject readMazeObject(final XDataReader reader,
            final String ident) throws IOException {
        if (ident.equals(this.getIdentifier4())) {
            final int cc = this.getCustomFormat();
            if (cc == MazeObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
                return this.readMazeObjectHook(reader,
                        FormatConstants.MAZE_FORMAT_4);
            } else {
                for (int x = 0; x < cc; x++) {
                    final int cx = reader.readInt();
                    this.setCustomProperty(x + 1, cx);
                }
            }
            return this;
        } else {
            return null;
        }
    }

    public final MazeObject readMazeObject(final XDataReader reader,
            final int ident) throws IOException {
        if (ident == this.getIdentifier5()) {
            final int cc = this.getCustomFormat();
            if (cc == MazeObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
                return this.readMazeObjectHook(reader,
                        FormatConstants.MAZE_FORMAT_5);
            } else {
                for (int x = 0; x < cc; x++) {
                    final int cx = reader.readInt();
                    this.setCustomProperty(x + 1, cx);
                }
            }
            return this;
        } else {
            return null;
        }
    }

    /**
     *
     * @param writer
     * @throws IOException
     */
    protected void writeMazeObjectHook(final XDataWriter writer)
            throws IOException {
        // Do nothing - but let subclasses override
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected MazeObject readMazeObjectHook(final XDataReader reader,
            final int formatVersion) throws IOException {
        // Dummy implementation, subclasses can override
        return this;
    }

    public final MazeObject readMazeObject(final XDataReader reader,
            final byte groupID, final byte objectID) throws IOException {
        if (groupID == this.getGroupID() && objectID == this.getObjectID()) {
            final int cc = this.getCustomFormat();
            if (cc == MazeObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
                return this.readMazeObjectHook(reader,
                        FormatConstants.MAZE_FORMAT_3);
            } else {
                for (int x = 0; x < cc; x++) {
                    final int cx = reader.readInt();
                    this.setCustomProperty(x + 1, cx);
                }
            }
            return this;
        } else {
            return null;
        }
    }

    public static final int resolveRelativeDirection(final int idirX,
            final int idirY) {
        final int dirX = (int) Math.signum(idirX);
        final int dirY = (int) Math.signum(idirY);
        if (dirX == 0 && dirY == 0) {
            return DirectionConstants.DIRECTION_NONE;
        } else if (dirX == 0 && dirY == -1) {
            return DirectionConstants.DIRECTION_NORTH;
        } else if (dirX == 0 && dirY == 1) {
            return DirectionConstants.DIRECTION_SOUTH;
        } else if (dirX == -1 && dirY == 0) {
            return DirectionConstants.DIRECTION_WEST;
        } else if (dirX == 1 && dirY == 0) {
            return DirectionConstants.DIRECTION_EAST;
        } else if (dirX == 1 && dirY == 1) {
            return DirectionConstants.DIRECTION_SOUTHEAST;
        } else if (dirX == -1 && dirY == 1) {
            return DirectionConstants.DIRECTION_SOUTHWEST;
        } else if (dirX == -1 && dirY == -1) {
            return DirectionConstants.DIRECTION_NORTHWEST;
        } else if (dirX == 1 && dirY == -1) {
            return DirectionConstants.DIRECTION_NORTHEAST;
        } else {
            return DirectionConstants.DIRECTION_INVALID;
        }
    }

    public static final int[] unresolveRelativeDirection(final int dir) {
        int[] res = new int[2];
        if (dir == DirectionConstants.DIRECTION_NONE) {
            res[0] = 0;
            res[1] = 0;
        } else if (dir == DirectionConstants.DIRECTION_NORTH) {
            res[0] = 0;
            res[1] = -1;
        } else if (dir == DirectionConstants.DIRECTION_SOUTH) {
            res[0] = 0;
            res[1] = 1;
        } else if (dir == DirectionConstants.DIRECTION_WEST) {
            res[0] = -1;
            res[1] = 0;
        } else if (dir == DirectionConstants.DIRECTION_EAST) {
            res[0] = 1;
            res[1] = 0;
        } else if (dir == DirectionConstants.DIRECTION_SOUTHEAST) {
            res[0] = 1;
            res[1] = 1;
        } else if (dir == DirectionConstants.DIRECTION_SOUTHWEST) {
            res[0] = -1;
            res[1] = 1;
        } else if (dir == DirectionConstants.DIRECTION_NORTHWEST) {
            res[0] = -1;
            res[1] = -1;
        } else if (dir == DirectionConstants.DIRECTION_NORTHEAST) {
            res[0] = 1;
            res[1] = -1;
        } else {
            res = null;
        }
        return res;
    }

    public static final String resolveDirectionConstantToName(final int dir) {
        String res = null;
        if (dir == DirectionConstants.DIRECTION_NORTH) {
            res = DirectionConstants.DIRECTION_NORTH_NAME;
        } else if (dir == DirectionConstants.DIRECTION_SOUTH) {
            res = DirectionConstants.DIRECTION_SOUTH_NAME;
        } else if (dir == DirectionConstants.DIRECTION_WEST) {
            res = DirectionConstants.DIRECTION_WEST_NAME;
        } else if (dir == DirectionConstants.DIRECTION_EAST) {
            res = DirectionConstants.DIRECTION_EAST_NAME;
        } else if (dir == DirectionConstants.DIRECTION_SOUTHEAST) {
            res = DirectionConstants.DIRECTION_SOUTHEAST_NAME;
        } else if (dir == DirectionConstants.DIRECTION_SOUTHWEST) {
            res = DirectionConstants.DIRECTION_SOUTHWEST_NAME;
        } else if (dir == DirectionConstants.DIRECTION_NORTHWEST) {
            res = DirectionConstants.DIRECTION_NORTHWEST_NAME;
        } else if (dir == DirectionConstants.DIRECTION_NORTHEAST) {
            res = DirectionConstants.DIRECTION_NORTHEAST_NAME;
        }
        return res;
    }
}
