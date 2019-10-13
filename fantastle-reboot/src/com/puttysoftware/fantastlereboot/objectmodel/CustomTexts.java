package com.puttysoftware.fantastlereboot.objectmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

final class CustomTexts {
    // Fields
    private ArrayList<String> texts;

    // Constructor
    public CustomTexts() {
        this.texts = new ArrayList<>();
    }

    // Methods
    public int length() {
        return this.texts.size();
    }

    public boolean add(final int count) {
        if (this.texts.size() <= count) {
            return false;
        }
        this.texts.addAll(Collections.nCopies(count, ""));
        return true;
    }

    public void append(final int count) {
        this.texts.addAll(Collections.nCopies(count, ""));
    }

    public void appendOne() {
        this.texts.add("");
    }

    public Optional<String> get(final int index) {
        if (this.texts.size() <= index) {
            return Optional.empty();
        }
        return Optional.of(this.texts.get(index));
    }

    public boolean set(final int index, final String value) {
        if (this.texts.size() <= index) {
            return false;
        }
        this.texts.set(index, value);
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.texts);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CustomTexts)) {
            return false;
        }
        CustomTexts other = (CustomTexts) obj;
        return Objects.equals(this.texts, other.texts);
    }
}
