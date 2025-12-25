/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.direction.DirectionQuery;

public abstract class GameObject implements ObjectModel {
    // Properties
    private final int uniqueID;
    private final Tile tile;
    private final SolidProperties sp;
    private final VisionProperties vp;
    private final MoveProperties mp;
    private final OtherProperties op;
    private final OtherCounters oc;
    private final CustomCounters cc;
    private final CustomFlags cf;
    private final CustomTexts ct;

    // Constructors
    public GameObject(final int objectID) {
	this.uniqueID = objectID;
	this.tile = null;
	this.sp = new SolidProperties();
	this.vp = new VisionProperties();
	this.mp = new MoveProperties();
	this.op = new OtherProperties();
	this.oc = new OtherCounters();
	this.cc = new CustomCounters();
	this.cf = new CustomFlags();
	this.ct = new CustomTexts();
    }

    public GameObject(final int objectID, final Appearance appearance) {
	this.uniqueID = objectID;
	this.tile = new Tile(appearance);
	this.sp = new SolidProperties();
	this.vp = new VisionProperties();
	this.mp = new MoveProperties();
	this.op = new OtherProperties();
	this.oc = new OtherCounters();
	this.cc = new CustomCounters();
	this.cf = new CustomFlags();
	this.ct = new CustomTexts();
    }

    protected final boolean addCustomCounter(final int count) {
	return this.cc.add(count);
    }

    protected final boolean addCustomFlag(final int count) {
	return this.cf.add(count);
    }

    protected final boolean addCustomText(final int count) {
	return this.ct.add(count);
    }

    protected final void addOneCustomCounter() {
	this.cc.addOne();
    }

    protected final void addOneCustomFlag() {
	this.cf.addOne();
    }

    protected final void addOneCustomText() {
	this.ct.addOne();
    }

    protected final void appendCustomCounter(final int count) {
	this.cc.append(count);
    }

    protected final void appendCustomFlag(final int count) {
	this.cf.append(count);
    }

    protected final void appendCustomText(final int count) {
	this.ct.append(count);
    }

    protected final void appendOneCustomCounter() {
	this.cc.appendOne();
    }

    protected final void appendOneCustomFlag() {
	this.cf.appendOne();
    }

    protected final void appendOneCustomText() {
	this.ct.appendOne();
    }

    protected final int customCountersLength() {
	return this.cc.length();
    }

    protected final int customFlagsLength() {
	return this.cf.length();
    }

    protected final int customTextsLength() {
	return this.ct.length();
    }

    protected final boolean decrementCustomCounter(final int index) {
	return this.cc.decrement(index);
    }

