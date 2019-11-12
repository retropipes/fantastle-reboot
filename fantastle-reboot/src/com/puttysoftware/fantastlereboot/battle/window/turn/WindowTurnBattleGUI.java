package com.puttysoftware.fantastlereboot.battle.window.turn;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.ai.window.AbstractWindowAIRoutine;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.battle.BattleResults;
import com.puttysoftware.fantastlereboot.battle.BossRewards;
import com.puttysoftware.images.BufferedImageIcon;

public class WindowTurnBattleGUI {
  // Fields
  private MainWindow battleFrame;
  private final JLabel iconLabel;
  private final Container holderPane;
  private final JTextArea messageArea;
  private final JButton attack, flee, spell, steal, drain, item, done;

  // Constructor
  public WindowTurnBattleGUI() {
    // Initialize GUI
    Container iconPane, messagePane, buttonPane;
    this.holderPane = new Container();
    iconPane = new Container();
    messagePane = new Container();
    buttonPane = new Container();
    this.iconLabel = new JLabel("");
    this.messageArea = new JTextArea();
    this.messageArea.setOpaque(true);
    this.messageArea.setEditable(false);
    this.attack = new JButton("Attack");
    this.flee = new JButton("Flee");
    this.spell = new JButton("Cast Spell");
    this.steal = new JButton("Steal");
    this.drain = new JButton("Drain");
    this.item = new JButton("Use Item");
    this.done = new JButton("Continue");
    iconPane.setLayout(new FlowLayout());
    messagePane.setLayout(new FlowLayout());
    buttonPane.setLayout(new GridLayout(2, 4));
    this.holderPane.setLayout(new BorderLayout());
    iconPane.add(this.iconLabel);
    messagePane.add(this.messageArea);
    buttonPane.add(this.attack);
    buttonPane.add(this.flee);
    buttonPane.add(this.spell);
    buttonPane.add(this.steal);
    buttonPane.add(this.drain);
    buttonPane.add(this.item);
    buttonPane.add(this.done);
    this.holderPane.add(iconPane, BorderLayout.WEST);
    this.holderPane.add(messagePane, BorderLayout.CENTER);
    this.holderPane.add(buttonPane, BorderLayout.SOUTH);
    // Initialize Event Handlers
    final BattleEventHandler handler = new BattleEventHandler();
    this.attack.addActionListener(handler);
    this.flee.addActionListener(handler);
    this.spell.addActionListener(handler);
    this.steal.addActionListener(handler);
    this.drain.addActionListener(handler);
    this.item.addActionListener(handler);
    this.done.addActionListener(handler);
    this.attack.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "Attack");
    this.attack.getActionMap().put("Attack", handler);
    this.flee.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "Flee");
    this.flee.getActionMap().put("Flee", handler);
    this.spell.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "Cast Spell");
    this.spell.getActionMap().put("Cast Spell", handler);
    this.steal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "Steal");
    this.steal.getActionMap().put("Steal", handler);
    this.drain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "Drain");
    this.drain.getActionMap().put("Drain", handler);
    this.item.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "Use Item");
    this.item.getActionMap().put("Use Item", handler);
  }

  void initBattle(final BufferedImageIcon enemyIcon) {
    this.iconLabel.setIcon(enemyIcon);
    this.attack.setVisible(true);
    this.flee.setVisible(true);
    this.spell.setVisible(true);
    this.steal.setVisible(true);
    this.drain.setVisible(true);
    this.item.setVisible(true);
    this.done.setVisible(false);
    this.attack.setEnabled(true);
    this.flee.setEnabled(true);
    this.spell.setEnabled(true);
    this.steal.setEnabled(true);
    this.drain.setEnabled(true);
    this.item.setEnabled(true);
    this.done.setEnabled(false);
    this.showBattle();
  }

  final void showBattle() {
    this.battleFrame = MainWindow.getOutputFrame();
    this.battleFrame.setTitle("Battle");
    this.battleFrame.setContentPane(this.holderPane);
    this.battleFrame.setDefaultButton(this.done);
    this.battleFrame.pack();
  }

  final void hideBattle() {
    this.battleFrame.setDefaultButton(null);
  }

  final void pack() {
    this.battleFrame.pack();
  }

  final void clearMessageArea() {
    this.messageArea.setText("");
  }

  public final void setStatusMessage(final String s) {
    this.messageArea.setText(this.messageArea.getText() + s + "\n");
  }

  final void stripExtraNewLine() {
    final String currText = this.messageArea.getText();
    this.messageArea.setText(currText.substring(0, currText.length() - 1));
  }

  void doResultCleanup() {
    // Cleanup
    this.attack.setVisible(false);
    this.flee.setVisible(false);
    this.spell.setVisible(false);
    this.steal.setVisible(false);
    this.drain.setVisible(false);
    this.item.setVisible(false);
    this.done.setVisible(true);
    this.attack.setEnabled(false);
    this.flee.setEnabled(false);
    this.spell.setEnabled(false);
    this.steal.setEnabled(false);
    this.drain.setEnabled(false);
    this.item.setEnabled(false);
    this.done.setEnabled(true);
  }

  void doResultFinalCleanup(final boolean flag) {
    // Final Cleanup
    this.stripExtraNewLine();
    this.battleFrame.pack();
    if (flag) {
      BossRewards.doRewards();
    }
  }

  private class BattleEventHandler extends AbstractAction {
    private static final long serialVersionUID = 20239525230523523L;

    public BattleEventHandler() {
      // Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      try {
        boolean success = true;
        final String cmd = e.getActionCommand();
        final WindowTurnBattleGUI wbg = WindowTurnBattleGUI.this;
        final Battle b = FantastleReboot.getBagOStuff().getBattle();
        // Clear Message Area
        wbg.clearMessageArea();
        // Display Beginning Stats
        wbg.setStatusMessage("*** Beginning of Round ***");
        b.displayBattleStats();
        wbg.setStatusMessage("*** Beginning of Round ***\n");
        // Do Player Actions
        if (cmd.equals("Attack") || cmd.equals("a")) {
          // Attack
          success = b.doPlayerActions(AbstractWindowAIRoutine.ACTION_ATTACK);
        } else if (cmd.equals("Flee") || cmd.equals("f")) {
          // Try to Flee
          success = b.doPlayerActions(AbstractWindowAIRoutine.ACTION_FLEE);
          if (success) {
            // Strip Extra Newline Character
            wbg.stripExtraNewLine();
            // Pack Battle Frame
            wbg.battleFrame.pack();
            // Get out of here
            b.doResult();
            return;
          } else {
            success = b.doPlayerActions(AbstractWindowAIRoutine.ACTION_ATTACK);
          }
        } else if (cmd.equals("Continue")) {
          // Battle Done
          b.battleDone();
          return;
        } else if (cmd.equals("Cast Spell") || cmd.equals("c")) {
          // Cast Spell
          success = b
              .doPlayerActions(AbstractWindowAIRoutine.ACTION_CAST_SPELL);
          if (!success) {
            // Strip Two Extra Newline Characters
            wbg.stripExtraNewLine();
            wbg.stripExtraNewLine();
            // Pack Battle Frame
            wbg.battleFrame.pack();
            // Get out of here
            return;
          }
        } else if (cmd.equals("Steal") || cmd.equals("s")) {
          // Steal Money
          success = b.doPlayerActions(AbstractWindowAIRoutine.ACTION_STEAL);
        } else if (cmd.equals("Drain") || cmd.equals("d")) {
          // Drain Enemy
          success = b.doPlayerActions(AbstractWindowAIRoutine.ACTION_DRAIN);
        } else if (cmd.equals("Use Item") || cmd.equals("i")) {
          // Use Item
          success = b.doPlayerActions(AbstractWindowAIRoutine.ACTION_USE_ITEM);
          if (!success) {
            // Strip Two Extra Newline Characters
            wbg.stripExtraNewLine();
            wbg.stripExtraNewLine();
            // Pack Battle Frame
            wbg.battleFrame.pack();
            // Get out of here
            return;
          }
        }
        // Maintain Player Effects
        b.maintainEffects(true);
        // Check result
        BattleResults bResult = b.getResult();
        if (bResult != BattleResults.IN_PROGRESS) {
          b.setResult(bResult);
          b.doResult();
          return;
        }
        // Do Enemy Actions
        b.executeNextAIAction();
        // Maintain Enemy Effects
        b.maintainEffects(false);
        // Display Active Effects
        b.displayActiveEffects();
        // Display End Stats
        wbg.setStatusMessage("\n*** End of Round ***");
        b.displayBattleStats();
        wbg.setStatusMessage("*** End of Round ***");
        // Check Result
        bResult = b.getResult();
        if (bResult != BattleResults.IN_PROGRESS) {
          b.setResult(bResult);
          b.doResult();
        } else {
          // Strip Extra Newline Character
          wbg.stripExtraNewLine();
          // Pack Battle Frame
          wbg.battleFrame.pack();
        }
      } catch (final Throwable t) {
        FantastleReboot.logError(t);
      }
    }
  }
}
