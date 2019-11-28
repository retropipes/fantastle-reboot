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
import com.puttysoftware.images.BufferedImageIcon;

public class AvatarImageLoader {
  private static Properties fileExtensions;
  private static final int MAX_FAMILY_INDEX = 5;
  private static final int MAX_HAIR_INDEX = 9;
  private static final int MAX_SKIN_INDEX = 9;

  public static BufferedImageIcon load(final int familyID, final int skinID,
      final int hairID) {
    if (AvatarImageLoader.fileExtensions == null) {
      try {
        AvatarImageLoader.fileExtensions = new Properties();
        AvatarImageLoader.fileExtensions
            .load(AvatarImageLoader.class.getResourceAsStream(
                "/assets/data/extensions/extensions.properties"));
      } catch (final IOException e) {
        FantastleReboot.exception(e);
      }
    }
    final String imageExt = AvatarImageLoader.fileExtensions
        .getProperty("images");
    final String name = "/assets/images/avatars/" + Integer.toString(familyID)
        + Integer.toString(skinID) + Integer.toString(hairID) + imageExt;
    return ImageLoader.load(name, AvatarImageLoader.class.getResource(name));
  }

  public static void cacheAll() {
    try {
      AvatarImageLoader.fileExtensions = new Properties();
      AvatarImageLoader.fileExtensions
          .load(AvatarImageLoader.class.getResourceAsStream(
              "/assets/data/extensions/extensions.properties"));
    } catch (final IOException e) {
      FantastleReboot.exception(e);
    }
    final String imageExt = AvatarImageLoader.fileExtensions
        .getProperty("images");
    for (int familyID = 0; familyID <= AvatarImageLoader.MAX_FAMILY_INDEX; familyID++) {
      for (int skinID = 0; skinID <= AvatarImageLoader.MAX_SKIN_INDEX; skinID++) {
        for (int hairID = 0; hairID <= AvatarImageLoader.MAX_HAIR_INDEX; hairID++) {
          final String name = "/assets/images/avatars/"
              + Integer.toString(familyID) + Integer.toString(skinID)
              + Integer.toString(hairID) + imageExt;
          ImageLoader.load(name, AvatarImageLoader.class.getResource(name));
        }
      }
    }
  }
}
