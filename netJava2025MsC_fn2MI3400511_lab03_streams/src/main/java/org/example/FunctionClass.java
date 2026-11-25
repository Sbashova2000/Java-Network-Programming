package org.example;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FunctionClass {
    public static void unzip(String zipFilePath, String destDirectory) {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();  // Create output directory if it doesn't exist
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zis.getNextEntry();

            // Process each entry in the zip file
            while (entry != null) {
                File file = new File(destDirectory, entry.getName());

                // Handle directories and files separately
                if (entry.isDirectory()) {
                    file.mkdirs();
                }
                else {
                    // Ensure parent directory exists before writing file
                    new File(file.getParent()).mkdirs();

                    // Write file content
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                // Move to the next entry in the zip file
                zis.closeEntry();
                entry = zis.getNextEntry();
            }

            System.out.println("Unzipping completed successfully.");

        }
        catch (IOException e) {
            System.err.println("Error unzipping file: " + e.getMessage());
        }
    }

    protected static Path concatenateThreeFiles(String content1, String content2, String content3) throws IOException {
        String concatenated = "src/main/resources/unzipped/concatenated.txt";
        try (FileWriter writer = new FileWriter(concatenated);
             BufferedReader reader1 = new BufferedReader(new StringReader(content1));
             BufferedReader reader2 = new BufferedReader(new StringReader(content2));
             BufferedReader reader3 = new BufferedReader(new StringReader(content3))) {

            String line;
            while ((line = reader1.readLine()) != null) {
                writer.write(line + System.lineSeparator());
            }
            while ((line = reader2.readLine()) != null) {
                writer.write(line + System.lineSeparator());
            }
            while ((line = reader3.readLine()) != null) {
                writer.write(line + System.lineSeparator());
            }

            return Path.of(concatenated);
        }
    }

    protected static void clearResultDirectoryAndItsChildSubdirectories() {
        File resultDirectory = new File("src/main/result");
        if (resultDirectory.exists()) {
            File[] files = resultDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        clearDirectory(file);
                    }
                    else {
                        file.delete();
                    }
                }
            }
        }
    }

    private static void clearDirectory(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        file.delete();
    }

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        clearResultDirectoryAndItsChildSubdirectories();

        //create zip archive from 3 files
        Path file1 = Path.of(Objects.requireNonNull(FunctionClass.class.getResource("/file1.txt")).toURI());
        Path file2 = Path.of(Objects.requireNonNull(FunctionClass.class.getResource("/file2.txt")).toURI());
        Path file3 = Path.of(Objects.requireNonNull(FunctionClass.class.getResource("/file3.txt")).toURI());


        String zipFilePath = "src/main/result/archive.zip";
        ZipArchiver.archiveFiles(zipFilePath, file1.toString(), file2.toString(), file3.toString());

        //unzip the archive
        String destDirectory = "src/main/result/unzipped";
        unzip(zipFilePath, destDirectory);

        //concatenate the content of the 3 files
        String f2 = "src/main/result/unzipped/file2.txt";
        String f3 = "src/main/result/unzipped/file3.txt";
        String f1 = "src/main/result/unzipped/file1.txt";

        String content1 = Files.readString(Path.of(f1));
        String content2 = Files.readString(Path.of(f2));
        String content3 = Files.readString(Path.of(f3));
        Path concatenated = concatenateThreeFiles(content1, content2, content3);

        //create zip archive from the concatenated file and the 3 original files
        String zipFilePathFinal = "src/main/result/finalArchive.zip";
        ZipArchiver.archiveFiles(zipFilePathFinal, file1.toString(), file2.toString(), file3.toString(), concatenated.toString());
    }
}
