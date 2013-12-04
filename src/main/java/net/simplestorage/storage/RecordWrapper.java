package net.simplestorage.storage;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class RecordWrapper {

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String DEFAULT_SEPARATOR = ";";

    public static final String DEFAULT_LIST_SEPARATOR = ",";

    private String original = "";

    private String[] fields;

    private String charSet = DEFAULT_CHARSET;

    private String separator = DEFAULT_SEPARATOR;

    /**
     * @param size
     */
    public RecordWrapper(int size) {
        fields = new String[size];
    }

    /**
     * @param record
     */
    public RecordWrapper(String record) {
        original = record;
        fields = record.split(DEFAULT_SEPARATOR);
    }

    /**
     * @param record
     * @throws java.io.UnsupportedEncodingException
     */
    public RecordWrapper(byte[] record) throws UnsupportedEncodingException {
        original = new String(record, DEFAULT_CHARSET);
        fields = original.split(DEFAULT_SEPARATOR);
    }

    // Setters

    public void set(String value, int index) {
        fields[index] = value;
    }

    public void set(Long value, int index) {
        fields[index] = String.valueOf(value);
    }

    public void set(Boolean value, int index) {
        fields[index] = String.valueOf(value);
    }

    public void set(List<String> list, int index) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (String string : list) {
            builder.append(string);
            if (count < fields.length - 1) {
                builder.append(DEFAULT_LIST_SEPARATOR);
            }
            count++;
        }
        fields[index] = builder.toString();
    }

    // Getters

    public String getString(int index) {
        return fields[index];
    }

    public Long getLong(int index) {
        return Long.valueOf(fields[index]);
    }

    public Boolean getBoolean(int index) {
        return Boolean.valueOf(fields[index]);
    }

    public List<String> getList(int index) {
        return Arrays.asList(fields[index].split(DEFAULT_LIST_SEPARATOR));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (String field : fields) {
            builder.append(field);
            if (count < fields.length - 1) {
                builder.append(DEFAULT_SEPARATOR);
            }
            count++;
        }
        return builder.toString();
    }

    public byte[] asByteArray(String charSet) throws UnsupportedEncodingException {
        return this.toString().getBytes(charSet);
    }

    public boolean contains(String value) {
        return original.contains(value);
    }

    public boolean contains(String value, int fieldIndex) {
        return fields[fieldIndex].equals(value);
    }

    public int size() {
        return fields.length;
    }

    public boolean isEmpty() {
        if (fields == null || fields.length == 0 || isAllFieldsEmpty()) {
            return true;
        }
        return false;
    }

    // Private methods

    private boolean isAllFieldsEmpty() {
        int emptyFields = 0;
        for (String field : fields) {
            if (field == null || field.isEmpty()) {
                emptyFields++;
            }
        }
        return (emptyFields == fields.length);
    }


}
