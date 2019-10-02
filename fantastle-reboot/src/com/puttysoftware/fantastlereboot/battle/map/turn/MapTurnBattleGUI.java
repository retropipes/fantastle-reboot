/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.battle.map.turn;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.DrawGrid;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.ai.AIRoutine;
import com.puttysoftware.fantastlereboot.assets.GameObjectImage;
import com.puttysoftware.fantastlereboot.battle.Battle;
import com.puttysoftware.fantastlereboot.battle.map.MapBattleDraw;
import com.puttysoftware.fantastlereboot.battle.map.MapBattleEffects;
import com.puttysoftware.fantastlereboot.battle.map.MapBattleViewingWindowManager;
import com.puttysoftware.fantastlereboot.loaders.ObjectImageLoader;
import com.puttysoftware.fantastlereboot.obsolete.maze2.MazeConstants;
import com.puttysoftware.fantastlereboot.obsolete.maze2.abc.AbstractMazeObject;
import com.puttysoftware.fantastlereboot.obsolete.maze2.objects.EmptyVoid;
import com.puttysoftware.images.BufferedImageIcon;

class MapTurnBattleGUI {
    // Fields
    private JFrame battleFrame;
    private MapBattleDraw battlePane;
    private JLabel messageLabel;
    private final MapBattleViewingWindowManager vwMgr;
    private final MapTurnBattleStats bs;
    private final MapBattleEffects be;
    private DrawGrid drawGrid;
    boolean eventHandlersOn;
    private JButton spell, steal, drain, item, end;
    private static final int MAX_TEXT = 1000;

    // Constructors
    MapTurnBattleGUI() {
        this.vwMgr = new MapBattleViewingWindowManager();
        this.bs = new MapTurnBattleStats();
        this.be = new MapBattleEffects();
        this.setUpGUI();
        this.eventHandlersOn = true;
    }

    // Methods
    JFrame getOutputFrame() {
        return this.battleFrame;
    }

    MapBattleViewingWindowManager getViewManager() {
        return this.vwMgr;
    }

    void clearStatusMessage() {
        this.messageLabel.setText(" ");
    }

    void setStatusMessage(final String msg) {
        if (this.messageLabel.getText().length() > MAX_TEXT) {
            this.clearStatusMessage();
        }
        if (!msg.isEmpty() && !msg.matches("\\s+")) {
            this.messageLabel.setText(msg);
        }
    }

    void showBattle() {
        this.battleFrame.setVisible(true);
        FantastleReboot.getBagOStuff().getMenuManager().attachMenus();
    }

    void hideBattle() {
        if (this.battleFrame != null) {
            this.battleFrame.setVisible(false);
        }
    }

