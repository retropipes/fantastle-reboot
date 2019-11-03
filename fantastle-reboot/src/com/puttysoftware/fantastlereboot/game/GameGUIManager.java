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

class GameGUIManager {
  // Fields
  private JFrame outputFrame;
  private Container borderPane;
  private JLabel messageLabel;
  private GameViewingWindowManager vwMgr = null;
  private final StatGUI sg;
  private DrawGrid drawGrid;
  private GameDraw outputPane;
  private boolean knm;
  private boolean deferredRedraw;
  boolean eventFlag;
  private static Darkness DARK = new Darkness();
  private static NoteObject NOTE = new NoteObject();
  private static Player PLAYER = new Player();

  // Constructors
  public GameGUIManager() {
    this.deferredRedraw = false;
    this.eventFlag = true;
    this.sg = new StatGUI();
  }

  // Methods
  public void updateStats() {
    this.sg.updateStats();
  }

  public void enableEvents() {
    this.outputFrame.setEnabled(true);
    this.eventFlag = true;
  }

  public void disableEvents() {
    this.outputFrame.setEnabled(false);
    this.eventFlag = false;
  }

  void initViewManager() {
    if (this.vwMgr == null) {
      this.vwMgr = FantastleReboot.getBagOStuff().getGameManager()
          .getViewManager();
      this.setUpGUI();
    }
  }

  void viewingWindowSizeChanged(final EffectManager em) {
    this.setUpGUI();
    this.updateGameGUI(em);
    this.deferredRedraw = true;
  }

  public JFrame getOutputFrame() {
    return this.outputFrame;
  }

