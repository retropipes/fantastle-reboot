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
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.MazeObjectList;
import com.puttysoftware.images.BufferedImageIcon;

public class ObjectImageManager {
    private static final Color TRANSPARENT = new Color(200, 100, 100);

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



    public static BufferedImageIcon getImage(final String name) {
        // Get it from the cache
        return ImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
        try {
            String dm;
            if (FantastleReboot.getBagOStuff().getPrefsManager()
                    .isMobileModeEnabled()) {
                dm = "mobile";
            } else {
                dm = "desktop";
            }
            final String normalName = ObjectImageManager.normalizeName(name);
            final URL url = ObjectImageManager.class.getResource("/assets/graphics/"
                    + dm + "/objects/" + normalName + ".png");
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            return icon;
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    public static BufferedImageIcon getCompositeImage(final String name1,
            final String name2) {
        try {
            final BufferedImageIcon icon1 = ImageCache.getCachedImage(name1);
            final BufferedImageIcon icon2 = ImageCache.getCachedImage(name2);
            final BufferedImageIcon result = new BufferedImageIcon(icon2);
            if (icon1 != null && icon2 != null) {
                for (int x = 0; x < ObjectImageManager.getImageSize(); x++) {
                    for (int y = 0; y < ObjectImageManager.getImageSize(); y++) {
                        final int pixel = icon2.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(ObjectImageManager.TRANSPARENT)) {
                            result.setRGB(x, y, icon1.getRGB(x, y));
                        }
                    }
                }
                return result;
            } else {
                return null;
            }
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    public static int getImageSize() {
        return 64;
    }

    public static BufferedImageIcon getVirtualCompositeImage(final String name1,
            final String name2, final String name3) {
        try {
            final BufferedImageIcon icon3 = ImageCache.getCachedImage(name3);
            final BufferedImageIcon icon2 = ObjectImageManager.getCompositeImage(name1,
                    name2);
            final BufferedImageIcon result = new BufferedImageIcon(icon3);
            if (icon3 != null && icon2 != null) {
                for (int x = 0; x < ObjectImageManager.getImageSize(); x++) {
                    for (int y = 0; y < ObjectImageManager.getImageSize(); y++) {
                        final int pixel = icon3.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(ObjectImageManager.TRANSPARENT)) {
                            result.setRGB(x, y, icon2.getRGB(x, y));
                        }
                    }
                }
                return result;
            } else {
                return null;
            }
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    private static class ImageCache {
        // Fields
        private static BufferedImageIcon[] cache;
        private static String[] nameCache;
        private static boolean cacheCreated = false;

        // Methods
        public static BufferedImageIcon getCachedImage(final String name) {
            if (!ImageCache.cacheCreated) {
                ImageCache.createCache();
            }
            for (int x = 0; x < ImageCache.nameCache.length; x++) {
                if (name.equals(ImageCache.nameCache[x])) {
                    return ImageCache.cache[x];
                }
            }
            return null;
        }

        private static void createCache() {
            if (!ImageCache.cacheCreated) {
                // Create the cache
                final MazeObjectList list = FantastleReboot.getBagOStuff()
                        .getObjects();
                ImageCache.nameCache = list.getAllNamesForCache();
                ImageCache.cache = new BufferedImageIcon[ImageCache.nameCache.length];
                for (int x = 0; x < ImageCache.nameCache.length; x++) {
                    ImageCache.cache[x] = ObjectImageManager
                            .getUncachedImage(ImageCache.nameCache[x]);
                }
                ImageCache.cacheCreated = true;
            }
        }
    }
}
