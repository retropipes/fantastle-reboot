/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.diane.loaders.ImageShader;
import com.puttysoftware.fantastlereboot.assets.GameAttributeImage;
import com.puttysoftware.fantastlereboot.loaders.AttributeImageLoader;
import com.puttysoftware.images.BufferedImageIcon;

class AttributeAppearance extends Appearance {
    public AttributeAppearance(final String name,
            final GameAttributeImage inImageIndex) {
        super(name, inImageIndex);
    }

    public AttributeAppearance(final String name,
            final GameAttributeImage inImageIndex, final ColorShader inShader) {
        super(name, inImageIndex, inShader);
    }

    private GameAttributeImage getWhichAttributeImage() {
        return (GameAttributeImage) super.getWhichImage();
    }

    @Override
    public BufferedImageIcon getImage() {
        BufferedImageIcon image = AttributeImageLoader
                .load(this.getWhichAttributeImage());
        if (this.hasShading()) {
            image = ImageShader.shade(this.getCacheName(), image,
                    this.getShading());
        }
        return image;
    }
}
