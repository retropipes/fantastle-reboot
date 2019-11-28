package com.puttysoftware.fantastlereboot.game;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.loaders.ImageConstants;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Player;
import com.puttysoftware.fantastlereboot.objects.temporary.Darkness;
import com.puttysoftware.fantastlereboot.objects.temporary.NoteObject;
import com.puttysoftware.fantastlereboot.world.World;
import com.puttysoftware.fantastlereboot.world.WorldManager;
import com.puttysoftware.images.BufferedImageIcon;

class GameCanvas extends JPanel {
  private static final long serialVersionUID = 1L;
  private final DrawGrid drawGrid;
  private static final Nothing NOTHING = new Nothing();
  private static final OpenSpace OPEN = new OpenSpace();
  private static final Darkness DARK = new Darkness();
  private static final NoteObject NOTE = new NoteObject();
  private static final Player PLAYER = new Player();

  public GameCanvas() {
    super();
    this.drawGrid = new DrawGrid(Prefs.getViewingWindowSize());
  }

  @Override
  public void paint(final Graphics g) {
    final World m = WorldManager.getWorld();
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
        inBounds = x >= 0 && x < World.getMaxRows() && y >= 0
            && y < World.getMaxColumns();
        if (inBounds) {
          if (visible) {
            final FantastleObjectModel obj1 = m.getCell(y, x,
                m.getPlayerLocationZ(), Layers.GROUND);
            final FantastleObjectModel obj2 = m.getCell(y, x,
                m.getPlayerLocationZ(), Layers.OBJECT);
            final BufferedImageIcon img1 = obj1.getGameImage();
            final BufferedImageIcon img2 = obj2.getGameImage();
            FantastleObjectModel obj3 = GameCanvas.OPEN;
            BufferedImageIcon img3 = obj3.getGameImage();
            FantastleObjectModel obj4 = GameCanvas.OPEN;
            BufferedImageIcon img4 = obj4.getGameImage();
            final boolean playerSquare = u == y && v == x;
            final boolean noteSquare = m.hasNote(x, y, z);
            if (playerSquare) {
              obj3 = GameCanvas.PLAYER;
              img3 = obj3.getGameImage();
            }
            if (noteSquare) {
              obj4 = GameCanvas.NOTE;
              img4 = obj4.getGameImage();
            }
            final String cacheName = GameCanvas.generateCacheName(obj1, obj2,
                obj3, obj4);
            this.drawGrid.setImageCell(
                ImageCompositor.composite(cacheName, img1, img2, img3, img4),
                xFix, yFix);
          } else {
            this.drawGrid.setImageCell(GameCanvas.DARK.getImage(), xFix, yFix);
          }
        } else {
          this.drawGrid.setImageCell(GameCanvas.NOTHING.getGameImage(), xFix,
              yFix);
        }
      }
    }
    final int gSize = ImageConstants.SIZE;
    final int vSize = Prefs.getViewingWindowSize();
    for (x = 0; x < vSize; x++) {
      for (y = 0; y < vSize; y++) {
        g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize,
            gSize, gSize, null);
      }
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
