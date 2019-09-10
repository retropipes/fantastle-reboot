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
package com.puttysoftware.fantastlereboot.maze;

import java.awt.desktop.OpenFilesEvent;
import java.awt.desktop.OpenFilesHandler;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.PreferencesManager;
import com.puttysoftware.fantastlereboot.generic.MazeObject;
import com.puttysoftware.fantastlereboot.utilities.FormatConstants;

public class MazeManager implements OpenFilesHandler, QuitHandler {
    // Fields
    private Maze gameMaze;
    private boolean loaded, isDirty;
    private String scoresFileName;
    private String lastUsedMazeFile;
    private String lastUsedGameFile;
    private boolean maze3CompatibleModeEnabled;
    private boolean maze4CompatibleModeEnabled;

    // Constructors
    public MazeManager() {
        this.loaded = false;
        this.isDirty = false;
        this.lastUsedMazeFile = "";
        this.lastUsedGameFile = "";
        this.scoresFileName = "";
        this.maze3CompatibleModeEnabled = false;
        this.maze4CompatibleModeEnabled = false;
    }

    // Methods
    public boolean maze3Compatible() {
        return this.maze3CompatibleModeEnabled;
    }

    public boolean maze4Compatible() {
        return this.maze4CompatibleModeEnabled;
    }

    public Maze getMaze() {
        return this.gameMaze;
    }

    public void setMaze(final Maze newMaze) {
        this.gameMaze = newMaze;
    }

    public void handleDeferredSuccess(final boolean value) {
        if (value) {
            this.setLoaded(true);
            this.setDirty(false);
        } else {
            this.setLoaded(false);
            this.setDirty(false);
        }
        this.maze3CompatibleModeEnabled = false;
        this.maze4CompatibleModeEnabled = false;
        FantastleReboot.getBagOStuff().getMenuManager().checkFlags();
    }

    public MazeObject getMazeObject(final int x, final int y, final int z,
            final int w, final int e) {
        try {
            return this.gameMaze.getCell(x, y, z, w, e);
        } catch (final ArrayIndexOutOfBoundsException ae) {
            return null;
        }
    }

    @Override
    public void handleQuitRequestWith(QuitEvent inE, QuitResponse inResponse) {
        boolean saved = true;
        int status = JOptionPane.DEFAULT_OPTION;
        if (this.getDirty()) {
            status = this.showSaveDialog();
            if (status == JOptionPane.YES_OPTION) {
                saved = this.saveMaze();
            } else if (status == JOptionPane.CANCEL_OPTION) {
                saved = false;
            } else {
                this.setDirty(false);
            }
        }
        if (saved) {
            FantastleReboot.getBagOStuff().getPrefsManager().writePrefs();
            inResponse.performQuit();
        } else {
            inResponse.cancelQuit();
        }
    }

    public int showSaveDialog() {
        String type, source;
        final BagOStuff app = FantastleReboot.getBagOStuff();
        final int mode = app.getMode();
        if (mode == BagOStuff.STATUS_EDITOR) {
            type = "maze";
            source = "Editor";
        } else {
            type = "game";
            source = "Fantastle";
        }
        int status = JOptionPane.DEFAULT_OPTION;
        status = Messager.showYNCConfirmDialog(
                "Do you want to save your " + type + "?", source);
        return status;
    }

    public boolean getLoaded() {
        return this.loaded;
    }