    @Override
    public final boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof final GameObject other)) {
	    return false;
	}
	return Objects.equals(this.mp, other.mp) && Objects.equals(this.oc, other.oc)
		&& Objects.equals(this.op, other.op) && Objects.equals(this.sp, other.sp)
		&& Objects.equals(this.cc, other.cc) && Objects.equals(this.cf, other.cf)
		&& Objects.equals(this.ct, other.ct) && this.uniqueID == other.uniqueID;
    }

    @Override
    public final BufferedImageIcon getBattleImage() {
	return this.getBattleImageHook();
    }

    protected BufferedImageIcon getBattleImageHook() {
	if (this.tile != null) {
	    return this.tile.getBattleImage();
	}
	return null;
    }

    protected final int getCustomCounter(final int index) {
	return this.cc.get(index);
    }

    protected final boolean getCustomFlag(final int index) {
	return this.cf.get(index);
    }

    protected final String getCustomText(final int index) {
	return this.ct.get(index);
    }

    @Override
    public final BufferedImageIcon getEditorImage() {
	return this.getEditorImageHook();
    }

    protected BufferedImageIcon getEditorImageHook() {
	if (this.tile != null) {
	    return this.tile.getEditorImage();
	}
	return null;
    }

    @Override
    public final BufferedImageIcon getGameImage() {
	return this.getGameImageHook();
    }

    protected BufferedImageIcon getGameImageHook() {
	if (this.tile != null) {
	    return this.tile.getGameImage();
	}
	return null;
    }

    @Override
    public final BufferedImageIcon getImage() {
	return this.getImageHook();
    }

    protected BufferedImageIcon getImageHook() {
	if (this.tile != null) {
	    return this.tile.getImage();
	}
	return null;
    }

    protected final int getTimerReset() {
	return this.oc.getTimerReset();
    }

    @Override
    public final int getTimerTicks() {
	return this.oc.getTimerTicks();
    }

    @Override
    public final int getUniqueID() {
	return this.uniqueID;
    }

    @Override
    public final int getUses() {
	return this.oc.getUses();
    }

    @Override
    public boolean hasFriction() {
	return this.op.hasFriction();
    }

    @Override
    public final int hashCode() {
	return Objects.hash(this.mp, this.oc, this.op, this.sp, this.cc, this.cf, this.ct, this.uniqueID);
    }

    protected final boolean incrementCustomCounter(final int index) {
	return this.cc.increment(index);
    }

    @Override
    public boolean isCarryable() {
	return this.op.isCarryable();
    }

    @Override
    public boolean isChainReacting() {
	return this.op.isChainReacting();
    }

    @Override
    public boolean isChainReactingHorizontally() {
	return this.op.isChainReactingHorizontally();
    }

    @Override
    public boolean isChainReactingVertically() {
	return this.op.isChainReactingVertically();
    }

    @Override
    public boolean isDestroyable() {
	return this.op.isDestroyable();
    }

    @Override
    public boolean isDirectionallyPullable(final DirectionQuery dir) {
	return this.mp.isDirectionallyPullable(dir);
    }

    @Override
    public boolean isDirectionallyPullableInto(final DirectionQuery dir) {
	return this.mp.isDirectionallyPullableInto(dir);
    }

    @Override
    public boolean isDirectionallyPullableOut(final DirectionQuery dir) {
	return this.mp.isDirectionallyPullableOut(dir);
    }

    @Override
    public boolean isDirectionallyPushable(final DirectionQuery dir) {
	return this.mp.isDirectionallyPushable(dir);
    }

    @Override
    public boolean isDirectionallyPushableInto(final DirectionQuery dir) {
	return this.mp.isDirectionallyPushableInto(dir);
    }

    @Override
    public boolean isDirectionallyPushableOut(final DirectionQuery dir) {
	return this.mp.isDirectionallyPushableOut(dir);
    }

    @Override
    public boolean isDirectionallySightBlocking(final DirectionQuery dir) {
	return this.vp.isDirectionallySightBlocking(dir);
    }

    @Override
    public boolean isDirectionallySolid(final DirectionQuery dir) {
	return this.sp.isDirectionallySolid(dir);
    }

    @Override
    public boolean isInternallyDirectionallySightBlocking(final DirectionQuery dir) {
	return this.vp.isInternallyDirectionallySightBlocking(dir);
    }

    @Override
    public boolean isInternallyDirectionallySolid(final DirectionQuery dir) {
	return this.sp.isInternallyDirectionallySolid(dir);
    }

    @Override
    public boolean isPullable() {
	return this.mp.isPullable();
    }

    @Override
    public boolean isPullableInto() {
	return this.mp.isPullableInto();
    }

    @Override
    public boolean isPullableOut() {
	return this.mp.isPullableOut();
    }

    @Override
    public boolean isPushable() {
	return this.mp.isPushable();
    }

    @Override
    public boolean isPushableInto() {
	return this.mp.isPushableInto();
    }

    @Override
    public boolean isPushableOut() {
	return this.mp.isPushableOut();
    }

    @Override
    public boolean isSightBlocking() {
	return this.vp.isSightBlocking();
    }

    @Override
    public boolean isSolid() {
	return this.sp.isSolid();
    }

    @Override
    public boolean isUsable() {
	return this.op.isUsable();
    }

    protected final boolean offsetCustomCounter(final int index, final int value) {
	return this.cc.offset(index, value);
    }

    @Override
    public final void resetTimer() {
	this.oc.resetTimer();
    }

    protected final void setBattleLook(final Appearance appearance) {
	if (this.tile != null) {
	    this.tile.setBattleLook(appearance);
	}
    }

    protected final void setCarryable(final boolean value) {
	this.op.setCarryable(value);
    }

    protected final void setChainReactingHorizontally(final boolean value) {
	this.op.setChainReactingHorizontally(value);
    }

    protected final void setChainReactingVertically(final boolean value) {
	this.op.setChainReactingVertically(value);
    }

    protected final boolean setCustomCounter(final int index, final int value) {
	return this.cc.set(index, value);
    }

    protected final boolean setCustomFlag(final int index, final boolean value) {
	return this.cf.set(index, value);
    }

    protected final boolean setCustomText(final int index, final String value) {
	return this.ct.set(index, value);
    }

    protected final void setDestroyable(final boolean value) {
	this.op.setDestroyable(value);
    }

    protected final void setDirectionallyPullable(final DirectionQuery dir, final boolean value) {
	this.mp.setDirectionallyPullable(dir, value);
    }

    protected final void setDirectionallyPullableInto(final DirectionQuery dir, final boolean value) {
	this.mp.setDirectionallyPullableInto(dir, value);
    }

    protected final void setDirectionallyPullableOut(final DirectionQuery dir, final boolean value) {
	this.mp.setDirectionallyPullableOut(dir, value);
    }

    protected final void setDirectionallyPushable(final DirectionQuery dir, final boolean value) {
	this.mp.setDirectionallyPushable(dir, value);
    }

    protected final void setDirectionallyPushableInto(final DirectionQuery dir, final boolean value) {
	this.mp.setDirectionallyPushableInto(dir, value);
    }

    protected final void setDirectionallyPushableOut(final DirectionQuery dir, final boolean value) {
	this.mp.setDirectionallyPushableOut(dir, value);
    }

    protected final void setDirectionallySightBlocking(final DirectionQuery dir, final boolean value) {
	this.vp.setDirectionallySightBlocking(dir, value);
    }

    protected final void setDirectionallySolid(final DirectionQuery dir, final boolean value) {
	this.sp.setDirectionallySolid(dir, value);
    }

    protected final void setEditorLook(final Appearance appearance) {
	if (this.tile != null) {
	    this.tile.setEditorLook(appearance);
	}
    }

    protected final void setFriction(final boolean value) {
	this.op.setFriction(value);
    }

    protected final void setGameLook(final Appearance appearance) {
	if (this.tile != null) {
	    this.tile.setGameLook(appearance);
	}
    }

    protected final void setInternallyDirectionallySightBlocking(final DirectionQuery dir, final boolean value) {
	this.vp.setInternallyDirectionallySightBlocking(dir, value);
    }

    protected final void setInternallyDirectionallySolid(final DirectionQuery dir, final boolean value) {
	this.sp.setInternallyDirectionallySolid(dir, value);
    }

    protected final void setPullable(final boolean value) {
	this.mp.setPullable(value);
    }

    protected final void setPullableInto(final boolean value) {
	this.mp.setPullableInto(value);
    }

    protected final void setPullableOut(final boolean value) {
	this.mp.setPullableOut(value);
    }

    protected final void setPushable(final boolean value) {
	this.mp.setPushable(value);
    }

    protected final void setPushableInto(final boolean value) {
	this.mp.setPushableInto(value);
    }

    protected final void setPushableOut(final boolean value) {
	this.mp.setPushableOut(value);
    }

    protected final void setSightBlocking(final boolean value) {
	this.vp.setSightBlocking(value);
    }

    protected final void setSolid(final boolean value) {
	this.sp.setSolid(value);
    }

    protected final void setTimerReset(final int value) {
	this.oc.setTimerReset(value);
    }

    protected final void setTimerTicks(final int value) {
	this.oc.setTimerTicks(value);
    }

    protected final void setUsable(final boolean value) {
	this.op.setUsable(value);
    }

    protected final void setUses(final int value) {
	this.oc.setUses(value);
    }

    @Override
    public final void tickTimer() {
	this.oc.tickTimer();
    }

    protected final boolean toggleCustomFlag(final int index) {
	return this.cf.toggle(index);
    }

    @Override
    public final void use() {
	if (this.isUsable()) {
	    this.oc.use();
	}
    }
}
