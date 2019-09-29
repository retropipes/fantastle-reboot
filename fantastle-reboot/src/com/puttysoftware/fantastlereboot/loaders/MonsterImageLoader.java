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
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.monsters.Element;
import com.puttysoftware.help.GraphicalHelpViewer;
import com.puttysoftware.images.BufferedImageIcon;

public class MonsterImageLoader {
    private static String[] allFilenames;
    private static Properties fileExtensions;

    static BufferedImageIcon loadUncached(final String name,
            final Element elem) {
        try {
            final URL url = MonsterImageLoader.class
                    .getResource("/assets/images/monsters/" + name);
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon input = new BufferedImageIcon(image);
            final BufferedImageIcon result = new BufferedImageIcon(input);
            final int imageWidth = input.getWidth();
            final int imageHeight = input.getHeight();
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    final int pixel = input.getRGB(x, y);
                    final Color c = new Color(pixel);
                    final Color nc = elem.applyTransform(c);
                    result.setRGB(x, y, nc.getRGB());
                }
            }
            return result;
        } catch (final IOException ie) {
            FantastleReboot.logError(ie);
            return null;
        }
    }

    public static BufferedImageIcon load(final int imageID,
            final Element elem) {
        if (allFilenames == null) {
            allFilenames = DataLoader.loadMonsterImageData();
        }
        if (fileExtensions == null) {
            try {
                fileExtensions = new Properties();
                fileExtensions.load(SoundLoader.class.getResourceAsStream(
                        "/assets/data/extensions/extensions.properties"));
            } catch (IOException e) {
                FantastleReboot.logError(e);
            }
        }
        String imageExt = fileExtensions.getProperty("images");
        return ImageCache.getCachedImage(allFilenames[imageID] + imageExt,
                elem);
    }

    public static void viewCache() {
        if (!ImageCache.cacheCreated) {
            ImageCache.createCache();
        }
        final GraphicalHelpViewer cv = new GraphicalHelpViewer(
                ImageCache.images(), ImageCache.names());
        final JFrame viewFrame = new JFrame("Monster Image Cache Viewer");
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
        private static ArrayList<ImageCacheEntry> cache;
        private static boolean cacheCreated = false;

        // Methods
        public static BufferedImageIcon getCachedImage(final String name,
                final Element elem) {
            if (!ImageCache.cacheCreated) {
                ImageCache.createCache();
            }
            for (ImageCacheEntry entry : ImageCache.cache) {
                if (name.equals(entry.name())
                        && elem.getName().equals(entry.elementName())) {
                    // Found
                    return entry.image();
                }
            }
            // Not found: Add to cache
            BufferedImageIcon newImage = MonsterImageLoader.loadUncached(name,
                    elem);
            ImageCacheEntry newEntry = new ImageCacheEntry(newImage, name,
                    elem.getName());
            ImageCache.cache.add(newEntry);
            return newImage;
        }

        public static BufferedImageIcon[] images() {
            int limit = ImageCache.cache.size();
            BufferedImageIcon[] result = new BufferedImageIcon[limit];
            for (int x = 0; x < limit; x++) {
                result[x] = ImageCache.cache.get(x).image();
            }
            return result;
        }

        public static String[] names() {
            int limit = ImageCache.cache.size();
            String[] result = new String[limit];
            for (int x = 0; x < limit; x++) {
                result[x] = ImageCache.cache.get(x).name();
            }
            return result;
        }

        private static void createCache() {
            if (!ImageCache.cacheCreated) {
                // Create the cache
                ImageCache.cache = new ArrayList<>();
                ImageCache.cacheCreated = true;
            }
        }
    }

    private static class ImageCacheEntry {
        // Fields
        private final BufferedImageIcon image;
        private final String name;
        private final String elementName;

        // Constructors
        public ImageCacheEntry(final BufferedImageIcon newImage,
                final String newName, final String newElementName) {
            this.image = newImage;
            this.name = newName;
            this.elementName = newElementName;
        }

        // Methods
        public BufferedImageIcon image() {
            return this.image;
        }

        public String name() {
            return this.name;
        }

        public String elementName() {
            return this.elementName;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.elementName, this.name);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ImageCacheEntry)) {
                return false;
            }
            ImageCacheEntry other = (ImageCacheEntry) obj;
            return Objects.equals(this.elementName, other.elementName)
                    && Objects.equals(this.name, other.name);
        }
    }
}
