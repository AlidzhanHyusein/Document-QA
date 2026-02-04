package com.docqa.docqa.controller;

import com.docqa.docqa.dto.request.DocumentUploadRequest;
import com.docqa.docqa.dto.response.DocumentResponse;
import com.docqa.docqa.dto.response.DocumentUploadResponse;
import com.docqa.docqa.exception.FileProcessingException;
import com.docqa.docqa.service.DocumentService;
import com.docqa.docqa.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/documents")
@Tag(name = "Document Manager",description = "Api for managing documents")
public class DocumentController {

    private final DocumentService documentService;
    private final FileService fileService;

    public DocumentController(DocumentService documentService, FileService fileService){
        this.documentService = documentService;
        this.fileService = fileService;
    }


    @PostMapping("/upload")
    @Operation(summary = "Upload a new document", description = "Upload document content with metadata")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {

        if(file.isEmpty()){
            throw new FileProcessingException("File cannot be empty");
        }


        String filename = file.getOriginalFilename();

        if (filename == null || filename.isEmpty()) {
            throw new FileProcessingException("Invalid filename");
        }

        String fileType;

        if (filename.toLowerCase().endsWith(".pdf")) {
            fileType = "PDF";
        } else if (filename.toLowerCase().endsWith(".txt")) {
            fileType = "TXT";
        } else {
            throw new FileProcessingException("Only PDF and TXT files are supported");
        }

        String content = fileService.extractText(file);


        DocumentUploadRequest documentUploadRequest = DocumentUploadRequest.builder()
                .filename(filename)
                .fileType(fileType)
                .fileSizeBytes(file.getSize())
                .content(content)
                .build();

        DocumentUploadResponse response = documentService.uploadDocument(documentUploadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Getting Document By Id", description = "Input Document id and receive it")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable Long id){

        return ResponseEntity.ok(documentService.getDocumentById(id));
    }
    @Operation(summary = "Get All Documents", description = "Get all documents with one click")
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments(){

        List<DocumentResponse> documents = documentService.getAllDocuments();

        return new ResponseEntity<>(documents,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document by id", description = "Input document id and delete it")

    public ResponseEntity<String> deleteDocument(@PathVariable Long id){
        String message = documentService.deleteDocument(id);
        return ResponseEntity.ok(message);
    }
}
