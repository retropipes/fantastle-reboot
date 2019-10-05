/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.fantastlereboot.assets.GameAttributeImage;
import com.puttysoftware.fantastlereboot.assets.GameObjectImage;
import com.puttysoftware.images.BufferedImageIcon;

public class ObjectModel {
    // Properties
    private final Tile tile;
    private final SolidProperties sp;
    private final MoveProperties mp;
    private final OtherProperties op;
    private final OtherCounters oc;

    // Constructors
    public ObjectModel(final String cacheName, final GameObjectImage image) {
        this.tile = new Tile(new ObjectAppearance(cacheName, image));
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.op = new OtherProperties();
        this.oc = new OtherCounters();
    }

    public ObjectModel(final String cacheName, final GameObjectImage image,
            final ColorShader shader) {
        this.tile = new Tile(new ObjectAppearance(cacheName, image, shader));
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.op = new OtherProperties();
        this.oc = new OtherCounters();
    }

    public ObjectModel(final String cacheObjectName,
            final GameObjectImage objectImage, final String cacheAttributeName,
            final GameAttributeImage attributeImage,
            final ColorShader attributeShader) {
        this.tile = new Tile(
                new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.op = new OtherProperties();
        this.oc = new OtherCounters();
    }

    public ObjectModel(final String cacheObjectName,
            final GameObjectImage objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final GameAttributeImage attributeImage,
            final ColorShader attributeShader) {
        this.tile = new Tile(new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
        this.sp = new SolidProperties();
        this.mp = new MoveProperties();
        this.op = new OtherProperties();
        this.oc = new OtherCounters();
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
        final ObjectModel other = (ObjectModel) obj;
        if (this.sp != other.sp
                && (this.sp == null || !this.sp.equals(other.sp))) {
            return false;
        }
        if (this.mp != other.mp
                && (this.mp == null || !this.mp.equals(other.mp))) {
            return false;
        }
        if (this.op != other.op
                && (this.op == null || !this.op.equals(other.op))) {
            return false;
        }
        if (this.oc != other.oc
                && (this.oc == null || !this.oc.equals(other.oc))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.sp != null ? this.sp.hashCode() : 0);
        hash = 59 * hash + (this.mp != null ? this.mp.hashCode() : 0);
        hash = 59 * hash + (this.op != null ? this.op.hashCode() : 0);
        hash = 59 * hash + (this.oc != null ? this.oc.hashCode() : 0);
        return hash;
    }

    protected final void setGameLook(final String cacheName,
            final GameObjectImage image) {
        this.tile.setGameLook(new ObjectAppearance(cacheName, image));
    }

    protected final void setGameLook(final String cacheName,
            final GameObjectImage image, final ColorShader shader) {
        this.tile.setGameLook(new ObjectAppearance(cacheName, image, shader));
    }

    protected final void setGameLook(final String cacheObjectName,
            final GameObjectImage objectImage, final String cacheAttributeName,
            final GameAttributeImage attributeImage,
            final ColorShader attributeShader) {
        this.tile
                .setGameLook(new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
    }

    protected final void setGameLook(final String cacheObjectName,
            final GameObjectImage objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final GameAttributeImage attributeImage,
            final ColorShader attributeShader) {
        this.tile.setGameLook(new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
    }

    protected final void setEditorLook(final String cacheName,
            final GameObjectImage image) {
        this.tile.setEditorLook(new ObjectAppearance(cacheName, image));
    }

    protected final void setEditorLook(final String cacheName,
            final GameObjectImage image, final ColorShader shader) {
        this.tile.setEditorLook(new ObjectAppearance(cacheName, image, shader));
    }

    protected final void setEditorLook(final String cacheObjectName,
            final GameObjectImage objectImage, final String cacheAttributeName,
            final GameAttributeImage attributeImage,
            final ColorShader attributeShader) {
        this.tile
                .setEditorLook(new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
    }

    protected final void setEditorLook(final String cacheObjectName,
            final GameObjectImage objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final GameAttributeImage attributeImage,
            final ColorShader attributeShader) {
        this.tile.setEditorLook(new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
    }

    protected final void setBattleLook(final String cacheName,
            final GameObjectImage image) {
        this.tile.setBattleLook(new ObjectAppearance(cacheName, image));
    }

    protected final void setBattleLook(final String cacheName,
            final GameObjectImage image, final ColorShader shader) {
        this.tile.setBattleLook(new ObjectAppearance(cacheName, image, shader));
    }

    protected final void setBattleLook(final String cacheObjectName,
            final GameObjectImage objectImage, final String cacheAttributeName,
            final GameAttributeImage attributeImage,
            final ColorShader attributeShader) {
        this.tile
                .setBattleLook(new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
    }

    protected final void setBattleLook(final String cacheObjectName,
            final GameObjectImage objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final GameAttributeImage attributeImage,
            final ColorShader attributeShader) {
        this.tile.setBattleLook(new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
    }

    public final BufferedImageIcon getImage() {
        return this.tile.getImage();
    }

    public final BufferedImageIcon getGameImage() {
        return this.tile.getGameImage();
    }

    public final BufferedImageIcon getEditorImage() {
        return this.tile.getEditorImage();
    }

    public final BufferedImageIcon getBattleImage() {
        return this.tile.getBattleImage();
    }

    public final boolean isSolid() {
        return this.sp.isSolid();
    }

    public final boolean isDirectionallySolid(final int dirX, final int dirY) {
        return this.sp.isDirectionallySolid(dirX, dirY);
    }

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

    public final boolean isPushable() {
        return this.mp.isPushable();
    }

    public final boolean isDirectionallyPushable(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPushable(dirX, dirY);
    }

    public final boolean isPullable() {
        return this.mp.isPullable();
    }

    public final boolean isDirectionallyPullable(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPullable(dirX, dirY);
    }

    public final boolean isPullableInto() {
        return this.mp.isPullableInto();
    }

    public final boolean isPushableInto() {
        return this.mp.isPushableInto();
    }

    public final boolean isDirectionallyPushableInto(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPushableInto(dirX, dirY);
    }

    public final boolean isDirectionallyPullableInto(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPullableInto(dirX, dirY);
    }

    public final boolean isPullableOut() {
        return this.mp.isPullableOut();
    }

    public final boolean isPushableOut() {
        return this.mp.isPushableOut();
    }

    public final boolean isDirectionallyPushableOut(final int dirX,
            final int dirY) {
        return this.mp.isDirectionallyPushableOut(dirX, dirY);
    }

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

    public final boolean hasFriction() {
        return this.op.hasFriction();
    }

    protected final void setFriction(final boolean value) {
        this.op.setFriction(value);
    }

    public final boolean isUsable() {
        return this.op.isUsable();
    }

    protected final void setUsable(final boolean value) {
        this.op.setUsable(value);
    }

    public final int getUses() {
        return this.oc.getUses();
    }

    public final void setUses(final int value) {
        this.oc.setUses(value);
    }

    public final void use() {
        if (this.isUsable()) {
            this.oc.use();
        }
    }

    public final boolean isDestroyable() {
        return this.op.isDestroyable();
    }

    protected final void setDestroyable(final boolean value) {
        this.op.setDestroyable(value);
    }

    public final boolean isChainReacting() {
        return this.op.isChainReacting();
    }

    public final boolean isChainReactingHorizontally() {
        return this.op.isChainReactingHorizontally();
    }

    public final boolean isChainReactingVertically() {
        return this.op.isChainReactingVertically();
    }

    protected final void setChainReactingHorizontally(final boolean value) {
        this.op.setChainReactingHorizontally(value);
    }

    protected final void setChainReactingVertically(final boolean value) {
        this.op.setChainReactingVertically(value);
    }

    public final boolean isCarryable() {
        return this.op.isCarryable();
    }

    protected final void setCarryable(final boolean value) {
        this.op.setCarryable(value);
    }
}
