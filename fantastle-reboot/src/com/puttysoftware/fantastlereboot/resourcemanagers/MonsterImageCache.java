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
package com.puttysoftware.fantastlereboot.resourcemanagers;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.help.GraphicalHelpViewer;
import com.puttysoftware.images.BufferedImageIcon;

public class MonsterImageCache {
    // Fields
    private static BufferedImageIcon[] cache;
    private static String[] nameCache;
    private static boolean cacheCreated = false;

    // Methods
    public static BufferedImageIcon getCachedMonsterImage(final String name) {
        if (!MonsterImageCache.cacheCreated) {
            MonsterImageCache.createMonsterCache();
        }
        for (int x = 0; x < MonsterImageCache.nameCache.length; x++) {
            if (name.equals(MonsterImageCache.nameCache[x])) {
                return MonsterImageCache.cache[x];
            }
        }
        return null;
    }

    private static void createMonsterCache() {
        if (!MonsterImageCache.cacheCreated) {
            // Create the cache
            MonsterImageCache.nameCache = MonsterNames.getAllNames();
            MonsterImageCache.cache = new BufferedImageIcon[MonsterImageCache.nameCache.length];
            for (int x = 0; x < MonsterImageCache.nameCache.length; x++) {
                MonsterImageCache.cache[x] = GraphicsManager
                        .getUncachedMonsterImage(
                                MonsterImageCache.nameCache[x]);
            }
            MonsterImageCache.cacheCreated = true;
        }
    }

    public static void viewMonsterCache() {
        if (!MonsterImageCache.cacheCreated) {
            MonsterImageCache.createMonsterCache();
        }
        final GraphicalHelpViewer cv = new GraphicalHelpViewer(
                MonsterImageCache.cache, MonsterImageCache.nameCache);
        final JFrame viewFrame = new JFrame("Monster Cache Viewer");
        viewFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        viewFrame.setLayout(new FlowLayout());
        viewFrame.add(cv.getHelp());
        cv.setHelpSize(GraphicsManager.MAX_DESKTOP_WINDOW_SIZE,
                GraphicsManager.MAX_DESKTOP_WINDOW_SIZE);
        viewFrame.pack();
        viewFrame.setResizable(false);
        viewFrame.setVisible(true);
    }

    public static void recreateMonsterCache() {
        MonsterImageCache.cacheCreated = false;
        MonsterImageCache.createMonsterCache();
    }
}