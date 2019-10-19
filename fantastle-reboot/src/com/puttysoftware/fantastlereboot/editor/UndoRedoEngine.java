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
package com.puttysoftware.fantastlereboot.editor;

import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;

public class UndoRedoEngine {
    // Fields
    private final LinkStack undoHistory;
    private final LinkStack redoHistory;
    private FantastleObjectModel object;
    private int destX, destY, destZ, destW, destE;

    // Constructors
    public UndoRedoEngine() {
        this.undoHistory = new LinkStack();
        this.redoHistory = new LinkStack();
        this.object = null;
        this.destX = -1;
        this.destY = -1;
        this.destZ = -1;
        this.destW = -1;
        this.destE = -1;
    }

    // Public methods
    public void undo() {
        if (!this.undoHistory.isEmpty()) {
            final Link entry = this.undoHistory.pop();
            this.object = entry.mo;
            this.destX = entry.coordX;
            this.destY = entry.coordY;
            this.destZ = entry.coordZ;
            this.destW = entry.coordW;
            this.destE = entry.coordE;
        } else {
            this.object = null;
            this.destX = -1;
            this.destY = -1;
            this.destZ = -1;
            this.destW = -1;
            this.destE = -1;
        }
    }

    public void redo() {
        if (!this.redoHistory.isEmpty()) {
            final Link entry = this.redoHistory.pop();
            this.object = entry.mo;
            this.destX = entry.coordX;
            this.destY = entry.coordY;
            this.destZ = entry.coordZ;
            this.destW = entry.coordW;
            this.destE = entry.coordE;
        } else {
            this.object = null;
            this.destX = -1;
            this.destY = -1;
            this.destZ = -1;
            this.destW = -1;
            this.destE = -1;
        }
    }

    public boolean tryUndo() {
        return !this.undoHistory.isEmpty();
    }

    public boolean tryRedo() {
        return !this.redoHistory.isEmpty();
    }

    public boolean tryBoth() {
        return this.undoHistory.isEmpty() && this.redoHistory.isEmpty();
    }

    public void updateUndoHistory(final FantastleObjectModel obj, final int x,
            final int y, final int z, final int w, final int e) {
        this.undoHistory.push(obj, x, y, z, w, e);
    }

    public void updateRedoHistory(final FantastleObjectModel obj, final int x,
            final int y, final int z, final int w, final int e) {
        this.redoHistory.push(obj, x, y, z, w, e);
    }

    public FantastleObjectModel getObject() {
        return this.object;
    }

    public int getX() {
        return this.destX;
    }

    public int getY() {
        return this.destY;
    }

    public int getZ() {
        return this.destZ;
    }

    public int getW() {
        return this.destW;
    }

    public int getE() {
        return this.destE;
    }

    // Inner classes
    private class Link {
        // Fields
        public FantastleObjectModel mo;
        public int coordX, coordY, coordZ, coordW, coordE;
        public Link next;

        public Link(final FantastleObjectModel obj, final int x, final int y, final int z,
                final int w, final int e) {
            this.mo = obj;
            this.coordX = x;
            this.coordY = y;
            this.coordZ = z;
            this.coordW = w;
            this.coordE = e;
            this.next = null;
        }
    }

    private class LinkList {
        // Fields
        private Link first;

        public LinkList() {
            this.first = null;
        }

        public boolean isEmpty() {
            return this.first == null;
        }

        public void insertFirst(final FantastleObjectModel obj, final int x, final int y,
                final int z, final int w, final int e) {
            final Link newLink = new Link(obj, x, y, z, w, e);
            newLink.next = this.first;
            this.first = newLink;
        }

        public Link deleteFirst() {
            final Link temp = this.first;
            this.first = this.first.next;
            return temp;
        }
    }

    private class LinkStack {
        // Fields
        private final LinkList theList;

        public LinkStack() {
            this.theList = new LinkList();
        }

        public void push(final FantastleObjectModel obj, final int x, final int y,
                final int z, final int w, final int e) {
            this.theList.insertFirst(obj, x, y, z, w, e);
        }

        public Link pop() {
            return this.theList.deleteFirst();
        }

        public boolean isEmpty() {
            return this.theList.isEmpty();
        }
    }
}
