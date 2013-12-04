package net.simplestorage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexException extends Exception {

    public final static IndexException INDEX_CREATION_FAILED = new IndexException(
            "Unable to create new index file to the filesystem.");
    public final static IndexException INDEX_READ_FAILED = new IndexException(
            "Unable to read the indexes from the filesystem.");
    public final static IndexException INDEX_WRITE_FAILED = new IndexException(
            "Unable to save the indexes to the filesystem.");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public IndexException(String message) {
        super(message);
        logger.error(message, IndexException.class);
    }

}
