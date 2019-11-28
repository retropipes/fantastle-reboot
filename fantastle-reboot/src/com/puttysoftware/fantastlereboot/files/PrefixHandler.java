package com.puttysoftware.fantastlereboot.files;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.files.versions.MazeVersionException;
import com.puttysoftware.fantastlereboot.files.versions.MazeVersions;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class PrefixHandler implements PrefixIO {
  @Override
  public int readPrefix(final XDataReader reader) throws IOException {
    final int formatVer = PrefixHandler.readFormatVersion(reader);
    final boolean res = MazeVersions.isCompatible(formatVer);
    if (!res) {
      throw new MazeVersionException(formatVer);
    }
    return formatVer;
  }

  @Override
  public void writePrefix(final XDataWriter writer) throws IOException {
    PrefixHandler.writeFormatVersion(writer);
  }

  private static int readFormatVersion(final XDataReader reader)
      throws IOException {
    return reader.readInt();
  }

  private static void writeFormatVersion(final XDataWriter writer)
      throws IOException {
    writer.writeInt(MazeVersions.LATEST);
  }
}
