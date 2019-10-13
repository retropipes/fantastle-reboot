package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

final class CustomCounters {
    // Fields
    private ArrayList<Integer> counters;

    // Constructor
    public CustomCounters() {
        this.counters = new ArrayList<>();
    }

    // Methods
    public int length() {
        return this.counters.size();
    }

    public boolean add(final int count) {
        if (this.counters.size() <= count) {
            return false;
        }
        this.counters.addAll(Collections.nCopies(count, 0));
        return true;
    }

    public void append(final int count) {
        this.counters.addAll(Collections.nCopies(count, 0));
    }

    public void appendOne() {
        this.counters.add(0);
    }

    public Optional<Integer> get(final int index) {
        if (this.counters.size() <= index) {
            return Optional.empty();
        }
        return Optional.of(this.counters.get(index));
    }

    public boolean decrement(final int index) {
        if (this.counters.size() <= index) {
            return false;
        }
        this.counters.set(index, this.counters.get(index) - 1);
        return true;
    }

    public boolean increment(final int index) {
        if (this.counters.size() <= index) {
            return false;
        }
        this.counters.set(index, this.counters.get(index) + 1);
        return true;
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

    @Override
    public int hashCode() {
        return Objects.hash(this.counters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CustomCounters)) {
            return false;
        }
        CustomCounters other = (CustomCounters) obj;
        return Objects.equals(this.counters, other.counters);
    }
}
