package net.simplestorage.storage;

import net.simplestorage.exception.StorageException;

public interface Storage<K, R extends Record<K>> {


    /**
     * Open the store.
     *
     * @throws StorageException
     */
    void open() throws StorageException, StorageException;

    /**
     * @param key The key corresponding to the desired record.
     * @return The record object. If no record found or key is null, return null.
     * @throws StorageException
     */
    R get(K key) throws StorageException;


    /**
     * @param record The record object to add to the storage.
     * @return
     * @throws StorageException
     */
    R put(R record) throws StorageException;

    /**
     * @param record The record object to update.
     * @throws StorageException
     */
    void update(R record) throws StorageException;

    /**
     * Delete record with index.
     *
     * @param key The key corresponding to the record to be deleted.
     */
    void delete(K key);

    /**
     * Gets number of records present in the storage.
     *
     * @return Long number of records.
     */
    long size();

    /**
     * Save modifications without closing the store.
     */
    void flush();

    /**
     * Save modifications and close the store.
     *
     * @throws StorageException
     */
    void close() throws StorageException;
}
