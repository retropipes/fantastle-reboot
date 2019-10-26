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

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.fantastlereboot.assets.UserInterfaceImageIndex;
import com.puttysoftware.fantastlereboot.loaders.AttributeImageLoader;
import com.puttysoftware.fantastlereboot.loaders.AvatarImageLoader;
import com.puttysoftware.fantastlereboot.loaders.BossImageLoader;
import com.puttysoftware.fantastlereboot.loaders.EffectImageLoader;
import com.puttysoftware.fantastlereboot.loaders.ItemImageLoader;
import com.puttysoftware.fantastlereboot.loaders.MonsterImageLoader;
import com.puttysoftware.fantastlereboot.loaders.ObjectImageLoader;
import com.puttysoftware.fantastlereboot.loaders.UserInterfaceImageLoader;

class CacheTask extends Thread {
  private JFrame waitFrame;
  private JLabel waitLabel;
  private JProgressBar waitProgress;

  // Constructors
  public CacheTask() {
    // Set up wait frame
    this.waitFrame = new JFrame("Please Wait...");
    this.waitLabel = new JLabel(
        UserInterfaceImageLoader.load(UserInterfaceImageIndex.LOADING));
    this.waitProgress = new JProgressBar();
    this.waitProgress.setMinimum(0);
    this.waitProgress.setMaximum(100);
    this.waitProgress.setValue(0);
    this.waitFrame.getContentPane().setLayout(new GridLayout(2, 1));
    this.waitFrame.getContentPane().add(this.waitLabel);
    this.waitFrame.getContentPane().add(this.waitProgress);
    this.waitFrame
        .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.waitFrame.setResizable(false);
    this.waitFrame.setAlwaysOnTop(true);
    this.waitFrame.pack();
  }

  @Override
  public void run() {
    try {
      // Enter Wait Mode
      this.enterWaitMode();
      // Cache UI images
      UserInterfaceImageLoader.cacheAll();
      this.updateWaitProgress(11);
      // Cache Boss images
      BossImageLoader.cacheAll();
      this.updateWaitProgress(22);
      // Cache Item images
      ItemImageLoader.cacheAll();
      this.updateWaitProgress(33);
      // Cache Effect images
      EffectImageLoader.cacheAll();
      this.updateWaitProgress(44);
      // Cache Monster images
      MonsterImageLoader.cacheAll();
      this.updateWaitProgress(55);
      // Cache Avatar images
      AvatarImageLoader.cacheAll();
      this.updateWaitProgress(66);
      // Cache Attribute images
      AttributeImageLoader.cacheAll();
      this.updateWaitProgress(78);
      // Cache Object images
      ObjectImageLoader.cacheAll();
      this.updateWaitProgress(88);
      // Final tasks
      BagOStuff bag = FantastleReboot.getBagOStuff();
      bag.postConstruct();
      bag.getObjects().initializeObjects();
      bag.getGeneralHelpManager().updateHelpSize();
      this.updateWaitProgress(100);
      // Exit Wait Mode
      this.exitWaitMode();
      bag.playLogoSound();
      bag.getGUIManager().showGUI();
    } catch (Throwable t) {
      FantastleReboot.logError(t);
    }
  }

  private void enterWaitMode() {
    this.waitFrame.setVisible(true);
  }

  private void updateWaitProgress(final int value) {
    this.waitProgress.setValue(value);
  }

  private void exitWaitMode() {
    this.waitFrame.setVisible(false);
  }
}
