package net.simplestorage.storage.impl.indexed;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class IndexedStorageFileTest {

    private IndexedStorageFile storageFile;

    @Before
    public void setUp() throws URISyntaxException, FileNotFoundException {
        File testFile = new File(ExistingIndexedStorageIntegrationTest.class.getResource("testIndexFile.txt").toURI());
        storageFile = new IndexedStorageFile(testFile.getAbsolutePath());
    }

    @Test
    public void testRead() throws Exception {
        String data = storageFile.read(0,7);
        assertEquals("test001", data);
        data = storageFile.read(7,7);
        assertEquals("test002", data);
    }

    @Test
    public void testWrite() throws Exception {
        assertEquals(14,storageFile.length());
        storageFile.write("test003", 14);
        assertEquals(21,storageFile.length());
        storageFile.clear(14,7);
    }


    @Test
    public void testAppend() throws Exception {
            assertEquals(14,storageFile.length());
            storageFile.append("test003");
            assertEquals(21,storageFile.length());
            storageFile.clear(14,7);
    }

    @Test
    public void testClear() throws Exception {

    }
}