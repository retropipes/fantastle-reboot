/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

final class CustomCounters {
    // Fields
    private final ArrayList<Integer> counters;

    // Constructor
    public CustomCounters() {
	this.counters = new ArrayList<>();
    }

    public boolean add(final int count) {
	if (this.counters.size() <= count) {
	    return false;
	}
	this.counters.addAll(Collections.nCopies(count, 0));
	return true;
    }

    public void addOne() {
	if (this.counters.size() == 0) {
	    this.counters.add(0);
	}
    }

    public void append(final int count) {
	this.counters.addAll(Collections.nCopies(count, 0));
    }

    public void appendOne() {
	this.counters.add(0);
    }

    public boolean decrement(final int index) {
	if (this.counters.size() <= index) {
	    return false;
	}
	this.counters.set(index, this.counters.get(index) - 1);
	return true;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof final CustomCounters other)) {
	    return false;
	}
	return Objects.equals(this.counters, other.counters);
    }

    public int get(final int index) {
	return this.counters.get(index);
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.counters);
    }

    public boolean increment(final int index) {
	if (this.counters.size() <= index) {
	    return false;
	}
	this.counters.set(index, this.counters.get(index) + 1);
	return true;
    }

    public int length() {
	return this.counters.size();
    }

    public boolean offset(final int index, final int value) {
	if (this.counters.size() <= index) {
	    return false;
	}
	this.counters.set(index, this.counters.get(index) + value);
	return true;
    }

    public boolean set(final int index, final int value) {
	if (this.counters.size() <= index) {
	    return false;
	}
	this.counters.set(index, value);
	return true;
    }
}
