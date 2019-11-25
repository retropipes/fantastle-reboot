/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.ai.AIRoutine;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.loaders.ObjectImageLoader;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.images.BufferedImageIcon;

class MapTurnBattleGUI {
  // Fields
  private final MainWindow battleFrame;
  private MapBattleDraw battlePane;
  private JLabel messageLabel;
  private final MapBattleViewingWindowManager vwMgr;
  private final MapTurnBattleStats bs;
  private final MapBattleEffects be;
  private final EventHandler handler = new EventHandler();
  private JPanel borderPane;
  private DrawGrid drawGrid;
  boolean eventHandlersOn;
  private JButton spell, steal, drain, item, end;
  private static final int MAX_TEXT = 1000;

  // Constructors
  MapTurnBattleGUI() {
    this.battleFrame = MainWindow.getOutputFrame();
    this.vwMgr = new MapBattleViewingWindowManager();
    this.bs = new MapTurnBattleStats();
    this.be = new MapBattleEffects();
    this.setUpGUI();
    this.eventHandlersOn = true;
  }

  // Methods
  MapBattleViewingWindowManager getViewManager() {
    return this.vwMgr;
  }

  void clearStatusMessage() {
    this.messageLabel.setText(" ");
  }

  void setStatusMessage(final String msg) {
    if (this.messageLabel.getText().length() > MapTurnBattleGUI.MAX_TEXT) {
      this.clearStatusMessage();
    }
    if (!msg.isEmpty() && !msg.matches("\\s+")) {
      this.messageLabel.setText(msg);
    }
  }

  void showBattle() {
    this.battleFrame.setTitle("Battle");
    this.battleFrame.attachContent(this.borderPane);
    this.battleFrame.addKeyListener(this.handler);
  }

  void hideBattle() {
    this.battleFrame.removeKeyListener(this.handler);
  }

  void redrawBattle(final MapBattleDefinitions mbd) {
    // Draw the battle
    int x, y;
    int xFix, yFix;
    final int xView = this.vwMgr.getViewingWindowLocationX();
    final int yView = this.vwMgr.getViewingWindowLocationY();
    final int xlView = this.vwMgr.getLowerRightViewingWindowLocationX();
    final int ylView = this.vwMgr.getLowerRightViewingWindowLocationY();
    for (x = xView; x <= xlView; x++) {
      for (y = yView; y <= ylView; y++) {
        xFix = x - xView;
        yFix = y - yView;
        if (mbd.getBattleMaze().cellRangeCheck(y, x, 0)) {
          final FantastleObjectModel obj1 = mbd.getBattleMaze().getCell(y, x, 0,
              Layers.GROUND);
          final FantastleObjectModel obj2 = mbd.getBattleMaze().getCell(y, x, 0,
              Layers.OBJECT);
          final String cacheName = MapTurnBattleGUI.generateCacheName(obj1,
              obj2);
          final BufferedImageIcon icon1 = obj1.getBattleImage();
          final BufferedImageIcon icon2 = obj2.getBattleImage();
          this.drawGrid.setImageCell(
              ImageCompositor.composite(cacheName, icon1, icon2), xFix, yFix);
        } else {
          final Nothing ev = new Nothing();
          this.drawGrid.setImageCell(ev.getBattleImage(), xFix, yFix);
        }
      }
    }
    this.battlePane.repaint();
    this.battleFrame.pack();
  }

  void redrawOneBattleSquare(final MapBattleDefinitions mbd, final int x,
      final int y, final FantastleObjectModel obj3) {
    // Draw the battle
    if (mbd.getBattleMaze().cellRangeCheck(y, x, 0)) {
      int xFix, yFix;
      final int xView = this.vwMgr.getViewingWindowLocationX();
      final int yView = this.vwMgr.getViewingWindowLocationY();
      xFix = y - xView;
      yFix = x - yView;
      final FantastleObjectModel obj1 = mbd.getBattleMaze().getCell(y, x, 0,
          Layers.GROUND);
      final FantastleObjectModel obj2 = mbd.getBattleMaze().getCell(y, x, 0,
          Layers.OBJECT);
      final String cacheName = MapTurnBattleGUI.generateCacheName(obj1, obj2,
          obj3);
      final BufferedImageIcon icon1 = obj1.getBattleImage();
      final BufferedImageIcon icon2 = obj2.getBattleImage();
      final BufferedImageIcon icon3 = obj3.getBattleImage();
      this.drawGrid.setImageCell(
          ImageCompositor.composite(cacheName, icon1, icon2, icon3), xFix,
          yFix);
      this.battlePane.repaint();
    }
    this.battleFrame.pack();
  }

  private static String
      generateCacheName(final FantastleObjectModel... objects) {
    final StringBuilder result = new StringBuilder();
    for (final FantastleObjectModel object : objects) {
      result.append(object.getName());
      result.append("_");
    }
    result.append("cache");
    return result.toString();
  }

