package com.example.jobsathi.service.impl;

import com.example.jobsathi.exception.ResumeException;
import com.example.jobsathi.util.DocType;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@Slf4j
public class DocumentExtractServiceUtil{
    private static long MAX_FILE_SIZE = 10 * 1024 * 1024L;

    public static String extractText(MultipartFile file, String extension) {
        LOGGER.info("extractText::Started Extracting file");
        validateFile(file);
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            throw new ResumeException("Filename is missing");
        }
        return switch (DocType.fromExtension(extension)) {
            case PDF -> extractPDF(file);
            case DOCX -> extractDoc(file);
        };

    }

    private static String extractPDF(MultipartFile file) {
        LOGGER.info("Started extracting Pdf");
        try {
            PDDocument pdf = Loader.loadPDF(file.getBytes());
            if (pdf.isEncrypted()) {
                throw new ResumeException("Pdf is password-protected. Please upload an unencrypted PDF");
            }
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setAddMoreFormatting(true);
            String text = stripper.getText(pdf);
            return normalise(text);
        } catch (ResumeException e) {
            throw e;
        } catch (Exception e) {
            throw new ResumeException("Failed to read PDF: " + e.getMessage());
        }
    }

    private static String extractDoc(MultipartFile file) {
        return null;
    }


    public static String getExtension(String originalFilename) {
        int dot = originalFilename.lastIndexOf(".");
        return dot == -1 ? "" : originalFilename.substring(dot + 1);
    }

    public static void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResumeException("File is empty or missing");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResumeException("File exceeds 10MB limit");
        }
    }
    private static String normalise(String raw) {
        return raw
                .replace("\r\n", "\n")
                .replaceAll("\n{3,}", "\n\n")
                .replaceAll("[ \t]+", " ")
                .trim();
    }
}
