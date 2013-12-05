package net.simplestorage.storage.test.util;

import net.simplestorage.storage.Record;

public class DataRecord implements Record<String> {

    private String key;
    private String data;

    public DataRecord(String key, String data) {
        this.key = key;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String getKey() {
        return key;
    }
}
