package net.simplestorage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageException extends Exception {
    public final static StorageException FILE_NOT_FOUND = new StorageException("File not found.");
    public final static StorageException INDEX_NOT_FOUND = new StorageException("Index not found.");
    public final static StorageException RECORD_NOT_FOUND = new StorageException(
            "RecordWrapper not found.");
    public final static StorageException INSERT_FAILED = new StorageException(
            "Unable to insert record");
    public final static StorageException UPDATE_FAILED = new StorageException(
            "Unable to update record");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public StorageException(String message) {
        super(message);
        logger.error(message, this);
    }
}
