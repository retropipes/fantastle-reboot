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
package com.puttysoftware.fantastlereboot.obsolete.maze1;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.utilities.FormatConstants;
import com.puttysoftware.xio.XDataReader;

public class LoadTask3 extends Thread {
    // Fields
    private Maze gameMaze;
    private final String filename;
    private static final byte FORMAT_VERSION_MAJOR = (byte) 3;
    private static final byte FORMAT_VERSION_MINOR = (byte) 1;

    // Constructors
    public LoadTask3(final String file) {
        this.filename = file;
    }

    // Methods
    @Override
    public void run() {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        int startW;
        String sg;
        app.getGameManager().setSavedGameFlag(false);
        sg = "Maze";
        try (final XDataReader mazeFile = new XDataReader(this.filename,
                    "ft3")){
            try {
                final boolean supported = LoadTask3.checkFormatVersion(
                        LoadTask3.readFormatVersion(mazeFile));
                if (!supported) {
                    throw new InvalidMazeException(
                            "Unsupported maze format version.");
                }
                if (this.gameMaze == null) {
                    this.gameMaze = new Maze5();
                }
                this.gameMaze = this.gameMaze.readMaze(mazeFile,
                        FormatConstants.MAZE_FORMAT_3);
                if (this.gameMaze == null) {
                    mazeFile.close();
                    throw new InvalidMazeException(
                            "Unknown object encountered.");
                }
                app.getMazeManager().setMaze(this.gameMaze);
                app.getGameManager().validateScore();
                mazeFile.close();
                startW = this.gameMaze.getStartLevel();
                final boolean playerExists = this.gameMaze
                        .findPlayerOnLevel(startW);
                if (playerExists) {
                    this.gameMaze.findAllStarts();
                    app.getGameManager().getPlayerManager().setPlayerLocation(
                            this.gameMaze.getFindResultColumn(startW),
                            this.gameMaze.getFindResultRow(startW),
                            this.gameMaze.getFindResultFloor(startW), startW);
                    app.getGameManager().resetViewingWindow();
                }
                this.gameMaze.save();
                app.getMazeManager().setMaze(this.gameMaze);
                // Final cleanup
                final String lum = app.getMazeManager().getLastUsedMaze();
                app.getMazeManager().clearLastUsedFilenames();
                app.getMazeManager().setLastUsedMaze(lum);
                app.getEditor().mazeChanged();
                Messager.showDialog(sg + " file loaded.");
                app.getMazeManager().handleDeferredSuccess(true);
            } catch (final IOException ie) {
                throw new InvalidMazeException(
                        "Error reading " + sg.toLowerCase() + " file.");
            }
        } catch (final InvalidMazeException ime) {
            Messager.showDialog(ime.getMessage());
            app.getMazeManager().handleDeferredSuccess(false);
        } catch (final Exception ex) {
            Messager.showDialog(
                    "Unknown error reading " + sg.toLowerCase() + " file.");
            app.getMazeManager().handleDeferredSuccess(false);
        }
    }

    private static byte[] readFormatVersion(final XDataReader reader)
            throws IOException {
        final byte major = reader.readByte();
        final byte minor = reader.readByte();
        return new byte[] { major, minor };
    }

    private static boolean checkFormatVersion(final byte[] version) {
        final byte major = version[0];
        final byte minor = version[1];
        if (major != LoadTask3.FORMAT_VERSION_MAJOR) {
            return false;
        } else {
            if (minor > LoadTask3.FORMAT_VERSION_MINOR) {
                return false;
            } else {
                return true;
            }
        }
    }
}