    public void setLoaded(final boolean status) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        this.loaded = status;
        app.getMenuManager().checkFlags();
    }

    public boolean getDirty() {
        return this.isDirty;
    }

    public void setDirty(final boolean newDirty) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        this.isDirty = newDirty;
        app.getMenuManager().checkFlags();
    }

    public void clearLastUsedFilenames() {
        this.lastUsedMazeFile = "";
        this.lastUsedGameFile = "";
    }

    public String getLastUsedMaze() {
        return this.lastUsedMazeFile;
    }

    public String getLastUsedGame() {
        return this.lastUsedGameFile;
    }

    public void setLastUsedMaze(final String newFile) {
        this.lastUsedMazeFile = newFile;
    }

    public void setLastUsedGame(final String newFile) {
        this.lastUsedGameFile = newFile;
    }

    public String getScoresFileName() {
        return this.scoresFileName;
    }

    public void setScoresFileName(final String filename) {
        this.scoresFileName = filename;
    }

    public boolean loadMaze() {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        int status = 0;
        boolean saved = true;
        String filename, extension;
        final String lastOpen = app.getPrefsManager().getLastDirOpen();
        File lastOpenDir = null;
        if (lastOpen != null) {
            lastOpenDir = new File(lastOpen);
        }
        final JFileChooser fc = new JFileChooser(lastOpenDir);
        final Maze2Filter m2f = new Maze2Filter();
        final Maze3Filter m3f = new Maze3Filter();
        final Maze4Filter m4f = new Maze4Filter();
        final Maze5Filter m5f = new Maze5Filter();
        final GameFilter gf = new GameFilter();
        if (this.getDirty()) {
            status = this.showSaveDialog();
            if (status == JOptionPane.YES_OPTION) {
                saved = this.saveMaze();
            } else if (status == JOptionPane.CANCEL_OPTION) {
                saved = false;
            } else {
                this.setDirty(false);
            }
        }
        if (saved) {
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(m2f);
            fc.addChoosableFileFilter(m3f);
            fc.addChoosableFileFilter(m4f);
            fc.addChoosableFileFilter(m5f);
            fc.addChoosableFileFilter(gf);
            final int filter = app.getPrefsManager().getLastFilterUsedIndex();
            FantastleReboot.getBagOStuff().getPrefsManager();
            if (filter == PreferencesManager.FILTER_GAME) {
                fc.setFileFilter(gf);
            } else {
                FantastleReboot.getBagOStuff().getPrefsManager();
                if (filter == PreferencesManager.FILTER_MAZE_V2) {
                    fc.setFileFilter(m2f);
                } else {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    if (filter == PreferencesManager.FILTER_MAZE_V3) {
                        fc.setFileFilter(m3f);
                    } else {
                        FantastleReboot.getBagOStuff().getPrefsManager();
                        if (filter == PreferencesManager.FILTER_MAZE_V4) {
                            fc.setFileFilter(m4f);
                        } else {
                            fc.setFileFilter(m5f);
                        }
                    }
                }
            }
            final int returnVal = fc.showOpenDialog(app.getOutputFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File file = fc.getSelectedFile();
                final FileFilter ff = fc.getFileFilter();
                if (ff.getDescription().equals(gf.getDescription())) {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    app.getPrefsManager().setLastFilterUsedIndex(
                            PreferencesManager.FILTER_GAME);
                } else if (ff.getDescription().equals(m2f.getDescription())) {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    app.getPrefsManager().setLastFilterUsedIndex(
                            PreferencesManager.FILTER_MAZE_V2);
                } else if (ff.getDescription().equals(m3f.getDescription())) {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    app.getPrefsManager().setLastFilterUsedIndex(
                            PreferencesManager.FILTER_MAZE_V3);
                } else if (ff.getDescription().equals(m4f.getDescription())) {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    app.getPrefsManager().setLastFilterUsedIndex(
                            PreferencesManager.FILTER_MAZE_V4);
                } else {
                    FantastleReboot.getBagOStuff().getPrefsManager();
                    app.getPrefsManager().setLastFilterUsedIndex(
                            PreferencesManager.FILTER_MAZE_V5);
                }
                app.getPrefsManager().setLastDirOpen(
                        fc.getCurrentDirectory().getAbsolutePath());
                filename = file.getAbsolutePath();
                extension = MazeManager.getExtension(file);
                app.getGameManager().resetObjectInventory();
                if (extension.equals(Extension.getGameExtension())) {
                    this.lastUsedGameFile = filename;
                    this.loadFile(filename, true,
                            FormatConstants.MAZE_FORMAT_5);
                } else if (extension.equals(Extension.getMaze2Extension())
                        || extension.equals(Extension.getMaze3Extension())) {
                    this.lastUsedMazeFile = filename;
                    this.scoresFileName = MazeManager
                            .getNameWithoutExtension(file.getName());
                    this.loadFile(filename, false,
                            FormatConstants.MAZE_FORMAT_3);
                } else if (extension.equals(Extension.getMaze4Extension())) {
                    this.lastUsedMazeFile = filename;
                    this.scoresFileName = MazeManager
                            .getNameWithoutExtension(file.getName());
                    this.loadFile(filename, false,
                            FormatConstants.MAZE_FORMAT_4);
                } else if (extension.equals(Extension.getMaze5Extension())) {
                    this.lastUsedMazeFile = filename;
                    this.scoresFileName = MazeManager
                            .getNameWithoutExtension(file.getName());
                    this.loadFile(filename, false,
                            FormatConstants.MAZE_FORMAT_5);
                } else {
                    Messager.showDialog(
                            "You opened something other than a maze file. Select a maze file, and try again.");
                }
            } else {
                // User cancelled
                if (this.loaded) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void openFiles(OpenFilesEvent inE) {
        final String infilename = inE.getFiles().get(0).getAbsolutePath();
        final BagOStuff app = FantastleReboot.getBagOStuff();
        if (!this.loaded) {
            String extension;
            final File file = new File(infilename);
            final String filename = file.getAbsolutePath();
            extension = MazeManager.getExtension(file);
            app.getGameManager().resetObjectInventory();
            if (extension.equals(Extension.getGameExtension())) {
                this.lastUsedGameFile = filename;
                this.loadFile(filename, true, FormatConstants.MAZE_FORMAT_5);
            } else if (extension.equals(Extension.getMaze2Extension())
                    || extension.equals(Extension.getMaze3Extension())) {
                this.lastUsedMazeFile = filename;
                this.scoresFileName = MazeManager
                        .getNameWithoutExtension(file.getName());
                this.loadFile(filename, false, FormatConstants.MAZE_FORMAT_3);
            } else if (extension.equals(Extension.getMaze4Extension())) {
                this.lastUsedMazeFile = filename;
                this.scoresFileName = MazeManager
                        .getNameWithoutExtension(file.getName());
                this.loadFile(filename, false, FormatConstants.MAZE_FORMAT_4);
            } else if (extension.equals(Extension.getMaze5Extension())) {
                this.lastUsedMazeFile = filename;
                this.scoresFileName = MazeManager
                        .getNameWithoutExtension(file.getName());
                this.loadFile(filename, false, FormatConstants.MAZE_FORMAT_5);
            } else if (extension.equals(Extension.getScoresExtension())) {
                Messager.showDialog(
                        "You double-clicked a scores file. These are automatically loaded when their associated maze is loaded, and need not be double-clicked.");
            } else if (extension.equals(Extension.getPreferencesExtension())) {
                Messager.showDialog(
                        "You double-clicked a preferences file. These are automatically loaded when the program is loaded, and need not be double-clicked.");
            }
        }
    }

    private void loadFile(final String filename, final boolean isSavedGame,
            final int formatVersion) {
        if (formatVersion == FormatConstants.MAZE_FORMAT_5) {
            this.maze3CompatibleModeEnabled = false;
            this.maze4CompatibleModeEnabled = false;
            final LoadTask5 lt5 = new LoadTask5(filename, isSavedGame);
            lt5.start();
        } else if (formatVersion == FormatConstants.MAZE_FORMAT_4) {
            this.maze3CompatibleModeEnabled = false;
            this.maze4CompatibleModeEnabled = true;
            final LoadTask4 lt4 = new LoadTask4(filename);
            lt4.start();
        } else {
            this.maze3CompatibleModeEnabled = true;
            this.maze4CompatibleModeEnabled = false;
            final LoadTask3 lt3 = new LoadTask3(filename);
            lt3.start();
        }
    }

    public boolean saveMaze() {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        if (app.getMode() == BagOStuff.STATUS_GAME) {
            if (this.lastUsedGameFile != null
                    && !this.lastUsedGameFile.equals("")) {
                final String extension = MazeManager
                        .getExtension(this.lastUsedGameFile);
                if (extension != null) {
                    if (!extension.equals(Extension.getGameExtension())) {
                        this.lastUsedGameFile = MazeManager
                                .getNameWithoutExtension(this.lastUsedGameFile)
                                + Extension.getGameExtensionWithPeriod();
                    }
                } else {
                    this.lastUsedGameFile += Extension
                            .getGameExtensionWithPeriod();
                }
                MazeManager.saveFile(this.lastUsedGameFile, true);
            } else {
                return this.saveMazeAs();
            }
        } else {
            if (this.lastUsedMazeFile != null
                    && !this.lastUsedMazeFile.equals("")) {
                final String extension = MazeManager
                        .getExtension(this.lastUsedMazeFile);
                if (extension != null) {
                    if (!extension.equals(Extension.getGameExtension())) {
                        this.lastUsedMazeFile = MazeManager
                                .getNameWithoutExtension(this.lastUsedMazeFile)
                                + Extension.getMaze5ExtensionWithPeriod();
                    }
                } else {
                    this.lastUsedMazeFile += Extension
                            .getMaze5ExtensionWithPeriod();
                }
                MazeManager.saveFile(this.lastUsedMazeFile, false);
            } else {
                return this.saveMazeAs();
            }
        }
        return false;
    }

    public boolean saveMazeAs() {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        String filename, extension;
        final String lastSave = app.getPrefsManager().getLastDirSave();
        File lastSaveDir = null;
        if (lastSave != null) {
            lastSaveDir = new File(lastSave);
        }
        final JFileChooser fc = new JFileChooser(lastSaveDir);
        final Maze5Filter bmf = new Maze5Filter();
        final GameFilter bsf = new GameFilter();
        fc.setAcceptAllFileFilterUsed(false);
        if (app.getMode() == BagOStuff.STATUS_GAME) {
            fc.addChoosableFileFilter(bsf);
            fc.setFileFilter(bsf);
        } else {
            fc.addChoosableFileFilter(bmf);
            fc.setFileFilter(bmf);
        }
        final int returnVal = fc.showSaveDialog(app.getOutputFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = fc.getSelectedFile();
            app.getPrefsManager()
                    .setLastDirSave(fc.getCurrentDirectory().getAbsolutePath());
            extension = MazeManager.getExtension(file);
            filename = file.getAbsolutePath();
            if (app.getMode() == BagOStuff.STATUS_GAME) {
                if (extension != null) {
                    if (!extension.equals(Extension.getGameExtension())) {
                        filename = MazeManager.getNameWithoutExtension(file)
                                + Extension.getGameExtensionWithPeriod();
                    }
                } else {
                    filename += Extension.getGameExtensionWithPeriod();
                }
                this.lastUsedGameFile = filename;
                MazeManager.saveFile(filename, true);
            } else {
                if (extension != null) {
                    if (!extension.equals(Extension.getMaze5Extension())) {
                        filename = MazeManager.getNameWithoutExtension(file)
                                + Extension.getMaze5ExtensionWithPeriod();
                    }
                } else {
                    filename += Extension.getMaze5ExtensionWithPeriod();
                }
                this.lastUsedMazeFile = filename;
                this.scoresFileName = MazeManager
                        .getNameWithoutExtension(file.getName());
                MazeManager.saveFile(filename, false);
            }
        }
        return false;
    }

    private static void saveFile(final String filename,
            final boolean isSavedGame) {
        final String sg;
        if (isSavedGame) {
            sg = "Saved Game";
        } else {
            sg = "Maze";
        }
        Messager.showMessage("Saving " + sg + " file...");
        final SaveTask st = new SaveTask(filename, isSavedGame);
        st.start();
    }

    private static String getExtension(final File f) {
        String ext = null;
        final String s = f.getName();
        final int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private static String getExtension(final String s) {
        String ext = null;
        final int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private static String getNameWithoutExtension(final File f) {
        String ext = null;
        final String s = f.getAbsolutePath();
        final int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(0, i);
        } else {
            ext = s;
        }
        return ext;
    }

    private static String getNameWithoutExtension(final String s) {
        String ext = null;
        final int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(0, i);
        } else {
            ext = s;
        }
        return ext;
    }
}
