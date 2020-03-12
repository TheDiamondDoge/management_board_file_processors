package com.iksanov.excellProcessors.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

@Component
public class Utils {
    @Value("${tmp.file.storage}")
    private String STORAGE_DIRECTORY;

    public String saveFile(MultipartFile file, String prefix) throws IOException {
        byte[] bytes = file.getBytes();
        long timestamp = new Date().getTime();
        String filename = Objects.requireNonNull(file.getOriginalFilename(), "File name should not be null");
        Path path = Paths.get(STORAGE_DIRECTORY + prefix + timestamp + "." + this.getFileFormat(filename));
        Files.write(path, bytes);

        return path.toString();
    }

    public String getFileFormat(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return filename.substring(lastDotIndex + 1);
    }
}
