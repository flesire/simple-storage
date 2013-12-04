package net.simplestorage.storage.impl.indexed;

public class Index<K> {
    public static final String FIELD_SEPARATOR = ";";
    /**
     * The key that identify the record
     */
    private K key;
    /**
     * Offset of the record in the file
     */
    private long offset;
    /**
     * length of the record
     */
    private int length;

    public Index(K key, long offset, int length) {
        this.key = key;
        this.offset = offset;
        this.length = length;
    }

    public K getKey() {
        return key;
    }

    public long getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Index index = (Index) o;

        if (length != index.length) return false;
        if (offset != index.offset) return false;
        if (key != null ? !key.equals(index.key) : index.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (int) (offset ^ (offset >>> 32));
        result = 31 * result + length;
        return result;
    }

    public String asString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(key).append(FIELD_SEPARATOR).append(offset).append(FIELD_SEPARATOR).append(length);
        return buffer.toString();
    }

    public void fromString(String string) {
        String[] fields = string.split(FIELD_SEPARATOR);
        this.key = (K) fields[0];
        this.offset = Long.valueOf(fields[1]);
        this.length = Integer.valueOf(fields[2]);
    }

}
