/*  Fantastle: A World-Solving Game
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;
import com.puttysoftware.images.BufferedImageIcon;

public class MonsterImageLoader {
  private static Properties fileExtensions;

  static BufferedImageIcon loadUncached(final String name,
      final ColorShader shader) {
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
          final Color c = new Color(pixel, true);
          final Color nc = shader.applyShade(c);
          result.setRGB(x, y, nc.getRGB());
        }
      }
      return result;
    } catch (final IOException ie) {
      FantastleReboot.exception(ie);
      return null;
    }
  }

  public static BufferedImageIcon load(final int imageID, final Faith faith) {
    final ColorShader shader = faith.getShader();
    if (MonsterImageLoader.fileExtensions == null) {
      try {
        MonsterImageLoader.fileExtensions = new Properties();
        MonsterImageLoader.fileExtensions
            .load(MonsterImageLoader.class.getResourceAsStream(
                "/assets/data/extensions/extensions.properties"));
      } catch (final IOException e) {
        FantastleReboot.exception(e);
      }
    }
    final String imageExt = MonsterImageLoader.fileExtensions
        .getProperty("images");
    final String cacheName = shader.getName() + "_" + Integer.toString(imageID);
    return ImageCache.getCachedImage(shader,
        Integer.toString(imageID) + imageExt, cacheName);
  }

  private static class ImageCache {
    // Fields
    private static ArrayList<ImageCacheEntry> cache;
    private static boolean cacheCreated = false;

    // Methods
    public static BufferedImageIcon getCachedImage(final ColorShader shader,
        final String imagePath, final String name) {
      if (!ImageCache.cacheCreated) {
        ImageCache.createCache();
      }
      for (final ImageCacheEntry entry : ImageCache.cache) {
        if (name.equals(entry.name())) {
          // Found
          return entry.image();
        }
      }
      // Not found: Add to cache
      final BufferedImageIcon newImage = MonsterImageLoader
          .loadUncached(imagePath, shader);
      final ImageCacheEntry newEntry = new ImageCacheEntry(newImage, name);
      ImageCache.cache.add(newEntry);
      return newImage;
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
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof ImageCacheEntry)) {
        return false;
      }
      final ImageCacheEntry other = (ImageCacheEntry) obj;
      return Objects.equals(this.name, other.name);
    }
  }
}
