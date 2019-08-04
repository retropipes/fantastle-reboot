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
package com.puttysoftware.fantastlereboot.generic;

public abstract class GenericPlug extends GenericInfiniteKey {
    // Fields
    private char letter;

    protected GenericPlug(final char newLetter) {
        super();
        this.letter = Character.toUpperCase(newLetter);
    }

    @Override
    public GenericPlug clone() {
        final GenericPlug copy = (GenericPlug) super.clone();
        copy.letter = this.letter;
        return copy;
    }

    @Override
    public String getName() {
        return this.letter + " Plug";
    }

    @Override
    public String getIdentifier4() {
        return this.letter + " Key";
    }

    @Override
    public String getPluralName() {
        return this.letter + " Plugs";
    }

    @Override
    public byte getGroupID() {
        return (byte) 10;
    }

    @Override
    public byte getObjectID() {
        switch (this.letter) {
        case 'A':
            return (byte) 1;
        case 'B':
            return (byte) 2;
        case 'C':
            return (byte) 3;
        case 'D':
            return (byte) 4;
        case 'E':
            return (byte) 5;
        case 'F':
            return (byte) 6;
        case 'G':
            return (byte) 7;
        case 'H':
            return (byte) 8;
        case 'I':
            return (byte) 9;
        case 'J':
            return (byte) 10;
        case 'K':
            return (byte) 11;
        case 'L':
            return (byte) 12;
        case 'M':
            return (byte) 13;
        case 'N':
            return (byte) 14;
        case 'O':
            return (byte) 15;
        case 'P':
            return (byte) 16;
        case 'Q':
            return (byte) 17;
        case 'R':
            return (byte) 18;
        case 'S':
            return (byte) 19;
        case 'T':
            return (byte) 20;
        case 'U':
            return (byte) 21;
        case 'V':
            return (byte) 22;
        case 'W':
            return (byte) 23;
        case 'X':
            return (byte) 24;
        case 'Y':
            return (byte) 25;
        case 'Z':
            return (byte) 26;
        default:
            return (byte) 0;
        }
    }

    @Override
    protected void setTypes() {
        this.type.set(TypeConstants.TYPE_LETTER_KEY);
        this.type.set(TypeConstants.TYPE_INFINITE_KEY);
        this.type.set(TypeConstants.TYPE_KEY);
        this.type.set(TypeConstants.TYPE_INVENTORYABLE);
        this.type.set(TypeConstants.TYPE_CONTAINABLE);
    }

    @Override
    public String getDescription() {
        return this.letter + " Plugs open " + this.letter
                + " Ports, and can be used infinitely many times.";
    }
}