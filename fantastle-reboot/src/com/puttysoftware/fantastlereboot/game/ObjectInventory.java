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
package com.puttysoftware.fantastlereboot.game;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModelList;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class ObjectInventory implements Cloneable {
  // Properties
  private final int[] uidList;
  private final int[] contents;
  private final int[][] uses;
  private final int MAX_QUANTITY = 100;

  // Constructors
  public ObjectInventory() {
    final FantastleObjectModelList list = FantastleReboot.getBagOStuff()
        .getObjects();
    this.uidList = list.getAllCarryableUIDs();
    this.contents = new int[this.uidList.length];
    this.uses = new int[this.uidList.length][this.MAX_QUANTITY];
  }

  // Accessors
  public int getItemCount(final FantastleObjectModel mo) {
    final int loc = this.indexOf(mo);
    return this.contents[loc];
  }

  public int getUses(final FantastleObjectModel mo) {
    final int loc = this.indexOf(mo);
    return this.uses[loc][this.contents[loc]];
  }

  private void setUses(final FantastleObjectModel mo, final int newUses) {
    final int loc = this.indexOf(mo);
    this.uses[loc][this.contents[loc]] = newUses;
  }

  public void use(final FantastleObjectModel mo) {
    int tempUses = this.getUses(mo);
    if (mo.isUsable() && tempUses > 0) {
      tempUses--;
      this.setUses(mo, tempUses);
      mo.use();
      if (tempUses == 0) {
        this.removeItem(mo);
      }
    }
  }

  public boolean isItemThere(final FantastleObjectModel mo) {
    final int loc = this.indexOf(mo);
    if (loc != -1) {
      if (this.contents[loc] != 0) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public void addItem(final FantastleObjectModel mo) {
    final int loc = this.indexOf(mo);
    if (this.contents[loc] != -1) {
      this.contents[loc]++;
    }
  }

  public void removeItem(final FantastleObjectModel mo) {
    final int loc = this.indexOf(mo);
    if (this.contents[loc] > 0) {
      this.contents[loc]--;
    }
  }

  // Helper methods
  private int indexOf(final FantastleObjectModel mo) {
    int x;
    for (x = 0; x < this.contents.length; x++) {
      if (mo.getUniqueID() == this.uidList[x]) {
        return x;
      }
    }
    return -1;
  }

  @Override
  public ObjectInventory clone() {
    final ObjectInventory clone = new ObjectInventory();
    for (int x = 0; x < this.contents.length; x++) {
      clone.contents[x] = this.contents[x];
    }
    for (int x = 0; x < this.contents.length; x++) {
      for (int y = 0; y < this.MAX_QUANTITY; y++) {
        clone.uses[x][y] = this.uses[x][y];
      }
    }
    return clone;
  }

  public static ObjectInventory readInventory(final XDataReader reader,
      final int formatVersion) throws IOException {
    final ObjectInventory i = new ObjectInventory();
    for (int x = 0; x < i.contents.length; x++) {
      i.contents[x] = reader.readInt();
    }
    for (int x = 0; x < i.contents.length; x++) {
      for (int y = 0; y < i.MAX_QUANTITY; y++) {
        i.uses[x][y] = reader.readInt();
      }
    }
    return i;
  }

  public void writeInventory(final XDataWriter writer) throws IOException {
    for (final int content : this.contents) {
      writer.writeInt(content);
    }
    for (int x = 0; x < this.contents.length; x++) {
      for (int y = 0; y < this.MAX_QUANTITY; y++) {
        writer.writeInt(this.uses[x][y]);
      }
    }
  }
}
