package com.puttysoftware.fantastlereboot;

import javax.swing.JFrame;

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
      MainWindow.SINGLETON.setResizable(false);
    }
    return MainWindow.SINGLETON;
  }
}
