package net.simplestorage.storage;

public interface Record<K> {
    /**
     * @return The key to identify the record.
     */
    K getKey();

}
