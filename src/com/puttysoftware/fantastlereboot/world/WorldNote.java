/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.world;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;

class WorldNote {
    // Fields
    private String contents;

    // Constructor
    public WorldNote() {
	this.contents = "Empty Note";
    }

    // Methods
    public String getContents() {
	return this.contents;
    }

    public void setContents(final String newContents) {
	this.contents = newContents;
    }

    static WorldNote readNote(final DataIOReader reader) throws IOException {
	final WorldNote mn = new WorldNote();
	mn.contents = reader.readString();
	return mn;
    }

    void writeNote(final DataIOWriter writer) throws IOException {
	writer.writeString(this.contents);
    }
}
