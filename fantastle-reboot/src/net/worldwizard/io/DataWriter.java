package net.worldwizard.io;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter implements DataConstants {
    // Fields
    private final boolean dataMode;
    private BufferedWriter bw;
    private DataOutputStream dos;
    private final File file;
    private static final String END_OF_LINE = "\n";

    // Constructors
    public DataWriter(final String filename) throws IOException {
        this.dataMode = DataConstants.DATA_MODE_TEXT;
        this.bw = new BufferedWriter(new FileWriter(filename));
        this.dos = null;
        this.file = new File(filename);
    }

    public DataWriter(final File filename) throws IOException {
        this.dataMode = DataConstants.DATA_MODE_TEXT;
        this.bw = new BufferedWriter(new FileWriter(filename));
        this.dos = null;
        this.file = filename;
    }

    public DataWriter(final String filename, final boolean mode)
            throws IOException {
        this.dataMode = mode;
        if (mode == DataConstants.DATA_MODE_TEXT) {
            this.bw = new BufferedWriter(new FileWriter(filename));
        } else {
            this.dos = new DataOutputStream(new FileOutputStream(filename));
        }
        this.file = new File(filename);
    }

    public DataWriter(final File filename, final boolean mode)
            throws IOException {
        this.dataMode = mode;
        if (mode == DataConstants.DATA_MODE_TEXT) {
            this.bw = new BufferedWriter(new FileWriter(filename));
        } else {
            this.dos = new DataOutputStream(new FileOutputStream(filename));
        }
        this.file = filename;
    }

    // Methods
    public File getFile() {
        return this.file;
    }

    public void close() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.close();
        } else {
            this.dos.close();
        }
    }

    public void writeInt(final int i) throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.write(Integer.toString(i) + DataWriter.END_OF_LINE);
        } else {
            this.dos.writeInt(i);
        }
    }

    public void writeFloat(final float f) throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.write(Float.toString(f) + DataWriter.END_OF_LINE);
        } else {
            this.dos.writeFloat(f);
        }
    }

    public void writeDouble(final double d) throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.write(Double.toString(d) + DataWriter.END_OF_LINE);
        } else {
            this.dos.writeDouble(d);
        }
    }

    public void writeLong(final long l) throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.write(Long.toString(l) + DataWriter.END_OF_LINE);
        } else {
            this.dos.writeLong(l);
        }
    }

    public void writeByte(final byte b) throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.write(Byte.toString(b) + DataWriter.END_OF_LINE);
        } else {
            this.dos.writeByte(b);
        }
    }

    public void writeBoolean(final boolean b) throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.write(Boolean.toString(b) + DataWriter.END_OF_LINE);
        } else {
            this.dos.writeBoolean(b);
        }
    }

    public void writeShort(final short s) throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.write(Short.toString(s) + DataWriter.END_OF_LINE);
        } else {
            this.dos.writeShort(s);
        }
    }

    public void writeString(final String s) throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.bw.write(s + DataWriter.END_OF_LINE);
        } else {
            this.dos.writeUTF(s);
        }
    }
}
