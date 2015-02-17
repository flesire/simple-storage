package net.simplestorage.storage.impl.indexed;

import net.simplestorage.exception.StorageException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class IndexedStorageFile extends RandomAccessFile {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private String charSet = DEFAULT_CHARSET;

    public IndexedStorageFile(String name) throws FileNotFoundException {
        super(name, "rw");
    }

    public IndexedStorageFile(String name, String charSet) throws FileNotFoundException {
        super(name, "rw");
        this.charSet = charSet;
    }

    public String read(long position, int length) throws StorageException {
        final byte[] buffer = new byte[length];
        try {
            this.seek(position);
            this.readFully(buffer);
            return new String(buffer);
        } catch (IOException e) {
            throw new StorageException(String.format("Unable to read record at position %d", position));
        }
    }

    public void write(String line, long position) throws StorageException {

        try {
            this.seek(position);
            final byte[] data = asByteArray(line, DEFAULT_CHARSET);
            this.seek(position);
            this.write(data);
            //this.writeChars(line);
        } catch (IOException e) {
            throw StorageException.INSERT_FAILED;
        }
    }

    public void append(String line) throws StorageException {
        try {
            final byte[] data = asByteArray(line, DEFAULT_CHARSET);
            this.write(data);
        } catch (IOException e) {
            throw StorageException.INSERT_FAILED;
        }
    }


    public void clear(long position, int length) throws StorageException {
        try {
            byte[] buffer = new byte[length];
            if(position + length >= this.length()) {
                Arrays.fill(buffer, (byte)' ');
                buffer = new byte[0];
            } else {
                Arrays.fill(buffer, (byte) ' ');
            }
            this.seek(position);
            this.write(buffer);
            this.setLength(position);
        } catch (IOException e) {
            throw new StorageException(e.getMessage());
        }
    }

    private byte[] asByteArray(String line, String charSet) throws UnsupportedEncodingException {
        return line.toString().getBytes(charSet);
    }

}
