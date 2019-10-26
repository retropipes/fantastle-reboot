/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import com.puttysoftware.fantastlereboot.loaders.MonsterImageLoader;
import com.puttysoftware.fantastlereboot.loaders.MonsterNames;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomRange;

abstract class AbstractBothRandomMonster extends Monster {
  // Constructors
  AbstractBothRandomMonster() {
    super();
    this.image = this.getInitialImage();
  }

  @Override
  protected BufferedImageIcon getInitialImage() {
    if (this.getLevel() == 0) {
      return null;
    } else {
      final String[] types = MonsterNames.getAllNames();
      final RandomRange r = new RandomRange(0, types.length - 1);
      int imageID = r.generate();
      this.setType(types[imageID]);
      return MonsterImageLoader.load(imageID, this.getFaith());
    }
  }

  @Override
  public void loadCreature() {
    this.image = this.getInitialImage();
  }
}
