/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.diane.objectmodel.GameObject;
import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;

public abstract class FantastleObject extends GameObject implements FantastleObjectModel {
    // Constructors
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
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        super(objectID, new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName,
                        attributeImage, attributeShader),
                cacheObjectName, objectImage));
    }

    public FantastleObject(final int objectID, final String cacheObjectName,
            final ObjectImageIndex objectImage, final ColorShader objectShader,
            final String cacheAttributeName,
            final AttributeImageIndex attributeImage,
            final ColorShader attributeShader) {
        super(objectID, new AttributedObjectAppearance(
                new AttributeAppearance(cacheAttributeName, attributeImage,
                        attributeShader),
                cacheObjectName, objectImage, objectShader));
    }
}
