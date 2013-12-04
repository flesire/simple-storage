package net.simplestorage.storage.impl.indexed;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class IndexFileTest {

    @Test
    public void createNewIndexFile() {
        IndexFile file = new IndexFile(System.getProperty("user.home") + "test.txt");
        assertThat(file.size(), is(0));
    }

    @Test
    public void addIndexes() {
        IndexFile<String> indexes = new IndexFile(System.getProperty("user.home") + "test.txt");
        indexes.open();
        indexes.add("key1", 10);
        indexes.add("key2", 15);
        indexes.add("key3", 12);
        indexes.close();
        indexes.open();
        assertIndex(indexes.get("key2"), "key2", 10, 15);
        assertIndex(indexes.get("key1"), "key1", 0, 10);
        assertIndex(indexes.get("key3"), "key3", 25, 12);
        indexes.close();
        indexes.open();
        indexes.add("key4", 20);
        assertIndex(indexes.get("key2"), "key2", 10, 15);
        assertIndex(indexes.get("key1"), "key1", 0, 10);
        assertIndex(indexes.get("key3"), "key3", 25, 12);
        assertIndex(indexes.get("key4"), "key4", 37, 20);
        indexes.close();
    }

    @Test
    public void testLoad() throws Exception {
        File testFile = new File(ExistingIndexedStorageIntegrationTest.class.getResource("test.txt.idx").toURI());
        IndexFile file = new IndexFile(testFile);
        file.open();
        assertEquals(3, file.size());
    }

    private void assertIndex(Index<String> index, String key, long offset, int recordSize) {
        assertEquals(key, index.getKey());
        assertEquals(offset, index.getOffset());
        assertEquals(recordSize, index.getLength());
    }


}
