/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.diane.loaders.ImageShader;
import com.puttysoftware.fantastlereboot.assets.GameObjectImage;
import com.puttysoftware.fantastlereboot.loaders.ObjectImageLoader;
import com.puttysoftware.images.BufferedImageIcon;

class ObjectAppearance extends Appearance {
    public ObjectAppearance(final String name,
            final GameObjectImage inImageIndex) {
        super(name, inImageIndex);
    }

    public ObjectAppearance(final String name,
            final GameObjectImage inImageIndex, final ColorShader inShader) {
        super(name, inImageIndex, inShader);
    }

    private GameObjectImage getWhichObjectImage() {
        return (GameObjectImage) super.getWhichImage();
    }

    @Override
    public BufferedImageIcon getImage() {
        BufferedImageIcon image = ObjectImageLoader
                .load(this.getWhichObjectImage());
        if (this.hasShading()) {
            image = ImageShader.shade(this.getCacheName(), image,
                    this.getShading());
        }
        return image;
    }
}
