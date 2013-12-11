package net.simplestorage.storage.test.util;

import net.simplestorage.storage.mapper.RecordMapper;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonDataRecordMapper implements RecordMapper<DataRecord> {

    @Override
    public DataRecord map(String source) {
        DataRecord record = null;
        try {
            record = new ObjectMapper().readValue(source, DataRecord.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return record;
    }

    @Override
    public String map(DataRecord record) {
        String line = null;
        try {
            line = new ObjectMapper().writeValueAsString(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
