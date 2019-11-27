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
package com.puttysoftware.fantastlereboot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.editor.Editor;
import com.puttysoftware.fantastlereboot.files.CharacterRegistration;
import com.puttysoftware.fantastlereboot.files.FileStateManager;
import com.puttysoftware.fantastlereboot.files.MazeFileManager;
import com.puttysoftware.fantastlereboot.game.Game;
//import com.puttysoftware.fantastlereboot.editor.MazeEditor;
import com.puttysoftware.fantastlereboot.game.InventoryViewer;
import com.puttysoftware.fantastlereboot.game.StatisticsViewer;
import com.puttysoftware.fantastlereboot.maze.GenerateTask;
import com.puttysoftware.fantastlereboot.maze.NoteManager;

public class MenuManager {
  // Fields
  private final JMenuBar mainMenuBar;
  private JMenu fileMenu, editMenu, playMenu, gameMenu, debugMenu, helpMenu;
  private JMenuItem fileNew, fileOpen, fileClose, fileSave, fileSaveAs,
      fileExit;
  private JMenuItem editUndo, editRedo, editPreferences, editCutLevel,
      editCopyLevel, editPasteLevel, editInsertLevelFromClipboard,
      editClearHistory, editGoTo, editUpOneFloor, editDownOneFloor,
      editUpOneLevel, editDownOneLevel, editAddLevel, editRemoveLevel,
      editResizeLevel, editToggleLayer, editMazePreferences;
  private JMenuItem playNewGame, playPlay, playEdit, playRegisterCharacter,
      playUnregisterCharacter, playRemoveCharacter;
  private JMenuItem gameEquipment, gameInventory, gameUse, gameReset,
      gameShowScore, gameShowTable, gameEditNote, gameViewStats,
      gameChangeLeader;
  private JMenuItem debugResetPreferences;
  private JMenuItem helpAbout, helpGeneralHelp, helpObjectHelp;
  private KeyStroke fileNewAccel, fileOpenAccel, fileCloseAccel, fileSaveAccel,
      fileSaveAsAccel;
  private KeyStroke editPreferencesAccel, editUndoAccel, editRedoAccel,
      editCutLevelAccel, editCopyLevelAccel, editPasteLevelAccel,
      editInsertLevelFromClipboardAccel, editClearHistoryAccel, editGoToAccel,
      editUpOneFloorAccel, editDownOneFloorAccel, editUpOneLevelAccel,
      editDownOneLevelAccel, editToggleLayerAccel;
  private KeyStroke playPlayMazeAccel, playEditMazeAccel;
  private KeyStroke gameNewGameAccel, gameInventoryAccel, gameUseAccel,
      gameResetAccel, gameShowScoreAccel, gameShowTableAccel;
  private final EventHandler handler;

  // Constructors
  public MenuManager(final JMenuBar menuBar) {
    this.mainMenuBar = menuBar;
    this.handler = new EventHandler();
    this.createAccelerators();
    this.createMenus();
    this.setInitialMenuState();
  }

  // Methods
  public void appendNewMenu(final JMenu newMenu) {
    this.mainMenuBar.add(newMenu);
  }

  public void setGameMenus() {
    this.fileNew.setEnabled(false);
    this.fileOpen.setEnabled(false);
    this.fileExit.setEnabled(true);
    this.editUndo.setEnabled(false);
    this.editRedo.setEnabled(false);
    this.editCutLevel.setEnabled(false);
    this.editCopyLevel.setEnabled(false);
    this.editPasteLevel.setEnabled(false);
    this.editInsertLevelFromClipboard.setEnabled(false);
    this.editPreferences.setEnabled(true);
    this.editClearHistory.setEnabled(false);
    this.editGoTo.setEnabled(false);
    this.editUpOneFloor.setEnabled(false);
    this.editDownOneFloor.setEnabled(false);
    this.editUpOneLevel.setEnabled(false);
    this.editDownOneLevel.setEnabled(false);
    this.editAddLevel.setEnabled(false);
    this.editRemoveLevel.setEnabled(false);
    this.editResizeLevel.setEnabled(false);
    this.editToggleLayer.setEnabled(false);
    this.editMazePreferences.setEnabled(false);
    this.gameEquipment.setEnabled(true);
    this.gameInventory.setEnabled(true);
    this.gameUse.setEnabled(true);
    this.gameReset.setEnabled(true);
    this.gameShowScore.setEnabled(true);
    this.gameShowTable.setEnabled(true);
    this.gameEditNote.setEnabled(true);
    this.gameViewStats.setEnabled(true);
    this.gameChangeLeader.setEnabled(true);
    this.checkFlags();
  }

