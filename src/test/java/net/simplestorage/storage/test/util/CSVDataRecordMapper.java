package net.simplestorage.storage.test.util;

import net.simplestorage.storage.mapper.RecordMapper;

public class CSVDataRecordMapper implements RecordMapper<DataRecord> {
    @Override
    public DataRecord map(String source) {
        String[] fields = source.split(";");
        return new DataRecord(fields[0], fields[1]);
    }

    @Override
    public String map(DataRecord record) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(record.getKey()).append(";").append(record.getData());
        return buffer.toString();
    }
}
