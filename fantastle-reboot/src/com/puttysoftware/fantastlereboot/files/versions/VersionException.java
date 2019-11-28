package com.puttysoftware.fantastlereboot.files.versions;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@SuppressWarnings("serial")
public abstract class VersionException extends RuntimeException {
  VersionException(final String message) {
    super(message);
  }

  @SuppressWarnings("static-method")
  private void writeObject(ObjectOutputStream out) throws IOException {
    throw new NotSerializableException();
  }

  @SuppressWarnings("static-method")
  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    throw new NotSerializableException();
  }
}
