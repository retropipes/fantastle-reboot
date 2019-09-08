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
package com.puttysoftware.fantastlereboot.game;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.puttysoftware.fantastlereboot.loaders.old.GraphicsManager;
import com.puttysoftware.fantastlereboot.oldcreatures.PCManager;
import com.puttysoftware.fantastlereboot.oldcreatures.PlayerCharacter;
import com.puttysoftware.images.BufferedImageIcon;

public class StatGUI {
    // Fields
    private Container statsPane;
    private JLabel hpLabel;
    private JLabel mpLabel;
    private JLabel mlLabel;
    private JLabel goldLabel;
    private JLabel attackLabel;
    private JLabel defenseLabel;
    private JLabel xpLabel;

    // Constructors
    public StatGUI() {
        this.setUpGUI();
    }

    // Methods
    public Container getStatsPane() {
        return this.statsPane;
    }

    public void updateStats() {
        final PlayerCharacter pc = PCManager.getPlayer();
        this.hpLabel.setText(pc.getHPString());
        this.mpLabel.setText(pc.getMPString());
        this.mlLabel.setText(Integer.toString(pc.getMonsterLevel()));
        this.goldLabel.setText(Integer.toString(pc.getGold()));
        this.attackLabel.setText(Integer.toString(pc.getAttack()));
        this.defenseLabel.setText(Integer.toString(pc.getDefense()));
        this.xpLabel.setText(pc.getXPString());
    }

    private void setUpGUI() {
        this.statsPane = new Container();
        this.statsPane.setLayout(new GridLayout(7, 1));
        this.hpLabel = new JLabel("", null, SwingConstants.LEFT);
        this.mpLabel = new JLabel("", null, SwingConstants.LEFT);
        this.mlLabel = new JLabel("", null, SwingConstants.LEFT);
        this.goldLabel = new JLabel("", null, SwingConstants.LEFT);
        this.attackLabel = new JLabel("", null, SwingConstants.LEFT);
        this.defenseLabel = new JLabel("", null, SwingConstants.LEFT);
        this.xpLabel = new JLabel("", null, SwingConstants.LEFT);
        this.statsPane.add(this.hpLabel);
        this.statsPane.add(this.mpLabel);
        this.statsPane.add(this.mlLabel);
        this.statsPane.add(this.goldLabel);
        this.statsPane.add(this.attackLabel);
        this.statsPane.add(this.defenseLabel);
        this.statsPane.add(this.xpLabel);
    }

    public void updateGUI() {
        final BufferedImageIcon hpImage = GraphicsManager
                .getStatImage("health");
        this.hpLabel.setIcon(hpImage);
        final BufferedImageIcon mpImage = GraphicsManager.getStatImage("magic");
        this.mpLabel.setIcon(mpImage);
        final BufferedImageIcon mlImage = GraphicsManager.getStatImage("ml");
        this.mlLabel.setIcon(mlImage);
        final BufferedImageIcon goldImage = GraphicsManager
                .getStatImage("gold");
        this.goldLabel.setIcon(goldImage);
        final BufferedImageIcon attackImage = GraphicsManager
                .getStatImage("attack");
        this.attackLabel.setIcon(attackImage);
        final BufferedImageIcon defenseImage = GraphicsManager
                .getStatImage("defense");
        this.defenseLabel.setIcon(defenseImage);
        final BufferedImageIcon xpImage = GraphicsManager.getStatImage("xp");
        this.xpLabel.setIcon(xpImage);
    }
}
