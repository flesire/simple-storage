package net.simplestorage.storage.impl.indexed;

import net.simplestorage.exception.IndexException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import static net.simplestorage.exception.IndexException.INDEX_WRITE_FAILED;

public class IndexFile<K> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * The path and file name of the physical file contains indexes
     */
    private final File indexFile;
    /**
     * A Map contains all indexes
     */
    private final Map<K, Index<K>> indexes = new TreeMap<K, Index<K>>();
    /**
     * The last created index
     */
    private Index<K> lastIndex;

    /**
     * Default constructor.
     *
     * @param filename Name of the file contains indexes.
     */
    public IndexFile(String filename) {
        this(new File(filename + ".idx"));
    }

    public IndexFile(File file) {
        this.indexFile = file;
    }

    /**
     * Read all indexes from the file. If the file doesn't exists, create a new one.
     */
    public void open() {
        try {
            boolean indexExists = indexFile.exists();
            if (indexExists) {
                this.load();
            } else {
                indexFile.createNewFile();
            }
        } catch (IOException e) {
            logger.error("Opening index file failed :", e);
        }
    }

    /**
     * Persist index changes to the file.
     */
    public void close() {
        try {
            save();
        } catch (IndexException e) {
            logger.error("Closing index file failed :", e);
        }
    }

    /**
     * Add a new index.
     *
     * @param key          The identifier of the record link to the index.
     * @param recordLength The record length.
     * @return A new index.
     */
    public Index add(K key, int recordLength) {
        long offset = 0;
        if (lastIndex != null) {
            offset = lastIndex.getOffset() + lastIndex.getLength();
        }

        Index<K> i = new Index<>(key, offset, recordLength);
        indexes.put(key, i);
        logger.debug(String.format("Add index : %s, offset: %d, size: %d", key, offset, recordLength));
        lastIndex = i;
        return lastIndex;
    }

    /**
     * Return The identifier of the record link to the index.
     *
     * @param key The key.
     * @return The index.
     */
    public Index get(K key) {
        return indexes.get(key);
    }

    public void remove(K key) {
        indexes.remove(key);
    }

    public void update(Index<K> index) {
        indexes.put(index.getKey(), index);
    }

    public void update(K oldKey, Index<K> newIndex) {
        indexes.put(newIndex.getKey(), newIndex);
        indexes.remove(oldKey);
    }

    public Iterator<Index<K>> iterator() {
        return indexes.values().iterator();
    }

    public int size() {
        return indexes.size();
    }


    public void save() throws IndexException {
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter(indexFile));

            for (Index index : indexes.values()) {
                buffer.write(index.asString() + "\n");
            }
            buffer.flush();
            buffer.close();
        } catch (IOException e) {
            throw INDEX_WRITE_FAILED;
        }
    }

    /**
     * Load all indexes from the file in memory.
     *
     * @throws java.io.IOException
     */
    private void load() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile.getAbsoluteFile())));
        String record;
        long count = 0;
        while ((record = br.readLine()) != null) {
            String[] fields = record.split(";");
            Long offset = Long.valueOf(fields[1]);
            Integer length = Integer.valueOf(fields[2]);
            K key = (K) fields[0];
            Index newIndex = new Index(key, offset, length);
            indexes.put(key, newIndex);
            count++;
        }
        if (indexes.size() > 0) {
            lastIndex = Collections.max(indexes.values(), new IndexOffsetComparator());
        }
        br.close();
        logger.info(String.format("%d indexes loaded.", count));
    }

    class IndexOffsetComparator implements Comparator<Index<K>> {

        @Override
        public int compare(Index<K> o1, Index<K> o2) {
            if (o1 == null || o2 == null) {
                return 0;
            }
            Long offset1 = o1.getOffset();
            Long offset2 = o2.getOffset();
            return offset1.compareTo(offset2);
        }
    }

}
