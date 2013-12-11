package net.simplestorage.storage.impl.indexed;

import net.simplestorage.exception.IndexException;
import net.simplestorage.exception.StorageException;
import net.simplestorage.storage.Record;
import net.simplestorage.storage.RecordWrapper;
import net.simplestorage.storage.Storage;
import net.simplestorage.storage.mapper.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class IndexedStorage<K, R extends Record<K>> implements Storage<K, R> {

    private Logger logger = LoggerFactory.getLogger(IndexedStorage.class);
    /**
     * The file name of the main storage file and index storage file
     */
    private String filename;
    /**
     * Storage for indexes
     */
    private IndexFile<K> indexes;
    /**
     * The physical file contains records
     */
    private IndexedStorageFile records;

    private RecordMapper<R> recordMapper;

    /**
     * Default constructor.
     *
     * @param filename The file name for both storage and index files.
     * @param recordMapper
     *
     */
    public IndexedStorage(final String filename, RecordMapper<R> recordMapper) {
        this.filename = filename;
        this.indexes = new IndexFile<K>(filename);
        this.recordMapper = recordMapper;
    }

    @Override
    public void open() throws StorageException {
        indexes.open();
        try {
            // Open storage
            records = new IndexedStorageFile(filename);
        } catch (FileNotFoundException e) {
            throw StorageException.FILE_NOT_FOUND;
        }
    }

    // Public methods

    @Override
    public R get(final K key) throws StorageException {
        if (key == null) {
            return null;
        }
        final Index index = indexes.get(key);
        if (index == null) {
            logger.info("No index found for key " + key);
            return null;
        }
        return readRecord(index);
    }

    @Override
    public R put(final R record) throws StorageException {
        if (record == null) {
            logger.warn("Try to insert a null record.");
            return record;
        }
        K key = record.getKey();
        if (indexes.get(key) != null) {
            throw new StorageException("Key must be unique. A record already exists with key " + key);
        }
        String line = recordMapper.map(record);
        records.append(line);
        indexes.add(key, line.length());
        return record;

    }

    @Override
    public void update(final R record) throws StorageException {
        if (record == null) {
            logger.warn("Try to update a null record.");
            return;
        }
        K key = record.getKey();
        Index previousIndex = indexes.get(key);
        String line = recordMapper.map(record);
        final int recordSize = line.length();
        if (recordSize > previousIndex.getLength()) {
            logger.warn("Updated record size is longer than previous record. The updated record will be save as a new one.");
            cleanCurrentRecord(previousIndex);
            records.append(line);
            indexes.add(key, line.length());
        } else {
            final long offset = previousIndex.getOffset();
            previousIndex = new Index(key, offset, recordSize);
            records.write(line, offset);
            indexes.update(previousIndex);

            logger.info("Record with key " + key + " is updated.");
        }
    }

    @Override
    public void delete(final K key) {
        if (key == null) {
            return;
        }
        indexes.remove(key);
    }

    @Override
    public long size() {
        return indexes.size();
    }

    @Override
    public void flush() {
        try {
            indexes.save();
        } catch (IndexException e) {
            logger.error("Error occurs when save indexes : ", e);
        }
    }

    @Override
    public void close() throws StorageException {
        indexes.close();
        try {
            records.close();
        } catch (IOException e) {
            throw new StorageException("Error occurs when closing storage file.");
        }
    }

    //

    /**
     * Read record object linked to the index.
     *
     * @param index The index to identify the expected record.
     * @return The record object.
     * @throws StorageException
     */
    private R readRecord(final Index index) throws StorageException {
        String record = records.read(index.getOffset(), index.getLength());
        return recordMapper.map(record);
    }

    private void cleanCurrentRecord(final Index index) throws StorageException {
        final long offset = index.getOffset();
        final int recordSize = index.getLength();
        records.clear(offset, recordSize);
        indexes.update(index);
    }

    class StorageFilterIterator implements Iterator<RecordWrapper> {


        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public RecordWrapper next() {
            return null;
        }

        @Override
        public void remove() {

        }
    }

}
