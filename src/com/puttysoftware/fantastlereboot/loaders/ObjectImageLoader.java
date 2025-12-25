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
import java.io.InputStream;
import java.util.Properties;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.asset.image.DianeImageLoader;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;

public class ObjectImageLoader {
    private static String[] allFilenames;
    private static Properties fileExtensions;
    private static final int MAX_INDEX = 244;

    public static BufferedImageIcon load(final ObjectImageIndex image) {
	if (image != ObjectImageIndex._NONE) {
	    final String imageExt = ObjectImageLoader.fileExtensions.getProperty("images");
	    final String name = "/assets/images/objects/" + ObjectImageLoader.allFilenames[image.ordinal()] + imageExt;
	    return DianeImageLoader.load(name, ObjectImageLoader.class.getResource(name));
	}
	return null;
    }

    public static void cacheAll() {
	ObjectImageLoader.allFilenames = DataLoader.loadObjectImageData();
	try (InputStream stream = ObjectImageLoader.class
		.getResourceAsStream("/assets/data/extensions/extensions.properties")) {
	    ObjectImageLoader.fileExtensions = new Properties();
	    ObjectImageLoader.fileExtensions.load(stream);
	} catch (final IOException e) {
	    FantastleReboot.exception(e);
	}
	final String imageExt = ObjectImageLoader.fileExtensions.getProperty("images");
	for (int i = 0; i <= ObjectImageLoader.MAX_INDEX; i++) {
	    final String name = "/assets/images/objects/" + ObjectImageLoader.allFilenames[i] + imageExt;
	    try {
		DianeImageLoader.load(name, ObjectImageLoader.class.getResource(name));
	    } catch (final IllegalArgumentException iae) {
		// Ignore - image unused
	    }
	}
    }
}
