/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.creatures.StatConstants;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;

public class StatisticsViewer {
  // Fields
  private static MainWindow statisticsFrame;
  private static JPanel statisticsPane;
  private static JPanel contentPane;
  private static JPanel buttonPane;
  private static JButton btnOK;
  private static JLabel[] statisticsValues;
  private static boolean inited = false;

  // Private Constructor
  private StatisticsViewer() {
    // Do nothing
  }

  // Methods
  public static void viewStatistics() {
    StatisticsViewer.setUpGUI();
    final PartyMember leader = PartyManager.getParty().getLeader();
    if (leader != null) {
      for (int x = 0; x < StatConstants.MAX_DISPLAY_STATS; x++) {
        final long value = leader.getStat(x);
        if (x == StatConstants.STAT_HIT || x == StatConstants.STAT_EVADE) {
          final double fmtVal = value / 100.0;
          StatisticsViewer.statisticsValues[x].setText(
              " " + StatConstants.STAT_NAMES[x] + ": " + fmtVal + "%  ");
        } else {
          StatisticsViewer.statisticsValues[x]
              .setText(" " + StatConstants.STAT_NAMES[x] + ": " + value + "  ");
        }
      }
      StatisticsViewer.statisticsFrame.pack();
    } else {
      CommonDialogs.showDialog("Nothing to display");
    }
  }

  private static void setUpGUI() {
    if (!StatisticsViewer.inited) {
      StatisticsViewer.statisticsFrame = MainWindow.getOutputFrame();
      StatisticsViewer.statisticsFrame.setTitle("Statistics");
      StatisticsViewer.statisticsPane = new JPanel();
      StatisticsViewer.statisticsPane.setLayout(new BorderLayout());
      StatisticsViewer.contentPane = new JPanel();
      StatisticsViewer.contentPane
          .setLayout(new GridLayout(StatConstants.MAX_STATS, 1));
      StatisticsViewer.buttonPane = new JPanel();
      StatisticsViewer.buttonPane.setLayout(new FlowLayout());
      StatisticsViewer.btnOK = new JButton("OK");
      StatisticsViewer.btnOK.addActionListener(e -> Game.showOutput());
      StatisticsViewer.statisticsValues = new JLabel[StatConstants.MAX_DISPLAY_STATS];
      for (int x = 0; x < StatConstants.MAX_DISPLAY_STATS; x++) {
        StatisticsViewer.statisticsValues[x] = new JLabel();
        StatisticsViewer.contentPane.add(StatisticsViewer.statisticsValues[x]);
      }
      StatisticsViewer.buttonPane.add(StatisticsViewer.btnOK);
      StatisticsViewer.statisticsPane.add(StatisticsViewer.contentPane,
          BorderLayout.CENTER);
      StatisticsViewer.statisticsPane.add(StatisticsViewer.buttonPane,
          BorderLayout.SOUTH);
      StatisticsViewer.statisticsFrame
          .attachContent(StatisticsViewer.statisticsPane);
      StatisticsViewer.inited = true;
    }
  }
}
