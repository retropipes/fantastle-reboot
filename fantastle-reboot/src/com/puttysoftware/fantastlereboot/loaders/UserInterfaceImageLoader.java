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
    allFilenames = DataLoader.loadUserInterfaceImageData();
    try {
      fileExtensions = new Properties();
      fileExtensions.load(UserInterfaceImageLoader.class.getResourceAsStream(
          "/assets/data/extensions/extensions.properties"));
    } catch (IOException e) {
      FantastleReboot.logError(e);
    }
  }

  public static BufferedImageIcon load(UserInterfaceImageIndex image) {
    String imageExt = fileExtensions.getProperty("images");
    String name = "/assets/images/ui/" + allFilenames[image.ordinal()]
        + imageExt;
    return ImageLoader.load(name,
        UserInterfaceImageLoader.class.getResource(name),
        FantastleReboot.getErrorHandler());
  }

  public static void cacheAll() {
    String imageExt = fileExtensions.getProperty("images");
    for (int i = 1; i <= MAX_INDEX; i++) {
      String name = "/assets/images/ui/" + allFilenames[i] + imageExt;
      ImageLoader.load(name, UserInterfaceImageLoader.class.getResource(name),
          FantastleReboot.getErrorHandler());
    }
  }
}
