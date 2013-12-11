package net.simplestorage.storage.mapper;

import net.simplestorage.storage.Record;

public interface RecordMapper<R extends Record> {

    /**
     * Map content of a source string to a record model.
     *
     * @param source The source.
     * @return A record object.
     */
    R map(String source);

    /**
     * Map content of a record object to a string.
     *
     * @param record The record model.
     * @return A string representation of the record.
     */
    String map(R record);

}
