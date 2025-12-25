/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

final class CustomTexts {
    // Fields
    private final ArrayList<String> texts;

    // Constructor
    public CustomTexts() {
	this.texts = new ArrayList<>();
    }

    public boolean add(final int count) {
	if (this.texts.size() <= count) {
	    return false;
	}
	this.texts.addAll(Collections.nCopies(count, ""));
	return true;
    }

    public void addOne() {
	if (this.texts.size() == 0) {
	    this.texts.add("");
	}
    }

    public void append(final int count) {
	this.texts.addAll(Collections.nCopies(count, ""));
    }

    public void appendOne() {
	this.texts.add("");
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof final CustomTexts other)) {
	    return false;
	}
	return Objects.equals(this.texts, other.texts);
    }

    public String get(final int index) {
	return this.texts.get(index);
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.texts);
    }

    public int length() {
	return this.texts.size();
    }

    public boolean set(final int index, final String value) {
	if (this.texts.size() <= index) {
	    return false;
	}
	this.texts.set(index, value);
	return true;
    }
}
