package com.docqa.docqa.service.impl;

import com.docqa.docqa.dto.request.DocumentUploadRequest;
import com.docqa.docqa.dto.response.DocumentResponse;
import com.docqa.docqa.dto.response.DocumentUploadResponse;
import com.docqa.docqa.entity.Document;
import com.docqa.docqa.entity.User;
import com.docqa.docqa.exception.DocumentNotFoundException;
import com.docqa.docqa.exception.FileProcessingException;
import com.docqa.docqa.repository.DocumentRepository;
import com.docqa.docqa.security.SecurityUtil;
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
    private final SecurityUtil securityUtil;

    public DocumentServiceImpl(DocumentRepository documentRepository, SecurityUtil securityUtil){
        this.repo = documentRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public DocumentUploadResponse uploadDocument(DocumentUploadRequest documentUploadRequest) {

        User currentUser = securityUtil.getCurrentUser();

        if(repo.existsByFilenameAndUser(documentUploadRequest.getFilename(),currentUser)){
            throw new FileProcessingException("Document with filename '" +
                    documentUploadRequest.getFilename() + "' already exists");
        }

        Document document = toEntity(documentUploadRequest);
        document.setUser(currentUser);

        repo.save(document);
        return toUploadDocument(document);
    }

    @Override
    public DocumentResponse getDocumentById(Long id) {
        User currentUser = securityUtil.getCurrentUser();

        Document document = repo.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));

        if (!document.getUser().getId().equals(currentUser.getId())) {
            throw new FileProcessingException("Access denied: Document does not belong to you");
        }

        return toDocumentResponse(document);
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {
        User currentUser = securityUtil.getCurrentUser();

        return repo.findByUser(currentUser).stream()
                .map(this::toDocumentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteDocument(Long documentId) {
        System.out.println("=== DELETE DOCUMENT ===");
        System.out.println("Document ID: " + documentId);

        User currentUser = securityUtil.getCurrentUser();
        System.out.println("Current User: " + currentUser.getUsername());

        Document document = repo.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));
        System.out.println("Document found: " + document.getFilename());
        System.out.println("Document user: " + (document.getUser() != null ? document.getUser().getUsername() : "NULL"));

        // Check if document has a user
        if (document.getUser() == null) {
            System.out.println("WARNING: Document has no user assigned!");
            repo.deleteById(document.getId());
            return "Document " + document.getFilename() + " deleted successfully";
        }

        // Security check
        if (!document.getUser().getId().equals(currentUser.getId())) {
            throw new FileProcessingException("Access denied: Cannot delete another user's document");
        }

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
