package com.puttysoftware.fantastlereboot.legacyio;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class DataReader implements DataConstants {
    // Fields
    private final boolean dataMode;
    private BufferedReader br;
    private DataInputStream dis;
    private final File file;

    // Constructors
    public DataReader(final String filename) throws IOException {
        this.dataMode = DataConstants.DATA_MODE_TEXT;
        this.br = new BufferedReader(new FileReader(filename));
        this.dis = null;
        this.file = new File(filename);
    }

    public DataReader(final File filename) throws IOException {
        this.dataMode = DataConstants.DATA_MODE_TEXT;
        this.br = new BufferedReader(new FileReader(filename));
        this.dis = null;
        this.file = filename;
    }

    public DataReader(final String filename, final boolean mode)
            throws IOException {
        this.dataMode = mode;
        if (mode == DataConstants.DATA_MODE_TEXT) {
            this.br = new BufferedReader(new FileReader(filename));
        } else {
            this.dis = new DataInputStream(new FileInputStream(filename));
        }
        this.file = new File(filename);
    }

    public DataReader(final File filename, final boolean mode)
            throws IOException {
        this.dataMode = mode;
        if (mode == DataConstants.DATA_MODE_TEXT) {
            this.br = new BufferedReader(new FileReader(filename));
        } else {
            this.dis = new DataInputStream(new FileInputStream(filename));
        }
        this.file = filename;
    }

    // Methods
    public File getFile() {
        return this.file;
    }

    public void close() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            this.br.close();
        } else {
            this.dis.close();
        }
    }

    public int readInt() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            return Integer.parseInt(this.br.readLine());
        } else {
            return this.dis.readInt();
        }
    }

    public float readFloat() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            return Float.parseFloat(this.br.readLine());
        } else {
            return this.dis.readFloat();
        }
    }

    public double readDouble() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            return Double.parseDouble(this.br.readLine());
        } else {
            return this.dis.readDouble();
        }
    }

    public long readLong() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            return Long.parseLong(this.br.readLine());
        } else {
            return this.dis.readLong();
        }
    }

    public byte readByte() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            return Byte.parseByte(this.br.readLine());
        } else {
            return this.dis.readByte();
        }
    }

    public boolean readBoolean() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            return Boolean.parseBoolean(this.br.readLine());
        } else {
            return this.dis.readBoolean();
        }
    }

    public short readShort() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            return Short.parseShort(this.br.readLine());
        } else {
            return this.dis.readShort();
        }
    }

    public String readString() throws IOException {
        if (this.dataMode == DataConstants.DATA_MODE_TEXT) {
            return this.br.readLine();
        } else {
            return this.dis.readUTF();
        }
    }
}
