package com.docqa.docqa.service.impl;

import com.docqa.docqa.dto.request.QuestionRequest;
import com.docqa.docqa.dto.response.QuestionHistoryResponse;
import com.docqa.docqa.dto.response.QuestionResponse;
import com.docqa.docqa.entity.Document;
import com.docqa.docqa.entity.QuestionHistory;
import com.docqa.docqa.exception.DocumentNotFoundException;
import com.docqa.docqa.repository.DocumentRepository;
import com.docqa.docqa.repository.QuestionHistoryRepository;
import com.docqa.docqa.service.AIService;
import com.docqa.docqa.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final DocumentRepository documentRepository;
    private final AIService aiService;
    private final QuestionHistoryRepository questionRepository;

    public QuestionServiceImpl(DocumentRepository documentRepository, AIService aiService, QuestionHistoryRepository questionRepository) {
        this.documentRepository = documentRepository;
        this.aiService = aiService;
        this.questionRepository = questionRepository;
    }

    @Override
    public QuestionResponse askQuestion(QuestionRequest request) {
        Document document = documentRepository.findById(request.getDocumentId()).orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        String content = document.getContent();

        long startTime = System.currentTimeMillis();
        String answer = aiService.generateAnswer(content,request.getQuestion());
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        QuestionHistory history = QuestionHistory.builder()
                .document(document)
                .question(request.getQuestion())
                .answer(answer)
                .responseTimeMs(responseTime)
                .build();

        questionRepository.save(history);


        QuestionResponse response = QuestionResponse.builder()
                .documentId(document.getId())
                .filename(document.getFilename())
                 .question(request.getQuestion())
                .answer(answer)
                .source("AI-Generated")
                .build();

        return response;
    }

    @Override
    public List<QuestionHistoryResponse> getAllHistory() {
        List<QuestionHistory> documents = questionRepository.findAll();

        return documents.stream().map(this::toQuestionHistory).collect(Collectors.toList());
    }

    @Override
    public List<QuestionHistoryResponse> getHistoryByDocumentId(Long documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(() -> new DocumentNotFoundException("Document with id " + documentId + " does not exist"));
        List<QuestionHistory> history = questionRepository.findByDocument_Id(documentId);

        return history.stream().map(this::toQuestionHistory).collect(Collectors.toList());
    }


    public QuestionHistoryResponse toQuestionHistory(QuestionHistory questionHistory){

        return QuestionHistoryResponse.builder()
                .id(questionHistory.getId())
                .documentId(questionHistory.getDocument().getId())
                .documentFilename(questionHistory.getDocument().getFilename())
                .question(questionHistory.getQuestion())
                .answer(questionHistory.getAnswer())
                .askedAt(questionHistory.getAskedAt())
                .responseTimeMs(questionHistory.getResponseTimeMs())
                .build();

    }
}
