package com.puttysoftware.fantastlereboot.game;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.loaders.ImageConstants;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Player;
import com.puttysoftware.fantastlereboot.objects.temporary.Darkness;
import com.puttysoftware.fantastlereboot.objects.temporary.NoteObject;
import com.puttysoftware.images.BufferedImageIcon;

class GameDraw extends Thread {
  private final DrawGrid drawGrid;
  private final JPanel drawDestination;
  private boolean full;
  private int drawX;
  private int drawY;
  private FantastleObjectModel drawObj;
  private static final Nothing NOTHING = new Nothing();
  private static final OpenSpace OPEN = new OpenSpace();
  private static final Darkness DARK = new Darkness();
  private static final NoteObject NOTE = new NoteObject();
  private static final Player PLAYER = new Player();

  public GameDraw(final JPanel destination) {
    super();
    this.setName("Game Drawing Handler");
    this.setDaemon(true);
    this.drawGrid = new DrawGrid(Prefs.getViewingWindowSize());
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
    m.moveAllMonsters();
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
        inBounds = x >= 0 && x < Maze.getMaxRows() && y >= 0
            && y < Maze.getMaxColumns();
        if (inBounds) {
          if (visible) {
            final FantastleObjectModel obj1 = m.getCell(y, x,
                m.getPlayerLocationZ(), Layers.GROUND);
            final FantastleObjectModel obj2 = m.getCell(y, x,
                m.getPlayerLocationZ(), Layers.OBJECT);
            final BufferedImageIcon img1 = obj1.getGameImage();
            final BufferedImageIcon img2 = obj2.getGameImage();
            FantastleObjectModel obj3 = GameDraw.OPEN;
            BufferedImageIcon img3 = obj3.getGameImage();
            FantastleObjectModel obj4 = GameDraw.OPEN;
            BufferedImageIcon img4 = obj4.getGameImage();
            final boolean playerSquare = u == y && v == x;
            final boolean noteSquare = m.hasNote(x, y, z);
            if (playerSquare) {
              obj3 = GameDraw.PLAYER;
              img3 = obj3.getGameImage();
            }
            if (noteSquare) {
              obj4 = GameDraw.NOTE;
              img4 = obj4.getGameImage();
            }
            final String cacheName = GameDraw.generateCacheName(obj1, obj2,
                obj3, obj4);
            this.drawGrid.setImageCell(
                ImageCompositor.composite(cacheName, img1, img2, img3, img4),
                xFix, yFix);
          } else {
            this.drawGrid.setImageCell(GameDraw.DARK.getImage(), xFix, yFix);
          }
        } else {
          this.drawGrid.setImageCell(GameDraw.NOTHING.getGameImage(), xFix,
              yFix);
        }
      }
    }
    final Graphics g = this.drawDestination.getGraphics();
    if (g != null) {
      final int gSize = ImageConstants.SIZE;
      final int vSize = Prefs.getViewingWindowSize();
      for (x = 0; x < vSize; x++) {
        for (y = 0; y < vSize; y++) {
          g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize,
              gSize, gSize, null);
        }
      }
      g.dispose();
    }
  }

  private void drawOne() {
    final Maze m = MazeManager.getMaze();
    m.moveAllMonsters();
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
    inBounds = x >= 0 && x < Maze.getMaxRows() && y >= 0
        && y < Maze.getMaxColumns();
    if (inBounds) {
      if (visible) {
        final FantastleObjectModel obj1 = m.getCell(y, x,
            m.getPlayerLocationZ(), Layers.GROUND);
        final FantastleObjectModel obj2 = m.getCell(y, x,
            m.getPlayerLocationZ(), Layers.OBJECT);
        final BufferedImageIcon img1 = obj1.getGameImage();
        final BufferedImageIcon img2 = obj2.getGameImage();
        FantastleObjectModel obj3 = GameDraw.OPEN;
        BufferedImageIcon img3 = obj3.getGameImage();
        FantastleObjectModel obj4 = GameDraw.OPEN;
        BufferedImageIcon img4 = obj4.getGameImage();
        final BufferedImageIcon img5 = obj5.getGameImage();
        final boolean playerSquare = u == y && v == x;
        final boolean noteSquare = m.hasNote(x, y, z);
        if (playerSquare) {
          obj3 = GameDraw.PLAYER;
          img3 = obj3.getGameImage();
        }
        if (noteSquare) {
          obj4 = GameDraw.NOTE;
          img4 = obj4.getGameImage();
        }
        final String cacheName = GameDraw.generateCacheName(obj1, obj2, obj3,
            obj4, obj5);
        this.drawGrid.setImageCell(
            ImageCompositor.composite(cacheName, img1, img2, img3, img4, img5),
            xFix, yFix);
      } else {
        this.drawGrid.setImageCell(GameDraw.DARK.getImage(), xFix, yFix);
      }
    } else {
      this.drawGrid.setImageCell(GameDraw.NOTHING.getGameImage(), xFix, yFix);
    }
    final Graphics g = this.drawDestination.getGraphics();
    if (g != null) {
      final int gSize = ImageConstants.SIZE;
      final int vSize = Prefs.getViewingWindowSize();
      for (x = 0; x < vSize; x++) {
        for (y = 0; y < vSize; y++) {
          g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize,
              gSize, gSize, null);
        }
      }
      g.dispose();
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
}
