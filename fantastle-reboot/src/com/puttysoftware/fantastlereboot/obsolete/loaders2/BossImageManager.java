/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.loaders2;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.images.BufferedImageIcon;

public class BossImageManager {
    private static final String DEFAULT_LOAD_PATH = "/com/puttysoftware/tallertower/resources/graphics/boss/";
    private static String LOAD_PATH = BossImageManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = BossImageManager.class;
    static int BOSS_IMAGE_SIZE = 64;

    public static BufferedImageIcon getBossImage() {
        // Get it from the cache
        final BufferedImageIcon bii = BossImageCache.getCachedImage("boss");
        return ImageTransformer.getTransformedImage(bii, BOSS_IMAGE_SIZE);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
        try {
            final String normalName = ImageTransformer.normalizeName(name);
            final URL url = BossImageManager.LOAD_CLASS
                    .getResource(BossImageManager.LOAD_PATH + normalName
                            + ".png");
            final BufferedImage image = ImageIO.read(url);
            return new BufferedImageIcon(image);
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }
}
