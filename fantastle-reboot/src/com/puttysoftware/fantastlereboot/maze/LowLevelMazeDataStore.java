package com.puttysoftware.fantastlereboot.maze;

import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;

class LowLevelMazeDataStore implements Cloneable {
  // Fields
  private final FantastleObjectModel[] dataStore;
  private final int[] dataShape;
  private final int[] interProd;

  // Constructor
  public LowLevelMazeDataStore(final int... shape) {
    this.dataShape = shape;
    this.interProd = new int[shape.length];
    int product = 1;
    for (int x = 0; x < shape.length; x++) {
      this.interProd[x] = product;
      product *= shape[x];
    }
    this.dataStore = new FantastleObjectModel[product];
  }

  // Methods
  private int ravelLocation(final int... loc) {
    int res = 0;
    // Sanity check #1
    if (loc.length != this.interProd.length) {
      throw new IllegalArgumentException(Integer.toString(loc.length));
    }
    for (int x = 0; x < this.interProd.length; x++) {
      // Sanity check #2
      if (loc[x] < 0 || loc[x] >= this.dataShape[x]) {
        throw new ArrayIndexOutOfBoundsException(loc[x]);
      }
      res += loc[x] * this.interProd[x];
    }
    return res;
  }

  public int[] getShape() {
    return this.dataShape;
  }

  public FantastleObjectModel getCell(final int... loc) {
    final int aloc = this.ravelLocation(loc);
    return this.dataStore[aloc];
  }

  public void setCell(final FantastleObjectModel obj, final int... loc) {
    if (obj == null) {
      throw new IllegalArgumentException("obj == NULL!");
    }
    final int aloc = this.ravelLocation(loc);
    this.dataStore[aloc] = obj;
  }
}
