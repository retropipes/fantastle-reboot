/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map.turn;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.puttysoftware.fantastlereboot.assets.EffectImageIndex;
import com.puttysoftware.fantastlereboot.loaders.EffectImageLoader;
import com.puttysoftware.fantastlereboot.objects.temporary.BattleCharacter;
import com.puttysoftware.images.BufferedImageIcon;

public class MapTurnBattleStats {
    // Fields
    private Container statsPane;
    private JLabel nameLabel;
    private JLabel teamLabel;
    private JLabel hpLabel;
    private JLabel mpLabel;
    private JLabel attLabel;
    private JLabel defLabel;
    private JLabel apLabel;

    // Constructors
    public MapTurnBattleStats() {
        this.setUpGUI();
        this.updateIcons();
    }

    // Methods
    public Container getStatsPane() {
        return this.statsPane;
    }

    public void updateStats(final BattleCharacter bc) {
        this.nameLabel.setText(bc.getName());
        this.teamLabel.setText(bc.getTeamString());
        this.hpLabel.setText(bc.getCreature().getHPString());
        this.mpLabel.setText(bc.getCreature().getMPString());
        this.attLabel.setText(Integer.toString(bc.getCreature().getAttack()));
        this.defLabel.setText(Integer.toString(bc.getCreature().getDefense()));
        this.apLabel.setText(bc.getActionString());
        this.updateActionIcon(bc.getCurrentActions());
    }

    private void setUpGUI() {
        this.statsPane = new Container();
        this.statsPane.setLayout(new GridLayout(9, 1));
        this.nameLabel = new JLabel("", null, SwingConstants.LEFT);
        this.teamLabel = new JLabel("", null, SwingConstants.LEFT);
        this.hpLabel = new JLabel("", null, SwingConstants.LEFT);
        this.mpLabel = new JLabel("", null, SwingConstants.LEFT);
        this.attLabel = new JLabel("", null, SwingConstants.LEFT);
        this.defLabel = new JLabel("", null, SwingConstants.LEFT);
        this.apLabel = new JLabel("", null, SwingConstants.LEFT);
        this.statsPane.add(this.nameLabel);
        this.statsPane.add(this.teamLabel);
        this.statsPane.add(this.hpLabel);
        this.statsPane.add(this.mpLabel);
        this.statsPane.add(this.attLabel);
        this.statsPane.add(this.defLabel);
        this.statsPane.add(this.apLabel);
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
        final BufferedImageIcon apImage = EffectImageLoader
                .load(EffectImageIndex.ACTIONS_00);
        this.apLabel.setIcon(apImage);
    }

    private void updateActionIcon(final int actionsLeft) {
        EffectImageIndex actionImageId;
        switch (actionsLeft) {
        case 1:
            actionImageId = EffectImageIndex.ACTIONS_01;
            break;
        case 2:
            actionImageId = EffectImageIndex.ACTIONS_02;
            break;
        case 3:
            actionImageId = EffectImageIndex.ACTIONS_03;
            break;
        case 4:
            actionImageId = EffectImageIndex.ACTIONS_04;
            break;
        case 5:
            actionImageId = EffectImageIndex.ACTIONS_05;
            break;
        case 6:
            actionImageId = EffectImageIndex.ACTIONS_06;
            break;
        case 7:
            actionImageId = EffectImageIndex.ACTIONS_07;
            break;
        case 8:
            actionImageId = EffectImageIndex.ACTIONS_08;
            break;
        case 9:
            actionImageId = EffectImageIndex.ACTIONS_09;
            break;
        case 10:
            actionImageId = EffectImageIndex.ACTIONS_10;
            break;
        case 11:
            actionImageId = EffectImageIndex.ACTIONS_11;
            break;
        case 12:
            actionImageId = EffectImageIndex.ACTIONS_12;
            break;
        case 13:
            actionImageId = EffectImageIndex.ACTIONS_13;
            break;
        case 14:
            actionImageId = EffectImageIndex.ACTIONS_14;
            break;
        case 15:
            actionImageId = EffectImageIndex.ACTIONS_15;
            break;
        case 16:
            actionImageId = EffectImageIndex.ACTIONS_16;
            break;
        case 17:
            actionImageId = EffectImageIndex.ACTIONS_17;
            break;
        case 18:
            actionImageId = EffectImageIndex.ACTIONS_18;
            break;
        case 19:
            actionImageId = EffectImageIndex.ACTIONS_19;
            break;
        default:
            actionImageId = EffectImageIndex.ACTIONS_00;
            break;
        }
        final BufferedImageIcon apImage = EffectImageLoader.load(actionImageId);
        this.apLabel.setIcon(apImage);
    }
}
