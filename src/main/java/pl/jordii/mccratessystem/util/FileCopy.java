package pl.jordii.mccratessystem.util;

import pl.jordii.mccratessystem.McCratesSystem;

import java.io.*;

public class FileCopy {

    private FileCopy() { //blokada tworzenia konstruktora
        throw new AssertionError();
    }

    public static void createFileFromResource(String resourceName, File file) {
        if (!file.exists()) {
            try (InputStream input = McCratesSystem.getPluginInstance().getResource(resourceName);
                 OutputStream output = new FileOutputStream(file)) {
                file.createNewFile();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
