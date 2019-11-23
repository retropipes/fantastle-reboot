package com.puttysoftware.fantastlereboot.maze;

import com.puttysoftware.diane.gui.CommonDialogs;

public class NoteManager {
  private NoteManager() {
    // Do nothing
  }

  public static void editNote() {
    final Maze m = MazeManager.getMaze();
    final int x = m.getPlayerLocationX();
    final int y = m.getPlayerLocationY();
    final int z = m.getPlayerLocationZ();
    String defaultText = "Empty Note";
    if (m.hasNote(x, y, z)) {
      defaultText = m.getNote(x, y, z).getContents();
    }
    final String result = CommonDialogs
        .showTextInputDialogWithDefault("Note Text:", "Edit Note", defaultText);
    if (result != null) {
      if (!m.hasNote(x, y, z)) {
        m.createNote(x, y, z);
      }
      final MazeNote mn = m.getNote(x, y, z);
      mn.setContents(result);
    }
  }
}