  public void setEditorMenus() {
    this.fileNew.setEnabled(false);
    this.fileOpen.setEnabled(false);
    this.fileExit.setEnabled(true);
    this.editCutLevel.setEnabled(true);
    this.editCopyLevel.setEnabled(true);
    this.editPasteLevel.setEnabled(true);
    this.editInsertLevelFromClipboard.setEnabled(true);
    this.editPreferences.setEnabled(true);
    this.editGoTo.setEnabled(true);
    this.editResizeLevel.setEnabled(true);
    this.editToggleLayer.setEnabled(true);
    this.editMazePreferences.setEnabled(true);
    this.gameEquipment.setEnabled(false);
    this.gameInventory.setEnabled(false);
    this.gameUse.setEnabled(false);
    this.gameReset.setEnabled(false);
    this.gameShowScore.setEnabled(false);
    this.gameShowTable.setEnabled(false);
    this.gameEditNote.setEnabled(false);
    this.gameViewStats.setEnabled(false);
    this.gameChangeLeader.setEnabled(false);
    this.checkFlags();
  }

  public void setPrefMenus() {
    this.fileNew.setEnabled(false);
    this.fileOpen.setEnabled(false);
    this.fileClose.setEnabled(false);
    this.fileSave.setEnabled(false);
    this.fileSaveAs.setEnabled(false);
    this.fileExit.setEnabled(true);
    this.editUndo.setEnabled(false);
    this.editRedo.setEnabled(false);
    this.editCutLevel.setEnabled(false);
    this.editCopyLevel.setEnabled(false);
    this.editPasteLevel.setEnabled(false);
    this.editInsertLevelFromClipboard.setEnabled(false);
    this.editPreferences.setEnabled(false);
    this.editClearHistory.setEnabled(false);
    this.editGoTo.setEnabled(false);
    this.editUpOneFloor.setEnabled(false);
    this.editDownOneFloor.setEnabled(false);
    this.editUpOneLevel.setEnabled(false);
    this.editDownOneLevel.setEnabled(false);
    this.editAddLevel.setEnabled(false);
    this.editRemoveLevel.setEnabled(false);
    this.editResizeLevel.setEnabled(false);
    this.editToggleLayer.setEnabled(false);
    this.editMazePreferences.setEnabled(false);
    this.gameEquipment.setEnabled(false);
    this.gameInventory.setEnabled(false);
    this.gameUse.setEnabled(false);
    this.gameReset.setEnabled(false);
    this.gameShowScore.setEnabled(false);
    this.gameShowTable.setEnabled(false);
    this.gameEditNote.setEnabled(false);
    this.gameViewStats.setEnabled(false);
    this.gameChangeLeader.setEnabled(false);
  }

  public void setMainMenus() {
    this.fileClose.setEnabled(false);
    this.fileSave.setEnabled(false);
    this.fileSaveAs.setEnabled(false);
    this.playNewGame.setEnabled(true);
    this.playPlay.setEnabled(false);
    this.playEdit.setEnabled(false);
    this.playRegisterCharacter.setEnabled(true);
    this.playUnregisterCharacter.setEnabled(true);
    this.playRemoveCharacter.setEnabled(true);
    this.debugResetPreferences.setEnabled(true);
    this.helpAbout.setEnabled(true);
    this.helpObjectHelp.setEnabled(true);
    this.fileNew.setEnabled(true);
    this.fileOpen.setEnabled(true);
    this.fileExit.setEnabled(true);
    this.editUndo.setEnabled(false);
    this.editRedo.setEnabled(false);
    this.editCutLevel.setEnabled(false);
    this.editCopyLevel.setEnabled(false);
    this.editPasteLevel.setEnabled(false);
    this.editInsertLevelFromClipboard.setEnabled(false);
    this.editPreferences.setEnabled(true);
    this.editClearHistory.setEnabled(false);
    this.editGoTo.setEnabled(false);
    this.editUpOneFloor.setEnabled(false);
    this.editDownOneFloor.setEnabled(false);
    this.editUpOneLevel.setEnabled(false);
    this.editDownOneLevel.setEnabled(false);
    this.editAddLevel.setEnabled(false);
    this.editRemoveLevel.setEnabled(false);
    this.editResizeLevel.setEnabled(false);
    this.editToggleLayer.setEnabled(false);
    this.editMazePreferences.setEnabled(false);
    this.gameEquipment.setEnabled(false);
    this.gameInventory.setEnabled(false);
    this.gameUse.setEnabled(false);
    this.gameReset.setEnabled(false);
    this.gameShowScore.setEnabled(false);
    this.gameShowTable.setEnabled(false);
    this.gameEditNote.setEnabled(false);
    this.gameViewStats.setEnabled(false);
    this.gameChangeLeader.setEnabled(false);
    this.checkFlags();
  }

