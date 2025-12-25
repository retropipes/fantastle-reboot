/*  Fantastle: A World-Solving Game
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

import org.retropipes.diane.Diane;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.diane.integration.Integration;

import com.puttysoftware.fantastlereboot.assets.UserInterfaceImageIndex;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.gui.MenuManager;
import com.puttysoftware.fantastlereboot.gui.Prefs;
import com.puttysoftware.fantastlereboot.gui.PrefsLauncher;
import com.puttysoftware.fantastlereboot.loaders.UserInterfaceImageLoader;

public class FantastleReboot {
    // Constants
    private static BagOStuff bag;
    static final String PROGRAM_NAME = "Fantastle Reboot";
    private static final Integration NATIVITY = new Integration();
    private static final int BATTLE_WORLD_SIZE = 16;
    private static MenuManager menus;
    private static final JMenuBar mainMenuBar = new JMenuBar();

    // Methods
    public static BagOStuff getBagOStuff() {
	return FantastleReboot.bag;
    }

    public static void exception(final Throwable t) {
	Diane.handleError(t);
    }

    static void silentlyLog(final Throwable t) {
	final RuntimeException re = new RuntimeException(t);
	Diane.handleWarning(re);
    }

    public static void exceptionWithMessage(final Throwable t, final String message) {
	CommonDialogs.showErrorDialog(message);
	Diane.handleError(t);
    }

    public static int getBattleWorldSize() {
	return FantastleReboot.BATTLE_WORLD_SIZE;
    }

    static MenuManager getMenuManager() {
	return FantastleReboot.menus;
    }

    static void doLateOSIntegration() {
	CommonDialogs.setDefaultTitle(FantastleReboot.PROGRAM_NAME);
	CommonDialogs.setIcon(UserInterfaceImageLoader.load(UserInterfaceImageIndex.MICRO_LOGO));
	FantastleReboot.NATIVITY.setQuitStrategy(QuitStrategy.NORMAL_EXIT);
	FantastleReboot.NATIVITY.setQuitHandler(new QuitRequestManager());
	FantastleReboot.NATIVITY.setPreferencesHandler(new PrefsLauncher());
	FantastleReboot.NATIVITY.setAboutHandler(FantastleReboot.bag.getAboutDialog());
    }

    public static void main(final String[] args) {
	// Install error handler
	Diane.installDefaultErrorHandler(FantastleReboot.PROGRAM_NAME);
	try {
	    // Early OS Integration
	    FantastleReboot.NATIVITY.configureLookAndFeel();
	    FantastleReboot.menus = new MenuManager(FantastleReboot.mainMenuBar);
	    FantastleReboot.NATIVITY.setDefaultMenuBar(FantastleReboot.mainMenuBar);
	    // Compute action cap
	    Creature.computeActionCap(FantastleReboot.BATTLE_WORLD_SIZE, FantastleReboot.BATTLE_WORLD_SIZE);
	    // Set default preferences
	    Prefs.setDefaultPrefs();
	    // Create the Bag O'Stuff
	    FantastleReboot.bag = new BagOStuff();
	    // Load stuff
	    new Loader().start();
	} catch (final Throwable t) {
	    FantastleReboot.exception(t);
	}
    }
}
