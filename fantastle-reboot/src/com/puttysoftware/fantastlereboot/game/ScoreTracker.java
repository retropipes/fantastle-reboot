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
package com.puttysoftware.fantastlereboot.game;

import java.io.File;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.maze.Extension;
import com.puttysoftware.scoremanager.SavedScoreManager;
import com.puttysoftware.scoremanager.ScoreManager;

public class ScoreTracker {
  // Fields
  private static String scoresFile = "";
  private static SavedScoreManager ssMgr;
  private static long score = 0L;
  private static boolean scoreValid = false;
  private static final String MAC_PREFIX = "HOME";
  private static final String WIN_PREFIX = "APPDATA";
  private static final String UNIX_PREFIX = "HOME";
  private static final String MAC_DIR = "/Library/Fantastle/Scores/";
  private static final String WIN_DIR = "\\Fantastle\\Scores\\";
  private static final String UNIX_DIR = "/.fantastle/scores/";

  // Constructors
  private ScoreTracker() {
    // Do nothing
  }

  // Methods
  public static boolean checkScore() {
    if (ScoreTracker.scoreValid) {
      return ScoreTracker.ssMgr.checkScore(ScoreTracker.score);
    } else {
      return false;
    }
  }

  public static void commitScore() {
    if (ScoreTracker.scoreValid) {
      final boolean result = ScoreTracker.ssMgr.addScore(ScoreTracker.score);
      if (result) {
        ScoreTracker.ssMgr.viewTable();
      }
    }
  }

  public static void invalidateScore() {
    ScoreTracker.scoreValid = false;
  }

  public static void resetScore() {
    ScoreTracker.score = 0L;
  }

  public static void setScoreFile(final String filename) {
    // Check validity
    if (ScoreTracker.scoreValid) {
      // Check filename argument
      if (filename != null) {
        if (filename.equals("")) {
          throw new IllegalArgumentException("Filename cannot be empty!");
        }
      } else {
        throw new IllegalArgumentException("Filename cannot be null!");
      }
      // Make sure the needed directories exist first
      final File sf = ScoreTracker.getScoresFile(filename);
      final File parent = new File(sf.getParent());
      if (!parent.exists()) {
        parent.mkdirs();
      }
      ScoreTracker.scoresFile = sf.getAbsolutePath();
      ScoreTracker.ssMgr = new SavedScoreManager(1, 10,
          ScoreManager.SORT_ORDER_ASCENDING, 0L, "Fantastle Scores",
          new String[] { "points" }, ScoreTracker.scoresFile);
    }
  }

  public static void incrementScore() {
    ScoreTracker.score++;
  }

  public static void deductStep() {
    ScoreTracker.score--;
  }

  public static void updateScore(final long increment) {
    ScoreTracker.score += increment;
  }

  public static void validateScore() {
    ScoreTracker.scoreValid = true;
  }

  public static long getScore() {
    return ScoreTracker.score;
  }

  public static void setScore(final long newScore) {
    ScoreTracker.score = newScore;
  }

  public static String getScoreUnits() {
    return "points";
  }

  public static void showCurrentScore() {
    if (ScoreTracker.scoreValid) {
      CommonDialogs.showDialog("Your current score: " + ScoreTracker.score + " points");
    } else {
      CommonDialogs.showDialog("The current score is not available at this time.");
    }
  }

  public static void showScoreTable() {
    ScoreTracker.ssMgr.viewTable();
  }

  private static String getScoreDirPrefix() {
    final String osName = System.getProperty("os.name");
    if (osName.indexOf("Mac OS X") != -1) {
      // Mac OS X
      return System.getenv(ScoreTracker.MAC_PREFIX);
    } else if (osName.indexOf("Windows") != -1) {
      // Windows
      return System.getenv(ScoreTracker.WIN_PREFIX);
    } else {
      // Other - assume UNIX-like
      return System.getenv(ScoreTracker.UNIX_PREFIX);
    }
  }

  private static String getScoreDirectory() {
    final String osName = System.getProperty("os.name");
    if (osName.indexOf("Mac OS X") != -1) {
      // Mac OS X
      return ScoreTracker.MAC_DIR;
    } else if (osName.indexOf("Windows") != -1) {
      // Windows
      return ScoreTracker.WIN_DIR;
    } else {
      // Other - assume UNIX-like
      return ScoreTracker.UNIX_DIR;
    }
  }

  private static File getScoresFile(final String filename) {
    final StringBuilder b = new StringBuilder();
    b.append(ScoreTracker.getScoreDirPrefix());
    b.append(ScoreTracker.getScoreDirectory());
    b.append(filename);
    b.append(Extension.getScoresExtensionWithPeriod());
    return new File(b.toString());
  }
}