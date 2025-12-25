package com.puttysoftware.fantastlereboot.files.versions;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class VersionException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 6414696022962587634L;

    VersionException(final String message) {
	super(message);
    }

    @SuppressWarnings("static-method")
    private void writeObject(@SuppressWarnings("unused") final ObjectOutputStream out) throws IOException {
	throw new NotSerializableException();
    }

    @SuppressWarnings("static-method")
    private void readObject(@SuppressWarnings("unused") final ObjectInputStream in) throws IOException {
	throw new NotSerializableException();
    }
}
