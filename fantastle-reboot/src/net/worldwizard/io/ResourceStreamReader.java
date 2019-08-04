package net.worldwizard.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceStreamReader implements DataConstants {
    // Fields
    private final BufferedReader br;

    // Constructors
    public ResourceStreamReader(final InputStream is) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(is));
    }

    // Methods
    public void close() throws IOException {
        this.br.close();
    }

    public int readInt() throws IOException {
        return Integer.parseInt(this.br.readLine());
    }

    public float readFloat() throws IOException {
        return Float.parseFloat(this.br.readLine());
    }

    public double readDouble() throws IOException {
        return Double.parseDouble(this.br.readLine());
    }

    public long readLong() throws IOException {
        return Long.parseLong(this.br.readLine());
    }

    public byte readByte() throws IOException {
        return Byte.parseByte(this.br.readLine());
    }

    public boolean readBoolean() throws IOException {
        return Boolean.parseBoolean(this.br.readLine());
    }

    public short readShort() throws IOException {
        return Short.parseShort(this.br.readLine());
    }

    public String readString() throws IOException {
        return this.br.readLine();
    }
}
