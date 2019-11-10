package com.puttysoftware.fantastlereboot;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainWindow extends JFrame {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private static MainWindow SINGLETON;

  private MainWindow() {
    super();
  }

  public static MainWindow getOutputFrame() {
    if (MainWindow.SINGLETON == null) {
      MainWindow.SINGLETON = new MainWindow();
      MainWindow.SINGLETON
          .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      MainWindow.SINGLETON.setResizable(false);
    }
    return MainWindow.SINGLETON;
  }
}
