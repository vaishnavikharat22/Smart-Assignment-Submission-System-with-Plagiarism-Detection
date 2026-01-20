package com.plagiarism.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class FileTextExtractor {

    public String extractTextFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        String fileName = file.getName().toLowerCase();

        try {
            if (fileName.endsWith(".pdf")) {
                return extractTextFromPdf(filePath);
            } else if (fileName.endsWith(".txt")) {
                return extractTextFromTxt(filePath);
            } else if (fileName.endsWith(".docx")) {
                // For DOCX, we can use Apache POI in the future
                return extractTextFromTxt(filePath);
            } else {
                throw new IllegalArgumentException("Unsupported file format: " + fileName);
            }
        } catch (Exception e) {
            log.error("Error extracting text from file: " + filePath, e);
            throw new RuntimeException("Failed to extract text from file", e);
        }
    }

    private String extractTextFromPdf(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromTxt(String filePath) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
    }
}
