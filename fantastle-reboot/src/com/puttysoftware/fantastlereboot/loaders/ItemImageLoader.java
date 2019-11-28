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

import java.io.IOException;
import java.util.Properties;

import com.puttysoftware.diane.loaders.ImageLoader;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.ItemImageIndex;
import com.puttysoftware.images.BufferedImageIcon;

public class ItemImageLoader {
  private static String[] allFilenames;
  private static Properties fileExtensions;
  private static final int MAX_INDEX = 7;

  public static BufferedImageIcon load(final ItemImageIndex image) {
    if (image != ItemImageIndex._NONE) {
      final String imageExt = ItemImageLoader.fileExtensions
          .getProperty("images");
      final String name = "/assets/images/items/"
          + ItemImageLoader.allFilenames[image.ordinal()] + imageExt;
      return ImageLoader.load(name, ItemImageLoader.class.getResource(name));
    }
    return null;
  }

  public static void cacheAll() {
    ItemImageLoader.allFilenames = DataLoader.loadItemImageData();
    try {
      ItemImageLoader.fileExtensions = new Properties();
      ItemImageLoader.fileExtensions
          .load(ItemImageLoader.class.getResourceAsStream(
              "/assets/data/extensions/extensions.properties"));
    } catch (final IOException e) {
      FantastleReboot.exception(e);
    }
    final String imageExt = ItemImageLoader.fileExtensions
        .getProperty("images");
    for (int i = 0; i <= ItemImageLoader.MAX_INDEX; i++) {
      final String name = "/assets/images/items/"
          + ItemImageLoader.allFilenames[i] + imageExt;
      ImageLoader.load(name, ItemImageLoader.class.getResource(name));
    }
  }
}
