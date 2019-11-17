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
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.game.Game;
import com.puttysoftware.fantastlereboot.gui.PreferencesManager;
import com.puttysoftware.fantastlereboot.loaders.ImageConstants;
import com.puttysoftware.fantastlereboot.maze.Maze;
import com.puttysoftware.fantastlereboot.maze.MazeManager;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModelList;
import com.puttysoftware.fantastlereboot.objectmodel.Layers;
import com.puttysoftware.fantastlereboot.objects.Nothing;
import com.puttysoftware.fantastlereboot.objects.OpenSpace;
import com.puttysoftware.fantastlereboot.objects.StairsDown;
import com.puttysoftware.fantastlereboot.objects.StairsUp;
import com.puttysoftware.images.BufferedImageIcon;

public class MazeEditor {
  // Declarations
  private MainWindow outputFrame;
  private Container outputPane, secondaryPane, borderPane;
  private GridBagLayout gridbag;
  private GridBagConstraints c;
  private JScrollBar vertScroll, horzScroll;
  private final EventHandler mhandler;
  private EditorPicturePicker picker;
  private final FantastleObjectModelList objectList;
  private final FantastleObjectModel[] groundObjects;
  private final FantastleObjectModel[] objectObjects;
  private final BufferedImageIcon[] groundEditorAppearances;
  private final BufferedImageIcon[] objectEditorAppearances;
  private int currentObjectIndex;
  private UndoRedoEngine engine;
  private EditorLocationManager elMgr;
  EditorViewingWindowManager evMgr;
  private JLabel[][] drawGrid;
  private boolean mazeChanged;
  private FantastleObjectModel savedObject;
  public static final int STAIRS_UP = 0;
  public static final int STAIRS_DOWN = 1;
  public static final FantastleObjectModel NOTHING = new Nothing();

  public MazeEditor() {
    this.mhandler = new EventHandler();
    this.engine = new UndoRedoEngine();
    this.objectList = FantastleReboot.getBagOStuff().getObjects();
    this.groundObjects = this.objectList.getAllGroundLayerObjects();
    this.objectObjects = this.objectList.getAllObjectLayerObjects();
    this.groundEditorAppearances = this.objectList
        .getAllGroundLayerEditorAppearances();
    this.objectEditorAppearances = this.objectList
        .getAllObjectLayerEditorAppearances();
    this.mazeChanged = true;
  }

  public void mazeChanged() {
    this.mazeChanged = true;
  }

  private EditorViewingWindowManager getViewManager() {
    return this.evMgr;
  }

  public void updateEditorPosition(final int x, final int y, final int z,
      final int w) {
    this.elMgr.offsetEditorLocationW(w);
    this.evMgr.offsetViewingWindowLocationX(x);
    this.evMgr.offsetViewingWindowLocationY(y);
    this.elMgr.offsetEditorLocationZ(z);
    if (w != 0) {
      // Level Change
      this.fixLimits();
      this.setUpGUI();
    }
    this.checkMenus();
    this.redrawEditor();
  }

  public void updateEditorLevelAbsolute(final int w) {
    this.elMgr.setEditorLocationW(w);
    // Level Change
    this.fixLimits();
    this.setUpGUI();
    this.checkMenus();
    this.redrawEditor();
  }