    void redrawBattle(final MapTurnBattleDefinitions bd) {
        // Draw the battle, if it is visible
        if (this.battleFrame.isVisible()) {
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
                    try {
                        final AbstractMazeObject obj1 = bd.getBattleMaze()
                                .getCell(y, x, 0, MazeConstants.LAYER_GROUND);
                        final AbstractMazeObject obj2 = bd.getBattleMaze()
                                .getCell(y, x, 0, MazeConstants.LAYER_OBJECT);
                        String cacheName = generateCacheName(obj1, obj2);
                        final BufferedImageIcon icon1 = obj1.battleRenderHook();
                        final BufferedImageIcon icon2 = obj2.battleRenderHook();
                        this.drawGrid.setImageCell(ImageCompositor.composite(
                                cacheName, icon1, icon2), xFix, yFix);
                    } catch (final ArrayIndexOutOfBoundsException ae) {
                        final EmptyVoid ev = new EmptyVoid();
                        this.drawGrid.setImageCell(ev.battleRenderHook(), xFix,
                                yFix);
                    } catch (final NullPointerException np) {
                        final EmptyVoid ev = new EmptyVoid();
                        this.drawGrid.setImageCell(ev.battleRenderHook(), xFix,
                                yFix);
                    }
                }
            }
            this.battlePane.repaint();
            this.battleFrame.pack();
        }
    }

    void redrawOneBattleSquare(final MapTurnBattleDefinitions bd, final int x,
            final int y, final AbstractMazeObject obj3) {
        // Draw the battle, if it is visible
        if (this.battleFrame.isVisible()) {
            try {
                int xFix, yFix;
                final int xView = this.vwMgr.getViewingWindowLocationX();
                final int yView = this.vwMgr.getViewingWindowLocationY();
                xFix = y - xView;
                yFix = x - yView;
                final AbstractMazeObject obj1 = bd.getBattleMaze().getCell(y, x,
                        0, MazeConstants.LAYER_GROUND);
                final AbstractMazeObject obj2 = bd.getBattleMaze().getCell(y, x,
                        0, MazeConstants.LAYER_OBJECT);
                String cacheName = generateCacheName(obj1, obj2, obj3);
                final BufferedImageIcon icon1 = obj1.battleRenderHook();
                final BufferedImageIcon icon2 = obj2.battleRenderHook();
                final BufferedImageIcon icon3 = obj3.battleRenderHook();
                this.drawGrid.setImageCell(ImageCompositor.composite(cacheName,
                        icon1, icon2, icon3), xFix, yFix);
                this.battlePane.repaint();
            } catch (final ArrayIndexOutOfBoundsException ae) {
                // Do nothing
            } catch (final NullPointerException np) {
                // Do nothing
            }
            this.battleFrame.pack();
        }
    }

    private static String generateCacheName(
            final AbstractMazeObject... objects) {
        StringBuilder result = new StringBuilder();
        for (AbstractMazeObject object : objects) {
            result.append(object.getBattleBaseID());
            result.append("_");
        }
        result.append("cache");
        return result.toString();
    }

    void updateStatsAndEffects(final MapTurnBattleDefinitions bd) {
        this.bs.updateStats(bd.getActiveCharacter());
        this.be.updateEffects(bd.getActiveCharacter());
    }

    private void setUpGUI() {
        final EventHandler handler = new EventHandler();
        final Container borderPane = new Container();
        final Container buttonPane = new Container();
        borderPane.setLayout(new BorderLayout());
        this.messageLabel = new JLabel(" ");
        this.messageLabel.setOpaque(true);
        this.battleFrame = new JFrame("Battle");
        this.battleFrame.setContentPane(borderPane);
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
        this.spell.addActionListener(handler);
        this.steal.addActionListener(handler);
        this.drain.addActionListener(handler);
        this.item.addActionListener(handler);
        this.end.addActionListener(handler);
        int modKey;
        if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
            modKey = InputEvent.META_DOWN_MASK;
        } else {
            modKey = InputEvent.CTRL_DOWN_MASK;
        }
        this.spell.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, modKey), "Cast Spell");
        this.spell.getActionMap().put("Cast Spell", handler);
        this.steal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_T, modKey), "Steal");
        this.steal.getActionMap().put("Steal", handler);
        this.drain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_D, modKey), "Drain");
        this.drain.getActionMap().put("Drain", handler);
        this.item.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_I, modKey), "Use Item");
        this.item.getActionMap().put("Use Item", handler);
        this.end.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, modKey), "End Turn");
        this.end.getActionMap().put("End Turn", handler);
        this.battleFrame
                .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.battleFrame.setResizable(false);
        this.drawGrid = new DrawGrid(
                MapBattleViewingWindowManager.getViewingWindowSize());
        BufferedImageIcon darknessImage = ObjectImageLoader
                .load(GameObjectImage.DARKNESS);
        for (int x = 0; x < MapBattleViewingWindowManager
                .getViewingWindowSize(); x++) {
            for (int y = 0; y < MapBattleViewingWindowManager
                    .getViewingWindowSize(); y++) {
                this.drawGrid.setImageCell(darknessImage, x, y);
            }
        }
        this.battlePane = new MapBattleDraw(this.drawGrid);
        borderPane.add(this.battlePane, BorderLayout.CENTER);
        borderPane.add(buttonPane, BorderLayout.WEST);
        borderPane.add(this.messageLabel, BorderLayout.NORTH);
        borderPane.add(this.bs.getStatsPane(), BorderLayout.EAST);
        borderPane.add(this.be.getEffectsPane(), BorderLayout.SOUTH);
        this.battleFrame.addKeyListener(handler);
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
            if (!FantastleReboot.getBagOStuff().getPrefsManager().oneMove()) {
                if (e.isShiftDown()) {
                    this.handleArrows(e);
                } else {
                    this.handleMovement(e);
                }
            }
        }

        @Override
        public void keyReleased(final KeyEvent e) {
            if (FantastleReboot.getBagOStuff().getPrefsManager().oneMove()) {
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
                if (System.getProperty("os.name")
                        .equalsIgnoreCase("Mac OS X")) {
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
                                "Are you sure you want to attack yourself?",
                                "Battle");
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
                if (System.getProperty("os.name")
                        .equalsIgnoreCase("Mac OS X")) {
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
                                "Are you sure you want to attack yourself?",
                                "Battle");
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
