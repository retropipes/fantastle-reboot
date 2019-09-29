/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.loaders;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.diane.loaders.ImageShader;
import com.puttysoftware.images.BufferedImageIcon;

public class ObjectImageManager {
    private static final String DEFAULT_LOAD_PATH = "/com/puttysoftware/tallertower/resources/graphics/objects/";
    private static String LOAD_PATH = ObjectImageManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = ObjectImageManager.class;

    /**
     *
     * @param name
     * @param baseID
     * @param transformColor
     * @return
     */
    public static BufferedImageIcon getImage(final String name,
            final int baseID, final int transformColor) {
        // Get it from the cache
        final String baseName = ObjectImageConstants.getObjectImageName(baseID);
        final BufferedImageIcon bii = ObjectImageCache.getCachedImage(name,
                baseName);
        return ImageShader.shade(baseName, bii,
                new ColorShader(baseName, new Color(transformColor, false)));
    }

    static BufferedImageIcon getUncachedImage(final String name) {
        try {
            final String normalName = normalizeName(name);
            final URL url = ObjectImageManager.LOAD_CLASS.getResource(
                    ObjectImageManager.LOAD_PATH + normalName + ".png");
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

    private static String normalizeName(final String name) {
        final StringBuilder sb = new StringBuilder(name);
        for (int x = 0; x < sb.length(); x++) {
            if (!Character.isLetter(sb.charAt(x))
                    && !Character.isDigit(sb.charAt(x))) {
                sb.setCharAt(x, '_');
            }
        }
        return sb.toString().toLowerCase();
    }
}
