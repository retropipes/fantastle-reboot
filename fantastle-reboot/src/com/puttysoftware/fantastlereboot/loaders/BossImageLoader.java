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
import com.puttysoftware.fantastlereboot.assets.BossImageIndex;
import com.puttysoftware.images.BufferedImageIcon;

public class BossImageLoader {
  private static String[] allFilenames;
  private static Properties fileExtensions;

  public static BufferedImageIcon load() {
    if (allFilenames == null) {
      allFilenames = DataLoader.loadBossImageData();
    }
    if (fileExtensions == null) {
      try {
        fileExtensions = new Properties();
        fileExtensions.load(SoundPlayer.class.getResourceAsStream(
            "/assets/data/extensions/extensions.properties"));
      } catch (IOException e) {
        FantastleReboot.logError(e);
      }
    }
    String imageExt = fileExtensions.getProperty("images");
    BossImageIndex image = BossImageIndex.BOSS;
    String name = "/assets/images/boss/" + allFilenames[image.ordinal()]
        + imageExt;
    return ImageLoader.load(name, BossImageLoader.class.getResource(name),
        FantastleReboot.getErrorHandler());
  }
}
