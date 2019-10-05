package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.diane.loaders.ImageShader;
import com.puttysoftware.fantastlereboot.assets.GameObjectImage;
import com.puttysoftware.fantastlereboot.loaders.ObjectImageLoader;
import com.puttysoftware.images.BufferedImageIcon;

public class AttributedObjectAppearance extends Appearance {
    private final AttributeAppearance attribute;

    public AttributedObjectAppearance(final AttributeAppearance inAttribute,
            final String name, final GameObjectImage inImageIndex) {
        super(name, inImageIndex);
        this.attribute = inAttribute;
    }

    public AttributedObjectAppearance(final AttributeAppearance inAttribute,
            final String name, final GameObjectImage inImageIndex,
            final ColorShader inShader) {
        super(name, inImageIndex, inShader);
        this.attribute = inAttribute;
    }

    private GameObjectImage getWhichObjectImage() {
        return (GameObjectImage) super.getWhichImage();
    }

    @Override
    public BufferedImageIcon getImage() {
        BufferedImageIcon oImage = ObjectImageLoader
                .load(this.getWhichObjectImage());
        if (this.hasShading()) {
            oImage = ImageShader.shade(this.getCacheName(), oImage,
                    this.getShading());
        }
        BufferedImageIcon aImage = this.attribute.getImage();
        String comboCacheName = this.getCacheName() + "_"
                + this.attribute.getCacheName();
        return ImageCompositor.composite(comboCacheName, oImage, aImage);
    }
}
