package com.puttysoftware.fantastlereboot;

import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class MainWindow {
  private static MainWindow SINGLETON;
  private JFrame frame;

  private MainWindow() {
    super();
    this.frame = new JFrame();
    this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.frame.setResizable(false);
  }

  public static MainWindow getOutputFrame() {
    if (MainWindow.SINGLETON == null) {
      MainWindow.SINGLETON = new MainWindow();
    }
    return MainWindow.SINGLETON;
  }

  public void setTitle(final String title) {
    this.frame.setTitle(title);
  }

  public void pack() {
    this.frame.pack();
  }

  public void setVisible(final boolean b) {
    this.frame.setVisible(b);
  }

  public void addWindowListener(final WindowListener l) {
    this.frame.addWindowListener(l);
  }

  public void removeWindowListener(final WindowListener l) {
    this.frame.removeWindowListener(l);
  }

  public void addKeyListener(final KeyListener l) {
    this.frame.addKeyListener(l);
  }

  public void removeKeyListener(final KeyListener l) {
    this.frame.removeKeyListener(l);
  }

  public void setContentPane(final Container contentPane) {
    this.frame.setContentPane(contentPane);
  }

  public void setDefaultButton(final JButton defaultButton) {
    this.frame.getRootPane().setDefaultButton(defaultButton);
  }
}
