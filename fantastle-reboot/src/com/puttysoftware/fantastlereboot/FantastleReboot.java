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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.errorlogger.ErrorLogger;
import com.puttysoftware.fantastlereboot.assets.GameUserInterfaceImage;
import com.puttysoftware.fantastlereboot.creatures.Creature;
import com.puttysoftware.fantastlereboot.loaders.ImageLoader;
import com.puttysoftware.fantastlereboot.loaders.UserInterfaceImageLoader;
import com.puttysoftware.integration.NativeIntegration;

public class FantastleReboot {
    // Constants
    private static BagOStuff bag;
    private static final String PROGRAM_NAME = "Fantastle";
    private static final String ERROR_MESSAGE = "Perhaps a bug is to blame for this error message.\n"
            + "Include the debug log with your bug report.\n"
            + "Email bug reports to: fantastle@worldwizard.net\n"
            + "Subject: Fantastle Bug Report";
    private static final String ERROR_TITLE = "Fantastle Error";
    private static final ErrorLogger debug = new ErrorLogger(
            FantastleReboot.PROGRAM_NAME);
    private static final NativeIntegration NATIVITY = new NativeIntegration();
    private static boolean IN_FANTASTLE = true;
    private static final int BATTLE_MAZE_SIZE = 16;

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

    public static int getBattleMazeSize() {
        return FantastleReboot.BATTLE_MAZE_SIZE;
    }

    public static boolean inFantastleReboot() {
        return FantastleReboot.IN_FANTASTLE;
    }

    public static void leaveFantastleReboot() {
        FantastleReboot.IN_FANTASTLE = false;
    }

    public static void attachMenus(JMenuBar defaultMenuBar) {
        NATIVITY.setDefaultMenuBar(defaultMenuBar);
    }

    public static void main(final String[] args) {
        try {
            // Compute action cap
            Creature.computeActionCap(FantastleReboot.BATTLE_MAZE_SIZE,
                    FantastleReboot.BATTLE_MAZE_SIZE);
            // Create the Bag O'Stuff
            FantastleReboot.bag = new BagOStuff();
            FantastleReboot.bag.postConstruct();
            // OS Integration
            NATIVITY.configureLookAndFeel();
            NATIVITY.setOpenFileHandler(FantastleReboot.bag.getMazeManager());
            NATIVITY.setQuitHandler(FantastleReboot.bag.getMazeManager());
            NATIVITY.setPreferencesHandler(
                    FantastleReboot.bag.getPrefsManager());
            NATIVITY.setAboutHandler(FantastleReboot.bag.getAboutDialog());
            // Load stuff
            FantastleReboot.showLoadingScreen();
            // Done loading
            FantastleReboot.bag.playLogoSound();
            FantastleReboot.bag.getGUIManager().showGUI();
        } catch (final Throwable t) {
            FantastleReboot.logError(t);
        }
    }

    private static void showLoadingScreen() {
        // Set up wait frame
        final JFrame waitFrame = new JFrame("Loading...");
        final Container logoContainer = new Container();
        final Container textContainer = new Container();
        final JLabel waitLogo = new JLabel("",
                UserInterfaceImageLoader.load(GameUserInterfaceImage.LOADING),
                SwingConstants.CENTER);
        final JLabel waitLabel = new JLabel("Creating Caches...");
        final JProgressBar waitProgress = new JProgressBar();
        waitProgress.setMinimum(0);
        waitProgress.setMaximum(100);
        waitProgress.setValue(0);
        waitFrame.getContentPane().setLayout(new BorderLayout());
        logoContainer.setLayout(new FlowLayout());
        textContainer.setLayout(new GridLayout(2, 1));
        logoContainer.add(waitLogo);
        textContainer.add(waitLabel);
        textContainer.add(waitProgress);
        waitFrame.getContentPane().add(logoContainer, BorderLayout.CENTER);
        waitFrame.getContentPane().add(textContainer, BorderLayout.SOUTH);
        waitFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        waitFrame.setResizable(false);
        waitFrame.pack();
        // Do the loading
        waitFrame.setVisible(true);
        // Create logo cache
        FantastleReboot.getBagOStuff().getGUIManager().updateLogo();
        waitProgress.setValue(25);
        // Create image cache
        ImageLoader.recreateCache();
        waitProgress.setValue(50);
        // Create sound cache
        waitProgress.setValue(75);
        // Create stat image cache
        FantastleReboot.getBagOStuff().getGameManager().updateStatGUI();
        waitProgress.setValue(100);
        waitFrame.setVisible(false);
    }
}
