package com.docqa.docqa.service.impl;

import com.docqa.docqa.dto.request.DocumentUploadRequest;
import com.docqa.docqa.dto.response.DocumentResponse;
import com.docqa.docqa.dto.response.DocumentUploadResponse;
import com.docqa.docqa.entity.Document;
import com.docqa.docqa.exception.DocumentNotFoundException;
import com.docqa.docqa.exception.FileProcessingException;
import com.docqa.docqa.repository.DocumentRepository;
import com.docqa.docqa.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository repo;

    public DocumentServiceImpl(DocumentRepository documentRepository){
        this.repo = documentRepository;
    }

    @Override
    public DocumentUploadResponse uploadDocument(DocumentUploadRequest documentUploadRequest) {
        if(repo.existsByFilename(documentUploadRequest.getFilename())){
            throw new FileProcessingException("Document with filename '" +
                    documentUploadRequest.getFilename() + "' already exists");
        }

        Document document = toEntity(documentUploadRequest);
        repo.save(document);
        return toUploadDocument(document);
    }

    @Override
    public DocumentResponse getDocumentById(Long id) {
        Document document = repo.findById(id).orElseThrow(() -> new DocumentNotFoundException("Document with this id does not exist"));

        return toDocumentResponse(document);
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {
        return repo.findAll().stream()
                .map(this::toDocumentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteDocument(Long documentId) {
        Document document = repo.findById(documentId).orElseThrow(() -> new DocumentNotFoundException(documentId));
        repo.deleteById(document.getId());
        return "Document " + document.getFilename() + " deleted successfully";
    }

    private DocumentUploadResponse toUploadDocument(Document document){
        return DocumentUploadResponse.builder()
                .documentId(document.getId())
                .filename(document.getFilename())
                .fileType(document.getFileType())
                .fileSizeBytes(document.getFileSizeBytes())
                .uploadedAt(document.getUploadedAt())
                .message("Document uploaded successfully")
                .build();
    }


    private DocumentResponse toDocumentResponse(Document document){
        return DocumentResponse.builder()
                .documentId(document.getId())
                .filename(document.getFilename())
                .fileType(document.getFileType())
                .fileSizeBytes(document.getFileSizeBytes())
                .uploadedAt(document.getUploadedAt())
                .build();

    }


    private Document toEntity(DocumentUploadRequest documentUploadRequest){
        Document entity = Document.builder()
                .filename(documentUploadRequest.getFilename())
                .fileType(documentUploadRequest.getFileType())
                .fileSizeBytes(documentUploadRequest.getFileSizeBytes())
                .content(documentUploadRequest.getContent())
                .build();

        return entity;
    }
}
