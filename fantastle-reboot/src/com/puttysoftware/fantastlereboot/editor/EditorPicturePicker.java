package com.puttysoftware.fantastlereboot.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.puttysoftware.images.BufferedImageIcon;

final class EditorPicturePicker {
  // Fields
  private BufferedImageIcon[] choices;
  private JLabel[] choiceArray;
  private final JPanel pickerJPanel;
  private final JPanel choiceJPanel;
  private final JPanel radioJPanel;
  private final JPanel choiceRadioJPanel;
  private final ButtonGroup radioGroup;
  private JRadioButton[] radioButtons;
  private final JScrollPane scrollPane;
  int index;
  private final EventHandler handler;

  // Constructor
  public EditorPicturePicker(final BufferedImageIcon[] pictures) {
    this.handler = new EventHandler();
    this.pickerJPanel = new JPanel();
    this.pickerJPanel.setLayout(new BorderLayout());
    this.choiceJPanel = new JPanel();
    this.radioJPanel = new JPanel();
    this.radioGroup = new ButtonGroup();
    this.choiceRadioJPanel = new JPanel();
    this.choiceRadioJPanel.setLayout(new BorderLayout());
    this.choiceRadioJPanel.add(this.radioJPanel, BorderLayout.WEST);
    this.choiceRadioJPanel.add(this.choiceJPanel, BorderLayout.CENTER);
    this.scrollPane = new JScrollPane(this.choiceRadioJPanel);
    this.scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.scrollPane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.pickerJPanel.add(this.scrollPane, BorderLayout.CENTER);
    this.updatePicker(pictures);
    this.index = 0;
  }

  // Methods
  public JPanel getPicker() {
    return this.pickerJPanel;
  }

  public void disablePicker() {
    this.pickerJPanel.setEnabled(false);
    for (final JRadioButton radioButton : this.radioButtons) {
      radioButton.setEnabled(false);
    }
  }

  public void enablePicker() {
    this.pickerJPanel.setEnabled(true);
    for (final JRadioButton radioButton : this.radioButtons) {
      radioButton.setEnabled(true);
    }
  }

  public void updatePicker(final BufferedImageIcon[] newImages) {
    this.choices = newImages;
    this.choiceJPanel.removeAll();
    this.radioJPanel.removeAll();
    this.radioButtons = new JRadioButton[this.choices.length];
    this.choiceJPanel.setLayout(new GridLayout(this.choices.length, 1));
    this.radioJPanel.setLayout(new GridLayout(this.choices.length, 1));
    this.choiceArray = new JLabel[this.choices.length];
    for (int x = 0; x < this.choices.length; x++) {
      this.choiceArray[x] = new JLabel("", this.choices[x], //$NON-NLS-1$
          SwingConstants.LEFT);
      this.choiceArray[x].setOpaque(true);
      this.choiceJPanel.add(this.choiceArray[x]);
      this.radioButtons[x] = new JRadioButton();
      this.radioButtons[x].setOpaque(true);
      this.radioButtons[x].setActionCommand(Integer.valueOf(x).toString());
      this.radioGroup.add(this.radioButtons[x]);
      this.radioButtons[x].addActionListener(this.handler);
      this.radioJPanel.add(this.radioButtons[x]);
    }
  }

  public void updatePickerLayout(final int maxHeight) {
    final int newPreferredWidth = this.pickerJPanel.getLayout()
        .preferredLayoutSize(this.pickerJPanel).width
        + this.scrollPane.getVerticalScrollBar().getWidth();
    final int newPreferredHeight = Math.min(maxHeight, this.pickerJPanel
        .getLayout().preferredLayoutSize(this.pickerJPanel).height);
    this.pickerJPanel
        .setPreferredSize(new Dimension(newPreferredWidth, newPreferredHeight));
  }

  public void selectLastPickedChoice(final int lastPicked) {
    this.radioButtons[lastPicked].setSelected(true);
  }

  /**
   *
   * @return the index of the picture picked
   */
  public int getPicked() {
    return this.index;
  }

  private class EventHandler implements ActionListener {
    public EventHandler() {
      // Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      final String cmd = e.getActionCommand();
      // A radio button
      EditorPicturePicker.this.index = Integer.parseInt(cmd);
    }
  }
}
