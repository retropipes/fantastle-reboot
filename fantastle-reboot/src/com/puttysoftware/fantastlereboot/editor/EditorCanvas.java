package com.puttysoftware.fantastlereboot.editor;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.loaders.ImageConstants;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.Player;
import com.puttysoftware.fantastlereboot.objects.temporary.NoteObject;
import com.puttysoftware.images.BufferedImageIcon;

class EditorCanvas extends JPanel {
  private static final long serialVersionUID = 1L;
  private final DrawGrid drawGrid;
  private static int MAX_LAYER = Layers.GROUND;
  private static final Nothing NOTHING = new Nothing();
  private static final OpenSpace OPEN = new OpenSpace();
  private static final NoteObject NOTE = new NoteObject();
  private static final Player PLAYER = new Player();

  public EditorCanvas() {
    super();
    this.setName("Editor Drawing Handler");
    this.drawGrid = new DrawGrid(Prefs.getEditorWindowSize());
  }

  public void requestDrawGround() {
    EditorCanvas.MAX_LAYER = Layers.GROUND;
  }

  public void requestDrawObjects() {
    EditorCanvas.MAX_LAYER = Layers.OBJECT;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    final Maze m = MazeManager.getMaze();
    int x, y, u, v;
    int xFix, yFix;
    boolean inBounds;
    u = m.getPlayerLocationX();
    v = m.getPlayerLocationY();
    final int viewX = EditorView.getLocX();
    final int viewY = EditorView.getLocY();
    final int viewLRX = EditorView.getLowerRightLocX();
    final int viewLRY = EditorView.getLowerRightLocY();
    final int z = EditorLoc.getLocZ();
    final boolean playerInBounds = m.cellRangeCheck(u, v,
        m.getPlayerLocationZ());
    for (x = viewX; x <= viewLRX; x++) {
      for (y = viewY; y <= viewLRY; y++) {
        xFix = x - viewX;
        yFix = y - viewY;
        inBounds = m.cellRangeCheck(y, x, z);
        if (inBounds) {
          final FantastleObjectModel obj1 = m.getCell(y, x, z, Layers.GROUND);
          final BufferedImageIcon img1 = obj1.getEditorImage();
          FantastleObjectModel obj3 = EditorCanvas.OPEN;
          BufferedImageIcon img3 = obj3.getEditorImage();
          FantastleObjectModel obj4 = EditorCanvas.OPEN;
          BufferedImageIcon img4 = obj4.getEditorImage();
          final boolean playerSquare = u == y && v == x;
          final boolean noteSquare = m.hasNote(x, y, z);
          if (playerInBounds && playerSquare) {
            obj3 = EditorCanvas.PLAYER;
            img3 = obj3.getEditorImage();
          }
          if (noteSquare) {
            obj4 = EditorCanvas.NOTE;
            img4 = obj4.getEditorImage();
          }
          if (EditorCanvas.MAX_LAYER >= Layers.OBJECT) {
            final FantastleObjectModel obj2 = m.getCell(y, x, z, Layers.OBJECT);
            final BufferedImageIcon img2 = obj2.getEditorImage();
            final String cacheName = EditorCanvas.generateCacheName(obj1, obj2,
                obj3, obj4);
            this.drawGrid.setImageCell(
                ImageCompositor.composite(cacheName, img1, img2, img3, img4),
                xFix, yFix);
          } else {
            final String cacheName = EditorCanvas.generateCacheName(obj1, obj3,
                obj4);
            this.drawGrid.setImageCell(
                ImageCompositor.composite(cacheName, img1, img3, img4), xFix,
                yFix);
          }
        } else {
          this.drawGrid.setImageCell(EditorCanvas.NOTHING.getEditorImage(),
              xFix, yFix);
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
