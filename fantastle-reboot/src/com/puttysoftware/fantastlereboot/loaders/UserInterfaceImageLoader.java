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
import com.puttysoftware.fantastlereboot.assets.UserInterfaceImageIndex;
import com.puttysoftware.images.BufferedImageIcon;

public class UserInterfaceImageLoader {
  private static String[] allFilenames;
  private static Properties fileExtensions;
  private static final int MAX_INDEX = 3;

  public static void preInit() {
    UserInterfaceImageLoader.allFilenames = DataLoader
        .loadUserInterfaceImageData();
    try {
      UserInterfaceImageLoader.fileExtensions = new Properties();
      UserInterfaceImageLoader.fileExtensions
          .load(UserInterfaceImageLoader.class.getResourceAsStream(
              "/assets/data/extensions/extensions.properties"));
    } catch (final IOException e) {
      FantastleReboot.exception(e);
    }
  }

  public static BufferedImageIcon load(final UserInterfaceImageIndex image) {
    final String imageExt = UserInterfaceImageLoader.fileExtensions
        .getProperty("images");
    final String name = "/assets/images/ui/"
        + UserInterfaceImageLoader.allFilenames[image.ordinal()] + imageExt;
    return ImageLoader.load(name,
        UserInterfaceImageLoader.class.getResource(name));
  }

  public static void cacheAll() {
    final String imageExt = UserInterfaceImageLoader.fileExtensions
        .getProperty("images");
    for (int i = 1; i <= UserInterfaceImageLoader.MAX_INDEX; i++) {
      final String name = "/assets/images/ui/"
          + UserInterfaceImageLoader.allFilenames[i] + imageExt;
      ImageLoader.load(name, UserInterfaceImageLoader.class.getResource(name));
    }
  }
}
