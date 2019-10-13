/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.Objects;
import java.util.Optional;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.images.BufferedImageIcon;

public abstract class GameObject implements ObjectModel {
    // Properties
    private final int uniqueID;
    private final Tile tile;
    private final SolidProperties sp;
    private final MoveProperties mp;
    private final OtherProperties op;
    private final OtherCounters oc;
    private final CustomCounters cc;
    private final CustomFlags cf;
    private final CustomTexts ct;

    // Constructors
    public GameObject(final int objectID, final String cacheName,
            final ObjectImageIndex image) {
        this.uniqueID = objectID;
        this.tile = new Tile(new ObjectAppearance(cacheName, image));
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.op = new OtherProperties();
        this.oc = new OtherCounters();
        this.cc = new CustomCounters();
        this.cf = new CustomFlags();
        this.ct = new CustomTexts();
    }

    public GameObject(final int objectID, final String cacheName,
            final ObjectImageIndex image, final ColorShader shader) {
        this.uniqueID = objectID;
        this.tile = new Tile(new ObjectAppearance(cacheName, image, shader));
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.op = new OtherProperties();
        this.oc = new OtherCounters();
        this.cc = new CustomCounters();
        this.cf = new CustomFlags();
        this.ct = new CustomTexts();
    }

    public GameObject(final int objectID, final String cacheObjectName,
            final ObjectImageIndex objectImage, final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        this.uniqueID = objectID;
        this.tile = new Tile(
                new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.op = new OtherProperties();
        this.oc = new OtherCounters();
        this.cc = new CustomCounters();
        this.cf = new CustomFlags();
        this.ct = new CustomTexts();
    }

    public GameObject(final int objectID, final String cacheObjectName,
            final ObjectImageIndex objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        this.uniqueID = objectID;
        this.tile = new Tile(new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.op = new OtherProperties();
        this.oc = new OtherCounters();
        this.cc = new CustomCounters();
        this.cf = new CustomFlags();
        this.ct = new CustomTexts();
    }

    // Methods
    @Override
    public final int hashCode() {
        return Objects.hash(this.mp, this.oc, this.op, this.sp, this.cc,
                this.cf, this.ct, this.uniqueID);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GameObject)) {
            return false;
        }
        GameObject other = (GameObject) obj;
        return Objects.equals(this.mp, other.mp)
                && Objects.equals(this.oc, other.oc)
                && Objects.equals(this.op, other.op)
                && Objects.equals(this.sp, other.sp)
                && Objects.equals(this.cc, other.cc)
                && Objects.equals(this.cf, other.cf)
                && Objects.equals(this.ct, other.ct)
                && this.uniqueID == other.uniqueID;
    }

    @Override
    public final int getUniqueID() {
        return this.uniqueID;
    }

    protected final void setGameLook(final String cacheName,
            final ObjectImageIndex image) {
        this.tile.setGameLook(new ObjectAppearance(cacheName, image));
    }

    protected final void setGameLook(final String cacheName,
            final ObjectImageIndex image, final ColorShader shader) {
        this.tile.setGameLook(new ObjectAppearance(cacheName, image, shader));
    }

