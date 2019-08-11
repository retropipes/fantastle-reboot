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
package com.puttysoftware.fantastlereboot;

import com.puttysoftware.fantastlereboot.loaders.assets.ImageCache;
import com.puttysoftware.fantastlereboot.loaders.assets.MonsterImageCache;

class CacheTask extends Thread {
    // Constructors
    public CacheTask() {
        // Do nothing
    }

    @Override
    public void run() {
        FantastleReboot.getApplication().getPrefsManager().updateWaitProgress(0);
        // Enter Wait Mode
        FantastleReboot.getApplication().getPrefsManager().enterWaitMode();
        // Update Micro Logo
        FantastleReboot.getApplication().updateMicroLogo();
        FantastleReboot.getApplication().getPrefsManager().updateWaitProgress(20);
        // Update GUI Logo
        FantastleReboot.getApplication().getGUIManager().updateLogo();
        FantastleReboot.getApplication().getPrefsManager().updateWaitProgress(40);
        // Recreate image cache
        ImageCache.recreateCache();
        FantastleReboot.getApplication().getPrefsManager().updateWaitProgress(60);
        // Recreate monster image cache
        MonsterImageCache.recreateMonsterCache();
        FantastleReboot.getApplication().getPrefsManager().updateWaitProgress(80);
        // Update stat image cache
        FantastleReboot.getApplication().getGameManager().getStatGUI().updateGUI();
        FantastleReboot.getApplication().getPrefsManager().updateWaitProgress(100);
        // Update Help
        FantastleReboot.getApplication().getObjectHelpManager().updateHelpSize();
        FantastleReboot.getApplication().getGeneralHelpManager().updateHelpSize();
        // Exit Wait Mode
        FantastleReboot.getApplication().getPrefsManager().exitWaitMode();
        FantastleReboot.getApplication().getPrefsManager().hidePrefs();
    }
}
