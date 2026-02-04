package com.docqa.docqa.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String extractText(MultipartFile file) throws IOException;
}
