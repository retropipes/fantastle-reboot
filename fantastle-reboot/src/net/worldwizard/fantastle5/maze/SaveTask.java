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
package net.worldwizard.fantastle5.maze;

import java.io.IOException;

import net.worldwizard.fantastle5.Application;
import net.worldwizard.fantastle5.Fantastle5;
import net.worldwizard.fantastle5.Messager;
import net.worldwizard.fantastle5.creatures.PCManager;
import net.worldwizard.io.DataConstants;
import net.worldwizard.io.DataWriter;

public class SaveTask extends Thread {
    // Fields
    private String filename;
    private final boolean isSavedGame;
    private static final byte FORMAT_VERSION_MAJOR = (byte) 5;
    private static final byte FORMAT_VERSION_MINOR = (byte) 0;

    // Constructors
    public SaveTask(final String file, final boolean saved) {
        this.filename = file;
        this.isSavedGame = saved;
    }

    @Override
    public void run() {
        final Application app = Fantastle5.getApplication();
        boolean success = true;
        final String sg;
        if (this.isSavedGame) {
            sg = "Saved Game";
        } else {
            sg = "Maze";
        }
        // filename check
        final boolean hasExtension = SaveTask.hasExtension(this.filename);
        if (!hasExtension) {
            if (this.isSavedGame) {
                this.filename += Extension.getGameExtensionWithPeriod();
            } else {
                this.filename += Extension.getMaze5ExtensionWithPeriod();
            }
        }
        try {
            final DataWriter mazeFile = new DataWriter(this.filename,
                    DataConstants.DATA_MODE_BINARY);
            SaveTask.writeFormatVersion(mazeFile);
            app.getMazeManager().getMaze().writeMaze(mazeFile);
            if (this.isSavedGame) {
                app.getGameManager().saveGameHook(mazeFile);
                PCManager.saveGameHook(mazeFile);
            } else {
                app.getGameManager().validateScore();
            }
            mazeFile.close();
        } catch (final Exception ex) {
            Messager.showDialog("Unknown error writing " + sg.toLowerCase()
                    + " file.");
            success = false;
        }
        if (success) {
            Messager.showMessage(sg + " file saved.");
        }
        app.getMazeManager().handleDeferredSuccess(success);
    }

    private static void writeFormatVersion(final DataWriter mazeFile)
            throws IOException {
        mazeFile.writeByte(SaveTask.FORMAT_VERSION_MAJOR);
        mazeFile.writeByte(SaveTask.FORMAT_VERSION_MINOR);
    }

    private static boolean hasExtension(final String s) {
        String ext = null;
        final int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        if (ext == null) {
            return false;
        } else {
            return true;
        }
    }
}