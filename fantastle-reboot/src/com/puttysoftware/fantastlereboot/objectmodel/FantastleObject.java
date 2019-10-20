/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.io.IOException;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.diane.objectmodel.GameObject;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.utilities.FormatConstants;
import com.puttysoftware.fantastlereboot.utilities.RandomGenerationRule;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public abstract class FantastleObject extends GameObject
        implements FantastleObjectModel {
    // Properties
    private FantastleObjectModel savedObject = null;

    // Constructors
    public FantastleObject(final int objectID) {
        super(objectID);
    }

    public FantastleObject(final int objectID, final String cacheName,
            final ObjectImageIndex image) {
        super(objectID, new ObjectAppearance(cacheName, image));
    }

    public FantastleObject(final int objectID, final String cacheName,
            final ObjectImageIndex image, final ColorShader shader) {
        super(objectID, new ObjectAppearance(cacheName, image, shader));
    }

    public FantastleObject(final int objectID, final String cacheObjectName,
            final ObjectImageIndex objectImage, final String cacheAttributeName,
            final AttributeImageIndex attributeImage) {
        super(objectID, new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage),
                cacheObjectName, objectImage));
    }

    public FantastleObject(final int objectID, final String cacheObjectName,
            final ObjectImageIndex objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final AttributeImageIndex attributeImage) {
        super(objectID,
                new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage),
                        cacheObjectName, objectImage, objectShader));
    }

    public FantastleObject(final int objectID, final String cacheObjectName,
            final ObjectImageIndex objectImage, final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        super(objectID,
                new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage));
    }

    public FantastleObject(final int objectID, final String cacheObjectName,
            final ObjectImageIndex objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        super(objectID,
                new AttributedObjectAppearance(
                        new AttributeAppearance(cacheAttributeName,
                                attributeImage, attributeShader),
                        cacheObjectName, objectImage, objectShader));
    }

    // Methods
    @Override
    public final FantastleObjectModel getSavedObject() {
        return this.savedObject;
    }

    @Override
    public final boolean hasSavedObject() {
        return this.savedObject != null;
    }

    @Override
    public final void setSavedObject(
            final FantastleObjectModel newSavedObject) {
        this.savedObject = newSavedObject;
    }

    @Override
    public int getLayer() {
        return Layers.OBJECT;
    }

    public String getName() {
        return "";
    }

    @Override
    public boolean shouldGenerateObject(final Maze maze, final int row,
            final int col, final int floor, final int level, final int layer) {
        if (layer == Layers.OBJECT) {
            // Handle object layer
            // Limit generation of objects to 20%, unless required
            if (this.isRequired()) {
                return true;
            } else {
                final RandomRange r = new RandomRange(1, 100);
                if (r.generate() <= 20) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            // Handle ground layer
            return true;
        }
    }

    @Override
    public int getMinimumRequiredQuantity(final Maze maze) {
        return RandomGenerationRule.NO_LIMIT;
    }

    @Override
    public int getMaximumRequiredQuantity(final Maze maze) {
        return RandomGenerationRule.NO_LIMIT;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public final void writeObject(final XDataWriter writer) throws IOException {
        writer.writeInt(this.getUniqueID());
        if (this.savedObject == null) {
            writer.writeInt(-1);
        } else {
            this.savedObject.writeObject(writer);
        }
        final int cc = this.customCountersLength();
        for (int x = 0; x < cc; x++) {
            final int cx = this.getCustomCounter(x + 1).get();
            writer.writeInt(cx);
        }
    }

    @Override
    public final FantastleObjectModel readObject(final XDataReader reader,
            final int uid) throws IOException {
        if (uid == this.getUniqueID()) {
            final int savedIdent = reader.readInt();
            if (savedIdent != -1) {
                this.savedObject = FantastleReboot.getBagOStuff().getObjects()
                        .readSavedObject(reader, savedIdent,
                                FormatConstants.MAZE_FORMAT_LATEST);
            }
            final int cc = this.customCountersLength();
            for (int x = 0; x < cc; x++) {
                final int cx = reader.readInt();
                this.setCustomCounter(x + 1, cx);
            }
            return this;
        } else {
            return null;
        }
    }
}
