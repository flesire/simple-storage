package net.simplestorage.storage.impl.indexed;

import net.simplestorage.exception.StorageException;
import net.simplestorage.storage.StringRecordWrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class IndexedStorageFile extends RandomAccessFile {

    public static final String DEFAULT_CHARSET = "UTF-8";

    public IndexedStorageFile(String name) throws FileNotFoundException {
        super(name, "rw");
    }

    public StringRecordWrapper read(long position, int length) throws StorageException {
        final byte[] buffer = new byte[length];
        try {
            this.seek(position);
            this.readFully(buffer);
            return new StringRecordWrapper(buffer);
        } catch (IOException e) {
            throw new StorageException(String.format("Unable to read record at position %d", position));
        }
    }

    public void write(StringRecordWrapper line, long position) throws StorageException {

        try {
            this.seek(position);
            final byte[] data = line.asByteArray(DEFAULT_CHARSET);
            this.seek(position);
            this.write(data);
        } catch (IOException e) {
            throw StorageException.INSERT_FAILED;
        }
    }

    public void append(StringRecordWrapper line) throws StorageException {
        try {
            final byte[] data = line.asByteArray(DEFAULT_CHARSET);
            this.write(data);
        } catch (IOException e) {
            throw StorageException.INSERT_FAILED;
        }
    }


    public void clear(long position, int length) throws StorageException {
        final byte[] buffer = new byte[length];
        Arrays.fill(buffer, (byte) ' ');
        try {
            this.seek(position);
            this.write(buffer);
        } catch (IOException e) {
            throw new StorageException(e.getMessage());
        }
    }

}
