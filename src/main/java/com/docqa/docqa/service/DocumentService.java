package com.docqa.docqa.service;

import com.docqa.docqa.dto.request.DocumentUploadRequest;
import com.docqa.docqa.dto.response.DocumentResponse;
import com.docqa.docqa.dto.response.DocumentUploadResponse;

import java.util.List;

public interface DocumentService {
    DocumentUploadResponse uploadDocument(DocumentUploadRequest documentUploadRequest);
    DocumentResponse getDocumentById(Long id);
    List<DocumentResponse> getAllDocuments();
    String deleteDocument (Long documentId);
}
