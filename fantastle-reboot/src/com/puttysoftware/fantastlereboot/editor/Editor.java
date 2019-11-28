/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.editor;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Modes;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.files.MazeFileManager;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.loaders.ImageConstants;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.GameObjects;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.StairsDown;
import com.puttysoftware.fantastlereboot.objects.StairsUp;
import com.puttysoftware.images.BufferedImageIcon;

public class Editor {
  // Declarations
  private static MainWindow outputFrame;
  private static JPanel outputPane, secondaryPane, borderPane;
  private static GridBagLayout gridbag;
  private static GridBagConstraints c;
  private static JScrollBar vertScroll, horzScroll;
  private static EventHandler mhandler;
  private static EditorPicturePicker picker;
  private static FantastleObjectModel[] groundObjects;
  private static FantastleObjectModel[] objectObjects;
  private static BufferedImageIcon[] groundEditorAppearances;
  private static BufferedImageIcon[] objectEditorAppearances;
  private static int currentObjectIndex;
  private static UndoRedoEngine engine;
  private static EditorDraw drawingThread;
  private static boolean mazeChanged;
  private static FantastleObjectModel savedObject;
  public static final int STAIRS_UP = 0;
  public static final int STAIRS_DOWN = 1;
  public static final FantastleObjectModel NOTHING = new Nothing();

  private Editor() {
    super();
  }

  static {
    Game.initialize();
    Editor.mhandler = new EventHandler();
    Editor.engine = new UndoRedoEngine();
    Editor.groundObjects = GameObjects.getAllGroundLayerObjects();
    Editor.objectObjects = GameObjects.getAllObjectLayerObjects();
    Editor.groundEditorAppearances = GameObjects
        .getAllGroundLayerEditorAppearances();
    Editor.objectEditorAppearances = GameObjects
        .getAllObjectLayerEditorAppearances();
    Editor.mazeChanged = true;
  }

  public static void mazeChanged() {
    Editor.mazeChanged = true;
  }

  public static void updateEditorPosition(final int x, final int y, final int z,
      final int w) {
    EditorLoc.offsetLocW(w);
    EditorView.offsetLocX(x);
    EditorView.offsetLocY(y);
    EditorLoc.offsetLocZ(z);
    if (w != 0) {
      // Level Change
      Editor.fixLimits();
      Editor.setUpGUI();
    }
    Editor.checkMenus();
    Editor.redrawEditor();
  }

  public static void updateEditorLevelAbsolute(final int w) {
    EditorLoc.setLocW(w);
    // Level Change
    Editor.fixLimits();
    Editor.setUpGUI();
    Editor.checkMenus();
    Editor.redrawEditor();
  }

