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

import com.puttysoftware.fantastlereboot.loaders.ImageLoader;

class CacheTask extends Thread {
    // Constructors
    public CacheTask() {
        // Do nothing
    }

    @Override
    public void run() {
        FantastleReboot.getBagOStuff().getPrefsManager().updateWaitProgress(0);
        // Enter Wait Mode
        FantastleReboot.getBagOStuff().getPrefsManager().enterWaitMode();
        // Update Micro Logo
        FantastleReboot.getBagOStuff().updateMicroLogo();
        FantastleReboot.getBagOStuff().getPrefsManager().updateWaitProgress(25);
        // Update GUI Logo
        FantastleReboot.getBagOStuff().getGUIManager().updateLogo();
        FantastleReboot.getBagOStuff().getPrefsManager().updateWaitProgress(50);
        // Recreate image cache
        ImageLoader.recreateCache();
        FantastleReboot.getBagOStuff().getPrefsManager().updateWaitProgress(75);
        // Update stat image cache
        FantastleReboot.getBagOStuff().getGameManager().updateStatGUI();
        FantastleReboot.getBagOStuff().getPrefsManager()
                .updateWaitProgress(100);
        // Update Help
        FantastleReboot.getBagOStuff().getObjectHelpManager().updateHelpSize();
        FantastleReboot.getBagOStuff().getGeneralHelpManager().updateHelpSize();
        // Exit Wait Mode
        FantastleReboot.getBagOStuff().getPrefsManager().exitWaitMode();
        FantastleReboot.getBagOStuff().getPrefsManager().hidePrefs();
    }
}
