package org.example;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchiver {

    public static void archiveFiles(String archiveName, String... filenames) throws IOException {

        try (FileOutputStream fos = new FileOutputStream(archiveName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : filenames) {
                File file = new File(filePath);

                if (!file.exists()) {
                    System.out.println("File " + filePath + " does not exist, skipping.");
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(file)) {
                    // Create a new zip entry for each file
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    // Read the file and write to the zip output stream
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }

                    // Close the current entry
                    zos.closeEntry();
                } catch (IOException e) {
                    System.err.println("Error reading file " + filePath + ": " + e.getMessage());
                }

            }
            System.out.println("Zip archive created successfully at " + archiveName);        }
        catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }
    }

}
