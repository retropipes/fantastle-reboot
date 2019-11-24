/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.effects.EffectManager;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.files.MazeFileManager;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.loaders.ImageConstants;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.maze.MonsterLocationManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Player;
import com.puttysoftware.fantastlereboot.objects.temporary.Darkness;
import com.puttysoftware.fantastlereboot.objects.temporary.NoteObject;
import com.puttysoftware.images.BufferedImageIcon;

class GameGUI {
  // Fields
  private static MainWindow outputFrame;
  private static JPanel borderPane;
  private static JLabel messageLabel;
  private static DrawGrid drawGrid;
  private static JPanel outputPane;
  private static final EventHandler handler = new EventHandler();
  private static boolean knm;
  private static boolean deferredRedraw = false;
  private static boolean eventFlag = true;
  private static final Nothing NOTHING = new Nothing();
  private static final OpenSpace OPEN = new OpenSpace();
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
    GameGUI.borderPane.setEnabled(true);
    GameGUI.eventFlag = true;
  }

  public static void disableEvents() {
    GameGUI.borderPane.setEnabled(false);
    GameGUI.eventFlag = false;
  }

  static void viewingWindowSizeChanged() {
    GameGUI.setUpGUI();
    GameGUI.updateGameGUI();
    GameGUI.deferredRedraw = true;
  }

  public static void showOutput() {
    GameGUI.outputFrame = MainWindow.getOutputFrame();
    GameGUI.outputFrame.setTitle("Game");
    GameGUI.outputFrame.attachContent(GameGUI.borderPane);
    GameGUI.outputFrame.addKeyListener(GameGUI.handler);
    GameGUI.outputFrame.addWindowListener(GameGUI.handler);
    if (GameGUI.deferredRedraw) {
      GameGUI.deferredRedraw = false;
      GameGUI.redrawMaze();
    }
    GameGUI.updateStats();
  }

  public static void hideOutput() {
    GameGUI.outputFrame.removeWindowListener(GameGUI.handler);
    GameGUI.outputFrame.removeKeyListener(GameGUI.handler);
  }

  public static void setStatusMessage(final String msg) {
    GameGUI.messageLabel.setText(msg);
  }

  private static void resetBorderPane() {
    GameGUI.borderPane.removeAll();
    GameGUI.borderPane.add(GameGUI.outputPane, BorderLayout.CENTER);
    GameGUI.borderPane.add(GameGUI.messageLabel, BorderLayout.NORTH);
    GameGUI.borderPane.add(StatGUI.getStatsPane(), BorderLayout.EAST);
    GameGUI.borderPane.add(EffectManager.getEffectMessageJPanel(),
        BorderLayout.SOUTH);
  }

  public static void redrawMaze() {
    // Draw the maze
    final Maze m = MazeManager.getMaze();
    MonsterLocationManager.moveAllMonsters(m);
    int x, y, u, v;
    int xFix, yFix;
    boolean visible;
    boolean inBounds;
    u = m.getPlayerLocationX();
    v = m.getPlayerLocationY();
    final int viewX = GameView.getViewingWindowLocationX();
    final int viewY = GameView.getViewingWindowLocationY();
    final int viewLRX = GameView.getLowerRightViewingWindowLocationX();
    final int viewLRY = GameView.getLowerRightViewingWindowLocationY();
    final int z = m.getPlayerLocationZ();
    for (x = viewX; x <= viewLRX; x++) {
      for (y = viewY; y <= viewLRY; y++) {
        xFix = x - viewX;
        yFix = y - viewY;
        visible = m.isSquareVisible(u, v, y, x);
        inBounds = x >= 0 && x < m.getRows() && y >= 0 && y < m.getColumns();
        if (inBounds) {
          if (visible) {
            final FantastleObjectModel obj1 = m.getCell(y, x,
                m.getPlayerLocationZ(), Layers.GROUND);
            final FantastleObjectModel obj2 = m.getCell(y, x,
                m.getPlayerLocationZ(), Layers.OBJECT);
            final BufferedImageIcon img1 = obj1.getGameImage();
            final BufferedImageIcon img2 = obj2.getGameImage();
            FantastleObjectModel obj3 = GameGUI.OPEN;
            BufferedImageIcon img3 = obj3.getGameImage();
            FantastleObjectModel obj4 = GameGUI.OPEN;
            BufferedImageIcon img4 = obj4.getGameImage();
            final boolean playerSquare = u == y && v == x;
            final boolean noteSquare = m.hasNote(x, y, z);
            if (playerSquare) {
              obj3 = GameGUI.PLAYER;
              img3 = obj3.getGameImage();
            }
            if (noteSquare) {
              obj4 = GameGUI.NOTE;
              img4 = obj4.getGameImage();
            }
            final String cacheName = GameGUI.generateCacheName(obj1, obj2, obj3,
                obj4);
            GameGUI.drawGrid.setImageCell(
                ImageCompositor.composite(cacheName, img1, img2, img3, img4),
                xFix, yFix);
          } else {
            GameGUI.drawGrid.setImageCell(GameGUI.DARK.getImage(), xFix, yFix);
          }
        } else {
          GameGUI.drawGrid.setImageCell(GameGUI.NOTHING.getGameImage(), xFix,
              yFix);
        }
      }
    }
    final Graphics g = GameGUI.outputPane.getGraphics();
    final int gSize = ImageConstants.SIZE;
    final int vSize = Prefs.getViewingWindowSize();
    for (x = 0; x < vSize; x++) {
      for (y = 0; y < vSize; y++) {
        g.drawImage(GameGUI.drawGrid.getImageCell(y, x), x * gSize, y * gSize,
            gSize, gSize, null);
      }
    }
    g.dispose();
    if (GameGUI.knm) {
      GameGUI.knm = false;
    } else {
      GameGUI.setStatusMessage(" ");
    }
    GameGUI.outputFrame.pack();
  }

  public static void redrawOneSquare(final int inX, final int inY,
      final FantastleObjectModel obj5) {
    // Draw the maze
    if (obj5 != null) {
      final Maze m = MazeManager.getMaze();
      MonsterLocationManager.moveAllMonsters(m);
      int x, y, u, v;
      int xFix, yFix;
      boolean visible;
      boolean inBounds;
      x = inX;
      y = inY;
      u = m.getPlayerLocationX();
      v = m.getPlayerLocationY();
      final int z = m.getPlayerLocationZ();
      xFix = x - GameView.getViewingWindowLocationX();
      yFix = y - GameView.getViewingWindowLocationY();
      visible = MazeManager.getMaze().isSquareVisible(u, v, y, x);
      inBounds = x >= 0 && x < m.getRows() && y >= 0 && y < m.getColumns();
      if (inBounds) {
        if (visible) {
          final FantastleObjectModel obj1 = m.getCell(y, x,
              m.getPlayerLocationZ(), Layers.GROUND);
          final FantastleObjectModel obj2 = m.getCell(y, x,
              m.getPlayerLocationZ(), Layers.OBJECT);
          final BufferedImageIcon img1 = obj1.getGameImage();
          final BufferedImageIcon img2 = obj2.getGameImage();
          FantastleObjectModel obj3 = GameGUI.OPEN;
          BufferedImageIcon img3 = obj3.getGameImage();
          FantastleObjectModel obj4 = GameGUI.OPEN;
          BufferedImageIcon img4 = obj4.getGameImage();
          final BufferedImageIcon img5 = obj5.getGameImage();
          final boolean playerSquare = u == y && v == x;
          final boolean noteSquare = m.hasNote(x, y, z);
          if (playerSquare) {
            obj3 = GameGUI.PLAYER;
            img3 = obj3.getGameImage();
          }
          if (noteSquare) {
            obj4 = GameGUI.NOTE;
            img4 = obj4.getGameImage();
          }
          final String cacheName = GameGUI.generateCacheName(obj1, obj2, obj3,
              obj4, obj5);
          GameGUI.drawGrid.setImageCell(ImageCompositor.composite(cacheName,
              img1, img2, img3, img4, img5), xFix, yFix);
        } else {
          GameGUI.drawGrid.setImageCell(GameGUI.DARK.getImage(), xFix, yFix);
        }
      } else {
        GameGUI.drawGrid.setImageCell(GameGUI.NOTHING.getGameImage(), xFix,
            yFix);
      }
      final Graphics g = GameGUI.outputPane.getGraphics();
      final int gSize = ImageConstants.SIZE;
      final int vSize = Prefs.getViewingWindowSize();
      for (x = 0; x < vSize; x++) {
        for (y = 0; y < vSize; y++) {
          g.drawImage(GameGUI.drawGrid.getImageCell(y, x), x * gSize, y * gSize,
              gSize, gSize, null);
        }
      }
      g.dispose();
      if (GameGUI.knm) {
        GameGUI.knm = false;
      } else {
        GameGUI.setStatusMessage(" ");
      }
      GameGUI.outputPane.repaint();
      GameGUI.outputFrame.pack();
    }
  }

  private static String
      generateCacheName(final FantastleObjectModel... objects) {
    final StringBuilder result = new StringBuilder();
    for (final FantastleObjectModel object : objects) {
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

  static void updateGameGUI() {
    GameGUI.resetBorderPane();
    StatGUI.updateImages();
    StatGUI.updateStats();
  }

  private static void setUpGUI() {
    GameGUI.borderPane = new JPanel();
    GameGUI.borderPane.setLayout(new BorderLayout());
    GameGUI.messageLabel = new JLabel(" ");
    GameGUI.messageLabel.setOpaque(true);
    GameGUI.drawGrid = new DrawGrid(Prefs.getViewingWindowSize());
    GameGUI.outputPane = new JPanel();
    final int vSize = Prefs.getViewingWindowSize();
    final int gSize = ImageConstants.SIZE;
    GameGUI.outputPane
        .setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
  }

  private static class EventHandler implements KeyListener, WindowListener {
    EventHandler() {
      // Do nothing
    }

    @Override
    public void keyPressed(final KeyEvent e) {
      if (GameGUI.eventFlag) {
        if (!Prefs.oneMove()) {
          EventHandler.handleMovement(e);
        }
      }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
      if (GameGUI.eventFlag) {
        if (Prefs.oneMove()) {
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
        boolean success = false;
        int status = 0;
        if (FileStateManager.getDirty()) {
          status = FileStateManager.showSaveDialog();
          if (status == JOptionPane.YES_OPTION) {
            success = MazeFileManager.saveGame();
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