  void updateStatsAndEffects(final MapBattleDefinitions mbd) {
    this.bs.updateStats(mbd.getActiveCharacter());
    this.be.updateEffects(mbd.getActiveCharacter());
  }

  private void setUpGUI() {
    this.borderPane = new JPanel();
    final JPanel buttonPane = new JPanel();
    this.borderPane.setLayout(new BorderLayout());
    this.messageLabel = new JLabel(" ");
    this.messageLabel.setOpaque(true);
    this.spell = new JButton("Cast Spell");
    this.steal = new JButton("Steal");
    this.drain = new JButton("Drain");
    this.item = new JButton("Use Item");
    this.end = new JButton("End Turn");
    buttonPane.setLayout(new GridLayout(5, 1));
    buttonPane.add(this.spell);
    buttonPane.add(this.steal);
    buttonPane.add(this.drain);
    buttonPane.add(this.item);
    buttonPane.add(this.end);
    this.spell.setFocusable(false);
    this.steal.setFocusable(false);
    this.drain.setFocusable(false);
    this.item.setFocusable(false);
    this.end.setFocusable(false);
    this.spell.addActionListener(this.handler);
    this.steal.addActionListener(this.handler);
    this.drain.addActionListener(this.handler);
    this.item.addActionListener(this.handler);
    this.end.addActionListener(this.handler);
    int modKey;
    if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
      modKey = InputEvent.META_DOWN_MASK;
    } else {
      modKey = InputEvent.CTRL_DOWN_MASK;
    }
    this.spell.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_C, modKey), "Cast Spell");
    this.spell.getActionMap().put("Cast Spell", this.handler);
    this.steal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_T, modKey), "Steal");
    this.steal.getActionMap().put("Steal", this.handler);
    this.drain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_D, modKey), "Drain");
    this.drain.getActionMap().put("Drain", this.handler);
    this.item.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_I, modKey), "Use Item");
    this.item.getActionMap().put("Use Item", this.handler);
    this.end.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, modKey), "End Turn");
    this.end.getActionMap().put("End Turn", this.handler);
    this.drawGrid = new DrawGrid(
        MapBattleViewingWindowManager.getViewingWindowSize());
    final BufferedImageIcon darknessImage = ObjectImageLoader
        .load(ObjectImageIndex.DARKNESS);
    for (int x = 0; x < MapBattleViewingWindowManager
        .getViewingWindowSize(); x++) {
      for (int y = 0; y < MapBattleViewingWindowManager
          .getViewingWindowSize(); y++) {
        this.drawGrid.setImageCell(darknessImage, x, y);
      }
    }
    this.battlePane = new MapBattleDraw(this.drawGrid);
    this.borderPane.add(this.battlePane, BorderLayout.CENTER);
    this.borderPane.add(buttonPane, BorderLayout.WEST);
    this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
    this.borderPane.add(this.bs.getStatsPane(), BorderLayout.EAST);
    this.borderPane.add(this.be.getEffectsPane(), BorderLayout.SOUTH);
  }

  void turnEventHandlersOff() {
    this.eventHandlersOn = false;
    this.spell.setEnabled(false);
    this.steal.setEnabled(false);
    this.drain.setEnabled(false);
    this.item.setEnabled(false);
    this.end.setEnabled(false);
  }

  void turnEventHandlersOn() {
    this.eventHandlersOn = true;
    this.spell.setEnabled(true);
    this.steal.setEnabled(true);
    this.drain.setEnabled(true);
    this.item.setEnabled(true);
    this.end.setEnabled(true);
  }

  boolean areEventHandlersOn() {
    return this.eventHandlersOn;
  }

  private class EventHandler extends AbstractAction implements KeyListener {
    private static final long serialVersionUID = 20239525230523524L;

    public EventHandler() {
      // Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      try {
        final String cmd = e.getActionCommand();
        final Battle b = FantastleReboot.getBagOStuff().getBattle();
        // Do Player Actions
        if (cmd.equals("Cast Spell") || cmd.equals("c")) {
          // Cast Spell
          b.doPlayerActions(AIRoutine.ACTION_CAST_SPELL);
        } else if (cmd.equals("Steal") || cmd.equals("t")) {
          // Steal Money
          b.doPlayerActions(AIRoutine.ACTION_STEAL);
        } else if (cmd.equals("Drain") || cmd.equals("d")) {
          // Drain Enemy
          b.doPlayerActions(AIRoutine.ACTION_DRAIN);
        } else if (cmd.equals("Use Item") || cmd.equals("i")) {
          // Use Item
          b.doPlayerActions(AIRoutine.ACTION_USE_ITEM);
        } else if (cmd.equals("End Turn") || cmd.equals("e")) {
          // End Turn
          b.endTurn();
        }
      } catch (final Throwable t) {
        FantastleReboot.logError(t);
      }
    }

    @Override
    public void keyPressed(final KeyEvent e) {
      if (!Prefs.oneMove()) {
        if (e.isShiftDown()) {
          this.handleArrows(e);
        } else {
          this.handleMovement(e);
        }
      }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
      if (Prefs.oneMove()) {
        if (e.isShiftDown()) {
          this.handleArrows(e);
        } else {
          this.handleMovement(e);
        }
      }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
      // Do nothing
    }

    private void handleMovement(final KeyEvent e) {
      try {
        if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
          if (e.isMetaDown()) {
            return;
          }
        } else {
          if (e.isControlDown()) {
            return;
          }
        }
        final Battle bl = FantastleReboot.getBagOStuff().getBattle();
        final MapTurnBattleGUI bg = MapTurnBattleGUI.this;
        if (bg.eventHandlersOn) {
          final int keyCode = e.getKeyCode();
          switch (keyCode) {
          case KeyEvent.VK_NUMPAD4:
          case KeyEvent.VK_LEFT:
          case KeyEvent.VK_A:
            bl.updatePosition(-1, 0);
            break;
          case KeyEvent.VK_NUMPAD2:
          case KeyEvent.VK_DOWN:
          case KeyEvent.VK_X:
            bl.updatePosition(0, 1);
            break;
          case KeyEvent.VK_NUMPAD6:
          case KeyEvent.VK_RIGHT:
          case KeyEvent.VK_D:
            bl.updatePosition(1, 0);
            break;
          case KeyEvent.VK_NUMPAD8:
          case KeyEvent.VK_UP:
          case KeyEvent.VK_W:
            bl.updatePosition(0, -1);
            break;
          case KeyEvent.VK_NUMPAD7:
          case KeyEvent.VK_Q:
            bl.updatePosition(-1, -1);
            break;
          case KeyEvent.VK_NUMPAD9:
          case KeyEvent.VK_E:
            bl.updatePosition(1, -1);
            break;
          case KeyEvent.VK_NUMPAD3:
          case KeyEvent.VK_C:
            bl.updatePosition(1, 1);
            break;
          case KeyEvent.VK_NUMPAD1:
          case KeyEvent.VK_Z:
            bl.updatePosition(-1, 1);
            break;
          case KeyEvent.VK_NUMPAD5:
          case KeyEvent.VK_S:
            // Confirm before attacking self
            final int res = CommonDialogs.showConfirmDialog(
                "Are you sure you want to attack yourself?", "Battle");
            if (res == JOptionPane.YES_OPTION) {
              bl.updatePosition(0, 0);
            }
            break;
          default:
            break;
          }
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    private void handleArrows(final KeyEvent e) {
      try {
        if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
          if (e.isMetaDown()) {
            return;
          }
        } else {
          if (e.isControlDown()) {
            return;
          }
        }
        final Battle bl = FantastleReboot.getBagOStuff().getBattle();
        final MapTurnBattleGUI bg = MapTurnBattleGUI.this;
        if (bg.eventHandlersOn) {
          final int keyCode = e.getKeyCode();
          switch (keyCode) {
          case KeyEvent.VK_NUMPAD4:
          case KeyEvent.VK_LEFT:
          case KeyEvent.VK_A:
            bl.fireArrow(-1, 0);
            break;
          case KeyEvent.VK_NUMPAD2:
          case KeyEvent.VK_DOWN:
          case KeyEvent.VK_X:
            bl.fireArrow(0, 1);
            break;
          case KeyEvent.VK_NUMPAD6:
          case KeyEvent.VK_RIGHT:
          case KeyEvent.VK_D:
            bl.fireArrow(1, 0);
            break;
          case KeyEvent.VK_NUMPAD8:
          case KeyEvent.VK_UP:
          case KeyEvent.VK_W:
            bl.fireArrow(0, -1);
            break;
          case KeyEvent.VK_NUMPAD7:
          case KeyEvent.VK_Q:
            bl.fireArrow(-1, -1);
            break;
          case KeyEvent.VK_NUMPAD9:
          case KeyEvent.VK_E:
            bl.fireArrow(1, -1);
            break;
          case KeyEvent.VK_NUMPAD3:
          case KeyEvent.VK_C:
            bl.fireArrow(1, 1);
            break;
          case KeyEvent.VK_NUMPAD1:
          case KeyEvent.VK_Z:
            bl.fireArrow(-1, 1);
            break;
          case KeyEvent.VK_NUMPAD5:
          case KeyEvent.VK_S:
            // Confirm before attacking self
            final int res = CommonDialogs.showConfirmDialog(
                "Are you sure you want to attack yourself?", "Battle");
            if (res == JOptionPane.YES_OPTION) {
              bl.fireArrow(0, 0);
            }
            break;
          default:
            break;
          }
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }
  }
}
