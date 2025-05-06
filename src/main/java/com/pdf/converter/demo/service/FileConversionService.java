package com.pdf.converter.demo.service;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileConversionService {

    private final DocumentConverter converter;

    public FileConversionService(DocumentConverter converter) {
        this.converter = converter;
    }

    public byte[] convertToPdf(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            File tempInputFile = File.createTempFile("input", getFileExtension(file));
            File tempOutputFile = File.createTempFile("output", ".pdf");

            file.transferTo(tempInputFile);

            converter.convert(tempInputFile).to(tempOutputFile).execute();

            try (InputStream pdfInputStream = new FileInputStream(tempOutputFile)) {
                pdfInputStream.transferTo(outputStream);
            }

            tempInputFile.delete();
            tempOutputFile.delete();

            return outputStream.toByteArray();

        } catch (IOException | OfficeException e) {
            throw new RuntimeException("Error en la conversión de archivo", e);
        }
    }

    private String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";
    }
}
