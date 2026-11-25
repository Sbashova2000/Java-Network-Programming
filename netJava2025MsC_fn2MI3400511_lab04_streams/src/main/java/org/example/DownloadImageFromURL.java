package org.example;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadImageFromURL {

    public static void main(String[] args) {
        String imageUrl =
                //"https://i0.wp.com/www.benchresources.net/wp-content/uploads/2018/11/DemoStudentClass.png?ssl=1";
                "https://img.freepik.com/premium-psd/colorful-butterfly-realistic-isolated-transparent-background-png-file_1304044-3924.jpg?semt=ais_hybrid&w=740&q=80";

        Path destination = Path.of("image.png");

        try {
            downloadImage(imageUrl, destination);
            System.out.println("Снимката е свалена успешно.");
        } catch (Exception e) {
            System.out.println("Грешка: " + e.getMessage());
        }
    }

    public static void downloadImage(String imageUrl, Path destination) throws Exception {
        URL url = new URL(imageUrl);

        try (InputStream in = url.openStream()) {
            Files.copy(in, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
