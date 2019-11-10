/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.DrawGrid;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.MainWindow;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.utilities.ImageConstants;

class GameGUI {
  // Fields
  private static JFrame outputFrame;
  private static Container borderPane;
  private static JLabel messageLabel;
  private static DrawGrid drawGrid;
  private static JPanel outputPane;
  private static GameDraw drawingThread;
  private static boolean knm;
  private static boolean deferredRedraw = false;
  private static boolean eventFlag = true;

  // Constructors
  private GameGUI() {
    // Do nothing
  }

  // Methods
  public static void updateStats() {
    StatGUI.updateStats();
  }

  public static void enableEvents() {
    GameGUI.outputFrame.setEnabled(true);
    GameGUI.eventFlag = true;
  }

  public static void disableEvents() {
    GameGUI.outputFrame.setEnabled(false);
    GameGUI.eventFlag = false;
  }

  static void viewingWindowSizeChanged() {
    GameGUI.setUpGUI();
    GameGUI.updateGameGUI();
    GameGUI.deferredRedraw = true;
  }

  public static JFrame getOutputFrame() {
    return GameGUI.outputFrame;
  }

  public static void showOutput() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    if (!GameGUI.outputFrame.isVisible()) {
      app.getMenuManager().setGameMenus();
      GameGUI.outputFrame.setVisible(true);
      app.getMenuManager().attachMenus();
      if (GameGUI.deferredRedraw) {
        GameGUI.deferredRedraw = false;
        GameGUI.redrawMaze();
      }
      GameGUI.updateStats();
    }
  }

  public static void hideOutput() {
    if (GameGUI.outputFrame != null) {
      GameGUI.outputFrame.setVisible(false);
    }
  }

  public static void setStatusMessage(final String msg) {
    GameGUI.messageLabel.setText(msg);
  }

  private static void resetBorderPane() {
    GameGUI.borderPane.removeAll();
    GameGUI.borderPane.add(GameGUI.outputPane, BorderLayout.CENTER);
    GameGUI.borderPane.add(GameGUI.messageLabel, BorderLayout.NORTH);
    GameGUI.borderPane.add(StatGUI.getStatsPane(), BorderLayout.EAST);
    GameGUI.borderPane.add(EffectManager.getEffectMessageContainer(),
        BorderLayout.SOUTH);
  }

  public static void redrawMaze() {
    // Draw the maze, if it is visible
    if (GameGUI.outputFrame.isVisible()) {
      GameGUI.drawingThread.requestDraw();
      if (GameGUI.knm) {
        GameGUI.knm = false;
      } else {
        GameGUI.setStatusMessage(" ");
      }
      GameGUI.outputFrame.pack();
      GameGUI.showOutput();
    }
  }

  public static void redrawOneSquare(final int inX, final int inY,
      final FantastleObjectModel obj5) {
    // Draw the maze, if it is visible
    if (GameGUI.outputFrame.isVisible() && obj5 != null) {
      GameGUI.drawingThread.requestDrawOne(inX, inY, obj5);
      if (GameGUI.knm) {
        GameGUI.knm = false;
      } else {
        GameGUI.setStatusMessage(" ");
      }
      GameGUI.outputPane.repaint();
      GameGUI.outputFrame.pack();
      GameGUI.showOutput();
    }
  }

  public static void keepNextMessage() {
    GameGUI.knm = true;
  }

  static void updateGameGUI() {
    GameGUI.resetBorderPane();
    StatGUI.updateImages();
    StatGUI.updateStats();
  }

  private static void setUpGUI() {
    final EventHandler handler = new EventHandler();
    GameGUI.borderPane = new Container();
    GameGUI.borderPane.setLayout(new BorderLayout());
    GameGUI.messageLabel = new JLabel(" ");
    GameGUI.messageLabel.setOpaque(true);
    GameGUI.outputFrame = MainWindow.getOutputFrame();
    GameGUI.outputFrame.setTitle("Game");
    GameGUI.drawGrid = new DrawGrid(PreferencesManager.getViewingWindowSize());
    GameGUI.outputPane = new JPanel();
    final int vSize = PreferencesManager.getViewingWindowSize();
    final int gSize = ImageConstants.SIZE;
    GameGUI.outputPane
        .setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
    GameGUI.drawingThread = new GameDraw(GameGUI.drawGrid, GameGUI.outputPane);
    GameGUI.drawingThread.start();
    GameGUI.outputFrame.setContentPane(GameGUI.borderPane);
    GameGUI.outputFrame
        .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    GameGUI.outputFrame.setResizable(false);
    GameGUI.outputFrame.addKeyListener(handler);
    GameGUI.outputFrame.addWindowListener(handler);
  }

  private static class EventHandler implements KeyListener, WindowListener {
    EventHandler() {
      // Do nothing
    }

    @Override
    public void keyPressed(final KeyEvent e) {
      if (GameGUI.eventFlag) {
        if (!PreferencesManager.oneMove()) {
          EventHandler.handleMovement(e);
        }
      }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
      if (GameGUI.eventFlag) {
        if (PreferencesManager.oneMove()) {
          EventHandler.handleMovement(e);
        }
      }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
      // Do nothing
    }

    public static void handleMovement(final KeyEvent e) {
      try {
        final int keyCode = e.getKeyCode();
        switch (keyCode) {
        case KeyEvent.VK_LEFT:
          if (e.isShiftDown()) {
            Game.updatePositionRelative(-1, -1, 0);
          } else {
            Game.updatePositionRelative(-1, 0, 0);
          }
          break;
        case KeyEvent.VK_DOWN:
          if (e.isShiftDown()) {
            Game.updatePositionRelative(-1, 1, 0);
          } else {
            Game.updatePositionRelative(0, 1, 0);
          }
          break;
        case KeyEvent.VK_RIGHT:
          if (e.isShiftDown()) {
            Game.updatePositionRelative(1, 1, 0);
          } else {
            Game.updatePositionRelative(1, 0, 0);
          }
          break;
        case KeyEvent.VK_UP:
          if (e.isShiftDown()) {
            Game.updatePositionRelative(1, -1, 0);
          } else {
            Game.updatePositionRelative(0, -1, 0);
          }
          break;
        case KeyEvent.VK_ENTER:
          if (e.isShiftDown()) {
            Game.updatePositionRelative(0, 0, 0);
          }
          break;
        case KeyEvent.VK_NUMPAD7:
        case KeyEvent.VK_Q:
          Game.updatePositionRelative(-1, -1, 0);
          break;
        case KeyEvent.VK_NUMPAD8:
        case KeyEvent.VK_W:
          Game.updatePositionRelative(0, -1, 0);
          break;
        case KeyEvent.VK_NUMPAD9:
        case KeyEvent.VK_E:
          Game.updatePositionRelative(1, -1, 0);
          break;
        case KeyEvent.VK_NUMPAD4:
        case KeyEvent.VK_A:
          Game.updatePositionRelative(-1, 0, 0);
          break;
        case KeyEvent.VK_NUMPAD5:
        case KeyEvent.VK_S:
          Game.updatePositionRelative(0, 0, 0);
          break;
        case KeyEvent.VK_NUMPAD6:
        case KeyEvent.VK_D:
          Game.updatePositionRelative(1, 0, 0);
          break;
        case KeyEvent.VK_NUMPAD1:
        case KeyEvent.VK_Z:
          Game.updatePositionRelative(-1, 1, 0);
          break;
        case KeyEvent.VK_NUMPAD2:
        case KeyEvent.VK_X:
          Game.updatePositionRelative(0, 1, 0);
          break;
        case KeyEvent.VK_NUMPAD3:
        case KeyEvent.VK_C:
          Game.updatePositionRelative(1, 1, 0);
          break;
        default:
          break;
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    // Handle windows
    @Override
    public void windowActivated(final WindowEvent we) {
      // Do nothing
    }

    @Override
    public void windowClosed(final WindowEvent we) {
      // Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent we) {
      try {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        boolean success = false;
        int status = 0;
        if (app.getMazeManager().getDirty()) {
          app.getMazeManager();
          status = MazeManager.showSaveDialog();
          if (status == JOptionPane.YES_OPTION) {
            app.getMazeManager();
            success = MazeManager.saveGame();
            if (success) {
              Game.exitGame();
            }
          } else if (status == JOptionPane.NO_OPTION) {
            Game.exitGame();
          }
        } else {
          Game.exitGame();
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }

    @Override
    public void windowDeactivated(final WindowEvent we) {
      // Do nothing
    }

    @Override
    public void windowDeiconified(final WindowEvent we) {
      // Do nothing
    }

    @Override
    public void windowIconified(final WindowEvent we) {
      // Do nothing
    }

    @Override
    public void windowOpened(final WindowEvent we) {
      // Do nothing
    }
  }
}
