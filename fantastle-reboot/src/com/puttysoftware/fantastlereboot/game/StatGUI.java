/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.puttysoftware.fantastlereboot.assets.EffectImageIndex;
import com.puttysoftware.fantastlereboot.creatures.party.Party;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;
import com.puttysoftware.fantastlereboot.loaders.EffectImageLoader;
import com.puttysoftware.images.BufferedImageIcon;

class StatGUI {
  // Fields
  private static Container statsPane;
  private static JLabel hpLabel;
  private static JLabel mpLabel;
  private static JLabel goldLabel;
  private static JLabel attackLabel;
  private static JLabel defenseLabel;
  private static JLabel xpLabel;
  private static JLabel levelLabel;
  private static boolean guiSetUp = false;

  // Constructors
  private StatGUI() {
    // Do nothing
  }

  // Methods
  static Container getStatsPane() {
    if (!StatGUI.guiSetUp) {
      StatGUI.setUpGUI();
      StatGUI.guiSetUp = true;
    }
    return StatGUI.statsPane;
  }

  static void updateStats() {
    if (!StatGUI.guiSetUp) {
      StatGUI.setUpGUI();
      StatGUI.guiSetUp = true;
    }
    final Party party = PartyManager.getParty();
    if (party != null) {
      final PartyMember pc = party.getLeader();
      if (pc != null) {
        StatGUI.hpLabel.setText(pc.getHPString());
        StatGUI.mpLabel.setText(pc.getMPString());
        StatGUI.goldLabel.setText(Integer.toString(pc.getGold()));
        StatGUI.attackLabel.setText(Integer.toString(pc.getAttack()));
        StatGUI.defenseLabel.setText(Integer.toString(pc.getDefense()));
        StatGUI.xpLabel.setText(pc.getXPString());
        StatGUI.levelLabel.setText(party.getMonsterLevelString());
      }
    }
  }

  private static void setUpGUI() {
    StatGUI.statsPane = new Container();
    StatGUI.statsPane.setLayout(new GridLayout(7, 1));
    StatGUI.hpLabel = new JLabel("", null, SwingConstants.LEFT);
    StatGUI.mpLabel = new JLabel("", null, SwingConstants.LEFT);
    StatGUI.goldLabel = new JLabel("", null, SwingConstants.LEFT);
    StatGUI.attackLabel = new JLabel("", null, SwingConstants.LEFT);
    StatGUI.defenseLabel = new JLabel("", null, SwingConstants.LEFT);
    StatGUI.xpLabel = new JLabel("", null, SwingConstants.LEFT);
    StatGUI.levelLabel = new JLabel("", null, SwingConstants.LEFT);
    StatGUI.statsPane.add(StatGUI.hpLabel);
    StatGUI.statsPane.add(StatGUI.mpLabel);
    StatGUI.statsPane.add(StatGUI.goldLabel);
    StatGUI.statsPane.add(StatGUI.attackLabel);
    StatGUI.statsPane.add(StatGUI.defenseLabel);
    StatGUI.statsPane.add(StatGUI.xpLabel);
    StatGUI.statsPane.add(StatGUI.levelLabel);
  }

  static void updateImages() {
    if (!StatGUI.guiSetUp) {
      StatGUI.setUpGUI();
      StatGUI.guiSetUp = true;
    }
    final BufferedImageIcon hpImage = EffectImageLoader
        .load(EffectImageIndex.HEALTH);
    StatGUI.hpLabel.setIcon(hpImage);
    final BufferedImageIcon mpImage = EffectImageLoader
        .load(EffectImageIndex.MAGIC);
    StatGUI.mpLabel.setIcon(mpImage);
    final BufferedImageIcon goldImage = EffectImageLoader
        .load(EffectImageIndex.MONEY);
    StatGUI.goldLabel.setIcon(goldImage);
    final BufferedImageIcon attackImage = EffectImageLoader
        .load(EffectImageIndex.MELEE_ATTACK);
    StatGUI.attackLabel.setIcon(attackImage);
    final BufferedImageIcon defenseImage = EffectImageLoader
        .load(EffectImageIndex.DEFENSE);
    StatGUI.defenseLabel.setIcon(defenseImage);
    final BufferedImageIcon xpImage = EffectImageLoader
        .load(EffectImageIndex.EXPERIENCE);
    StatGUI.xpLabel.setIcon(xpImage);
    final BufferedImageIcon levelImage = EffectImageLoader
        .load(EffectImageIndex.CREATURE_LEVEL);
    StatGUI.levelLabel.setIcon(levelImage);
  }
}