  public void enableUpOneFloor() {
    this.editUpOneFloor.setEnabled(true);
  }

  public void disableUpOneFloor() {
    this.editUpOneFloor.setEnabled(false);
  }

  public void enableDownOneFloor() {
    this.editDownOneFloor.setEnabled(true);
  }

  public void disableDownOneFloor() {
    this.editDownOneFloor.setEnabled(false);
  }

  public void enableUpOneLevel() {
    this.editUpOneLevel.setEnabled(true);
  }

  public void disableUpOneLevel() {
    this.editUpOneLevel.setEnabled(false);
  }

  public void enableDownOneLevel() {
    this.editDownOneLevel.setEnabled(true);
  }

  public void disableDownOneLevel() {
    this.editDownOneLevel.setEnabled(false);
  }

  public void enableAddLevel() {
    this.editAddLevel.setEnabled(true);
  }

  public void disableAddLevel() {
    this.editAddLevel.setEnabled(false);
  }

  public void enableRemoveLevel() {
    this.editRemoveLevel.setEnabled(true);
  }

  public void disableRemoveLevel() {
    this.editRemoveLevel.setEnabled(false);
  }

  public void enableUndo() {
    this.editUndo.setEnabled(true);
  }

  public void disableUndo() {
    this.editUndo.setEnabled(false);
  }

  public void enableRedo() {
    this.editRedo.setEnabled(true);
  }

  public void disableRedo() {
    this.editRedo.setEnabled(false);
  }

  public void enableClearHistory() {
    this.editClearHistory.setEnabled(true);
  }

  public void disableClearHistory() {
    this.editClearHistory.setEnabled(false);
  }

  public void enableCutLevel() {
    this.editCutLevel.setEnabled(true);
  }

  public void disableCutLevel() {
    this.editCutLevel.setEnabled(false);
  }

  public void enablePasteLevel() {
    this.editPasteLevel.setEnabled(true);
  }

  public void disablePasteLevel() {
    this.editPasteLevel.setEnabled(false);
  }

  public void enableInsertLevelFromClipboard() {
    this.editInsertLevelFromClipboard.setEnabled(true);
  }

  public void disableInsertLevelFromClipboard() {
    this.editInsertLevelFromClipboard.setEnabled(false);
  }

  public void checkFlags() {
    if (FileStateManager.getDirty()) {
      this.setMenusDirtyOn();
    } else {
      this.setMenusDirtyOff();
    }
    if (FileStateManager.getLoaded()) {
      this.setMenusLoadedOn();
    } else {
      this.setMenusLoadedOff();
    }
  }

  private void setMenusDirtyOn() {
    this.fileSave.setEnabled(true);
  }

  private void setMenusDirtyOff() {
    this.fileSave.setEnabled(false);
  }

  private void setMenusLoadedOn() {
    final BagOStuff app = FantastleReboot.getBagOStuff();
    if (app.getMode() == BagOStuff.STATUS_GUI) {
      this.fileClose.setEnabled(false);
      this.fileSaveAs.setEnabled(false);
      this.playPlay.setEnabled(true);
      this.playEdit.setEnabled(true);
    } else {
      this.fileClose.setEnabled(true);
      this.fileSaveAs.setEnabled(true);
      this.playPlay.setEnabled(false);
      this.playEdit.setEnabled(false);
    }
  }

