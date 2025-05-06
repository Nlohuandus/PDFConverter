package com.pdf.converter.demo.controller;

import com.pdf.converter.demo.service.FileConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileConversionService fileConversionService;

    public FileController(FileConversionService fileConversionService) {
        this.fileConversionService = fileConversionService;
    }

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertToPdf(@RequestParam("file") MultipartFile file) {
        byte[] pdfBytes = fileConversionService.convertToPdf(file);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.pdf").contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
    }

}