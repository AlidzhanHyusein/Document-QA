package com.docqa.docqa.service.impl;

import com.docqa.docqa.exception.FileProcessingException;
import com.docqa.docqa.service.FileService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
public class FileServiceImpl implements FileService {
    @Override
    public String extractText(MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();

        if (file.isEmpty() || filename == null) {
            throw new FileProcessingException("File is empty");
        }

        if(filename.toLowerCase().endsWith(".pdf")){
            return extractFromPDF(file);
        } else if(filename.toLowerCase().endsWith(".txt")){
            return extractFromTXT(file);
        }else {
            throw new FileProcessingException("Unsupported file type. Only PDF and TXT allowed.");
        }
    }



    private String extractFromPDF(MultipartFile file) throws IOException{
        try (PDDocument document = Loader.loadPDF(file.getBytes())){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e){
            throw new FileProcessingException("Failed to extract text from PDF: " + e.getMessage());
        }
    }

    private String extractFromTXT(MultipartFile file) throws IOException{
        try{
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        }catch (IOException e){
            throw new FileProcessingException("Failed to read text file: " + e.getMessage());

        }
    }
}
