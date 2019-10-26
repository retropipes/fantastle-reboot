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

import com.puttysoftware.fantastlereboot.loaders.AttributeImageLoader;
import com.puttysoftware.fantastlereboot.loaders.AvatarImageLoader;
import com.puttysoftware.fantastlereboot.loaders.BossImageLoader;
import com.puttysoftware.fantastlereboot.loaders.EffectImageLoader;
import com.puttysoftware.fantastlereboot.loaders.ItemImageLoader;
import com.puttysoftware.fantastlereboot.loaders.MonsterImageLoader;
import com.puttysoftware.fantastlereboot.loaders.ObjectImageLoader;
import com.puttysoftware.fantastlereboot.loaders.UserInterfaceImageLoader;

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
    // Cache UI images
    UserInterfaceImageLoader.cacheAll();
    prefs.updateWaitProgress(11);
    // Cache Boss images
    BossImageLoader.cacheAll();
    prefs.updateWaitProgress(22);
    // Cache Item images
    ItemImageLoader.cacheAll();
    prefs.updateWaitProgress(33);
    // Cache Effect images
    EffectImageLoader.cacheAll();
    prefs.updateWaitProgress(44);
    // Cache Monster images
    MonsterImageLoader.cacheAll();
    prefs.updateWaitProgress(55);
    // Cache Avatar images
    AvatarImageLoader.cacheAll();
    prefs.updateWaitProgress(66);
    // Cache Attribute images
    AttributeImageLoader.cacheAll();
    prefs.updateWaitProgress(78);
    // Cache Object images
    ObjectImageLoader.cacheAll();
    prefs.updateWaitProgress(88);
    // Final tasks
    bag.getGeneralHelpManager().updateHelpSize();
    prefs.updateWaitProgress(100);
    // Exit Wait Mode
    prefs.exitWaitMode();
    bag.getGUIManager().showGUI();
  }
}
