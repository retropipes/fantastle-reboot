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

import java.io.IOException;

import com.puttysoftware.fantastlereboot.BagOStuff;
import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.Messager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.xio.XDataReader;

public class LoadTask5 extends Thread {
    // Fields
    private Maze gameMaze;
    private final String filename;
    private final boolean isSavedGame;
    private static final byte FORMAT_VERSION_MAJOR = (byte) 5;
    private static final byte FORMAT_VERSION_MINOR = (byte) 0;

    // Constructors
    public LoadTask5(final String file, final boolean saved) {
        this.filename = file;
        this.isSavedGame = saved;
    }

    // Methods
    @Override
    public void run() {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        int startW;
        String sg;
        if (this.isSavedGame) {
            app.getGameManager().setSavedGameFlag(true);
            sg = "Saved Game";
        } else {
            app.getGameManager().setSavedGameFlag(false);
            sg = "Maze";
        }
        try (final XDataReader mazeFile = new XDataReader(this.filename,
                "ft5")) {
            try {
                final boolean supported = LoadTask5.checkFormatVersion(
                        LoadTask5.readFormatVersion(mazeFile));
                if (!supported) {
                    throw new InvalidMazeException(
                            "Unsupported maze format version.");
                }
                if (this.gameMaze == null) {
                    this.gameMaze = new Maze5();
                }
                this.gameMaze = this.gameMaze.readMaze(mazeFile,
                        FormatConstants.MAZE_FORMAT_5);
                if (this.gameMaze == null) {
                    mazeFile.close();
                    throw new InvalidMazeException(
                            "Unknown object encountered.");
                }
                app.getMazeManager().setMaze(this.gameMaze);
                app.getGameManager().validateScore();
                if (this.isSavedGame) {
                    app.getGameManager().loadGameHook(mazeFile,
                            FormatConstants.MAZE_FORMAT_5);
                    PartyManager.loadGameHook(mazeFile);
                }
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
                if (!this.isSavedGame) {
                    this.gameMaze.save();
                }
                app.getMazeManager().setMaze(this.gameMaze);
                // Final cleanup
                final String lum = app.getMazeManager().getLastUsedMaze();
                final String lug = app.getMazeManager().getLastUsedGame();
                app.getMazeManager().clearLastUsedFilenames();
                if (this.isSavedGame) {
                    app.getMazeManager().setLastUsedGame(lug);
                } else {
                    app.getMazeManager().setLastUsedMaze(lum);
                }
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
            ex.printStackTrace();
            Messager.showDialog(
                    "Unknown error reading " + sg.toLowerCase() + " file.");
            app.getMazeManager().handleDeferredSuccess(false);
        }
    }

    private static byte[] readFormatVersion(final XDataReader mazeFile)
            throws IOException {
        final byte major = mazeFile.readByte();
        final byte minor = mazeFile.readByte();
        return new byte[] { major, minor };
    }

    private static boolean checkFormatVersion(final byte[] version) {
        final byte major = version[0];
        final byte minor = version[1];
        if (major != LoadTask5.FORMAT_VERSION_MAJOR) {
            return false;
        } else {
            if (minor > LoadTask5.FORMAT_VERSION_MINOR) {
                return false;
            } else {
                return true;
            }
        }
    }
}
