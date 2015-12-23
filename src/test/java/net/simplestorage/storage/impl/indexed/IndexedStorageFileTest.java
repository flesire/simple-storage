package net.simplestorage.storage.impl.indexed;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class IndexedStorageFileTest {

    private IndexedStorageFile storageFile;

    private File testFile;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        testFile = new File(ExistingIndexedStorageIntegrationTest.class.getResource("testIndexFile.txt").toURI());
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(testFile));
        stream.write("test001test002".toString().getBytes("UTF-8"));
        stream.close();
        storageFile = new IndexedStorageFile(testFile.getAbsolutePath());
    }

    @After
    public void tearDown() {
        testFile.delete();
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
}