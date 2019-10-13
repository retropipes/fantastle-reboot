package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

final class CustomFlags {
    // Fields
    private ArrayList<Boolean> flags;

    // Constructor
    public CustomFlags() {
        this.flags = new ArrayList<>();
    }

    // Methods
    public int length() {
        return this.flags.size();
    }

    public boolean add(final int count) {
        if (this.flags.size() <= count) {
            return false;
        }
        this.flags.addAll(Collections.nCopies(count, false));
        return true;
    }

    public void append(final int count) {
        this.flags.addAll(Collections.nCopies(count, false));
    }

    public void appendOne() {
        this.flags.add(false);
    }

    public Optional<Boolean> get(final int index) {
        if (this.flags.size() <= index) {
            return Optional.empty();
        }
        return Optional.of(this.flags.get(index));
    }

    public boolean toggle(final int index) {
        if (this.flags.size() <= index) {
            return false;
        }
        this.flags.set(index, !this.flags.get(index));
        return true;
    }

    public boolean set(final int index, final boolean value) {
        if (this.flags.size() <= index) {
            return false;
        }
        this.flags.set(index, value);
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.flags);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CustomFlags)) {
            return false;
        }
        CustomFlags other = (CustomFlags) obj;
        return Objects.equals(this.flags, other.flags);
    }
}