  private void setMenusLoadedOff() {
    this.fileClose.setEnabled(false);
    this.fileSaveAs.setEnabled(false);
    this.playPlay.setEnabled(false);
    this.playEdit.setEnabled(false);
  }

  private void createAccelerators() {
    int modKey;
    if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
      modKey = InputEvent.META_DOWN_MASK;
    } else {
      modKey = InputEvent.CTRL_DOWN_MASK;
    }
    this.fileNewAccel = KeyStroke.getKeyStroke(KeyEvent.VK_N, modKey);
    this.fileOpenAccel = KeyStroke.getKeyStroke(KeyEvent.VK_O, modKey);
    this.fileCloseAccel = KeyStroke.getKeyStroke(KeyEvent.VK_W, modKey);
    this.fileSaveAccel = KeyStroke.getKeyStroke(KeyEvent.VK_S, modKey);
    this.fileSaveAsAccel = KeyStroke.getKeyStroke(KeyEvent.VK_S,
        modKey | InputEvent.SHIFT_DOWN_MASK);
    this.editUndoAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Z, modKey);
    this.editRedoAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
        modKey | InputEvent.SHIFT_DOWN_MASK);
    this.editCutLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_X, modKey);
    this.editCopyLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_C, modKey);
    this.editPasteLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_V, modKey);
    this.editInsertLevelFromClipboardAccel = KeyStroke
        .getKeyStroke(KeyEvent.VK_F, modKey);
    this.editPreferencesAccel = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA,
        modKey);
    this.editClearHistoryAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Y, modKey);
    this.editGoToAccel = KeyStroke.getKeyStroke(KeyEvent.VK_G,
        modKey | InputEvent.SHIFT_DOWN_MASK);
    this.playPlayMazeAccel = KeyStroke.getKeyStroke(KeyEvent.VK_P, modKey);
    this.playEditMazeAccel = KeyStroke.getKeyStroke(KeyEvent.VK_E, modKey);
    this.gameNewGameAccel = KeyStroke.getKeyStroke(KeyEvent.VK_N,
        modKey | InputEvent.SHIFT_DOWN_MASK);
    this.gameInventoryAccel = KeyStroke.getKeyStroke(KeyEvent.VK_I, modKey);
    this.gameUseAccel = KeyStroke.getKeyStroke(KeyEvent.VK_U, modKey);
    this.gameResetAccel = KeyStroke.getKeyStroke(KeyEvent.VK_R, modKey);
    this.gameShowScoreAccel = KeyStroke.getKeyStroke(KeyEvent.VK_G, modKey);
    this.gameShowTableAccel = KeyStroke.getKeyStroke(KeyEvent.VK_T, modKey);
    this.editUpOneFloorAccel = KeyStroke.getKeyStroke(KeyEvent.VK_UP, modKey);
    this.editDownOneFloorAccel = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
        modKey);
    this.editUpOneLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_UP,
        modKey | InputEvent.SHIFT_DOWN_MASK);
    this.editDownOneLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
        modKey | InputEvent.SHIFT_DOWN_MASK);
    this.editToggleLayerAccel = KeyStroke.getKeyStroke(KeyEvent.VK_L, modKey);
  }

  private void createMenus() {
    this.fileMenu = new JMenu("File");
    this.editMenu = new JMenu("Edit");
    this.playMenu = new JMenu("Play");
    this.gameMenu = new JMenu("Game");
    this.debugMenu = new JMenu("Debug");
    this.helpMenu = new JMenu("Help");
    this.fileNew = new JMenuItem("New...");
    this.fileNew.setAccelerator(this.fileNewAccel);
    this.fileOpen = new JMenuItem("Open...");
    this.fileOpen.setAccelerator(this.fileOpenAccel);
    this.fileClose = new JMenuItem("Close");
    this.fileClose.setAccelerator(this.fileCloseAccel);
    this.fileSave = new JMenuItem("Save");
    this.fileSave.setAccelerator(this.fileSaveAccel);
    this.fileSaveAs = new JMenuItem("Save As...");
    this.fileSaveAs.setAccelerator(this.fileSaveAsAccel);
    this.fileExit = new JMenuItem("Exit");
    this.editUndo = new JMenuItem("Undo");
    this.editUndo.setAccelerator(this.editUndoAccel);
    this.editRedo = new JMenuItem("Redo");
    this.editRedo.setAccelerator(this.editRedoAccel);
    this.editCutLevel = new JMenuItem("Cut Level");
    this.editCutLevel.setAccelerator(this.editCutLevelAccel);
    this.editCopyLevel = new JMenuItem("Copy Level");
    this.editCopyLevel.setAccelerator(this.editCopyLevelAccel);
    this.editPasteLevel = new JMenuItem("Paste Level");
    this.editPasteLevel.setAccelerator(this.editPasteLevelAccel);
    this.editInsertLevelFromClipboard = new JMenuItem(
        "Insert Level From Clipboard");
    this.editInsertLevelFromClipboard
        .setAccelerator(this.editInsertLevelFromClipboardAccel);
    this.editPreferences = new JMenuItem("Preferences...");
    this.editPreferences.setAccelerator(this.editPreferencesAccel);
    this.editClearHistory = new JMenuItem("Clear History");
    this.editClearHistory.setAccelerator(this.editClearHistoryAccel);
    this.editGoTo = new JMenuItem("Go To...");
    this.editGoTo.setAccelerator(this.editGoToAccel);
    this.editUpOneFloor = new JMenuItem("Up One Floor");
    this.editUpOneFloor.setAccelerator(this.editUpOneFloorAccel);
    this.editDownOneFloor = new JMenuItem("Down One Floor");
    this.editDownOneFloor.setAccelerator(this.editDownOneFloorAccel);
    this.editUpOneLevel = new JMenuItem("Up One Level");
    this.editUpOneLevel.setAccelerator(this.editUpOneLevelAccel);
    this.editDownOneLevel = new JMenuItem("Down One Level");
    this.editDownOneLevel.setAccelerator(this.editDownOneLevelAccel);
    this.editAddLevel = new JMenuItem("Add a Level...");
    this.editRemoveLevel = new JMenuItem("Remove a Level...");
    this.editResizeLevel = new JMenuItem("Resize Current Level...");
    this.editToggleLayer = new JMenuItem("Toggle Layer");
    this.editToggleLayer.setAccelerator(this.editToggleLayerAccel);
    this.editMazePreferences = new JMenuItem("Maze Preferences...");
    this.playNewGame = new JMenuItem("New Game");
    this.playNewGame.setAccelerator(this.gameNewGameAccel);
    this.playPlay = new JMenuItem("Play");
    this.playPlay.setAccelerator(this.playPlayMazeAccel);
    this.playEdit = new JMenuItem("Edit");
    this.playEdit.setAccelerator(this.playEditMazeAccel);
    this.playRegisterCharacter = new JMenuItem("Register Character...");
    this.playUnregisterCharacter = new JMenuItem("Unregister Character...");
    this.playRemoveCharacter = new JMenuItem("Remove Character...");
    this.gameEquipment = new JMenuItem("Show Equipment...");
    this.gameInventory = new JMenuItem("Show Inventory...");
    this.gameInventory.setAccelerator(this.gameInventoryAccel);
    this.gameUse = new JMenuItem("Use an Item...");
    this.gameUse.setAccelerator(this.gameUseAccel);
    this.gameReset = new JMenuItem("Reset Current Level");
    this.gameReset.setAccelerator(this.gameResetAccel);
    this.gameShowScore = new JMenuItem("Show Current Score");
    this.gameShowScore.setAccelerator(this.gameShowScoreAccel);
    this.gameShowTable = new JMenuItem("Show Score Table");
    this.gameShowTable.setAccelerator(this.gameShowTableAccel);
    this.gameEditNote = new JMenuItem("Edit Note...");
    this.gameViewStats = new JMenuItem("View Statistics...");
    this.gameChangeLeader = new JMenuItem("Change Party Leader...");
    this.debugResetPreferences = new JMenuItem("Reset Preferences");
    this.helpAbout = new JMenuItem("About Fantastle...");
    this.helpGeneralHelp = new JMenuItem("Fantastle Help");
    this.helpObjectHelp = new JMenuItem("Fantastle Object Help");
    this.fileNew.addActionListener(this.handler);
    this.fileOpen.addActionListener(this.handler);
    this.fileClose.addActionListener(this.handler);
    this.fileSave.addActionListener(this.handler);
    this.fileSaveAs.addActionListener(this.handler);
    this.fileExit.addActionListener(this.handler);
    this.editUndo.addActionListener(this.handler);
    this.editRedo.addActionListener(this.handler);
    this.editCutLevel.addActionListener(this.handler);
    this.editCopyLevel.addActionListener(this.handler);
    this.editPasteLevel.addActionListener(this.handler);
    this.editInsertLevelFromClipboard.addActionListener(this.handler);
    this.editPreferences.addActionListener(this.handler);
    this.editClearHistory.addActionListener(this.handler);
    this.editGoTo.addActionListener(this.handler);
    this.editUpOneFloor.addActionListener(this.handler);
    this.editDownOneFloor.addActionListener(this.handler);
    this.editUpOneLevel.addActionListener(this.handler);
    this.editDownOneLevel.addActionListener(this.handler);
    this.editAddLevel.addActionListener(this.handler);
    this.editRemoveLevel.addActionListener(this.handler);
    this.editResizeLevel.addActionListener(this.handler);
    this.editToggleLayer.addActionListener(this.handler);
    this.editMazePreferences.addActionListener(this.handler);
    this.playPlay.addActionListener(this.handler);
    this.playEdit.addActionListener(this.handler);
    this.gameEquipment.addActionListener(this.handler);
    this.gameInventory.addActionListener(this.handler);
    this.gameUse.addActionListener(this.handler);
    this.gameReset.addActionListener(this.handler);
    this.gameShowScore.addActionListener(this.handler);
    this.gameShowTable.addActionListener(this.handler);
    this.gameChangeLeader.addActionListener(this.handler);
    this.playNewGame.addActionListener(this.handler);
    this.playRegisterCharacter.addActionListener(this.handler);
    this.playUnregisterCharacter.addActionListener(this.handler);
    this.playRemoveCharacter.addActionListener(this.handler);
    this.gameEditNote.addActionListener(this.handler);
    this.gameViewStats.addActionListener(this.handler);
    this.debugResetPreferences.addActionListener(this.handler);
    this.helpAbout.addActionListener(this.handler);
    this.helpGeneralHelp.addActionListener(this.handler);
    this.helpObjectHelp.addActionListener(this.handler);
    this.fileMenu.add(this.fileNew);
    this.fileMenu.add(this.fileOpen);
    this.fileMenu.add(this.fileClose);
    this.fileMenu.add(this.fileSave);
    this.fileMenu.add(this.fileSaveAs);
    if (!System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
      this.fileMenu.add(this.fileExit);
    }
    this.editMenu.add(this.editUndo);
    this.editMenu.add(this.editRedo);
    this.editMenu.add(this.editCutLevel);
    this.editMenu.add(this.editCopyLevel);
    this.editMenu.add(this.editPasteLevel);
    this.editMenu.add(this.editInsertLevelFromClipboard);
    if (!System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
      this.editMenu.add(this.editPreferences);
    }
    this.editMenu.add(this.editClearHistory);
    this.editMenu.add(this.editGoTo);
    this.editMenu.addSeparator();
    this.editMenu.add(this.editUpOneFloor);
    this.editMenu.add(this.editDownOneFloor);
    this.editMenu.add(this.editUpOneLevel);
    this.editMenu.add(this.editDownOneLevel);
    this.editMenu.add(this.editAddLevel);
    this.editMenu.add(this.editRemoveLevel);
    this.editMenu.add(this.editResizeLevel);
    this.editMenu.add(this.editToggleLayer);
    this.editMenu.add(this.editMazePreferences);
    this.playMenu.add(this.playNewGame);
    this.playMenu.add(this.playPlay);
    this.playMenu.add(this.playEdit);
    this.playMenu.add(this.playRegisterCharacter);
    this.playMenu.add(this.playUnregisterCharacter);
    this.playMenu.add(this.playRemoveCharacter);
    this.gameMenu.add(this.gameEquipment);
    this.gameMenu.add(this.gameInventory);
    this.gameMenu.add(this.gameUse);
    this.gameMenu.add(this.gameReset);
    this.gameMenu.add(this.gameShowScore);
    this.gameMenu.add(this.gameShowTable);
    this.gameMenu.add(this.gameEditNote);
    this.gameMenu.add(this.gameViewStats);
    this.gameMenu.add(this.gameChangeLeader);
    this.debugMenu.add(this.debugResetPreferences);
    if (!System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
      this.helpMenu.add(this.helpAbout);
    }
    this.helpMenu.add(this.helpGeneralHelp);
    this.helpMenu.add(this.helpObjectHelp);
    this.mainMenuBar.add(this.fileMenu);
    this.mainMenuBar.add(this.editMenu);
    this.mainMenuBar.add(this.playMenu);
    this.mainMenuBar.add(this.gameMenu);
    this.mainMenuBar.add(this.debugMenu);
    this.mainMenuBar.add(this.helpMenu);
  }

  private void setInitialMenuState() {
    this.fileNew.setEnabled(false);
    this.fileOpen.setEnabled(false);
    this.fileClose.setEnabled(false);
    this.fileSave.setEnabled(false);
    this.fileSaveAs.setEnabled(false);
    this.fileExit.setEnabled(false);
    this.editUndo.setEnabled(false);
    this.editRedo.setEnabled(false);
    this.editCutLevel.setEnabled(false);
    this.editCopyLevel.setEnabled(false);
    this.editPasteLevel.setEnabled(false);
    this.editInsertLevelFromClipboard.setEnabled(false);
    this.editPreferences.setEnabled(false);
    this.editClearHistory.setEnabled(false);
    this.editGoTo.setEnabled(false);
    this.editUpOneFloor.setEnabled(false);
    this.editDownOneFloor.setEnabled(false);
    this.editUpOneLevel.setEnabled(false);
    this.editDownOneLevel.setEnabled(false);
    this.editAddLevel.setEnabled(false);
    this.editRemoveLevel.setEnabled(false);
    this.editResizeLevel.setEnabled(false);
    this.editToggleLayer.setEnabled(false);
    this.editMazePreferences.setEnabled(false);
    this.playNewGame.setEnabled(false);
    this.playPlay.setEnabled(false);
    this.playEdit.setEnabled(false);
    this.playRegisterCharacter.setEnabled(false);
    this.playUnregisterCharacter.setEnabled(false);
    this.playRemoveCharacter.setEnabled(false);
    this.gameEquipment.setEnabled(false);
    this.gameInventory.setEnabled(false);
    this.gameUse.setEnabled(false);
    this.gameReset.setEnabled(false);
    this.gameShowScore.setEnabled(false);
    this.gameShowTable.setEnabled(false);
    this.gameEditNote.setEnabled(false);
    this.gameViewStats.setEnabled(false);
    this.gameChangeLeader.setEnabled(false);
    this.debugResetPreferences.setEnabled(false);
    this.helpAbout.setEnabled(false);
    this.helpObjectHelp.setEnabled(false);
  }

  private class EventHandler implements ActionListener {
    public EventHandler() {
      // Do nothing
    }

    // Handle menus
    @Override
    public void actionPerformed(final ActionEvent e) {
      try {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        boolean loaded = false;
        final String cmd = e.getActionCommand();
        if (cmd.equals("New...")) {
          loaded = Editor.newMaze();
          FileStateManager.setLoaded(loaded);
        } else if (cmd.equals("Open...")) {
          loaded = MazeFileManager.loadGame();
          FileStateManager.setLoaded(loaded);
        } else if (cmd.equals("Close")) {
          // Close the window
          if (app.getMode() == BagOStuff.STATUS_EDITOR) {
            Editor.handleCloseWindow();
          } else if (app.getMode() == BagOStuff.STATUS_GAME) {
            boolean saved = true;
            int status = 0;
            if (FileStateManager.getDirty()) {
              status = FileStateManager.showSaveDialog();
              if (status == JOptionPane.YES_OPTION) {
                saved = MazeFileManager.saveGame();
              } else if (status == JOptionPane.CANCEL_OPTION) {
                saved = false;
              } else {
                FileStateManager.setDirty(false);
              }
            }
            if (saved) {
              Game.hideOutput();
              app.getGUIManager().showGUI();
            }
          }
        } else if (cmd.equals("Save")) {
          if (FileStateManager.getLoaded()) {
            MazeFileManager.saveGame();
          } else {
            CommonDialogs.showDialog("No Maze Opened");
          }
        } else if (cmd.equals("Exit")) {
          // Exit program
          System.exit(0);
        } else if (cmd.equals("Undo")) {
          // Undo most recent action
          Editor.undo();
        } else if (cmd.equals("Redo")) {
          // Redo most recent undone action
          Editor.redo();
        } else if (cmd.equals("Preferences...")) {
          // Show preferences dialog
          Prefs.showPrefs();
        } else if (cmd.equals("Clear History")) {
          // Clear undo/redo history, confirm first
          final int res = CommonDialogs.showConfirmDialog(
              "Are you sure you want to clear the history?", "Editor");
          if (res == JOptionPane.YES_OPTION) {
            Editor.clearHistory();
          }
        } else if (cmd.equals("Go To...")) {
          // Go To
          Editor.goToHandler();
        } else if (cmd.equals("Up One Floor")) {
          // Go up one floor
          Editor.updateEditorPosition(0, 0, 1, 0);
        } else if (cmd.equals("Down One Floor")) {
          // Go down one floor
          Editor.updateEditorPosition(0, 0, -1, 0);
        } else if (cmd.equals("Up One Level")) {
          // Go up one level
          Editor.updateEditorPosition(0, 0, 0, 1);
        } else if (cmd.equals("Down One Level")) {
          // Go down one level
          Editor.updateEditorPosition(0, 0, 0, -1);
        } else if (cmd.equals("Add a Level...")) {
          // Add a level
          Editor.addLevel();
        } else if (cmd.equals("Toggle Layer")) {
          // Toggle current layer
          Editor.toggleLayer();
        } else if (cmd.equals("New Game")) {
          // Start a new game
          final boolean proceed = Game.newGame();
          if (proceed) {
            new GenerateTask(true).start();
            Editor.mazeChanged();
          }
        } else if (cmd.equals("Play")) {
          // Play the current maze
          Game.playMaze();
        } else if (cmd.equals("Edit")) {
          // Edit the current maze
          Editor.editMaze();
        } else if (cmd.equals("Show Equipment...")) {
          if (!Game.usingAnItem()) {
            InventoryViewer.showEquipmentDialog();
          }
        } else if (cmd.equals("Reset Current Level")) {
          if (!Game.usingAnItem()) {
            final int result = CommonDialogs.showConfirmDialog(
                "Are you sure you want to reset the current level?",
                "Fantastle");
            if (result == JOptionPane.YES_OPTION) {
              Game.resetCurrentLevel();
            }
          }
        } else if (cmd.equals("Show Current Score")) {
          Game.showCurrentScore();
        } else if (cmd.equals("Show Score Table")) {
          Game.showScoreTable();
        } else if (cmd.equals("Register Character...")) {
          // Register Character
          CharacterRegistration.registerCharacter();
        } else if (cmd.equals("Unregister Character...")) {
          // Unregister Character
          CharacterRegistration.unregisterCharacter();
        } else if (cmd.equals("Remove Character...")) {
          // Confirm
          final int confirm = CommonDialogs.showConfirmDialog(
              "WARNING: This will DELETE the character from disk,\n"
                  + "and CANNOT be undone! Proceed anyway?",
              "Remove Character");
          if (confirm == CommonDialogs.YES_OPTION) {
            // Remove Character
            CharacterRegistration.removeCharacter();
          }
        } else if (cmd.equals("Edit Note...")) {
          // Edit Note
          NoteManager.editNote();
        } else if (cmd.equals("View Statistics...")) {
          // View Statistics
          StatisticsViewer.viewStatistics();
        } else if (cmd.equals("Change Party Leader...")) {
          // Change Party Leader
          PartyManager.getParty().pickLeader();
        } else if (cmd.equals("Reset Preferences")) {
          app.resetPreferences();
          CommonDialogs.showDialog("Preferences reset to defaults.");
        } else if (cmd.equals("About Fantastle...")) {
          app.getAboutDialog().showAboutDialog();
        } else if (cmd.equals("Fantastle Help")) {
          app.getGeneralHelpManager().showHelp();
        }
        MenuManager.this.checkFlags();
      } catch (final Exception ex) {
        FantastleReboot.logError(ex);
      }
    }
  }
}
