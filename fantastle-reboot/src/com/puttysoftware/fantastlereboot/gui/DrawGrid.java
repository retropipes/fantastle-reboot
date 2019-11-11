package com.puttysoftware.fantastlereboot.gui;

import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.storage.ObjectStorage;

public class DrawGrid extends ObjectStorage {
  public DrawGrid(final int numSquares) {
    super(numSquares, numSquares);
  }

  public BufferedImageIcon getImageCell(final int row, final int col) {
    return (BufferedImageIcon) this.getCell(row, col);
  }

  public void setImageCell(final BufferedImageIcon bii, final int row,
      final int col) {
    this.setCell(bii, row, col);
  }
}