  private void checkMenus() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    if (this.elMgr.getEditorLocationZ() == this.elMgr.getMinEditorLocationZ()) {
      app.getMenuManager().disableDownOneFloor();
    } else {
      app.getMenuManager().enableDownOneFloor();
    }
    if (this.elMgr.getEditorLocationZ() == this.elMgr.getMaxEditorLocationZ()) {
      app.getMenuManager().disableUpOneFloor();
    } else {
      app.getMenuManager().enableUpOneFloor();
    }
    if (this.elMgr.getEditorLocationW() == this.elMgr.getMinEditorLocationW()) {
      app.getMenuManager().disableDownOneLevel();
    } else {
      app.getMenuManager().enableDownOneLevel();
    }
    if (this.elMgr.getEditorLocationW() == this.elMgr.getMaxEditorLocationW()) {
      app.getMenuManager().disableUpOneLevel();
    } else {
      app.getMenuManager().enableUpOneLevel();
    }
    if (!this.engine.tryUndo()) {
      app.getMenuManager().disableUndo();
    } else {
      app.getMenuManager().enableUndo();
    }
    if (!this.engine.tryRedo()) {
      app.getMenuManager().disableRedo();
    } else {
      app.getMenuManager().enableRedo();
    }
    if (this.engine.tryBoth()) {
      app.getMenuManager().disableClearHistory();
    } else {
      app.getMenuManager().enableClearHistory();
    }
  }

  public void toggleLayer() {
    if (this.elMgr.getEditorLocationE() == Layers.GROUND) {
      this.elMgr.setEditorLocationE(Layers.OBJECT);
    } else {
      this.elMgr.setEditorLocationE(Layers.GROUND);
    }
    this.updatePicker();
    this.redrawEditor();
  }

  public void redrawEditor() {
    if (this.elMgr.getEditorLocationE() == Layers.GROUND) {
      this.redrawGround();
    } else {
      this.redrawGroundAndObjects();
    }
  }

  private void redrawGround() {
    // Draw the maze in edit mode
    final BagOStuff app = FantastleReboot.getBagOStuff();
    int x, y, w;
    int xFix, yFix;
    w = this.elMgr.getEditorLocationW();
    for (x = this.evMgr.getViewingWindowLocationX(); x <= this.evMgr
        .getLowerRightViewingWindowLocationX(); x++) {
      for (y = this.evMgr.getViewingWindowLocationY(); y <= this.evMgr
          .getLowerRightViewingWindowLocationY(); y++) {
        xFix = x - this.evMgr.getViewingWindowLocationX();
        yFix = y - this.evMgr.getViewingWindowLocationY();
        try {
          final FantastleObjectModel obj1 = app.getMazeManager().getMaze()
              .getCell(y, x, this.elMgr.getEditorLocationZ(), Layers.GROUND);
          this.drawGrid[xFix][yFix].setIcon(obj1.getEditorImage());
        } catch (final ArrayIndexOutOfBoundsException ae) {
          this.drawGrid[xFix][yFix].setIcon(NOTHING.getEditorImage());
        }
      }
    }
    this.outputFrame.pack();
    this.outputFrame.setTitle("Editor (Ground Layer) - Floor "
        + (this.elMgr.getEditorLocationZ() + 1) + " Level " + (w + 1));
    this.showOutput();
  }

  private void redrawGroundAndObjects() {
    // Draw the maze in edit mode
    final BagOStuff app = FantastleReboot.getBagOStuff();
    int x, y, w;
    int xFix, yFix;
    w = this.elMgr.getEditorLocationW();
    for (x = this.evMgr.getViewingWindowLocationX(); x <= this.evMgr
        .getLowerRightViewingWindowLocationX(); x++) {
      for (y = this.evMgr.getViewingWindowLocationY(); y <= this.evMgr
          .getLowerRightViewingWindowLocationY(); y++) {
        xFix = x - this.evMgr.getViewingWindowLocationX();
        yFix = y - this.evMgr.getViewingWindowLocationY();
        try {
          final FantastleObjectModel obj1 = app.getMazeManager().getMaze()
              .getCell(y, x, this.elMgr.getEditorLocationZ(), Layers.GROUND);
          final FantastleObjectModel obj2 = app.getMazeManager().getMaze()
              .getCell(y, x, this.elMgr.getEditorLocationZ(), Layers.OBJECT);
          this.drawGrid[xFix][yFix].setIcon(ImageCompositor.composite(
              MazeEditor.generateCacheName(obj1, obj2), obj1.getEditorImage(),
              obj2.getEditorImage()));
        } catch (final ArrayIndexOutOfBoundsException ae) {
          this.drawGrid[xFix][yFix].setIcon(NOTHING.getEditorImage());
        }
      }
    }
    this.outputFrame.pack();
    this.outputFrame.setTitle("Editor (Object Layer) - Floor "
        + (this.elMgr.getEditorLocationZ() + 1) + " Level " + (w + 1));
    this.showOutput();
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

  public void editObject(final int x, final int y) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    this.currentObjectIndex = this.picker.getPicked();
    final int xOffset = this.vertScroll.getValue()
        - this.vertScroll.getMinimum();
    final int yOffset = this.horzScroll.getValue()
        - this.horzScroll.getMinimum();
    final int gridX = x / ImageConstants.SIZE
        + this.evMgr.getViewingWindowLocationX() - xOffset + yOffset;
    final int gridY = y / ImageConstants.SIZE
        + this.evMgr.getViewingWindowLocationY() + xOffset - yOffset;
    try {
      this.savedObject = app.getMazeManager().getMaze().getCell(gridX, gridY,
          this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationE());
    } catch (final ArrayIndexOutOfBoundsException ae) {
      return;
    }
    FantastleObjectModel[] choices = null;
    if (this.elMgr.getEditorLocationE() == Layers.GROUND) {
      choices = this.groundObjects;
    } else {
      choices = this.objectObjects;
    }
    final FantastleObjectModel mo = choices[this.currentObjectIndex];
    this.elMgr.setEditorLocationX(gridX);
    this.elMgr.setEditorLocationY(gridY);
    try {
      this.updateUndoHistory(this.savedObject, gridX, gridY,
          this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW(),
          this.elMgr.getEditorLocationE());
      app.getMazeManager().getMaze().setCell(mo, gridX, gridY,
          this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationE());
      this.checkStairPair(this.elMgr.getEditorLocationZ(),
          this.elMgr.getEditorLocationW());
      FileStateManager.setDirty(true);
      this.checkMenus();
      this.redrawEditor();
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      app.getMazeManager().getMaze().setCell(this.savedObject, gridX, gridY,
          this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationE());
      this.redrawEditor();
    }
  }

  public void editObjectProperties(final int x, final int y) {
    Game.setStatusMessage("This object has no properties");
  }

  private void checkStairPair(final int z, final int w) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    FantastleObjectModel obj1 = app.getMazeManager().getMaze().getCell(
        this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), z,
        Layers.OBJECT);
    FantastleObjectModel obj2 = app.getMazeManager().getMaze().getCell(
        this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), z + 1,
        Layers.OBJECT);
    FantastleObjectModel obj3 = app.getMazeManager().getMaze().getCell(
        this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), z - 1,
        Layers.OBJECT);
    if (!(obj1 instanceof StairsUp)) {
      if (obj2 instanceof StairsDown) {
        this.unpairStairs(MazeEditor.STAIRS_UP, z, w);
      } else if (!(obj1 instanceof StairsDown)) {
        if (obj3 instanceof StairsUp) {
          this.unpairStairs(MazeEditor.STAIRS_DOWN, z, w);
        }
      }
    }
  }

  private void reverseCheckStairPair(final int z, final int w) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    FantastleObjectModel obj1 = app.getMazeManager().getMaze().getCell(
        this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), z,
        Layers.OBJECT);
    FantastleObjectModel obj2 = app.getMazeManager().getMaze().getCell(
        this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), z + 1,
        Layers.OBJECT);
    FantastleObjectModel obj3 = app.getMazeManager().getMaze().getCell(
        this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), z - 1,
        Layers.OBJECT);
    if (obj1 instanceof StairsUp) {
      if (!(obj2 instanceof StairsDown)) {
        this.pairStairs(MazeEditor.STAIRS_UP, z, w);
      } else if (obj1 instanceof StairsDown) {
        if (!(obj3 instanceof StairsUp)) {
          this.pairStairs(MazeEditor.STAIRS_DOWN, z, w);
        }
      }
    }
  }

  public void pairStairs(final int type) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    switch (type) {
    case STAIRS_UP:
      try {
        app.getMazeManager().getMaze().setCell(new StairsDown(),
            this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(),
            this.elMgr.getEditorLocationZ() + 1, Layers.OBJECT);
      } catch (final ArrayIndexOutOfBoundsException e) {
        // Do nothing
      }
      break;
    case STAIRS_DOWN:
      try {
        app.getMazeManager().getMaze().setCell(new StairsUp(),
            this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(),
            this.elMgr.getEditorLocationZ() - 1, Layers.OBJECT);
      } catch (final ArrayIndexOutOfBoundsException e) {
        // Do nothing
      }
      break;
    default:
      break;
    }
  }

  private void pairStairs(final int type, final int z, final int w) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    switch (type) {
    case STAIRS_UP:
      try {
        app.getMazeManager().getMaze().setCell(new StairsDown(),
            this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(),
            z + 1, Layers.OBJECT);
      } catch (final ArrayIndexOutOfBoundsException e) {
        // Do nothing
      }
      break;
    case STAIRS_DOWN:
      try {
        app.getMazeManager().getMaze().setCell(new StairsUp(),
            this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(),
            z - 1, Layers.OBJECT);
      } catch (final ArrayIndexOutOfBoundsException e) {
        // Do nothing
      }
      break;
    default:
      break;
    }
  }

  private void unpairStairs(final int type, final int z, final int w) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    switch (type) {
    case STAIRS_UP:
      try {
        app.getMazeManager().getMaze().setCell(new OpenSpace(),
            this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(),
            z + 1, Layers.OBJECT);
      } catch (final ArrayIndexOutOfBoundsException e) {
        // Do nothing
      }
      break;
    case STAIRS_DOWN:
      try {
        app.getMazeManager().getMaze().setCell(new OpenSpace(),
            this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(),
            z - 1, Layers.OBJECT);
      } catch (final ArrayIndexOutOfBoundsException e) {
        // Do nothing
      }
      break;
    default:
      break;
    }
  }

  public void setPlayerLocation() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    app.getMazeManager().getMaze().setStartRow(this.elMgr.getEditorLocationY());
    app.getMazeManager().getMaze()
        .setStartColumn(this.elMgr.getEditorLocationX());
    app.getMazeManager().getMaze()
        .setStartFloor(this.elMgr.getEditorLocationZ());
  }

  public void editMaze() {
    final BagOStuff bag = FantastleReboot.getBagOStuff();
    if (FileStateManager.getLoaded()) {
      bag.setInEditor();
      // Reset game state
      Game.resetGameState();
      // Create the managers
      if (this.mazeChanged) {
        this.elMgr = new EditorLocationManager();
        this.evMgr = new EditorViewingWindowManager();
        final int mazeSizeX = bag.getMazeManager().getMaze().getRows();
        final int mazeSizeY = bag.getMazeManager().getMaze().getColumns();
        bag.getEditor().getViewManager()
            .halfOffsetMaximumViewingWindowLocation(mazeSizeX, mazeSizeY);
        this.mazeChanged = false;
      }
      // Reset the level
      this.elMgr.setEditorLocationZ(0);
      this.elMgr.setEditorLocationW(0);
      this.elMgr.setLimitsFromMaze(bag.getMazeManager().getMaze());
      this.evMgr.halfOffsetMaximumViewingWindowLocationsFromMaze(
          bag.getMazeManager().getMaze());
      this.setUpGUI();
      this.clearHistory();
      this.checkMenus();
      this.borderPane.removeAll();
      this.borderPane.add(this.outputPane, BorderLayout.CENTER);
      this.borderPane.add(this.picker.getPicker(), BorderLayout.EAST);
      bag.getMenuManager().setEditorMenus();
      this.showOutput();
      this.redrawEditor();
    } else {
      CommonDialogs.showDialog("No Maze Opened");
    }
  }

  public boolean newMaze() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    boolean success = true;
    boolean saved = true;
    int status = 0;
    if (FileStateManager.getDirty()) {
      status = FileStateManager.showSaveDialog();
      if (status == JOptionPane.YES_OPTION) {
        saved = MazeManager.saveGame();
      } else if (status == JOptionPane.CANCEL_OPTION) {
        saved = false;
      } else {
        FileStateManager.setDirty(false);
      }
    }
    if (saved) {
      Game.resetPlayerLocation();
      app.getMazeManager().setMaze(new Maze());
      success = this.addLevelInternal(true);
      if (success) {
        this.clearHistory();
        Game.invalidateScore();
      }
    } else {
      success = false;
    }
    if (success) {
      this.mazeChanged = true;
    }
    return success;
  }

  public void fixLimits() {
    // Fix limits
    final BagOStuff app = FantastleReboot.getBagOStuff();
    if (app.getMazeManager().getMaze() != null && this.elMgr != null
        && this.evMgr != null) {
      this.elMgr.setLimitsFromMaze(app.getMazeManager().getMaze());
      this.evMgr.halfOffsetMaximumViewingWindowLocationsFromMaze(
          app.getMazeManager().getMaze());
    }
  }

  public boolean addLevel() {
    return this.addLevelInternal(false);
  }

  private boolean addLevelInternal(final boolean flag) {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    int levelSizeX, levelSizeY, levelSizeZ;
    final int absoluteRCLimit = 64;
    final int absoluteFLimit = 8;
    String msg = null;
    if (flag) {
      msg = "New Maze";
    } else {
      msg = "New Level";
    }
    boolean success = true;
    String input1, input2, input3;
    input1 = CommonDialogs.showTextInputDialog("Number of rows?", msg);
    if (input1 != null) {
      input2 = CommonDialogs.showTextInputDialog("Number of columns?", msg);
      if (input2 != null) {
        input3 = CommonDialogs.showTextInputDialog("Number of floors?", msg);
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
            success = app.getMazeManager().getMaze().addLevel(levelSizeX,
                levelSizeY, levelSizeZ);
            if (success) {
              this.fixLimits();
              if (!flag) {
                this.evMgr.setViewingWindowLocationX(
                    0 - (EditorViewingWindowManager.getViewingWindowSizeX() - 1)
                        / 2);
                this.evMgr.setViewingWindowLocationY(
                    0 - (EditorViewingWindowManager.getViewingWindowSizeY() - 1)
                        / 2);
              }
              app.getMazeManager().getMaze().fill(
                  PreferencesManager.getEditorDefaultFill(), new OpenSpace());
              this.checkMenus();
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

  public void goToHandler() {
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
              this.updateEditorPosition(locX, locY, locZ, locW);
            } catch (final NumberFormatException nf) {
              CommonDialogs.showDialog(nf.getMessage());
            }
          }
        }
      }
    }
  }

  public void showOutput() {
    this.outputFrame = MainWindow.getOutputFrame();
    this.outputFrame.setContentPane(this.borderPane);
    this.outputFrame.addWindowListener(this.mhandler);
  }

  public void hideOutput() {
    this.outputFrame.removeWindowListener(this.mhandler);
  }

  void disableOutput() {
    this.outputPane.setEnabled(false);
  }

  void enableOutput() {
    this.outputPane.setEnabled(true);
    this.checkMenus();
  }

  public void exitEditor() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    // Hide the editor
    this.hideOutput();
    final MazeManager mm = app.getMazeManager();
    // Save the entire level
    mm.getMaze().save();
    // Reset the viewing window
    Game.resetViewingWindowAndPlayerLocation();
    FantastleReboot.getBagOStuff().getGUIManager().showGUI();
  }

  private void setUpGUI() {
    this.outputPane = new Container();
    this.secondaryPane = new Container();
    this.borderPane = new Container();
    this.borderPane.setLayout(new BorderLayout());
    this.drawGrid = new JLabel[EditorViewingWindowManager
        .getViewingWindowSizeX()][EditorViewingWindowManager
            .getViewingWindowSizeY()];
    for (int x = 0; x < EditorViewingWindowManager
        .getViewingWindowSizeX(); x++) {
      for (int y = 0; y < EditorViewingWindowManager
          .getViewingWindowSizeY(); y++) {
        this.drawGrid[x][y] = new JLabel();
        // Mac OS X-specific fix to make draw grid line up properly
        if (System.getProperty("os.name").startsWith("Mac OS X")) {
          this.drawGrid[x][y].setBorder(new EmptyBorder(0, 0, 0, 0));
        }
        this.secondaryPane.add(this.drawGrid[x][y]);
      }
    }
    this.borderPane.add(this.outputPane, BorderLayout.CENTER);
    this.gridbag = new GridBagLayout();
    this.c = new GridBagConstraints();
    this.outputPane.setLayout(this.gridbag);
    this.c.fill = GridBagConstraints.BOTH;
    this.secondaryPane.setLayout(
        new GridLayout(EditorViewingWindowManager.getViewingWindowSizeX(),
            EditorViewingWindowManager.getViewingWindowSizeY()));
    this.horzScroll = new JScrollBar(Adjustable.HORIZONTAL,
        EditorViewingWindowManager.getMinimumViewingWindowLocationY(),
        EditorViewingWindowManager.getViewingWindowSizeY(),
        EditorViewingWindowManager.getMinimumViewingWindowLocationY(),
        this.evMgr.getMaximumViewingWindowLocationY());
    this.vertScroll = new JScrollBar(Adjustable.VERTICAL,
        EditorViewingWindowManager.getMinimumViewingWindowLocationX(),
        EditorViewingWindowManager.getViewingWindowSizeX(),
        EditorViewingWindowManager.getMinimumViewingWindowLocationX(),
        this.evMgr.getMaximumViewingWindowLocationX());
    this.c.gridx = 0;
    this.c.gridy = 0;
    this.gridbag.setConstraints(this.secondaryPane, this.c);
    this.outputPane.add(this.secondaryPane);
    this.c.gridx = 1;
    this.c.gridy = 0;
    this.c.gridwidth = GridBagConstraints.REMAINDER;
    this.gridbag.setConstraints(this.vertScroll, this.c);
    this.outputPane.add(this.vertScroll);
    this.c.gridx = 0;
    this.c.gridy = 1;
    this.c.gridwidth = 1;
    this.c.gridheight = GridBagConstraints.REMAINDER;
    this.gridbag.setConstraints(this.horzScroll, this.c);
    this.outputPane.add(this.horzScroll);
    this.horzScroll.addAdjustmentListener(this.mhandler);
    this.vertScroll.addAdjustmentListener(this.mhandler);
    this.secondaryPane.addMouseListener(this.mhandler);
    this.updatePicker();
    this.borderPane.add(this.picker.getPicker(), BorderLayout.EAST);
  }

  public void undo() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    this.engine.undo();
    final FantastleObjectModel obj = this.engine.getObject();
    final int x = this.engine.getX();
    final int y = this.engine.getY();
    final int z = this.engine.getZ();
    final int w = this.engine.getW();
    final int e = this.engine.getE();
    this.elMgr.setEditorLocationX(x);
    this.elMgr.setEditorLocationY(y);
    this.elMgr.setCameFromZ(z);
    this.elMgr.setCameFromW(w);
    if (x != -1 && y != -1 && z != -1 && w != -1) {
      final FantastleObjectModel oldObj = app.getMazeManager().getMazeCell(x, y,
          z, e);
      app.getMazeManager().getMaze().setCell(obj, x, y, z, e);
      if (!(obj instanceof StairsUp) && !(obj instanceof StairsDown)) {
        this.checkStairPair(z, w);
      } else {
        this.reverseCheckStairPair(z, w);
      }
      this.updateRedoHistory(oldObj, x, y, z, w, e);
      this.checkMenus();
      this.redrawEditor();
    } else {
      Game.setStatusMessage("Nothing to undo");
    }
  }

  public void redo() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    this.engine.redo();
    final FantastleObjectModel obj = this.engine.getObject();
    final int x = this.engine.getX();
    final int y = this.engine.getY();
    final int z = this.engine.getZ();
    final int w = this.engine.getW();
    final int e = this.engine.getE();
    this.elMgr.setEditorLocationX(x);
    this.elMgr.setEditorLocationY(y);
    this.elMgr.setCameFromZ(z);
    this.elMgr.setCameFromW(w);
    if (x != -1 && y != -1 && z != -1 && w != -1) {
      final FantastleObjectModel oldObj = app.getMazeManager().getMazeCell(x, y,
          z, e);
      app.getMazeManager().getMaze().setCell(obj, x, y, z, e);
      if (!(obj instanceof StairsUp) && !(obj instanceof StairsDown)) {
        this.checkStairPair(z, w);
      } else {
        this.reverseCheckStairPair(z, w);
      }
      this.updateUndoHistory(oldObj, x, y, z, w, e);
      this.checkMenus();
      this.redrawEditor();
    } else {
      Game.setStatusMessage("Nothing to redo");
    }
  }

  public void clearHistory() {
    this.engine = new UndoRedoEngine();
    this.checkMenus();
  }

  private void updateUndoHistory(final FantastleObjectModel obj, final int x,
      final int y, final int z, final int w, final int e) {
    this.engine.updateUndoHistory(obj, x, y, z, w, e);
  }

  private void updateRedoHistory(final FantastleObjectModel obj, final int x,
      final int y, final int z, final int w, final int e) {
    this.engine.updateRedoHistory(obj, x, y, z, w, e);
  }

  public void updatePicker() {
    BufferedImageIcon[] newImages = null;
    if (this.elMgr.getEditorLocationE() == Layers.GROUND) {
      newImages = this.groundEditorAppearances;
    } else {
      newImages = this.objectEditorAppearances;
    }
    if (this.picker != null) {
      this.picker.updatePicker(newImages);
    } else {
      this.picker = new EditorPicturePicker(newImages);
    }
  }

  public void handleCloseWindow() {
    try {
      boolean success = false;
      int status = JOptionPane.DEFAULT_OPTION;
      if (FileStateManager.getDirty()) {
        status = FileStateManager.showSaveDialog();
        if (status == JOptionPane.YES_OPTION) {
          success = MazeManager.saveGame();
          if (success) {
            this.exitEditor();
          }
        } else if (status == JOptionPane.NO_OPTION) {
          FileStateManager.setDirty(false);
          this.exitEditor();
        }
      } else {
        this.exitEditor();
      }
    } catch (final Exception ex) {
      FantastleReboot.logError(ex);
    }
  }

  private class EventHandler
      implements AdjustmentListener, MouseListener, WindowListener {
    public EventHandler() {
      super();
    }

    // handle scrollbars
    @Override
    public void adjustmentValueChanged(final AdjustmentEvent e) {
      try {
        final MazeEditor me = MazeEditor.this;
        final Adjustable src = e.getAdjustable();
        final int dir = src.getOrientation();
        final int value = src.getValue();
        int relValue = 0;
        switch (dir) {
        case Adjustable.HORIZONTAL:
          relValue = value - me.evMgr.getViewingWindowLocationY();
          me.updateEditorPosition(0, relValue, 0, 0);
          break;
        case Adjustable.VERTICAL:
          relValue = value - me.evMgr.getViewingWindowLocationX();
          me.updateEditorPosition(relValue, 0, 0, 0);
          break;
        default:
          break;
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
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
        final MazeEditor me = MazeEditor.this;
        final int x = e.getX();
        final int y = e.getY();
        if (e.isAltDown()) {
          me.editObjectProperties(x, y);
        } else {
          me.editObject(x, y);
        }
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
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
      MazeEditor.this.handleCloseWindow();
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
