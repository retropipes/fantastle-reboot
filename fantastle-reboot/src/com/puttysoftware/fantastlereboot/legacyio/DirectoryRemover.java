package com.puttysoftware.fantastlereboot.legacyio;

import java.io.File;
import java.io.IOException;

public class DirectoryRemover {
    public static void removeDirectory(final File location) throws IOException {
        if (location.isDirectory()) {
            final String[] children = location.list();
            for (final String element : children) {
                DirectoryRemover.removeDirectory(new File(location, element));
            }
            location.delete();
        } else {
            location.delete();
        }
    }
}