/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.simplestorage.storage.impl.csv;

import java.io.File;
import net.simplestorage.storage.impl.indexed.ExistingIndexedStorageIntegrationTest;
import net.simplestorage.storage.impl.indexed.IndexedStorage;
import net.simplestorage.storage.test.util.CSVDataRecordMapper;
import net.simplestorage.storage.test.util.DataRecord;
import org.junit.Before;

public class CsvStorageUnitTest {
    
    private CsvStorage<String, DataRecord> storage;
    
     @Before
    public void setUp() throws Exception {
        File testFile = new File(ExistingIndexedStorageIntegrationTest.class.getResource("test.txt").toURI());
        storage = new CsvStorage<String, DataRecord>();
        storage.open();
    }
    
    
    
}
