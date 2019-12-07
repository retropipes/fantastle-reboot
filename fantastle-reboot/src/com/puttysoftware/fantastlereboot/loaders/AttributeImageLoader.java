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

import java.util.Properties;

import com.puttysoftware.diane.loaders.ImageLoader;
import com.puttysoftware.fantastlereboot.assets.AttributeImageIndex;
import com.puttysoftware.images.BufferedImageIcon;

public class AttributeImageLoader {
  private static String[] allFilenames;
  private static Properties fileExtensions;
  //private static final int MAX_INDEX = 69;

  public static BufferedImageIcon load(final AttributeImageIndex image) {
    if (image != AttributeImageIndex._NONE) {
      final String imageExt = AttributeImageLoader.fileExtensions
          .getProperty("images");
      final String name = "/assets/images/attributes/"
          + AttributeImageLoader.allFilenames[image.ordinal()] + imageExt;
      return ImageLoader.load(name,
          AttributeImageLoader.class.getResource(name));
    }
    return null;
  }
}
