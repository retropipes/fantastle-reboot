package net.worldwizard.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DirectoryCopier {
    // If targetLocation does not exist, it will be created.
    public static void copyDirectory(final File sourceLocation,
            final File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            final String[] children = sourceLocation.list();
            for (final String element : children) {
                DirectoryCopier.copyDirectory(new File(sourceLocation, element),
                        new File(targetLocation, element));
            }
        } else {
            try (final InputStream in = new FileInputStream(sourceLocation);
                    final OutputStream out = new FileOutputStream(
                            targetLocation)) {
                // Copy the bits from instream to outstream
                final byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }
}
