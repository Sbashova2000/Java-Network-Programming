package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FunctionClassTest {

    @BeforeEach
    void cleanUnzippedFolder() {
        // Define the destination directory
        String destDirectory = "src/test/resources/unzipped";
        // Delete the unzipped files
        Path destDir = Path.of(destDirectory);
        if (destDir.toFile().exists()) {
            for (File file : destDir.toFile().listFiles()) {
                file.delete();
            }
        }
    }

    @Test
    void testUnzip() {
        // Define the name of the zip file
        String zipFilePath = "src/test/resources/test.zip";
        // Define the destination directory
        String destDirectory = "src/test/resources/unzipped";
        // Call the method to be tested
        FunctionClass.unzip(zipFilePath, destDirectory);
        // Define the expected files
        List<String> expectedFiles = new ArrayList<>();
        expectedFiles.add("src/test/resources/unzipped/file1.txt");
        expectedFiles.add("src/test/resources/unzipped/file2.txt");
        expectedFiles.add("src/test/resources/unzipped/file3.txt");
        // Check if the files are unzipped
        for (String file : expectedFiles) {
            assertTrue(Path.of(file).toFile().exists());
        }
    }
}
