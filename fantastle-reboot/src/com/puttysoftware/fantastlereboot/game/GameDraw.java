package com.puttysoftware.fantastlereboot.game;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
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

class GameDraw extends Thread {
  private DrawGrid drawGrid;
  private JPanel drawDestination;
  private boolean full;
  private int drawX;
  private int drawY;
  private FantastleObjectModel drawObj;
  private static final Nothing NOTHING = new Nothing();
  private static final OpenSpace OPEN = new OpenSpace();
  private static final Darkness DARK = new Darkness();
  private static final NoteObject NOTE = new NoteObject();
  private static final Player PLAYER = new Player();

  public GameDraw(final DrawGrid grid, final JPanel destination) {
    super();
    this.setName("Game Drawing Handler");
    this.setDaemon(true);
    this.drawGrid = grid;
    this.drawDestination = destination;
    this.drawX = -1;
    this.drawY = -1;
    this.drawObj = null;
    this.full = true;
  }

  @Override
  public void run() {
    try {
      while (true) {
        this.waitForWork();
        if (this.full) {
          this.draw();
        } else {
          this.drawOne();
        }
      }
    } catch (final Throwable t) {
      FantastleReboot.logError(t);
    }
  }

  private synchronized void waitForWork() throws InterruptedException {
    this.wait();
  }

  public synchronized void requestDraw() {
    this.drawX = -1;
    this.drawY = -1;
    this.drawObj = null;
    this.full = true;
    this.notify();
  }

  public synchronized void requestDrawOne(final int x, final int y,
      final FantastleObjectModel obj) {
    this.drawX = x;
    this.drawY = y;
    this.drawObj = obj;
    this.full = false;
    this.notify();
  }

  private void draw() {
    final Maze m = MazeManager.getMaze();
    MonsterLocationManager.moveAllMonsters(m);
    int x, y, u, v;
    int xFix, yFix;
    boolean visible;
    boolean inBounds;
    u = m.getPlayerLocationX();
    v = m.getPlayerLocationY();
    final int z = m.getPlayerLocationZ();
    for (x = GameView.getViewingWindowLocationX(); x <= GameView
        .getLowerRightViewingWindowLocationX(); x++) {
      for (y = GameView.getViewingWindowLocationY(); y <= GameView
          .getLowerRightViewingWindowLocationY(); y++) {
        xFix = x - GameView.getViewingWindowLocationX();
        yFix = y - GameView.getViewingWindowLocationY();
        visible = m.isSquareVisible(u, v, y, x);
        inBounds = (x >= 0 && x < Maze.getMaxRows() && y >= 0
            && y < Maze.getMaxColumns());
        if (inBounds) {
          if (visible) {
            final FantastleObjectModel obj1 = m.getCell(y, x,
                m.getPlayerLocationZ(), Layers.GROUND);
            final FantastleObjectModel obj2 = m.getCell(y, x,
                m.getPlayerLocationZ(), Layers.OBJECT);
            final BufferedImageIcon img1 = obj1.getGameImage();
            final BufferedImageIcon img2 = obj2.getGameImage();
            FantastleObjectModel obj3 = OPEN;
            BufferedImageIcon img3 = obj3.getGameImage();
            FantastleObjectModel obj4 = OPEN;
            BufferedImageIcon img4 = obj4.getGameImage();
            boolean playerSquare = (u == y && v == x);
            boolean noteSquare = m.hasNote(x, y, z);
            if (playerSquare) {
              obj3 = PLAYER;
              img3 = obj3.getGameImage();
            }
            if (noteSquare) {
              obj4 = NOTE;
              img4 = obj4.getGameImage();
            }
            String cacheName = generateCacheName(obj1, obj2, obj3, obj4);
            this.drawGrid.setImageCell(
                ImageCompositor.composite(cacheName, img1, img2, img3, img4),
                xFix, yFix);
          } else {
            this.drawGrid.setImageCell(DARK.getImage(), xFix, yFix);
          }
        } else {
          this.drawGrid.setImageCell(NOTHING.getGameImage(), xFix, yFix);
        }
      }
    }
    final Graphics g = this.drawDestination.getGraphics();
    final int gSize = ImageConstants.SIZE;
    final int vSize = PreferencesManager.getViewingWindowSize();
    for (x = 0; x < vSize; x++) {
      for (y = 0; y < vSize; y++) {
        g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize,
            gSize, gSize, null);
      }
    }
    g.dispose();
    // Check for battle
    int px = m.getPlayerLocationX();
    int py = m.getPlayerLocationY();
    MonsterLocationManager.checkForBattle(px, py);
  }

  private void drawOne() {
    final Maze m = MazeManager.getMaze();
    MonsterLocationManager.moveAllMonsters(m);
    int x, y, u, v;
    int xFix, yFix;
    boolean visible;
    boolean inBounds;
    x = this.drawX;
    y = this.drawY;
    u = m.getPlayerLocationX();
    v = m.getPlayerLocationY();
    final int z = m.getPlayerLocationZ();
    final FantastleObjectModel obj5 = this.drawObj;
    xFix = x - GameView.getViewingWindowLocationX();
    yFix = y - GameView.getViewingWindowLocationY();
    visible = MazeManager.getMaze().isSquareVisible(u, v, y, x);
    inBounds = (x >= 0 && x < Maze.getMaxRows() && y >= 0
        && y < Maze.getMaxColumns());
    if (inBounds) {
      if (visible) {
        final FantastleObjectModel obj1 = m.getCell(y, x,
            m.getPlayerLocationZ(), Layers.GROUND);
        final FantastleObjectModel obj2 = m.getCell(y, x,
            m.getPlayerLocationZ(), Layers.OBJECT);
        final BufferedImageIcon img1 = obj1.getGameImage();
        final BufferedImageIcon img2 = obj2.getGameImage();
        FantastleObjectModel obj3 = OPEN;
        BufferedImageIcon img3 = obj3.getGameImage();
        FantastleObjectModel obj4 = OPEN;
        BufferedImageIcon img4 = obj4.getGameImage();
        BufferedImageIcon img5 = obj5.getGameImage();
        boolean playerSquare = (u == y && v == x);
        boolean noteSquare = m.hasNote(x, y, z);
        if (playerSquare) {
          obj3 = PLAYER;
          img3 = obj3.getGameImage();
        }
        if (noteSquare) {
          obj4 = NOTE;
          img4 = obj4.getGameImage();
        }
        String cacheName = generateCacheName(obj1, obj2, obj3, obj4, obj5);
        this.drawGrid.setImageCell(
            ImageCompositor.composite(cacheName, img1, img2, img3, img4, img5),
            xFix, yFix);
      } else {
        this.drawGrid.setImageCell(DARK.getImage(), xFix, yFix);
      }
    } else {
      this.drawGrid.setImageCell(NOTHING.getGameImage(), xFix, yFix);
    }
    final Graphics g = this.drawDestination.getGraphics();
    final int gSize = ImageConstants.SIZE;
    final int vSize = PreferencesManager.getViewingWindowSize();
    for (x = 0; x < vSize; x++) {
      for (y = 0; y < vSize; y++) {
        g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize,
            gSize, gSize, null);
      }
    }
    g.dispose();
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
}
