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

import javax.swing.JOptionPane;

public class Messager {
    public static void showMessage(final String msg) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        app.getGameManager().setStatusMessage(msg);
    }

    public static void showDialog(final String msg) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        JOptionPane.showMessageDialog(app.getOutputFrame(), msg, "Fantastle",
                JOptionPane.INFORMATION_MESSAGE, app.getMicroLogo());
    }

    public static void showTitledDialog(final String msg, final String title) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        JOptionPane.showMessageDialog(app.getOutputFrame(), msg, title,
                JOptionPane.INFORMATION_MESSAGE, app.getMicroLogo());
    }

    public static void showErrorDialog(final String msg, final String title) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        JOptionPane.showMessageDialog(app.getOutputFrame(), msg, title,
                JOptionPane.ERROR_MESSAGE, app.getMicroLogo());
    }

    public static String showInputDialog(final String prompt,
            final String title, final Object[] choices,
            final String defaultChoice) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        return (String) JOptionPane.showInputDialog(app.getOutputFrame(),
                prompt, title, JOptionPane.QUESTION_MESSAGE, app.getMicroLogo(),
                choices, defaultChoice);
    }

    public static String showTextInputDialog(final String prompt,
            final String title) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        return (String) JOptionPane.showInputDialog(app.getOutputFrame(),
                prompt, title, JOptionPane.QUESTION_MESSAGE, app.getMicroLogo(),
                null, null);
    }

    public static String showTextInputDialogWithDefault(final String prompt,
            final String title, final String defaultValue) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        return (String) JOptionPane.showInputDialog(app.getOutputFrame(),
                prompt, title, JOptionPane.QUESTION_MESSAGE, app.getMicroLogo(),
                null, defaultValue);
    }

    public static int showConfirmDialog(final String prompt,
            final String title) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        return JOptionPane.showConfirmDialog(app.getOutputFrame(), prompt,
                title, JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, app.getMicroLogo());
    }

    public static int showYNCConfirmDialog(final String prompt,
            final String title) {
        final BagOStuff app = FantastleReboot.getBagOStuff();
        return JOptionPane.showConfirmDialog(app.getOutputFrame(), prompt,
                title, JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE, app.getMicroLogo());
    }
}
