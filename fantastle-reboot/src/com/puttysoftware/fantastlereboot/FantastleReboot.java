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
package com.puttysoftware.fantastlereboot;

import java.awt.desktop.QuitStrategy;

import javax.swing.JMenuBar;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.errorlogger.ErrorLogger;
import com.puttysoftware.fantastlereboot.assets.UserInterfaceImageIndex;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.gui.MenuManager;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.gui.PrefsLauncher;
import com.puttysoftware.fantastlereboot.loaders.UserInterfaceImageLoader;
import com.puttysoftware.integration.NativeIntegration;

public class FantastleReboot {
  // Constants
  private static BagOStuff bag;
  private static final String PROGRAM_NAME = "Fantastle Reboot";
  private static final String ERROR_MESSAGE = "Perhaps a bug is to blame for this error message.\n"
      + "Include the debug log with your bug report.\n"
      + "Email bug reports to: support@puttysoftware.com\n"
      + "Subject: Fantastle Reboot Error Report";
  private static final String ERROR_TITLE = "Fantastle Error";
  private static final String WARNING_MESSAGE = "Perhaps a bug is to blame for this warning message.\n"
      + "Include the debug log with your bug report.\n"
      + "Email bug reports to: support@puttysoftware.com\n"
      + "Subject: Fantastle Reboot Warning Report";
  private static final String WARNING_TITLE = "Fantastle Warning";
  private static final ErrorLogger debug = new ErrorLogger(
      FantastleReboot.PROGRAM_NAME);
  private static final NativeIntegration NATIVITY = new NativeIntegration();
  private static boolean IN_FANTASTLE = true;
  private static final int BATTLE_MAZE_SIZE = 16;
  private static MenuManager menus;
  private static final JMenuBar mainMenuBar = new JMenuBar();

  // Methods
  public static BagOStuff getBagOStuff() {
    return FantastleReboot.bag;
  }

  public static ErrorLogger getErrorHandler() {
    return FantastleReboot.debug;
  }

  public static void logError(final Throwable t) {
    CommonDialogs.showErrorDialog(FantastleReboot.ERROR_MESSAGE,
        FantastleReboot.ERROR_TITLE);
    FantastleReboot.debug.logError(t);
  }

  public static void logWarning(final Throwable t) {
    CommonDialogs.showErrorDialog(FantastleReboot.WARNING_MESSAGE,
        FantastleReboot.WARNING_TITLE);
    FantastleReboot.debug.logNonFatalError(t);
  }

  public static void logWarningWithMessage(final Throwable t,
      final String message) {
    CommonDialogs.showErrorDialog(message, FantastleReboot.WARNING_TITLE);
    FantastleReboot.debug.logNonFatalError(t);
  }

  public static int getBattleMazeSize() {
    return FantastleReboot.BATTLE_MAZE_SIZE;
  }

  public static boolean inFantastleReboot() {
    return FantastleReboot.IN_FANTASTLE;
  }

  public static void leaveFantastleReboot() {
    FantastleReboot.IN_FANTASTLE = false;
  }

  static MenuManager getMenuManager() {
    return FantastleReboot.menus;
  }

  static void doLateOSIntegration() {
    CommonDialogs.setDefaultTitle(FantastleReboot.PROGRAM_NAME);
    CommonDialogs.setIcon(
        UserInterfaceImageLoader.load(UserInterfaceImageIndex.MICRO_LOGO));
    NATIVITY.setQuitStrategy(QuitStrategy.NORMAL_EXIT);
    NATIVITY.setQuitHandler(new QuitRequestManager());
    NATIVITY.setPreferencesHandler(new PrefsLauncher());
    NATIVITY.setAboutHandler(FantastleReboot.bag.getAboutDialog());
  }

  public static void main(final String[] args) {
    try {
      // Early OS Integration
      NATIVITY.configureLookAndFeel();
      menus = new MenuManager(mainMenuBar);
      NATIVITY.setDefaultMenuBar(mainMenuBar);
      // Compute action cap
      Creature.computeActionCap(FantastleReboot.BATTLE_MAZE_SIZE,
          FantastleReboot.BATTLE_MAZE_SIZE);
      // Set default preferences
      Prefs.setDefaultPrefs();
      // Create the Bag O'Stuff
      FantastleReboot.bag = new BagOStuff();
      // Load stuff
      new Loader().start();
    } catch (final Throwable t) {
      FantastleReboot.logError(t);
    }
  }
}
