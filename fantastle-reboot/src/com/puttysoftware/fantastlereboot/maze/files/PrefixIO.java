package com.puttysoftware.fantastlereboot.maze.files;

import java.io.IOException;

import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public interface PrefixIO {
  void writePrefix(XDataWriter writer) throws IOException;

  int readPrefix(XDataReader reader) throws IOException;
}
