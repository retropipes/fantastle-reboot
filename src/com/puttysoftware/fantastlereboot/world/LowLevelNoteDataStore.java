/*  FantastleReboot: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.world;

import org.retropipes.diane.storage.ObjectStorage;

class LowLevelNoteDataStore extends ObjectStorage<WorldNote> {
    // Constructor
    LowLevelNoteDataStore(final int... shape) {
	super(shape);
    }

    // Methods
    public WorldNote getNote(final int... loc) {
	return this.getCell(loc);
    }

    public void setNote(final WorldNote obj, final int... loc) {
	this.setCell(obj, loc);
    }
}