  public void showOutput() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    if (!this.outputFrame.isVisible()) {
      app.getMenuManager().setGameMenus();
      this.outputFrame.setVisible(true);
      app.getMenuManager().attachMenus();
      if (this.deferredRedraw) {
        this.deferredRedraw = false;
        this.redrawMaze();
      }
      this.updateStats();
    }
  }

  public void hideOutput() {
    if (this.outputFrame != null) {
      this.outputFrame.setVisible(false);
    }
  }

  public void setStatusMessage(final String msg) {
    this.messageLabel.setText(msg);
  }

  private void resetBorderPane(final EffectManager em) {
    this.borderPane.removeAll();
    this.borderPane.add(this.outputPane, BorderLayout.CENTER);
    this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
    this.borderPane.add(this.sg.getStatsPane(), BorderLayout.EAST);
    this.borderPane.add(em.getEffectMessageContainer(), BorderLayout.SOUTH);
  }

  public void redrawMaze() {
    // Draw the maze, if it is visible
    if (this.outputFrame.isVisible()) {
      final BagOStuff app = FantastleReboot.getBagOStuff();
      final Maze m = app.getMazeManager().getMaze();
      int x, y, u, v;
      int xFix, yFix;
      boolean visible;
      u = m.getPlayerLocationX();
      v = m.getPlayerLocationY();
      final int z = m.getPlayerLocationZ();
      final FantastleObjectModel ev = new Nothing();
      for (x = this.vwMgr.getViewingWindowLocationX(); x <= this.vwMgr
          .getLowerRightViewingWindowLocationX(); x++) {
        for (y = this.vwMgr.getViewingWindowLocationY(); y <= this.vwMgr
            .getLowerRightViewingWindowLocationY(); y++) {
          xFix = x - this.vwMgr.getViewingWindowLocationX();
          yFix = y - this.vwMgr.getViewingWindowLocationY();
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
              if (playerSquare) {
                obj3 = PLAYER;
                img3 = obj3.getGameImage();
              }
              if (noteSquare) {
                obj4 = NOTE;
                img4 = obj4.getGameImage();
              }
              if (monsterSquare && app.getPrefsManager().monstersVisible()) {
                obj5 = MonsterObjectFactory.createMonster();
                img5 = obj5.getGameImage();
              }
              String cacheName = generateCacheName(obj1, obj2, obj3, obj4,
                  obj5);
              this.drawGrid.setImageCell(ImageCompositor.composite(cacheName,
                  img1, img2, img3, img4, img5), xFix, yFix);
            } else {
              this.drawGrid.setImageCell(DARK.getImage(), xFix, yFix);
            }
          } catch (final ArrayIndexOutOfBoundsException ae) {
            this.drawGrid.setImageCell(ev.getGameImage(), xFix, yFix);
          }
        }
      }
      if (this.knm) {
        this.knm = false;
      } else {
        this.setStatusMessage(" ");
      }
      this.outputPane.repaint();
      this.outputFrame.pack();
      this.showOutput();
    }
  }

  public void redrawOneSquare(final int inX, final int inY,
      final FantastleObjectModel obj6) {
    // Draw the maze, if it is visible
    if (this.outputFrame.isVisible() && obj6 != null) {
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
      xFix = x - this.vwMgr.getViewingWindowLocationX();
      yFix = y - this.vwMgr.getViewingWindowLocationY();
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
          if (playerSquare) {
            obj3 = PLAYER;
            img3 = obj3.getGameImage();
          }
          if (noteSquare) {
            obj4 = NOTE;
            img4 = obj4.getGameImage();
          }
          if (monsterSquare && app.getPrefsManager().monstersVisible()) {
            obj5 = MonsterObjectFactory.createMonster();
            img5 = obj5.getGameImage();
          }
          String cacheName = generateCacheName(obj1, obj2, obj3, obj4, obj5,
              obj6);
          this.drawGrid.setImageCell(ImageCompositor.composite(cacheName, img1,
              img2, img3, img4, img5, img6), xFix, yFix);
        } else {
          this.drawGrid.setImageCell(DARK.getImage(), xFix, yFix);
        }
      } catch (final ArrayIndexOutOfBoundsException ae) {
        this.drawGrid.setImageCell(ev.getGameImage(), xFix, yFix);
      }
      if (this.knm) {
        this.knm = false;
      } else {
        this.setStatusMessage(" ");
      }
      this.outputPane.repaint();
      this.outputFrame.pack();
      this.showOutput();
    }
  }

  private static String
      generateCacheName(final FantastleObjectModel... objects) {
    StringBuilder result = new StringBuilder();
    for (FantastleObjectModel object : objects) {
      result.append(object.getUniqueID());
      result.append("_");
    }
    result.append("cache");
    return result.toString();
  }

  public void keepNextMessage() {
    this.knm = true;
  }

  void updateGameGUI(final EffectManager em) {
    this.resetBorderPane(em);
    this.sg.updateImages();
    this.sg.updateStats();
  }

  private void setUpGUI() {
    final EventHandler handler = new EventHandler();
    this.borderPane = new Container();
    this.borderPane.setLayout(new BorderLayout());
    this.messageLabel = new JLabel(" ");
    this.messageLabel.setOpaque(true);
    this.outputFrame = new JFrame("FantastleReboot");
    this.drawGrid = new DrawGrid(FantastleReboot.getBagOStuff()
        .getPrefsManager().getViewingWindowSize());
    this.outputPane = new GameDraw(this.drawGrid);
    this.outputFrame.setContentPane(this.borderPane);
    this.outputFrame
        .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.outputFrame.setResizable(false);
    this.outputFrame.addKeyListener(handler);
    this.outputFrame.addWindowListener(handler);
  }

  private class EventHandler implements KeyListener, WindowListener {
    EventHandler() {
      // Do nothing
    }

    @Override
    public void keyPressed(final KeyEvent e) {
      if (GameGUIManager.this.eventFlag) {
        if (!FantastleReboot.getBagOStuff().getPrefsManager().oneMove()) {
          this.handleMovement(e);
        }
      }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
      if (GameGUIManager.this.eventFlag) {
        if (FantastleReboot.getBagOStuff().getPrefsManager().oneMove()) {
          this.handleMovement(e);
        }
      }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
      // Do nothing
    }

    public void handleMovement(final KeyEvent e) {
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
