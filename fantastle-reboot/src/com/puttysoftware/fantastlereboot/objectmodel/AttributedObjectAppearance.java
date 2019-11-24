/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.fantastlereboot.objectmodel;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.diane.loaders.ImageShader;
import com.puttysoftware.diane.objectmodel.Appearance;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.loaders.ObjectImageLoader;
import com.puttysoftware.images.BufferedImageIcon;

class AttributedObjectAppearance extends Appearance {
  private final AttributeAppearance attribute;

  public AttributedObjectAppearance(final AttributeAppearance inAttribute,
      final String name, final ObjectImageIndex inImageIndex) {
    super(name, inImageIndex);
    this.attribute = inAttribute;
  }

  public AttributedObjectAppearance(final AttributeAppearance inAttribute,
      final String name, final ObjectImageIndex inImageIndex,
      final ColorShader inShader) {
    super(name, inImageIndex, inShader);
    this.attribute = inAttribute;
  }

  private ObjectImageIndex getWhichObjectImage() {
    return (ObjectImageIndex) super.getWhichImage();
  }

  @Override
  public BufferedImageIcon getImage() {
    BufferedImageIcon oImage = ObjectImageLoader
        .load(this.getWhichObjectImage());
    if (this.hasShading()) {
      oImage = ImageShader.shade(this.getCacheName(), oImage,
          this.getShading());
    }
    final BufferedImageIcon aImage = this.attribute.getImage();
    final String comboCacheName = this.getCacheName() + "_"
        + this.attribute.getCacheName();
    return ImageCompositor.composite(comboCacheName, oImage, aImage);
  }
}
