package net.simplestorage.storage.impl.indexed;

import net.simplestorage.exception.StorageException;
import net.simplestorage.storage.Record;
import net.simplestorage.storage.RecordWrapper;
import net.simplestorage.storage.Storage;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author flesire
 */
@Ignore
public class ExistingIndexedStorageIntegrationTest {

    private Storage<String, DataRecord> storage;

    @Before
    public void setUp() throws Exception {
        File testFile = new File(ExistingIndexedStorageIntegrationTest.class.getResource("test.txt").toURI());
        storage = new DataStorage(testFile.getAbsolutePath());
        storage.open();
    }

    @After
    public void tearDown() throws StorageException {
        storage.close();
    }

    @Test
    public void getStorageSize() throws StorageException {
        assertThat(storage.size(), is(3L));
    }

    @Test
    public void getRecord() throws StorageException {
        DataRecord record = storage.get("key2");
        assertNotNull(record);
    }

    @Test
    public void getUnknownRecord() throws StorageException {
        DataRecord record = storage.get("key4");
        assertNull(record);
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
            StringBuffer buffer = new StringBuffer();
            buffer.append(record.getKey()).append(";").append(record.getData());
            return buffer.toString();
        }

        @Override
        public DataRecord convert(RecordWrapper record) {
            return new DataRecord(record.getString(0), record.getString(1));
        }
    }

}