  private static void checkMenus() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    if (EditorLoc.getLocZ() == EditorLoc.getMinLocZ()) {
      app.getMenuManager().disableDownOneFloor();
    } else {
      app.getMenuManager().enableDownOneFloor();
    }
    if (EditorLoc.getLocZ() == EditorLoc.getMaxLocZ()) {
      app.getMenuManager().disableUpOneFloor();
    } else {
      app.getMenuManager().enableUpOneFloor();
    }
    if (EditorLoc.getLocW() == EditorLoc.getMinLocW()) {
      app.getMenuManager().disableDownOneLevel();
    } else {
      app.getMenuManager().enableDownOneLevel();
    }
    if (EditorLoc.getLocW() == EditorLoc.getMaxLocW()) {
      app.getMenuManager().disableUpOneLevel();
    } else {
      app.getMenuManager().enableUpOneLevel();
    }
    if (!Editor.engine.tryUndo()) {
      app.getMenuManager().disableUndo();
    } else {
      app.getMenuManager().enableUndo();
    }
    if (!Editor.engine.tryRedo()) {
      app.getMenuManager().disableRedo();
    } else {
      app.getMenuManager().enableRedo();
    }
    if (Editor.engine.tryBoth()) {
      app.getMenuManager().disableClearHistory();
    } else {
      app.getMenuManager().enableClearHistory();
    }
  }

  public static void toggleLayer() {
    if (EditorLoc.getLocE() == Layers.GROUND) {
      EditorLoc.setLocE(Layers.OBJECT);
    } else {
      EditorLoc.setLocE(Layers.GROUND);
    }
    Editor.updatePicker();
    Editor.redrawEditor();
  }

  public static void redrawEditor() {
    if (EditorLoc.getLocE() == Layers.GROUND) {
      Editor.redrawGround();
    } else {
      Editor.redrawGroundAndObjects();
    }
  }

  private static void redrawGround() {
    final int z = EditorLoc.getLocZ();
    final int w = EditorLoc.getLocW();
    Editor.drawingThread.requestDrawGround();
    Editor.outputFrame.pack();
    Editor.outputFrame.setTitle(
        "Editor (Ground Layer) - Floor " + (z + 1) + " Level " + (w + 1));
    Editor.showOutput();
  }

  private static void redrawGroundAndObjects() {
    final int z = EditorLoc.getLocZ();
    final int w = EditorLoc.getLocW();
    Editor.drawingThread.requestDrawObjects();
    Editor.outputFrame.pack();
    Editor.outputFrame.setTitle(
        "Editor (Object Layer) - Floor " + (z + 1) + " Level " + (w + 1));
    Editor.showOutput();
  }

  public static void editObject(final int x, final int y) {
    final Maze maze = MazeManager.getMaze();
    Editor.currentObjectIndex = Editor.picker.getPicked();
    final int xOffset = Editor.vertScroll.getValue()
        - Editor.vertScroll.getMinimum();
    final int yOffset = Editor.horzScroll.getValue()
        - Editor.horzScroll.getMinimum();
    final int gridX = x / ImageConstants.SIZE + EditorView.getLocX() - xOffset
        + yOffset;
    final int gridY = y / ImageConstants.SIZE + EditorView.getLocY() + xOffset
        - yOffset;
    final int gridZ = EditorLoc.getLocZ();
    final int gridW = EditorLoc.getLocW();
    final int gridE = EditorLoc.getLocE();
    if (maze.cellRangeCheck(gridX, gridY, gridZ)) {
      Editor.savedObject = maze.getCell(gridX, gridY, gridZ, gridE);
    } else {
      return;
    }
    FantastleObjectModel[] choices = null;
    if (EditorLoc.getLocE() == Layers.GROUND) {
      choices = Editor.groundObjects;
    } else {
      choices = Editor.objectObjects;
    }
    final FantastleObjectModel mo = choices[Editor.currentObjectIndex];
    EditorLoc.setLocX(gridX);
    EditorLoc.setLocY(gridY);
    if (maze.cellRangeCheck(gridX, gridY, gridZ)) {
      Editor.updateUndoHistory(Editor.savedObject, gridX, gridY, gridZ,
          EditorLoc.getLocW(), gridE);
      maze.setCell(mo, gridX, gridY, gridZ, gridE);
      Editor.checkStairPair(gridZ, gridW);
      FileStateManager.setDirty(true);
      Editor.checkMenus();
      Editor.redrawEditor();
    } else {
      maze.setCell(Editor.savedObject, gridX, gridY, gridZ, gridE);
      Editor.redrawEditor();
    }
  }

  public static void editObjectProperties(final int x, final int y) {
    Game.setStatusMessage("This object has no properties");
  }

  private static void checkStairPair(final int z, final int w) {
    final FantastleObjectModel obj1 = MazeManager.getMaze()
        .getCell(EditorLoc.getLocX(), EditorLoc.getLocY(), z, Layers.OBJECT);
    final FantastleObjectModel obj2 = MazeManager.getMaze().getCell(
        EditorLoc.getLocX(), EditorLoc.getLocY(), z + 1, Layers.OBJECT);
    final FantastleObjectModel obj3 = MazeManager.getMaze().getCell(
        EditorLoc.getLocX(), EditorLoc.getLocY(), z - 1, Layers.OBJECT);
    if (!(obj1 instanceof StairsUp)) {
      if (obj2 instanceof StairsDown) {
        Editor.unpairStairs(Editor.STAIRS_UP, z, w);
      } else if (!(obj1 instanceof StairsDown)) {
        if (obj3 instanceof StairsUp) {
          Editor.unpairStairs(Editor.STAIRS_DOWN, z, w);
        }
      }
    }
  }

  private static void reverseCheckStairPair(final int z, final int w) {
    final FantastleObjectModel obj1 = MazeManager.getMaze()
        .getCell(EditorLoc.getLocX(), EditorLoc.getLocY(), z, Layers.OBJECT);
    final FantastleObjectModel obj2 = MazeManager.getMaze().getCell(
        EditorLoc.getLocX(), EditorLoc.getLocY(), z + 1, Layers.OBJECT);
    final FantastleObjectModel obj3 = MazeManager.getMaze().getCell(
        EditorLoc.getLocX(), EditorLoc.getLocY(), z - 1, Layers.OBJECT);
    if (obj1 instanceof StairsUp) {
      if (!(obj2 instanceof StairsDown)) {
        Editor.pairStairs(Editor.STAIRS_UP, z, w);
      } else if (obj1 instanceof StairsDown) {
        if (!(obj3 instanceof StairsUp)) {
          Editor.pairStairs(Editor.STAIRS_DOWN, z, w);
        }
      }
    }
  }

  public static void pairStairs(final int type) {
    final Maze maze = MazeManager.getMaze();
    final int locX = EditorLoc.getLocX();
    final int locY = EditorLoc.getLocY();
    final int locZ = EditorLoc.getLocZ();
    switch (type) {
    case STAIRS_UP:
      if (maze.cellRangeCheck(locX, locY, locZ + 1)) {
        MazeManager.getMaze().setCell(new StairsDown(), locX, locY, locZ + 1,
            Layers.OBJECT);
      }
      break;
    case STAIRS_DOWN:
      if (maze.cellRangeCheck(locX, locY, locZ - 1)) {
        MazeManager.getMaze().setCell(new StairsUp(), locX, locY, locZ - 1,
            Layers.OBJECT);
      }
      break;
    default:
      break;
    }
  }

  private static void pairStairs(final int type, final int z, final int w) {
    final Maze maze = MazeManager.getMaze();
    final int locX = EditorLoc.getLocX();
    final int locY = EditorLoc.getLocY();
    switch (type) {
    case STAIRS_UP:
      if (maze.cellRangeCheck(locX, locY, z + 1)) {
        maze.setCell(new StairsDown(), locX, locY, z + 1, Layers.OBJECT);
      }
      break;
    case STAIRS_DOWN:
      if (maze.cellRangeCheck(locX, locY, z - 1)) {
        maze.setCell(new StairsUp(), locX, locY, z - 1, Layers.OBJECT);
      }
      break;
    default:
      break;
    }
  }

  private static void unpairStairs(final int type, final int z, final int w) {
    final Maze maze = MazeManager.getMaze();
    final int locX = EditorLoc.getLocX();
    final int locY = EditorLoc.getLocY();
    switch (type) {
    case STAIRS_UP:
      if (maze.cellRangeCheck(locX, locY, z + 1)) {
        maze.setCell(new OpenSpace(), locX, locY, z + 1, Layers.OBJECT);
      }
      break;
    case STAIRS_DOWN:
      if (maze.cellRangeCheck(locX, locY, z - 1)) {
        maze.setCell(new OpenSpace(), locX, locY, z - 1, Layers.OBJECT);
      }
      break;
    default:
      break;
    }
  }

  public static void setPlayerLocation() {
    MazeManager.getMaze().setStartRow(EditorLoc.getLocY());
    MazeManager.getMaze().setStartColumn(EditorLoc.getLocX());
    MazeManager.getMaze().setStartFloor(EditorLoc.getLocZ());
  }

  public static void editMaze() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    if (FileStateManager.getLoaded()) {
      Modes.setInEditor();
      // Reset game state
      Game.resetGameState();
      // Create the managers
      if (Editor.mazeChanged) {
        final int mazeSizeX = MazeManager.getMaze().getRows();
        final int mazeSizeY = MazeManager.getMaze().getColumns();
        EditorView.halfOffsetMax(mazeSizeX, mazeSizeY);
        Editor.mazeChanged = false;
      }
      // Reset the level
      EditorLoc.setLocZ(0);
      EditorLoc.setLocW(0);
      EditorLoc.setLimitsFromMaze(MazeManager.getMaze());
      EditorView.halfOffsetMaxFromMaze(MazeManager.getMaze());
      Editor.setUpGUI();
      Editor.clearHistory();
      Editor.checkMenus();
      Editor.borderPane.removeAll();
      Editor.borderPane.add(Editor.outputPane, BorderLayout.CENTER);
      Editor.borderPane.add(Editor.picker.getPicker(), BorderLayout.EAST);
      bag.getMenuManager().setEditorMenus();
      Editor.showOutput();
      Editor.redrawEditor();
    } else {
      CommonDialogs.showDialog("No Maze Opened");
    }
  }

  public static boolean newMaze() {
    boolean success = true;
    boolean saved = true;
    int status = 0;
    if (FileStateManager.getDirty()) {
      status = FileStateManager.showSaveDialog();
      if (status == JOptionPane.YES_OPTION) {
        saved = MazeFileManager.saveMaze();
      } else if (status == JOptionPane.CANCEL_OPTION) {
        saved = false;
      } else {
        FileStateManager.setDirty(false);
      }
    }
    if (saved) {
      Game.resetPlayerLocation();
      MazeManager.setMaze(new Maze());
      success = Editor.addLevelInternal(true);
      if (success) {
        Editor.clearHistory();
        Game.invalidateScore();
      }
    } else {
      success = false;
    }
    if (success) {
      Editor.mazeChanged = true;
    }
    return success;
  }

  public static void fixLimits() {
    // Fix limits
    if (MazeManager.getMaze() != null) {
      EditorLoc.setLimitsFromMaze(MazeManager.getMaze());
      EditorView.halfOffsetMaxFromMaze(MazeManager.getMaze());
    }
  }

  public static boolean addLevel() {
    return Editor.addLevelInternal(false);
  }

  private static boolean addLevelInternal(final boolean flag) {
    int levelSizeX, levelSizeY, levelSizeZ;
    final int absoluteRCLimit = 64;
    final int absoluteFLimit = 16;
    String msg = null;
    if (flag) {
      msg = "New Maze";
    } else {
      msg = "New Level";
    }
    boolean success = true;
    String input1, input2, input3;
    input1 = CommonDialogs.showTextInputDialogWithDefault(
        "Number of rows? (minimum 2, maximum " + absoluteRCLimit + ")", msg,
        Integer.toString(absoluteRCLimit));
    if (input1 != null) {
      input2 = CommonDialogs.showTextInputDialogWithDefault(
          "Number of columns? (minimum 2, maximum " + absoluteRCLimit + ")",
          msg, Integer.toString(absoluteRCLimit));
      if (input2 != null) {
        input3 = CommonDialogs.showTextInputDialogWithDefault(
            "Number of floors? (minimum 1, maximum " + absoluteFLimit + ")",
            msg, Integer.toString(absoluteFLimit));
        if (input3 != null) {
          try {
            levelSizeX = Integer.parseInt(input1);
            levelSizeY = Integer.parseInt(input2);
            levelSizeZ = Integer.parseInt(input3);
            if (levelSizeX < 2 || levelSizeY < 2) {
              throw new NumberFormatException(
                  "Rows and columns must be at least 2.");
            }
            if (levelSizeX > absoluteRCLimit || levelSizeY > absoluteRCLimit) {
              throw new NumberFormatException(
                  "Rows and columns must be less than or equal to "
                      + absoluteRCLimit + ".");
            }
            if (levelSizeZ < 1) {
              throw new NumberFormatException("Floors must be at least 1.");
            }
            if (levelSizeZ > absoluteFLimit) {
              throw new NumberFormatException(
                  "Floors must be less than or equal to " + absoluteFLimit
                      + ".");
            }
            success = MazeManager.getMaze().addLevel(levelSizeX, levelSizeY,
                levelSizeZ);
            if (success) {
              Editor.fixLimits();
              if (!flag) {
                EditorView.setLocX(0 - (EditorView.getSizeX() - 1) / 2);
                EditorView.setLocY(0 - (EditorView.getSizeY() - 1) / 2);
              }
              MazeManager.getMaze().fill(Prefs.getEditorDefaultFill(),
                  new OpenSpace());
              Editor.checkMenus();
            }
          } catch (final NumberFormatException nf) {
            CommonDialogs.showDialog(nf.getMessage());
            success = false;
          }
        } else {
          // User cancelled
          success = false;
        }
      } else {
        // User cancelled
        success = false;
      }
    } else {
      // User cancelled
      success = false;
    }
    return success;
  }

  public static void goToHandler() {
    int locX, locY, locZ, locW;
    final String msg = "Go To...";
    String input1, input2, input3, input4;
    input1 = CommonDialogs.showTextInputDialog("Row?", msg);
    if (input1 != null) {
      input2 = CommonDialogs.showTextInputDialog("Column?", msg);
      if (input2 != null) {
        input3 = CommonDialogs.showTextInputDialog("Floor?", msg);
        if (input3 != null) {
          input4 = CommonDialogs.showTextInputDialog("Level?", msg);
          if (input4 != null) {
            try {
              locX = Integer.parseInt(input1) - 1;
              locY = Integer.parseInt(input2) - 1;
              locZ = Integer.parseInt(input3) - 1;
              locW = Integer.parseInt(input4) - 1;
              Editor.updateEditorPosition(locX, locY, locZ, locW);
            } catch (final NumberFormatException nf) {
              CommonDialogs.showDialog(nf.getMessage());
            }
          }
        }
      }
    }
  }

  public static void showOutput() {
    Editor.outputFrame = MainWindow.getOutputFrame();
    Editor.outputFrame.attachContent(Editor.borderPane);
    Editor.outputFrame.addWindowListener(Editor.mhandler);
  }

  public static void hideOutput() {
    Editor.outputFrame.removeWindowListener(Editor.mhandler);
  }

  static void disableOutput() {
    Editor.outputPane.setEnabled(false);
  }

  static void enableOutput() {
    Editor.outputPane.setEnabled(true);
    Editor.checkMenus();
  }

  public static void exitEditor() {
    // Hide the editor
    Editor.hideOutput();
    // Save the entire level
    MazeManager.getMaze().save();
    // Reset the viewing window
    Game.resetViewingWindowAndPlayerLocation();
    FantastleReboot.getBagOStuff().getGUIManager().showGUI();
  }

  private static void setUpGUI() {
    Editor.outputPane = new JPanel();
    Editor.secondaryPane = new JPanel();
    Editor.borderPane = new JPanel();
    Editor.borderPane.setLayout(new BorderLayout());
    Editor.borderPane.add(Editor.outputPane, BorderLayout.CENTER);
    Editor.gridbag = new GridBagLayout();
    Editor.c = new GridBagConstraints();
    Editor.outputPane.setLayout(Editor.gridbag);
    Editor.c.fill = GridBagConstraints.BOTH;
    Editor.secondaryPane.setLayout(
        new GridLayout(EditorView.getSizeX(), EditorView.getSizeY()));
    Editor.horzScroll = new JScrollBar(Adjustable.HORIZONTAL,
        EditorView.getMinY(), EditorView.getSizeY(), EditorView.getMinY(),
        EditorView.getMaxY());
    Editor.vertScroll = new JScrollBar(Adjustable.VERTICAL,
        EditorView.getMinX(), EditorView.getSizeX(), EditorView.getMinX(),
        EditorView.getMaxX());
    Editor.c.gridx = 0;
    Editor.c.gridy = 0;
    Editor.gridbag.setConstraints(Editor.secondaryPane, Editor.c);
    Editor.outputPane.add(Editor.secondaryPane);
    Editor.c.gridx = 1;
    Editor.c.gridy = 0;
    Editor.c.gridwidth = GridBagConstraints.REMAINDER;
    Editor.gridbag.setConstraints(Editor.vertScroll, Editor.c);
    Editor.outputPane.add(Editor.vertScroll);
    Editor.c.gridx = 0;
    Editor.c.gridy = 1;
    Editor.c.gridwidth = 1;
    Editor.c.gridheight = GridBagConstraints.REMAINDER;
    Editor.gridbag.setConstraints(Editor.horzScroll, Editor.c);
    Editor.outputPane.add(Editor.horzScroll);
    Editor.horzScroll.addAdjustmentListener(Editor.mhandler);
    Editor.vertScroll.addAdjustmentListener(Editor.mhandler);
    Editor.secondaryPane.addMouseListener(Editor.mhandler);
    Editor.updatePicker();
    Editor.borderPane.add(Editor.picker.getPicker(), BorderLayout.EAST);
    Editor.drawingThread = new EditorDraw(Editor.secondaryPane);
    Editor.drawingThread.start();
  }

  public static void undo() {
    final Maze maze = MazeManager.getMaze();
    Editor.engine.undo();
    final FantastleObjectModel obj = Editor.engine.getObject();
    final int x = Editor.engine.getX();
    final int y = Editor.engine.getY();
    final int z = Editor.engine.getZ();
    final int w = Editor.engine.getW();
    final int e = Editor.engine.getE();
    EditorLoc.setLocX(x);
    EditorLoc.setLocY(y);
    EditorLoc.setCameFromZ(z);
    EditorLoc.setCameFromW(w);
    if (Editor.engine.isDataValid() && maze.cellRangeCheck(x, y, z, w, e)) {
      final FantastleObjectModel oldObj = maze.getCell(x, y, z, e);
      maze.setCell(obj, x, y, z, e);
      if (!(obj instanceof StairsUp) && !(obj instanceof StairsDown)) {
        Editor.checkStairPair(z, w);
      } else {
        Editor.reverseCheckStairPair(z, w);
      }
      Editor.updateRedoHistory(oldObj, x, y, z, w, e);
      Editor.checkMenus();
      Editor.redrawEditor();
    } else {
      Game.setStatusMessage("Nothing to undo");
    }
  }

  public static void redo() {
    final Maze maze = MazeManager.getMaze();
    Editor.engine.redo();
    final FantastleObjectModel obj = Editor.engine.getObject();
    final int x = Editor.engine.getX();
    final int y = Editor.engine.getY();
    final int z = Editor.engine.getZ();
    final int w = Editor.engine.getW();
    final int e = Editor.engine.getE();
    EditorLoc.setLocX(x);
    EditorLoc.setLocY(y);
    EditorLoc.setCameFromZ(z);
    EditorLoc.setCameFromW(w);
    if (Editor.engine.isDataValid() && maze.cellRangeCheck(x, y, z, w, e)) {
      final FantastleObjectModel oldObj = maze.getCell(x, y, z, e);
      maze.setCell(obj, x, y, z, e);
      if (!(obj instanceof StairsUp) && !(obj instanceof StairsDown)) {
        Editor.checkStairPair(z, w);
      } else {
        Editor.reverseCheckStairPair(z, w);
      }
      Editor.updateUndoHistory(oldObj, x, y, z, w, e);
      Editor.checkMenus();
      Editor.redrawEditor();
    } else {
      Game.setStatusMessage("Nothing to redo");
    }
  }

  public static void clearHistory() {
    Editor.engine = new UndoRedoEngine();
    Editor.checkMenus();
  }

  private static void updateUndoHistory(final FantastleObjectModel obj,
      final int x, final int y, final int z, final int w, final int e) {
    Editor.engine.updateUndoHistory(obj, x, y, z, w, e);
  }

  private static void updateRedoHistory(final FantastleObjectModel obj,
      final int x, final int y, final int z, final int w, final int e) {
    Editor.engine.updateRedoHistory(obj, x, y, z, w, e);
  }

  public static void updatePicker() {
    BufferedImageIcon[] newImages = null;
    if (EditorLoc.getLocE() == Layers.GROUND) {
      newImages = Editor.groundEditorAppearances;
    } else {
      newImages = Editor.objectEditorAppearances;
    }
    if (Editor.picker != null) {
      Editor.picker.updatePicker(newImages);
    } else {
      Editor.picker = new EditorPicturePicker(newImages);
    }
  }

  public static void handleCloseWindow() {
    try {
      boolean success = false;
      int status = JOptionPane.DEFAULT_OPTION;
      if (FileStateManager.getDirty()) {
        status = FileStateManager.showSaveDialog();
        if (status == JOptionPane.YES_OPTION) {
          success = MazeFileManager.saveMaze();
          if (success) {
            Editor.exitEditor();
          }
        } else if (status == JOptionPane.NO_OPTION) {
          FileStateManager.setDirty(false);
          Editor.exitEditor();
        }
      } else {
        Editor.exitEditor();
      }
    } catch (final Exception ex) {
      FantastleReboot.exception(ex);
    }
  }

  private static class EventHandler
      implements AdjustmentListener, MouseListener, WindowListener {
    public EventHandler() {
      super();
    }

    // handle scrollbars
    @Override
    public void adjustmentValueChanged(final AdjustmentEvent e) {
      try {
        final Adjustable src = e.getAdjustable();
        final int dir = src.getOrientation();
        final int value = src.getValue();
        int relValue = 0;
        switch (dir) {
        case Adjustable.HORIZONTAL:
          relValue = value - EditorView.getLocY();
          Editor.updateEditorPosition(0, relValue, 0, 0);
          break;
        case Adjustable.VERTICAL:
          relValue = value - EditorView.getLocX();
          Editor.updateEditorPosition(relValue, 0, 0, 0);
          break;
        default:
          break;
        }
      } catch (final Exception ex) {
        FantastleReboot.exception(ex);
      }
    }

    // handle mouse
    @Override
    public void mousePressed(final MouseEvent e) {
      // Do nothing
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
      // Do nothing
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
      try {
        final int x = e.getX();
        final int y = e.getY();
        if (e.isAltDown()) {
          Editor.editObjectProperties(x, y);
        } else {
          Editor.editObject(x, y);
        }
      } catch (final Exception ex) {
        FantastleReboot.exception(ex);
      }
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
      // Do nothing
    }

    @Override
    public void mouseExited(final MouseEvent e) {
      // Do nothing
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
      Editor.handleCloseWindow();
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
