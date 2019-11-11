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
package com.puttysoftware.fantastlereboot.gui;

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.fantastlereboot.game.Game;

public class Messager {
  public static void showMessage(final String msg) {
    Game.setStatusMessage(msg);
  }

  public static void showDialog(final String msg) {
    CommonDialogs.showDialog(msg);
  }

  public static void showTitledDialog(final String msg, final String title) {
    CommonDialogs.showTitledDialog(msg, title);
  }

  public static void showErrorDialog(final String msg, final String title) {
    CommonDialogs.showErrorDialog(msg, title);
  }

  public static String showInputDialog(final String prompt, final String title,
      final Object[] choices, final String defaultChoice) {
    return CommonDialogs.showInputDialog(prompt, title, choices, defaultChoice);
  }

  public static String showTextInputDialog(final String prompt,
      final String title) {
    return CommonDialogs.showTextInputDialog(prompt, title);
  }

  public static String showTextInputDialogWithDefault(final String prompt,
      final String title, final String defaultValue) {
    return CommonDialogs.showTextInputDialogWithDefault(prompt, title,
        defaultValue);
  }

  public static int showConfirmDialog(final String prompt, final String title) {
    return CommonDialogs.showConfirmDialog(prompt, title);
  }

  public static int showYNCConfirmDialog(final String prompt,
      final String title) {
    return CommonDialogs.showYNCConfirmDialog(prompt, title);
  }
}
