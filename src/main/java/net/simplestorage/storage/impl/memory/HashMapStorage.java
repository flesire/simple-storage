package net.simplestorage.storage.impl.memory;

import net.simplestorage.exception.StorageException;
import net.simplestorage.storage.Record;
import net.simplestorage.storage.Storage;
import net.simplestorage.storage.impl.indexed.IndexFile;
import net.simplestorage.storage.mapper.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HashMapStorage<K, R extends Record<K>> implements Storage<K, R> {

    private Logger logger = LoggerFactory.getLogger(HashMapStorage.class);
    /**
     * The file name of the main storage file and index storage file
     */
    private String filename;
    /**
     * Storage for indexes
     */
    private IndexFile<K> indexes;

    private RecordMapper<R> recordMapper;

    private Map<K, R> records = new HashMap<>();

    public HashMapStorage(final String filename, RecordMapper<R> recordMapper) {
        this.filename = filename;
        this.recordMapper = recordMapper;
        this.records = new HashMap<>();
    }

    @Override
    public void open() throws StorageException, StorageException {

    }

    @Override
    public R get(K key) throws StorageException {
        return null;
    }

    @Override
    public R put(R record) throws StorageException {
        return null;
    }

    @Override
    public void update(R record) throws StorageException {

    }

    @Override
    public void delete(K key) {

    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws StorageException {

    }
}
