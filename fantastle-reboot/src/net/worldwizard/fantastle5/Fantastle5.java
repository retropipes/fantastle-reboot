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
package net.worldwizard.fantastle5;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.errorlogger.ErrorLogger;
//import com.puttysoftware.integration.NativeIntegration;

import net.worldwizard.fantastle5.resourcemanagers.GraphicsManager;
import net.worldwizard.fantastle5.resourcemanagers.ImageCache;
import net.worldwizard.fantastle5.resourcemanagers.MonsterImageCache;
import net.worldwizard.fantastle5.resourcemanagers.SoundCache;

public class Fantastle5 {
    // Constants
    private static Application application;
    private static final String PROGRAM_NAME = "Fantastle";
    private static final String ERROR_MESSAGE = "Perhaps a bug is to blame for this error message.\n"
            + "Include the debug log with your bug report.\n"
            + "Email bug reports to: fantastle@worldwizard.net\n"
            + "Subject: Fantastle Bug Report";
    private static final String ERROR_TITLE = "Fantastle Error";
    private static final ErrorLogger debug = new ErrorLogger(
            Fantastle5.PROGRAM_NAME);
    private static boolean IN_FANTASTLE_5 = true;

    // Methods
    public static Application getApplication() {
        return Fantastle5.application;
    }

    public static void logError(final Throwable t) {
        CommonDialogs.showErrorDialog(Fantastle5.ERROR_MESSAGE,
                Fantastle5.ERROR_TITLE);
        Fantastle5.debug.logError(t);
    }

    public static boolean inFantastle5() {
        return Fantastle5.IN_FANTASTLE_5;
    }

    public static void leaveFantastle5() {
        Fantastle5.IN_FANTASTLE_5 = false;
    }

    public static void main(final String[] args) {
        if (System.getProperty("os.name").startsWith("Mac OS X")) {
            // Mac OS X-specific stuff
            System.setProperty(
                    "com.apple.mrj.application.apple.menu.about.name",
                    "Fantastle");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        } else {
            try {
                // Tell the UIManager to use the platform native look and feel
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (final Exception e) {
                // Do nothing
            }
        }
        try {
            Fantastle5.application = new Application();
            Fantastle5.application.postConstruct();
            // Load stuff
            Fantastle5.showLoadingScreen();
            // Done loading
            Fantastle5.application.playLogoSound();
            Fantastle5.application.getGUIManager().showGUI();
            // OS Integration (FIXME: broken)
            // NativeIntegration ni = new NativeIntegration();
            // ni.setOpenFileHandler(Fantastle5.application.getMazeManager(),
            // Fantastle5.application.getMazeManager().getClass()
            // .getDeclaredMethod("loadFromOSHandler",
            // String.class));
            // ni.setQuitHandler(Fantastle5.application.getMazeManager(),
            // Fantastle5.application.getMazeManager().getClass()
            // .getDeclaredMethod("quitHandler"));
            // ni.setPreferencesHandler(Fantastle5.application.getPrefsManager(),
            // Fantastle5.application.getPrefsManager().getClass()
            // .getDeclaredMethod("showPrefs"));
            // ni.setAboutHandler(Fantastle5.application.getAboutDialog(),
            // Fantastle5.application.getAboutDialog().getClass()
            // .getDeclaredMethod("showAboutDialog"));
        } catch (final Throwable t) {
            Fantastle5.logError(t);
        }
    }

    private static void showLoadingScreen() {
        // Set up wait frame
        final JFrame waitFrame = new JFrame("Loading...");
        final Container logoContainer = new Container();
        final Container textContainer = new Container();
        final JLabel waitLogo = new JLabel("", GraphicsManager.getLoadingLogo(),
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
        Fantastle5.getApplication().getGUIManager().updateLogo();
        waitProgress.setValue(20);
        // Create image cache
        ImageCache.recreateCache();
        waitProgress.setValue(40);
        // Create monster image cache
        MonsterImageCache.recreateMonsterCache();
        waitProgress.setValue(60);
        // Create sound cache
        SoundCache.recreateCache();
        waitProgress.setValue(80);
        // Create stat image cache
        Fantastle5.getApplication().getGameManager().getStatGUI().updateGUI();
        waitProgress.setValue(100);
        waitFrame.setVisible(false);
    }
}
