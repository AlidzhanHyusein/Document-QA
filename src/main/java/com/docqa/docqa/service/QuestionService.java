package com.docqa.docqa.service;

import com.docqa.docqa.dto.request.QuestionRequest;
import com.docqa.docqa.dto.response.QuestionHistoryResponse;
import com.docqa.docqa.dto.response.QuestionResponse;

import java.util.List;

public interface QuestionService {
    QuestionResponse askQuestion(QuestionRequest request);
    List<QuestionHistoryResponse> getAllHistory();
    List<QuestionHistoryResponse> getHistoryByDocumentId(Long documentId);
}
