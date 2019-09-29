package com.puttysoftware.fantastlereboot.loaders;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.help.GraphicalHelpViewer;
import com.puttysoftware.images.BufferedImageIcon;

public class ImageCompositor {
    private static BufferedImageIcon compositeTwo(
            final BufferedImageIcon input1, final BufferedImageIcon input2) {
        if (input1 != null && input2 != null) {
            int width = input1.getWidth();
            int height = input1.getHeight();
            int width2 = input2.getWidth();
            int height2 = input2.getHeight();
            if (width == width2 && height == height2) {
                final BufferedImageIcon result = new BufferedImageIcon(input2);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        final int pixel = input2.getRGB(x, y);
                        final Color c = new Color(pixel, true);
                        if (c.getAlpha() == 0) {
                            result.setRGB(x, y, input1.getRGB(x, y));
                        }
                    }
                }
                return result;
            }
        }
        return null;
    }

    static BufferedImageIcon compositeUncached(final String name,
            final BufferedImageIcon... inputs) {
        if (inputs != null && inputs.length >= 2) {
            BufferedImageIcon result = ImageCompositor.compositeTwo(inputs[0],
                    inputs[1]);
            int beyond2 = inputs.length;
            for (int i = 2; i < beyond2; i++) {
                result = ImageCompositor.compositeTwo(result, inputs[i]);
            }
            return result;
        }
        return null;
    }

    public static BufferedImageIcon composite(final String name,
            final BufferedImageIcon... inputs) {
        return ImageCache.getCachedImage(name, inputs);
    }

    public static void viewCache() {
        if (!ImageCache.cacheCreated) {
            ImageCache.createCache();
        }
        final GraphicalHelpViewer cv = new GraphicalHelpViewer(
                ImageCache.images(), ImageCache.names());
        final JFrame viewFrame = new JFrame("Composite Image Cache Viewer");
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
                final BufferedImageIcon... inputs) {
            if (!ImageCache.cacheCreated) {
                ImageCache.createCache();
            }
            for (ImageCacheEntry entry : ImageCache.cache) {
                if (name.equals(entry.name())) {
                    // Found
                    return entry.image();
                }
            }
            // Not found: Add to cache
            BufferedImageIcon newImage = ImageCompositor.compositeUncached(name,
                    inputs);
            ImageCacheEntry newEntry = new ImageCacheEntry(newImage, name);
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

        // Constructors
        public ImageCacheEntry(final BufferedImageIcon newImage,
                final String newName) {
            this.image = newImage;
            this.name = newName;
        }

        // Methods
        public BufferedImageIcon image() {
            return this.image;
        }

        public String name() {
            return this.name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name);
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
            return Objects.equals(this.name, other.name);
        }
    }
}
