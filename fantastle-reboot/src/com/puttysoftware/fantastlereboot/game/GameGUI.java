/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.DrawGrid;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.Player;
import com.puttysoftware.fantastlereboot.objects.temporary.Darkness;
import com.puttysoftware.fantastlereboot.objects.temporary.MonsterObjectFactory;
import com.puttysoftware.fantastlereboot.objects.temporary.NoteObject;
import com.puttysoftware.images.BufferedImageIcon;

class GameGUI {
  // Fields
  private static JFrame outputFrame;
  private static Container borderPane;
  private static JLabel messageLabel;
  private static DrawGrid drawGrid;
  private static GameDraw outputPane;
  private static boolean knm;
  private static boolean deferredRedraw = false;
  private static boolean eventFlag = true;
  private static final Darkness DARK = new Darkness();
  private static final NoteObject NOTE = new NoteObject();
  private static final Player PLAYER = new Player();

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

  static void viewingWindowSizeChanged(final EffectManager em) {
    GameGUI.setUpGUI();
    GameGUI.updateGameGUI(em);
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

  private static void resetBorderPane(final EffectManager em) {
    GameGUI.borderPane.removeAll();
    GameGUI.borderPane.add(GameGUI.outputPane, BorderLayout.CENTER);
    GameGUI.borderPane.add(GameGUI.messageLabel, BorderLayout.NORTH);
    GameGUI.borderPane.add(StatGUI.getStatsPane(), BorderLayout.EAST);
    GameGUI.borderPane.add(em.getEffectMessageContainer(), BorderLayout.SOUTH);
  }

  public static void redrawMaze() {
    // Draw the maze, if it is visible
    if (GameGUI.outputFrame.isVisible()) {
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final Maze m = app.getMazeManager().getMaze();
      int x, y, u, v;
      int xFix, yFix;
      boolean visible;
      u = m.getPlayerLocationX();
      v = m.getPlayerLocationY();
      final int z = m.getPlayerLocationZ();
      final FantastleObjectModel ev = new Nothing();
      for (x = GameView
          .getViewingWindowLocationX(); x <= GameView
              .getLowerRightViewingWindowLocationX(); x++) {
        for (y = GameView
            .getViewingWindowLocationY(); y <= GameView
                .getLowerRightViewingWindowLocationY(); y++) {
          xFix = x - GameView.getViewingWindowLocationX();
          yFix = y - GameView.getViewingWindowLocationY();
          visible = app.getMazeManager().getMaze().isSquareVisible(u, v, y, x);
          try {
            if (visible) {
              final FantastleObjectModel obj1 = m.getCell(y, x,
                  m.getPlayerLocationZ(), Layers.GROUND);
              final FantastleObjectModel obj2 = m.getCell(y, x,
                  m.getPlayerLocationZ(), Layers.OBJECT);
              final BufferedImageIcon img1 = obj1.getGameImage();
              final BufferedImageIcon img2 = obj2.getGameImage();
              FantastleObjectModel obj3 = null;
              BufferedImageIcon img3 = null;
              FantastleObjectModel obj4 = null;
              BufferedImageIcon img4 = null;
              FantastleObjectModel obj5 = null;
              BufferedImageIcon img5 = null;
              boolean playerSquare = (u == y && v == x);
              boolean noteSquare = m.hasNote(x, y, z);
              boolean monsterSquare = m.hasMonster(xFix, y, z);
              if (monsterSquare && PreferencesManager.monstersVisible()) {
                obj3 = MonsterObjectFactory.createMonster();
                img3 = obj3.getGameImage();
              }
              if (playerSquare) {
                obj4 = PLAYER;
                img4 = obj4.getGameImage();
              }
              if (noteSquare) {
                obj5 = NOTE;
                img5 = obj5.getGameImage();
              }
              String cacheName = generateCacheName(obj1, obj2, obj3, obj4,
                  obj5);
              GameGUI.drawGrid.setImageCell(ImageCompositor.composite(cacheName,
                  img1, img2, img3, img4, img5), xFix, yFix);
            } else {
              GameGUI.drawGrid.setImageCell(DARK.getImage(), xFix, yFix);
            }
          } catch (final ArrayIndexOutOfBoundsException ae) {
            GameGUI.drawGrid.setImageCell(ev.getGameImage(), xFix, yFix);
          }
        }
      }
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

  public static void redrawOneSquare(final int inX, final int inY,
      final FantastleObjectModel obj6) {
    // Draw the maze, if it is visible
    if (GameGUI.outputFrame.isVisible() && obj6 != null) {
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final Maze m = app.getMazeManager().getMaze();
      final int z = m.getPlayerLocationZ();
      int x, y, u, v;
      x = inX;
      y = inY;
      int xFix, yFix;
      boolean visible;
      u = m.getPlayerLocationX();
      v = m.getPlayerLocationY();
      final FantastleObjectModel ev = new Nothing();
      xFix = x - GameView.getViewingWindowLocationX();
      yFix = y - GameView.getViewingWindowLocationY();
      visible = app.getMazeManager().getMaze().isSquareVisible(u, v, y, x);
      try {
        if (visible) {
          final FantastleObjectModel obj1 = m.getCell(y, x,
              m.getPlayerLocationZ(), Layers.GROUND);
          final FantastleObjectModel obj2 = m.getCell(y, x,
              m.getPlayerLocationZ(), Layers.OBJECT);
          final BufferedImageIcon img1 = obj1.getGameImage();
          final BufferedImageIcon img2 = obj2.getGameImage();
          FantastleObjectModel obj3 = null;
          BufferedImageIcon img3 = null;
          FantastleObjectModel obj4 = null;
          BufferedImageIcon img4 = null;
          FantastleObjectModel obj5 = null;
          BufferedImageIcon img5 = null;
          BufferedImageIcon img6 = obj6.getGameImage();
          boolean playerSquare = (u == y && v == x);
          boolean noteSquare = m.hasNote(x, y, z);
          boolean monsterSquare = m.hasMonster(xFix, y, z);
          if (monsterSquare && PreferencesManager.monstersVisible()) {
            obj3 = MonsterObjectFactory.createMonster();
            img3 = obj3.getGameImage();
          }
          if (playerSquare) {
            obj4 = PLAYER;
            img4 = obj4.getGameImage();
          }
          if (noteSquare) {
            obj5 = NOTE;
            img5 = obj5.getGameImage();
          }
          String cacheName = generateCacheName(obj1, obj2, obj3, obj4, obj5,
              obj6);
          GameGUI.drawGrid.setImageCell(ImageCompositor.composite(cacheName,
              img1, img2, img3, img4, img5, img6), xFix, yFix);
        } else {
          GameGUI.drawGrid.setImageCell(DARK.getImage(), xFix, yFix);
        }
      } catch (final ArrayIndexOutOfBoundsException ae) {
        GameGUI.drawGrid.setImageCell(ev.getGameImage(), xFix, yFix);
      }
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

  private static String
      generateCacheName(final FantastleObjectModel... objects) {
    StringBuilder result = new StringBuilder();
    for (FantastleObjectModel object : objects) {
      if (object != null) {
        result.append(object.getUniqueID());
        result.append("_");
      }
    }
    result.append("cache");
    return result.toString();
  }

  public static void keepNextMessage() {
    GameGUI.knm = true;
  }

  static void updateGameGUI(final EffectManager em) {
    GameGUI.resetBorderPane(em);
    StatGUI.updateImages();
    StatGUI.updateStats();
  }

  private static void setUpGUI() {
    final EventHandler handler = new EventHandler();
    GameGUI.borderPane = new Container();
    GameGUI.borderPane.setLayout(new BorderLayout());
    GameGUI.messageLabel = new JLabel(" ");
    GameGUI.messageLabel.setOpaque(true);
    GameGUI.outputFrame = new JFrame("FantastleReboot");
    GameGUI.drawGrid = new DrawGrid(PreferencesManager.getViewingWindowSize());
    GameGUI.outputPane = new GameDraw(GameGUI.drawGrid);
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
        final GameLogicManager glm = FantastleReboot.getBagOStuff()
            .getGameManager();
        final int keyCode = e.getKeyCode();
        switch (keyCode) {
        case KeyEvent.VK_LEFT:
          if (e.isShiftDown()) {
            glm.updatePositionRelative(-1, -1, 0);
          } else {
            glm.updatePositionRelative(-1, 0, 0);
          }
          break;
        case KeyEvent.VK_DOWN:
          if (e.isShiftDown()) {
            glm.updatePositionRelative(-1, 1, 0);
          } else {
            glm.updatePositionRelative(0, 1, 0);
          }
          break;
        case KeyEvent.VK_RIGHT:
          if (e.isShiftDown()) {
            glm.updatePositionRelative(1, 1, 0);
          } else {
            glm.updatePositionRelative(1, 0, 0);
          }
          break;
        case KeyEvent.VK_UP:
          if (e.isShiftDown()) {
            glm.updatePositionRelative(1, -1, 0);
          } else {
            glm.updatePositionRelative(0, -1, 0);
          }
          break;
        case KeyEvent.VK_ENTER:
          if (e.isShiftDown()) {
            glm.updatePositionRelative(0, 0, 0);
          }
          break;
        case KeyEvent.VK_NUMPAD7:
        case KeyEvent.VK_Q:
          glm.updatePositionRelative(-1, -1, 0);
          break;
        case KeyEvent.VK_NUMPAD8:
        case KeyEvent.VK_W:
          glm.updatePositionRelative(0, -1, 0);
          break;
        case KeyEvent.VK_NUMPAD9:
        case KeyEvent.VK_E:
          glm.updatePositionRelative(1, -1, 0);
          break;
        case KeyEvent.VK_NUMPAD4:
        case KeyEvent.VK_A:
          glm.updatePositionRelative(-1, 0, 0);
          break;
        case KeyEvent.VK_NUMPAD5:
        case KeyEvent.VK_S:
          glm.updatePositionRelative(0, 0, 0);
          break;
        case KeyEvent.VK_NUMPAD6:
        case KeyEvent.VK_D:
          glm.updatePositionRelative(1, 0, 0);
          break;
        case KeyEvent.VK_NUMPAD1:
        case KeyEvent.VK_Z:
          glm.updatePositionRelative(-1, 1, 0);
          break;
        case KeyEvent.VK_NUMPAD2:
        case KeyEvent.VK_X:
          glm.updatePositionRelative(0, 1, 0);
          break;
        case KeyEvent.VK_NUMPAD3:
        case KeyEvent.VK_C:
          glm.updatePositionRelative(1, 1, 0);
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
              app.getGameManager().exitGame();
            }
          } else if (status == JOptionPane.NO_OPTION) {
            app.getGameManager().exitGame();
          }
        } else {
          app.getGameManager().exitGame();
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
