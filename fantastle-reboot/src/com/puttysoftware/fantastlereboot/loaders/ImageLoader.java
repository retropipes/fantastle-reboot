/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.loaders;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.GameBossImage;
import com.puttysoftware.fantastlereboot.assets.GameEffectImage;
import com.puttysoftware.fantastlereboot.obsolete.maze1.generic.MazeObjectList;
import com.puttysoftware.help.GraphicalHelpViewer;
import com.puttysoftware.images.BufferedImageIcon;

public class ImageLoader {
    public static final int MAX_MOBILE_WINDOW_SIZE = 320;
    public static final int MAX_DESKTOP_WINDOW_SIZE = 700;
    private static final Color TRANSPARENT = new Color(200, 100, 100);
    private static Color REPLACE = null;
    private static String bossFilename;
    private static String[] allEffectFilenames;
    private static Properties fileExtensions;

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
            final String normalName = ImageLoader.normalizeName(name);
            final URL url = ImageLoader.class.getResource("/assets/graphics/"
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

    public static BufferedImageIcon getTransformedImage(final String name) {
        if (ImageLoader.REPLACE == null) {
            ImageLoader.defineReplacementColor();
        }
        try {
            final BufferedImageIcon icon = ImageCache.getCachedImage(name);
            final BufferedImageIcon result = new BufferedImageIcon(icon);
            if (icon != null) {
                for (int x = 0; x < ImageLoader.getGraphicSize(); x++) {
                    for (int y = 0; y < ImageLoader.getGraphicSize(); y++) {
                        final int pixel = icon.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(ImageLoader.TRANSPARENT)) {
                            result.setRGB(x, y, ImageLoader.REPLACE.getRGB());
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

    public static BufferedImageIcon getCompositeImage(final String name1,
            final String name2) {
        try {
            final BufferedImageIcon icon1 = ImageCache.getCachedImage(name1);
            final BufferedImageIcon icon2 = ImageCache.getCachedImage(name2);
            final BufferedImageIcon result = new BufferedImageIcon(icon2);
            if (icon1 != null && icon2 != null) {
                for (int x = 0; x < ImageLoader.getGraphicSize(); x++) {
                    for (int y = 0; y < ImageLoader.getGraphicSize(); y++) {
                        final int pixel = icon2.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(ImageLoader.TRANSPARENT)) {
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

    public static BufferedImageIcon getVirtualCompositeImage(final String name1,
            final String name2, final String name3) {
        try {
            final BufferedImageIcon icon3 = ImageCache.getCachedImage(name3);
            final BufferedImageIcon icon2 = ImageLoader.getCompositeImage(name1,
                    name2);
            final BufferedImageIcon result = new BufferedImageIcon(icon3);
            if (icon3 != null && icon2 != null) {
                for (int x = 0; x < ImageLoader.getGraphicSize(); x++) {
                    for (int y = 0; y < ImageLoader.getGraphicSize(); y++) {
                        final int pixel = icon3.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(ImageLoader.TRANSPARENT)) {
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

    private static void ensureFileExtensions() {
        if (fileExtensions == null) {
            try {
                fileExtensions = new Properties();
                fileExtensions.load(SoundLoader.class.getResourceAsStream(
                        "/assets/data/extensions/extensions.properties"));
            } catch (IOException e) {
                FantastleReboot.logError(e);
            }
        }
    }

    public static BufferedImageIcon loadBossImage() {
        if (bossFilename == null) {
            bossFilename = DataLoader.loadBossImageData()[0];
        }
        ImageLoader.ensureFileExtensions();
        GameBossImage image = GameBossImage.BOSS;
        String imageExt = fileExtensions.getProperty("images");
        return ImageCache
                .getCachedImage(allEffectFilenames[image.ordinal()] + imageExt);
    }

    public static BufferedImageIcon loadEffectImage(GameEffectImage image) {
        if (allEffectFilenames == null) {
            allEffectFilenames = DataLoader.loadEffectImageData();
        }
        ImageLoader.ensureFileExtensions();
        String imageExt = fileExtensions.getProperty("images");
        return ImageCache
                .getCachedImage(allEffectFilenames[image.ordinal()] + imageExt);
    }

    private static String normalizeName(final String name) {
        final StringBuffer sb = new StringBuffer(name);
        for (int x = 0; x < sb.length(); x++) {
            if (!Character.isLetter(sb.charAt(x))
                    && !Character.isDigit(sb.charAt(x))) {
                sb.setCharAt(x, '_');
            }
        }
        return sb.toString().toLowerCase();
    }

    private static void defineReplacementColor() {
        if (System.getProperty("os.name").startsWith("Mac OS X")) {
            ImageLoader.REPLACE = UIManager.getColor("text");
        } else {
            ImageLoader.REPLACE = UIManager.getColor("control");
        }
    }

    public static int getGraphicSize() {
        return 64;
    }

    public static void viewCache() {
        if (!ImageCache.cacheCreated) {
            ImageCache.createCache();
        }
        final GraphicalHelpViewer cv = new GraphicalHelpViewer(ImageCache.cache,
                ImageCache.nameCache);
        final JFrame viewFrame = new JFrame("Image Cache Viewer");
        viewFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        viewFrame.setLayout(new FlowLayout());
        viewFrame.add(cv.getHelp());
        cv.setHelpSize(ImageLoader.MAX_DESKTOP_WINDOW_SIZE,
                ImageLoader.MAX_DESKTOP_WINDOW_SIZE);
        viewFrame.pack();
        viewFrame.setResizable(false);
        viewFrame.setVisible(true);
    }

    public static void recreateCache() {
        ImageCache.cacheCreated = false;
        ImageCache.createCache();
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
                    ImageCache.cache[x] = ImageLoader
                            .getUncachedImage(ImageCache.nameCache[x]);
                }
                ImageCache.cacheCreated = true;
            }
        }
    }
}
