/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map.time;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.puttysoftware.fantastlereboot.assets.EffectImageIndex;
import com.puttysoftware.fantastlereboot.loaders.EffectImageLoader;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.images.BufferedImageIcon;

public class MapTimeBattleStats {
  // Fields
  private JPanel statsPane;
  private JLabel nameLabel;
  private JLabel teamLabel;
  private JLabel hpLabel;
  private JLabel mpLabel;
  private JLabel attLabel;
  private JLabel defLabel;

  // Constructors
  public MapTimeBattleStats() {
    this.setUpGUI();
    this.updateIcons();
  }

  // Methods
  public JPanel getStatsPane() {
    return this.statsPane;
  }

  public void updateStats(final BattleCharacter bc) {
    this.nameLabel.setText(bc.getName());
    this.teamLabel.setText(bc.getTeamString());
    this.hpLabel.setText(bc.getCreature().getHPString());
    this.mpLabel.setText(bc.getCreature().getMPString());
    this.attLabel.setText(Integer.toString(bc.getCreature().getAttack()));
    this.defLabel.setText(Integer.toString(bc.getCreature().getDefense()));
  }

  private void setUpGUI() {
    this.statsPane = new JPanel();
    this.statsPane.setLayout(new GridLayout(6, 1));
    this.nameLabel = new JLabel("", null, SwingConstants.LEFT);
    this.teamLabel = new JLabel("", null, SwingConstants.LEFT);
    this.hpLabel = new JLabel("", null, SwingConstants.LEFT);
    this.mpLabel = new JLabel("", null, SwingConstants.LEFT);
    this.attLabel = new JLabel("", null, SwingConstants.LEFT);
    this.defLabel = new JLabel("", null, SwingConstants.LEFT);
    this.statsPane.add(this.nameLabel);
    this.statsPane.add(this.teamLabel);
    this.statsPane.add(this.hpLabel);
    this.statsPane.add(this.mpLabel);
    this.statsPane.add(this.attLabel);
    this.statsPane.add(this.defLabel);
  }

  private void updateIcons() {
    final BufferedImageIcon nameImage = EffectImageLoader
        .load(EffectImageIndex.CREATURE_ID);
    this.nameLabel.setIcon(nameImage);
    final BufferedImageIcon teamImage = EffectImageLoader
        .load(EffectImageIndex.CREATURE_TEAM);
    this.teamLabel.setIcon(teamImage);
    final BufferedImageIcon hpImage = EffectImageLoader
        .load(EffectImageIndex.HEALTH);
    this.hpLabel.setIcon(hpImage);
    final BufferedImageIcon mpImage = EffectImageLoader
        .load(EffectImageIndex.MAGIC);
    this.mpLabel.setIcon(mpImage);
    final BufferedImageIcon attImage = EffectImageLoader
        .load(EffectImageIndex.MELEE_ATTACK);
    this.attLabel.setIcon(attImage);
    final BufferedImageIcon defImage = EffectImageLoader
        .load(EffectImageIndex.DEFENSE);
    this.defLabel.setIcon(defImage);
  }
}
