package net.simplestorage.storage.impl.indexed;

import net.simplestorage.exception.StorageException;
import net.simplestorage.storage.Storage;
import net.simplestorage.storage.test.util.CSVDataRecordMapper;
import net.simplestorage.storage.test.util.DataRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class NewIndexedStorageIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(NewIndexedStorageIntegrationTest.class);

    private File testFile;
    private Storage<String, DataRecord> storage;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("data", ".txt");
        logger.info("Test file {} created.", testFile.getAbsolutePath());
        storage = new IndexedStorage<String, DataRecord>(testFile.getAbsolutePath(), new CSVDataRecordMapper());

    }

    @After
    public void tearDown() {
        boolean result = testFile.delete();
        if (!result) {
            logger.info("Cannot delete file : " + testFile.getAbsolutePath());
        } else {
            logger.info("Test file deleted.");
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
        String data = updated.getData();
        assertEquals("data1112", data);
        storage.close();
    }

    @Test
    public void openNewStorageAndAddUpdateKeyLongerRecord() throws StorageException {
        storage.open();
        storage.put(new DataRecord("key1", "data1111"));
        storage.put(new DataRecord("key3", "data1113"));
        storage.update(new DataRecord("key1", "data11112"));
        storage.flush();
        DataRecord updated = storage.get("key1");
        assertEquals("data11112", updated.getData());
        assertThat(storage.size(), is(2L));
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

}
