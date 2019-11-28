/*  FantastleReboot: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.files;

import java.io.File;
import java.io.FilenameFilter;

public class WorldFinder implements FilenameFilter {
  @Override
  public boolean accept(final File f, final String s) {
    final String extension = WorldFinder.getExtension(s);
    if (extension != null) {
      if (extension.equals(FileExtensions.getWorldExtension())) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  private static String getExtension(final String s) {
    String ext = null;
    final int i = s.lastIndexOf('.');
    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(i + 1).toLowerCase();
    }
    return ext;
  }
}
