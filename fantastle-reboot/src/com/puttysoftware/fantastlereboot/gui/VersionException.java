package com.puttysoftware.fantastlereboot.gui;

import java.io.IOException;

public class VersionException extends IOException {
  private static final long serialVersionUID = 7521249394165201264L;

  public VersionException(final String message) {
    super(message);
  }
}
