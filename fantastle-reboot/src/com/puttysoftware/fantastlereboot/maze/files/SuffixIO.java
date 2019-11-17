package com.puttysoftware.fantastlereboot.maze.files;

import java.io.IOException;

import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public interface SuffixIO {
  void writeSuffix(XDataWriter writer) throws IOException;

  void readSuffix(XDataReader reader, int formatVersion) throws IOException;
}