    protected final void setGameLook(final String cacheObjectName,
            final ObjectImageIndex objectImage, final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        this.tile
                .setGameLook(new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
    }

    protected final void setGameLook(final String cacheObjectName,
            final ObjectImageIndex objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        this.tile.setGameLook(new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
    }

    protected final void setEditorLook(final String cacheName,
            final ObjectImageIndex image) {
        this.tile.setEditorLook(new ObjectAppearance(cacheName, image));
    }

    protected final void setEditorLook(final String cacheName,
            final ObjectImageIndex image, final ColorShader shader) {
        this.tile.setEditorLook(new ObjectAppearance(cacheName, image, shader));
    }

    protected final void setEditorLook(final String cacheObjectName,
            final ObjectImageIndex objectImage, final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        this.tile
                .setEditorLook(new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
    }

    protected final void setEditorLook(final String cacheObjectName,
            final ObjectImageIndex objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        this.tile.setEditorLook(new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
    }

    protected final void setBattleLook(final String cacheName,
            final ObjectImageIndex image) {
        this.tile.setBattleLook(new ObjectAppearance(cacheName, image));
    }

    protected final void setBattleLook(final String cacheName,
            final ObjectImageIndex image, final ColorShader shader) {
        this.tile.setBattleLook(new ObjectAppearance(cacheName, image, shader));
    }

    protected final void setBattleLook(final String cacheObjectName,
            final ObjectImageIndex objectImage, final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        this.tile
                .setBattleLook(new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
    }

    protected final void setBattleLook(final String cacheObjectName,
            final ObjectImageIndex objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        this.tile.setBattleLook(new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
    }

    @Override
    public final BufferedImageIcon getImage() {
        return this.tile.getImage();
    }

    @Override
    public final BufferedImageIcon getGameImage() {
        return this.tile.getGameImage();
    }

    @Override
    public final BufferedImageIcon getEditorImage() {
        return this.tile.getEditorImage();
    }

    @Override
    public final BufferedImageIcon getBattleImage() {
        return this.tile.getBattleImage();
    }

    protected final int customCountersLength() {
        return this.cc.length();
    }

    protected final boolean addCustomCounter(final int count) {
        return this.cc.add(count);
    }

    protected final void appendCustomCounter(final int count) {
        this.cc.append(count);
    }

    protected final void appendOneCustomCounter() {
        this.cc.appendOne();
    }

    protected final Optional<Integer> getCustomCounter(final int index) {
        return this.cc.get(index);
    }

    protected final boolean decrementCustomCounter(final int index) {
        return this.cc.decrement(index);
    }

    protected final boolean incrementCustomCounter(final int index) {
        return this.cc.increment(index);
    }

    protected final boolean offsetCustomCounter(final int index,
            final int value) {
        return this.cc.offset(index, value);
    }

    protected final boolean setCustomCounter(final int index, final int value) {
        return this.cc.set(index, value);
    }

    protected final int customFlagsLength() {
        return this.cf.length();
    }

    protected final boolean addCustomFlag(final int count) {
        return this.cf.add(count);
    }

    protected final void appendCustomFlag(final int count) {
        this.cf.append(count);
    }

    protected final void appendOneCustomFlag() {
        this.cf.appendOne();
    }

    protected final Optional<Boolean> getCustomFlag(final int index) {
        return this.cf.get(index);
    }

    protected final boolean toggleCustomFlag(final int index) {
        return this.cf.toggle(index);
    }

    protected final boolean setCustomFlag(final int index,
            final boolean value) {
        return this.cf.set(index, value);
    }

    protected final int customTextsLength() {
        return this.ct.length();
    }

    protected final boolean addCustomText(final int count) {
        return this.ct.add(count);
    }

    protected final void appendCustomText(final int count) {
        this.ct.append(count);
    }

    protected final void appendOneCustomText() {
        this.ct.appendOne();
    }

    protected final Optional<String> getCustomText(final int index) {
        return this.ct.get(index);
    }

    protected final boolean setCustomText(final int index, final String value) {
        return this.ct.set(index, value);
    }

    @Override
    public final boolean isSolid() {
        return this.sp.isSolid();
    }

    @Override
    public final boolean isDirectionallySolid(final int dirX, final int dirY) {
        return this.sp.isDirectionallySolid(dirX, dirY);
    }

    @Override
    public final boolean isInternallyDirectionallySolid(final int dirX,
            final int dirY) {
        return this.sp.isInternallyDirectionallySolid(dirX, dirY);
    }

    protected final void setSolid(final boolean value) {
        this.sp.setSolid(value);
    }

    protected final void setDirectionallySolid(final int dir,
            final boolean value) {
        this.sp.setDirectionallySolid(dir, value);
    }

    protected final void setInternallyDirectionallySolid(final int dir,
            final boolean value) {
        this.sp.setInternallyDirectionallySolid(dir, value);
    }

    @Override
    public final boolean isPushable() {
        return this.mp.isPushable();
    }

    @Override
    public final boolean isDirectionallyPushable(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPushable(dirX, dirY);
    }

    @Override
    public final boolean isPullable() {
        return this.mp.isPullable();
    }

    @Override
    public final boolean isDirectionallyPullable(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPullable(dirX, dirY);
    }

    @Override
    public final boolean isPullableInto() {
        return this.mp.isPullableInto();
    }

    @Override
    public final boolean isPushableInto() {
        return this.mp.isPushableInto();
    }

    @Override
    public final boolean isDirectionallyPushableInto(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPushableInto(dirX, dirY);
    }

    @Override
    public final boolean isDirectionallyPullableInto(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPullableInto(dirX, dirY);
    }

    @Override
    public final boolean isPullableOut() {
        return this.mp.isPullableOut();
    }

    @Override
    public final boolean isPushableOut() {
        return this.mp.isPushableOut();
    }

    @Override
    public final boolean isDirectionallyPushableOut(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPushableOut(dirX, dirY);
    }

    @Override
    public final boolean isDirectionallyPullableOut(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPullableOut(dirX, dirY);
    }

    protected final void setPushable(final boolean value) {
        this.mp.setPushable(value);
    }

    protected final void setDirectionallyPushable(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushable(dir, value);
    }

    protected final void setPullable(final boolean value) {
        this.mp.setPullable(value);
    }

    protected final void setDirectionallyPullable(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullable(dir, value);
    }

    protected final void setPushableInto(final boolean value) {
        this.mp.setPushableInto(value);
    }

    protected final void setDirectionallyPushableInto(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushableInto(dir, value);
    }

    protected final void setPullableInto(final boolean value) {
        this.mp.setPullableInto(value);
    }

    protected final void setDirectionallyPullableInto(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullableInto(dir, value);
    }

    protected final void setPushableOut(final boolean value) {
        this.mp.setPushableOut(value);
    }

    protected final void setDirectionallyPushableOut(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPushableOut(dir, value);
    }

    protected final void setPullableOut(final boolean value) {
        this.mp.setPullableOut(value);
    }

    protected final void setDirectionallyPullableOut(final int dir,
            final boolean value) {
        this.mp.setDirectionallyPullableOut(dir, value);
    }

    @Override
    public final boolean hasFriction() {
        return this.op.hasFriction();
    }

    protected final void setFriction(final boolean value) {
        this.op.setFriction(value);
    }

    @Override
    public final boolean isUsable() {
        return this.op.isUsable();
    }

    protected final void setUsable(final boolean value) {
        this.op.setUsable(value);
    }

    @Override
    public final int getUses() {
        return this.oc.getUses();
    }

    protected final void setUses(final int value) {
        this.oc.setUses(value);
    }

    @Override
    public final void use() {
        if (this.isUsable()) {
            this.oc.use();
        }
    }

    @Override
    public final boolean isDestroyable() {
        return this.op.isDestroyable();
    }

    protected final void setDestroyable(final boolean value) {
        this.op.setDestroyable(value);
    }

    @Override
    public final boolean isChainReacting() {
        return this.op.isChainReacting();
    }

    @Override
    public final boolean isChainReactingHorizontally() {
        return this.op.isChainReactingHorizontally();
    }

    @Override
    public final boolean isChainReactingVertically() {
        return this.op.isChainReactingVertically();
    }

    protected final void setChainReactingHorizontally(final boolean value) {
        this.op.setChainReactingHorizontally(value);
    }

    protected final void setChainReactingVertically(final boolean value) {
        this.op.setChainReactingVertically(value);
    }

    @Override
    public final boolean isCarryable() {
        return this.op.isCarryable();
    }

    protected final void setCarryable(final boolean value) {
        this.op.setCarryable(value);
    }
}
