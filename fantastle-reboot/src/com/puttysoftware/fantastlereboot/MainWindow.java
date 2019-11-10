package com.puttysoftware.fantastlereboot;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private static final MainWindow SINGLETON = new MainWindow();

  private MainWindow() {
    super();
  }

  public static MainWindow getOutputFrame() {
    return MainWindow.SINGLETON;
  }
}
