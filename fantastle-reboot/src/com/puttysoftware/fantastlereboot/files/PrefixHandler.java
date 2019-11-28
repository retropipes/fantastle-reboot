package com.puttysoftware.fantastlereboot.files;

import java.io.IOException;

import com.puttysoftware.fantastlereboot.files.versions.WorldVersionException;
import com.puttysoftware.fantastlereboot.files.versions.WorldVersions;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class PrefixHandler implements PrefixIO {
  @Override
  public int readPrefix(final XDataReader reader) throws IOException {
    final int formatVer = PrefixHandler.readFormatVersion(reader);
    final boolean res = WorldVersions.isCompatible(formatVer);
    if (!res) {
      throw new WorldVersionException(formatVer);
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
    writer.writeInt(WorldVersions.LATEST);
  }
}
