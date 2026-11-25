package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZipArchiverTest {

    @BeforeEach
    public void beforeEach () {
        deleteFiles("archive.zip", "concatenated.txt");
    }

    public void deleteFiles(String... filenames) {
        for (String filename : filenames) {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }


    @Test
    public void testArchiveFiles() {
        try {
            // Create temporary files for testing
            File tempFile1 = File.createTempFile("tempFile1", ".txt");
            File tempFile2 = File.createTempFile("tempFile2", ".txt");
            File tempFile3 = File.createTempFile("tempFile3", ".txt");

            // Write some content to the temporary files
            Files.writeString(tempFile1.toPath(), "Content of file 1");
            Files.writeString(tempFile2.toPath(), "Content of file 2");
            Files.writeString(tempFile3.toPath(), "Content of file 3");

            // Define the name of the archive file
            String archiveName = "testArchive.zip";

            // Call the method to be tested
            ZipArchiver.archiveFiles(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath(), tempFile3.getAbsolutePath(), archiveName);

            // Assert that the archive file is created
            Assertions.assertTrue(Files.exists(Path.of(archiveName)));

            // Clean up temporary files
            tempFile1.delete();
            tempFile2.delete();
            tempFile3.delete();
            new File(archiveName).delete();

        } catch (IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }
}