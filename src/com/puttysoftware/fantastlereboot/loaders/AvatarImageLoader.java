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
import org.retropipes.diane.asset.image.ColorReplaceRules;
import org.retropipes.diane.asset.image.DianeImageLoader;

import com.puttysoftware.fantastlereboot.FantastleReboot;

public class AvatarImageLoader {
    private static Properties fileExtensions;
    private static final int MAX_FAMILY_INDEX = 7;

    public static BufferedImageIcon load(final int familyID, final ColorReplaceRules rules) {
	if (AvatarImageLoader.fileExtensions == null) {
	    AvatarImageLoader.fileExtensions = new Properties();
	    try (final InputStream stream = AvatarImageLoader.class
		    .getResourceAsStream("/assets/data/extensions/extensions.properties")) {
		AvatarImageLoader.fileExtensions.load(stream);
	    } catch (final IOException e) {
		FantastleReboot.exception(e);
	    }
	}
	final String imageExt = AvatarImageLoader.fileExtensions.getProperty("images");
	final String name = "/assets/images/avatars/" + Integer.toString(familyID) + imageExt;
	return rules.applyAll(DianeImageLoader.load(name, AvatarImageLoader.class.getResource(name)));
    }

    public static void cacheAll() {
	AvatarImageLoader.fileExtensions = new Properties();
	try (final InputStream stream = AvatarImageLoader.class
		.getResourceAsStream("/assets/data/extensions/extensions.properties")) {
	    AvatarImageLoader.fileExtensions.load(stream);
	} catch (final IOException e) {
	    FantastleReboot.exception(e);
	}
	final String imageExt = AvatarImageLoader.fileExtensions.getProperty("images");
	for (int familyID = 0; familyID <= AvatarImageLoader.MAX_FAMILY_INDEX; familyID++) {
	    final String name = "/assets/images/avatars/" + Integer.toString(familyID) + imageExt;
	    DianeImageLoader.load(name, AvatarImageLoader.class.getResource(name));
	}
    }
}
