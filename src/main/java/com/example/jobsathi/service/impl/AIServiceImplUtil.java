package com.example.jobsathi.service.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by Rabindra Adhikari on 3/22/26
 */
public class AIServiceImplUtil {
    private AIServiceImplUtil() {
    }

    private static final String UPLOAD_DIR="src/main/resources/resume";

    public static String saveFileToDir(String originalFileName, MultipartFile pdfFile) throws IOException {

        // 1. Generate unique filename
        String storedFileName = UUID.randomUUID() + "_" + originalFileName;

        // 2. Create directory if not exists
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3. Resolve full path
        Path filePath = uploadPath.resolve(storedFileName);

        // 4. Save file
        Files.copy(pdfFile.getInputStream(), filePath);

        return filePath.toString(); // return path to save in DB
    }
}
