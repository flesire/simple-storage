package net.simplestorage.storage.impl.indexed;

import net.simplestorage.exception.StorageException;
import net.simplestorage.storage.Record;
import net.simplestorage.storage.RecordWrapper;
import net.simplestorage.storage.Storage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author flesire
 */
public class NewIndexedStorageIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(NewIndexedStorageIntegrationTest.class);

    private File testFile;
    private Storage<String, DataRecord> storage;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("data", "txt");
        storage = new DataStorage(testFile.getAbsolutePath());

    }

    @After
    public void tearDown() {
        boolean result = testFile.delete();
        if (!result) {
            logger.debug("Cannot delete file : " + testFile.getAbsolutePath());
        }
    }

    @Test
    public void openNewStorage() throws StorageException {
        storage.open();
        assertThat(storage.size(), is(0L));
        storage.close();
    }

    @Test
    public void openNewStorageAndAddDeleteKey() throws StorageException {
        storage.open();
        assertThat(storage.size(), is(0L));
        //
        storage.put(new DataRecord("key1", "data1111"));
        assertThat(storage.size(), is(1L));
        storage.delete("key1");
        assertThat(storage.size(), is(0L));
        storage.close();
    }

    @Test
    public void openNewStorageAndAddUpdateKey() throws StorageException {
        storage.open();
        storage.put(new DataRecord("key1", "data1111"));
        storage.update(new DataRecord("key1", "data1112"));
        DataRecord updated = storage.get("key1");
        assertEquals("data1112", updated.getData());
        storage.close();
    }

    @Test
    public void openNewStorageAndAddUpdateKeyLongerRecord() throws StorageException {
        storage.open();
        storage.put(new DataRecord("key1", "data1111"));
        storage.update(new DataRecord("key1", "data11112"));
        DataRecord updated = storage.get("key1");
        assertEquals("data11112", updated.getData());
        assertThat(storage.size(), is(1L));
        storage.close();
    }

    @Test(expected = StorageException.class)
    public void openNewStorageAndAddDuplicateKeys() throws StorageException {
        try {
            storage.open();
            DataRecord record = new DataRecord("key1", "data1111");
            storage.put(record);
            storage.put(record);
        } finally {
            storage.close();
        }

    }

    class DataRecord implements Record<String> {

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

    class DataStorage extends IndexedStorage<String, DataRecord> {

        public DataStorage(String filename) {
            super(filename);
        }

        public String convert(DataRecord record) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(record.getKey()).append(";").append(record.getData());
            return buffer.toString();
        }

        @Override
        public DataRecord convert(RecordWrapper record) {
            return new DataRecord(record.getString(0), record.getString(1));
        }
    }

}
