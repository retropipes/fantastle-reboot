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
package net.worldwizard.fantastle5.resourcemanagers;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.help.GraphicalHelpViewer;
import com.puttysoftware.images.BufferedImageIcon;

import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.generic.MazeObjectList;

public class ImageCache {
    // Fields
    private static BufferedImageIcon[] cache;
    private static String[] nameCache;
    private static boolean cacheCreated = false;

    // Methods
    public static BufferedImageIcon getCachedImage(final String name) {
        if (!ImageCache.cacheCreated) {
            ImageCache.createCache();
        }
        for (int x = 0; x < ImageCache.nameCache.length; x++) {
            if (name.equals(ImageCache.nameCache[x])) {
                return ImageCache.cache[x];
            }
        }
        return null;
    }

    private static void createCache() {
        if (!ImageCache.cacheCreated) {
            // Create the cache
            final MazeObjectList list = Fantastle5.getApplication()
                    .getObjects();
            ImageCache.nameCache = list.getAllNamesForCache();
            ImageCache.cache = new BufferedImageIcon[ImageCache.nameCache.length];
            for (int x = 0; x < ImageCache.nameCache.length; x++) {
                ImageCache.cache[x] = GraphicsManager
                        .getUncachedImage(ImageCache.nameCache[x]);
            }
            ImageCache.cacheCreated = true;
        }
    }

    public static void viewCache() {
        if (!ImageCache.cacheCreated) {
            ImageCache.createCache();
        }
        final GraphicalHelpViewer cv = new GraphicalHelpViewer(ImageCache.cache,
                ImageCache.nameCache);
        final JFrame viewFrame = new JFrame("Image Cache Viewer");
        viewFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        viewFrame.setLayout(new FlowLayout());
        viewFrame.add(cv.getHelp());
        cv.setHelpSize(GraphicsManager.MAX_DESKTOP_WINDOW_SIZE,
                GraphicsManager.MAX_DESKTOP_WINDOW_SIZE);
        viewFrame.pack();
        viewFrame.setResizable(false);
        viewFrame.setVisible(true);
    }

    public static void recreateCache() {
        ImageCache.cacheCreated = false;
        ImageCache.createCache();
    }
}