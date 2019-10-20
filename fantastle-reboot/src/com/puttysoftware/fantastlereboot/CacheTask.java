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

class CacheTask extends Thread {
    // Constructors
    public CacheTask() {
        // Do nothing
    }

    @Override
    public void run() {
        BagOStuff bag = FantastleReboot.getBagOStuff();
        PreferencesManager prefs = bag.getPrefsManager();
        prefs.updateWaitProgress(0);
        // Enter Wait Mode
        prefs.enterWaitMode();
        // Update user interface images
        bag.updateMicroLogo();
        bag.getGUIManager().updateLogo();
        prefs.updateWaitProgress(50);
        // Update Help
        bag.getGeneralHelpManager().updateHelpSize();
        prefs.updateWaitProgress(100);
        // Exit Wait Mode
        prefs.exitWaitMode();
        bag.getGUIManager().showGUI();
    }
}
