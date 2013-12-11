package net.simplestorage.storage.impl.indexed;

import net.simplestorage.exception.StorageException;
import net.simplestorage.storage.Storage;
import net.simplestorage.storage.test.util.CSVDataRecordMapper;
import net.simplestorage.storage.test.util.DataRecord;
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
        storage = new IndexedStorage<String, DataRecord>(testFile.getAbsolutePath(), new CSVDataRecordMapper());
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

}
